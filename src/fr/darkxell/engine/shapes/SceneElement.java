package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;
import fr.darkxell.engine.materials.Material;

public abstract class SceneElement {

	/** Material used for this Element render by a camera */
	public Material mat = Material.PRESET_DEFAULTWHITE;
	
	/**
	 * @return the intersection distance between this element and the parsed vector.
	 *         The vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Sphere.
	 */
	public abstract Optional<HitResult> intersect(Point source, Point vector);

}
