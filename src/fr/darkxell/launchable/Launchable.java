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
import fr.darkxell.engine.shapes.Triangle;
import fr.darkxell.front.GraphConsole;
import fr.darkxell.utility.Filesutility;

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
		scene.camera.width = 7680;
		scene.camera.height = 4320;
		scene.camera.antialiasing = Camera.ANTIALIASING_OFF;

		// Bounding cubes
		Cube c = new Cube(new Point(12.5d, 0d, 6d), 6f, 6f, 6f);
		c.mat = Material.PRESET_DEFAULTSLIGHTBLUE;
		scene.elements.add(c);
		c = new Cube(new Point(12.5d, 0d, -6d), 6f, 6f, 6f);
		c.mat = Material.PRESET_DEFAULTSLIGHTGREEN;
		scene.elements.add(c);
		c = new Cube(new Point(18.5d, 0d, 0d), 6f, 6f, 6f);
		c.mat = Material.PRESET_DEFAULTSLIGHTRED;
		scene.elements.add(c);
		
		scene.elements.add(new Cube(new Point(12.5d, 6d, 0d), 6f, 6f, 6f));
		scene.elements.add(new Cube(new Point(12.5d, -6d, 0d), 6f, 6f, 6f));
		
		Sphere s = new Sphere(new Point(9.1d, 0.5d, 1.3d), 1.1f);
		s.mat = Material.PRESET_GLASSPERFECT;
		scene.elements.add(s);
		
		Triangle t = new Triangle(new Point(13, -3, -0.3),new Point(15, -1, -0.9), new Point(14.2, -3, -1.2));
		t.mat = new Material(0, 0, new Color(255, 80, 150));
		scene.elements.add(t);
				
		s = new Sphere(new Point(13.5d, 0.5d, -0.9d), 1f);
		s.mat = Material.PRESET_MIRRORPERFECT;
		scene.elements.add(s);

		s = new Sphere(new Point(12.5d, -1.05d, 0.9d), 0.8f);
		s.mat = new Material(0, 0, new Color(255, 189, 114));
		scene.elements.add(s);

		c = new Cube(new Point(10.5d, 2d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.mat = Material.PRESET_REFCYAN;
		scene.elements.add(c);
		c = new Cube(new Point(10.5d, 1d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.mat = Material.PRESET_REFCYAN;
		scene.elements.add(c);
		c = new Cube(new Point(10.5d, 0d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.mat = Material.PRESET_REFCYAN;
		scene.elements.add(c);

		// Background cube
		c = new Cube(new Point(-201d, 0d, 0d), 200f, 200f, 200f);
		c.mat = Material.PRESET_GLOWYPURPLE;
		scene.elements.add(c);

		scene.lights.add(new LightSource(new Point(9d, -1d, -2d), 60, 1));
		scene.elements.add(new Sphere(new Point(9d, -1.4d, -2d), 0.15f));

		scene.lights.add(new LightSource(new Point(12d, -2.1d, -1.8d), 60, 1));
		scene.elements.add(new Sphere(new Point(12d, -2.3d, -1.8d), 0.15f));

		scene.lights.add(new LightSource(new Point(10d, 1.8d, 2.5d), 30, 1));
		scene.elements.add(new Sphere(new Point(10d, 2d, 2.5d), 0.15f));

		BufferedImage img = scene.renderMulti(4);
		Filesutility.saveImage(img);
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
