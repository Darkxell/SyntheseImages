package fr.darkxell.engine.materials;

public class Material {

	public Material(double reflection, double refraction, ColorDouble color) {
		this.reflection = reflection;
		this.refraction = refraction;
		this.color = color;
	}

	/**
	 * If true, any raycast to this will snap to this material's color. This
	 * discards most shadow effects reflection and refraction, ending up looking
	 * terrible. May be useful for debug purposes.
	 */
	public boolean ultrabright = false;

	/**
	 * Reflection index, between 1 and 0. 1 Will be a perfect mirror, where as 0
	 * will not show any reflectiveness.
	 */
	public double reflection = 0d;
	/**
	 * Transparency index, between 1-reflection and 0. 1 Will be perfectly
	 * transparent.
	 */
	public double refraction = 0d;

	/**
	 * Fuzzyness index, meaning this material will refract rays in a slightly random
	 * direction, instead of perfectly.
	 */
	public double fuzziness_refraction = 0d;
	/**
	 * Fuzzyness index, meaning this material will reflect rays in a slightly random
	 * direction, instead of perfectly.
	 */
	public double fuzziness_reflection = 0d;

	/**
	 * The color of this material. Will not have effect if reflection + refraction =
	 * 1.
	 */
	public ColorDouble color = ColorDouble.WHITE;

	/** https://en.wikipedia.org/wiki/List_of_refractive_indices */
	public double refractioncoef = 1.5d; // 1.5 = sand glass

	/** A mat white material. Simplest possible material. */
	public static final Material PRESET_DEFAULTWHITE = new Material(0, 0, ColorDouble.WHITE);
	/** A mat red material. Simplest possible material. */
	public static final Material PRESET_DEFAULTRED = new Material(0, 0, ColorDouble.RED);
	/** A mat green material. Simplest possible material. */
	public static final Material PRESET_DEFAULTGREEN = new Material(0, 0, ColorDouble.GREEN);
	/** A mat blue material. Simplest possible material. */
	public static final Material PRESET_DEFAULTBLUE = new Material(0, 0, ColorDouble.BLUE);
	/** An ultrabright purple material, used for debug. */
	public static final Material PRESET_GLOWYPURPLE = new Material(0, 0, new ColorDouble(70, 1, 119));
	/** A mat white material. Simplest possible material. */
	public static final Material PRESET_DEFAULTSLIGHTBLUE = new Material(0, 0, new ColorDouble(210, 210, 255));
	/** A mat white material. Simplest possible material. */
	public static final Material PRESET_DEFAULTSLIGHTRED = new Material(0, 0, new ColorDouble(255, 210, 210));
	/** A mat white material. Simplest possible material. */
	public static final Material PRESET_DEFAULTSLIGHTGREEN = new Material(0, 0, new ColorDouble(210, 255, 210));
	/**
	 * A fully reflective material. This material will render white if rays die on
	 * it.
	 */
	public static final Material PRESET_MIRRORPERFECT = new Material(1d, 0d, ColorDouble.WHITE);
	/**
	 * A fully reflective material. This material will render white if rays die on
	 * it.
	 */
	public static final Material PRESET_GLASSPERFECT = new Material(0d, 1d, ColorDouble.WHITE);
	/**
	 * A slightly reflective light cyan material.
	 */
	public static final Material PRESET_REFCYAN = new Material(0.1d, 0d, new ColorDouble(163, 255, 248));
	/** A golden reflective material */
	public static final Material PRESET_POLISHEDGOLD = new Material(0.7d, 0d, ColorDouble.GOLD);
	/** A semi reflective transparent material, with a slight silver color. */
	public static final Material PRESET_MERCURY = new Material(0.035d, 0.75d, ColorDouble.SILVER);

	static {
		PRESET_GLOWYPURPLE.ultrabright = true;
		PRESET_POLISHEDGOLD.fuzziness_reflection = 0.08d;
	}

}
