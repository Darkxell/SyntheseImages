package fr.darkxell.launchable;

import java.awt.Color;
import java.awt.image.BufferedImage;

import fr.darkxell.engine.Camera;
import fr.darkxell.engine.LightSource;
import fr.darkxell.engine.Point;
import fr.darkxell.engine.Scene;
import fr.darkxell.engine.materials.Material;
import fr.darkxell.engine.shapes.Cube;
import fr.darkxell.engine.shapes.Sphere;
import fr.darkxell.front.GraphConsole;

public class Launchable {

	public static GraphConsole gc;
	
	public static void main(String[] args) {

//		String s = Filesutility.readFile("C:\\Users\\ncandela\\Desktop\\docs\\test.txt");
//
//		BufferedImage image = Filesutility.readImage("C:\\Users\\ncandela\\Desktop\\docs\\test.png");
//		System.out.println(image == null ? "Null image pointer" : image.getHeight() + "x" + image.getWidth());

		gc = new GraphConsole();

		Scene scene = new Scene();
		scene.camera = new Camera(new Point(0, 0, 0), new Point(3, 0.0000001d, 0.0000001d));
		scene.camera.width = 200*2;
		scene.camera.height = 120*2;
		scene.camera.antialiasing = Camera.ANTIALIASING_OFF;

		scene.elements.add(new Cube(new Point(12.5d, 0d, 6d), 6f, 6f, 6f));
		scene.elements.add(new Cube(new Point(12.5d, 0d, -6d), 6f, 6f, 6f));
		scene.elements.add(new Cube(new Point(12.5d, 6d, 0d), 6f, 6f, 6f));
		scene.elements.add(new Cube(new Point(12.5d, -6d, 0d), 6f, 6f, 6f));

		scene.elements.add(new Cube(new Point(18.5d, 0d, 0d), 6f, 6f, 6f));

		Sphere s = new Sphere(new Point(12d, 0d, 1.3d), 1.6f);
		s.mat.color = new Color(255, 189, 114);
		scene.elements.add(s);

		s = new Sphere(new Point(14.5d, 0.5d, -0.7d), 1.2f);
		s.mat.color = new Color(163, 255, 248);
		s.mat.reflection = Material.REFLECTION_REFLECTIVE;
		scene.elements.add(s);

		scene.elements.add(new Cube(new Point(13.1d, 2.05d, -1.8d), 0.7f, 0.7f, 0.7f));

		Cube c = new Cube(new Point(-201d, 0d, 0d), 200f, 200f, 200f);
		c.mat.reflection = Material.REFLECTION_GLOWY;
		c.mat.color = Color.RED;
		scene.elements.add(c);

		scene.lights.add(new LightSource(new Point(11d, -1d, -2d)));
		scene.elements.add(new Sphere(new Point(11d, -1.4d, -2d), 0.15f));

		scene.lights.add(new LightSource(new Point(10d, 1.8d, 2.5d), 30));
		scene.elements.add(new Sphere(new Point(10d, 2d, 2.5d), 0.15f));

		BufferedImage img = scene.render();
//		Filesutility.saveImage(img);
		gc.p(img);
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
