package fr.darkxell.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;

import fr.darkxell.utility.MathUtil;

public class Scene {

	public ArrayList<SceneElement> elements;
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
		BufferedImage img = new BufferedImage(camera.width, camera.height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);

		double theta = camera.fov / camera.width;
		for (int i = 0; i < camera.width; ++i)
			for (int j = 0; j < camera.height; ++j) {
				int imod = i - camera.width / 2, jmod = j - camera.height / 2;
				double ti0 = imod * theta, tj0 = jmod * theta;
				double rasterIX = camera.rasterOrigin.x() - (1 - Math.cos(ti0));
				double rasterJY = camera.rasterOrigin.y() + Math.sin(tj0);
				double rasterIZ = camera.rasterOrigin.z() + Math.sin(ti0);

				// Hard coded line for linear raster between ~-15->15 centered in (3,0,0)
//				Point rasterpixel = new Point(3,(double)(imod)/10,(double)(jmod)/10);
				Point rasterpixel = new Point(rasterIX, rasterJY, rasterIZ);

				Point direction = rasterpixel.clone().substract(camera.origin);

				float pixeldepth = Float.MAX_VALUE;
				for (int k = 0; k < elements.size(); k++) {
					Optional<Float> intersect = elements.get(k).intersect(camera.origin, direction);
					if (!intersect.isEmpty() && intersect.get().floatValue() > 0
							&& pixeldepth > intersect.get().floatValue()) {
						pixeldepth = intersect.get().floatValue();
						int inter = (int) MathUtil.rgrad255(4.3f, 5.5f, intersect.get().floatValue());
						g.setColor(new Color(inter, inter, inter));
						g.fillRect(i, j, 1, 1);

						for (int l = 0; l < lights.size(); ++l) {
							Point collision = camera.rasterOrigin.clone().substract(camera.origin).normalize()
									.multiply(pixeldepth);
							if(i==121&&j==16)System.out.println("collision is at: " + collision);//PIXELDEPTH VALUE IS BUGGED HERE. DEFINITIVELY.
							// Normalised vector containing the direction from the collision vector towards
							// the light
							Point lightdirection = lights.get(l).pos.clone().substract(collision).normalize();
							// Small padding towards the light to avoid collision with the element that
							// started the ray
							collision.add(lightdirection.clone().multiply(0.1d));
							boolean isblocked = false;
							for (int m = 0; m < elements.size(); m++) {
								Optional<Float> lightersect = elements.get(m).intersect(collision, lightdirection);
								if (!lightersect.isEmpty() && lightersect.get().floatValue() * lightersect.get()
										.floatValue() < lights.get(l).pos.clone().substract(collision).normSquared()) {
									System.out.println("blocked");
									isblocked = true;
									break;
								}
							}
							if (isblocked) {
								g.setColor(new Color(inter, inter, inter));
								g.fillRect(i, j, 1, 1);
							} else {
								g.setColor(Color.GREEN);
								g.fillRect(i, j, 1, 1);
							}

							// if fine, compute the light color * surface color * inference angle
							// draw the fucking pixel
						}

					}
				}

			}
		g.dispose();
		return img;
	}

}
