package fr.darkxell.engine.fluids;

import fr.darkxell.launchable.Launchable;

public class ChunkedUniverse {

	public char[][] FRAMEBUFFER;
	public final int WIDTH = 50;
	public final int HEIGHT = 15;

	public ChunkedUniverse() {
		FRAMEBUFFER = new char[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				FRAMEBUFFER[i][j] = i == 0 || i == WIDTH - 1 ? '═' : j == 0 || j == HEIGHT - 1 ? '║' : ' ';
		FRAMEBUFFER[0][0] = '╔';
		FRAMEBUFFER[WIDTH - 1][0] = '╚';
		FRAMEBUFFER[0][HEIGHT - 1] = '╗';
		FRAMEBUFFER[WIDTH - 1][HEIGHT - 1] = '╝';
	}

	public void tick() {
		
	}

	/**
	 * Prints a representation of this universe on the given character buffer. Uses
	 * the memory addresses pointed by the method parameter.
	 */
	public void print() {
		printBuffer();
	}

	private void printBuffer() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < FRAMEBUFFER.length; i++)
			sb.append(FRAMEBUFFER[i]).append('\n');
		Launchable.gc.p(sb);
	}

}
