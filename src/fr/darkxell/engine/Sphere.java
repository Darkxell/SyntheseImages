package fr.darkxell.engine;

import java.util.Optional;

public class Sphere {

	public Point center;
	public float radius;

	public Sphere(Point center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	/**
	 * @return the intersection between this Sphere and the parsed vector. The
	 *         vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Sphere.
	 */
	public Optional<Float> intersect(Point source, Point vector) {
		Point XmC = source.clone().substract(center);
		System.out.println(XmC);
		float a = (float) vector.normSquared(), b = (float) (2 * (XmC.scalarproduct(vector))),
				c = (float) XmC.normSquared() - radius * radius, delta = b * b - 4 * a * c;
		System.out.println(a + "/" + b + "/" + c);
		if (delta < 0)
			return Optional.empty();
		float f1 = (float) ((-b - Math.sqrt(delta)) / (2 * a)), f2 = (float) ((-b + Math.sqrt(delta)) / (2 * a));
		return Optional.of((f1 < 0 && f2 < 0) ? -1 : (f1 < 0 ? f2 : Math.min(f1, f2)));
	}

}
