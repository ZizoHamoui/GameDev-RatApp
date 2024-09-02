import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.ArrayList;

import processing.core.PVector;

public class Rat {
	protected Ellipse2D.Double body;
	protected Ellipse2D.Double head;
	protected Ellipse2D.Double eye1;
	protected Ellipse2D.Double eye2;
	protected Ellipse2D.Double noseTip;
	protected Ellipse2D.Double ear1;
	protected Ellipse2D.Double ear2;
	
	protected int ratW, ratH;
	protected Color color;
	protected float scale, fullSpeed;
	protected PVector pos, vel;
	protected float speedCeiling;
	private Dimension dimension;
	protected Area outline;
	protected Arc2D.Double fov;
	private float sight; 
	protected float runTimer;
	protected float runDuration = 4000;
	protected boolean run = false;
	
	protected boolean isTurning;
    protected long turnStartTime;
    protected long turnDuration;
	
	public Rat(PVector pos, int ratW, int ratH, float scale, Color color) {
		this.pos = pos;
		this.ratW = ratW;
		this.ratH = ratH;
		this.scale = scale;
		this.color = color;
		this.dimension = new Dimension(ratW, ratH);
		vel = new PVector(2, 2);
		body = new Ellipse2D.Double();
		head = new Ellipse2D.Double();
		eye1 = new Ellipse2D.Double();
		eye2 = new Ellipse2D.Double();
		noseTip = new Ellipse2D.Double();
		ear1 = new Ellipse2D.Double();
		ear2 = new Ellipse2D.Double();
		
		while (fullSpeed == 0)
			this.fullSpeed = Util.random(2, 4);
		
		speedCeiling = fullSpeed;
		vel.limit(speedCeiling);
		this.vel = Util.randomPVector(fullSpeed);
		
		isTurning = false;
		turnStartTime = 0;
		turnDuration = 4000;
		
		attributesAssignment();
		setOutline();
	}
	
	/* 
	 * The attributeAssigment was used in Rat which is super class because it will be needed in both Predator and prey
	 * This will allow the usage of this method without declaring it multiple times
	 */
	public void attributesAssignment() {
		body.setFrame(-ratW / 2, -ratH / 2, ratW, ratH);
		head.setFrame(ratW / 2 - 10, -ratH / 4, ratW / 4, ratW / 4);
		eye1.setFrame(ratW / 2, -ratH / 4, ratW / 10, ratH / 7);
		eye2.setFrame(ratW / 2, 0, ratW / 10, ratH / 7);
		noseTip.setFrame(ratW / 4 * 3, -ratH / 10, ratW / 10, ratH / 7);
		ear1.setFrame(ratW / 2 - 50, -ratH, ratW / 2, ratW / 2);
		ear2.setFrame(ratW / 2 - 50, ratH / 5, ratW / 2, ratW / 2);
		
		sight = dimension.width * speedCeiling * .15f;
		fov = new Arc2D.Double(-sight, -sight, sight * 2, sight * 2, -55, 110, Arc2D.PIE);
	}
	
	/* 
	 * The setOutline was used in Rat which is super class because it will be needed in both Predator and prey
	 * to get the general outline, this will allow the usage of this method without declaring it multiple times
	 */
	protected void setOutline() {
		outline = new Area();
		outline.add(new Area(body));
		outline.add(new Area(head));
		outline.add(new Area(eye1));
		outline.add(new Area(eye2));
		outline.add(new Area(noseTip));
		outline.add(new Area(ear1));
		outline.add(new Area(ear2));
	}
	
	protected Shape getOutline() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}
	
	/* 
	 * The drawRat was used in Rat which is super class because it will be needed in both Predator and prey
	 * The main difference in both comes in the sharp change in colour hence since all body parts are the same in shape and size
	 * drawRat was used in the super class
	 */
	public void drawRat(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform ms = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		
		float angle = vel.heading();
		g2.rotate(angle);
		
		//body
		attributesAssignment();
		g2.setColor(new Color(196, 164, 132));
		g2.fill(body);
		
		//head
		g2.setColor(new Color(196, 164, 132));
		g2.fill(head);
		
		//eyes
		g2.setColor(new Color(255,255,255));
		g2.fill(eye1);
		g2.fill(eye2);
		
		//nose
		g2.setColor(new Color(196, 164, 132));
		int[] xPoints = {ratW/3, ratW/4 * 3, ratW/3};
		int[] yPoints = {-ratH / 10, -ratH/20, ratH/5};
		g2.fillPolygon(xPoints, yPoints, 3);
		
		//noseTip
		g2.setColor(new Color(0,0,0));
		g2.fill(noseTip);
		
		//ears
		g2.setColor(color);
		g2.fill(ear1);
		g2.fill(ear2);
		
		g2.setTransform(ms);
		
	}
	
	/* 
	 * The turnforce method was used in Rat which is super class because it will be needed in both Predator and prey
	 * this method allows for the detection of edges and turning which both sub classes will need to use
	 */
	
	protected PVector turnForce() {
		PVector force = new PVector();
		float gardenVariable = 40.0f;
		
		double vectorLength = 0;
		
		vectorLength  = RatPanel.leftEdge.ptLineDist(pos.x, pos.y) - ratW * scale;
		force.add(new PVector((float) (gardenVariable / Math.pow(vectorLength , 2)), 0.0f));
		
		vectorLength  = RatPanel.rightEdge.ptLineDist(pos.x, pos.y) - ratW * scale;
		force.add(new PVector((float) (-gardenVariable/ Math.pow(vectorLength , 2)), 0.0f));
		
		vectorLength  = RatPanel.topEdge.ptLineDist(pos.x, pos.y) - ratH * scale;
		force.add(new PVector(0.0f, (float) (gardenVariable / Math.pow(vectorLength , 2)), 0.0f));
		
		vectorLength  = RatPanel.bottomEdge.ptLineDist(pos.x, pos.y) - ratH * scale;
		force.add(new PVector(0.0f, (float) (-gardenVariable/ Math.pow(vectorLength , 2)),  0.0f));
		
		return force;
	}
	
	/* 
	 * The detectcollision method was used in Rat because all types of its subclass will need to make use of it
	 */
	
	public boolean detectCollision(Rat Rat2) {
		boolean hit = false;

		if (getOutline().intersects(Rat2.getOutline().getBounds2D())
				&& Rat2.getOutline().intersects(getOutline().getBounds2D())) {
			hit = true;

		}

		return hit;
	}
	
	/* 
	 * The resolveCollision method was used in Rat because all types of its subclass will need to make use of it
	 */
	void resolveCollision(Rat Rat2){

		float angle = (float) Math.atan2(pos.y - Rat2.pos.y, pos.x - Rat2.pos.x);
		
		if(scale < Rat2.scale) {	
			vel = PVector.fromAngle(angle);
			PVector gracefulTurn = turnForce().div((float)scale);
			
			float speed = vel.mag();
			
			vel.add(gracefulTurn);
					
			vel.normalize().mult(speed);

			pos.add(vel);
		}
		else {
			Rat2.vel = PVector.fromAngle(angle-(float)Math.PI);
			Rat2.vel.mult(fullSpeed);
			Rat2.vel.add(turnForce().div((float)scale));
		}
	}
	
	public Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}
	
	public PVector getRat() {
		return pos;
	}
	
	/* 
	 * Move will be used in predator and prey however their actions will defer based on their behaviour
	 * Chompcheck will only be used in prey which is why it was not initiated in the superclass but just declared
	 */
	
	public void move(ArrayList<Seed> seeds, Rat rat) {}
	public boolean chompCheck(ArrayList<Seed> seeds) {return false;}
	
	protected boolean detect(Rat rat2) {
		Rectangle2D.Double bod = new Rectangle2D.Double(rat2.pos.x, rat2.pos.y, rat2.ratW, rat2.ratH); 
		if (getFOV().intersects(bod)){
			runTimer = System.currentTimeMillis();
			run = true;
			return true;
		}
		return false;
	}

}
