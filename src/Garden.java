import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public class Garden {
	private int fSize;
	private  PVector pos;
	private Color bgcolor, fPetal, fCentre;
	private Rectangle2D.Double bg;
	private Ellipse2D.Double p1;
	private Ellipse2D.Double p2;
	private Ellipse2D.Double p3;
	private Ellipse2D.Double c1;
	private Ellipse2D.Double p11;
	private Ellipse2D.Double p12;
	private Ellipse2D.Double p13;
	private Ellipse2D.Double c2;
	private Ellipse2D.Double p21;
	private Ellipse2D.Double p22;
	private Ellipse2D.Double p23;
	private Ellipse2D.Double c3;
	
	public Garden () {
		fSize = 20;
		pos = new PVector (RatPanel.pWidth/2, RatPanel.pHeight/2);
		bgcolor = new Color(51,105,53);
		fPetal = new Color(255,255,255);
		fCentre = new Color(255,255,0);
		bg = new Rectangle2D.Double(-RatPanel.pWidth/2 + RatPanel.borders, -RatPanel.pHeight/2 +
				RatPanel.borders, RatPanel.pWidth - RatPanel.borders * 2, RatPanel.pHeight - RatPanel.borders * 2);
		p1 = new Ellipse2D.Double(-120, -170, fSize, fSize);
		p2 = new Ellipse2D.Double(-105, -150, fSize, fSize);
		p3 = new Ellipse2D.Double(-135, -150, fSize, fSize);
		c1 = new Ellipse2D.Double(-120, -155, fSize, fSize);
		p11 = new Ellipse2D.Double(100, -120, fSize, fSize);
		p12 = new Ellipse2D.Double(85, -100, fSize, fSize);
		p13 = new Ellipse2D.Double(115, -100, fSize, fSize);
		c2 = new Ellipse2D.Double(100, -105, fSize, fSize);
		p21 = new Ellipse2D.Double(-180, 140, fSize, fSize);
		p22 = new Ellipse2D.Double(-165, 120, fSize, fSize);
		p23 = new Ellipse2D.Double(-195, 120, fSize, fSize);
		c3 = new Ellipse2D.Double(-180, 125, fSize, fSize);
	}
	
	public void drawGarden (Graphics2D g2) {
		//draw Background rectangle
		AffineTransform op = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.setColor(bgcolor);
		g2.fill(bg);
		
		//petals
		g2.setColor(fPetal);
		g2.fill(p1);
		g2.fill(p2);
		g2.fill(p3);
		g2.fill(p11);
		g2.fill(p12);
		g2.fill(p13);
		g2.fill(p21);
		g2.fill(p22);
		g2.fill(p23);
		
		//center
		g2.setColor(fCentre);
		g2.fill(c1);
		g2.fill(c2);
		g2.fill(c3);
		g2.setTransform(op);
	}

}
