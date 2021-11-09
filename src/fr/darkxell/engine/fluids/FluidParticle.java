package fr.darkxell.engine.fluids;

import fr.darkxell.engine.raytracing.Point;

public class FluidParticle {

	public UniverseCell container;
	/** The horizontal position of this particle in its container. */
	public float x;
	/** The vertical position of this particle in its container. */
	public float y;
	/** The velocity of the fluid particle. 0 means it's not moving. */
	public Point velocity = new Point(0, 0);

	public boolean debugEnabled = false;
	/**Time, in frames, this particle has spent in his container.*/
	private int timeincontainer = 0;
	
	public FluidParticle(UniverseCell container, float x, float y) {
		this.container = container;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		// http://www.ligum.umontreal.ca/Clavet-2005-PVFS/pvfs.pdf
		timeincontainer++;
		if(timeincontainer > 10 && timeincontainer % 2 == 0) 
			return;
		if(timeincontainer > 50 && timeincontainer % 10 == 0) 
			return;
		if (debugEnabled) {
			System.out.println(" ------------- Tick begin ------------");
		}
		// Apply gravity
		velocity.pp()[1] += container.parent.GRAVITY;
		// Apply viscosity
		// applyViscosity();
		// TODO : Adjust springs
		// Apply Spring displacement
		doubleDensityRelax();
		// TODO : DoubleDensity relaxation
		// TODO : Resolve collisions
		
		if(velocity.pp()[1] < 0)
			velocity.pp()[1] =  velocity.pp()[1] / 3;
		
		float oldx = x, oldy = y;
		this.x += velocity.pp()[0];
		this.y += velocity.pp()[1];
		velocity.pp()[0] = this.x - oldx;
		velocity.pp()[1] = this.y - oldy;
		rebound();

		if (debugEnabled) {
			System.out.println("Velocity : " + velocity);
			System.out.println("Position x : " + x + " / y : " + y);
			System.out.println(" ------------- Tick end --------------");
		}
	}

	private void applyViscosity() {
		float h = 1f;
		for (int tx = -1; tx <= 1; tx++)
			for (int ty = -1; ty <= 1; ty++) {
				UniverseCell neig = container.getNeighbour(tx, ty);
				if (neig != null) {
					for (int i = 0; i < neig.content.size(); i++) {
						float rij = (float) Math.sqrt(Math.pow((float) tx + neig.content.get(i).x - this.x, 2)
								+ Math.pow((float) ty + neig.content.get(i).y - this.y, 2));
						float q = rij / h;
						if (q < 1) {
							
							
							
							
						}
					}
				}
			}
	}
	
	private void doubleDensityRelax() {
		// h : interaction radius
		// k : strength of the pullback when close
		// p0 : value of p at which we should start to push back
		// knear : strength of the pullback when very close
		float h = 1f, k = 0.1f, p0 = 20, knear = 0.08f;
		// First iteration on neighbors to compute density
		float p = 0f, pnear = 0f;
		int total = 0, computed = 0;
		for (int tx = -1; tx <= 1; tx++)
			for (int ty = -1; ty <= 1; ty++) {
				UniverseCell neig = container.getNeighbour(tx, ty);
				if (neig != null) {
					total += neig.content.size();
					for (int i = 0; i < neig.content.size(); i++) {
						float rij = (float) Math.sqrt(Math.pow((float) tx + neig.content.get(i).x - this.x, 2)
								+ Math.pow((float) ty + neig.content.get(i).y - this.y, 2));
						float q = rij / h;
						if (q < 1) {
							computed++;
							p += Math.pow(1 - q, 2);
							pnear += Math.pow(1 - q, 3);
						}
					}
				}
			}

		float P = k * (p - p0);
		float Pnear = knear * pnear;

		if (debugEnabled)
			System.out.println("Density: " + p + " / NearDensity: " + pnear + " - Computed on " + total
					+ " neighbors with " + computed + " nearby enough to count.");

		// Second iteration on neighbors to push them away
		Point dx = new Point(0, 0);
		for (int tx = -1; tx <= 1; tx++)
			for (int ty = -1; ty <= 1; ty++) {
				UniverseCell neig = container.getNeighbour(tx, ty);
				if (neig != null) {
					for (int i = 0; i < neig.content.size(); i++) {
						float rij = (float) Math.sqrt(Math.pow((float) tx + neig.content.get(i).x - this.x, 2)
								+ Math.pow((float) ty + neig.content.get(i).y - this.y, 2));
						float q = rij / h;
						if (q < 1) {
							// Apply displacements
							Point rijv = new Point(this.x - neig.content.get(i).x, this.y - neig.content.get(i).y).normalizeIfNonZero();
							Point D = rijv.multiply(P * (1 - q) + Pnear * Math.pow((1 - q), 2));
							neig.content.get(i).x += D.pp()[0] / 2;
							neig.content.get(i).y += D.pp()[1] / 2;
							dx.add(D.multiply(0.5d));
						}
					}
				}
			}

		this.x -= dx.pp()[0];
		this.y -= dx.pp()[1];
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
			x = changeContainer(newparent, false) ? x + 1 : 0f;
			haschanged = true;
		} else if (x > 1) {
			UniverseCell newparent = container.getNeighbour(1, 0);
			x = changeContainer(newparent, false) ? x - 1 : 1f;
			haschanged = true;
		} else if (y < 0) {
			UniverseCell newparent = container.getNeighbour(0, -1);
			y = changeContainer(newparent, true) ? y + 1 : 0f;
			haschanged = true;
		} else if (y > 1) {
			UniverseCell newparent = container.getNeighbour(0, 1);
			y = changeContainer(newparent, true) ? y - 1 : 1f;
			haschanged = true;
		}
		if (haschanged){ // Remove this if recursion is lagging, technically works without it.
			rebound();
			timeincontainer = 0;
		}
			
	}

	private boolean changeContainer(UniverseCell newparent, boolean floor) {
		if (newparent == null || newparent.type == UniverseCell.SOLID) {
			if (newparent.type == UniverseCell.SOLID) {
				if (floor)
					velocity.pp()[1] = -velocity.pp()[1];
				else
					velocity.pp()[0] = -velocity.pp()[0];
				velocity.multiply(0.5d);
			}
			return false;
		}
		container.content.remove(this);
		this.container = newparent;
		container.content.add(this);
		return true;
	}

}
