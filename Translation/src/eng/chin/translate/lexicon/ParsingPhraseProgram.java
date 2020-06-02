package eng.chin.translate.lexicon;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsingPhraseProgram {

	boolean stop=false;
	String processedPhr="";
	
	public String getProcessedPhr() {
		return processedPhr;
	}

	public void setProcessedPhr(String processedPhr) {
		this.processedPhr = processedPhr;
	}

	public ParsingPhraseProgram(String[] eng,String[] chin,String[] pos){
		
		int wordCount=eng.length;
		String seq="";
		String[] posWithNum=new String[wordCount];
		for(int i=0;i<wordCount;i++){
			posWithNum[i]=pos[i]+"-"+i;
		}
		
		//Make Sentennce
		for(int i=0;i<wordCount;i++)
			seq=seq+posWithNum[i]+",";
		seq=seq.substring(0,seq.length()-1);
		
		processedPhr=FindVerbPhrases(seq,eng,chin,pos);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		System.out.println("\nAfter Verb...@@@@.........................\n"+processedPhr);
		

		//Manner 
		processedPhr=FindManner(processedPhr);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		//System.out.println("\nAfter Preposition............................\n"+processedPhr);
		
		//Change prepoostion
		processedPhr=ChangePrepositionName(processedPhr);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		//System.out.println("\nAfter Preposition............................\n"+processedPhr);
		//Noun
		
		//Place
		processedPhr=ParsePlacePhrase1(processedPhr);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		System.out.println("After Place stage........."+processedPhr);
		
		/*//Time
		processedPhr=ParseTimePhrase1(processedPhr);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		System.out.println("After Time stage........."+processedPhr);*/
		
		processedPhr=NounPhraseParse1(processedPhr);
		if(processedPhr.endsWith(","))
			processedPhr=processedPhr.substring(0,processedPhr.length()-1);
		System.out.println("\nAfter Noun............................\n"+processedPhr);
		boolean remainPhrase=CheckStopOrNot(processedPhr);
		//Check another phrase
		if(remainPhrase==false){		
			remainPhrase=CheckStopOrNot(processedPhr);
			processedPhr=ParsePlacePhrase1(processedPhr);			
			System.out.println("All possible phrases are found at place phrase");			
		}
		else
			System.out.println("All possible phrases are found at long noun phrase");	
	}
	
	String AddHash(String sent){
		String temp="";
		sent=sent.trim();
		String[] phrases=sent.split("#");
		String pp="";
		for(int i=0;i<phrases.length;i++)
		{
			String tt=phrases[i].trim();
			System.out.println(tt);
			String[] nextSplit=tt.split(" ");
			for(int kk=0;kk<nextSplit.length;kk++){
				String ss=nextSplit[kk].trim();
				pp+="#"+ss+" ";
			}
			
		}		
		temp=pp;
		return temp;
	}
		
	String NounPhraseParse1(String str){
		str=AddHash(str);
		
		String np="";
		str=str.trim();
		String[] phr=str.split("#");
		for(int i=0;i<phr.length;i++){
			String temp=phr[i].trim();
			if(temp.endsWith(","))
				temp=temp.substring(0,temp.length()-1).trim();
			if(temp.startsWith(","))
				temp=temp.substring(1);
				
			if(temp!=null && !temp.equals("")){
				if(!temp.contains("[")){
					
					if(!temp.contains(",") && temp.contains("RB") && !temp.contains("o'clock"))
						np+=" #Manner["+temp+"] ";
					else
						np+=" #NP["+temp+"] ";
				}			
				else
					np+=" #"+temp+" ";
			}			
		}	
		return np;
	}
	
	String NounPhraseParse(String str){
		String np="";
		String[] phr=str.split("#");
		for(int i=0;i<phr.length;i++){
			String temp=phr[i].trim();
			if(temp.endsWith(","))
				temp=temp.substring(0,temp.length()-1).trim();
			if(temp.startsWith(","))
				temp=temp.substring(1);
			
			if(!temp.contains("[") && (temp!=null && !temp.equals(""))){
				String temp_t=CheckNoun(temp);
				np+="#NP["+temp_t+"] ";
			}
			else if(temp.contains("[")&& (temp!=null && !temp.equals("")))
				np+="#NP["+temp+"] ";
			
		}	
		return np;
	}
	
	String CheckNoun(String tt){
		String noun="";
		ArrayList<String> nounphrases=GetSmallNP();
		String[] words=tt.split(",");
		String numPairs="";
		String posPairs="";
		String[] posArr=new String[words.length];
		String[] numArr=new String[words.length];
		for(int j=0;j<words.length;j++){
			if(words[j].contains("-")){
				String[] pp=words[j].split("-");
				String pos=pp[0];
				String num=pp[1];
				posArr[j]=pos;
				numArr[j]=num;
				numPairs+=num+",";
				posPairs+=pos+",";
			}
			
			
		}
		numPairs=numPairs.substring(0,numPairs.length()-1);
		posPairs=posPairs.substring(0,posPairs.length()-1);
		for(int i=0;i<nounphrases.size();i++){
			
			String npPhrases=nounphrases.get(i);
			int posNum=Integer.parseInt(numArr[0])-1;	
			//System.out.println(npPhrases+"........##...."+posPairs+"...........st="+tt);
			if(posPairs.equals(npPhrases))
			{
				String[] kk=posPairs.split(",");				
				for(int ii=0;ii<kk.length;ii++){
					noun+=kk[ii]+"-"+(++posNum)+",";
				}
				
			}						
		}
		if(noun!=null && !noun.equals(""))
			noun=noun.substring(0,noun.length()-1)+"";
		//System.out.println(numPairs+"........##...."+posPairs+"...........st="+noun);
		return noun;
	}
	
	boolean CheckStopOrNot(String str){
		boolean stop=false;
		String[] pp=str.split("#");
		String sent="";
		for(int i=0;i<pp.length;i++){
			String temp=pp[i].trim();
			if(temp!=null && !temp.equals("")){
				sent+="#"+temp+" ";
			}
			
		}
		sent=sent.trim();
		String[] qq=sent.split("#");
		int cc=qq.length-1;
		int count=0;
		for(int i=0;i<qq.length;i++){
			String temp=qq[i].trim();
			if(temp.contains("["))
				++count;
		}
		if(count==cc)
			stop=true;
		
		return stop;
	}
	
	String ParsePlacePhrase1(String sent){
		
		String proecessedData="";
		ArrayList<String> places=GetPlaceNP();
		String sentence="";
		String[] phrase=sent.split("#");
		for(int i=0;i<phrase.length;i++){
			String phr=phrase[i].trim();
			//phr=phr.replace(",","");
			if(phr.startsWith(","))
				phr=phr.substring(phr.indexOf(",")+1);
			if(phr.endsWith(","))
				phr=phr.substring(0,phr.length()-1);
			if(phr!=null && !phr.equals(""))
			{
				sentence+="#"+phr+" ";
			}			
		}
			
		if(sentence.endsWith("#"))
			sentence=sentence.substring(0,sentence.length()-1);

		String[] phrase_num=sentence.split("#");
		String tt="";
		for(int i=0;i<phrase_num.length;i++){
			String str=phrase_num[i].trim();
			if(str.contains("PREP")){			
				String temp1=str.substring(0,str.indexOf("PREP"));
				if(temp1.trim().isEmpty()){
					if(str.contains("CD"))
						tt+=" #NP_Time["+str+"] ";
					else
						tt+=" #NP_Place["+str+"] ";
				}
				else{
					String kk="";
					if(str.contains("CD"))
						kk=str.replace(temp1, " #"+temp1+" #NP_Time[");
					else
						kk=str.replace(temp1, " #"+temp1+" #NP_Place[");
					tt+=kk+"] ";
				}
			}
			else{
				
				if(!str.trim().isEmpty()){
					tt+=" #"+str+" ";				
				}
			}				
		}		
		proecessedData=tt;
		return proecessedData;
	}
	
	String ParseTimePhrase1(String sent){
		
		String proecessedData="";
		ArrayList<String> places=GetTimeNP();
		String sentence="";
		String[] phrase=sent.split("#");
		for(int i=0;i<phrase.length;i++){
			String phr=phrase[i].trim();
			//phr=phr.replace(",","");
			if(phr.startsWith(","))
				phr=phr.substring(phr.indexOf(",")+1);
			if(phr.endsWith(","))
				phr=phr.substring(0,phr.length()-1);
			//System.out.println(">>>>>>>>>>"+phr);
			if(phr!=null && !phr.equals(""))
			{
				sentence+="#"+phr+" ";
			}
			
		}			
		if(sentence.endsWith("#"))
			sentence=sentence.substring(0,sentence.length()-1);

		return proecessedData;
	}
		
	boolean CheckNounPhrase1(String phrase){
		boolean place=false;
		phrase=phrase.trim();
		if(phrase.endsWith(","))
			phrase=phrase.substring(0,phrase.length()-1);
		if(phrase.startsWith(","))
			phrase=phrase.substring(1);
		if(phrase.contains(","))
		{
			String[] phr=phrase.split(",");
			int length=phr.length;
			ArrayList<String> places=GetPlaceNP();
			for(int n=0;n<places.size();n++)
			{
				String[] strr=places.get(n).split(",");
				
				if(strr.length==phr.length)
				{
					String temp1=places.get(n);
					String temp2=phr[n];
					if(temp1.equalsIgnoreCase(temp2)){
						place=true;
					}
				}
			}
		}
		return place;
	}
	
	String ReplacePOSWithNum(String mainSent,String placephr,String[] posArr,String[] numArr)
	{
		String str=mainSent;
		str=str.replace(placephr, "#NP_Place["+placephr+"]");
		if(str.startsWith(","))
			str=str.substring(1);
		if(str.endsWith(","))
			str=str.substring(0,str.length()-1);
		System.out.println("MIN Sent "+str);
		//System.out.println("@@@@@@@@@@@@@@@"+str);
		String[] pp=str.split("#");
		int count=posArr.length;
		
		String temp_pp=numArr[0].trim();
		int temp_pp_var=0;
		if(temp_pp.equals("") || temp_pp==null)
			temp_pp_var=1;
		int foundCount=temp_pp_var-1;
		String temp_str="";
		for(int i=0;i<pp.length;i++){
			String temp=pp[i].trim();
			temp=TrimThem(temp);
			if(temp.contains("[")){
				String temp_combine_var=" #";
				String[] qq=temp.split(",");
				for(int k=0;k<qq.length;k++){
					if(!qq[k].trim().endsWith("]"))
						temp_combine_var+=qq[k]+"-"+(++foundCount)+",";
					else
					{
						String tt=qq[k].replace("]", "");
						temp_combine_var+=tt+"-"+(++foundCount)+"],";
					}
				}
				
				temp_str+=temp_combine_var;		
			}
			else{
				String t_var="";
				String[] tt=temp.split(",");
				for(int jj=0;jj<tt.length;jj++){
					++foundCount;
					t_var+=tt[jj]+"-"+foundCount+",";
				}
				t_var=t_var.substring(0,t_var.length()-1);
				temp_str+=t_var;
			}
		}
		return temp_str;
	}
	
	String TrimThem(String temp){
		if(temp.startsWith(","))
			temp=temp.substring(1);
		if(temp.endsWith(","))
			temp=temp.substring(0,temp.length()-1);
		
		return temp;
	}
	String ChangeNounPhrase(String sent){
		String precessedData="";
		String[] phrase=sent.split("#");
		
		
		for(int i=0;i<phrase.length;i++){
			String phr=phrase[i].trim();
			if(phr!=null && !phr.equals("")){
				if(phr.endsWith(","))
					phr=phr.substring(0,phr.length()-1);
				if(phr.startsWith(","))
					phr=phr.substring(1);
				if(phr.contains("VB"))
					precessedData+="#"+phr+"#";
				else{
					String[] words=phr.split(",");
					String numPairs="";
					String posPairs="";
					for(int j=0;j<words.length;j++){
					//	System.out.println(">>>>>>>>"+words[j]);
						String uu=words[j].trim();
						if(uu!=null && !uu.equals("")){
							String[] pp=words[j].split("-");
							String pos=pp[0];
							String num=pp[1];							
							numPairs+=num+",";
							posPairs+=pos+",";
						}						
					}
					numPairs=numPairs.substring(0,numPairs.length()-1);
					posPairs=posPairs.substring(0,posPairs.length()-1);
					
					//Check
					boolean found=FindNP(posPairs,numPairs);
					if(found==true){
						phr="NP["+phr+"] ";
					}
					precessedData+=phr;
				}
			}			
		}			
		return precessedData;
	}
		
	boolean FindNP(String pos,String numpair){
		ArrayList<String> nounphrases=GetSmallNP();
		boolean found=false;
		for(int i=0;i<nounphrases.size();i++){
			String np=nounphrases.get(i);
			if(pos.equals(np)){
				found=true;
				break;
			}
		}
		return found;		
	}	
	
	String ChangePrepositionName(String sent){
		String changepre="";
		String[] phrase=sent.split(",");
		
		for(int i=0;i<phrase.length;i++){
			if(phrase[i].contains("TO")){
				phrase[i]=phrase[i].replace("TO", "PREP");				
			}
			if(phrase[i].contains("UNDER")){
				phrase[i]=phrase[i].replace("UNDER", "PREP");
				
			}
			if(phrase[i].contains("IN")){
				phrase[i]=phrase[i].replace("IN", "PREP");				
			}
			changepre+=phrase[i]+",";
		}
		
		return changepre;
	}
	
	String FindManner(String sent){
		
		String proecessedData="";
		String sentence="";
		String[] phrase=sent.split("#");
		if(sent.contains("RB")){
			for(int i=0;i<phrase.length;i++){
				String phr=phrase[i].trim();
				//phr=phr.replace(",","");
				if(phr.startsWith(","))
					phr=phr.substring(phr.indexOf(",")+1);
				if(phr.endsWith(","))
					phr=phr.substring(0,phr.length()-1);
				if(phr!=null && !phr.equals(""))
				{
					sentence+="#"+phr+" ";
				}
				
			}
				
			if(sentence.endsWith("#"))
				sentence=sentence.substring(0,sentence.length()-1);
			
			String[] phrase_num=sentence.split("#");
			String tt="";
			for(int i=0;i<phrase_num.length;i++){
				String str=phrase_num[i].trim();
				if(str.contains("RB") && !str.contains("o'clock")){
					String[] eachIndiv=str.split(",");
					String temp_str="";
					for(int kkk=0;kkk<eachIndiv.length;kkk++){
						String manner_str=eachIndiv[kkk].trim();
						if(manner_str.contains("RB") && manner_str.contains("o'clock")){
							temp_str=temp_str.trim();
							if(temp_str.endsWith(","))
								temp_str=temp_str.substring(0,temp_str.length()-1);
							
							temp_str+=" #Manner["+manner_str+"] ";
						}
						else
							temp_str+=manner_str+",";
					}
					tt+=temp_str;					
				}
				else{
						if(!str.trim().isEmpty())
							tt+="#"+str+" ";								
				}				
			}		
			proecessedData=tt;
		}
		else
			proecessedData=sent;
		return proecessedData;
	}
	
	public String FindVerbPhrases(String SeqWithNum,String[] eng,String[] chin,String[] pos){
		String[] phrases=SeqWithNum.split(",");
		
		String temp="";
		String verbphr="";
		int verbcount=0;
		boolean foundPrePhrase=false;
		for(int i=0;i<phrases.length;i++){
			String phr=phrases[i].trim();
			if(phr.contains("MD")){
				verbphr+=phr+",";
				++verbcount;
			}
			else if(phr.contains("VB")){
				verbphr+=phr+",";
				++verbcount;
			}
			else{
				if(verbcount>0 && foundPrePhrase==false){
					temp+="#VB_A["+verbphr.substring(0,verbphr.length()-1)+"]#,"+phr+",";
					foundPrePhrase=true;
				}
				else{
					temp+=phr+",";
				}
			}
			
		}//end of for
		if(!temp.contains("VB_A") && verbphr.length()>2){
			temp+="#VB_A["+verbphr.substring(0,verbphr.length()-1)+"]#,";
			verbphr="";
		}
		
		return temp;
	}
	
	ArrayList<String> GetTimeNP(){
		GetLexiWord rules_lexi=new GetLexiWord();
		ArrayList<String> nphrases=new ArrayList<String>();
		try {
			nphrases=rules_lexi.ExtractTimePhrases();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nphrases;
	}
	
	ArrayList<String> GetPlaceNP(){
		GetLexiWord rules_lexi=new GetLexiWord();
		ArrayList<String> nphrases=new ArrayList<String>();
		try {
			nphrases=rules_lexi.ExtractPlacePhrases();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nphrases;
	}
	
	ArrayList<String> GetSmallNP(){
		GetLexiWord rules_lexi=new GetLexiWord();
		ArrayList<String> nphrases=new ArrayList<String>();
		try {
			nphrases=rules_lexi.ExtractPlacePhrases();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nphrases;
	}
	
	String CheckActiveOrPasive(String verbphr){
		String active="active";
		String[] vphrases=verbphr.split(",");
		String str="";
		
		if(vphrases.length==1 && vphrases[0].contains("VBG")){
			active="Adj";
		}
		else{
			for(int i=0;i<vphrases.length;i++){
				String[] pairs=vphrases[i].split("-");
				str+=pairs[0]+",";
			}
			str=str.substring(0,str.length()-1);
			System.out.println(str);
			
		
			if(str.contains("VBN"))
				if(str.contains("VBZ") || str.contains("VBD"))
					active="passive";
		}		
		return active;
	}
	
	/*public static void main(String[] args) {
		HashMap<String,String> engPOSpair=new HashMap<String,String>();
		engPOSpair.put("the","DT");
		engPOSpair.put("girl","NN");
		engPOSpair.put("in","IN");
		engPOSpair.put("class","NN");
		engPOSpair.put("is","VBZ");
		engPOSpair.put("eating","VBG");
		engPOSpair.put("an","DT");
		engPOSpair.put("apple","NN");
		
		String[] eng= {"The", "girl","sitting","under","the","tree", "is", "eating", "an","apple","in","the","class"};
		String[] chin={"the","mainkalay","htainnay","aungmhar","the","thitpin","is","sarnay","takhu","apple","htaemhar","the","atan"};
		String[] pos={"DT","NN","VBG","UNDER","DT","NN","VBZ","VBG","DT","NN","IN","DT","NN"};
		new ParsingPhraseProgram(eng,chin,pos);
	}
*/
}
