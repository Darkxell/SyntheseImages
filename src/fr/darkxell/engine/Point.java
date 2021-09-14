package fr.darkxell.engine;


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
		return positions.length > n ? 0 : positions[n];
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
	 */
	public void add(Point p) {
		int maxlength = Math.max(p.getDimention(), this.getDimention());
		double[] toreturn = new double[maxlength];
		for (int i = 0; i < maxlength; i++)
			toreturn[i] = p.n(i) + this.n(i);
		this.positions = toreturn;
		// TODO : optimisation: reuse the memory addresses in the array if p.dim <=
		// this.dim
	}

	/**
	 * Changes the values of this point, so that the norm of the vector formed by
	 * its coordinates is 1. This is a rather expensive calculation.
	 */
	public void normalize() {
		double magnitude_squared=0;
		for (int i = 0; i < positions.length; i++)
			magnitude_squared += positions[i]*positions[i];
		float invsqrt = MathUtil.Q_rsqrt((float)magnitude_squared);		
		for (int i = 0; i < positions.length; i++)
			positions[i] *= invsqrt;
	}
	
	
	
	// compare norm
	// Comparenorm manhatan
	
	
	
	@Override
	public String toString() {
		String toreturn = "Point in dim:" + this.getDimention() + " [";
		for (int i = 0; i < this.getDimention(); i++)
			toreturn += i == 0 ? this.getN(i) : "," + this.getN(i);
		return toreturn + "]";
	}
}
