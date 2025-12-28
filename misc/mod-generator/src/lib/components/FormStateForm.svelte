<script lang="ts">
  import type { FormState } from "../types/state";
  import { blockTexturePathForSlot } from "../util";
  import dirtPackIconUrl from "../../assets/dirt_pack_icon.png";
  import dirtItemTextureUrl from "../../assets/tiny_dirt_item_texture.png";
  import dirtBlockTextureUrl from "../../assets/tiny_dirt_block_texture.png";
  import ImagePreview from "./ImagePreview.svelte";
  import Delete from "./icons/Delete.svelte";
  import Add from "./icons/Add.svelte";
  import Image from "./icons/Image.svelte";
  import ImageUpload from "./icons/ImageUpload.svelte";
  import Check from "./icons/Check.svelte";
  import Empty from "./icons/Empty.svelte";

  const PREDEFINED_BLOCK_MODELS = [
    {
      value: "garden",
      name: "1 layer (default stem)",
      id: "tiny_flowers:garden",
      slots: ["flowerbed"],
    },
    {
      value: "garden_untinted",
      name: "1 layer (custom stem)",
      id: "tiny_flowers:garden_untinted",
      slots: ["flowerbed", "stem"],
    },
    {
      value: "garden_double",
      name: "2 layers (default stem)",
      id: "tiny_flowers:garden_double",
      slots: ["flowerbed", "flowerbed_upper"],
    },
    {
      value: "garden_double_untinted",
      name: "2 layers (custom stem)",
      id: "tiny_flowers:garden_double_untinted",
      slots: ["flowerbed", "flowerbed_upper", "stem"],
    },
    {
      value: "garden_triple",
      name: "3 layers (default stem)",
      id: "tiny_flowers:garden_triple",
      slots: ["flowerbed", "flowerbed_middle", "flowerbed_upper"],
    },
    {
      value: "garden_triple_untinted",
      name: "3 layers (custom stem)",
      id: "tiny_flowers:garden_triple_untinted",
      slots: ["flowerbed", "flowerbed_middle", "flowerbed_upper", "stem"],
    },
  ];

  let formState: FormState = $state({
    stateVersion: 1,
    metadata: {
      id: "",
      version: "1.0.0",
      name: "",
      description: "",
      authors: [""],
      license: "MPL-2.0",
      icon: undefined,
    },
    flowers: [
      {
        id: "",
        name: [{ language: "en_us", name: "" }],
        originalId: "",
        isSegmented: false,
        canSurviveOn: ["#tiny_flowers:tiny_flower_can_survive_on"],
        suspiciousStewEffects: [],
        itemTexture: undefined,
        tintSource: "grass",
        modelParentBase: "",
        blockTextures: [],
      },
    ],
  });

  // #region Dirt pack
  let dirtPackImagesPromise =
    $state<Promise<Record<"icon" | "item" | "block", File>>>();
  async function getDirtPackImages() {
    if (!dirtPackImagesPromise) {
      var iconPromise = fetch(dirtPackIconUrl)
        .then((response) => response.blob())
        .then((blob) => new File([blob], "icon.png"));
      var itemPromise = fetch(dirtItemTextureUrl)
        .then((response) => response.blob())
        .then((blob) => new File([blob], "tiny_dirt.png"));
      var blockPromise = fetch(dirtBlockTextureUrl)
        .then((response) => response.blob())
        .then((blob) => new File([blob], "tiny_dirt.png"));

      dirtPackImagesPromise = Promise.all([
        iconPromise,
        itemPromise,
        blockPromise,
      ]).then(([icon, item, block]) => ({ icon, item, block }));
    }

    return dirtPackImagesPromise;
  }
  async function setToDirtFlower() {
    const images = await getDirtPackImages();

    formState = {
      stateVersion: 1,
      metadata: {
        id: "tiny_dirt_flower",
        version: "1.0.0",
        name: "Tiny Dirt Flower",
        description: "An example pack that adds a tiny flower.",
        authors: ["secret_online"],
        license: "MPL-2.0",
        icon: images.icon,
      },
      flowers: [
        {
          id: "tiny_dirt_flower:tiny_dirt",
          name: [{ language: "en_us", name: "Tiny Dirt Flower" }],
          originalId: "minecraft:dirt",
          isSegmented: false,
          canSurviveOn: ["#tiny_flowers:tiny_flower_can_survive_on"],
          suspiciousStewEffects: [{ id: "minecraft:darkness", duration: 140 }],
          itemTexture: images.item,
          tintSource: "grass",
          modelParentBase: "tiny_flowers:block/garden",
          blockTextures: [
            {
              slot: "flowerbed",
              texture: { type: "file", file: images.block },
            },
          ],
        },
      ],
    };
  }
  // #endregion

  // #region Functions
  function addFlower() {
    formState.flowers.push({
      id: "",
      name: [{ language: "en_us", name: "" }],
      originalId: "",
      isSegmented: false,
      canSurviveOn: ["#tiny_flowers:tiny_flower_can_survive_on"],
      suspiciousStewEffects: [],
      itemTexture: undefined,
      tintSource: "grass",
      modelParentBase: "",
      blockTextures: [],
    });
  }

  function removeFlower(index: number) {
    formState.flowers.splice(index, 1);
  }

  function addAuthor() {
    formState.metadata.authors.push("");
  }

  function removeAuthor(index: number) {
    formState.metadata.authors.splice(index, 1);
  }

  function addFlowerName(flowerIndex: number) {
    formState.flowers[flowerIndex].name.push({ language: "", name: "" });
  }

  function removeFlowerName(flowerIndex: number, index: number) {
    formState.flowers[flowerIndex].name.splice(index, 1);
  }

  function addSurvivalBlock(flowerIndex: number, value = "") {
    formState.flowers[flowerIndex].canSurviveOn.push(value);
  }

  function removeSurvivalBlock(flowerIndex: number, index: number) {
    formState.flowers[flowerIndex].canSurviveOn.splice(index, 1);
  }

  function addEffect(flowerIndex: number) {
    formState.flowers[flowerIndex].suspiciousStewEffects.push({
      id: "",
      duration: 0,
    });
  }

  function removeEffect(flowerIndex: number, index: number) {
    formState.flowers[flowerIndex].suspiciousStewEffects.splice(index, 1);
  }

  function addBlockTexture(flowerIndex: number) {
    formState.flowers[flowerIndex].blockTextures.push({
      slot: "",
      texture: formState.flowers[flowerIndex].isSegmented
        ? { type: "reference", reference: "" }
        : { type: "file", file: undefined },
    });
  }

  function removeBlockTexture(flowerIndex: number, index: number) {
    formState.flowers[flowerIndex].blockTextures.splice(index, 1);
  }
  // #endregion
</script>

<div class="form-container">
  <h1>Tiny Flower Mod Generator</h1>
  <p>
    Create packs of tiny flowers for the <a
      href="https://modrinth.com/mod/tiny-flowers"
      target="_blank"
      rel="noopener noreferrer external">Tiny Flowers</a
    > mod.
  </p>
  <button class="button" type="button" onclick={() => setToDirtFlower()}
    >Load example pack</button
  >

  <h2>
    {formState.metadata.name || "New mod"} (v{formState.metadata.version ??
      "???"})
  </h2>
  <section class="metadata-section layout-grid input-group">
    <div class="block-group metadata-id">
      <label for="mod-id">Mod ID</label>
      <div class="inline-group">
        <input
          type="text"
          class="text-input"
          id="mod-id"
          bind:value={formState.metadata.id}
          placeholder="your_mod_id"
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
          bind:value={formState.metadata.version}
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
          bind:value={formState.metadata.name}
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
          bind:value={formState.metadata.description}
          placeholder="Adds support for ... to Tiny Flowers."
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
              if (formState.metadata.icon) {
                dt.items.add(formState.metadata.icon);
              }
              return dt.files;
            },
            (newFiles) => {
              formState.metadata.icon = newFiles?.[0] ?? undefined;
            }
          }
        />
        <label class="file-input-facade button" for="mod-icon">
          {#if formState.metadata.icon?.name}
            <Image /><span>{formState.metadata.icon.name}</span>
          {:else}
            <ImageUpload /><span>Browse...</span>
          {/if}
        </label>
      </div>

      <label class="image-preview-label" for="mod-icon">
        <ImagePreview
          file={formState.metadata.icon}
          alt={formState.metadata.name}
        />
      </label>
    </div>

    <div class="block-group metadata-license">
      <label for="mod-license">License</label>
      <div class="inline-group">
        <input
          type="text"
          class="text-input"
          id="mod-license"
          bind:value={formState.metadata.license}
        />
      </div>
    </div>

    <div class="block-group metadata-authors input-group">
      <h4 class="input-group-heading">Authors</h4>
      <ul class="input-list">
        {#each formState.metadata.authors as author, i (i)}
          <li class="input-list-item inline-group">
            <label class="visually-hidden" for={`author_${i}`}>Name</label>
            <input
              type="text"
              class="text-input"
              id={`author_${i}`}
              bind:value={formState.metadata.authors[i]}
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

  <section class="flowers-section">
    <div class="inline-group">
      <h3>Flowers</h3>
    </div>

    {#each formState.flowers as flower, flowerIndex (flowerIndex)}
      {@const hasFileTexture = flower.blockTextures.some(
        (entry) => entry.texture.type === "file",
      )}
      <div class="flower-data-section layout-grid input-group">
        <div class="block-group flower-data-id">
          <label for="flower-id-{flowerIndex}">ID</label>
          <div class="inline-group">
            <input
              type="text"
              class="text-input"
              id="flower-id-{flowerIndex}"
              bind:value={flower.id}
              placeholder="your_mod_id:tiny_flower_id"
            />
            <button
              class="button icon-button color-delete"
              type="button"
              onclick={() => removeFlower(flowerIndex)}
            >
              <Delete />
            </button>
          </div>
        </div>

        <div class="block-group flower-data-original">
          <label for="original-id-{flowerIndex}">Full Block ID</label>
          <div class="inline-group">
            <input
              type="text"
              class="text-input"
              id="original-id-{flowerIndex}"
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
          <label for="is-segmented-{flowerIndex}">Is Segmented</label>
          <div class="inline-group">
            <input
              type="checkbox"
              class="visually-hidden"
              id="is-segmented-{flowerIndex}"
              bind:checked={flower.isSegmented}
            />
            <label class="button checkbox" for="is-segmented-{flowerIndex}">
              {#if flower.isSegmented}
                <Check />
              {:else}
                <Empty />
              {/if}
            </label>
          </div>
          <p>
            Check this box if the original flower is made of multiple parts
            (e.g. Pink Petals, Wildflowers, or Leaf Litter).
          </p>
        </div>

        <div class="block-group flower-data-item">
          <label for="item-texture-{flowerIndex}">Item Texture</label>
          <div class="inline-group">
            <input
              type="file"
              class="visually-hidden"
              id="item-texture-{flowerIndex}"
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
            <label
              class="file-input-facade button"
              for={`item-texture-${flowerIndex}`}
            >
              {#if flower.itemTexture?.name}
                <Image /><span>{flower.itemTexture.name}</span>
              {:else}
                <ImageUpload /><span>Browse...</span>
              {/if}
            </label>
          </div>

          <label class="image-preview-label" for="item-texture-{flowerIndex}">
            <ImagePreview
              file={flower.itemTexture}
              alt={flower.name.find((e) => e.language === "en_us")?.name ??
                flower.id}
            />
          </label>
        </div>

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
                      onclick={() => removeFlowerName(flowerIndex, i)}
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
            onclick={() => addFlowerName(flowerIndex)}
          >
            <Add /><span>Add translation</span>
          </button>
        </div>

        <div class="block-group flower-data-survive input-group">
          <h4 class="input-group-heading">Can Survive On</h4>
          <ul class="input-list">
            {#each flower.canSurviveOn as block, i (i)}
              <li class="input-list-item inline-group">
                <input
                  type="text"
                  class="text-input"
                  bind:value={flower.canSurviveOn[i]}
                />
                <button
                  class="button icon-button color-delete"
                  type="button"
                  onclick={() => removeSurvivalBlock(flowerIndex, i)}
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
              onclick={() => addSurvivalBlock(flowerIndex)}
            >
              <Add /><span>Add block or tag</span>
            </button>

            <button
              class="button color-add"
              type="button"
              onclick={() =>
                addSurvivalBlock(
                  flowerIndex,
                  "#tiny_flowers:tiny_flower_can_survive_on",
                )}
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
                      onclick={() => removeEffect(flowerIndex, i)}
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
            onclick={() => addEffect(flowerIndex)}
          >
            <Add /><span>Add effect</span>
          </button>
        </div>

        <div class="block-group flower-data-parent-preset">
          <label for="model-parent-preset-{flowerIndex}"
            >Model Parent Preset</label
          >
          <p>
            The Tiny Flowers mod provides a set of built-in models that only
            need the upwards facing textures defined. In general, try to use the
            fewest number of layers while still getting the idea of the original
            flower across.
          </p>
          <div class="preset-button-grid">
            {#each PREDEFINED_BLOCK_MODELS as model}
              <button
                type="button"
                class="button"
                value={model.value}
                onclick={() => {
                  flower.modelParentBase = model.id;
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
          <label for="model-parent-base-{flowerIndex}"
            >Model Parent Identifier</label
          >
          <div class="inline-group">
            <input
              type="text"
              class="text-input"
              id="model-parent-base-{flowerIndex}"
              bind:value={flower.modelParentBase}
            />
          </div>
          <p>
            This generator will generate 4 block models, with parents of <code
              >&lt;parent&gt;_1</code
            >, <code>&lt;parent&gt;_2</code>, <code>&lt;parent&gt;_3</code>, and
            <code>&lt;parent&gt;_4</code>.
          </p>
        </div>

        <div class="block-group flower-data-tint">
          <label for="tint-source-{flowerIndex}">Tint Source</label>
          <div class="inline-group">
            <select
              class="button"
              id="tint-source-{flowerIndex}"
              bind:value={flower.tintSource}
            >
              <option value="grass">Grass</option>
              <option value="dry_foliage">Dry Foliage</option>
            </select>
          </div>
          <p>
            When using the default stem, or any other model that has an element
            with a <code>tintindex</code>, set which source should be used for
            the tint. The Tiny Flowers mod currently does not support multiple
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
                {#if hasFileTexture}
                  <th>File</th>
                {/if}
                <th>Identifier</th>
                <th class="delete-column"></th>
                {#if hasFileTexture}
                  <th>Preview</th>
                {/if}
              </tr>
            </thead>
            <tbody>
              {#each flower.blockTextures as entry, i (i)}
                <tr class="texture-item">
                  <td>
                    <label
                      class="visually-hidden"
                      for={`${flower.id}_texture_slot_${i}`}>Texture slot</label
                    >
                    <div class="inline-group">
                      <input
                        type="text"
                        class="text-input"
                        id={`${flower.id}_texture_slot_${i}`}
                        bind:value={entry.slot}
                      />
                    </div>
                  </td>
                  <td>
                    <label
                      class="visually-hidden"
                      for={`${flower.id}_texture_type_${i}`}>Source</label
                    >
                    <div class="inline-group">
                      <select
                        class="button"
                        id={`${flower.id}_texture_type_${i}`}
                        bind:value={
                          () => entry.texture.type,
                          (newType) => {
                            if (newType === "reference") {
                              entry.texture = {
                                type: newType,
                                reference: blockTexturePathForSlot(
                                  flower.id,
                                  entry.slot,
                                ),
                              };
                            } else if (newType === "file") {
                              entry.texture = {
                                type: newType,
                                file: undefined,
                              };
                            }
                          }
                        }
                      >
                        <option value="reference">Existing identifier</option>
                        <option value="file">Upload image</option>
                      </select>
                    </div>
                  </td>
                  {#if hasFileTexture}
                    <td>
                      {#if entry.texture.type === "file"}
                        {@const tex = entry.texture}
                        <label
                          class="visually-hidden"
                          for={`${flower.id}_texture_file_${i}`}>Upload</label
                        >
                        <div class="inline-group">
                          <input
                            type="file"
                            class="visually-hidden"
                            id={`${flower.id}_texture_file_${i}`}
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
                            for={`${flower.id}_texture_file_${i}`}
                          >
                            {#if tex.file?.name}
                              <Image /><span>{tex.file.name}</span>
                            {:else}
                              <ImageUpload /><span>Browse...</span>
                            {/if}
                          </label>
                        </div>
                      {/if}
                    </td>
                  {/if}
                  <td>
                    <label
                      class="visually-hidden"
                      for={`${flower.id}_texture_reference_${i}`}
                      >Identifier</label
                    >
                    <div class="inline-group">
                      {#if entry.texture.type === "file"}
                        <input
                          type="text"
                          class="text-input"
                          id={`${flower.id}_texture_reference_${i}`}
                          disabled
                          value={blockTexturePathForSlot(flower.id, entry.slot)}
                        />
                      {:else if entry.texture.type === "reference"}
                        <input
                          type="text"
                          class="text-input"
                          id={`${flower.id}_texture_reference_${i}`}
                          bind:value={entry.texture.reference}
                        />
                      {/if}
                    </div>
                  </td>
                  <td>
                    <button
                      class="button icon-button color-delete"
                      type="button"
                      onclick={() => removeBlockTexture(flowerIndex, i)}
                    >
                      <Delete />
                    </button>
                  </td>
                  {#if hasFileTexture}
                    <td>
                      {#if entry.texture.type === "file"}
                        <label
                          class="image-preview-label"
                          for={`${flower.id}_texture_file_${i}`}
                        >
                          <ImagePreview
                            file={entry.texture.file}
                            alt={entry.slot}
                          />
                        </label>
                      {/if}
                    </td>
                  {/if}
                </tr>
              {/each}
            </tbody>
          </table>

          <button
            class="button color-add"
            type="button"
            onclick={() => addBlockTexture(flowerIndex)}
          >
            <Add /><span>Add texture mapping</span>
          </button>
        </div>
      </div>
    {/each}

    <button class="button color-add" type="button" onclick={() => addFlower()}>
      <Add /><span>Add new flower</span>
    </button>
  </section>

  <pre>{JSON.stringify(
      formState,
      (key, value) => {
        if (value instanceof File) {
          return `[File: ${value.name}]`;
        }
        return value;
      },
      2,
    )}</pre>
</div>

<style>
  .form-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  label {
    font-weight: 600;
  }

  .input-group-heading {
    font-weight: 600;
    margin: 0;
  }

  .input-group {
    background: #f5f5f5;
    border: 1px solid lightgrey;
    border-radius: 12px;
    padding: 0.5rem;
  }

  button,
  input,
  select {
    font-size: inherit;
    font-family: inherit;
  }

  input[type="text"],
  input[type="number"],
  select,
  .file-input-facade {
    flex-grow: 1;
    width: unset;
  }

  .layout-grid {
    display: grid;
    gap: 1rem;
    overflow-x: hidden;
  }

  .layout-grid > * {
    overflow-x: auto;
  }

  .metadata-section {
    grid-template-areas: "id" "version" "name" "description" "icon" "license" "authors";
    margin-block-start: 1rem;
  }

  .flowers-section {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-block-start: 1.5rem;
  }

  .flower-data-section {
    grid-template-areas:
      "id" "original" "segmented" "item" "translations"
      "survive" "effects" "parent-preset" "parent" "tint" "block";
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

    .flower-data-section {
      grid-template-columns: repeat(2, minmax(0, 1fr));
      grid-template-areas:
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
    .metadata-section {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      grid-template-areas:
        "id version license icon"
        "name name authors icon"
        "description description authors icon";
    }

    .flower-data-section {
      grid-template-columns: repeat(4, minmax(0, 1fr));
      grid-template-areas:
        "id id id id"
        "original original segmented item"
        "translations translations translations translations"
        "survive survive effects effects"
        "parent-preset parent-preset parent tint"
        "block block block block";
    }
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

  .input-list {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding-inline-start: 0;
  }

  .inline-group {
    display: flex;
    gap: 0.5rem;
    align-items: center;
  }

  .block-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    align-items: stretch;
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

  .image-preview-label {
    align-self: center;
  }
</style>
