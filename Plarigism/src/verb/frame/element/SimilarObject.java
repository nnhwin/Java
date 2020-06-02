package verb.frame.element;

public class SimilarObject {
	public String phraseType;
	public String coreType;
	
	public String getCoreType() {
		return coreType;
	}
	public void setCoreType(String coreType) {
		this.coreType = coreType;
	}
	public String getPhraseType() {
		return phraseType;
	}
	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
	}
	public SimilarObject(){
		
	}
	public SimilarObject(String similarType,String phraseType,String coreType, boolean found, String inputStr,String articleStr, String foundInputPhrase,
			String foundArtcilePhrase) {
		super();
		this.similarType = similarType;
		this.found = found;
		this.inputStr = inputStr;
		this.articleStr = articleStr;
		this.foundInputPhrase = foundInputPhrase;
		this.foundArtcilePhrase = foundArtcilePhrase;
		this.phraseType=phraseType;
		this.coreType=coreType;
	}
	public String similarType;
	
	public String getSimilarType() {
		return similarType;
	}
	public void setSimilarType(String similarType) {
		this.similarType = similarType;
	}
	public boolean found;
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	public String getInputStr() {
		return inputStr;
	}
	public void setInputStr(String inputStr) {
		this.inputStr = inputStr;
	}
	public String getArticleStr() {
		return articleStr;
	}
	public void setArticleStr(String articleStr) {
		this.articleStr = articleStr;
	}
	public String getFoundInputPhrase() {
		return foundInputPhrase;
	}
	public void setFoundInputPhrase(String foundInputPhrase) {
		this.foundInputPhrase = foundInputPhrase;
	}
	public String getFoundArtcilePhrase() {
		return foundArtcilePhrase;
	}
	public void setFoundArtcilePhrase(String foundArtcilePhrase) {
		this.foundArtcilePhrase = foundArtcilePhrase;
	}
	public String inputStr;
	public String articleStr;
	public String foundInputPhrase;
	public String foundArtcilePhrase;
}
