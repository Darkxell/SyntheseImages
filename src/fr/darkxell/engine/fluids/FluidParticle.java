package fr.darkxell.engine.fluids;

import java.util.Iterator;

import fr.darkxell.engine.raytracing.Point;

public class FluidParticle {

	public UniverseCell container;
	/** The horizontal position of this particle in its container. */
	public float x;
	/** The vertical position of this particle in its container. */
	public float y;
	/** The velocity of the fluid particle. 0 means it's not moving. */
	public Point velocity = new Point(0, 0);

	public FluidParticle(UniverseCell container, float x, float y) {
		this.container = container;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		// Apply gravity
		velocity.pp()[1] += container.parent.GRAVITY;
		// TODO : Apply viscosity
		// TODO : Adjust springs
		// Apply Spring displacement
		doubleDensityRelax();
		// TODO : DoubleDensity relaxation
		// TODO : Resolve collisions
		this.x += velocity.pp()[0];
		this.y += velocity.pp()[1];
		rebound();
	}

	private void doubleDensityRelax() {
		// First iteration on neighbors to compute density
		float nearDensity = 0f;
		for (int tx = -1; tx <= 1; tx++)
			for (int ty = -1; ty <= 1; ty++) {
				UniverseCell neig = container.getNeighbour(tx, ty);
				if (neig != null) {
					nearDensity += neig.content.size(); // replaces the for loop for faster times
//					for (int i = 0; i < neig.content.size(); i++) {
//						nearDensity += 1;
//					}
				}
			}
		// Second iteration on neighbors to push them away
		for (int tx = -1; tx <= 1; tx++)
			for (int ty = -1; ty <= 1; ty++) {
				UniverseCell neig = container.getNeighbour(tx, ty);
				if (neig != null) {
					
				}
			}
	}

	/**
	 * Check if this particle is out of bounds for this container, and transmits it
	 * to its neighbor if needed. This method recursively changes parents and may
	 * lag for extreme velocities.
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
