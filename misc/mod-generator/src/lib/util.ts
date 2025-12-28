export function identifierNamespace(identifier: string): string {
  return identifier.replace(/:.*$/, "");
}
export function identifierPath(identifier: string): string {
  return identifier.replace(/^.*:/, "");
}
export function identifierPathFinal(identifier: string): string {
  return identifier.replace(/^.*[:\/]/, "");
}

export function blockTexturePathForSlot(blockId: string, slot: string): string {
  const slotSuffix = slot.startsWith("flowerbed")
    ? slot.replace(/^flowerbed/, "")
    : `_${slot}`;

  const blockNamespace = blockId.replace(/:.*$/, "");
  const blockPath = blockId.replace(/^.*:/, "");

  return `${blockNamespace}:block/${blockPath}${slotSuffix}`;
}
