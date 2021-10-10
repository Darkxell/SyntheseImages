package fr.darkxell.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import fr.darkxell.engine.materials.ColorDouble;
import fr.darkxell.engine.shapes.SceneElement;
import fr.darkxell.launchable.Launchable;
import fr.darkxell.utility.MathUtil;
import fr.darkxell.utility.PairTemplate;
import fr.darkxell.utility.ThreadSafeCounter;

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

	private ColorDouble skyboxerrorcolor = ColorDouble.SKYPINK;

	/**
	 * Renders this scene trough the view of the camera in it.
	 * 
	 * @return An image containing a visual representation of said view.
	 */
	public BufferedImage render() {
		long start = System.currentTimeMillis();
		Launchable.gc.p("Render started in thread " + Thread.currentThread().getName() + "...\nProcessing.");
		BufferedImage img = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(skyboxerrorcolor.toAWTColor());
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);
		for (int i = 0; i < camera.width; ++i) {
			for (int j = 0; j < camera.height; ++j) {
				g.setColor(computePixelForRaster(i, j).toAWTColor());
				g.fillRect(i, j, 1, 1);
			}
			if (i % 37 == 31)
				Launchable.gc.p("Rendered " + i + "/" + img.getWidth() + " columns so far ("
						+ ((float) i / (float) img.getWidth() * 100) + "%)");
		}
		g.dispose();
		Launchable.gc.p("Render complete in " + ((System.currentTimeMillis() - start) / 1000d) + " seconds for thread "
				+ Thread.currentThread().getName() + ".");
		return img;
	}

	/**
	 * Renders this scene trough the view of the camera in it. This method uses
	 * multiple threads.
	 * 
	 * @param t The numbers of threads to create and use for this job
	 * @return An image containing a visual representation of said view.
	 */
	public BufferedImage renderMulti(int t) {
		if (t <= 1)
			return render();
		long start = System.currentTimeMillis();
		Launchable.gc.p("Multithreaded render started with " + t + " runners...\nGodspeeding.");
		BufferedImage[] response = new BufferedImage[t];
		ThreadSafeCounter finished = new ThreadSafeCounter();

		for (int tindex = 0; tindex < t; tindex++) {
			response[tindex] = new BufferedImage(camera.width / t, camera.height, BufferedImage.TYPE_INT_RGB);
			BufferedImage workspace = response[tindex];
			final int offset = tindex * camera.width / t;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Graphics g = workspace.createGraphics();
					g.setColor(skyboxerrorcolor.toAWTColor());
					g.fillRect(0, 0, workspace.getWidth(), workspace.getHeight());
					g.setColor(Color.BLACK);
					for (int i = offset; i < offset + workspace.getWidth(); ++i) {
						for (int j = 0; j < camera.height; ++j) {
							g.setColor(computePixelForRaster(i, j).toAWTColor());
							g.fillRect(i - offset, j, 1, 1);
						}
						if (offset == 0 && i % 37 == 31)
							Launchable.gc.p("Completion approximation for render: " + i + "/" + workspace.getWidth()
									+ " columns (" + ((float) i / (float) workspace.getWidth() * 100) + "%)");

					}
					g.dispose();
					finished.iterate();
					Launchable.gc.p("Thread work complete: " + finished.get() + "/" + t + " finished.");
				}
			});
			thread.start();
		}

		while (finished.get() < t) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}

		Launchable.gc.p("Multithreads render completed in " + ((System.currentTimeMillis() - start) / 1000d)
				+ " seconds. Now assembling...");
		BufferedImage assembly = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);
		Graphics ga = assembly.getGraphics();
		for (int i = 0; i < response.length; i++) {
			ga.drawImage(response[i], i * camera.width / t, 0, null);
		}
		ga.dispose();
		Launchable.gc.p("Multithreads (" + t + ") render complete in " + ((System.currentTimeMillis() - start) / 1000d)
				+ " seconds total.");
		return assembly;
	}

	private ColorDouble computePixelForRaster(int i, int j) {
		ColorDouble toreturn = new ColorDouble(0d, 0d, 0d);
		double aliasingmultiplier = 1d / camera.antialiasing;
		for (int aliasingcounter = 0; aliasingcounter < camera.antialiasing; ++aliasingcounter) {
			Point rasterpixel = camera.rasterPixel(i, j);
			Point direction = rasterpixel.clone().substract(camera.origin).normalize();
			toreturn.add(computePixelFor(camera.origin, direction, camera.refractions).mul(aliasingmultiplier));
		}
		return toreturn;
	}

	/**
	 * Computes
	 * 
	 * @return a new instance of color, NOT shared by other resources.
	 */
	private ColorDouble computePixelFor(Point v_ori, Point v_dir, int recursion) {
		HitResult hit = hitScan(v_ori, v_dir);
		if (hit == null)
			return skyboxerrorcolor.clone();
		if (hit.hitElement.mat.ultrabright)
			return hit.hitElement.mat.color.clone();

		Point collision = hit.getLocation().clone();
		Point normal = hit.getNormal();

		if (recursion <= 0 || (hit.hitElement.mat.reflection < 1d
				&& hit.hitElement.mat.refraction < 1d) /* FIXME: fuzzy here */) {
			double totalintensity = 0d;
			for (int l = 0; l < lights.size(); ++l)
				totalintensity += computeLightOnElement(lights.get(l), collision, normal);
			double inter = MathUtil.gradN(0.0001d, 8d, totalintensity, 0d, 1d);
			ColorDouble matcolor = hit.hitElement.mat.color;
			return matcolor.clone().mul(inter);
		}

		if (hit.hitElement.mat.refraction > 0) {
			Point refraction = v_dir.refraction(normal, hit.hitElement.mat.refractioncoef);
			if (refraction == null) {
				// Case for total refraction
				Point reflection = v_dir.reflection(normal);
				collision.add(reflection.clone().multiply(0.001d));
				return computePixelFor(collision, reflection, recursion - 1);
			} else {
				collision.add(refraction.clone().multiply(0.001d));
				return computePixelFor(collision, refraction, recursion - 1);
			}
		}

		if (hit.hitElement.mat.reflection > 0) {
			Point reflection = v_dir.reflection(normal);
			collision.add(reflection.clone().multiply(0.001d));
			return computePixelFor(collision, reflection, recursion - 1);
		}

		System.err.println("Material error, returning skybox error color");
		return skyboxerrorcolor.clone();
	}

	/** Returns the intensity of a light on a given point */
	private double computeLightOnElement(LightSource ls, Point point, Point normal) {
		double toreturn = 0d;
		// Iterate N times on values next to the light source
		for (int liter = 0; liter < ls.fuzziness; ++liter) {
			Point efflightpose;
			if (ls.fuzziness > 1) {
				Point rdev = new Point(ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius,
						ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius,
						ThreadLocalRandom.current().nextDouble() * ls.radius * 2 - ls.radius);
				efflightpose = ls.pos.clone().add(rdev);
			} else
				efflightpose = ls.pos;
			// Normalized vector containing the direction from the collision vector towards
			// the light
			Point lightdirection = efflightpose.clone().substract(point).normalize();
			// Small padding towards the light to avoid collision with the element that
			// started the ray
			Point collisionpadded = point.clone().add(lightdirection.clone().multiply(0.001d));
			boolean isblocked = false;
			for (int m = 0; m < elements.size(); m++) {
				Optional<HitResult> lightersect = elements.get(m).intersect(collisionpadded, lightdirection);
				if (!lightersect.isEmpty() && Math.pow(lightersect.get().hitDistance, 2) < efflightpose.clone()
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
	 * @return a HitResult if this ray hits something in this scene. null if it hits
	 *         nothing. If the ray hits multiple elements, this method returns the
	 *         one with the closest hit to the origin point.
	 * @see HitResult
	 */
	private HitResult hitScan(Point rayOri, Point rayDir) {
		double pixeldepth = Double.MAX_VALUE;
		SceneElement intersectElement = null;
		for (int k = 0; k < elements.size(); k++) {
			Optional<HitResult> intersect = elements.get(k).intersect(rayOri, rayDir);
			if (!intersect.isEmpty() && intersect.get().hitDistance > 0 && pixeldepth > intersect.get().hitDistance) {
				intersectElement = elements.get(k);
				pixeldepth = intersect.get().hitDistance;
			}
		}
		if (intersectElement != null)
			return new HitResult(rayOri, rayDir, pixeldepth, intersectElement);
		else
			return null;
	}

}
