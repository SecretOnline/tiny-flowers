<script lang="ts">
  import type { ModMetadata } from "../../types/state";
  import IconGenerator from "./ModIconGenerator.svelte";
  import Add from "../icons/Add.svelte";
  import Delete from "../icons/Delete.svelte";
  import ImageUpload from "../icons/ImageUpload.svelte";
  import Image from "../icons/Image.svelte";
  import ImagePreview from "../ImagePreview.svelte";

  interface Props {
    metadata: ModMetadata;
  }

  const { metadata = $bindable() }: Props = $props();

  function addAuthor() {
    metadata.authors.push("");
  }

  function removeAuthor(index: number) {
    metadata.authors.splice(index, 1);
  }
</script>

<section class="metadata-section layout-grid input-group">
  <div class="block-group metadata-id">
    <label for="mod-id">Mod ID</label>
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="mod-id"
        bind:value={metadata.id}
        placeholder="your_flower_pack_id"
      />
    </div>
  </div>

  <div class="block-group metadata-version">
    <label for="mod-version">Version</label>
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="mod-version"
        bind:value={metadata.version}
      />
    </div>
  </div>

  <div class="block-group metadata-name">
    <label for="mod-name">Mod Name</label>
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="mod-name"
        bind:value={metadata.name}
        placeholder="Tiny Flowers for ..."
      />
    </div>
  </div>

  <div class="block-group metadata-description">
    <label for="mod-description">Description</label>
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="mod-description"
        bind:value={metadata.description}
        placeholder="Adds Tiny Flowers support to the ... mod."
      />
    </div>
  </div>

  <div class="block-group metadata-icon">
    <label for="mod-icon">Mod Icon</label>
    <div class="inline-group">
      <input
        type="file"
        class="visually-hidden"
        id="mod-icon"
        accept="image/png"
        bind:files={
          () => {
            const dt = new DataTransfer();
            if (metadata.icon) {
              dt.items.add(metadata.icon);
            }
            return dt.files;
          },
          (newFiles) => {
            metadata.icon = newFiles?.[0] ?? undefined;
          }
        }
      />
      <label class="file-input-facade button" for="mod-icon">
        {#if metadata.icon?.name}
          <Image /><span>{metadata.icon.name}</span>
        {:else}
          <ImageUpload /><span>Browse...</span>
        {/if}
      </label>
    </div>

    <label class="image-preview-label" for="mod-icon">
      <ImagePreview file={metadata.icon} alt={metadata.name} />
    </label>

    <IconGenerator
      onGenerate={(file) => {
        metadata.icon = file;
      }}
    />
  </div>

  <div class="block-group metadata-license">
    <label for="mod-license">License</label>
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="mod-license"
        bind:value={metadata.license}
      />
    </div>
  </div>

  <div class="block-group metadata-authors input-group">
    <h4 class="input-group-heading">Authors</h4>
    <ul class="input-list">
      {#each metadata.authors, i (i)}
        <li class="input-list-item inline-group">
          <label class="visually-hidden" for={`author_${i}`}>Name</label>
          <input
            type="text"
            class="text-input"
            id={`author_${i}`}
            bind:value={metadata.authors[i]}
          />
          <button
            class="button icon-button color-delete"
            type="button"
            onclick={() => removeAuthor(i)}
          >
            <Delete />
          </button>
        </li>
      {/each}

      <button
        class="button color-add"
        type="button"
        onclick={() => addAuthor()}
      >
        <Add /><span>Add author</span>
      </button>
    </ul>
  </div>
</section>

<style>
  .metadata-section {
    grid-template-areas: "id" "version" "name" "description" "icon" "license" "authors";
    margin-block-start: 1rem;
  }

  .metadata-id {
    grid-area: id;
  }
  .metadata-name {
    grid-area: name;
  }
  .metadata-description {
    grid-area: description;
  }
  .metadata-version {
    grid-area: version;
  }
  .metadata-icon {
    grid-area: icon;
  }
  .metadata-authors {
    grid-area: authors;
  }
  .metadata-license {
    grid-area: license;
  }

  @media (min-width: 560px) {
    .metadata-section {
      grid-template-columns: repeat(2, minmax(0, 1fr));
      grid-template-areas:
        "id id"
        "name name"
        "description description"
        "version license"
        "authors icon";
    }
  }

  @media (min-width: 1072px) {
    .metadata-section {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      grid-template-areas:
        "id version license icon"
        "name name authors icon"
        "description description authors icon";
    }
  }

  .metadata-icon .image-preview-label {
    flex-grow: 1;
  }
  .metadata-icon .image-preview-label :global(.image-preview) {
    width: 92px;
    height: 92px;
  }
</style>
