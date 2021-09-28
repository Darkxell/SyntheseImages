package fr.darkxell.engine;

import java.util.Optional;

public interface SceneElement {

	/**
	 * @return the intersection distance between this element and the parsed vector.
	 *         The vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Sphere.
	 */
	public Optional<Float> intersect(Point source, Point vector);

	/**
	 * @return the normal at the given point on the element. The point may not be
	 *         exactly part of the element, but is expected to be very close for a
	 *         meaningful return value. This method returns a new Point with its own
	 *         memory addresses.
	 */
	public Point normal(Point reference);

}
