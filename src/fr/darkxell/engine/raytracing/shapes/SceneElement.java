package fr.darkxell.engine.raytracing.shapes;

import java.util.Optional;

import fr.darkxell.engine.raytracing.HitResult;
import fr.darkxell.engine.raytracing.Point;
import fr.darkxell.engine.raytracing.materials.Material;

public abstract class SceneElement {

	/** Material used for this Element render by a camera */
	private Material mat = Material.PRESET_DEFAULTWHITE;

	/** @return a pointer to this material's element */
	public Material getMat() {
		return mat;
	}

	/** Setter for this Element's material */
	public void setMat(Material m) {
		mat = m;
	}

	/**
	 * @return the intersection distance between this element and the parsed vector.
	 *         The vector originates from the source point.<br/>
	 *         void Optional if the vector does not intersect the Element.
	 */
	public abstract Optional<HitResult> intersect(Point source, Point vector);

}
