package eng.chin.translate.lexicon;

import java.awt.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetPhrases {
	
	Boolean activeVerb=false;
	HashMap<String,String> engPhraseTypePairs=new HashMap<String,String>();
	HashMap<String,String> engChinaPairs=new HashMap<String,String>();
	String posSeq="";
	
	
	public Boolean getActiveVerb() {
		return activeVerb;
	}
	public void setActiveVerb(Boolean activeVerb) {
		this.activeVerb = activeVerb;
	}
	int CountVerb(String sent){
		int count=0;
		String[] str=sent.split(",");
		for(int i=0;i<str.length;i++){
			if(str[i].startsWith("VB"))
				++count;
		}
		return count;
	}
	public GetPhrases(String posSeq,HashMap<String,String> engPoS){
		this.posSeq=posSeq;
		HashMap<String,String> engPOS=engPoS;
		//Count Verb (base+heling verb)
		
		
		HashMap<String,String> pair=GetEngChinPhrase();
		/*System.out.println("Eng to Chin Phrase Structure ");
		for (Map.Entry<String,String> num : pair.entrySet())  {
			System.out.println(num.getKey()+"............"+num.getValue());
		}
		
		System.out.println("Eng to Chin Phrase Structure ");
		for (Map.Entry<String,String> engPair : engPhraseTypePairs.entrySet())  {	
			System.out.println(engPair.getKey()+"............"+engPair.getValue());
		}
		
		System.out.println("Eng to Chin Phrase Structure ");
		for (Map.Entry<String,String> engChinPair : engChinaPairs.entrySet())  {	
			System.out.println(engChinPair.getKey()+"............"+engChinPair.getValue());
		}*/
				
		String afterVerbSent=GetVerbPhrase(engPOS);
		System.out.println("After verb "+afterVerbSent);
	//	String afterSmallNounPhrase=GetSmallNounPhrase(engPOS,afterVerbSent);
	//	System.out.println("After Noun => "+afterSmallNounPhrase);
		
		String afternoun=GetSmallNounPhrase(engPOS,engPhraseTypePairs,afterVerbSent);
		System.out.println("After noun "+afternoun);
	}
	
	String ChangePreposition(String engPOS){
		String str="";
		if(str.contains("TO"))
				str=str.replace("TO", "PREP");
		return str;
	}
	
	String GetSmallNounPhrase(HashMap<String,String> engPoS,HashMap<String,String> engPhraseTypePairs,String sent){
		String afternoun="";
		//System.out.println("POSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		/*HashMapPrint(engPoS);
		
		System.out.println("TYPEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
		HashMapPrint(engPhraseTypePairs);*/
		
		
		System.out.println(sent);
		ArrayList<String>  nphrases=GetSmallNP();
		String[] phrases=sent.split("/");
		int count=0;
		
		for(int j=0;j<phrases.length;j++){
			String temp_phr=phrases[j];
			if(temp_phr.contains("{")){
				afternoun+=temp_phr+"/";
			}
			else{
				for(int i=0;i<nphrases.size();i++){
					String temp_nounphr=nphrases.get(i);
					System.out.println(temp_phr+"............."+temp_nounphr);
					if(temp_phr.equalsIgnoreCase(temp_nounphr)){
						temp_phr=temp_phr+"{NP}/";
						afternoun+=temp_phr;
						break;
					}
					else if(temp_phr.contains(temp_nounphr)){
						temp_phr=temp_phr.replace(temp_nounphr,temp_nounphr+"{NP}/");
						afternoun+=temp_phr;
						break;
					}
				}
			}
			
		}
		
		return afternoun;
	}
	
	void HashMapPrint(HashMap<String,String>  data){
		for (Map.Entry<String,String> num : data.entrySet())  {
			System.out.println(num.getKey()+"............"+num.getValue());
		}	
	}
	
	
	String GetVerbPhrase(HashMap<String,String> engPoS){
		int count=CountVerb(posSeq);
		for (Map.Entry<String,String> engPair : engPhraseTypePairs.entrySet())  {		
			for (Map.Entry<String,String> chinPair : engChinaPairs.entrySet())  {				
				String eng=engPair.getKey();
				String chin=chinPair.getKey();
				
				
				//eng Pair DT,NN NP
				//chinPair DT,NN(eng) NN,DT(chin)
				if(eng.equalsIgnoreCase(chin)){
					String verbphrase=engPair.getKey();
					String type=engPair.getValue();
					//verb Check
					if(verbphrase.contains("VB")){
						int verbcount=CountVerb(verbphrase);
						if(verbcount==count){
							if(posSeq.contains(verbphrase))
							{
								//Get Eng For POS
								String posWithWord=GetEngWordsForPOS(verbphrase,engPoS);
								posSeq=posSeq.replace(verbphrase, "/"+posWithWord+"{"+type+"}/");
							}
						}
					}
					//System.out.println("Eng Pattern "+engPair.getKey()+"  Chin Pattern   "+chinPair.getKey()+"   Phrase Type  "+engPair.getValue());
					break;
				}
				
			}
			
		}//end for eng Pair
		
		return posSeq;
	}
	
	String GetEngWordsForPOS(String str,HashMap<String,String> engPOS){
		String vv="";
		vv=str;
		String newValue="";
		String[] strs=vv.split(",");
		for(int i=0;i<strs.length;i++){
			String pos=strs[i];
			for (Map.Entry<String,String> engPOSPair : engPOS.entrySet())  {	
				String eng=engPOSPair.getValue();
				if(eng.equalsIgnoreCase(pos)){
					newValue+=engPOSPair.getKey()+"["+pos+"],";
					break;
				}
			}
		}
		newValue=newValue.substring(0,newValue.length()-1);
		return newValue;
	}
	
	Connection con;
	public void openConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/translationdb?useUnicode=yes&characterEncoding=UTF-8","root","root");
	}
	public void closeConnection() throws SQLException
	{
		con.close();
	}
	public HashMap<String,String> GetEngChinPhrase()
	{
		HashMap<String,String> posPatternPair=new HashMap<String,String>();
		try {
			openConnection();
			String sqlstr="select * from rules";	
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(sqlstr);
			while(rs.next()){
				String engSeq=rs.getString("EngPatterns");
				String chinSeq=rs.getString("ChinPatterns");
				String phraseType=rs.getString("PhraseTypes");
				posPatternPair.put(engSeq, chinSeq);
				engPhraseTypePairs.put(engSeq, phraseType);
				engChinaPairs.put(engSeq,chinSeq);
			}
			closeConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return posPatternPair;	
	}
	public static void main(String[] args) {
		HashMap<String,String> engPOSpair=new HashMap<String,String>();
		engPOSpair.put("the","DT");
		engPOSpair.put("girl","NN");
		engPOSpair.put("in","IN");
		engPOSpair.put("class","NN");
		engPOSpair.put("is","VBZ");
		engPOSpair.put("eating","VBG");
		engPOSpair.put("an","DT");
		engPOSpair.put("apple","NN");
		
		new GetPhrases("DT,NN,IN,DT,NN,VBZ,VBG,DT,NN",engPOSpair);
	}

	ArrayList<String> GetSmallNP(){
		ArrayList<String> nphrases=new ArrayList<String>();
		nphrases.add("DT,NN");
		nphrases.add("DT,JJ,NN");
		
		return nphrases;
	}
	
}
