<script lang="ts">
  import examplePackUrl from "../../assets/example_1.0.0.jar?url";
  import {
    convertFilesToForm,
    convertFilesToZip,
    convertFormToFiles,
    convertZipToFiles,
  } from "../conversion";
  import type { FormState } from "../types/state";
  import FlowerSection from "./form/FlowerSection.svelte";
  import MetadataSection from "./form/MetadataSection.svelte";
  import Add from "./icons/Add.svelte";
  import Cube from "./icons/Cube.svelte";
  import Download from "./icons/Download.svelte";
  import Upload from "./icons/Upload.svelte";

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
        parentModel: { type: "prefix", prefix: "" },
        blockTextures: [],
        isExpanded: true,
      },
    ],
  });

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
      parentModel: { type: "prefix", prefix: "" },
      blockTextures: [],
      isExpanded: true,
    });
  }

  function removeFlower(index: number) {
    formState.flowers.splice(index, 1);
  }

  async function downloadJar() {
    const snapshot = $state.snapshot(formState);

    const files = convertFormToFiles(snapshot);
    const zip = await convertFilesToZip(files);

    const url = URL.createObjectURL(zip);

    const link = document.createElement("a");
    link.download = zip.name;
    link.href = url;

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(url);
  }

  async function uploadJar(file: File) {
    if (
      ![
        "application/java-archive",
        "application/jar",
        "application/zip",
        "application/x-zip-compressed",
      ].includes(file.type)
    ) {
      return;
    }

    const asZip = new File([file], "input.zip", { type: "application/zip" });

    const files = await convertZipToFiles(asZip);
    const newState = convertFilesToForm(files);
    formState = newState;
  }

  async function setToExamplePack() {
    const jar = await fetch(examplePackUrl)
      .then((response) => response.blob())
      .then(
        (blob) =>
          new File([blob], "example_1.0.0.jar", { type: "application/jar" }),
      );

    await uploadJar(jar);
  }
</script>

<div class="form-container">
  <h1>Tiny Flower Pack Generator</h1>
  <div class="block-group">
    <p>
      Create packs of tiny flowers for the <a
        href="https://modrinth.com/mod/tiny-flowers"
        target="_blank"
        rel="noopener noreferrer external">Tiny Flowers</a
      >
      mod. If you need even more customisation, check out the "For other mod developers"
      section of the
      <a
        href="https://github.com/SecretOnline/tiny-flowers"
        target="_blank"
        rel="noopener noreferrer">README on GitHub</a
      > for documentation on the JSON files the mod uses.
    </p>
    <p>
      All processing happens in your browser, no data is sent anywhere. If you
      need to save your progress, just download a .jar file and upload it again
      to restore.
    </p>
    <div class="inline-group">
      <input
        type="file"
        class="visually-hidden"
        id="upload"
        accept=".jar,.zip"
        onchange={(event) => {
          const file = event.currentTarget.files?.[0];
          if (file) {
            uploadJar(file);
          }
        }}
      />
      <label
        class="file-input-facade button color-add upload-button"
        for="upload"
      >
        <Upload />
        <span>Upload .jar</span>
      </label>
      <button
        class="button color-add"
        type="button"
        onclick={() => downloadJar()}
      >
        <Download />
        <span>Download .jar</span>
      </button>
      <button class="button" type="button" onclick={() => setToExamplePack()}>
        <Cube />
        <span>Load example pack</span>
      </button>
    </div>
  </div>

  <h2>
    {formState.metadata.name || "New mod"} (v{formState.metadata.version ??
      "???"})
  </h2>

  <MetadataSection bind:metadata={formState.metadata} />

  <section class="flowers-section">
    <div class="inline-group">
      <h3>Flowers</h3>
    </div>

    {#each formState.flowers, flowerIndex (flowerIndex)}
      <FlowerSection
        bind:flower={formState.flowers[flowerIndex]}
        onRemove={() => removeFlower(flowerIndex)}
      />
    {/each}

    <button class="button color-add" type="button" onclick={() => addFlower()}>
      <Add /><span>Add new flower</span>
    </button>
  </section>
</div>

<style>
  .form-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .flowers-section {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-block-start: 1.5rem;
  }

  .upload-button {
    flex-grow: 0;
  }
</style>
