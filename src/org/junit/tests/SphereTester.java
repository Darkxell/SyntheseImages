package org.junit.tests;

import java.util.Optional;

import org.junit.Test;

import fr.darkxell.engine.Point;
import fr.darkxell.engine.shapes.Sphere;
import fr.darkxell.utility.MathUtil;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class SphereTester extends TestCase {

	public static void main(String[] args) {
		TestSuite suite = new TestSuite("Tous les tests");
		suite.addTestSuite(SphereTester.class);
		TestRunner.run(suite);
	}

	@Test
	public void testIntersect() {
		Point x = new Point(0, 0, 0);
		Point dir = new Point(1, 0, 0);
		Point dir3 = new Point(1, 1, 1);

		Sphere s1 = new Sphere(new Point(5, 0, 0), 1);
		Optional<Float> intersect1 = s1.intersect(x, dir);
		assertEquals(4f, intersect1.isEmpty() ? 0 : intersect1.get().floatValue());

		Sphere s2 = new Sphere(new Point(10, 10, 10), 2);
		Optional<Float> intersect2 = s2.intersect(x, dir3);
		assertEquals(8.8453f, intersect2.isEmpty() ? 0 : intersect2.get().floatValue());

		Sphere s3 = new Sphere(new Point(-5, 0, 0), 5.5f);
		Optional<Float> intersect3 = s3.intersect(x, dir);
		assertEquals(0.5f, intersect3.isEmpty() ? 0 : intersect3.get().floatValue());
	}

	@Test
	public void testGrad() {
		assertEquals(255f, MathUtil.gradN(0, 10, 11, 0, 255));
		assertEquals(0f, MathUtil.gradN(0, 10, -2, 0, 255));
		assertTrue(MathUtil.gradN(0, 10, 8, 0, 255) > MathUtil.gradN(0, 10, 3, 0, 255));
	}

	@Test
	public void testNormal() {
		Sphere s = new Sphere(new Point(0, 0, 0), 5f);
		Point n = s.normal(new Point(5, 0, 0));

		assertEquals(n.x(), 1d);
		assertEquals(n.y(), 0d);
		assertEquals(n.z(), 0d);
	}

}
