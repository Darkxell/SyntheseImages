package fr.darkxell.engine;

public class Camera {

	public Point origin;
	public Point rasterOrigin;
	/**Camera FOV, in radians*/
	public double fov = 1.22; // 1.22 = 70degrees
	public int width = 200;
	public int height = 130;

	public Camera(Point origin, Point rasterOrigin) {
		this.origin = origin;
		this.rasterOrigin = rasterOrigin;
	}

}

