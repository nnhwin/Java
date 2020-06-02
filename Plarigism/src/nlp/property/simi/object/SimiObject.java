package nlp.property.simi.object;

import java.util.HashMap;

public class SimiObject {
	HashMap<String,Float> structureSimilarity;
	public HashMap<String, Float> getStructureSimilarity() {
		return structureSimilarity;
	}
	public void setStructureSimilarity(HashMap<String, Float> structureSimilarity) {
		this.structureSimilarity = structureSimilarity;
	}
	public HashMap<String, Float> getNgramSimilarity() {
		return ngramSimilarity;
	}
	public void setNgramSimilarity(HashMap<String, Float> ngramSimilarity) {
		this.ngramSimilarity = ngramSimilarity;
	}
	public HashMap<String, Float> getTotalFinalScore() {
		return totalFinalScore;
	}
	public void setTotalFinalScore(HashMap<String, Float> totalFinalScore) {
		this.totalFinalScore = totalFinalScore;
	}
	public HashMap<String, Float> getPlarigismValue() {
		return plarigismValue;
	}
	public void setPlarigismValue(HashMap<String, Float> plarigismValue) {
		this.plarigismValue = plarigismValue;
	}
	HashMap<String,Float> ngramSimilarity;
	HashMap<String,Float> totalFinalScore;
	HashMap<String,Float> plarigismValue;
	public SimiObject(HashMap<String,Float> structureSimilarity,HashMap<String,Float> ngramSimilarity,HashMap<String,Float> totalFinalScore,HashMap<String,Float> plarigismValue){
		this.structureSimilarity=structureSimilarity;
		this.ngramSimilarity=ngramSimilarity;
		this.totalFinalScore=totalFinalScore;
		this.plarigismValue=plarigismValue;
	}
}
