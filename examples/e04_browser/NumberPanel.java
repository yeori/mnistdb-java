package e04_browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import github.yeori.mnist.db.Mnistlet;

public class NumberPanel extends JComponent {

	private Mnistlet src;
	private static Dimension dim = new Dimension(28, 28);
	private Font indexFont = new Font("Arial", Font.PLAIN, 10);
	
	public NumberPanel(Mnistlet mlet) {
		this.src  = mlet;
		this.setPreferredSize(dim);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int W = getWidth();
		int H = getHeight();
		
		BufferedImage img = src.asImage();
		drawImage(g, img , W, H);
		drawIndex ( g, W, H, String.format("%5d", src.index()) );
	}
	private void drawIndex(Graphics g, int W, int H, String index) {
		g.setColor(Color.BLACK);
		g.setFont(indexFont);
		FontMetrics fm = getFontMetrics(indexFont);
		int fw = fm.stringWidth(index);
		
		g.drawString(index, fw, fm.getHeight());
		
	}
	private void drawImage(Graphics g, BufferedImage img, int W, int H) {
		g.drawImage(img, 0, 0, null);
	}
}
