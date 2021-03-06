package fr.darkxell.engine.raytracing;

import java.util.concurrent.ThreadLocalRandom;

public class Camera {

	public static final int ANTIALIASING_OFF = 1;
	public static final int ANTIALIASING_2X = 2;
	public static final int ANTIALIASING_8X = 8;
	public static final int ANTIALIASING_16X = 16;
	public static final int ANTIALIASING_32X = 32;
	public static final int ANTIALIASING_128X = 128;

	public Point origin;
	public Point rasterOrigin;
	/** Camera FOV, in radians */
	public double fov = 1.22d; // 1.22 = 70degrees
	/** Width of the camera, ie the width of the render output image. */
	public int width = 200*1;
	/** Height of the camera, ie the height of the render output image. */
	public int height = 130*1;
	/** number of iterations on one pixel */
	public int antialiasing = ANTIALIASING_OFF;
	/**
	 * Numbers of reflections/refractions recursions this camera checks to render a
	 * scene
	 */
	public int refractions = 4;

	public Camera(Point origin, Point rasterOrigin) {
		this.origin = origin;
		this.rasterOrigin = rasterOrigin;
	}

	/**
	 * Returns the 3d Point where the "pixel" for the given position on the screen
	 * is. This is a position, not a vector from the camera source. If antialiasing
	 * is on, this returns a value that may be off by up to 0.5 pixels.
	 */
	public Point rasterPixel(int x, int y) {
		double aliasingX = antialiasing > ANTIALIASING_OFF ? ThreadLocalRandom.current().nextDouble() - 0.5d : 0d,
				aliasingY = antialiasing > ANTIALIASING_OFF ? ThreadLocalRandom.current().nextDouble() - 0.5d : 0d;
		double theta = fov * 2 / width;
		double imod = x - width / 2 + aliasingX, jmod = y - height / 2 + aliasingY;
		double ti0 = imod * theta, tj0 = jmod * theta;
		double rasterIX = rasterOrigin.x() - (1 - Math.cos(ti0));
		double rasterJY = rasterOrigin.y() + Math.sin(tj0);
		double rasterIZ = rasterOrigin.z() + Math.sin(ti0);
		return new Point(rasterIX + origin.x(), rasterJY + origin.y(), rasterIZ + origin.z());
	}

}
