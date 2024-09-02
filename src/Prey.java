import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Prey extends Rat{
	
	public Prey(PVector pos, int ratW, int ratH, float scale, Color color) {
		super(pos, ratW, ratH, scale, color);
	}
	
	public void proceedTarget (Seed seed) {
		PVector approach = null;

		if (seed != null)
			approach = seed.getSeed();

		PVector accel = new PVector(0, 0);
		if (approach != null) {
			PVector path = PVector.sub(approach, getRat());
			path.normalize();
			path.mult((float) 0.5);
			vel.add(path);
			pos.add(vel);
		}
		
		vel.add(accel);
		vel.limit(speedCeiling);
		
		PVector gracefulTurn = turnForce().div((float)scale);
		
		float speed = vel.mag();
		
		vel.add(gracefulTurn);
				
		vel.normalize().mult(speed);

		pos.add(vel);
		
	}
	
	/* 
	 * move is updated to check for if the predator is near and it leaves the food for duration timer and then returns to eating
	 */
	
	public void move(ArrayList<Seed> seeds, Rat rat) {
		super.move(seeds, rat);
		if (run) {
			long time = System.currentTimeMillis();
			if (time - runTimer > runDuration) {
				escapeMode(rat);
				run = false;
			}
		}
		
		else {
			if (seeds.size() == 0) {
				proceedTarget(null);
				return;
			}
			
			Seed desiredFood = selectDesiredFood(seeds);
		    if (desiredFood != null) {
		        proceedTarget(desiredFood);
		    }
		    
			if (desiredFood != null) {
	            if (!isTurning) {
	                proceedTarget(desiredFood);
	            } 
	            else {
	                long currentTime = System.currentTimeMillis();
	                if (currentTime - turnStartTime >= turnDuration) {
	                    isTurning = false;
	                }
	            }
	        }
		}
	}
	
	/* 
	 * This is kept in subclass because it is niether needed by other types of rats nor needed in the ratpanel
	 * hence as a private method there is no need to list it in super class
	 */
	private Seed selectDesiredFood(ArrayList<Seed> seeds) {
	    Seed desiredFood = null;
	    double maxAFC = Double.MIN_VALUE;

	    for (Seed seed : seeds) {
	        double distance = pos.dist(seed.getSeed());
	        if (distance <= 0.1) {
	            return seed;
	        }

	        double AFC = seed.getSize() / distance;
	        if (AFC > maxAFC) {
	            maxAFC = AFC;
	            desiredFood = seed;
	        }
	    }

	    return desiredFood;
	}
	
	public boolean chomp(Seed seed) {
		PVector approach = seed.getSeed();
		double size = (double) seed.getSize();
		PVector nosecolli = new PVector ((ratW / 4 * 3) * scale, (-ratH / 10) * scale);
		return PVector.dist(pos, approach) < (size / 2 + ratH / 2) || PVector.dist(nosecolli, approach) < size / 2;
	}
	
	public boolean chompCheck(ArrayList<Seed> seeds) {
		super.chompCheck(seeds);
		for (int i = 0; i < seeds.size(); i++)
			if (chomp(seeds.get(i))) {
				seeds.remove(i);
				return true;
			}
		return false;
	}
	
	/* 
	 * escape mode is when the prey detects a predator and starts running in the opposite direction
	 */
	public void escapeMode (Rat rat2) {
		if (detect(rat2)) {
		float angle = (float) Math.atan2(pos.y - rat2.pos.y, pos.x - rat2.pos.x);
		vel = PVector.fromAngle(angle);
		PVector gracefulTurn = turnForce().div((float)scale);
		
		float speed = vel.mag();
		
		vel.add(gracefulTurn);
				
		vel.normalize().mult(speed);

		pos.add(vel);
		}
	}

}
