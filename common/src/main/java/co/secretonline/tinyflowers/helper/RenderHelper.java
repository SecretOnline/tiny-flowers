package co.secretonline.tinyflowers.helper;

public class RenderHelper {
	public static float[] unpackColorInt(int tint) {
		float[] color = new float[3];

		int r = (tint >> 16) & 0xFF;
		int g = (tint >> 8) & 0xFF;
		int b = tint & 0xFF;

		color[0] = (float) r / 255.0F;
		color[1] = (float) g / 255.0F;
		color[2] = (float) b / 255.0F;

		return color;
	}
}
