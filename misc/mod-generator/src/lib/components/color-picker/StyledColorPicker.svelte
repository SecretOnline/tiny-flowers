<script lang="ts">
  import ColorPicker from "svelte-awesome-color-picker";
  import ColorPickerButton from "./ColorPickerButton.svelte";
  import ColorPickerWrapper from "./ColorPickerWrapper.svelte";
  import { setPortalId } from "./context";

  interface Props {
    color: string;
    swatches?: string[];
  }

  let { color = $bindable(), swatches }: Props = $props();

  const uid = $props.id();
  const portalId = `color-picker-${uid}`;

  setPortalId(portalId);
</script>

<div class="styled-color-picker">
  <div id={portalId} class="portal-container"></div>
  <ColorPicker
    bind:hex={color}
    position="responsive"
    {swatches}
    components={{ input: ColorPickerButton, wrapper: ColorPickerWrapper }}
  />
</div>

<style>
  .styled-color-picker {
    --cp-swatch-grid-template-columns: repeat(auto-fit, 36px);
  }

  .portal-container {
    width: 100%;
    position: absolute;
  }
</style>
