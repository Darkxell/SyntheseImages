package fr.darkxell.front;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GraphConsole {

	private JFrame frame;
	private TransPane pane;
	private BufferedImage consoleBuffer;

	public GraphConsole() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frame = new JFrame();
		// frame.setBackground(new Color(0, 0, 0, 0));
		this.pane = new TransPane(800, 600, this);
		this.consoleBuffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		frame.add(this.pane);
		frame.setLocationRelativeTo(null);
		frame.setName("Gabriel console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (;;) {
						Thread.sleep(16);
						tick();
					}
				} catch (Exception e) {
				}
			}
		});
		t.run();

	}

	/** Shifts the console up by a line, and prints the text parameter to it. */
	public void print(String text) {
		BufferedImage framebuffer = new BufferedImage(this.consoleBuffer.getWidth(), this.consoleBuffer.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) framebuffer.getGraphics().create();
		g2d.drawImage(this.consoleBuffer, 0, -30, null);
		g2d.setColor(Color.BLACK);
		g2d.drawString(text, 40, 40);
		g2d.dispose();
		this.consoleBuffer = framebuffer;
	}

	/** Called 60 times per second by an independent updater. */
	private void tick() {
		pane.repaint();
	}

	private class TransPane extends JPanel {

		private static final long serialVersionUID = -8878819377156064029L;
		private int width, height;
		private final GraphConsole parent;

		public TransPane(int width, int height, GraphConsole parent) {
			setOpaque(false);
			setLayout(new GridBagLayout());
			this.width = width;
			this.height = height;
			this.parent = parent;
		}

		public void customResize(int width, int height) {
			this.width = width;
			this.height = height;
			this.setSize(width, height);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(this.width, this.height);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage(consoleBuffer, 0, 0, null);
			g2d.dispose();
		}

	}

}
