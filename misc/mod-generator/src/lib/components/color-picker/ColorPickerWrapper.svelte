<script>
  import { portal } from "svelte-portal";
  import { getPortalId } from "./context";

  let { wrapper = $bindable(), isOpen, isDialog, children } = $props();

  const portalId = getPortalId();
</script>

<div
  use:portal={`#${portalId}`}
  bind:this={wrapper}
  class="wrapper"
  class:is-open={isOpen}
  role={isDialog ? "dialog" : undefined}
  aria-label="color picker"
>
  {@render children()}
</div>

<style>
  div {
    padding: 8px;
    background-color: var(--cp-bg-color, white);
    margin: 0 10px 10px;
    border: 1px solid var(--cp-border-color, black);
    border-radius: 12px;
    display: none;
    width: max-content;
  }
  .is-open {
    display: inline-block;
  }
  [role="dialog"] {
    position: absolute;
    top: calc(var(--input-size, 25px) + 36px);
    left: 8px;
    z-index: var(--picker-z-index, 2);
  }
</style>
