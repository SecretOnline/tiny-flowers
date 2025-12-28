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
  <path d="M450-140v-310H140v-60h310v-310h60v310h310v60H510v310h-60Z" />
</svg>
