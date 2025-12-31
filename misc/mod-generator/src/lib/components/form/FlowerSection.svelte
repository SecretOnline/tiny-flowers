<script lang="ts">
  import type { CombinedFlowerData, TextureFile } from "../../types/state";
  import Add from "../icons/Add.svelte";
  import Check from "../icons/Check.svelte";
  import Delete from "../icons/Delete.svelte";
  import Empty from "../icons/Empty.svelte";
  import ExpandDown from "../icons/ExpandDown.svelte";
  import ExpandRight from "../icons/ExpandRight.svelte";
  import Image from "../icons/Image.svelte";
  import ImageUpload from "../icons/ImageUpload.svelte";
  import ImagePreview from "../ImagePreview.svelte";
  import BlockTextureRow from "./BlockTextureRow.svelte";

  const PREDEFINED_BLOCK_MODELS = [
    {
      value: "petals",
      name: "Pink Petals",
      id: "minecraft:block/flowerbed",
      slots: ["flowerbed"],
    },
    {
      value: "leaf_litter",
      name: "Leaf Litter",
      id: "tiny_flowers:block/garden_leaf_litter",
      slots: ["flowerbed"],
    },
    {
      value: "garden",
      name: "1 layer (default stem)",
      id: "tiny_flowers:block/garden",
      slots: ["flowerbed"],
    },
    {
      value: "garden_untinted",
      name: "1 layer (custom stem)",
      id: "tiny_flowers:block/garden_untinted",
      slots: ["flowerbed", "stem"],
    },
    {
      value: "garden_double",
      name: "2 layers (default stem)",
      id: "tiny_flowers:block/garden_double",
      slots: ["flowerbed", "flowerbed_upper"],
    },
    {
      value: "garden_double_untinted",
      name: "2 layers (custom stem)",
      id: "tiny_flowers:block/garden_double_untinted",
      slots: ["flowerbed", "flowerbed_upper", "stem"],
    },
    {
      value: "garden_triple",
      name: "3 layers (default stem)",
      id: "tiny_flowers:block/garden_triple",
      slots: ["flowerbed", "flowerbed_middle", "flowerbed_upper"],
    },
    {
      value: "garden_triple_untinted",
      name: "3 layers (custom stem)",
      id: "tiny_flowers:block/garden_triple_untinted",
      slots: ["flowerbed", "flowerbed_middle", "flowerbed_upper", "stem"],
    },
  ];

  interface Props {
    flower: CombinedFlowerData;
    onRemove?: () => void;
  }

  let { flower = $bindable(), onRemove }: Props = $props();
  const uid = $props.id();

  function addFlowerName() {
    flower.name.push({ language: "", name: "" });
  }

  function removeFlowerName(index: number) {
    flower.name.splice(index, 1);
  }

  function addSurvivalBlock(value = "") {
    flower.canSurviveOn.push(value);
  }

  function removeSurvivalBlock(index: number) {
    flower.canSurviveOn.splice(index, 1);
  }

  function addEffect() {
    flower.suspiciousStewEffects.push({
      id: "",
      duration: 0,
    });
  }

  function removeEffect(index: number) {
    flower.suspiciousStewEffects.splice(index, 1);
  }

  function addBlockTexture() {
    flower.blockTextures.push({
      slot: "",
      texture: flower.isSegmented
        ? { type: "reference", reference: "" }
        : { type: "file", file: undefined },
    });
  }

  function removeBlockTexture(index: number) {
    flower.blockTextures.splice(index, 1);
  }

  let flowerName = $derived(
    flower.name.find((lang) => lang.language === "en_us")?.name ||
      flower.id ||
      "New flower",
  );

  let hasFileTexture = $derived(
    flower.blockTextures.some((entry) => entry.texture.type === "file"),
  );
  let hasCreateTexture = $derived(
    flower.blockTextures.some((entry) => entry.texture.type === "create"),
  );
</script>

{#if flower.isExpanded}
  <section class="flower-data-section layout-grid input-group">
    <div class="inline-group flower-data-expand">
      <button
        type="button"
        class="button icon-button"
        onclick={() => {
          flower.isExpanded = false;
        }}
      >
        <ExpandDown />
      </button>
      <h4 class="expand-heading">
        {flowerName}
      </h4>
      {#if onRemove}
        <button
          class="button icon-button color-delete"
          type="button"
          onclick={onRemove}
        >
          <Delete />
        </button>
      {/if}
    </div>

    <div class="block-group flower-data-id">
      <label for="flower-id-{uid}">ID</label>
      <div class="inline-group">
        <input
          type="text"
          class="text-input"
          id="flower-id-{uid}"
          bind:value={flower.id}
          placeholder="your_mod_id:tiny_flower_id"
        />
      </div>
    </div>

    <div class="block-group flower-data-original">
      <label for="original-id-{uid}">Full Block ID</label>
      <div class="inline-group">
        <input
          type="text"
          class="text-input"
          id="original-id-{uid}"
          bind:value={flower.originalId}
          placeholder="your_mod_id:flower_id"
        />
      </div>
      <p>
        When Florists' Shears are used on this block, it will be turned into
        Tiny Flowers.
      </p>
    </div>

    <div class="block-group flower-data-segmented">
      <label for="is-segmented-{uid}">Is Segmented</label>
      <div class="inline-group">
        <input
          type="checkbox"
          class="visually-hidden"
          id="is-segmented-{uid}"
          bind:checked={flower.isSegmented}
        />
        <label class="button checkbox" for="is-segmented-{uid}">
          {#if flower.isSegmented}
            <Check />
          {:else}
            <Empty />
          {/if}
        </label>
      </div>
      <p>
        Check this box if the original flower is made of multiple parts (e.g.
        Pink Petals, Wildflowers, or Leaf Litter).
      </p>
    </div>

    {#if !flower.isSegmented}
      <div class="block-group flower-data-item">
        <label for="item-texture-{uid}">Item Texture</label>
        <div class="inline-group">
          <input
            type="file"
            class="visually-hidden"
            id="item-texture-{uid}"
            accept="image/png"
            bind:files={
              () => {
                const dt = new DataTransfer();
                if (flower.itemTexture) {
                  dt.items.add(flower.itemTexture);
                }
                return dt.files;
              },
              (newFiles) => {
                flower.itemTexture = newFiles?.[0] ?? undefined;
              }
            }
          />
          <label class="file-input-facade button" for={`item-texture-${uid}`}>
            {#if flower.itemTexture?.name}
              <Image /><span>{flower.itemTexture.name}</span>
            {:else}
              <ImageUpload /><span>Browse...</span>
            {/if}
          </label>
        </div>

        <label class="image-preview-label" for="item-texture-{uid}">
          <ImagePreview
            file={flower.itemTexture}
            alt={flower.name.find((e) => e.language === "en_us")?.name ??
              flower.id}
          />
        </label>
      </div>
    {/if}

    {#if !flower.isSegmented}
      <div class="block-group flower-data-translations input-group">
        <h4 class="input-group-heading">Translations</h4>

        <table class="input-table">
          <thead>
            <tr>
              <th>Language ID</th>
              <th>Flower Name</th>
              <th class="delete-column"></th>
            </tr>
          </thead>
          <tbody>
            {#each flower.name as entry, i (i)}
              <tr class="name-item">
                <td>
                  <label
                    class="visually-hidden"
                    for={`${flower.id}_lang_id_${entry.language}`}
                    >Language ID</label
                  >
                  <div class="inline-group">
                    <input
                      type="text"
                      class="text-input"
                      id={`${flower.id}_lang_id_${entry.language}`}
                      bind:value={flower.name[i].language}
                    />
                  </div>
                </td>
                <td>
                  <label
                    class="visually-hidden"
                    for={`${flower.id}_lang_name_${entry.language}`}
                    >Flower Name</label
                  >
                  <div class="inline-group">
                    <input
                      type="text"
                      class="text-input"
                      id={`${flower.id}_lang_name_${entry.language}`}
                      bind:value={flower.name[i].name}
                      placeholder={entry.language.startsWith("en_")
                        ? "Tiny ..."
                        : undefined}
                    />
                  </div>
                </td>
                <td>
                  <button
                    class="button icon-button color-delete"
                    type="button"
                    onclick={() => removeFlowerName(i)}
                  >
                    <Delete />
                  </button>
                </td>
              </tr>
            {/each}
          </tbody>
        </table>

        <button
          class="button color-add"
          type="button"
          onclick={() => addFlowerName()}
        >
          <Add /><span>Add translation</span>
        </button>
      </div>
    {/if}

    <div class="block-group flower-data-survive input-group">
      <h4 class="input-group-heading">Can Survive On</h4>
      <ul class="input-list">
        {#each flower.canSurviveOn, i (i)}
          <li class="input-list-item inline-group">
            <input
              type="text"
              class="text-input"
              bind:value={flower.canSurviveOn[i]}
            />
            <button
              class="button icon-button color-delete"
              type="button"
              onclick={() => removeSurvivalBlock(i)}
            >
              <Delete />
            </button>
          </li>
        {/each}
      </ul>

      <div class="inline-group">
        <button
          class="button color-add"
          type="button"
          onclick={() => addSurvivalBlock()}
        >
          <Add /><span>Add block or tag</span>
        </button>

        <button
          class="button color-add"
          type="button"
          onclick={() =>
            addSurvivalBlock("#tiny_flowers:tiny_flower_can_survive_on")}
        >
          <Add /><span>Add default tag</span>
        </button>
      </div>
    </div>

    <div class="block-group flower-data-effects input-group">
      <h4 class="input-group-heading">Suspicious Stew Effects</h4>

      <table class="input-table">
        <thead>
          <tr>
            <th>Effect ID</th>
            <th>Duration (ticks)</th>
            <th class="delete-column"></th>
          </tr>
        </thead>
        <tbody>
          {#each flower.suspiciousStewEffects as effect, i (i)}
            <tr class="effect-item">
              <td>
                <label
                  class="visually-hidden"
                  for={`${flower.id}_effect_id_${i}`}>Effect ID</label
                >
                <div class="inline-group">
                  <input
                    type="text"
                    class="text-input"
                    id={`${flower.id}_effect_id_${i}`}
                    bind:value={effect.id}
                    placeholder="minecraft:effect_id"
                  />
                </div>
              </td>
              <td>
                <label
                  class="visually-hidden"
                  for={`${flower.id}_effect_duration_${i}`}
                  >Duration (ticks)</label
                >
                <div class="inline-group">
                  <input
                    type="number"
                    class="text-input"
                    id={`${flower.id}_effect_duration_${i}`}
                    bind:value={effect.duration}
                  />
                </div>
              </td>
              <td>
                <button
                  class="button icon-button color-delete"
                  type="button"
                  onclick={() => removeEffect(i)}
                >
                  <Delete />
                </button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>

      <button
        class="button color-add"
        type="button"
        onclick={() => addEffect()}
      >
        <Add /><span>Add effect</span>
      </button>
    </div>

    <div class="block-group flower-data-parent-preset">
      <label for="model-parent-preset-{uid}">Model Parent Preset</label>
      <p>
        The Tiny Flowers mod provides a set of built-in models that only need
        the upwards facing textures defined. In general, try to use the fewest
        number of layers while still getting the idea of the original flower
        across.
      </p>
      <p>
        The "Pink Petals" model has only a single stem in positions 2 and 4. The
        layered models added by Tiny Flowers have three stems in position 2, and
        two stems in position 4.
      </p>
      <div class="preset-button-grid">
        {#each PREDEFINED_BLOCK_MODELS as model}
          <button
            type="button"
            class="button"
            value={model.value}
            onclick={() => {
              flower.parentModel = { type: "prefix", prefix: model.id };
              flower.blockTextures = model.slots.map(
                (slot) =>
                  flower.blockTextures.find((t) => t.slot === slot) ?? {
                    slot,
                    texture: flower.isSegmented
                      ? { type: "reference", reference: "" }
                      : { type: "file", file: undefined },
                  },
              );
            }}>{model.name}</button
          >
        {/each}
      </div>
    </div>

    <div class="block-group flower-data-parent">
      <label for="model-parent-base-{uid}">Model Parent Identifier</label>
      <div class="inline-group">
        <select
          class="button"
          id="parent-model-type-{uid}"
          value={flower.parentModel.type}
          onchange={(event) => {
            if (event.currentTarget.value === "prefix") {
              flower.parentModel = { type: "prefix", prefix: "" };
            } else if (event.currentTarget.value === "custom") {
              if (
                flower.parentModel.type === "prefix" &&
                flower.parentModel.prefix
              ) {
                flower.parentModel = {
                  type: "custom",
                  model1: `${flower.parentModel.prefix}_1`,
                  model2: `${flower.parentModel.prefix}_2`,
                  model3: `${flower.parentModel.prefix}_3`,
                  model4: `${flower.parentModel.prefix}_4`,
                };
              } else {
                flower.parentModel = {
                  type: "custom",
                  model1: "",
                  model2: "",
                  model3: "",
                  model4: "",
                };
              }
            }
          }}
        >
          <option value="prefix">Prefix</option>
          <option value="custom">Custom</option>
        </select>
      </div>
      {#if flower.parentModel.type === "prefix"}
        {@const prefix = flower.parentModel.prefix ?? "<prefix>"}
        <div class="inline-group">
          <input
            type="text"
            class="text-input"
            id="parent-model-prefix-{uid}"
            placeholder="namespace:block/identifier"
            bind:value={flower.parentModel.prefix}
          />
        </div>
        <p>
          This generator will generate 4 block models, with parents of <code
            >{prefix}_1</code
          >, <code>{prefix}_2</code>, <code>{prefix}_3</code>, and
          <code>{prefix}_4</code>.
        </p>
      {:else if flower.parentModel.type === "custom"}
        <div class="inline-group">
          <input
            type="text"
            class="text-input"
            id="parent-model-1-{uid}"
            placeholder="namespace:block/identifier_1"
            bind:value={flower.parentModel.model1}
          />
        </div>
        <div class="inline-group">
          <input
            type="text"
            class="text-input"
            id="parent-model-2-{uid}"
            placeholder="namespace:block/identifier_2"
            bind:value={flower.parentModel.model2}
          />
        </div>
        <div class="inline-group">
          <input
            type="text"
            class="text-input"
            id="parent-model-3-{uid}"
            placeholder="namespace:block/identifier_3"
            bind:value={flower.parentModel.model3}
          />
        </div>
        <div class="inline-group">
          <input
            type="text"
            class="text-input"
            id="parent-model-4-{uid}"
            placeholder="namespace:block/identifier_4"
            bind:value={flower.parentModel.model4}
          />
        </div>
      {/if}
    </div>

    <div class="block-group flower-data-tint">
      <label for="tint-source-{uid}">Tint Source</label>
      <div class="inline-group">
        <select
          class="button"
          id="tint-source-{uid}"
          bind:value={flower.tintSource}
        >
          <option value="grass">Grass</option>
          <option value="dry_foliage">Dry Foliage</option>
        </select>
      </div>
      <p>
        When using the default stem, or any other model that has an element with
        a <code>tintindex</code>, set which source should be used for the tint.
        The Tiny Flowers mod currently does not support multiple
        <code>tintindex</code>es in the same model.
      </p>
    </div>

    <div class="block-group flower-data-block input-group">
      <h4 class="input-group-heading">Block Textures</h4>

      <table class="input-table">
        <thead>
          <tr>
            <th>Texture Slot</th>
            <th>Type</th>
            {#if hasCreateTexture}
              <th> Template </th>
            {/if}
            {#if hasFileTexture || hasCreateTexture}
              <th>
                {#if hasFileTexture}
                  File
                {/if}
                {#if hasFileTexture && hasCreateTexture}
                  /
                {/if}
                {#if hasCreateTexture}
                  Colors
                {/if}
              </th>
            {/if}
            <th>Identifier</th>
            <th class="delete-column"></th>
            {#if hasFileTexture || hasCreateTexture}
              <th>Preview</th>
            {/if}
          </tr>
        </thead>
        <tbody>
          {#each flower.blockTextures, i (i)}
            <BlockTextureRow
              bind:entry={flower.blockTextures[i]}
              {hasFileTexture}
              {hasCreateTexture}
              blockId={flower.id}
              itemTexture={flower.itemTexture}
              onRemove={() => removeBlockTexture(i)}
            />
          {/each}
        </tbody>
      </table>

      <button
        class="button color-add"
        type="button"
        onclick={() => addBlockTexture()}
      >
        <Add /><span>Add texture mapping</span>
      </button>
    </div>
  </section>
{:else}
  {@const previewTexture =
    flower.itemTexture ??
    (
      flower.blockTextures.find(
        (t) => t.slot === "particle" && t.texture.type === "file",
      )?.texture as TextureFile
    )?.file ??
    undefined}
  <section class="flower-data-section-collapsed input-group inline-group">
    <button
      type="button"
      class="button icon-button expand-align-top"
      onclick={() => {
        flower.isExpanded = true;
      }}
    >
      <ExpandRight />
    </button>
    <h4 class="expand-heading expand-align-top">
      {flowerName}
    </h4>
    <ImagePreview file={previewTexture} alt={flowerName} />
    <button
      class="button icon-button color-delete expand-align-top"
      type="button"
      onclick={onRemove}
    >
      <Delete />
    </button>
  </section>
{/if}

<style>
  .flower-data-section-collapsed {
    display: flex;
  }

  .expand-heading {
    flex-grow: 1;
  }

  .expand-align-top {
    align-self: flex-start;
    align-self: start;
  }

  .flower-data-section {
    grid-template-areas:
      "expand" "id" "original" "segmented" "item" "translations"
      "survive" "effects" "parent-preset" "parent" "tint" "block";
  }

  .flower-data-expand {
    grid-area: expand;
  }
  .flower-data-id {
    grid-area: id;
  }
  .flower-data-translations {
    grid-area: translations;
  }
  .flower-data-original {
    grid-area: original;
  }
  .flower-data-segmented {
    grid-area: segmented;
  }
  .flower-data-survive {
    grid-area: survive;
  }
  .flower-data-item {
    grid-area: item;
  }
  .flower-data-tint {
    grid-area: tint;
  }
  .flower-data-effects {
    grid-area: effects;
  }
  .flower-data-parent {
    grid-area: parent;
  }
  .flower-data-parent-preset {
    grid-area: parent-preset;
  }
  .flower-data-block {
    grid-area: block;
  }

  @media (min-width: 560px) {
    .flower-data-section {
      grid-template-columns: repeat(2, minmax(0, 1fr));
      grid-template-areas:
        "expand expand"
        "id id"
        "original segmented"
        "item item"
        "translations translations"
        "survive survive"
        "effects effects"
        "parent-preset parent-preset"
        "parent tint"
        "block block";
    }
  }

  @media (min-width: 1072px) {
    .flower-data-section {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      grid-template-areas:
        "expand expand expand expand"
        "id id id id"
        "original original segmented item"
        "translations translations translations translations"
        "survive survive effects effects"
        "parent-preset parent-preset parent tint"
        "block block block block";
    }
  }

  .input-table {
    width: 100%;
    overflow-x: auto;
  }

  .preset-button-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 0.5rem;
  }

  .delete-column {
    width: 28px;
  }
</style>
