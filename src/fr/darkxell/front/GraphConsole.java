package fr.darkxell.front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial") // Debug class, whatever.
public class GraphConsole extends JFrame {

	private static final int DEFAULT_WIDTH = 840;
	private static final int DEFAULT_HEIGHT = 600;

	protected int currentWidth = DEFAULT_WIDTH;
	protected int currentHeight = DEFAULT_HEIGHT;

	protected ArrayList<ConsoleLine> content = new ArrayList<>(200);

	public GraphConsole() {
		setTitle("Graph Console");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		InCanvas component = new InCanvas();
		add(component);
		getContentPane().validate();
		getContentPane().repaint();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (;;) {
						Thread.sleep(40);
						repaint();
					}
				} catch (Exception e) {
				}
			}
		});
		t.start();
	}

	/** Prints the wanted bufferedImage onto the console */
	public void p(Object obj) {
		if (obj instanceof BufferedImage) {
			content.add(new ImgLine((BufferedImage) obj));
		} else {
			content.add(new StringLine(obj));
		}
	}

	private class InCanvas extends JComponent {

		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			int heightiter = getHeight() - 20;
			for (int i = content.size() - 1; i >= 0 && heightiter > 0; i--) {
				heightiter -= content.get(i).getHeight() + 2;
				g2d.setColor(new Color(230, 230, 230));
				g2d.fillRect(15, heightiter, getWidth() - 40, content.get(i).getHeight());
				content.get(i).print(g2d, 20, heightiter);
			}
		}
	}

}
