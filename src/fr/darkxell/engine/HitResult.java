package fr.darkxell.engine;

import fr.darkxell.engine.shapes.NormalPrimitive;
import fr.darkxell.engine.shapes.SceneElement;

public class HitResult {

	public final Point rayOrigin;
	public final Point rayDirection;
	public final double hitDistance;
	public final SceneElement hitElement;

	private Point hitLocation;
	private Point hitNormal;

	public HitResult(Point rayOrigin, Point rayDirection, double distance, SceneElement hitElement) {
		this.rayOrigin = rayOrigin;
		this.rayDirection = rayDirection;
		this.hitDistance = distance;
		this.hitElement = hitElement;
	}

	public HitResult(Point rayOrigin, Point rayDirection, double distance, SceneElement hitElement, Point normal) {
		this(rayOrigin, rayDirection, distance, hitElement);
		this.hitNormal = normal;
	}
	
	public Point getLocation() {
		if (hitLocation != null)
			return hitLocation;
		Point collision = rayDirection.clone().normalize().multiply(hitDistance).add(rayOrigin);
		this.hitLocation = collision;
		return hitLocation;
	}

	public Point getNormal() {
		if (hitNormal != null)
			return hitNormal;
		if (hitElement instanceof NormalPrimitive) {
			Point normal = ((NormalPrimitive) hitElement).normal(getLocation());
			hitNormal = normal;
			return hitNormal;
		}
		System.err.println("Tried to get the normal of a non normalisable primitive, this makes no sense."
				+ "\nReturned a unit vector (1,0,0).");
		return new Point(1d, 0d, 0d);
	}

}
