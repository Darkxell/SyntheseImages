package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.Point;
import fr.darkxell.engine.SceneElement;

public class Triangle extends SceneElement {

	public final Point v1;
	public final Point v2;
	public final Point v3;

	private static double EPSILON = 0.0000001d;

	public Triangle(Point p1,Point p2,Point p3) {
		v1 = p1;
		v2 = p2;
		v3 = p3;
	}
	
	@Override
	public Optional<Float> intersect(Point source, Point vector) {
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
			return Optional.of((float) t);
		else // On a bien une intersection de droite, mais pas de rayon.
			return Optional.empty();
	}

	@Override
	public Point normal(Point reference) {
		Point u = v2.clone().substract(v1);
		Point v = v3.clone().substract(v1);
		return new Point(u.y() * v.z() - u.z() * v.y(), u.z() * v.x() - u.x() * v.z(), u.x() * v.y() - u.y() * v.x());
	}

}
