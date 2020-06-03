package clustering.properties;

import java.util.HashMap;

public class ClassProperties {

	HashMap<Integer,float[]> centroidMap=new HashMap<Integer,float[]>();
	public HashMap<Integer, float[]> getCentroidMap() {
		return centroidMap;
	}

	public void setCentroidMap(HashMap<Integer, float[]> centroidMap) {
		this.centroidMap = centroidMap;
	}

	public float[][] getTwoDiWeight() {
		return twoDiWeight;
	}

	public void setTwoDiWeight(float[][] twoDiWeight) {
		this.twoDiWeight = twoDiWeight;
	}

	float[][] twoDiWeight;
	
	public ClassProperties(HashMap<Integer,float[]> map,float[][] weight) {
		this.centroidMap=map;
		this.twoDiWeight=weight;
	}

}
