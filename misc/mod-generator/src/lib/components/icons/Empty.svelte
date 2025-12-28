<script lang="ts">
  interface Props {
    class?: string;
  }

  const { class: className }: Props = $props();
</script>

<svg
  xmlns="http://www.w3.org/2000/svg"
  viewBox="0 -960 960 960"
  class={className}
>
</svg>
