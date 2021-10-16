package fr.darkxell.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;

import fr.darkxell.engine.materials.ColorDouble;
import fr.darkxell.engine.shapes.SceneElement;
import fr.darkxell.launchable.Launchable;
import fr.darkxell.utility.MathUtil;
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
				try {
					g.setColor(computePixelForRaster(i, j).toAWTColor());
					g.fillRect(i, j, 1, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
							try {
								g.setColor(computePixelForRaster(i, j).toAWTColor());
								g.fillRect(i - offset, j, 1, 1);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (offset == 0 && i % 37 == 31)
							Launchable.gc.p("Completion approximation for render: " + i + "/" + workspace.getWidth()
									+ " columns (" + ((float) i / (float) workspace.getWidth() * 100) + "%)");

					}
					g.dispose();
					finished.iterate();
					Launchable.gc.p(Thread.currentThread().getName() + " work complete: " + finished.get() + "/" + t
							+ " finished.");
				}
			});
			thread.setName("Renderer" + tindex);
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
	 * Computes the color seen from a ray in a scene.
	 * 
	 * @return a new instance of color, NOT shared by other resources.
	 */
	private ColorDouble computePixelFor(Point v_ori, Point v_dir, int recursion) {
		HitResult hit = hitScan(v_ori, v_dir);
		if (hit == null)
			return skyboxerrorcolor.clone();
		if (hit.hitElement.getMat().ultrabright)
			return hit.hitElement.getMat().color.clone();

		ColorDouble toreturn = new ColorDouble(0, 0, 0);
		double reflectioncoef = recursion > 0 ? hit.hitElement.getMat().reflection : 0d,
				refractioncoef = recursion > 0 ? hit.hitElement.getMat().refraction : 0d,
				matcoef = MathUtil.clamp(1 - reflectioncoef - refractioncoef, 0, 1);

		if (recursion <= 0 || matcoef > 0d) {
			double totalintensity = 0d;
			for (int l = 0; l < lights.size(); ++l)
				totalintensity += hit.computeLightOnThis(lights.get(l), elements);
			double inter = MathUtil.gradN(0.0001d, 8d, totalintensity, 0.05d, 1d);
			ColorDouble matcolor = hit.hitElement.getMat().color;
			toreturn.add(matcolor.clone().mul(inter).mul(matcoef));
		}

		if (refractioncoef > 0) {
			Point refraction = v_dir.refraction(hit.getNormal(), hit.hitElement.getMat().refractioncoef);
			if (refraction == null) {
				// Case for total refraction
				reflectioncoef += refractioncoef;
				refractioncoef = 0d;
			} else {
				if(hit.hitElement.getMat().fuzziness_refraction > 0d)
					refraction.offsetRandom(hit.hitElement.getMat().fuzziness_refraction);
				toreturn.add(computePixelFor(hit.getLocationEpsilonTowards(refraction), refraction, recursion - 1)
						.mul(refractioncoef));
			}
		}

		if (reflectioncoef > 0) {
			Point reflection = v_dir.reflection(hit.getNormal());
			if(hit.hitElement.getMat().fuzziness_reflection > 0d)
				reflection.offsetRandom(hit.hitElement.getMat().fuzziness_reflection);
			ColorDouble reflectedcolor = computePixelFor(hit.getLocationEpsilonTowards(reflection), reflection,
					recursion - 1);
			toreturn.add(reflectedcolor.mul(reflectioncoef));
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
				intersectElement = intersect.get().hitElement;
				pixeldepth = intersect.get().hitDistance;
			}
		}

		if (intersectElement != null)
			return new HitResult(rayOri, rayDir, pixeldepth, intersectElement);
		else
			return null;
	}

}
