<script lang="ts">
  import ColorPicker from "svelte-awesome-color-picker";
  import Edit from "../icons/Edit.svelte";
  import InkEraser from "../icons/InkEraser.svelte";
  import ImagePreview from "../ImagePreview.svelte";

  interface Props {
    original?: File;
    value?: File;
    swatches?: string[];
  }

  type Tool = "pencil" | "eraser";

  let { swatches, value = $bindable(), original }: Props = $props();

  let color = $state("#4CAA47");
  let tool = $state<Tool>("pencil");
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
    <canvas class="canvas"></canvas>
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
    width: 128px;
    height: 128px;
  }
</style>
