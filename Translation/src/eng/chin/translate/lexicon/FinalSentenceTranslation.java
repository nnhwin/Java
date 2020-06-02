package eng.chin.translate.lexicon;

import java.util.HashMap;

public class FinalSentenceTranslation {

	boolean singularSub=true;
	String verbphrase="";
	int verbtype=0;
	int weSignal=0;
	int iSignal=0;
	public FinalSentenceTranslation() {
		
	}

	public String DoPerform(String input){
		String pro_sent="";
		HashMap<String,String> pair=new HashMap<String,String>();
		String[] phr=input.split("#");
			
		int count=0;
		String nameSeq="";
		
		for(int i=0;i<phr.length;i++){
			String word=phr[i].trim();
			if(!word.isEmpty()){
				if(word.contains("Subject")){
					if(word.contains("NNS") || word.toLowerCase().contains("a mau te") || word.toLowerCase().contains("ei te")){
						singularSub=false;
						if(word.contains("ei te")){
							weSignal=1;
						}
					}
					else if(word.contains("NN") && word.toLowerCase().contains("kei mah"))
					{
						iSignal=1;
					}
				}			
				if(word.contains("VB")){
					verbphrase=ProcessVerbIssue(word);
					//System.out.println(word+".......######Verb###### "+verbphrase+"......pro_sent......"+pro_sent);
					pro_sent+=verbphrase+" ";
				}
				else{
				
					String phrName=word.substring(0,word.indexOf("["));
					word=word.replace(phrName, "").trim();
					word=word.replace("[", "");
					word=word.replace("]", "");
					if(word.contains(","))
					{
						String[] temp=word.split(",");
						for(int k=0;k<temp.length;k++){
							String one=temp[k].trim();
							if(one.contains("/")){
								String chin=one.substring(0,one.indexOf("/"));
								//တခု တကောင် တ​ေယောက် ကို ပြောင်းရမှာ
								//System.out.println(">>>>>>>>>>>Chin "+chin);
								if(chin.toLowerCase().equals("khat")){
									if(k!=0){
										String preword=temp[k-1].trim();
										if(preword.contains("Fruit")){
											chin="pum khat";
										}
										else if(preword.contains("Ppl")){
											chin="mi khat";
										}
										else 
											chin="khat";
									}
								}							
								nameSeq+=chin+" ";
							}
						}
						nameSeq=nameSeq.trim()+", ";		
					}
					else{
						if(word.contains("/")){
							String chin=word.substring(0,word.indexOf("/"));
							nameSeq+=chin+" ";
						}
					}
					if(nameSeq.endsWith(","))
						nameSeq=nameSeq.substring(0,nameSeq.length()-1);				
					
					nameSeq=RemoveUnncessaryWord(nameSeq);					
					//Check noun
					if(phrName.equals("Subject")){
						nameSeq=trimThem(nameSeq);
						nameSeq+=" in ";
					}
					else if(phrName.equals("Object")){
						nameSeq=trimThem(nameSeq);
						nameSeq+=" ";
					}					
					pro_sent=" "+nameSeq+" "+verbphrase;
				}			
			}//if
			
		}//for
		
		pro_sent=trimThem(pro_sent);
		pro_sent=pro_sent.replace("[", "");
		
		//Ending word
		pro_sent=pro_sent.replace(".", "").trim();
		
		if(pro_sent.length()>3)
		{
			String tempstr=String.valueOf(pro_sent.charAt(0)).toUpperCase();
			String remainingStr=tempstr+pro_sent.substring(1).toLowerCase();	
			pro_sent=remainingStr;
		}
		
		pro_sent+=" hi.";
		
		pro_sent=pro_sent.replace(",", " ");
		pro_sent=pro_sent.replace("  ", " ");
		pro_sent=pro_sent.replace("hi hi","hi");
		pro_sent=pro_sent.replace("-","");
		
		pro_sent=pro_sent.trim();
		
		
		System.out.println("Final Sentence >>>>>>>>> "+pro_sent);
		pro_sent=posProcess(pro_sent);
		
		return pro_sent;
	}
	
	String posProcess(String str){
		str=str.replace("[", "").trim();
		str=str.replace("thethe","the");
		return str;
	}
	
	String ProcessVerbIssue(String str)
	{
		String verb="";
		String[] temp=str.split(",");
		if(temp.length>1){
			String verbphr="";
			for(int k=0;k<temp.length;k++){
				String one=temp[k].trim();
				if(temp.length>=2){
					if(k==0){
						if(one.contains("[")){
							one=one.substring(one.indexOf("[")+1,one.indexOf("/"));
						}
						if(one.equals("phitthe"))
							continue;
						else{
								verbphr+=one+" ";
							}					
					}						
					else
					{
						String tmp=one.substring(0,one.indexOf("/"));
						verbphr+=tmp+" ";
					}
				}
				
			}
			verb=verbphr;
		}
		else
		{
			if(str.contains("["))
				str=str.substring(str.indexOf("["));
			verb=str.substring(0,str.indexOf("/"));
			if(verb.contains("thethe"))
				verb=verb.replace("thethe", "hi.");
		}
		
		//add
		if(iSignal==1)
			verb="ka "+verb+" ";
		else if(weSignal==1)
			verb="i "+verb+" uh ";
		else if(singularSub==false)
			verb="a "+verb+" uh ";
		else if(singularSub==true)
			verb="a "+verb+" ";
		
		System.out.println("Verb return........."+verb);
		return verb;
		
	}
	String RemoveUnncessaryWord(String str){
		String pp="";
		str=str.replace(".Fruit", "").trim();
		str=str.replace(".Animal", "").trim();
		str=str.replace(".Ppl", "").trim();
		str=str.replace(".Car", "").trim();
		str=str.replace(".Tree", "").trim();
		str=str.replace(".Fruit", "").trim();
		str=str.replace("%", "").trim();
		pp=str;
		return pp;
	}
	
	String trimThem(String str){
		String tt=str;
		tt=tt.trim();
		if(tt.endsWith(","))
			tt=tt.substring(0,tt.length()-1);
		
		return tt;
	}
	
	/*public static void main(String[] args) {
		FinalSentenceTranslation ft=new FinalSentenceTranslation();
		//String st="#Subject[thuma/PRP] #Object[takhu/DT,panthee.Fruit/NN,%/NN,%/DT,sarpwae/NN] #VP[phitthe/VBZ,sarnay/VBG]";
		String st="#Subject[A mah pa sal/PRP]  #VP_A[ne/VBZ]  #Manner[nak pi tak in/RB]";
		//String st="#Subject[,nau pang te/NNS]  #VP_A[ne/VBP]  #NP[apple.Fruit/NN,khat/DT]";
		ft.DoPerform(st);
	}*/

}
