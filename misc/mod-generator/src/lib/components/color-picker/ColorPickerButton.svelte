<script lang="ts">
  import Palette from "../icons/Palette.svelte";

  // Much copied from the original at:
  // https://github.com/Ennoriel/svelte-awesome-color-picker/blob/master/src/lib/components/variant/default/Input.svelte

  interface Props {
    /** DOM element of the label wrapper */
    labelElement: HTMLLabelElement | undefined;
    /** hex color */
    hex: string | null;
    /** input label */
    label: string;
    /** input name, useful in a native form */
    name?: string | undefined;
    /** directionality left to right, or right to left*/
    dir: "ltr" | "rtl";
  }

  let {
    labelElement = $bindable(),
    hex,
    label,
    name = undefined,
    dir,
  }: Props = $props();

  function preventDefault(e: MouseEvent) {
    e.preventDefault();
    /* prevent browser color picker from opening unless javascript is broken */
  }
</script>

<!-- svelte-ignore a11y_no_noninteractive_element_interactions, a11y_click_events_have_key_events -->
<label
  bind:this={labelElement}
  onclick={preventDefault}
  onmousedown={preventDefault}
  {dir}
>
  <div class="container">
    <input
      type="color"
      {name}
      value={hex}
      onclick={preventDefault}
      onmousedown={preventDefault}
      aria-haspopup="dialog"
    />
    <div class="button icon-button color-button" style="--color: {hex}">
      <Palette />
    </div>
  </div>
  <span class="visually-hidden">{label}</span>
</label>

<style>
  label {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    border-radius: 3px;
    margin: 4px;
    user-select: none;
  }

  .container {
    position: relative;
    display: block;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  input {
    margin: 0;
    padding: 0;
    border: none;
    width: 1px;
    height: 1px;
    flex-shrink: 0;
    opacity: 0;
  }

  .color-button {
    --solid: color(from var(--color) srgb r g b / 1);
    --light: color-mix(in hsl shorter hue, var(--solid) 100%, #9b9b9b 50%);
    --dark: color-mix(in hsl shorter hue, var(--solid) 100%, #282828 50%);

    border-inline-start-color: var(--light, #2079c0);
    border-block-start-color: var(--light, #2079c0);
    border-inline-end-color: var(--dark, #054473);
    border-block-end-color: var(--dark, #054473);
    background-color: var(--solid, #0167ac);
  }
</style>
