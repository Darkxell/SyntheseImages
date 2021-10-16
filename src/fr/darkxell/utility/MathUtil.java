package fr.darkxell.utility;

public abstract class MathUtil {

	/**
	 * Computes the fast inverse square root of a number.<br/>
	 * https://en.wikipedia.org/wiki/Fast_inverse_square_root
	 */
	public static float Q_rsqrt(float number) {
		// FIXME: Not sure this works, this fails a unit test later down the line
		// Fuck quake 3 algorithms anyways, too big brain.
		float xhalf = 0.5f * number;
	    int i = Float.floatToIntBits(number);
	    i = 0x5f3759df - (i >> 1);
	    number = Float.intBitsToFloat(i);
	    number *= (1.5f - xhalf * number * number);
	    return number;
	}

	/**
	 * Returns a clamped value between min and max. If min or max are Nan, Double
	 * bounds will be used in their stead. If the value is Nan, Nan will be
	 * returned.
	 */
	public static double clamp(double value, double min, double max) {
		if (Double.isNaN(min))
			min = -Double.MAX_VALUE;
		if (Double.isNaN(max))
			max = Double.MAX_VALUE;
		if (value < min)
			return min;
		else if (value > max)
			return max;
		else
			return value;
	}

	/**
	 * Returns a gradiantN linearly between min and max, with the gradiant between
	 * nmin and nmax
	 */
	public static double gradN(double min, double max, double number, double nmin, double nmax) {
		if (number >= max)
			return nmax;
		if (number <= min)
			return nmin;
		return (number - min) / (max - min) * nmax + nmin;
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
