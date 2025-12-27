export interface AllFiles {
  fabricModJson: FabricModJson;
  data: {
    tinyFlowers: Record<string, TinyFlowerDataJson | undefined>;
  };
  assets: {
    icon: File | undefined;
    tinyFlowers: Record<string, TinyFlowerResourcesJson | undefined>;
    lang: Record<string, LanguageJson | undefined>;
    textures: {
      block: Record<string, File | undefined>;
      item: Record<string, File | undefined>;
    };
    models: {
      block: Record<string, BlockModelGeneratedJson | undefined>;
      item: Record<string, ItemModelGeneratedJson | undefined>;
    };
    items: Record<"tiny_flower", ItemsModelDefinitionJson | undefined>;
  };
}

export interface FabricModJson {
  schemaVersion: 1;
  id: string;
  version: string;
  name: string;
  description: string;
  authors: string[];
  license: string;
  icon: string;
  environment: string;
  entrypoints: {
    main: string[];
    client: string[];
  };
  depends: {
    tiny_flowers: string;
  };
  custom: {
    modmenu: {
      parent: "tiny_flowers";
    };
  };
}

export interface TinyFlowerDataJson {
  id: string;
  original_id: string;
  is_segmented?: boolean;
  can_survive_on?: string[];
  suspicious_stew_effects?: {
    id: string;
    duration: number;
  }[];
}

export interface TinyFlowerResourcesJson {
  id: string;
  item_model: string;
  tint_source?: "grass" | "dry_foliage";
  model1: string;
  model2: string;
  model3: string;
  model4: string;
}

export interface LanguageJson {
  [key: string]: string;
}

export interface ItemsModelDefinitionJson {
  model: {
    type: "minecraft:select";
    cases: {
      model: {
        type: "minecraft:model";
        model: string;
      };
      when: string;
    }[];
    property: "tiny_flowers:tiny_flower";
  };
}

export interface ItemModelGeneratedJson {
  parent: "minecraft:item/generated";
  textures: {
    layer0: string;
  };
}

export interface BlockModelGeneratedJson {
  parent: string;
  textures: Record<string, string>;
}
