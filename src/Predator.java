import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Predator extends Rat{

	public Predator(PVector pos, int ratW, int ratH, float scale, Color color) {
		super(pos, ratW, ratH, scale, color);
	}
	
	public void drawRat(Graphics2D g) {
		super.drawRat(g);
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform ms = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		
		float angle = vel.heading();
		g2.rotate(angle);
		
		g2.setColor(new Color(255,0,0));
		g2.fill(body);
		
		g2.setTransform(ms);
	}
	
	/* 
	 * This updated the move class in order for the predator to chase or move normally in the canvas
	 */
	
	public void move(ArrayList<Seed> seeds, Rat rat) {
		super.move(seeds, rat);
		if (run) {
			long time = System.currentTimeMillis();
			if (time - runTimer > runDuration) {
				chasingMode(rat);
				run = false;
			}
		}
		
		else {
			vel.limit(speedCeiling);
			
			PVector gracefulTurn = turnForce().div((float)scale);
			
			float speed = vel.mag();
			
			vel.add(gracefulTurn);
					
			vel.normalize().mult(speed);
	
			pos.add(vel);
		}

	}
	
	/* 
	 * The eat method is created in this class to detect the collision with another rat and "Eat" it
	 */
	public boolean eat(Rat Rat2) {
		if (checkHeadOn(Rat2)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public boolean checkHeadOn(Rat Rat2) {
		if (vel.x * Rat2.vel.x < 0 && vel.y * Rat2.vel.y < 0) {
			return true;
		}
		return false;
	}
	
	/* 
	 * chasing mode is when the predator detects a prey and calculates the path to it in order to go after it
	 */
	public void chasingMode (Rat rat2) {
		if (detect(rat2)) {
		PVector approach = rat2.getRat();
		PVector path = PVector.sub(approach, getRat());
		path.normalize();
		vel.add(path);
		
		vel.limit(speedCeiling);
		
		PVector gracefulTurn = turnForce().div((float)scale);
		
		float speed = vel.mag();
		
		vel.add(gracefulTurn);
				
		vel.normalize().mult(speed);

		pos.add(vel);}
	}


}
