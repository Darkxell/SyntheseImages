package fr.darkxell.front;

import java.awt.Color;
import java.awt.Graphics2D;

public class StringLine extends ConsoleLine {

	public final String content;
	private String[] contentsplit;

	public StringLine(Object o) {
		content = o.toString();
		contentsplit = content.split("\n");
	}

	public StringLine(int o) {
		content = "" + o;
		contentsplit = content.split("\n");
	}

	public StringLine(double o) {
		content = "" + o;
		contentsplit = content.split("\n");
	}

	public StringLine(float o) {
		content = "" + o;
		contentsplit = content.split("\n");
	}

	public StringLine(byte o) {
		content = "" + o;
		contentsplit = content.split("\n");
	}

	@Override
	public int getHeight() {
		return contentsplit.length * 12 + 4;
	}

	@Override
	public void print(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < contentsplit.length; i++) {
			g2d.drawString(contentsplit[i], x, y + 12 * (i + 1));
		}

	}

}
