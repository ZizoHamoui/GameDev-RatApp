/*Author: Abdul Aziz Hamoui
 Date of creation: October 28th 2023 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Color;

import javax.swing.JPanel;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.Timer;

import processing.core.PVector;

public class RatPanel extends JPanel implements ActionListener {
	private Garden garden;
	private Timer timer;
	private ArrayList<Seed> seeds = new ArrayList<>();
	static public ArrayList<Rat> rats = new ArrayList<>();
	
	public final static int pWidth = 1300;
	public final static int pHeight = 950;
	public final static int borders = 50;
	public final static int PREY_COUNT = 5;
	public final static int PREDATOR_COUNT = 2;
	public final static int SEED_COUNT = PREY_COUNT * 3;
	
	public static Line2D.Double rightEdge;
	public static Line2D.Double leftEdge;
	public static Line2D.Double topEdge;
	public static Line2D.Double bottomEdge;
	private boolean generateSeed = false;
	
	public RatPanel() {
		super();
		this.setPreferredSize(new Dimension(pWidth, pHeight));
		timer = new Timer(33, this);
		timer.start();
		garden = new Garden();
		
		leftEdge = new Line2D.Double(borders, borders, borders, pHeight - borders);
		rightEdge = new Line2D.Double(pWidth - borders, borders, pWidth - borders, pHeight - borders);
		topEdge = new Line2D.Double(borders, borders, pWidth - borders, borders);
		bottomEdge = new Line2D.Double(borders, pHeight - borders, pWidth - borders, pHeight - borders);
		
		for (int i = 0; i < PREY_COUNT; ++i) {
			float size = Util.random(0.5f, 1f);
			Color color = new Color (196, (int)Util.random(1, 200), (int)Util.random(1, 200));
			float locX = Util.random(100f, 1100f);
			float locY = Util.random(100f, 700f);

			rats.add(new Prey(new PVector (locX, locY), 90, 60, size, color));
		}
		
		for (int i = 0; i < PREDATOR_COUNT; ++i) {
			float size = Util.random(0.5f, 1f);
			Color color = new Color (196, (int)Util.random(1, 200), (int)Util.random(1, 200));
			float locX = Util.random(100f, 1100f);
			float locY = Util.random(100f, 700f);

			rats.add(new Predator(new PVector (locX, locY), 90, 60, size, color));
		}
		
		for (int i = 0; i < SEED_COUNT; ++i) {
			addSeed();
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		garden.drawGarden(g2);
		
		
		for (int i = 0; i < seeds.size(); ++i) {
               seeds.get(i).drawSeed(g2);
        }
		
		for (int i = 0; i < rats.size(); ++i)
			rats.get(i).drawRat(g2);
		
		if (seeds.size() < SEED_COUNT) {
			addSeed();
		}
		
		repaint();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		for (int i = 0; i < rats.size(); i++) {
			Rat rat1 = rats.get(i);
			for (int j = 0; (i != j) && j < rats.size(); j++) {
				Rat rat2 = rats.get(j);
				if(rat1.detectCollision(rat2)){
					//rat1.resolveCollision(rat2);

						if (rat1 instanceof Predator && rat2 instanceof Prey) {
							Predator rat3 = (Predator) rat1;
							if (rat3.eat(rat2))
								killRat(rat2);
							else
								rat1.resolveCollision(rat2);
						} else if (rat1 instanceof Prey && rat2 instanceof Predator) {
							Predator rat3 = (Predator) rat2;
							if (rat3.eat(rat1))
								killRat(rat1);
							else
								rat1.resolveCollision(rat2);
						} else {
							rat1.resolveCollision(rat2);
						}
					}
			}
			rat1.move(seeds, rat1);
			if (rat1.chompCheck(seeds) && generateSeed) {
				addSeed();
			}
		}
		
		repaint();
	}
	
	public void addSeed() {
			float size = Util.random(15f, 25f);
			int locX =(int) Util.random(100f, 1100f);
			int locY = (int)Util.random(100f, 650f);

			seeds.add(new Seed(new Point (locX, locY), size));
	}
	
	void killRat(Rat b) {
		rats.remove(b);
	}
	
}