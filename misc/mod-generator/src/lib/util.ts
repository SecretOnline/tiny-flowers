export function blockTexturePathForSlot(
  modId: string,
  blockId: string,
  slot: string
) {
  const slotSuffix = slot.startsWith("flowerbed")
    ? slot.replace(/^flowerbed/, "")
    : `_${slot}`;

  return `${modId}:block/${blockId}${slotSuffix}`;
}
