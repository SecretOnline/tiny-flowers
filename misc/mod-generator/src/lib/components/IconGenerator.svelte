<script lang="ts">
  import { getAbortSignal } from "svelte";
  import ColorPicker from "svelte-awesome-color-picker";
  import bg from "../../assets/generator/bg.png";
  import grassOnly from "../../assets/generator/grass-only.png";
  import defaultFlowers from "../../assets/generator/default-flowers.png";
  import stems from "../../assets/generator/stems.png";
  import title from "../../assets/generator/title-only.png";
  import { delay } from "../util";
  import ColorPickerButton from "./color-picker/ColorPickerButton.svelte";
  import ColorPickerWrapper from "./color-picker/ColorPickerWrapper.svelte";
  import Image from "./icons/Image.svelte";
  import ImageUpload from "./icons/ImageUpload.svelte";
  import Progress from "./icons/Progress.svelte";

  const ICON_SIZE = 512;
  const OVERLAY_SAFE_START = 0;
  const OVERLAY_SAFE_END = 211;
  const OVERLAY_MARGIN = 12;
  const PX_SIZE = 16;

  const SAFE_START_X = OVERLAY_MARGIN;
  const SAFE_START_Y = OVERLAY_SAFE_START + OVERLAY_MARGIN;
  const SAFE_WIDTH = ICON_SIZE / 2 - SAFE_START_X - OVERLAY_MARGIN;
  const SAFE_HEIGHT =
    OVERLAY_SAFE_END - OVERLAY_SAFE_START - 2 * OVERLAY_MARGIN;
  const SAFE_ASPECT_RATIO = SAFE_WIDTH / SAFE_HEIGHT;

  interface TransformInput {
    px: number;
    py: number;
    degrees: number;
    size: number;
    skew: number;
    rotation: number;
  }

  const FLOWER_TRANSFORM_1: TransformInput = {
    px: 266,
    py: 447,
    degrees: 28.3,
    size: 334,
    skew: 1.04,
    rotation: -0.04,
  };
  const FLOWER_TRANSFORM_2: TransformInput = {
    px: 300,
    py: 461,
    degrees: 27.6,
    size: 298,
    skew: 0.85,
    rotation: -0.12,
  };
  const FLOWER_TRANSFORM_3: TransformInput = {
    px: 284,
    py: 412,
    degrees: 24.9,
    size: 242,
    skew: 0.91,
    rotation: -0.05,
  };
  const FLOWER_TRANSFORM_4: TransformInput = {
    px: 258,
    py: 449,
    degrees: 28.3,
    size: 270,
    skew: 1.06,
    rotation: -0.02,
  };

  interface Props {
    onGenerate?: (file: File) => void;
  }

  let { onGenerate }: Props = $props();

  let image = $state<File>();
  let color = $state("#4CAA47DB");

  let flower1 = $state<File>();
  let flower2 = $state<File>();
  let flower3 = $state<File>();
  let flower4 = $state<File>();

  const bitmapPromiseMap = new WeakMap<File, Promise<ImageBitmap>>();
  function getBitmapForFile(file: File) {
    if (!bitmapPromiseMap.has(file)) {
      bitmapPromiseMap.set(file, window.createImageBitmap(file));
    }

    return bitmapPromiseMap.get(file)!;
  }

  let loadImageBitmapsPromise = Promise.all(
    [bg, grassOnly, stems, title, defaultFlowers].map((url) =>
      fetch(url)
        .then((response) => response.blob())
        .then((blob) => window.createImageBitmap(blob)),
    ),
  ).then(([bg, grassBlock, stems, title, defaultFlowers]) => ({
    bg,
    grassBlock,
    stems,
    title,
    defaultFlowers,
  }));

  function drawFlowerPart(
    ctx: OffscreenCanvasRenderingContext2D,
    bitmap: ImageBitmap,
    index: number,
  ) {
    let transform: TransformInput;
    let insetX: number;
    let insetY: number;

    switch (index) {
      case 1:
        transform = FLOWER_TRANSFORM_1;
        insetX = 0;
        insetY = 0;
        break;
      case 2:
        transform = FLOWER_TRANSFORM_2;
        insetX = 0;
        insetY = 0.5;
        break;
      case 3:
        transform = FLOWER_TRANSFORM_3;
        insetX = 0.5;
        insetY = 0.5;
        break;
      case 4:
        transform = FLOWER_TRANSFORM_4;
        insetX = 0.5;
        insetY = 0;
        break;
      default:
        throw new Error("Invalid flower index");
    }

    const subX = insetX * bitmap.width;
    const subY = insetY * bitmap.height;
    const subWidth = bitmap.width / 2;
    const subHeight = bitmap.height / 2;

    const angle = (transform.degrees * Math.PI) / 180; // 30 degrees
    const cos = Math.cos(angle);
    const sin = Math.sin(angle);

    const scale = transform.size / PX_SIZE;

    // Transform and draw
    ctx.save();
    ctx.imageSmoothingEnabled = false;
    ctx.transform(
      -cos * scale,
      -sin * scale,
      cos * scale * transform.skew,
      -sin * scale * transform.skew,
      transform.px,
      transform.py,
    );
    ctx.rotate(transform.rotation);
    ctx.drawImage(
      bitmap,
      subX,
      subY,
      subWidth,
      subHeight,
      subX,
      subY,
      PX_SIZE / 2,
      PX_SIZE / 2,
    );
    ctx.restore();
  }

  function drawTitlePart(
    ctx: OffscreenCanvasRenderingContext2D,
    bitmap: ImageBitmap,
    isLeft: boolean,
  ) {
    const bitmapAspect = bitmap.width / bitmap.height;

    const drawWidth =
      bitmapAspect > SAFE_ASPECT_RATIO
        ? SAFE_WIDTH
        : SAFE_HEIGHT * bitmapAspect;
    const drawHeight =
      bitmapAspect > SAFE_ASPECT_RATIO
        ? SAFE_WIDTH / bitmapAspect
        : SAFE_HEIGHT;

    const startX =
      SAFE_START_X +
      (ICON_SIZE / 2) * (isLeft ? 0 : 1) +
      (SAFE_WIDTH - drawWidth) / 2;
    const startY = SAFE_START_Y + (SAFE_HEIGHT - drawHeight) / 2;

    if (bitmap.width < drawWidth || bitmap.height < drawHeight) {
      ctx.imageSmoothingEnabled = false;
    }

    ctx.drawImage(bitmap, startX, startY, drawWidth, drawHeight);
  }

  let isLoading = $state(false);
  $effect(() => {
    const signal = getAbortSignal();

    const colorSnapshot = $state.snapshot(color);

    // Force references to the various files that are only accessed asynchronously
    $state.snapshot(image);
    $state.snapshot(flower1);
    $state.snapshot(flower2);
    $state.snapshot(flower3);
    $state.snapshot(flower4);

    if (!image) {
      return;
    }
    const trueImage = image;

    delay(100, signal).then(
      async () => {
        isLoading = true;
        const loadingDelay = delay(150, signal);

        const canvas = new OffscreenCanvas(ICON_SIZE, ICON_SIZE);
        const ctx = canvas.getContext("2d");
        if (!ctx) {
          throw new Error("Unable to get canvas context");
        }

        const imageBitmaps = await loadImageBitmapsPromise;
        const [flowerBitmap1, flowerBitmap2, flowerBitmap3, flowerBitmap4] =
          await Promise.all(
            [flower1, flower2, flower3, flower4].map((file) =>
              file ? getBitmapForFile(file) : imageBitmaps.defaultFlowers,
            ),
          );

        ctx.drawImage(imageBitmaps.bg, 0, 0);
        ctx.drawImage(imageBitmaps.grassBlock, 0, 0);

        ctx.save();
        ctx.fillStyle = colorSnapshot;
        ctx.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
        ctx.restore();

        ctx.save();
        ctx.globalAlpha = 0.42;
        ctx.drawImage(imageBitmaps.grassBlock, 0, 0);
        ctx.restore();

        ctx.drawImage(imageBitmaps.stems, 0, 0);

        drawFlowerPart(ctx, flowerBitmap3, 3);
        drawFlowerPart(ctx, flowerBitmap4, 4);
        drawFlowerPart(ctx, flowerBitmap2, 2);
        drawFlowerPart(ctx, flowerBitmap1, 1);

        const bitmap = await getBitmapForFile(trueImage);
        drawTitlePart(ctx, bitmap, false);
        drawTitlePart(ctx, imageBitmaps.title, true);

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
        accept="image/*"
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
      <label class="file-input-facade button" for="generator-icon">
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
    <div class="inline-group">
      <input
        type="file"
        class="visually-hidden"
        id="generator-flower-1"
        accept="image/*"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (flower1) {
              dt.items.add(flower1);
            }
            return dt.files;
          },
          (newFiles) => {
            flower1 = newFiles?.[0] ?? undefined;
          }
        }
      />
      <input
        type="file"
        class="visually-hidden"
        id="generator-flower-2"
        accept="image/*"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (flower2) {
              dt.items.add(flower2);
            }
            return dt.files;
          },
          (newFiles) => {
            flower2 = newFiles?.[0] ?? undefined;
          }
        }
      />
      <input
        type="file"
        class="visually-hidden"
        id="generator-flower-3"
        accept="image/*"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (flower3) {
              dt.items.add(flower3);
            }
            return dt.files;
          },
          (newFiles) => {
            flower3 = newFiles?.[0] ?? undefined;
          }
        }
      />
      <input
        type="file"
        class="visually-hidden"
        id="generator-flower-4"
        accept="image/*"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (flower4) {
              dt.items.add(flower4);
            }
            return dt.files;
          },
          (newFiles) => {
            flower4 = newFiles?.[0] ?? undefined;
          }
        }
      />
      <span>Flower textures:</span>
      <label class="button icon-button" for="generator-flower-1">
        {#if flower1}
          <Image />
        {:else}
          <ImageUpload />
        {/if}
      </label>
      <label class="button icon-button" for="generator-flower-2">
        {#if flower2}
          <Image />
        {:else}
          <ImageUpload />
        {/if}
      </label>
      <label class="button icon-button" for="generator-flower-3">
        {#if flower3}
          <Image />
        {:else}
          <ImageUpload />
        {/if}
      </label>
      <label class="button icon-button" for="generator-flower-4">
        {#if flower4}
          <Image />
        {:else}
          <ImageUpload />
        {/if}
      </label>
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
