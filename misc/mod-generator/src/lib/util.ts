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

  return `${identifierNamespace(blockId)}:block/${identifierPathFinal(
    blockId
  )}${slotSuffix}`;
}
