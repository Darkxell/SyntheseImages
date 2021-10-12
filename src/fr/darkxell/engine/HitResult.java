package fr.darkxell.engine;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
	
	public Point getLocationEpsilonTowards(Point towards) {
		return getLocation().clone().add(towards.clone().multiply(0.001d));
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
		Thread.dumpStack();
		return new Point(1d, 0d, 0d);
	}
	
	/** Returns the intensity of a light on this hitresult location */
	public double computeLightOnThis(LightSource ls, ArrayList<SceneElement> elements) {
		double toreturn = 0d;
		// Iterate N times on values next to the light source
		for (int liter = 0; liter < ls.fuzziness; ++liter) {
			Point efflightpose;
			if (ls.fuzziness > 1) {
				Point rdev = new Point(ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius,
						ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius,
						ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius);
				efflightpose = ls.pos.clone().add(rdev);
			} else
				efflightpose = ls.pos;
			// Normalized vector containing the direction from the collision vector towards
			// the light
			Point lightdirection = efflightpose.clone().substract(getLocation()).normalize();
			// Small padding towards the light to avoid collision with the element that
			// started the ray
			Point collisionpadded = getLocation().clone().add(lightdirection.clone().multiply(0.001d));
			boolean isblocked = false;
			for (int m = 0; m < elements.size(); m++) {
				Optional<HitResult> lightersect = elements.get(m).intersect(collisionpadded, lightdirection);
				if (!lightersect.isEmpty() && Math.pow(lightersect.get().hitDistance, 2) < efflightpose.clone()
						.substract(collisionpadded).normSquared()) {
					isblocked = true;
					break;
				}
			}
			if (!isblocked) {
				double lighting = (Math.abs(getNormal().scalarproduct(lightdirection)) * ls.intensity)
						/ (Math.PI * getLocation().clone().substract(ls.pos).norm());
				toreturn += lighting / ls.fuzziness;
			}
		}
		return toreturn;
	}

}
