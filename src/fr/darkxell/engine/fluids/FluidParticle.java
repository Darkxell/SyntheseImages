package fr.darkxell.engine.fluids;

public class FluidParticle {

	public UniverseCell container;
	/** The horizontal position of this particle in its container. */
	public float x;
	/** The vertical position of this particle in its container. */
	public float y;

	public FluidParticle(UniverseCell container, float x, float y) {
		this.container = container;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		y += 0.2;
		rebound();
	}

	/**
	 * Check if this particle is out of bounds for this container, and transmits it
	 * to its neighbor if needed. This method recursively changes parents and may lag for extreme velocities.
	 */
	private void rebound() {
		boolean haschanged = false;
		if (x < 0) {
			UniverseCell newparent = container.getNeighbour(-1, 0);
			x = changeContainer(newparent) ? x + 1 : 0f;
			haschanged = true;
		} else if (x > 1) {
			UniverseCell newparent = container.getNeighbour(1, 0);
			x = changeContainer(newparent) ? x - 1 : 1f;
			haschanged = true;
		} else if (y < 0) {
			UniverseCell newparent = container.getNeighbour(0, -1);
			y = changeContainer(newparent) ? y + 1 : 0f;
			haschanged = true;
		} else if (y > 1) {
			UniverseCell newparent = container.getNeighbour(0, 1);
			y = changeContainer(newparent) ? y - 1 : 1f;
			haschanged = true;
		}
		if (haschanged)// Remove this if recursion is lagging, technically works without it.
			rebound();
	}

	private boolean changeContainer(UniverseCell newparent) {
		if (newparent == null || newparent.type == UniverseCell.SOLID)
			return false;
		container.content.remove(this);
		this.container = newparent;
		container.content.add(this);
		return true;
	}

}
