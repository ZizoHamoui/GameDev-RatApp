
 /*Author: Abdul Aziz Hamoui
 Date of creation: October 28th 2023 */

public class RatApp extends javax.swing.JFrame {
	public RatApp(String title) {
		super(title);
		this.setSize(1300, 950);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.add(new RatPanel());
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new RatApp("Rat Eats Seed");
	}
}
