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

export interface CombinedFlowerData {
  id: string;
  name: Record<string, string>;
  originalId: string;
  isSegmented: boolean;
  canSurviveOn: string[];
  suspiciousStewEffects: { id: string; duration: number }[];
  itemTexture: File | undefined;
  tintSource: "grass" | "dry_foliage";
  modelParentBase: string;
  blockTextures: Record<string, File | string | undefined>;
}
