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
  ><path
    d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"
  /></svg
>
