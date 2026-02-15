export interface FormState {
  stateVersion: 1;
  metadata: ModMetadata;
  flowers: CombinedFlowerData[];
}

export interface ModMetadata {
  id: string;
  version: string;
  name: string;
  description: string;
  authors: string[];
  license: string;
  icon: File | undefined;
}

export interface TextureReference {
  type: "reference";
  reference: string;
}

export interface TextureFile {
  type: "file";
  file: File | undefined;
}

export interface TextureCreate {
  type: "create";
  template: "tiny_flowers" | "pink_petals";
  file: File | undefined;
}

export type TextureType = TextureReference | TextureFile | TextureCreate;

export interface BlockTextureEntry {
  slot: string;
  texture: TextureType;
}

export interface ParentModelPrefix {
  type: "prefix";
  prefix: string;
}

export interface ParentModelCustom {
  type: "custom";
  model1: string;
  model2: string;
  model3: string;
  model4: string;
}

export type ParentModelType = ParentModelPrefix | ParentModelCustom;

export interface CombinedFlowerData {
  id: string;
  name: { language: string; name: string }[];
  originalId: string;
  isSegmented: boolean;
  canSurviveOn: string[];
  suspiciousStewEffects: { id: string; duration: number }[];
  behaviors: unknown[];
  itemTexture: File | undefined;
  tintSource: "grass" | "dry_foliage";
  parentModel: ParentModelType;
  blockTextures: BlockTextureEntry[];
  isExpanded: boolean;
}
