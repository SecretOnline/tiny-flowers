<script lang="ts">
  import ColorPicker from "svelte-awesome-color-picker";
  import ColorPickerButton from "./ColorPickerButton.svelte";
  import ColorPickerWrapper from "./ColorPickerWrapper.svelte";
  import { setPortalId } from "./context";

  interface Props {
    color: string;
  }

  let { color = $bindable() }: Props = $props();

  const uid = $props.id();
  const portalId = `color-picker-${uid}`;

  setPortalId(portalId);
</script>

<div class="styled-color-picker">
  <div id={portalId} class="portal-container"></div>
  <ColorPicker
    bind:hex={color}
    position="responsive"
    components={{ input: ColorPickerButton, wrapper: ColorPickerWrapper }}
  />
</div>

<style>
  .portal-container {
    width: 100%;
    position: absolute;
  }
</style>
