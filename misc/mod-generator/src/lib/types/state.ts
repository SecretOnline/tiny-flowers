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

export type TextureType = TextureReference | TextureFile;

export interface CombinedFlowerData {
  id: string;
  name: { language: string; name: string }[];
  originalId: string;
  isSegmented: boolean;
  canSurviveOn: string[];
  suspiciousStewEffects: { id: string; duration: number }[];
  itemTexture: File | undefined;
  tintSource: "grass" | "dry_foliage";
  modelParentBase: string;
  blockTextures: { slot: string; texture: TextureType }[];
}
