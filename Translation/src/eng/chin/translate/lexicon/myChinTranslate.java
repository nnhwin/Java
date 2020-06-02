package eng.chin.translate.lexicon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class myChinTranslate {

		
	public String DoTranslate1(String sent){
		
		String[] phrase=sent.split("#");
		String processedSent="";
		
		for(int i=0;i<phrase.length;i++){
			String phr=phrase[i].trim();
			
			if(!phr.isEmpty()){
				//Extract PhraseName
				System.out.println("Each phrase "+phr);
				String phrName=phr.substring(0,phr.indexOf("["));
				
				String subPhr=phr.replace(phrName, "");				
				//Remove num -1 ,-2 
				subPhr=RemoveNumFromPhrase(subPhr);			
				System.out.println("Phr Name = "+phrName);
				System.out.println("Sub Name = "+subPhr+"\n");
					
				//
				if(subPhr.contains(",")){
					//Check Change or Something
					String rulePattern=GetRulePattern(subPhr);
					HashMap<String,String> posDataPair=GetPair(subPhr);
					HashMap<String,String> rulepair=GetRulePairs(subPhr,phrName);
					//System.out.println("RUle pair "+rulepair.size());
					String newPhr="";
					if(rulepair.size()>0)
					{
						//Find Rule Pattern
						Iterator hmIterator = rulepair.entrySet().iterator();				       
				        while (hmIterator.hasNext()) { 
				            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
				            String engPattern=(String)mapElement.getKey();
				            
				            if(engPattern.equalsIgnoreCase(rulePattern)){
				            	//System.out.println(engPattern+"........eng&chin........... "+mapElement.getValue());
				            	//GetBack Order
				            	String chinOrder=(String)mapElement.getValue();
				            	String[] words=chinOrder.split(",");
				            	for(int k=0;k<words.length;k++){
				            		String ww=words[k];
				            		Iterator itr = posDataPair.entrySet().iterator();				       
							        while (itr.hasNext()) { 
							        	Map.Entry dataElement = (Map.Entry)itr.next(); 
							        	String token=(String)dataElement.getKey();
							        	String element=(String)dataElement.getValue();
							        	//System.out.println(token+"........match........... "+ww);
							        	if(token.equalsIgnoreCase(ww)){
							        		newPhr+=element+"/"+token+",";
							        		break;
							        	}
							        		
							        }
				            	}				            	
				            	break;
				            }//Rule match
				            
				        } 
					}
					if(newPhr.endsWith(","))
						newPhr="["+newPhr.substring(0,newPhr.length()-1)+"]";
					else
						newPhr="["+newPhr+"]";
					
					processedSent+="#"+phrName+newPhr+" ";
					
				}//if
				else{
					processedSent+="#"+phrName+subPhr+" ";
				}
									
			}//if empty check
			
		}//for i	
		System.out.println("Input "+sent);
		System.out.println("After rule match.... "+processedSent);
		return processedSent;
	}
	
	public HashMap<String,String> GetPair(String pattern){
		HashMap<String,String> pairs=new HashMap<String,String>();
		
		pattern=pattern.replace("[", "");
		pattern=pattern.replace("]", "");
		
		String[] words=pattern.split(",");
		
		for(int i=0;i<words.length;i++){
			String temp=words[i].trim();
			if(!temp.contains("%/")){
				String[] ss=temp.split("/");
				pairs.put(ss[1],ss[0]);
			}
		}
		
		return pairs;
	}
	
	public HashMap<String,String> GetRulePairs(String pattern,String type){
		HashMap<String,String> pairs=new HashMap<String,String>();
		if(type.equalsIgnoreCase("VB_A") || type.equalsIgnoreCase("VP_A")){
			type="VB_A";
		}
		else if(type.equalsIgnoreCase("subject") || type.equalsIgnoreCase("object") || type.equalsIgnoreCase("np"))
		{
			type="NP";
		}
		else if(type.equalsIgnoreCase("NP_Place"))
		{
			type="NP_Place";
		}
		
		GetLexiWord getData=new GetLexiWord();
		try {
			pairs=getData.ExtractAllPhrases(type);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pairs;
	}
	
	public String GetRulePattern(String inputPattern){
		String rulePattern="";
		inputPattern=inputPattern.replace("[", "");
		inputPattern=inputPattern.replace("]", "");
		
		String[] words=inputPattern.split(",");
		String temp="";
		String type="";
		for(int i=0;i<words.length;i++){
			temp=words[i].trim();
			if(temp.contains("/")){
				type=temp.substring(temp.indexOf("/")+1);
				rulePattern+=type+",";
			}
		}
		
		if(rulePattern.endsWith(","))
			rulePattern=rulePattern.substring(0,rulePattern.length()-1);
		
		
		return rulePattern;
	}
	
	public String RemoveNumFromPhrase(String str){
		String rr=str;
		str=str.replace("-0", "");
		str=str.replace("-1", "");
		str=str.replace("-2", "");
		str=str.replace("-3", "");
		str=str.replace("-4", "");
		str=str.replace("-5", "");
		str=str.replace("-6", "");
		str=str.replace("-7", "");
		str=str.replace("-8", "");
		str=str.replace("-9", "");
		str=str.replace("-10", "");
		str=str.replace("-11", "");
		str=str.replace("-12", "");
		str=str.replace("-13", "");
		str=str.replace("-14", "");
		str=str.replace("-15", "");
		str=str.replace("-16", "");
		str=str.replace("-17", "");
		str=str.replace("-18", "");
		str=str.replace("-19", "");
		str=str.replace(",,", ",");
		rr=str;
		return rr;
	}
	
	/*public static void main(String[] args){
		
		String sent="#Subject[a mah nu mei/PRP]  #VP_A[ne khin/VBD]  #NP_Place[ah/IN,,sa buai/NN]";
		//String sent="#Subject[thu/PRP-0] #VP_A[phitthe/VBZ-1,sarnay/VBG-2] #Object[takhu/DT-3,panthee/NN-4] #NP_Place[at/IN-5,%/DT-6,pankan/NN-7]";
		//String sent="#Subject[A mah pa sal/PRP]  #VP_A[kha ding/MD,ne/VB]  #Object[apple.Fruit/NN,khat/DT]";
		String[] eng={"he","is","eating","an","apple","on","the","plate"};
		String[] chin={"thu","phitthe","sarnay","takhu","panthee","on","#","pankan"};
		String[] pos={"PRP","VBZ","VBG","DT","NN","IN","DT","NN"};
		
		
		myChinTranslate tchin=new myChinTranslate();
		tchin.DoTranslate1(sent);
	}*/
}
