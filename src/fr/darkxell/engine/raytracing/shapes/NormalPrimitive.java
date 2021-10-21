package fr.darkxell.engine.raytracing.shapes;

import fr.darkxell.engine.raytracing.Point;

/** Shape that has a deterministic normal given a point. */
public interface NormalPrimitive {

	/**
	 * @return the normal at the given point on the element. The point may not be
	 *         exactly part of the element, but is expected to be very close for a
	 *         meaningful return value. This method returns a new Point with its own
	 *         memory addresses.
	 */
	public abstract Point normal(Point reference);
}
