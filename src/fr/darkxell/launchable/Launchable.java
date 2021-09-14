package fr.darkxell.launchable;

import java.awt.image.BufferedImage;

import fr.darkxell.front.GraphConsole;
import fr.darkxell.utility.Filesutility;

public class Launchable {

	public static void main(String[] a) {
		System.out.println("hw");

		String s = Filesutility.readFile("C:\\Users\\ncandela\\Desktop\\test.txt");
		System.out.println(s);

		BufferedImage image = Filesutility.readImage("C:\\Users\\ncandela\\Desktop\\test.png");

		System.out.println(image == null ? "Null image pointer" : image.getHeight() + "x" + image.getWidth());
		
		GraphConsole gc = new GraphConsole();
		
		//gc.display();

		
		//gc.print("Hello world!");
	}

}
