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

interface HSL {
  h: number;
  s: number;
  l: number;
}

interface RGB {
  r: number;
  g: number;
  b: number;
}

export function toHsl(r: number, g: number, b: number): HSL {
  // Hue calculation adapted from:
  // https://gist.github.com/arenagroove/ffec49d5322245c71d6fc7867066c5eb

  const rf = r / 255;
  const gf = g / 255;
  const bf = b / 255;

  let h = 0;
  const max = Math.max(rf, gf, bf);
  const min = Math.min(rf, gf, bf);
  const delta = max - min;
  if (delta !== 0) {
    if (max === rf) h = ((gf - bf) / delta) % 6;
    else if (max === gf) h = (bf - rf) / delta + 2;
    else if (max === bf) h = (rf - gf) / delta + 4;

    h *= 60;
    if (h < 0) h += 360;
  }

  h = parseFloat(h.toFixed(2));

  let l = (max + min) / 2;
  let s = delta === 0 ? 0 : delta / (1 - Math.abs(2 * l - 1));

  return { h, s, l };
}

export function packRgb(r: number, g: number, b: number): number {
  return (r << 16) + (g << 8) + b;
}

export function unpackRgb(packed: number): RGB {
  return {
    r: (packed & 0xff0000) >> 16,
    g: (packed & 0xff00) >> 8,
    b: packed & 0xff,
  };
}

function hslDistance(hsl1: HSL, hsl2: HSL): number {
  let hDist = Math.abs(hsl1.h - hsl2.h);
  if (hDist > 180) hDist = 360 - hDist;

  return Math.sqrt(
    (hDist * 0.5) ** 2 +
      (hsl1.s - hsl2.s) ** 2 * 100 +
      (hsl1.l - hsl2.l) ** 2 * 100
  );
}

export function clusterColors(
  colors: number[],
  threshold: number = 30
): number[] {
  const clusteredAverages: number[] = [];
  const convertedMap = new Map<number, { rgb: RGB; hsl: HSL }>();

  for (const packed of colors) {
    const rgb = unpackRgb(packed);
    convertedMap.set(packed, { rgb, hsl: toHsl(rgb.r, rgb.g, rgb.b) });
  }

  const used = new Set<number>();

  for (const color of colors) {
    if (used.has(color)) continue;

    const { rgb: rgb1, hsl: hsl1 } = convertedMap.get(color)!;

    let sumR = rgb1.r,
      sumG = rgb1.g,
      sumB = rgb1.b;
    let count = 1;

    for (const otherColor of colors) {
      if (used.has(otherColor) || otherColor === color) continue;

      const { rgb: rgb2, hsl: hsl2 } = convertedMap.get(otherColor)!;

      if (hslDistance(hsl1, hsl2) < threshold) {
        sumR += rgb2.r;
        sumG += rgb2.g;
        sumB += rgb2.b;
        count++;
        used.add(otherColor);
      }
    }

    const avgColor = packRgb(
      Math.round(sumR / count),
      Math.round(sumG / count),
      Math.round(sumB / count)
    );
    clusteredAverages.push(avgColor);
    used.add(color);
  }

  const finalColors: number[] = [];
  for (const ave of clusteredAverages) {
    let closest = Infinity;
    let closestDistance = Infinity;

    const averageRgb = unpackRgb(ave);
    const averageHsl = toHsl(averageRgb.r, averageRgb.g, averageRgb.b);

    for (const color of colors) {
      const { rgb: rgb1, hsl } = convertedMap.get(color)!;
      const distance = hslDistance(averageHsl, hsl);

      if (distance < closestDistance) {
        closest = color;
        closestDistance = distance;
      }
    }

    if (closestDistance < Infinity) {
      finalColors.push(closest);
    }
  }

  return finalColors;
}
