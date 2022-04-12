package jfk.gameprogrammingsamples.maps.generation.cellularautomata;

public class GeneratorConfiguration {

	private int convertToWaterBelow, convertToLandAbove;
	private double initialLandpercentage;
	private String name = "Unnamed";
	
	public int getConvertToWaterBelow() {
		return convertToWaterBelow;
	}

	public void setConvertToWaterBelow(int convertToWaterBelow) {
		this.convertToWaterBelow = convertToWaterBelow;
	}

	public int getConvertToLandAbove() {
		return convertToLandAbove;
	}

	public void setConvertToLandAbove(int convertToLandAbove) {
		this.convertToLandAbove = convertToLandAbove;
	}

	public double getInitialLandpercentage() {
		return initialLandpercentage;
	}

	public void setInitialLandpercentage(double initialLandpercentage) {
		this.initialLandpercentage = initialLandpercentage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GeneratorConfiguration(String name, double initialLandpercentage, int convertToWaterBelow, int convertToLandAbove) {
		setName(name);
		setConvertToWaterBelow(convertToWaterBelow);
		setConvertToLandAbove(convertToLandAbove);
		setInitialLandpercentage(initialLandpercentage);
	}

	
	@Override
	public String toString() {
		return getName() + "(init: " + getInitialLandpercentage()* 100 + "% land, water < " + getConvertToWaterBelow() + ", land > " + getConvertToLandAbove();
	}
	
}