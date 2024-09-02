import java.awt.*;
import java.awt.geom.Ellipse2D;

import processing.core.PVector;

public class Seed {
	private Ellipse2D.Double body;
	private Ellipse2D.Double hole1;
	private Ellipse2D.Double hole2;
	
	private double size;
	private PVector pos;
	
	public Seed(Point pos, double size){
		this.pos = new PVector(pos.x, pos.y);
		this.size = size;
		body = new Ellipse2D.Double(pos.x, pos.y, size*1.5, size);
		hole1 = new Ellipse2D.Double(pos.x + size/2 - size/4, pos.y + size/2, size/3, size/3);
		hole2 = new Ellipse2D.Double(pos.x + size - size/4,pos.y + size/2, size/3, size/3);
	}
	
	public void drawSeed(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		g2.fill(body);
		
		
		//dots on seed
		g2.setColor(Color.BLACK);
		g2.draw(hole1);
		g2.draw(hole2);
	}
	
	public PVector getSeed() {
		return pos;
	}
	
	
	public double getSize() {
		return size;
	}

}
