package fr.darkxell.engine.fluids;

public class ChunkedUniverse {

	/**
	 * Flag that indicates that the buffer for this object is currently being edited
	 * and may contain garbage value.
	 */
	public boolean bufferlockon = false;
	/**
	 * Char buffer to display this universe in a console. NOT doublebuffered, any
	 * changes may instantly apply depending on the reader's handling of the lock
	 * flag.
	 */
	public char[][] FRAMEBUFFER;
	public UniverseCell[][] content;
	public final int WIDTH = 100;
	public final int HEIGHT = 30;
	
	public ChunkedUniverse() {
		FRAMEBUFFER = new char[HEIGHT][WIDTH];
		cleanBuffer();
	}

	private int particleX = 1;
	private int particleY = 10;

	public void tick() {
		particleX++;
		if (particleX >= WIDTH - 1) {
			particleX = 1;
			particleY++;
			if (particleY >= HEIGHT - 1) 
				particleY = 1;
		}
			
	}

	/**
	 * Updates the framebuffer of this universe to be ready for printing
	 */
	public void print() {
		bufferlockon = true;
		cleanBuffer();
		FRAMEBUFFER[particleY][particleX] = 'x';
		bufferlockon = false;
	}

	private void cleanBuffer() {
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				FRAMEBUFFER[j][i] = i == 0 || i == WIDTH - 1 ? '║' : j == 0 || j == HEIGHT - 1 ? '═' : ' ';
		FRAMEBUFFER[0][0] = '╔';
		FRAMEBUFFER[HEIGHT - 1][0] = '╚';
		FRAMEBUFFER[0][WIDTH - 1] = '╗';
		FRAMEBUFFER[HEIGHT - 1][WIDTH - 1] = '╝';
	}

}
