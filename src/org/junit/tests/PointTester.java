package org.junit.tests;

import org.junit.Test;

import fr.darkxell.engine.raytracing.Point;
import fr.darkxell.utility.MathUtil;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class PointTester extends TestCase {

	public static void main(String[] args) {
		TestSuite suite = new TestSuite("Tous les tests");
		suite.addTestSuite(PointTester.class);
		TestRunner.run(suite);
	}

	@Test
	public void testAddition() {
		Point a = new Point(4, 5, 6);
		Point b = new Point(12, 11, -3);
		a.add(b);

		assertEquals((int) a.x(), 16);
		assertEquals((int) a.y(), 16);
		assertEquals((int) a.z(), 3);
	}

	@Test
	public void testSubstraction() {
		Point a = new Point(4, 5, 6);
		Point b = new Point(12, 11, -3);
		a.substract(b);

		assertEquals((int) a.x(), -8);
		assertEquals((int) a.y(), -6);
		assertEquals((int) a.z(), 9);
	}

	@Test
	public void testMultiply() {
		Point a = new Point(4, 5, 6);
		a.multiply(2);

		assertEquals((int) a.x(), 8);
		assertEquals((int) a.y(), 10);
		assertEquals((int) a.z(), 12);
	}

	@Test
	public void testNormalize() {
		Point a = new Point(55, 16, 31);
		Point b = a.clone();
		double n = a.norm();
		a.multiply(1 / n);

		b.normalize();

		double norma = a.norm();
		double normb = b.norm();

		assertTrue(norma >= normb - 0.001d && norma <= normb + 0.001d);
	}

	@Test
	public void testNormalizeUltra() {
		Point a = new Point(55, 16, 31);
		Point b = a.clone();
		double n = a.norm();
		a.multiply(1 / n);

		b.normalizeUltra();

		double norma = a.norm();
		double normb = b.norm();

		assertTrue(norma >= normb - 0.001d && norma <= normb + 0.001d);
	}

	@Test
	public void testScalar() {
		Point a = new Point(1, 0);
		Point b = new Point(4, 5);
		double s = a.scalarproduct(b);

		assertEquals(s, 4d);
	}

	@Test
	public void testMathNAN() {
//		System.out.println("0d/0d : " + (double) (0d / 0d));
//		System.out.println("1d/0d : " + (double) (1d / 0d));
//		System.out.println("-1d/0d : " + (double) (-1d / 0d));
//		System.out.println(Double.valueOf((double) (0d / 0d)).equals(Double.valueOf(Double.NaN)));
//
//		System.out.println(Double.NaN == Double.NaN);
		
		assertEquals(0d / 0d, Double.NaN);
		assertEquals(1d / 0d, Double.POSITIVE_INFINITY);
		// this is some wild fuckery.
		assertEquals(Double.NaN, MathUtil.ieeemax(Double.NaN, Double.NaN));
		assertEquals(Double.NaN, MathUtil.ieeemax(0d / 0d, 0d / 0d));
		assertEquals(Double.POSITIVE_INFINITY, MathUtil.ieeemax(1d / 0d, 0d / 0d));
		assertEquals(Double.POSITIVE_INFINITY, MathUtil.ieeemax(0d / 0d, 1d / 0d));
		assertEquals(Double.NEGATIVE_INFINITY, MathUtil.ieeemax(-1d / 0d, 0d / 0d));
		assertEquals(Double.NEGATIVE_INFINITY, MathUtil.ieeemin(-1d / 0d, 0d / 0d));
	}

}
