package fr.darkxell.front;

import java.awt.Graphics2D;

/** Represents one entry in a GraphConsole */
public abstract class ConsoleLine {

	public abstract int getHeight();

	public abstract void print(Graphics2D g2d, int x, int y);

	/** Timestamp of this line's creation. */
	public final long creation = System.currentTimeMillis();

}
