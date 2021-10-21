package fr.darkxell.launchable;

import java.awt.image.BufferedImage;

import fr.darkxell.engine.raytracing.Camera;
import fr.darkxell.engine.raytracing.LightSource;
import fr.darkxell.engine.raytracing.Point;
import fr.darkxell.engine.raytracing.Scene;
import fr.darkxell.engine.raytracing.materials.ColorDouble;
import fr.darkxell.engine.raytracing.materials.Material;
import fr.darkxell.engine.raytracing.shapes.Cube;
import fr.darkxell.engine.raytracing.shapes.Mesh;
import fr.darkxell.engine.raytracing.shapes.Sphere;
import fr.darkxell.engine.raytracing.shapes.Triangle;
import fr.darkxell.front.GraphConsole;

public class Launchable {

	public static GraphConsole gc;

	public static void main(String[] args) {
		gc = new GraphConsole();
		gc.p("Hello world!");
//		String s = Filesutility.readFile("C:\\Users\\ncandela\\Desktop\\docs\\test.txt");
//
//		BufferedImage image = Filesutility.readImage("C:\\Users\\ncandela\\Desktop\\docs\\test.png");
//		System.out.println(image == null ? "Null image pointer" : image.getHeight() + "x" + image.getWidth());
		build3Dscene();
	}

	private static void build3Dscene() {
		gc.p("Building scene...");

		Scene scene = new Scene();
		scene.camera = new Camera(new Point(0, 0, 0), new Point(3, 0, 0));
		scene.camera.width = 200 * 2;
		scene.camera.height = 130 * 2;
		scene.camera.antialiasing = Camera.ANTIALIASING_OFF;

		// Bounding cubes
		Cube c = new Cube(new Point(12.5d, 0d, 6d), 6f, 6f, 6f);
		c.setMat(Material.PRESET_DEFAULTSLIGHTBLUE);
		scene.elements.add(c);
		c = new Cube(new Point(12.5d, 0d, -6d), 6f, 6f, 6f);
		c.setMat(Material.PRESET_DEFAULTSLIGHTGREEN);
		scene.elements.add(c);
		c = new Cube(new Point(18.5d, 0d, 0d), 6f, 6f, 6f);
		c.setMat(Material.PRESET_DEFAULTSLIGHTRED);
		scene.elements.add(c);

		scene.elements.add(new Cube(new Point(12.5d, 6d, 0d), 6f, 6f, 6f));
		scene.elements.add(new Cube(new Point(12.5d, -6d, 0d), 6f, 6f, 6f));

		Sphere s = new Sphere(new Point(9.1d, 0.5d, 1.3d), 1.1f);
		s.setMat(Material.PRESET_MERCURY);
		scene.elements.add(s);

		Triangle t = new Triangle(new Point(13, -3, -0.5), new Point(15.5, -3, -1.2), new Point(15.5, -1, -0.5));
		t.setMat(Material.PRESET_DEFAULTSLIGHTRED);
		scene.elements.add(t);
		t = new Triangle(new Point(13, -3, -0.5), new Point(15.5, -1, -0.5), new Point(15.5, -3, 0.2));
		t.setMat(Material.PRESET_DEFAULTSLIGHTRED);
		scene.elements.add(t);

		s = new Sphere(new Point(13.5d, 0.5d, -0.9d), 1f);
		s.setMat(Material.PRESET_POLISHEDGOLD);
		scene.elements.add(s);

		s = new Sphere(new Point(12.5d, -1.05d, 0.9d), 0.8f);
		s.setMat(new Material(0, 0, new ColorDouble(255, 189, 114)));
		scene.elements.add(s);

		c = new Cube(new Point(10.5d, 2d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.setMat(Material.PRESET_REFCYAN);
		scene.elements.add(c);
		c = new Cube(new Point(10.5d, 1d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.setMat(Material.PRESET_REFCYAN);
		scene.elements.add(c);
		c = new Cube(new Point(10.5d, 0d, -1.9d), 0.7f, 0.7f, 0.7f);
		c.setMat(Material.PRESET_REFCYAN);
		scene.elements.add(c);

//		Mesh m = new Mesh("C:\\Users\\ncandela\\Desktop\\docs\\offmodels\\bunny.off", 11d, -1.4d, -1.9d, 6);
//		m.setMat(Material.PRESET_REFCYAN);
//		scene.elements.add(m);

		// Background cube
		c = new Cube(new Point(-201d, 0d, 0d), 200f, 200f, 200f);
		c.setMat(Material.PRESET_GLOWYPURPLE);
		scene.elements.add(c);

		scene.lights.add(new LightSource(new Point(9d, -1d, -2d), 60, 1));
		scene.elements.add(new Sphere(new Point(9d, -1.4d, -2d), 0.15f));

		scene.lights.add(new LightSource(new Point(12d, -2.1d, -1.8d), 60, 1));
		scene.elements.add(new Sphere(new Point(12d, -2.3d, -1.8d), 0.15f));

		scene.lights.add(new LightSource(new Point(10d, 1.8d, 2.5d), 30, 1));
		scene.elements.add(new Sphere(new Point(10d, 2d, 2.5d), 0.15f));

		BufferedImage img = scene.renderMulti(4);
//		Filesutility.saveImage(img);
		gc.p(img);
	}

}
