package fr.darkxell.utility;

public abstract class MathUtil {

	/**
	 * Computes the fast inverse square root of a number.<br/>
	 * https://en.wikipedia.org/wiki/Fast_inverse_square_root
	 */
	public static float Q_rsqrt(float number) {
		// FIXME: Not sure this works, this fails a unit test later down the line
		// Fuck quake 3 algorithms anyways, too big brain.
		long i = 0;
		float x2, y;
		final float threehalfs = 1.5F;
		x2 = number * 0.5F;
		y = number;
		i = 0x5f3759df - (i >> 1);
		y = y * (threehalfs - (x2 * y * y)); // 1st iteration
		return y;
	}

	/** Returns a gradiant255 linearly between min and max */
	public static float grad255(float min, float max, float number) {
		if (number >= max)
			return 255f;
		if (number <= min)
			return 0f;
		return (number - min) / (max - min) * 255;
	}

	/** Returns the inverse grad255 */
	public static float rgrad255(float min, float max, float number) {
		return 255 - grad255(min, max, number);
	}

	/**
	 * @return the minimum value given the two parameters. If one is NaN, will
	 *         return the other. May return Nan anyways if both numbers are NaN.
	 */
	public static double ieeemin(double a, double b) {
		if (Double.isNaN(a))
			return b;
		if (Double.isNaN(b))
			return a;
		if (a == Double.POSITIVE_INFINITY)
			return b;
		if (b == Double.POSITIVE_INFINITY)
			return a;
		if (a == Double.NEGATIVE_INFINITY)
			return a;
		if (b == Double.NEGATIVE_INFINITY)
			return b;
		return a < b ? a : b;
	}

	/**
	 * @return the maximum value given the two parameters. If one is NaN, will
	 *         return the other. May return Nan anyways if both numbers are NaN.
	 */
	public static double ieeemax(double a, double b) {
		if (Double.isNaN(a))
			return b;
		if (Double.isNaN(b))
			return a;
		if (a == Double.POSITIVE_INFINITY)
			return a;
		if (b == Double.POSITIVE_INFINITY)
			return b;
		if (a == Double.NEGATIVE_INFINITY)
			return b;
		if (b == Double.NEGATIVE_INFINITY)
			return a;
		return a > b ? a : b;
	}

}
