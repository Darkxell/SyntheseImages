package fr.darkxell.engine;

import java.awt.Color;

public class LightSource {

	public Color color = Color.WHITE;
	public Point pos;
	/** Intensity of the light source, in watts */
	public int intensity = 100;
	/** radius of the source */
	public double radius = 0.2;
	/**
	 * The amount of iterations this light does to calculate if a point is lit or
	 * not
	 */
	public int fuzziness = 20;

	public LightSource(Point p) {
		this.pos = p;
	}

	public LightSource(Point p, int intensity) {
		this.pos = p;
		this.intensity = intensity;
	}

}
