export function blockTexturePathForSlot(blockId: string, slot: string) {
  const slotSuffix = slot.startsWith("flowerbed")
    ? slot.replace(/^flowerbed/, "")
    : `_${slot}`;

  const blockNamespace = blockId.replace(/:.*$/, "");
  const blockPath = blockId.replace(/^.*:/, "");

  return `${blockNamespace}:block/${blockPath}${slotSuffix}`;
}
