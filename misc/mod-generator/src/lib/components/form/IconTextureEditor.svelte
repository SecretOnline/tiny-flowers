<script lang="ts">
  import ColorPicker from "svelte-awesome-color-picker";
  import Edit from "../icons/Edit.svelte";
  import InkEraser from "../icons/InkEraser.svelte";
  import ImagePreview from "../ImagePreview.svelte";
  import { getAbortSignal } from "svelte";

  interface Props {
    original?: File;
    value?: File;
    swatches?: string[];
  }

  const CANVAS_RESOLUTION = 16;

  type Tool = "pencil" | "eraser";

  let { swatches, value = $bindable(), original }: Props = $props();

  // Purposefully only get the first satch on load
  // svelte-ignore state_referenced_locally
  let color = $state(swatches?.[0] ?? "#4CAA47");
  let tool = $state<Tool>("pencil");

  let canvasEl = $state<HTMLCanvasElement>();
  let ctx: CanvasRenderingContext2D | null = null;
  let lastFile: File | undefined;
  let lastPixel: { x: number; y: number } | null = null;
  let isDrawing = false;
  let isCommitting = false;
  let pendingCommit = false;

  $effect(() => {
    const signal = getAbortSignal();

    if (!canvasEl) {
      return;
    }
    if (!ctx) {
      ctx = canvasEl.getContext("2d");
      if (!ctx) {
        console.error("IconTextureEditor: unable to acquire 2d canvas context");
        return;
      }
      ctx.imageSmoothingEnabled = false;
    }

    const currentValue = value;
    if (currentValue === lastFile) {
      return;
    }

    if (!currentValue) {
      ctx.clearRect(0, 0, 16, 16);
      lastPixel = null;
      return;
    }

    const fileToLoad = currentValue;
    window
      .createImageBitmap(fileToLoad)
      .then((bitmap) => {
        if (!ctx || value !== fileToLoad || signal.aborted) {
          return;
        }
        ctx.clearRect(0, 0, 16, 16);
        ctx.drawImage(bitmap, 0, 0, 16, 16);
      })
      .catch(() => {});

    return () => {
      ctx = null;
    };
  });

  function pixelFromEvent(event: PointerEvent): { x: number; y: number } {
    const rect = canvasEl!.getBoundingClientRect();
    const x = Math.floor(
      ((event.clientX - rect.left) / rect.width) * CANVAS_RESOLUTION,
    );
    const y = Math.floor(
      ((event.clientY - rect.top) / rect.height) * CANVAS_RESOLUTION,
    );
    return {
      x: Math.min(CANVAS_RESOLUTION - 1, Math.max(0, x)),
      y: Math.min(CANVAS_RESOLUTION - 1, Math.max(0, y)),
    };
  }

  function paintPixel(x: number, y: number) {
    if (!ctx) {
      return;
    }
    if (tool === "pencil") {
      ctx.fillStyle = color;
      ctx.fillRect(x, y, 1, 1);
    } else if (tool === "eraser") {
      ctx.clearRect(x, y, 1, 1);
    }
  }

  function commit() {
    if (!canvasEl) {
      return;
    }
    if (isCommitting) {
      pendingCommit = true;
      return;
    }

    isCommitting = true;
    canvasEl.toBlob((blob) => {
      isCommitting = false;
      if (!blob) {
        if (pendingCommit) {
          pendingCommit = false;
          commit();
        }
        return;
      }
      const file = new File([blob], "item.png", { type: "image/png" });
      lastFile = file;
      value = file;
      if (pendingCommit) {
        pendingCommit = false;
        commit();
      }
    }, "image/png");
  }

  function handlePointerDown(event: PointerEvent) {
    if (!ctx) {
      return;
    }
    event.preventDefault();
    canvasEl!.setPointerCapture(event.pointerId);
    isDrawing = true;
    const p = pixelFromEvent(event);
    lastPixel = p;
    paintPixel(p.x, p.y);
    commit();
  }

  function handlePointerMove(event: PointerEvent) {
    if (!isDrawing || !ctx) {
      return;
    }
    const p = pixelFromEvent(event);
    if (lastPixel && p.x === lastPixel.x && p.y === lastPixel.y) {
      return;
    }
    lastPixel = p;
    paintPixel(p.x, p.y);
    commit();
  }

  function handlePointerUp(event: PointerEvent) {
    if (!isDrawing) {
      return;
    }
    isDrawing = false;
    lastPixel = null;
    try {
      canvasEl!.releasePointerCapture(event.pointerId);
    } catch {}
  }
</script>

<div class="editor-section">
  <div class="editor-tools">
    <button
      class={["button icon-button", tool !== "pencil" && "color-disabled"]}
      type="button"
      onclick={() => (tool = "pencil")}
    >
      <Edit />
    </button>
    <button
      class={["button icon-button", tool !== "eraser" && "color-disabled"]}
      type="button"
      onclick={() => (tool = "eraser")}
    >
      <InkEraser />
    </button>
  </div>
  <div class="editor-color">
    <ColorPicker
      bind:hex={color}
      position="responsive"
      {swatches}
      isDialog={false}
    />
  </div>
  <div class="editor-canvas">
    <canvas
      bind:this={canvasEl}
      class="canvas"
      width={CANVAS_RESOLUTION}
      height={CANVAS_RESOLUTION}
      onpointerdown={handlePointerDown}
      onpointermove={handlePointerMove}
      onpointerup={handlePointerUp}
      onpointercancel={handlePointerUp}
    ></canvas>
    {#if original}
      <ImagePreview file={original} alt="Original" --preview-size="128px" />
    {/if}
  </div>
</div>

<style>
  .editor-section {
    display: grid;
    grid-template-areas: "tools" "color" "canvas";
  }

  .editor-tools {
    grid-area: tools;

    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  .editor-color {
    grid-area: color;
  }
  .editor-canvas {
    grid-area: canvas;
  }

  @media (min-width: 560px) {
    .editor-section {
      grid-template-columns: repeat(2, minmax(0, 1fr));
      grid-template-areas:
        "tools color"
        "canvas canvas";
    }
  }

  @media (min-width: 1072px) {
    .editor-section {
      grid-template-columns: 1fr 1fr 2fr;
      grid-template-areas: "tools color canvas";
    }
  }

  .canvas {
    background:
      linear-gradient(45deg, #eee 25%, #0000 25%, #0000 75%, #eee 75%) 0 0 /
        10px 10px,
      linear-gradient(45deg, #eee 25%, #0000 25%, #0000 75%, #eee 75%) 5px 5px /
        10px 10px,
      #fff;
    width: 128px;
    height: 128px;
    image-rendering: pixelated;
    touch-action: none;
    cursor: crosshair;
  }
</style>
