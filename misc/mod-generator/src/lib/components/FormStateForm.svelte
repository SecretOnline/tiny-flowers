<script lang="ts">
  import type { FormState } from "../types/state";
  import { blockTexturePathForSlot } from "../util";
  import dirtPackIconUrl from "../../assets/dirt_pack_icon.png";
  import dirtItemTextureUrl from "../../assets/tiny_dirt_item_texture.png";
  import dirtBlockTextureUrl from "../../assets/tiny_dirt_block_texture.png";
  import ImagePreview from "./ImagePreview.svelte";

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
      id: "tiny_flowers:garden",
      slots: ["flowerbed", "stem"],
    },
    {
      value: "garden_double",
      name: "2 layers (default stem)",
      id: "tiny_flowers:garden",
      slots: ["flowerbed", "flowerbed_upper"],
    },
    {
      value: "garden_double_untinted",
      name: "2 layers (custom stem)",
      id: "tiny_flowers:garden",
      slots: ["flowerbed", "flowerbed_upper", "stem"],
    },
    {
      value: "garden_triple",
      name: "3 layers (default stem)",
      id: "tiny_flowers:garden",
      slots: ["flowerbed", "flowerbed_middle", "flowerbed_upper"],
    },
    {
      value: "garden_triple_untinted",
      name: "3 layers (custom stem)",
      id: "tiny_flowers:garden",
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
          id: "tiny_dirt",
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
      texture: { type: "reference", reference: "" },
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
  <button type="button" onclick={() => setToDirtFlower()}
    >Load example pack</button
  >

  <h2>
    {formState.metadata.name || "New mod"} (v{formState.metadata.version ??
      "???"})
  </h2>
  <section class="metadata-section">
    <div class="block-group metadata-id">
      <label for="mod-id">Mod ID</label>
      <div class="inline-group">
        <input
          type="text"
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
          id="mod-icon"
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
      </div>

      <ImagePreview
        file={formState.metadata.icon}
        alt={formState.metadata.name}
      />
    </div>

    <div class="block-group metadata-license">
      <label for="mod-license">License</label>
      <div class="inline-group">
        <input
          type="text"
          id="mod-license"
          bind:value={formState.metadata.license}
        />
      </div>
    </div>

    <fieldset class="inline-group metadata-authors">
      <legend>Authors</legend>
      <ul class="input-list">
        {#each formState.metadata.authors as author, i (i)}
          <li class="input-list-item inline-group">
            <label class="visually-hidden" for={`author_${i}`}>Name</label>
            <input
              type="text"
              id={`author_${i}`}
              bind:value={formState.metadata.authors[i]}
            />
            <button type="button" onclick={() => removeAuthor(i)}>Remove</button
            >
          </li>
        {/each}
        <button type="button" onclick={addAuthor}>Add Author</button>
      </ul>
    </fieldset>
  </section>

  <section class="flowers-section">
    <h2>Flowers</h2>

    {#each formState.flowers as flower, flowerIndex (flowerIndex)}
      {@const hasFileTexture = flower.blockTextures.some(
        (entry) => entry.texture.type === "file",
      )}
      <div class="flower-item">
        <div class="flower-data-section">
          <div class="block-group flower-data-id">
            <label for="flower-id-{flowerIndex}">ID:</label>
            <div class="inline-group">
              <input
                type="text"
                id="flower-id-{flowerIndex}"
                bind:value={flower.id}
                placeholder="tiny_flower_id"
              />
            </div>
          </div>

          <div class="inline-group flower-data-remove">
            <button type="button" onclick={() => removeFlower(flowerIndex)}
              >Remove Flower</button
            >
          </div>

          <div class="block-group flower-data-original">
            <label for="original-id-{flowerIndex}">Original ID:</label>
            <div class="inline-group">
              <input
                type="text"
                id="original-id-{flowerIndex}"
                bind:value={flower.originalId}
                placeholder="flower_id"
              />
            </div>
            <p>
              When Florists' Shears are used on this block, it will be turned
              into Tiny Flowers.
            </p>
          </div>

          <div class="block-group flower-data-segmented">
            <label for="is-segmented-{flowerIndex}">Is Segmented</label>
            <div class="inline-group">
              <input
                type="checkbox"
                id="is-segmented-{flowerIndex}"
                bind:checked={flower.isSegmented}
              />
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
                id="item-texture-{flowerIndex}"
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
            </div>

            <ImagePreview
              file={flower.itemTexture}
              alt={flower.name.find((e) => e.language === "en_us")?.name ??
                flower.id}
            />
          </div>

          <fieldset class="block-group flower-data-translations">
            <legend>Translations</legend>
            <table class="input-table">
              <thead>
                <tr>
                  <th>Language ID</th>
                  <th>Flower Name</th>
                  <th></th>
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
                      <input
                        type="text"
                        id={`${flower.id}_lang_id_${entry.language}`}
                        bind:value={flower.name[i].language}
                      />
                    </td>
                    <td>
                      <label
                        class="visually-hidden"
                        for={`${flower.id}_lang_name_${entry.language}`}
                        >Flower Name</label
                      >
                      <input
                        type="text"
                        id={`${flower.id}_lang_name_${entry.language}`}
                        bind:value={flower.name[i].name}
                        placeholder={entry.language.startsWith("en_")
                          ? "Tiny ..."
                          : undefined}
                      />
                    </td>
                    <td>
                      <button
                        type="button"
                        onclick={() => removeFlowerName(flowerIndex, i)}
                        >Remove</button
                      >
                    </td>
                  </tr>
                {/each}
              </tbody>
            </table>
            <div class="input-list">
              <button type="button" onclick={() => addFlowerName(flowerIndex)}
                >Add Language</button
              >
            </div>
          </fieldset>

          <fieldset class="block-group flower-data-survive">
            <legend>Can Survive On</legend>
            <ul class="input-list">
              {#each flower.canSurviveOn as block, i (i)}
                <li class="input-list-item inline-group">
                  <input type="text" bind:value={flower.canSurviveOn[i]} />
                  <button
                    type="button"
                    onclick={() => removeSurvivalBlock(flowerIndex, i)}
                    >Remove</button
                  >
                </li>
              {/each}
            </ul>

            <div class="inline-group">
              <button
                type="button"
                onclick={() => addSurvivalBlock(flowerIndex)}
                >Add Block or Tag</button
              >
              <button
                type="button"
                onclick={() =>
                  addSurvivalBlock(
                    flowerIndex,
                    "#tiny_flowers:tiny_flower_can_survive_on",
                  )}>Add Default</button
              >
            </div>
          </fieldset>

          <fieldset class="block-group flower-data-effects">
            <legend>Suspicious Stew Effects:</legend>

            <table class="input-table">
              <thead>
                <tr>
                  <th>Effect ID</th>
                  <th>Duration (ticks)</th>
                  <th></th>
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
                      <input
                        type="text"
                        id={`${flower.id}_effect_id_${i}`}
                        bind:value={effect.id}
                        placeholder="minecraft:effect_id"
                      />
                    </td>
                    <td>
                      <label
                        class="visually-hidden"
                        for={`${flower.id}_effect_duration_${i}`}
                        >Duration (ticks)</label
                      >
                      <input
                        type="number"
                        id={`${flower.id}_effect_duration_${i}`}
                        bind:value={effect.duration}
                      />
                    </td>
                    <td>
                      <button
                        type="button"
                        onclick={() => removeEffect(flowerIndex, i)}
                        >Remove</button
                      >
                    </td>
                  </tr>
                {/each}
              </tbody>
            </table>

            <button type="button" onclick={() => addEffect(flowerIndex)}
              >Add Effect</button
            >
          </fieldset>

          <div class="block-group flower-data-parent-preset">
            <label for="model-parent-preset-{flowerIndex}"
              >Model Parent Preset</label
            >
            <p>
              The Tiny Flowers mod provides a set of built-in models that only
              need the upwards facing textures defined. In general, try to use
              the fewest number of layers while still getting the essence of the
              original flower across.
            </p>
            <div class="preset-button-grid">
              {#each PREDEFINED_BLOCK_MODELS as model}
                <button
                  type="button"
                  value={model.value}
                  onclick={() => {
                    flower.modelParentBase = model.id;
                    flower.blockTextures = model.slots.map(
                      (slot) =>
                        flower.blockTextures.find((t) => t.slot === slot) ?? {
                          slot,
                          texture: { type: "file", file: undefined },
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
                id="model-parent-base-{flowerIndex}"
                bind:value={flower.modelParentBase}
              />
            </div>
          </div>

          <div class="block-group flower-data-tint">
            <label for="tint-source-{flowerIndex}">Tint Source</label>
            <div class="inline-group">
              <select
                id="tint-source-{flowerIndex}"
                bind:value={flower.tintSource}
              >
                <option value="grass">Grass</option>
                <option value="dry_foliage">Dry Foliage</option>
              </select>
            </div>
          </div>

          <fieldset class="block-group flower-data-block">
            <legend>Block Textures:</legend>

            <table class="input-table">
              <thead>
                <tr>
                  <th>Texture Slot</th>
                  <th>Type</th>
                  {#if hasFileTexture}
                    <th>File</th>
                  {/if}
                  <th>Identifier</th>
                  <th></th>
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
                        for={`${flower.id}_texture_slot_${i}`}
                        >Texture slot</label
                      >
                      <input
                        type="text"
                        id={`${flower.id}_texture_slot_${i}`}
                        bind:value={entry.slot}
                      />
                    </td>
                    <td>
                      <label
                        class="visually-hidden"
                        for={`${flower.id}_texture_type_${i}`}>Type</label
                      >
                      <select
                        id={`${flower.id}_texture_type_${i}`}
                        bind:value={
                          () => entry.texture.type,
                          (newType) => {
                            if (newType === "reference") {
                              entry.texture = {
                                type: newType,
                                reference: blockTexturePathForSlot(
                                  formState.metadata.id,
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
                        <option value="reference">Identifier</option>
                        <option value="file">Upload</option>
                      </select>
                    </td>
                    {#if hasFileTexture}
                      <td>
                        {#if entry.texture.type === "file"}
                          {@const tex = entry.texture}
                          <label
                            class="visually-hidden"
                            for={`${flower.id}_texture_file_${i}`}>Upload</label
                          >
                          <input
                            type="file"
                            id={`${flower.id}_texture_file_${i}`}
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
                        {/if}
                      </td>
                    {/if}
                    <td>
                      <label
                        class="visually-hidden"
                        for={`${flower.id}_texture_reference_${i}`}
                        >Identifier</label
                      >
                      {#if entry.texture.type === "file"}
                        <input
                          type="text"
                          id={`${flower.id}_texture_reference_${i}`}
                          disabled
                          value={blockTexturePathForSlot(
                            formState.metadata.id,
                            flower.id,
                            entry.slot,
                          )}
                        />
                      {:else if entry.texture.type === "reference"}
                        <input
                          type="text"
                          id={`${flower.id}_texture_reference_${i}`}
                          bind:value={entry.texture.reference}
                        />
                      {/if}
                    </td>
                    <td>
                      <button
                        type="button"
                        onclick={() => removeBlockTexture(flowerIndex, i)}
                        >Remove</button
                      >
                    </td>
                    {#if hasFileTexture}
                      <td>
                        {#if entry.texture.type === "file"}
                          <ImagePreview
                            file={entry.texture.file}
                            alt={entry.slot}
                          />
                        {/if}
                      </td>
                    {/if}
                  </tr>
                {/each}
              </tbody>
            </table>

            <button type="button" onclick={() => addBlockTexture(flowerIndex)}
              >Add Texture</button
            >
          </fieldset>
        </div>
      </div>
    {/each}

    <button type="button" onclick={addFlower} class="add-flower-btn"
      >Add Flower</button
    >
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

  button,
  input,
  select {
    font-size: inherit;
    font-family: inherit;
  }

  input[type="text"] {
    flex-grow: 1;
    width: unset;
  }

  .metadata-section {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 1rem;

    grid-template-areas:
      "id version license icon"
      "name name authors icon"
      "description description authors icon";

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
  }

  .flower-data-section {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 1rem;

    grid-template-areas:
      "id id id remove"
      "original original segmented item"
      "translations translations translations translations"
      "survive survive effects effects"
      "parent-preset parent-preset parent tint"
      "block block block block";

    .flower-data-id {
      grid-area: id;
    }
    .flower-data-remove {
      grid-area: remove;
      justify-content: end;
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
    align-items: baseline;
  }

  .block-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    align-items: stretch;
  }

  .input-table {
    width: 100%;
  }

  .preset-button-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 0.5rem;
  }
</style>
