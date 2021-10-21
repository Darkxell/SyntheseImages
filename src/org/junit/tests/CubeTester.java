package org.junit.tests;

import java.util.Optional;

import org.junit.Test;

import fr.darkxell.engine.raytracing.HitResult;
import fr.darkxell.engine.raytracing.Point;
import fr.darkxell.engine.raytracing.shapes.Cube;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class CubeTester extends TestCase {

	public static void main(String[] args) {
		TestSuite suite = new TestSuite("Tous les tests");
		suite.addTestSuite(CubeTester.class);
		TestRunner.run(suite);
	}

	@Test
	public void testIntersect() {
		Point x = new Point(0, 0, 0);
		Point dir = new Point(1, 0, 0);
//		Point dir2 = new Point(1, 1, 0);

		Cube c1 = new Cube(new Point(5, 0, 0), 2, 2, 2);
		Optional<HitResult> intersect1 = c1.intersect(x, dir);
		assertEquals(4d, intersect1.isEmpty() ? -999d : intersect1.get().hitDistance);

//		Cube c2 = new Cube(new Point(5, 5, 0), 2, 2, 2);
//		Optional<Float> intersect2 = c2.intersect(x, dir2);
//		assertEquals((float) Math.sqrt(2) * 4f, intersect2.isEmpty() ? 0 : intersect2.get().floatValue());

	}

}
