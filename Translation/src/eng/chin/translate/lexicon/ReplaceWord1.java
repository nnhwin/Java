package eng.chin.translate.lexicon;

public class ReplaceWord1 {
	
	public String DoPerform(String str,String[] pos,String[] eng){
		String[] phr=str.split("#");
		String sent_temp="";
		for(int i=0;i<phr.length;i++){
			String onePhrase=phr[i].trim();
			
			if(!onePhrase.equals("") && onePhrase!=null){
				if(onePhrase.contains("["))
				{				
					String phraseName=onePhrase.substring(0,onePhrase.indexOf("["));
					String temp=onePhrase.replace(phraseName, "").trim();
					String r_value=DoRePhrase(temp,pos,eng);
					String combine="#"+phraseName+"["+r_value+"]";
					sent_temp+=combine;
				}
			} 
		}//phrase
		
		//System.out.println("After Verb........."+sent_temp);
		
		//Start Time Here
		sent_temp=GetTimePhrase(sent_temp);
		sent_temp=CheckPlaceOrTime(sent_temp);
		
		return sent_temp;
	}//end function
	
	String CheckPlaceOrTime(String input){
		String output="";
		String[] phr=input.trim().split("#");
		for(int i=0;i<phr.length;i++){
			String onePhr=phr[i].trim();
			if(!onePhr.equals("") && onePhr!=null){
				if(TimeWords(onePhr)==true && onePhr.contains("NP_Place"))
					onePhr=onePhr.replace("NP_Place", "NP_Time");
				output+="#"+onePhr+" ";
			}
		}		
		return output;
	}
	
	boolean TimeWords(String phr){
		boolean found=false;
		String[] timewords={"morning","evening","night","today","weekends",
				"weekend","weekday","weekdays","tonight","clock","tomorrow","next time","next day","yesterday","everyday"};
		
		for(int i=0;i<timewords.length;i++){
			if(phr.contains(timewords[i])){
				found=true;
				break;
			}
		}
		
		return found;
	}
	
	String GetTimePhrase(String sent){
		String timephr="";
		String[] str=sent.split("#");
		for(int i=0;i<str.length;i++){
			String temp=str[i].trim();
			if(temp!=null && !temp.equals("")){
				if(temp.contains("NP_Place")){
					temp=CheckTimeSignal(temp);
					timephr+="#"+temp+" ";
				}
				else if(temp.contains("NP")){
					temp=CheckTimeSignal(temp);
					timephr+="#"+temp+" ";
				}
				else
					timephr+="#"+temp+" ";
			}
		}//for
		
		
		return timephr;
	}
	
	String CheckTimeSignal(String phrase){
		//System.out.println(" IN TIME CHECK ............"+phrase);
		if(phrase.contains("every") && phrase.contains("morning")){
			phrase=phrase.replace("every", "]#NP_Time[every");	
		}
		else if(phrase.contains("every") && phrase.contains("evening")){
			phrase=phrase.replace("every", "]#NP_Time[every");	
		}
		else if(phrase.contains("every") && phrase.contains("night")){
			phrase=phrase.replace("every", "]#NP_Time[every");	
		}
		else if(phrase.contains("this") && phrase.contains("evening")){
			phrase=phrase.replace("this", "]#NP_Time[this");	
		}
		else if(phrase.contains("this") && phrase.contains("morning")){
			phrase=phrase.replace("this", "]#NP_Time[this");	
		}
		else if(phrase.contains("this") && phrase.contains("night")){
			phrase=phrase.replace("this", "]#NP_Time[this");	
		}
		else if(phrase.contains("today"))
		{
			phrase=phrase.replace("today", "]#NP_Time[today");	
		}
		else if(phrase.contains("tonight"))
		{
			phrase=phrase.replace("tonight", "]#NP_Time[tonight");	
		}
		else if(phrase.contains("tomorrow"))
		{
			phrase=phrase.replace("tomorrow", "]#NP_Time[tomorrow");	
		}
		else if(phrase.contains("yesterday"))
		{
			phrase=phrase.replace("yesterday", "]#NP_Time[yesterday");	
		}
		else if(phrase.contains("everyday"))
		{
			phrase=phrase.replace("everyday", "]#NP_Time[everyday");	
		}
		
		if(phrase.contains("sim") && phrase.contains("zing sang")){
			phrase=phrase.replace("sim", "]#NP_Time[sim");	
		}
		else if(phrase.contains("sim") && phrase.contains("zan")){
			phrase=phrase.replace("sim", "]#NP_Time[sim");	
		}
		else if(phrase.contains("sim") && phrase.contains("ni")){
			phrase=phrase.replace("sim", "]#NP_Time[sim");	
		}
		else if(phrase.contains("tu ni"))
		{
			phrase=phrase.replace("tu ni", "]#NP_Time[tu ni");	
		}
		else if(phrase.contains("zan ni"))
		{
			phrase=phrase.replace("zan ni", "]#NP_Time[zan ni");	
		}
		else if(phrase.contains("zing ciang"))
		{
			phrase=phrase.replace("zing ciang", "]#NP_Time[zing ciang");	
		}
		else if(phrase.contains("everyday"))
		{
			phrase=phrase.replace("everyday", "]#NP_Time[everyday");	
		}
		
		
		phrase=phrase.replace(",]","]");
		phrase=phrase.replace("NP[]","");
		phrase=phrase.replace("#NP_Time[]","");
		return phrase;
	}
	
	String DoRePhrase(String str1,String[] pos,String[] eng){
		
		String retrun_value="";
		//System.out.println("STR 1 "+str1);
		if(str1.contains(","))
		{
			String[] temp=str1.split(",");
			for(int i=0;i<temp.length;i++){
				String str=temp[i].trim();	
				String rr=WordReplace(str,pos,eng);			
				retrun_value+=rr+",";
			}						
		}
		else
		{
			String rr=WordReplace(str1,pos,eng);
			retrun_value+=rr+",";
		}
		
		if(retrun_value.endsWith(","))
			retrun_value=retrun_value.substring(0,retrun_value.length()-1);
		return retrun_value;
	}
	
	String WordReplace(String str1,String[] str2,String[] eng)
	{
		String pp=str1;
	
		if(str1.contains("20")){
			str1=eng[20]+"/"+str2[20]+"-20";		
		}
		else if(str1.contains("19")){
			str1=eng[19]+"/"+str2[19]+"-19";		
		}
		else if(str1.contains("18")){
			str1=eng[18]+"/"+str2[18]+"-18";		
		}
		else if(str1.contains("17")){
			str1=eng[17]+"/"+str2[17]+"-17";		
		}
		else if(str1.contains("16")){
			str1=eng[16]+"/"+str2[16]+"-16";		
		}
		else if(str1.contains("15")){
			str1=eng[15]+"/"+str2[15]+"-15";		
		}
		else if(str1.contains("14")){
			str1=eng[14]+"/"+str2[14]+"-14";		
		}
		else if(str1.contains("13")){
			str1=eng[13]+"/"+str2[13]+"-13";		
		}
		else if(str1.contains("12")){
			str1=eng[12]+"/"+str2[12]+"-12";		
		}
		else if(str1.contains("11")){
			str1=eng[11]+"/"+str2[11]+"-11";	
		}
		else if(str1.contains("10")){
			str1=eng[10]+"/"+str2[10]+"-10";		
		}
		else if(str1.contains("9")){
			str1=eng[9]+"/"+str2[9]+"-9";	
		}
		else if(str1.contains("8")){
			str1=eng[8]+"/"+str2[8]+"-8";		
		}
		else if(str1.contains("7")){
			str1=eng[7]+"/"+str2[7]+"-7";		
		}
		else if(str1.contains("6")){
			str1=eng[6]+"/"+str2[6]+"-6";	
		}
		else if(str1.contains("5")){
			str1=eng[5]+"/"+str2[5]+"-5";	
		}
		else if(str1.contains("4")){
			str1=eng[4]+"/"+str2[4]+"-4";	
		}
		else if(str1.contains("3")){
			str1=eng[3]+"/"+str2[3]+"-3";
		}
		else if(str1.contains("2")){	
			str1=eng[2]+"/"+str2[2]+"-2";
		}
		else if(str1.contains("1") && !str1.contains("10")){	
			str1=eng[1]+"/"+str2[1]+"-1";
		}
		else if(str1.contains("0") && !str1.contains("10") && !str1.contains("20")){
			
				str1=eng[0]+"/"+str2[0]+"-0";
			
		}
		
		return str1;
	}
	
	
}
