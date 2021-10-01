package fr.darkxell.engine.materials;

import java.awt.Color;

public class Material {

	public static final byte REFLECTION_MAT = 0;
	public static final byte REFLECTION_REFLECTIVE = 1;
	public static final byte REFLECTION_TRANSPARENT = 2;
	public static final byte REFLECTION_GLOWY = 3;
	
	public Color color = Color.WHITE;
	public byte reflection = REFLECTION_MAT;
	
}
