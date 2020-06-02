package nlp.pos.processes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

import db.handler.pos.phrases.DBProcess;
import POS.POST;

public class POSTagger {

	String input,article;
	
	
	//For input
	
	
	//For article
	public HashMap<Integer,String> sentListForArticle=new HashMap<Integer,String>();
	public HashMap<Integer,String> wordListPerSentenceForArticle=new HashMap<Integer,String>();
	public HashMap<Integer,String> taggedSentenceListForArticle=new HashMap<Integer,String>();
	
	public POSTagger(){
		
	}
	public HashMap<Integer,String> TaggingProcess(String input) throws ClassNotFoundException, SQLException{
		
		
		 HashMap<Integer,String> sentListForInput=new HashMap<Integer,String>();
		 HashMap<Integer,String> wordListPerSentenceForInput=new HashMap<Integer,String>();
		 HashMap<Integer,String> taggedSentenceListForInput=new HashMap<Integer,String>();
		
		sentListForInput=SplitParaIntoSent(input);
		wordListPerSentenceForInput=SplitWordfromSent(sentListForInput);
		taggedSentenceListForInput=FindPOS(sentListForInput,wordListPerSentenceForInput);
		
		/*sentListForInput=SplitParaIntoSent(input);
		sentListForArticle=SplitParaIntoSent(article);
		
		
		wordListPerSentenceForInput=SplitWordfromSent(sentListForInput);
		wordListPerSentenceForArticle=SplitWordfromSent(sentListForArticle);
		
		taggedSentenceListForInput=FindPOS(sentListForInput,wordListPerSentenceForInput);
		taggedSentenceListForArticle=FindPOS(sentListForArticle,wordListPerSentenceForArticle);
		
		
		System.out.println("############POS#################################");
		Print(taggedSentenceListForInput);
		Print(taggedSentenceListForArticle);
		
		System.out.println("\n\n..........................................");
		*/
		

		return taggedSentenceListForInput;
		
		
		
		
	}
	public void Print(HashMap<Integer,String> sentList){
		for(Integer key : sentList.keySet())
		{
			System.out.println(key+"..."+sentList.get(key));
		}
	}
	public HashMap<Integer,String> FindPOS(HashMap<Integer,String> sentList,HashMap<Integer,String> wordList) throws ClassNotFoundException, SQLException{
		HashMap<Integer,String> processedTaggedSent=new HashMap<Integer,String>();
					for(Integer key : sentList.keySet())
					{
						String sent=sentList.get(key);
						String wordSent=wordList.get(key);									
						String processedNounPhrase=FindPOSTag(sent,wordSent);						
						processedTaggedSent.put(key, processedNounPhrase);					
					}	
		return processedTaggedSent;
	}
	
	String FindPOSTag(String sent,String wordPerSent) throws ClassNotFoundException, SQLException{
		String processedVerbPhrase="";
		DBProcess db=new DBProcess();
		processedVerbPhrase=db.SearchToDB(sent,wordPerSent);
		return processedVerbPhrase;
	}
	
	public HashMap<Integer,String> SplitParaIntoSent(String inputpara){
		HashMap<Integer,String> sentList=new HashMap<Integer,String>();
		StringTokenizer sentences = new StringTokenizer(inputpara,"။");
		
		int count=0;
		while (sentences.hasMoreTokens()) {
			sentList.put(++count,sentences.nextToken()+"။");
		}
		
		return sentList;
	}
	
	public HashMap<Integer,String> SplitWordfromSent(HashMap<Integer,String> sentList){
		POST pos;
		HashMap<Integer,String> wordListPerSentence=new HashMap<Integer,String>();
		String str=null;
		
		for(Integer key : sentList.keySet())
		{
			str=sentList.get(key);
			pos=new POST(str);
			ArrayList<String> sentArr=pos.getAllSegmentedWord();
			String oneSentence=sentArr.toString();
			//replace Ã¡â‚¬ï¿½Ã¡â‚¬Â½Ã¡â‚¬â€žÃ¡â‚¬Â·Ã¡â‚¬Âº Ã¡â‚¬â€žÃ¡â‚¬Â·Ã¡â‚¬Âº Ã¡â‚¬â‚¬ Ã¡â‚¬â‚¬Ã¡â‚¬Â½Ã¡â‚¬Â²Ã¡â‚¬â€�Ã¡â‚¬Â±Ã¡â‚¬ï¿½Ã¡â‚¬Â¬Ã¡â‚¬â‚¬Ã¡â‚¬Â­Ã¡â‚¬Â¯ Ã¡â‚¬â€¢Ã¡â‚¬Â¼Ã¡â‚¬â€�Ã¡â‚¬ÂºÃ¡â‚¬â€¦Ã¡â‚¬Â­ Ã¡â‚¬â„¢Ã¡â‚¬Å¡Ã¡â‚¬Âº Ã¡â‚¬Â¡ Ã¡â‚¬â‚¬Ã¡â‚¬Â­Ã¡â‚¬Â¯Ã¡â‚¬Å“Ã¡â‚¬Â² Ã¡â‚¬â€¢Ã¡â‚¬Â¼Ã¡â‚¬â€�Ã¡â‚¬Âº Ã¡â‚¬â€¦Ã¡â‚¬Â­Ã¡â‚¬â„¢Ã¡â‚¬Å¡Ã¡â‚¬Âº
			oneSentence=oneSentence.replace(" + ", ",");
			oneSentence=oneSentence.substring(1,oneSentence.length()-1);
			//khwint atwat
			oneSentence=oneSentence.replace(",င့်", "င့်");
			oneSentence=oneSentence.replace("အ,", "အ");		
			wordListPerSentence.put(key,oneSentence);	
			
		}
		
		
		return wordListPerSentence;
	}
}
