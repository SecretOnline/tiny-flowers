<script lang="ts">
  import type { BlockTextureEntry } from "../../types/state";
  import { blockTexturePathForSlot } from "../../util";
  import Delete from "../icons/Delete.svelte";
  import Image from "../icons/Image.svelte";
  import ImageUpload from "../icons/ImageUpload.svelte";
  import ImagePreview from "../ImagePreview.svelte";
  import FlowerbedTextureGenerator from "./FlowerbedTextureGenerator.svelte";

  interface Props {
    entry: BlockTextureEntry;
    blockId: string;
    hasFileTexture: boolean;
    hasCreateTexture: boolean;
    itemTexture?: File;
    onRemove?: () => void;
  }

  const {
    entry = $bindable(),
    blockId,
    hasFileTexture,
    hasCreateTexture,
    itemTexture,
    onRemove,
  }: Props = $props();
  const uid = $props.id();
</script>

<tr class="texture-item">
  <td>
    <label class="visually-hidden" for="block_texture_{uid}_slot"
      >Texture slot</label
    >
    <div class="inline-group">
      <input
        type="text"
        class="text-input"
        id="block_texture_{uid}_slot"
        bind:value={entry.slot}
      />
    </div>
  </td>
  <td>
    <label class="visually-hidden" for="block_texture_{uid}_source"
      >Source</label
    >
    <div class="inline-group">
      <select
        class="button"
        id="block_texture_{uid}_source"
        bind:value={
          () => entry.texture.type,
          (newType) => {
            if (newType === "reference") {
              entry.texture = {
                type: newType,
                reference: blockTexturePathForSlot(blockId, entry.slot),
              };
            } else if (newType === "file") {
              entry.texture = {
                type: newType,
                file: undefined,
              };
            } else if (newType === "create") {
              entry.texture = {
                type: newType,
                template: "tiny_flowers",
                file: undefined,
              };
            }
          }
        }
      >
        <option value="reference">Existing identifier</option>
        <option value="file">Upload image</option>
        <option value="create">Create from template</option>
      </select>
    </div>
  </td>
  {#if hasCreateTexture}
    <td>
      {#if entry.texture.type === "create"}
        <label class="visually-hidden" for="block_texture_{uid}_template"
          >Source</label
        >
        <div class="inline-group">
          <select
            class="button"
            id="block_texture_{uid}_template"
            bind:value={entry.texture.template}
          >
            <option value="tiny_flowers">Tiny Flowers</option>
            <option value="pink_petals">Pink Petals</option>
          </select>
        </div>
      {/if}
    </td>
  {/if}
  {#if hasFileTexture || hasCreateTexture}
    <td>
      {#if entry.texture.type === "file"}
        {@const tex = entry.texture}
        <label class="visually-hidden" for="block_texture_{uid}_file"
          >Upload</label
        >
        <div class="inline-group">
          <input
            type="file"
            class="visually-hidden"
            id="block_texture_{uid}_file"
            accept="image/png"
            bind:files={
              () => {
                const dt = new DataTransfer();
                if (tex.file) {
                  dt.items.add(tex.file);
                }
                return dt.files;
              },
              (newFiles) => {
                tex.file = newFiles?.[0] ?? undefined;
              }
            }
          />
          <label
            class="file-input-facade button"
            for="block_texture_{uid}_file"
          >
            {#if tex.file?.name}
              <Image /><span>{tex.file.name}</span>
            {:else}
              <ImageUpload /><span>Browse...</span>
            {/if}
          </label>
        </div>
      {:else if entry.texture.type === "create"}
        <FlowerbedTextureGenerator
          template={entry.texture.template}
          onGenerate={(file) => {
            if (entry.texture.type === "create") {
              entry.texture.file = file;
            }
          }}
          {itemTexture}
        />
      {/if}
    </td>
  {/if}
  <td>
    <label class="visually-hidden" for="block_texture_{uid}_reference"
      >Identifier</label
    >
    <div class="inline-group">
      {#if entry.texture.type === "file" || entry.texture.type === "create"}
        <input
          type="text"
          class="text-input"
          id="block_texture_{uid}_reference"
          disabled
          value={blockTexturePathForSlot(blockId, entry.slot)}
        />
      {:else if entry.texture.type === "reference"}
        <input
          type="text"
          class="text-input"
          id="block_texture_{uid}_reference"
          bind:value={entry.texture.reference}
        />
      {/if}
    </div>
  </td>
  <td>
    <button
      class="button icon-button color-delete"
      type="button"
      onclick={onRemove}
    >
      <Delete />
    </button>
  </td>
  {#if hasFileTexture || hasCreateTexture}
    <td>
      {#if entry.texture.type === "file" || entry.texture.type === "create"}
        <label class="image-preview-label" for="block_texture_{uid}_file">
          <ImagePreview file={entry.texture.file} alt={entry.slot} />
        </label>
      {/if}
    </td>
  {/if}
</tr>

<style>
</style>
