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

export function delay(ms: number, signal: AbortSignal): Promise<void> {
  return new Promise((res, rej) => {
    const handle = window.setTimeout(res, ms);

    signal.addEventListener("abort", () => {
      window.clearTimeout(handle);
      rej();
    });
  });
}
