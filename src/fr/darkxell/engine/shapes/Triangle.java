package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;

public class Triangle extends SceneElement implements NormalPrimitive {

	public final Point v1;
	public final Point v2;
	public final Point v3;

	private static double EPSILON = 0.0000001d;

	public Triangle(Point p1, Point p2, Point p3) {
		v1 = p1;
		v2 = p2;
		v3 = p3;
	}

	@Override
	public Optional<HitResult> intersect(Point source, Point vector) {
		Point edge1 = v2.clone().substract(v1);
		Point edge2 = v3.clone().substract(v1);
		Point h = vector.clone().cross(edge2);
		Point s = source.clone().substract(v1);
		Point q = s.clone().cross(edge1);
		double a, f, u, v;
		a = edge1.scalarproduct(h);
		if (a > -EPSILON && a < EPSILON)
			return Optional.empty(); // Le rayon est parallèle au triangle.
		f = 1.0 / a;
		u = f * (s.scalarproduct(h));
		if (u < 0.0 || u > 1.0) {
			return Optional.empty();
		}
		v = f * vector.scalarproduct(q);
		if (v < 0.0 || u + v > 1.0) {
			return Optional.empty();
		}
		// On calcule t pour savoir ou le point d'intersection se situe sur la ligne.
		double t = f * edge2.scalarproduct(q);
		if (t > EPSILON) // // Intersection avec le rayon
			return Optional.of(new HitResult(source, vector, t, this));
		else // On a bien une intersection de droite, mais pas de rayon.
			return Optional.empty();
	}

	@Override
	public Point normal(Point reference) {
		Point u = v3.clone().substract(v1);
		Point v = v2.clone().substract(v1);
		return new Point(u.y() * v.z() - u.z() * v.y(), u.z() * v.x() - u.x() * v.z(), u.x() * v.y() - u.y() * v.x()).normalize();
	}

	@Override
	public String toString() {
		return "[Triangle " + v1 + "/" + v2 + "/" + v3 + "]";
	}

	/**
	 * @return the minimum position of this triangle in the X direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double minX() {
		return Math.min(v1.x(),Math.min(v2.x(), v3.x()));
	}
	
	/**
	 * @return the maximum position of this triangle in the X direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double maxX() {
		return Math.max(v1.x(),Math.max(v2.x(), v3.x()));
	}
	
	/**
	 * @return the minimum position of this triangle in the Y direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double minY() {
		return Math.min(v1.y(),Math.min(v2.y(), v3.y()));
	}
	
	/**
	 * @return the maximum position of this triangle in the Y direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double maxY() {
		return Math.max(v1.y(),Math.max(v2.y(), v3.y()));
	}
	
	/**
	 * @return the minimum position of this triangle in the Z direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double minZ() {
		return Math.min(v1.z(),Math.min(v2.z(), v3.z()));
	}
	
	/**
	 * @return the maximum position of this triangle in the Z direction, as long as
	 *         this triangle doesn't have NaN position.
	 */
	public double maxZ() {
		return Math.max(v1.z(),Math.max(v2.z(), v3.z()));
	}

}
