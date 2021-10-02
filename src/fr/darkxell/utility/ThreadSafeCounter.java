package fr.darkxell.utility;

public class ThreadSafeCounter {

	private int value = 0;
	
	public synchronized void iterate() {
		value++;
	}
	
	public int get() {
		return value;
	}
	
}
