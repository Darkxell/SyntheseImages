package fr.darkxell.utility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Filesutility {

	/**
	 * Buffers a file in ram and returns its content in String format. Don't load
	 * huge shit.
	 */
	public static String readFile(String path) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Path was : " + path);
			return null;
		}
	}

	/**
	 * Returns an awt pixel raster from a binary image file.
	 * 
	 * @see BufferedImage
	 */
	public static BufferedImage readImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveImage(BufferedImage img) {
		File outputfile = new File("C:\\Users\\ncandela\\Desktop\\java_" + System.currentTimeMillis() + ".png");
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
