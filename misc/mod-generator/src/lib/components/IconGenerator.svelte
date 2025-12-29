<script lang="ts">
  import { getAbortSignal } from "svelte";
  import ColorPicker from "svelte-awesome-color-picker";
  import bg from "../../assets/generator/bg.png";
  import flowers1 from "../../assets/generator/flowers1.png";
  import flowers2 from "../../assets/generator/flowers2.png";
  import flowers3 from "../../assets/generator/flowers3.png";
  import flowers4 from "../../assets/generator/flowers4.png";
  import grassBlock from "../../assets/generator/grass-t.png";
  import stems from "../../assets/generator/stems.png";
  import title from "../../assets/generator/title.png";
  import { delay } from "../util";
  import ColorPickerButton from "./color-picker/ColorPickerButton.svelte";
  import ColorPickerWrapper from "./color-picker/ColorPickerWrapper.svelte";
  import Image from "./icons/Image.svelte";
  import ImageUpload from "./icons/ImageUpload.svelte";
  import Progress from "./icons/Progress.svelte";

  const ICON_SIZE = 512;
  const OVERLAY_SAFE_START = 211;
  const OVERLAY_MARGIN = 12;

  const SAFE_START_X = OVERLAY_MARGIN;
  const SAFE_START_Y = OVERLAY_SAFE_START + OVERLAY_MARGIN;
  const SAFE_WIDTH = ICON_SIZE - SAFE_START_X - OVERLAY_MARGIN;
  const SAFE_HEIGHT = ICON_SIZE - SAFE_START_Y - OVERLAY_MARGIN;
  const SAFE_ASPECT_RATIO = SAFE_WIDTH / SAFE_HEIGHT;

  interface Props {
    onGenerate?: (file: File) => void;
  }

  let { onGenerate }: Props = $props();

  let image = $state<File>();
  let imageBitmapPromise = $derived.by(() =>
    image ? window.createImageBitmap(image) : undefined,
  );
  let color = $state("#4CAA47DB");

  let loadImageBitmapsPromise =
    $state<
      Promise<
        Record<
          "bg" | `flowers${1 | 2 | 3 | 4}` | "grassBlock" | "stems" | "title",
          ImageBitmap
        >
      >
    >();
  async function loadImageBitmaps(signal: AbortSignal) {
    if (!loadImageBitmapsPromise) {
      const promises = [
        bg,
        flowers1,
        flowers2,
        flowers3,
        flowers4,
        grassBlock,
        stems,
        title,
      ].map((url) =>
        fetch(url, { signal })
          .then((response) => response.blob())
          .then((blob) => window.createImageBitmap(blob)),
      );

      loadImageBitmapsPromise = Promise.all(promises).then(
        ([
          bg,
          flowers1,
          flowers2,
          flowers3,
          flowers4,
          grassBlock,
          stems,
          title,
        ]) => ({
          bg,
          flowers1,
          flowers2,
          flowers3,
          flowers4,
          grassBlock,
          stems,
          title,
        }),
      );

      let didFinish = false;
      loadImageBitmapsPromise.then(() => {
        didFinish = true;
      });

      signal.addEventListener("abort", () => {
        if (!didFinish) {
          loadImageBitmapsPromise = undefined;
        }
      });
    }

    return loadImageBitmapsPromise;
  }

  let isLoading = $state(false);
  $effect(() => {
    const signal = getAbortSignal();

    const imageSnapshot = $state.snapshot(image);
    const colorSnapshot = $state.snapshot(color);

    if (!imageSnapshot) {
      return;
    }
    if (!imageBitmapPromise) {
      console.warn("Had image but no Promise for bitmap");
      return;
    }

    delay(100, signal).then(
      async () => {
        isLoading = true;
        const loadingDelay = delay(150, signal);

        const canvas = new OffscreenCanvas(ICON_SIZE, ICON_SIZE);
        const ctx = canvas.getContext("2d");
        if (!ctx) {
          throw new Error("Unable to get canvas context");
        }

        const imageBitmaps = await loadImageBitmaps(signal);

        ctx.drawImage(imageBitmaps.bg, 0, 0);
        ctx.fillStyle = colorSnapshot;
        ctx.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
        ctx.drawImage(imageBitmaps.grassBlock, 0, 0);
        ctx.drawImage(imageBitmaps.stems, 0, 0);
        ctx.drawImage(imageBitmaps.flowers1, 0, 0);
        ctx.drawImage(imageBitmaps.flowers2, 0, 0);
        ctx.drawImage(imageBitmaps.flowers3, 0, 0);
        ctx.drawImage(imageBitmaps.flowers4, 0, 0);
        ctx.drawImage(imageBitmaps.title, 0, 0);

        const bitmap = await imageBitmapPromise;

        const bitmapAspect = bitmap.width / bitmap.height;

        const drawWidth =
          bitmapAspect > SAFE_ASPECT_RATIO
            ? SAFE_WIDTH
            : SAFE_HEIGHT * bitmapAspect;
        const drawHeight =
          bitmapAspect > SAFE_ASPECT_RATIO
            ? SAFE_WIDTH / bitmapAspect
            : SAFE_HEIGHT;

        const startX = SAFE_START_X + (SAFE_WIDTH - drawWidth) / 2;
        const startY = SAFE_START_Y + (SAFE_HEIGHT - drawHeight) / 2;

        if (bitmap.width < SAFE_WIDTH || bitmap.height < SAFE_HEIGHT) {
          ctx.imageSmoothingEnabled = false;
        }

        ctx.drawImage(bitmap, startX, startY, drawWidth, drawHeight);

        const png = await canvas.convertToBlob({ type: "image/png" });
        const file = new File([png], "icon.png", { type: "image/png" });

        loadingDelay.then(
          () => {
            if (!signal.aborted) {
              isLoading = false;
              onGenerate?.(file);
            }
          },
          () => {},
        );
      },
      () => {},
    );
  });
</script>

{#if typeof window.OffscreenCanvas !== "undefined"}
  <div class="block-group">
    <p>
      You can also create an icon by uploading an image (e.g. your mod's logo)
      and selecting a color.
    </p>
    <div class="inline-group">
      <input
        type="file"
        class="visually-hidden"
        id="generator-icon"
        accept="image/png"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (image) {
              dt.items.add(image);
            }
            return dt.files;
          },
          (newFiles) => {
            image = newFiles?.[0] ?? undefined;
          }
        }
      />
      <label class="file-input-facade button icon-upload" for="generator-icon">
        {#if image}
          {#if isLoading}
            <Progress class="spin" />
          {:else}
            <Image />
          {/if}
          <span class="file-name">{image.name || "Uploaded file"}</span>
        {:else}
          <ImageUpload /><span>Browse...</span>
        {/if}
      </label>
      <div>
        <ColorPicker
          bind:hex={color}
          position="responsive"
          components={{ input: ColorPickerButton, wrapper: ColorPickerWrapper }}
        />
        <div id="color-picker-portal"></div>
      </div>
    </div>
  </div>
{/if}

<style>
  .file-input-facade {
    flex-grow: 1;
    width: unset;
    min-width: 0;
  }

  .file-name {
    text-overflow: ellipsis;
    overflow: hidden;
  }

  #color-picker-portal {
    position: absolute;
  }
</style>
