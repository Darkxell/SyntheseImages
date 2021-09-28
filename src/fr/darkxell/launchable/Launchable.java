package fr.darkxell.launchable;

import java.awt.image.BufferedImage;

import fr.darkxell.engine.Camera;
import fr.darkxell.engine.LightSource;
import fr.darkxell.engine.Point;
import fr.darkxell.engine.Scene;
import fr.darkxell.engine.shapes.Cube;
import fr.darkxell.engine.shapes.Sphere;
import fr.darkxell.utility.Filesutility;

public class Launchable {

	public static void main(String[] args) {

//		System.out.println("Hello world.");
//
//		String s = Filesutility.readFile("C:\\Users\\ncandela\\Desktop\\docs\\test.txt");
//		System.out.println(s);
//
//		BufferedImage image = Filesutility.readImage("C:\\Users\\ncandela\\Desktop\\docs\\test.png");
//		System.out.println(image == null ? "Null image pointer" : image.getHeight() + "x" + image.getWidth());

//		GraphConsole gc = new GraphConsole();
//		gc.display();
//		gc.print("Hello world!");

		Scene scene = new Scene();
		scene.camera = new Camera(new Point(0, 0, 0), new Point(3, 0, 0));
		// scene.elements.add(new Sphere(new Point(15d, 0d, 1d), 1.6f));
		//scene.elements.add(new Sphere(new Point(16d, 0.5d, -0.5d), 1.2f));
		scene.elements.add(new Cube(new Point(16d, 0.5d, -0.5d), 1f, 1f, 1f));

		scene.lights.add(new LightSource(new Point(15d, -1d, -3d)));
		scene.elements.add(new Sphere(new Point(15d, -1.4d, -3d), 0.15f));

		scene.lights.add(new LightSource(new Point(15d, 1.8d, 2.5d), 30));
		scene.elements.add(new Sphere(new Point(15d, 2d, 2.5d), 0.15f));

		BufferedImage img = scene.render();
		Filesutility.saveImage(img);
	}

	/** Tp1, made a function for quick collapse in eclipse. Try me. */
	@SuppressWarnings("unused")
	private static void tp1() {
		Point person0 = new Point(0, 0, 4000);
		Point vit0 = new Point(50, 0, 0);

		for (int i = 0; i < 60; i++) {

			double C = 0.37f;
			double m = 10000d; // mass of the person, in grams

			float t = ((float) i) / 10; // time spent since t=0, in seconds

			Point frottements = vit0.isZero() ? new Point(0, 0, 0)
					: vit0.clone().normalize().multiply(vit0.normSquared()).multiply(C).multiply(1 / m);
			System.out.println("Frottements : " + frottements);

			Point a = frottements.add(new Point(0, 0, -9.8));
			System.out.println("Acceleration : " + frottements);

			vit0.add(a.multiply(t));
			System.out.println("Speed : " + vit0);
			person0.add(vit0.multiply(t));
			System.out.println("Position : " + person0);

			System.out.println("----------------------------------------------------------------------------");
		}
	}

}
