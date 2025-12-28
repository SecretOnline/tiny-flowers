import JSZip from "jszip";
import type {
  AllFiles,
  BlockModelGeneratedJson,
  ItemsModelDefinitionJson,
} from "./types/files";
import type { CombinedFlowerData, FormState, TextureType } from "./types/state";
import {
  identifierNamespace,
  identifierPath,
  identifierPathFinal,
} from "./util";

function trimFileName(name: string): string {
  return name.replace(/\.[^\.]+$/, "");
}

export function convertFormToFiles(state: FormState): AllFiles {
  const cases: ItemsModelDefinitionJson["model"]["cases"] = [];

  const value: AllFiles = {
    fabricModJson: {
      schemaVersion: 1,
      id: state.metadata.id,
      version: state.metadata.version,
      name: state.metadata.name,
      description: state.metadata.description,
      authors: state.metadata.authors,
      license: state.metadata.license,
      icon: `assets/${state.metadata.id}/icon.png`,
      environment: "*",
      entrypoints: { client: [], main: [] },
      depends: { tiny_flowers: ">=2.0.0" },
      custom: { modmenu: { parent: "tiny_flowers" } },
    },
    data: {
      tinyFlowers: {},
    },
    assets: {
      icon: state.metadata.icon,
      tinyFlowers: {},
      lang: {},
      textures: {
        block: {},
        item: {},
      },
      models: {
        block: {},
        item: {},
      },
      items: {
        tiny_flower: {
          model: {
            type: "minecraft:select",
            cases,
            property: "tiny_flowers:tiny_flower",
            fallback: {
              type: "minecraft:model",
              model: "tiny_flowers:item/tiny_garden",
            },
          },
        },
      },
    },
  };

  for (const flower of state.flowers) {
    const flowerNamespace = identifierNamespace(flower.id);
    const flowerPath = identifierPath(flower.id);

    value.data.tinyFlowers[flower.id] = {
      id: flower.id,
      original_id: flower.originalId,
      can_survive_on:
        flower.canSurviveOn.length === 1 &&
        flower.canSurviveOn[0] === "#tiny_flowers:tiny_flower_can_survive_on"
          ? undefined
          : flower.canSurviveOn,
      is_segmented: flower.isSegmented ? true : undefined,
      suspicious_stew_effects:
        flower.suspiciousStewEffects.length === 0
          ? undefined
          : flower.suspiciousStewEffects,
    };

    value.assets.tinyFlowers[flower.id] = {
      id: flower.id,
      item_model: `${flowerNamespace}:item/${flowerPath}`,
      tint_source:
        flower.tintSource === "grass" ? undefined : flower.tintSource,
      model1: `${flowerNamespace}:block/tiny_flowers/${flowerPath}_1`,
      model2: `${flowerNamespace}:block/tiny_flowers/${flowerPath}_2`,
      model3: `${flowerNamespace}:block/tiny_flowers/${flowerPath}_3`,
      model4: `${flowerNamespace}:block/tiny_flowers/${flowerPath}_4`,
    };

    const flowerLangKey = `block.${flowerNamespace}.${flowerPath}`;
    for (const { language, name } of flower.name) {
      if (!value.assets.lang[language]) {
        value.assets.lang[language] = {};
      }

      value.assets.lang[language][flowerLangKey] = name;
    }

    const textureMap: Record<string, string> = {};
    for (const { slot, texture } of flower.blockTextures) {
      if (texture.type === "reference") {
        textureMap[slot] = texture.reference;
      } else if (texture.type === "file") {
        if (!texture.file) {
          continue;
        }

        const textureIdentifier = `${flowerNamespace}:block/${trimFileName(
          texture.file.name
        )}`;
        value.assets.textures.block[textureIdentifier] = texture.file;
        textureMap[slot] = textureIdentifier;
      }
    }

    function blockModelId(index: number) {
      return `${flowerNamespace}:block/tiny_flowers/${flowerPath}_${index}`;
    }
    function blockModelContent(index: number): BlockModelGeneratedJson {
      return {
        parent: `${flower.modelParentBase}_${index}`,
        textures: textureMap,
      };
    }
    value.assets.models.block[blockModelId(1)] = blockModelContent(1);
    value.assets.models.block[blockModelId(2)] = blockModelContent(2);
    value.assets.models.block[blockModelId(3)] = blockModelContent(3);
    value.assets.models.block[blockModelId(4)] = blockModelContent(4);

    if (flower.itemTexture) {
      value.assets.textures.item[flower.id] = flower.itemTexture;
      value.assets.models.item[flower.id] = {
        parent: "minecraft:item/generated",
        textures: {
          layer0: `${flowerNamespace}:item/${trimFileName(
            flower.itemTexture.name
          )}`,
        },
      };
    }

    cases.push({
      model: {
        type: "minecraft:model",
        model: `${flowerNamespace}:item/${flowerPath}`,
      },
      when: `${flowerNamespace}:${flowerPath}`,
    });
  }

  return value;
}

export function convertFilesToForm(files: AllFiles): FormState {
  var manifest = files.fabricModJson;

  var state: FormState = {
    stateVersion: 1,
    metadata: {
      id: manifest.id,
      version: manifest.version,
      name: manifest.name,
      description: manifest.description,
      authors: manifest.authors,
      license: manifest.license,
      icon: files.assets.icon,
    },
    flowers: [],
  };

  for (const data of Object.values(files.data.tinyFlowers)) {
    if (!data) {
      continue;
    }

    const resources = files.assets.tinyFlowers[data.id];
    if (!resources) {
      throw new Error(
        `Flower ${data.id} has no resources definition (Tried to find assets/${manifest.id}/tiny_flowers/tiny_flower/${data.id}.json)`
      );
    }

    const firstModel = files.assets.models.block[`${data.id}_1`];
    if (!firstModel) {
      throw new Error(
        `Flower ${data.id} has no block model defined (Tried to find assets/${manifest.id}/models/block/tiny_flowers/${data.id}_1.json)`
      );
    }

    const blockTextures: CombinedFlowerData["blockTextures"] = [];
    function setTextureForSlot(slot: string, texture: TextureType) {
      const existingItem = blockTextures.find((entry) => entry.slot === slot);
      if (!existingItem) {
        blockTextures.push({ slot, texture });
      } else {
        existingItem.texture = texture;
      }
    }
    for (const [slot, path] of Object.entries(firstModel.textures)) {
      if (path.startsWith(`${data.id}:block/`)) {
        const name = path.split(":block/")[1];
        const file = files.assets.textures.block[name];
        if (!file) {
          console.warn(
            `Unable to find texture referenced in ${data.id}_1: ${path}. Falling back to string value.`
          );
          setTextureForSlot(slot, { type: "reference", reference: path });
        } else {
          setTextureForSlot(slot, { type: "file", file });
        }
      } else {
        setTextureForSlot(slot, { type: "reference", reference: path });
      }
    }

    const nameList: CombinedFlowerData["name"] = [
      { language: "en_us", name: "" },
    ];
    function setItemNameForLanguage(language: string, name: string) {
      const existingItem = nameList.find(
        (entry) => entry.language === language
      );
      if (!existingItem) {
        nameList.push({ language, name });
      } else {
        existingItem.name = name;
      }
    }
    for (const [languageKey, values] of Object.entries(files.assets.lang)) {
      if (!values) {
        continue;
      }

      const value = values[data.id];
      if (value) {
        setItemNameForLanguage(languageKey, value);
      }
    }

    const item: CombinedFlowerData = {
      id: data.id,
      name: nameList,
      originalId: data.original_id,
      isSegmented: data.is_segmented ?? false,
      canSurviveOn: data.can_survive_on ?? [
        "#tiny_flowers:tiny_flower_can_survive_on",
      ],
      suspiciousStewEffects: data.suspicious_stew_effects ?? [],
      tintSource: resources.tint_source ?? "grass",
      itemTexture: files.assets.textures.item[data.id],
      modelParentBase: firstModel.parent.replace(/_\d+$/, ""),
      blockTextures,
      isExpanded: false,
    };

    state.flowers.push(item);
  }

  return state;
}

function appendJson(zip: JSZip, json: object, fileName: string) {
  const str = JSON.stringify(json, null, 2);
  const file = new File([str], fileName, { type: "application/json" });
  zip.file(fileName, file);
}

function appendFile(zip: JSZip, file: File) {
  zip.file(file.name, file);
}

export async function convertFilesToZip(files: AllFiles): Promise<File> {
  const zip = new JSZip();

  appendJson(zip, files.fabricModJson, "fabric.mod.json");

  const dirCache: Record<
    | "flowerData"
    | "assets"
    | "items"
    | "lang"
    | "flowerResources"
    | "blockModel"
    | "itemModel"
    | "blockTexture"
    | "itemTexture",
    Record<string, JSZip>
  > = {
    flowerData: {},
    assets: {},
    items: {},
    lang: {},
    flowerResources: {},
    blockModel: {},
    itemModel: {},
    blockTexture: {},
    itemTexture: {},
  };
  function getZipDir(key: keyof typeof dirCache, namespace: string): JSZip {
    if (!dirCache[key][namespace]) {
      let fullPath: string;
      switch (key) {
        case "flowerData":
          fullPath = `data/${namespace}/tiny_flowers/tiny_flower`;
          break;
        case "flowerResources":
          fullPath = `assets/${namespace}/tiny_flowers/tiny_flower`;
          break;
        case "assets":
          fullPath = `assets/${namespace}`;
          break;
        case "items":
          fullPath = `assets/${namespace}/items`;
          break;
        case "lang":
          fullPath = `assets/${namespace}/lang`;
          break;
        case "blockModel":
          fullPath = `assets/${namespace}/models/block/tiny_flowers`;
          break;
        case "itemModel":
          fullPath = `assets/${namespace}/models/item`;
          break;
        case "blockTexture":
          fullPath = `assets/${namespace}/textures/block`;
          break;
        case "itemTexture":
          fullPath = `assets/${namespace}/textures/item`;
          break;
      }

      dirCache[key][namespace] = zip.folder(fullPath)!;
    }

    return dirCache[key][namespace];
  }

  function appendIdentifiedJson(
    key: keyof typeof dirCache,
    identifier: string,
    json: object
  ) {
    appendJson(
      getZipDir(key, identifierNamespace(identifier)),
      json,
      `${identifierPathFinal(identifier)}.json`
    );
  }

  for (const [identifier, flowerData] of Object.entries(
    files.data.tinyFlowers
  )) {
    if (!flowerData) {
      continue;
    }

    appendIdentifiedJson("flowerData", identifier, flowerData);
  }

  if (files.assets.icon) {
    appendFile(getZipDir("assets", files.fabricModJson.id), files.assets.icon);
  }

  if (files.assets.items.tiny_flower) {
    appendJson(
      getZipDir("items", files.fabricModJson.id),
      files.assets.items.tiny_flower,
      "tiny_flower.json"
    );
  }

  for (const [key, languageData] of Object.entries(files.assets.lang)) {
    if (!languageData) {
      continue;
    }

    appendJson(
      getZipDir("lang", files.fabricModJson.id),
      languageData,
      `${key}.json`
    );
  }

  for (const [identifier, flowerResources] of Object.entries(
    files.assets.tinyFlowers
  )) {
    if (!flowerResources) {
      continue;
    }

    appendIdentifiedJson("flowerResources", identifier, flowerResources);
  }

  for (const [identifier, model] of Object.entries(files.assets.models.block)) {
    if (!model) {
      continue;
    }

    appendIdentifiedJson("blockModel", identifier, model);
  }

  for (const [identifier, model] of Object.entries(files.assets.models.item)) {
    if (!model) {
      continue;
    }

    appendIdentifiedJson("itemModel", identifier, model);
  }

  for (const [identifier, textureFile] of Object.entries(
    files.assets.textures.block
  )) {
    if (!textureFile) {
      continue;
    }

    appendFile(
      getZipDir("blockTexture", identifierNamespace(identifier)),
      textureFile
    );
  }

  for (const [identifier, textureFile] of Object.entries(
    files.assets.textures.item
  )) {
    if (!textureFile) {
      continue;
    }

    appendFile(
      getZipDir("itemTexture", identifierNamespace(identifier)),
      textureFile
    );
  }

  const exportBlob = await zip.generateAsync({ type: "blob" });
  const exportFile = new File(
    [exportBlob],
    `${files.fabricModJson.id}_${files.fabricModJson.version}.jar`,
    { type: "application/json" }
  );

  return exportFile;
}

export function convertZipToFiles(zip: unknown): AllFiles {
  return null as any;
}
