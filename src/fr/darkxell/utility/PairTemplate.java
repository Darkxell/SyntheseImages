package fr.darkxell.utility;

public class PairTemplate<U, V> {

	/**
	 * The first element of this <code>Pair</code>
	 */
	public U left;

	/**
	 * The second element of this <code>Pair</code>
	 */
	public V right;

	/**
	 * Constructs a new <code>Pair</code> with the given values.
	 * 
	 * @param first  the first element
	 * @param second the second element
	 */
	public PairTemplate(U first, V second) {

		this.left = first;
		this.right = second;
	}

}
