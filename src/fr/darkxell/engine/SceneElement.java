package fr.darkxell.engine;

import java.util.Optional;

public interface SceneElement {

	/**
	 * @return the intersection between this Sphere and the parsed vector. The
	 *         vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Sphere.
	 */
	public Optional<Float> intersect(Point source, Point vector);

}
