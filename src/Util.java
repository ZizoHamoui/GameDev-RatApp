import processing.core.PVector;

public class Util {
	public static float random(double min, double max) {
		return (float) (Math.random()*(max-min)+min);
	}
	
	public static PVector randomPVector(float magnitude) {
		return PVector.random2D().mult(magnitude);
	}

}
