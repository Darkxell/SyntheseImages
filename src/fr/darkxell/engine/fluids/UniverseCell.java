package fr.darkxell.engine.fluids;

import java.util.ArrayList;

public class UniverseCell {

	public final ChunkedUniverse parent;
	public final int x;
	public final int y;

	public byte type = AIR;

	public static final byte AIR = 0;
	public static final byte SOLID = 1;
	public static final byte SPONGE = 2;

	public ArrayList<FluidParticle> content = new ArrayList<>(50);

	public UniverseCell(ChunkedUniverse parent, int x, int y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		for (int i = 0; i < content.size(); i++)
			content.get(i).tick();
	}

	/** Returns a visualization of this tile as a single char */
	public char getChar() {
		switch (type) {
		case AIR:
			return content.size() == 0 ? ' ' : content.size() < 2 ? 'โ' : content.size() < 4 ? 'โ' : 'โ';
		case SOLID:
			return getSolidTexture();
		case SPONGE:
			return 'ยค';
		}
		return '?';
	}

	/**
	 * Returns the tile relative to this tile by x/y. May return null if out of
	 * bounds.
	 */
	public UniverseCell getNeighbour(int x, int y) {
		if (x == 0 && y == 0)
			return this;
		int k = parent.osffsetN(parent.xyToN(this.x, this.y), x, y);
		if (k == -1)
			return null;
		return parent.content[k];
	}

	@Override
	public String toString() {
		return "[UniverseCell at " + x + "/" + y + " containing " + content.size() + " particles]";
	}

	private char solidtexturebuffer = 'A';

	public char getSolidTexture() {
		if (solidtexturebuffer == 'A')
			solidtexturebuffer = computeSolidTexture();
		return solidtexturebuffer;
	}

	public char computeSolidTexture() {
		UniverseCell up = getNeighbour(0, -1), down = getNeighbour(0, 1), left = getNeighbour(-1, 0),
				right = getNeighbour(1, 0);
		boolean upSolid = up == null ? false : up.type == UniverseCell.SOLID,
				downSolid = down == null ? false : down.type == UniverseCell.SOLID,
				leftSolid = left == null ? false : left.type == UniverseCell.SOLID,
				rightSolid = right == null ? false : right.type == UniverseCell.SOLID;
		if (upSolid && downSolid && leftSolid && rightSolid)
			return 'โฌ';
		if (downSolid && leftSolid && rightSolid)
			return 'โฆ';
		if (upSolid && leftSolid && rightSolid)
			return 'โฉ';
		if (upSolid && downSolid && rightSolid)
			return 'โ?';
		if (upSolid && downSolid && leftSolid)
			return 'โฃ';
		if (upSolid && rightSolid)
			return 'โ';
		if (upSolid && leftSolid)
			return 'โ';
		if (downSolid && rightSolid)
			return 'โ';
		if (downSolid && leftSolid)
			return 'โ';
		if (downSolid || upSolid)
			return 'โ';
		if (leftSolid || rightSolid)
			return 'โ';
		return 'โก';
	}

}
