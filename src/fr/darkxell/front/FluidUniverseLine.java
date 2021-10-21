package fr.darkxell.front;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.darkxell.engine.fluids.ChunkedUniverse;

public class FluidUniverseLine extends ConsoleLine {

	private ChunkedUniverse universe;
	
	public FluidUniverseLine(ChunkedUniverse o) {
		this.universe = o;
	}
	
	@Override
	public int getHeight() {
		return this.universe.HEIGHT * 12 + 4;
	}

	@Override
	public void print(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		for (int i = 0; i < this.universe.HEIGHT; i++) {
			String s = new String(universe.FRAMEBUFFER[i]);
			g2d.drawString(s, x, y + 12 * (i + 1));
		}
	}

	
	
}
