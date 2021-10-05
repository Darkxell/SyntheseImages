package fr.darkxell.engine.materials;

import java.awt.Color;

/** ColorDouble is a mutable color type that stores its internal on a double. */
public class ColorDouble {

	public double red;
	public double green;
	public double blue;

	public ColorDouble(int r, int g, int b) {
		this.red = ((double) r) / 255d;
		this.green = ((double) g) / 255d;
		this.blue = ((double) b) / 255d;
	}

	public ColorDouble(double r, double g, double b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	@Override
	public ColorDouble clone() {
		return new ColorDouble(red, green, blue);
	}

	public ColorDouble add(ColorDouble toadd) {
		this.red += toadd.red;
		this.green += toadd.green;
		this.blue += toadd.blue;
		return this;
	}

	public ColorDouble mul(double scalar) {
		this.red *= scalar;
		this.green *= scalar;
		this.blue *= scalar;
		return this;
	}

	/**
	 * The returned object should be disposed if used once, as AWT colors leak
	 * memory logarithmically up to 63 GB if procedurally generated.
	 * 
	 * @return a new instance of awt color for graphics filling.
	 */
	public Color toAWTColor() {
		return new Color((float) red, (float) green, (float) blue);
	}

	@Override
	public String toString() {
		return "ColorDouble[r:" + (float) red + "/g:" + (float) green + "/b:" + (float) blue + "]";
	}

	public static final ColorDouble WHITE = new ColorDouble(1d, 1d, 1d);
	public static final ColorDouble RED = new ColorDouble(1d, 0d, 0d);
	public static final ColorDouble GREEN = new ColorDouble(0d, 1d, 0d);
	public static final ColorDouble BLUE = new ColorDouble(0d, 0d, 1d);
	public static final ColorDouble BLACK = new ColorDouble(0d, 0d, 0d);
	public static final ColorDouble SKYPINK = new ColorDouble(255, 155, 221);

}
