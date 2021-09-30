package fr.darkxell.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import fr.darkxell.utility.MathUtil;

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

	public BufferedImage render() {
		Random rand = new Random();
		long start = System.currentTimeMillis();
		BufferedImage img = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);

		for (int i = 0; i < camera.width; ++i)
			for (int j = 0; j < camera.height; ++j) {
				Point rasterpixel = camera.rasterPixel(i, j);

				Point direction = rasterpixel.clone().substract(camera.origin).normalize();
				// For each element, computes which one this pixel ray intersects
				float pixeldepth = Float.MAX_VALUE;
				SceneElement intersectElement = null;
				for (int k = 0; k < elements.size(); k++) {
					Optional<Float> intersect = elements.get(k).intersect(camera.origin, direction);
					if (!intersect.isEmpty() && intersect.get().floatValue() > 0
							&& pixeldepth > intersect.get().floatValue()) {
						intersectElement = elements.get(k);
						pixeldepth = intersect.get().floatValue();
					}
				}

				// For each light, compute the intensity of that light on the given point.
				double totalintensity = 0d;
				if (intersectElement != null) {
					for (int l = 0; l < lights.size(); ++l) {
						final int fuzzylightiterations = 20;
						Point collision = direction.clone().multiply(pixeldepth).add(camera.origin);
						// Iterate N times on values next to the light source
						for (int liter = 0; liter < fuzzylightiterations; ++liter) {
							Point rdev = new Point(rand.nextDouble() * lights.get(l).radius * 2 - lights.get(l).radius,
									rand.nextDouble() * lights.get(l).radius * 2 - lights.get(l).radius,
									rand.nextDouble() * lights.get(l).radius * 2 - lights.get(l).radius);
							Point efflightpose = lights.get(l).pos.clone().add(rdev);
							// Normalised vector containing the direction from the collision vector towards
							// the light
							Point lightdirection = efflightpose.clone().substract(collision).normalize();
							// Small padding towards the light to avoid collision with the element that
							// started the ray
							Point collisionpadded = collision.clone().add(lightdirection.clone().multiply(0.01d));
							boolean isblocked = false;
							for (int m = 0; m < elements.size(); m++) {
								Optional<Float> lightersect = elements.get(m).intersect(collisionpadded,
										lightdirection);
								if (!lightersect.isEmpty() && Math.pow(lightersect.get().floatValue(), 2) < efflightpose
										.clone().substract(collisionpadded).normSquared()) {
									isblocked = true;
									break;
								}
							}
							if (!isblocked) {
								double lighting = (Math
										.abs(intersectElement.normal(collision).scalarproduct(lightdirection))
										* lights.get(l).intensity)
										/ (Math.PI * collision.clone().substract(lights.get(l).pos).norm());
								totalintensity += lighting / fuzzylightiterations;
							}
						}

					}
					// print the pixel with intensity
					float inter = MathUtil.grad255(0.0001f, 8f, (float) totalintensity) / 255f;
					Color matcolor = intersectElement.mat.color;
					g.setColor(new Color((int) (matcolor.getRed() * inter), (int) (matcolor.getGreen() * inter),
							(int) (matcolor.getBlue() * inter)));
					g.fillRect(i, j, 1, 1);
				}
			}
		g.dispose();
		System.out.println("Render complete in " + ((System.currentTimeMillis() - start) / 1000d) + " seconds");
		return img;
	}

}
