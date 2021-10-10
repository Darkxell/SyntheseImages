package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;

public class Sphere extends SceneElement implements NormalPrimitive{

	public Point center;
	public float radius;

	public Sphere(Point center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Optional<HitResult> intersect(Point source, Point vector) {
		Point XmC = source.clone().substract(center);
		double a =  vector.normSquared(), b =2 * (XmC.scalarproduct(vector)),
				c = XmC.normSquared() - radius * radius, delta = b * b - 4 * a * c;
		if (delta < 0)
			return Optional.empty();
		double f1 = (-b - Math.sqrt(delta)) / (2 * a), f2 =  (-b + Math.sqrt(delta) / (2 * a));
		if (f1 < 0 && f2 < 0)
			return Optional.empty();
		HitResult toreturn = new HitResult(source, vector, f1 < 0 ? f2 : Math.min(f1, f2), this);
		return Optional.of(toreturn);
	}

	@Override
	public Point normal(Point reference) {
		return reference.clone().substract(center).normalize();
	}

}
