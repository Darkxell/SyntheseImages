package fr.darkxell.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import fr.darkxell.engine.materials.Material;
import fr.darkxell.utility.MathUtil;
import fr.darkxell.utility.PairTemplate;

public class Scene {

	/** The elements of the scene. Spheres, ext... */
	public ArrayList<SceneElement> elements;
	/** List of light sources in the scene. */
	public ArrayList<LightSource> lights;
	public Camera camera;

	public Scene(ArrayList<SceneElement> elements) {
		this.elements = elements;
		this.lights = new ArrayList<>(5);
	}

	public Scene() {
		this.elements = new ArrayList<>(5);
		this.lights = new ArrayList<>(5);
	}

	private Random rand = new Random();

	private Color skyboxerrorcolor = Color.PINK;

	public BufferedImage render() {
		long start = System.currentTimeMillis();
		System.out.println("Render started in thread " + Thread.currentThread().getName() + "... Processing.");
		BufferedImage img = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(skyboxerrorcolor);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);
		for (int i = 0; i < camera.width; ++i) {
			for (int j = 0; j < camera.height; ++j) {
				g.setColor(computePixelForRaster(i, j));
				g.fillRect(i, j, 1, 1);
			}
			if (i % 37 == 0)
				System.out.println("Rendered " + i + "/" + img.getWidth() + " columns so far ("
						+ ((float) i / (float) img.getWidth() * 100) + "%)");
		}
		g.dispose();
		System.out.println("Render complete in " + ((System.currentTimeMillis() - start) / 1000d)
				+ " seconds for thread " + Thread.currentThread().getName() + ".");
		return img;
	}

	private Color computePixelForRaster(int i, int j) {
		float toreturn_r = 0, toreturn_g = 0, toreturn_b = 0;
		float aliasingmultiplier = 1f / camera.antialiasing;
		for (int aliasingcounter = 0; aliasingcounter < camera.antialiasing; ++aliasingcounter) {
			Point rasterpixel = camera.rasterPixel(i, j);
			Point direction = rasterpixel.clone().substract(camera.origin).normalize();
			Color aapixel = computePixelFor(camera.origin, direction, camera.refractions);
			toreturn_r += aapixel.getRed() * aliasingmultiplier;
			toreturn_g += aapixel.getGreen() * aliasingmultiplier;
			toreturn_b += aapixel.getBlue() * aliasingmultiplier;
		}
		return new Color((int) toreturn_r, (int) toreturn_g, (int) toreturn_b);
	}

	private Color computePixelFor(Point v_ori, Point v_dir, int recursion) {
		PairTemplate<Float, SceneElement> hit = hitScan(v_ori, v_dir);
		float pixeldepth = hit == null ? Float.MAX_VALUE : hit.left;
		SceneElement intersectElement = hit == null ? null : hit.right;
		if (intersectElement == null) {
			return skyboxerrorcolor;
		}

		Point collision = v_dir.clone().multiply(pixeldepth).add(v_ori);
		Point normal = intersectElement.normal(collision);
		switch (intersectElement.mat.reflection) {
		case Material.REFLECTION_TRANSPARENT:
			return skyboxerrorcolor;
		// break;
		case Material.REFLECTION_REFLECTIVE:
			if (recursion >= 0) {
				Point reflection = v_dir.reflection(normal);
				System.out.println( "Reflection : " + reflection + " for direction vector " + v_dir + "(normal was " + normal + ")");
				collision.add(reflection.clone().multiply(0.001d));
				return computePixelFor(collision, reflection, recursion - 1);
			} // If recursion is over, behave as a regular material
		case Material.REFLECTION_MAT:
			double totalintensity = 0d;
			for (int l = 0; l < lights.size(); ++l)
				totalintensity += computeLightOnElement(lights.get(l), collision, normal);
			float inter = MathUtil.grad255(0.0001f, 8f, (float) totalintensity) / 255f;
			Color matcolor = intersectElement.mat.color;
			return new Color((int) (matcolor.getRed() * inter), (int) (matcolor.getGreen() * inter),
					(int) (matcolor.getBlue() * inter));
		}
		System.err.println("Material error, returning skybox error color");
		return skyboxerrorcolor;
	}

	/** Returns the intensity of a light on a given point */
	private double computeLightOnElement(LightSource ls, Point point, Point normal) {
		double toreturn = 0d;
		// Iterate N times on values next to the light source
		for (int liter = 0; liter < ls.fuzziness; ++liter) {
			Point rdev = new Point(rand.nextDouble() * ls.radius * 2 - ls.radius,
					rand.nextDouble() * ls.radius * 2 - ls.radius, rand.nextDouble() * ls.radius * 2 - ls.radius);
			Point efflightpose = ls.pos.clone().add(rdev);
			// Normalized vector containing the direction from the collision vector towards
			// the light
			Point lightdirection = efflightpose.clone().substract(point).normalize();
			// Small padding towards the light to avoid collision with the element that
			// started the ray
			Point collisionpadded = point.clone().add(lightdirection.clone().multiply(0.001d));
			boolean isblocked = false;
			for (int m = 0; m < elements.size(); m++) {
				Optional<Float> lightersect = elements.get(m).intersect(collisionpadded, lightdirection);
				if (!lightersect.isEmpty() && Math.pow(lightersect.get().floatValue(), 2) < efflightpose.clone()
						.substract(collisionpadded).normSquared()) {
					isblocked = true;
					break;
				}
			}
			if (!isblocked) {
				double lighting = (Math.abs(normal.scalarproduct(lightdirection)) * ls.intensity)
						/ (Math.PI * point.clone().substract(ls.pos).norm());
				toreturn += lighting / ls.fuzziness;
			}
		}
		return toreturn;
	}

	/**
	 * @return a Pair of distance + Element if this ray hits something in this
	 *         scene. null if it hits nothing. If the ray hits multiple elements,
	 *         this method returns the one with the closest hit to the origin point.
	 */
	private PairTemplate<Float, SceneElement> hitScan(Point rayOri, Point rayDir) {
		float pixeldepth = Float.MAX_VALUE;
		SceneElement intersectElement = null;
		for (int k = 0; k < elements.size(); k++) {
			Optional<Float> intersect = elements.get(k).intersect(camera.origin, rayDir);
			if (!intersect.isEmpty() && intersect.get().floatValue() > 0 && pixeldepth > intersect.get().floatValue()) {
				intersectElement = elements.get(k);
				pixeldepth = intersect.get().floatValue();
			}
		}
		if (intersectElement != null)
			return new PairTemplate<>(pixeldepth, intersectElement);
		else
			return null;
	}

}
