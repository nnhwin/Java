package eng.chin.translate.lexicon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WholeSentenceTranslation {

	public WholeSentenceTranslation() {
		
	}
	
	public String DoTranslate1(String sent)
	{
		String pro_sent="";
		HashMap<String,String> pair=new HashMap<String,String>();
		String[] phr=sent.split("#");
		HashMap<String,String> phrAndDataPair=new HashMap<String,String>();
		for(int i=0;i<phr.length;i++){
			String word=phr[i].trim();
			if(!word.isEmpty()){
				String phrName=word.substring(0,word.indexOf("["));
				String data=word.replace(phrName, "").trim();
				phrAndDataPair.put(phrName, data);
				
			}
		}
		
		//S,M,T,P,O,V
		String[] arr={"Subject","Manner","Time","Place","Object","VB"};
		String arrangeSent="";
		for(int i=0;i<arr.length;i++){
			String tempStr=arr[i];
			for (Map.Entry<String, String> ee : phrAndDataPair.entrySet()) {
				String value=ee.getValue();
				String key=ee.getKey();
				if(key.contains(tempStr) || key.equals(tempStr)){
					arrangeSent+="#"+key+value+" ";
					break;
				}			
					
			}
		}
		
		pro_sent=arrangeSent;
		//ExtractAllPhrases
		//System.out.println("\n\nWhole Sent\nBeforeChange ....."+sent);
		//System.out.println("After Change........."+pro_sent);
		return pro_sent;
	}
	
	/*public String DoTranslate(String sent)
	{
		String pro_sent="";
		HashMap<String,String> pair=new HashMap<String,String>();
		String[] phr=sent.split("#");
		String[] phraseName=new String[phr.length];
		String[] phraePhrase=new String[phr.length];
			
		int count=0;
		String nameSeq="";
		for(int i=0;i<phr.length;i++){
			String word=phr[i].trim();
			if(!word.isEmpty()){
				String phrName=word.substring(0,word.indexOf("["));
				if(phrName.equals("VB_A")){
					phrName="VB";
					word=word.replace("VB_A", "");
				}
									
				else
					word=word.replace(phrName, "").trim();
				
				//Change Place Name
				if(phrName.equalsIgnoreCase("NP_Place"))
					phrName="Place";
				nameSeq+=phrName+",";
				String subPhr=word;
				pair.put(phrName, subPhr);
				phraseName[i]=phrName;
				phraePhrase[i]=subPhr;
			}
		}
		
		nameSeq=nameSeq.trim();
		if(nameSeq.endsWith(","))
			nameSeq=nameSeq.substring(0,nameSeq.length()-1);
		
		String after=SearchStructure(nameSeq);
		if(after.equals(nameSeq)){
			//no change
			//System.out.println("in no change.......");
			pro_sent=sent;
		}
		else{
			//Change
			//System.out.println("in change......."+after);
			String[] pp=after.split(",");
			for(int i=0;i<pp.length;i++){
				String nn=pp[i].trim();
				pro_sent+="#"+nn+pair.get(nn)+" ";
				
			}
		}
		
		//ExtractAllPhrases
		System.out.println("\n\nWhole Sent\nBeforeChange ....."+sent);
		System.out.println("After Change........."+pro_sent);
		return pro_sent;
	}*/
	
	String SearchStructure(String nameSeq){
		////ExtractAllPhrases
		GetLexiWord gw=new GetLexiWord();
		String temp="";
		boolean found=false;
		try {
			HashMap<String,String> pp=gw.ExtractAllPhrases("Sentence");
			for (Map.Entry<String,String> entry : pp.entrySet()) {
				String pos1=(String)entry.getKey();
				if(nameSeq.equalsIgnoreCase(pos1)){
					
					temp=entry.getValue();
					found=true;
					break;				
				}
			}
			if(found==false)
				temp=nameSeq;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	/*public static void main(String[] str){
		WholeSentenceTranslation ws=new WholeSentenceTranslation();
		String sent="#Subject[thu/PRP]  #VP_A[phitthe/VBZ,sarnay/VBG]  #Object[panthee/NN,takhu/DT] #NP_Place[%/IN,pankan/NN,%/DT] ";
		//String sent="#Subject[thuma/PRP] #Object[panthee.Fruit/NN,takhu/DT] #Place[%/IN,sarpwae/NN,%/DT] #VP[phitthe/VBZ,sarnay/VBG] ";
		ws.DoTranslate(sent);
	}*/
}
