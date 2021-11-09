package fr.darkxell.engine.fluids;

import java.util.Iterator;

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
	public UniverseCell[] content;
	public final int WIDTH = 100;
	public final int HEIGHT = 30;

	public final float GRAVITY = 0.005f;

	public ChunkedUniverse() {
		FRAMEBUFFER = new char[HEIGHT][WIDTH];
		content = new UniverseCell[HEIGHT * WIDTH];
		for (int i = 0; i < content.length; i++) {
			int x = i % WIDTH, y = i / WIDTH;
			content[i] = new UniverseCell(this, x, y);
			if (x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1)
				content[i].type = UniverseCell.SOLID;
		}

		content[xyToN(1, 1)].type = UniverseCell.SOLID;
		content[xyToN(WIDTH - 2, 1)].type = UniverseCell.SOLID;
		content[xyToN(1, HEIGHT - 2)].type = UniverseCell.SOLID;
		content[xyToN(WIDTH - 2, HEIGHT - 2)].type = UniverseCell.SOLID;
		
		for (int i = 20; i < 60; i++) 
			content[xyToN(i, 18)].type = UniverseCell.SOLID;
		content[xyToN(20, 17)].type = UniverseCell.SOLID;
		content[xyToN(20, 16)].type = UniverseCell.SOLID;
		content[xyToN(20, 14)].type = UniverseCell.SOLID;
		content[xyToN(20, 13)].type = UniverseCell.SOLID;
		content[xyToN(20, 12)].type = UniverseCell.SOLID;
		content[xyToN(20, 11)].type = UniverseCell.SOLID;
		content[xyToN(19, 12)].type = UniverseCell.SOLID;
		content[xyToN(19, 11)].type = UniverseCell.SOLID;
		content[xyToN(59, 17)].type = UniverseCell.SOLID;
		content[xyToN(59, 16)].type = UniverseCell.SOLID;
		
		for (int i = 50; i < 70; i++) 
			content[xyToN(i, 25)].type = UniverseCell.SOLID;
		content[xyToN(50, 24)].type = UniverseCell.SOLID;
		content[xyToN(50, 23)].type = UniverseCell.SOLID;
		content[xyToN(69, 24)].type = UniverseCell.SOLID;
		content[xyToN(69, 23)].type = UniverseCell.SOLID;

		for (int i = 30; i < 68; i++)
			for (int j = 2; j < 6; j++) {
					addParticle(1.1f + i, 5.1f + j);
					addParticle(1.6f + i, 5.1f + j);
					addParticle(1.1f + i, 5.6f + j);
					addParticle(1.6f + i, 5.6f + j);
			}
//		FluidParticle debugpart = addParticle(50.1f, 11.1f);
//		debugpart.velocity.pp()[0] = 0.4;
//		debugpart.debugEnabled = true;

	}

	public void tick() {
		for (int i = 0; i < content.length; i++)
			content[i].tick();
		
		int partcount = 0;
		for (int i = 0; i < content.length; i++) {
			partcount += content[i].content.size();
		}
		System.out.println("Universe particle count : " + partcount);
	}

	/** Translates squared coordinates into a single dimension memory offset. */
	public int xyToN(int x, int y) {
		return y * WIDTH + x;
	}

	public FluidParticle addParticle(float x, float y) {
		int cellX = (int) x, cellY = (int) y;
		if (cellX < 0)
			cellX = 0;
		else if (cellX >= WIDTH)
			cellX = WIDTH - 1;
		if (cellY < 0)
			cellY = 0;
		else if (cellY >= HEIGHT)
			cellY = HEIGHT - 1;
		UniverseCell container = content[xyToN(cellX, cellY)];
		FluidParticle toreturn = new FluidParticle(container, x % 1, y % 1);
		container.content.add(toreturn);
		return toreturn;
	}

	/**
	 * returns a new position of tile offset by the given scalars. -1 if no tile
	 * exists.
	 */
	public int osffsetN(int n, int x, int y) {
		int currentx = n % WIDTH, currenty = n / WIDTH;
		if (currentx + x < 0 || currentx + x >= WIDTH || currenty + y < 0 || currenty + y >= HEIGHT)
			return -1;
		return n + x + y * WIDTH;
	}

	/**
	 * Updates the framebuffer of this universe to be ready for printing
	 */
	public void print() {
		bufferlockon = true;
		for (int i = 0; i < content.length; i++) {
			UniverseCell c = content[i];
			FRAMEBUFFER[c.y][c.x] = c.getChar();
		}
		bufferlockon = false;
	}

//	private void cleanBuffer() {
//		for (int i = 0; i < WIDTH; i++)
//			for (int j = 0; j < HEIGHT; j++)
//				FRAMEBUFFER[j][i] = i == 0 || i == WIDTH - 1 ? '║' : j == 0 || j == HEIGHT - 1 ? '═' : ' ';
//		FRAMEBUFFER[0][0] = '╔';
//		FRAMEBUFFER[HEIGHT - 1][0] = '╚';
//		FRAMEBUFFER[0][WIDTH - 1] = '╗';
//		FRAMEBUFFER[HEIGHT - 1][WIDTH - 1] = '╝';
//	}

}
