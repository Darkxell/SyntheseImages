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
		if (content.getHeight() < 125)
			g2d.drawImage(content, x + 3, y + 3, content.getWidth() * 4, content.getHeight() * 4, null);
		else if (content.getHeight() < 250)
			g2d.drawImage(content, x + 3, y + 3, content.getWidth() * 2, content.getHeight() * 2, null);
		else
			g2d.drawImage(content, x + 3, y + 3, null);
	}

	@Override
	public int getHeight() {
		if (content.getHeight() < 125)
			return content.getHeight() * 4 + 6;
		if (content.getHeight() < 250)
			return content.getHeight() * 2 + 6;
		return content.getHeight() + 6;
	}

}
