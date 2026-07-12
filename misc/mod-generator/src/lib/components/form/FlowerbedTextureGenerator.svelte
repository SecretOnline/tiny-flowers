<script lang="ts">
  import { getAbortSignal } from "svelte";
  import type { TextureCreate } from "../../types/state";
  import { delay } from "../../util";
  import StyledColorPicker from "../color-picker/StyledColorPicker.svelte";

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
    swatches?: string[];
    onGenerate?: (file: File) => void;
  }

  let { template, onGenerate, swatches }: Props = $props();

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
