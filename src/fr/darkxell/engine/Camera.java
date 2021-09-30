package fr.darkxell.engine;

public class Camera {

	public Point origin;
	public Point rasterOrigin;
	/** Camera FOV, in radians */
	public double fov = 1.22d; // 1.22 = 70degrees
	/** Width of the camera, ie the width of the render output image. */
	public int width = 200;
	/** Height of the camera, ie the height of the render output image. */
	public int height = 130;

	public Camera(Point origin, Point rasterOrigin) {
		this.origin = origin;
		this.rasterOrigin = rasterOrigin;
	}

	/**
	 * Returns the 3d Point where the "pixel" for the given position on the screen
	 * is. This is a position, not a vector from the camera source.
	 */
	public Point rasterPixel(int x, int y) {
		double theta = fov / width;
		int imod = x - width / 2, jmod = y - height / 2;
		double ti0 = imod * theta, tj0 = jmod * theta;
		double rasterIX = rasterOrigin.x() - (1 - Math.cos(ti0));
		double rasterJY = rasterOrigin.y() + Math.sin(tj0);
		double rasterIZ = rasterOrigin.z() + Math.sin(ti0);
		return new Point(rasterIX + origin.x(), rasterJY + origin.y(), rasterIZ + origin.z());
	}

}
