export interface HSL {
  h: number;
  s: number;
  l: number;
}

export interface RGB {
  r: number;
  g: number;
  b: number;
}

function toHsl(r: number, g: number, b: number): HSL {
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

function packRgb(r: number, g: number, b: number): number {
  return (r << 16) + (g << 8) + b;
}

function unpackRgb(packed: number): RGB {
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
      (hsl1.l - hsl2.l) ** 2 * 100,
  );
}

export function clusterColors(
  colors: number[],
  threshold: number = 30,
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
      Math.round(sumB / count),
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

export async function extractSwatches(itemTexture: File): Promise<string[]> {
  const itemTextureBitmap = await window.createImageBitmap(itemTexture);

  const canvas = new OffscreenCanvas(
    itemTextureBitmap.width,
    itemTextureBitmap.height,
  );
  const ctx = canvas.getContext("2d");
  if (!ctx) {
    throw new Error("Unable to get canvas context");
  }

  ctx.drawImage(
    itemTextureBitmap,
    0,
    0,
    itemTextureBitmap.width,
    itemTextureBitmap.height,
  );

  const imageData = ctx.getImageData(
    0,
    0,
    itemTextureBitmap.width,
    itemTextureBitmap.height,
    { colorSpace: "srgb" },
  );

  const colorSet = new Set<number>();
  for (let row = 0; row < itemTextureBitmap.height; row++) {
    for (let column = 0; column < itemTextureBitmap.width; column++) {
      const startIndex = (row * itemTextureBitmap.width + column) * 4;
      const r = imageData.data[startIndex];
      const g = imageData.data[startIndex + 1];
      const b = imageData.data[startIndex + 2];
      const a = imageData.data[startIndex + 3];

      if (a === 0) {
        // Pixel is transparent, skip.
        continue;
      }

      const hsl = toHsl(r, g, b);
      if (hsl.h > 75 && hsl.h < 155 && hsl.s > 0.4) {
        // Pixel is a vibrant green, skip.
        continue;
      }

      colorSet.add(packRgb(r, g, b));
    }
  }

  // If there are too many colours, try and cluster them. This is done by lowering the
  // threshold iteratively until there are at least 7 colours. It may turn out this is
  // never the case, in which case we just take whatever is last.
  const colorList = Array.from(colorSet);
  let finalColors = colorList;
  if (finalColors.length > 6) {
    let threshold = 24;

    finalColors = clusterColors(colorList, threshold);

    while (finalColors.length < 7 && threshold >= 4) {
      threshold -= 2;
      finalColors = clusterColors(colorList, threshold);
    }
  }

  const rgbStrings = finalColors.map(
    (packed) => `#${packed.toString(16).padStart(6, "0")}`,
  );

  return rgbStrings;
}
