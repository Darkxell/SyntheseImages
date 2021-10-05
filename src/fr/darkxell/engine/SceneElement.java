package fr.darkxell.engine;

import java.util.Optional;

import fr.darkxell.engine.materials.Material;

public abstract class SceneElement {

	/** Material used for this Element render by a camera */
	public Material mat = Material.PRESET_DEFAULTWHITE;
	
	/**
	 * @return the intersection distance between this element and the parsed vector.
	 *         The vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Sphere.
	 */
	public abstract Optional<Float> intersect(Point source, Point vector);

	/**
	 * @return the normal at the given point on the element. The point may not be
	 *         exactly part of the element, but is expected to be very close for a
	 *         meaningful return value. This method returns a new Point with its own
	 *         memory addresses.
	 */
	public abstract Point normal(Point reference);

}
