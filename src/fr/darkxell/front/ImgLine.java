package fr.darkxell.front;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImgLine extends ConsoleLine {

	public final BufferedImage content;
	
	public ImgLine(BufferedImage img) {
		content = img;
	}

	@Override
	public void print(Graphics2D g2d, int x, int y) {
		g2d.drawImage(content, x+3, y+3, null);
	}

	@Override
	public int getHeight() {
		return content.getHeight()+6;
	}

}
