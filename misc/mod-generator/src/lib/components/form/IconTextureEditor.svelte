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

  // Reset the tool to pencil when changing the colour
  $effect(() => {
    color;
    tool = "pencil";
  });

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

<div class="input-group">
  <div class="editor-grid">
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
    <div class="editor-tools">
      <button
        class={[
          "button icon-button",
          tool === "pencil" ? "color-dynamic" : "color-disabled",
        ]}
        type="button"
        onclick={() => (tool = "pencil")}
        style="--dynamic-color: {color}"
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
  </div>
</div>

<style>
  .editor-grid {
    display: grid;
    grid-template-areas: "tools" "color" "canvas";
    gap: 0.5rem;
  }

  .editor-tools {
    grid-area: tools;

    display: flex;
    gap: 0.5rem;
  }
  .editor-color {
    grid-area: color;
  }
  .editor-canvas {
    grid-area: canvas;

    display: flex;
    gap: 0.5rem;
    align-items: center;
    justify-content: center;
  }

  @media (min-width: 560px) {
    .editor-grid {
      grid-template-columns: auto 1fr;
      grid-template-areas:
        "tools canvas"
        "color canvas";
    }
  }

  .canvas {
    background:
      linear-gradient(45deg, #eee 25%, #0000 25%, #0000 75%, #eee 75%) 0 0 /
        10px 10px,
      linear-gradient(45deg, #eee 25%, #0000 25%, #0000 75%, #eee 75%) 5px 5px /
        10px 10px,
      #fff;
    box-shadow: 0 0 10px #0004;
    width: 192px;
    height: 192px;
    image-rendering: pixelated;
    touch-action: none;
    cursor: crosshair;
  }
</style>
