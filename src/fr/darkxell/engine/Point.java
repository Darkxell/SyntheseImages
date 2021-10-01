package fr.darkxell.engine;

import java.util.Arrays;

import fr.darkxell.utility.MathUtil;

/**
 * Denotes a point in N dimensions. <br/>
 * May be used as a vector, and contains math utility to compute common vector
 * algebra.
 */
public class Point {

	private double[] positions;

	public Point(double[] positions) {
		this.positions = positions;
	}

	public Point(double x, double y) {
		this.positions = new double[] { x, y };
	}

	public Point(double x, double y, double z) {
		this.positions = new double[] { x, y, z };
	}

	/**
	 * Returns the dimension scope of this point. For exemple, a x,y,z triplet will
	 * simply return 3.
	 */
	public int getDimention() {
		return positions.length;
	}

	/**
	 * Gets the position of this point in dimension n. Returns 0 if this point is in
	 * a smaller dimension than parsed n param.
	 */
	public double getN(int n) {
		return positions.length <= n ? 0 : positions[n];
	}

	/** Alias to <code>getN(int)</code> */
	public double n(int n) {
		return getN(n);
	}

	/**
	 * Shortcut method. Returns the first coordinate of this point in space. 0 if
	 * this point is in dimension below 1 (what are you doing...?)
	 */
	public double x() {
		return positions.length >= 1 ? positions[0] : 0;
	}

	/**
	 * Shortcut method. Returns the second coordinate of this point in space. 0 if
	 * this point is in dimension below 2
	 */
	public double y() {
		return positions.length >= 2 ? positions[1] : 0;
	}

	/**
	 * Shortcut method. Returns the first coordinate of this point in space. 0 if
	 * this point is in dimension below 3
	 */
	public double z() {
		return positions.length >= 3 ? positions[2] : 0;
	}

	/** Short for position pointer. Returns a pointer to the inner pointer array. */
	public double[] pp() {
		return this.positions;
	}

	/** @return the addition of the parsed two points. */
	public static Point add(Point a, Point b) {
		int maxlength = Math.max(a.getDimention(), b.getDimention());
		double[] toreturn = new double[maxlength];
		for (int i = 0; i < maxlength; i++)
			toreturn[i] = a.n(i) + b.n(i);
		return new Point(toreturn);
	}

	/**
	 * Adds the positions of parsed point to this point positions. If one point has
	 * a higher dimension than the other, both will be cast to mach that same higher
	 * dimension.
	 * 
	 * @return
	 */
	public Point add(Point p) {
		int maxlength = Math.max(p.getDimention(), this.getDimention());
		double[] toreturn = new double[maxlength];
		for (int i = 0; i < maxlength; i++)
			toreturn[i] = p.n(i) + this.n(i);
		this.positions = toreturn;
		return this;
		// TODO : optimisation: reuse the memory addresses in the array if p.dim <=
		// this.dim
	}

	/**
	 * Multiplies this Point value by value with the given point.
	 * 
	 * @return a pointer to this point.
	 */
	public Point mul(Point p) {
		int maxlength = Math.max(p.getDimention(), this.getDimention());
		double[] toreturn = new double[maxlength];
		for (int i = 0; i < maxlength; i++)
			toreturn[i] = p.n(i) * this.n(i);
		this.positions = toreturn;
		return this;
	}

	public Point substract(Point p) {
		int maxlength = Math.max(p.getDimention(), this.getDimention());
		double[] toreturn = new double[maxlength];
		for (int i = 0; i < maxlength; i++)
			toreturn[i] = this.n(i) - p.n(i);
		this.positions = toreturn;
		return this;
		// TODO : optimisation: reuse the memory addresses in the array if p.dim <=
		// this.dim
	}

	/**
	 * Changes the values of this point, so that the norm of the vector formed by
	 * its coordinates is 1. This is a rather expensive calculation. This specific
	 * implementation uses quake3's fast inverse square root method.
	 */
	public Point normalizeUltra() {
		double magnitude_squared = 0;
		for (int i = 0; i < positions.length; i++)
			magnitude_squared += positions[i] * positions[i];
		float invsqrt = MathUtil.Q_rsqrt((float) magnitude_squared);
		for (int i = 0; i < positions.length; i++)
			positions[i] *= invsqrt;
		return this;
	}

	/**
	 * Changes the values of this point, so that the norm of the vector formed by
	 * its coordinates is 1. This is a rather expensive calculation.
	 */
	public Point normalize() {
		double n = this.norm();
		multiply(1 / n);
		return this;
	}

	/** Returns the norm of this vector */
	public double norm() {
		return Math.sqrt(this.normSquared());
	}

	/**
	 * Returns the square of this vector's norm. Faster then the actual norm
	 * calculation.
	 */
	public double normSquared() {
		double toreturn = 0;
		for (int i = 0; i < positions.length; i++)
			toreturn += positions[i] * positions[i];
		return toreturn;
	}

	/**
	 * Predicate that returns true if this vector has a larger norm than the parsed
	 * vector's norm. Returns true if both norms are equals.
	 */
	public boolean isBiggerThan(Point p) {
		return this.normSquared() >= p.normSquared();
	}

	/** multiply the coordinates of this point by the given scalar */
	public Point multiply(double m) {
		for (int i = 0; i < positions.length; i++)
			positions[i] *= m;
		return this;
	}

	/**
	 * @return scalar product of this vector by the parsed vector. 0 if both vectors
	 *         have different dimensions.
	 */
	public double scalarproduct(Point q) {
		// FIXME: assume 0 if different dimention, this is bug prone.
		double scalarproduct = 0.0;
		if (this.positions.length == q.positions.length)
			for (int i = 0; i < q.positions.length; ++i)
				scalarproduct += q.positions[i] * this.positions[i];
		return scalarproduct;
	}

	/**
	 * This method does not modify this object or the parsed one, and returns a new
	 * Point with its own memory allocation.
	 * 
	 * @return the reflection of this vector according to a normal.
	 */
	public Point reflection(Point normal) {
		Point n2i = normal.clone().multiply(this.scalarproduct(normal) * 2f);
		return this.clone().substract(n2i);
	}

	public Point oneOn() {
		for (int i = 0; i < positions.length; i++)
			if (positions[i] != 0)
				positions[i] = 1 / positions[i];
			else
				positions[i] = 0d; // WHAT THE FUCK JAVA
		return this;
	}

	public Point abs() {
		for (int i = 0; i < positions.length; i++)
			positions[i] = Math.abs(positions[i]);
		return this;
	}

	public boolean isZero() {
		for (int i = 0; i < positions.length; i++)
			if (positions[i] != 0)
				return false;
		return true;
	}

	/** Returns a new point object identical to this one. */
	public Point clone() {
		return new Point(Arrays.copyOf(positions, positions.length));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		if (this.getDimention() != ((Point) obj).getDimention())
			return false;
		for (int i = 0; i < positions.length; i++)
			if (this.positions[i] != ((Point) obj).n(i))
				return false;
		return true;
	}

	@Override
	public String toString() {
		String toreturn = "[dim=" + this.getDimention() + ":";
		for (int i = 0; i < this.getDimention(); i++)
			toreturn += i == 0 ? (float) this.getN(i) : "," + (float) this.getN(i);
		return toreturn + "]";
	}
}
