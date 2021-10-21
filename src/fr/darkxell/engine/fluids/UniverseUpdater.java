package fr.darkxell.engine.fluids;

public class UniverseUpdater implements Runnable {

	private ChunkedUniverse universe;

	public UniverseUpdater(ChunkedUniverse universe) {
		this.universe = universe;
	}

	/**
	 * Amount of miliseconds betwen two ticks of this updater. <br/>
	 * 20 = 50 ticks per second
	 */
	private static int MILIDELTA = 20;

	/** Reads the updates methods and refreshes the canvas periodically. */
	public void run() {
		long milistart = System.currentTimeMillis();
		int frame = 0;
		for (;;) {
			while (milistart + (frame * MILIDELTA) > System.currentTimeMillis()) {
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
			++frame;

			// Update
			try {
				universe.tick();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Print
			try {
				universe.print();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
