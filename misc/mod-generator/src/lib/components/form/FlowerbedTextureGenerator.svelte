<script lang="ts">
  import { getAbortSignal } from "svelte";
  import bg from "../../../assets/generator/bg.png";
  import defaultFlowers from "../../../assets/generator/default-flowers.png";
  import grassOnly from "../../../assets/generator/grass-only.png";
  import stems from "../../../assets/generator/stems.png";
  import title from "../../../assets/generator/title-only.png";
  import { clusterColors, delay, packRgb, toHsl, unpackRgb } from "../../util";
  import StyledColorPicker from "../color-picker/StyledColorPicker.svelte";
  import Image from "../icons/Image.svelte";
  import ImageUpload from "../icons/ImageUpload.svelte";
  import Progress from "../icons/Progress.svelte";
  import type { TextureCreate } from "../../types/state";

  const TEXTURE_SIZE = 16;

  type ColorPositionsMap = Record<
    TextureCreate["template"],
    Record<`color${1 | 2 | 3 | 4 | 5}`, [number, number][]>
  >;

  const COLOR_POSITIONS: ColorPositionsMap = {
    tiny_flowers: {
      color1: [
        [13, 1],
        [1, 4],
        [10, 6],
        [9, 8],
        [14, 9],
        [13, 10],
        [3, 11],
        [4, 13],
        [13, 14],
      ],
      color2: [
        [3, 1],
        [5, 1],
        [14, 2],
        [0, 5],
        [2, 5],
        [6, 5],
        [9, 5],
        [5, 6],
        [6, 9],
        [8, 9],
        [2, 10],
        [5, 10],
        [9, 10],
        [14, 11],
        [11, 12],
        [13, 13],
        [3, 14],
        [10, 14],
        [11, 15],
      ],
      color3: [
        [4, 0],
        [4, 2],
        [12, 2],
        [13, 3],
        [10, 4],
        [11, 5],
        [1, 6],
        [7, 6],
        [6, 7],
        [5, 8],
        [4, 9],
        [10, 9],
        [15, 10],
        [1, 11],
        [2, 12],
        [12, 12],
        [10, 13],
        [5, 14],
        [4, 15],
        [12, 15],
      ],
      color4: [
        [1, 5],
        [6, 6],

        [9, 9],
        [14, 10],
        [11, 13],
        [4, 14],
        [12, 14],
      ],
      color5: [
        [4, 1],
        [13, 2],
        [10, 5],
        [5, 9],
        [2, 11],
        [12, 13],
        [11, 14],
      ],
    },
    pink_petals: {
      color1: [
        [4, 0],
        [3, 1],
        [5, 1],
        [4, 2],
        [3, 8],
        [4, 8],
        [5, 8],
        [4, 9],
        [14, 9],
        [1, 10],
        [7, 10],
        [13, 10],
        [15, 10],
        [1, 11],
        [2, 11],
        [6, 11],
        [7, 11],
        [14, 11],
        [1, 12],
        [7, 12],
        [11, 12],
        [12, 12],
        [4, 13],
        [10, 13],
        [3, 14],
        [4, 14],
        [5, 14],
        [10, 14],
      ],
      color2: [
        [10, 0],
        [11, 0],
        [12, 0],
        [4, 1],
        [11, 1],
        [8, 2],
        [14, 2],
        [8, 3],
        [9, 3],
        [13, 3],
        [14, 3],
        [8, 4],
        [14, 4],
        [11, 5],
        [10, 6],
        [11, 6],
        [12, 6],
        [9, 8],
        [3, 9],
        [5, 9],
        [8, 9],
        [10, 9],
        [2, 10],
        [4, 10],
        [6, 10],
        [9, 10],
        [14, 10],
        [3, 11],
        [5, 11],
        [5, 12],
        [2, 12],
        [4, 12],
        [6, 12],
        [3, 13],
        [5, 13],
        [13, 13],
        [13, 14],
        [11, 15],
        [12, 15],
      ],
      color3: [
        [10, 1],
        [12, 1],
        [9, 2],
        [11, 2],
        [13, 2],
        [10, 3],
        [12, 3],
        [1, 4],
        [9, 4],
        [11, 4],
        [13, 4],
        [0, 5],
        [2, 5],
        [10, 5],
        [12, 5],
        [1, 6],
        [9, 9],
        [11, 13],
        [12, 14],
      ],
      color4: [
        [10, 2],
        [12, 2],
        [10, 4],
        [12, 4],
        [1, 5],
        [6, 5],
        [5, 6],
        [7, 6],
        [6, 7],
        [3, 10],
        [5, 10],
        [3, 12],
        [5, 12],
        [12, 13],
        [11, 14],
      ],
      color5: [
        [11, 3],
        [6, 6],
        [4, 11],
      ],
    },
  };

  interface Props {
    template: TextureCreate["template"];
    onGenerate?: (file: File) => void;
    itemTexture?: File;
  }

  let { template, onGenerate, itemTexture }: Props = $props();

  let color1 = $state("#E0E0E0");
  let color2 = $state("#D0D0D0");
  let color3 = $state("#C6C6C6");
  let color4 = $state("#A8A8A8");
  let color5 = $state("#7B7B7B");

  $effect(() => {
    const signal = getAbortSignal();

    const color1Snapshot = $state.snapshot(color1);
    const color2Snapshot = $state.snapshot(color2);
    const color3Snapshot = $state.snapshot(color3);
    const color4Snapshot = $state.snapshot(color4);
    const color5Snapshot = $state.snapshot(color5);

    if (!COLOR_POSITIONS[template]) {
      return;
    }

    delay(100, signal).then(
      async () => {
        const loadingDelay = delay(150, signal);

        const canvas = new OffscreenCanvas(TEXTURE_SIZE, TEXTURE_SIZE);
        const ctx = canvas.getContext("2d");
        if (!ctx) {
          throw new Error("Unable to get canvas context");
        }

        const colorPositions = [
          [color1Snapshot, COLOR_POSITIONS[template].color1],
          [color2Snapshot, COLOR_POSITIONS[template].color2],
          [color3Snapshot, COLOR_POSITIONS[template].color3],
          [color4Snapshot, COLOR_POSITIONS[template].color4],
          [color5Snapshot, COLOR_POSITIONS[template].color5],
        ] as const;

        for (const [color, positions] of colorPositions) {
          ctx.fillStyle = color;
          for (const [x, y] of positions) {
            ctx.fillRect(x, y, 1, 1);
          }
        }

        const png = await canvas.convertToBlob({ type: "image/png" });
        const file = new File([png], "icon.png", { type: "image/png" });

        loadingDelay.then(
          () => {
            if (!signal.aborted) {
              onGenerate?.(file);
            }
          },
          () => {},
        );
      },
      () => {},
    );
  });

  let swatches = $state<string[]>([]);

  $effect(() => {
    const signal = getAbortSignal();

    if (!itemTexture) {
      return;
    }

    delay(100, signal).then(
      async () => {
        const loadingDelay = delay(150, signal);

        const itemTextureBitmap = await window.createImageBitmap(itemTexture);

        const canvas = new OffscreenCanvas(
          itemTextureBitmap.width,
          itemTextureBitmap.height,
        );
        const ctx = canvas.getContext("2d");
        if (!ctx) {
          throw new Error("Unable to get canvas context");
        }

        ctx.drawImage(
          itemTextureBitmap,
          0,
          0,
          itemTextureBitmap.width,
          itemTextureBitmap.height,
        );

        const imageData = ctx.getImageData(
          0,
          0,
          itemTextureBitmap.width,
          itemTextureBitmap.height,
          { colorSpace: "srgb" },
        );

        const colorSet = new Set<number>();
        for (let row = 0; row < itemTextureBitmap.height; row++) {
          for (let column = 0; column < itemTextureBitmap.width; column++) {
            const startIndex = (row * itemTextureBitmap.width + column) * 4;
            const r = imageData.data[startIndex];
            const g = imageData.data[startIndex + 1];
            const b = imageData.data[startIndex + 2];
            const a = imageData.data[startIndex + 3];

            if (a === 0) {
              // Pixel is transparent, skip.
              continue;
            }

            const hsl = toHsl(r, g, b);
            if (hsl.h > 75 && hsl.h < 155 && hsl.s > 0.4) {
              // Pixel is a vibrant green, skip.
              continue;
            }

            colorSet.add(packRgb(r, g, b));
          }
        }

        // If there are too many colours, try and cluster them. This is done by lowering the
        // threshold iteratively until there are at least 7 colours. It may turn out this is
        // never the case, in which case we just take whatever is last.
        const colorList = Array.from(colorSet);
        let finalColors = colorList;
        if (finalColors.length > 6) {
          let threshold = 24;

          finalColors = clusterColors(colorList, threshold);

          while (finalColors.length < 7 && threshold >= 4) {
            threshold -= 2;
            finalColors = clusterColors(colorList, threshold);
          }
        }

        const rgbStrings = finalColors.map(
          (packed) => `#${packed.toString(16).padStart(6, "0")}`,
        );

        loadingDelay.then(
          () => {
            if (!signal.aborted) {
              swatches = Array.from(rgbStrings);
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
  <div class="inline-group color-picker-list">
    <StyledColorPicker bind:color={color1} {swatches} />
    <StyledColorPicker bind:color={color2} {swatches} />
    <StyledColorPicker bind:color={color3} {swatches} />
    <StyledColorPicker bind:color={color4} {swatches} />
    <StyledColorPicker bind:color={color5} {swatches} />
  </div>
{/if}

<style>
  .color-picker-list {
    gap: 0.25rem;
  }
</style>
