import type {
  AllFiles,
  BlockModelGeneratedJson,
  ItemsModelDefinitionJson,
} from "./types/files";
import type { CombinedFlowerData, FormState } from "./types/state";

function trimFileName(name: string): string {
  return name.replace(/\.[^\.]+$/, "");
}

function makeBlockModel(
  parentBase: string,
  index: number,
  textureMap: Record<string, string>
): BlockModelGeneratedJson {
  return {
    parent: `${parentBase}_${index}`,
    textures: textureMap,
  };
}

export function convertFormToFiles(state: FormState): AllFiles {
  const modId = state.metadata.id;

  const cases: ItemsModelDefinitionJson["model"]["cases"] = [];

  const value: AllFiles = {
    fabricModJson: {
      schemaVersion: 1,
      id: modId,
      version: state.metadata.version,
      name: state.metadata.name,
      description: state.metadata.description,
      authors: state.metadata.authors,
      license: state.metadata.license,
      icon: `assets/${modId}/icon.png`,
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
          },
        },
      },
    },
  };

  for (const flower of state.flowers) {
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
      item_model: `${modId}:item/${flower.id}`,
      tint_source:
        flower.tintSource === "grass" ? undefined : flower.tintSource,
      model1: `${modId}:block/tiny_flowers/${flower.id}_1`,
      model2: `${modId}:block/tiny_flowers/${flower.id}_2`,
      model3: `${modId}:block/tiny_flowers/${flower.id}_3`,
      model4: `${modId}:block/tiny_flowers/${flower.id}_4`,
    };

    for (const [language, name] of Object.entries(flower.name)) {
      if (!value.assets.lang[language]) {
        value.assets.lang[language] = {};
      }

      value.assets.lang[language][flower.id] = name;
    }

    const textureMap: Record<string, string> = {};
    for (const [key, file] of Object.entries(flower.blockTextures)) {
      if (!file) {
        continue;
      }

      if (typeof file === "string") {
        textureMap[key] = file;
      } else {
        value.assets.textures.block[trimFileName(file.name)] = file;
        textureMap[key] = `${modId}:block/${trimFileName(file.name)}`;
      }
    }

    value.assets.textures.item[flower.id] = flower.itemTexture;

    value.assets.models.block[`${flower.id}_1`] = makeBlockModel(
      flower.modelParentBase,
      1,
      textureMap
    );
    value.assets.models.block[`${flower.id}_2`] = makeBlockModel(
      flower.modelParentBase,
      2,
      textureMap
    );
    value.assets.models.block[`${flower.id}_3`] = makeBlockModel(
      flower.modelParentBase,
      3,
      textureMap
    );
    value.assets.models.block[`${flower.id}_4`] = makeBlockModel(
      flower.modelParentBase,
      4,
      textureMap
    );

    if (flower.itemTexture) {
      value.assets.models.item[flower.id] = {
        parent: "minecraft:item/generated",
        textures: {
          layer0: `${modId}:block/${trimFileName(flower.itemTexture.name)}`,
        },
      };
    }

    cases.push({
      model: { type: "minecraft:model", model: `${modId}:item/${flower.id}` },
      when: `${modId}:${flower.id}`,
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

    const blockTextures: CombinedFlowerData["blockTextures"] = {};
    for (const [textureKey, path] of Object.entries(firstModel.textures)) {
      if (path.startsWith(`${data.id}:block/`)) {
        const name = path.split(":block/")[1];
        const file = files.assets.textures.block[name];
        if (!file) {
          console.warn(
            `Unable to find texture referenced in ${data.id}_1: ${path}. Falling back to string value.`
          );
          blockTextures[textureKey] = path;
        } else {
          blockTextures[textureKey] = file;
        }
      } else {
        blockTextures[textureKey] = path;
      }
    }

    const nameMap: CombinedFlowerData["name"] = { en_us: "<unknown>" };
    for (const [languageKey, values] of Object.entries(files.assets.lang)) {
      if (!values) {
        continue;
      }

      const value = values[data.id];
      if (value) {
        nameMap[languageKey] = value;
      }
    }

    const item: CombinedFlowerData = {
      id: data.id,
      name: nameMap,
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
    };

    state.flowers.push(item);
  }

  return state;
}

export function convertFilesToZip(files: AllFiles): unknown {
  return null;
}

export function convertZipToFiles(zip: unknown): AllFiles {
  return null as any;
}
