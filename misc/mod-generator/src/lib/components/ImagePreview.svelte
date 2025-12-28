<script lang="ts">
  import defaultImage from "../../assets/missing32.png";

  interface Props {
    file: File | undefined;
    alt: string;
  }

  let { file, alt }: Props = $props();

  let url = $state<string>();
  $effect(() => {
    if (file) {
      url = URL.createObjectURL(file);

      return () => {
        if (url) {
          URL.revokeObjectURL(url);
        }
      };
    } else {
      url = undefined;
    }
  });

  let src = $derived(url ?? defaultImage);
</script>

<img {src} {alt} class="image-preview" />

<style>
  .image-preview {
    aspect-ratio: 1/1;
    width: 64px;
    height: 64px;
    image-rendering: pixelated;
  }
</style>
