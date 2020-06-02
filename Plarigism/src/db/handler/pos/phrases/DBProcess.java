package db.handler.pos.phrases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import POS.POST;

public class DBProcess {
	Connection con;
	String orPhrase;
	String lastword="";
	int count=0;
	
	public DBProcess(){
		
	}
	public void openConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/nlpdb?useUnicode=yes&characterEncoding=UTF-8","root","root");
	}
	public void closeConnection() throws SQLException
	{
		con.close();
	}
	public String SearchToDB(String sent,String sentWithWord) throws ClassNotFoundException, SQLException{
		String processedPhrase="";
		lastword=FindLastWord(sentWithWord);
	
		boolean b=isAllTagged(sent);
			if(b==false){
				
				//Find untagged phrase
				String phrase=FindUntaggedPhrase(sent);
				String wordwithcomma="";
				if(!phrase.isEmpty()){
						wordwithcomma=SplitWordfromSent(phrase);
						orPhrase=phrase;
						processedPhrase=FindWord(phrase);
				}			
			}//end of b==false		
			return processedPhrase;
	}
	
	String FindLastWord(String wordWithComma){
		String[] word=wordWithComma.split(",");
		int len=word.length;
		
		String str=word[len-1];
		return str;
	}
	
	
	
	String FindWord(String phrase) throws ClassNotFoundException, SQLException{
		String processedphrase="";
		if(count<100){
		++count;
		
		String tagPhrase=phrase;
		String previousword="";
		boolean numbersignal=false;
		String endSent="";
	    boolean  kasignal=false;
			
			String wordwithcomma=SplitWordfromSent(phrase);
			String[] words=wordwithcomma.split(",");
		loopForUnProcessedPhrase:
						for(String word:words){
							openConnection();
							if(numbersignal==true){
								word=previousword+word;
								numbersignal=false;
								previousword="";
							}
							if(word.equals("က") && ! word.equals("ကံ")){
								kasignal=true;
								previousword=word;
								continue;
							}
							else if(kasignal==true){
								word=previousword+word;
								word=FindDBToTest(word);
								kasignal=false;
							}
																
							String sqlstr="select * from framenettable  where MMWord=convert('"+word.trim()+"' using binary)";
							//System.out.println("SQL>>>>>>>>>>>>>>>>"+sqlstr);
					
							Statement st=con.createStatement();
							ResultSet rs=st.executeQuery(sqlstr);
							if(rs.next()){						
									String[] str=GetRSData(rs);
									String wordtype=str[4].trim();
									String postype=str[2].trim();
									String MMWord=str[1].trim();
									String possiblepair=str[5];
									
									if(wordtype.equals("indiv")){
										String taggedType="["+postype+"] ";
										
										//	orPhrase=PartialTag(orPhrase,word,taggedType);
											orPhrase=orPhrase.replaceFirst(word," "+word+taggedType);
											tagPhrase=tagPhrase.replace(word, "");
											break loopForUnProcessedPhrase;
											
									}//for indiv if
									else if(wordtype.equals("pair")){
										
												if(postype.equals("Number")){
													previousword=word;
													numbersignal=true;
												}
												else{//else if not number
														String combinephrase=pairWordProcess(word,possiblepair,phrase,wordtype);
														if(!combinephrase.contains(":") && (combinephrase.equals("NotFound") || !combinephrase.equals("NULL"))){
															
															orPhrase=orPhrase.replaceFirst(word, " "+word+" ");
															tagPhrase=tagPhrase.replace(word,"");
														}
														else{
															if(combinephrase.contains(":")){
																String[] combineAndType=combinephrase.split(":");
																String combine=combineAndType[0].trim();
																String type=combineAndType[1].trim();
																
																String taggedType=type;
																if(!combine.equals("က") && !(orPhrase.contains("လူ့") && combine.equals("လူ")))
															//		orPhrase=PartialTag(orPhrase,word,taggedType);
																	orPhrase=orPhrase.replaceFirst(combine," "+combine+taggedType+" ");
																tagPhrase=tagPhrase.replace(combine, "");
															}
															else{
																//orPhrase=PartialTag(orPhrase,word,postype);
																orPhrase=orPhrase.replaceFirst(word, " "+word+"["+postype+"] ");
																tagPhrase=tagPhrase.replace(word,"");
															}
															
															break loopForUnProcessedPhrase;
														}//inner else
												}//outer else
										
									}
									else {// for pair and
										
										//for number
										if(postype.equals("Number")){
											previousword=word;
											numbersignal=true;
										}
										else{//else if not number
													String combinephrase=pairWordProcess(word,possiblepair,phrase,wordtype);
													
													if(combinephrase.contains(":")){
														String[] combineAndType=combinephrase.split(":");
														String combine=combineAndType[0].trim();
														String type=combineAndType[1].trim();
														
														if(type.equals("NULL"))
															type="["+postype+"]";
														
														String taggedType=type;
														if(!combine.equals("က") && !(orPhrase.contains("လူ့") && combine.equals("လူ")))
														//	orPhrase=PartialTag(orPhrase,word,taggedType);
															orPhrase=orPhrase.replaceFirst(combine," "+combine+taggedType+" ");
														tagPhrase=tagPhrase.replace(combine, "");
													}
													else{
														//orPhrase=PartialTag(orPhrase,word,postype);
														orPhrase=orPhrase.replaceFirst(word, " "+word+"["+postype+"] ");
														tagPhrase=tagPhrase.replace(word,"");
													}													
													break loopForUnProcessedPhrase;
										}//end of if not number									
									}//end ofelse if  pair								
									
							}//end of rs.next if
							else{//not found in framenet table
										if(word.contains("က") && word.length()>1){
												
										}
										else{//else 6
											
											//UPDated 9-2-
											/*String foundResult=SearchingLUWords(word,"nounlu");
											System.out.println("OR PHRAE="+orPhrase+"...........word="+word);
											if(!foundResult.equals("null")){
												
													orPhrase=orPhrase.replaceFirst( word,word+"[N]");
												
													
											}
											else{
												System.out.println("FOR VERB.............."+word);
												foundResult=SearchingLUWords(word,"verbluframenet");*/
												
											endSent=word;
											tagPhrase=tagPhrase.replace(word, "");
												
																						
										}//else 6
							}//end of else not found rs.next
						}//end of for word:words
							
				//if(!endSent.equals("á�‹") || !endSent.equals(lastword))
				if(!endSent.equals("။") && !endSent.equals(lastword) )
					FindWord(tagPhrase);			
				processedphrase=orPhrase;
		}
		return processedphrase;
	}
	String SearchingLUWords(String searchword,String tablename) throws ClassNotFoundException, SQLException
	{
		String type="";
		
		LexicalUnitProcess luprocess=new LexicalUnitProcess();
		type=luprocess.FindLexicalUnitFromLU(searchword, tablename);
		
		return type;
	}
	String pairWordProcess(String word,String possiblepairs,String phrase,String wordtype){
		String replaceWord="";
		String pairStr="";
		String  combinetemp="";
		String  type="";
		
		boolean found=false;
		
		if(!possiblepairs.equals(";")){
							String[] pairs=possiblepairs.split(";");
							pairloop:
							for(String str:pairs){
													if(found==true)
														break pairloop;
													else{
																	 replaceWord=str.substring(0,str.indexOf("["));
																	 type=str.substring(str.indexOf("["),str.indexOf("]"))+"]";
																	 combinetemp=word.trim()+replaceWord.trim();
																	 System.out.println("Phrase......."+phrase+"////a combine word"+combinetemp+"....type="+type);
																	if(phrase.contains(combinetemp) ){	
																			pairStr=combinetemp+":"+type;																	
																			found=true;								
																	}
													}//end of else	
							}//end for
						
							if(found==false){
								if(!wordtype.equals("pair"))
									type="NULL";
									pairStr=word+":"+type;
									pairStr="NULL";
								 found=true;
							}
			/*else{
				pairStr="NotFound";
			}*/
		}
		else{
			String str=possiblepairs;
			replaceWord=str.substring(0,str.indexOf("["));
			 type=str.substring(str.indexOf("["),str.indexOf("]"))+"]";
			 combinetemp=word.trim()+replaceWord.trim();
			// System.out.println("phrase="+phrase+".......and combinetemp="+combinetemp);
			 if(phrase.contains(combinetemp)){
			//	 if(phrase.equals(combinetemp)){
					pairStr=combinetemp+":"+type;
					found=true;
			//	}
				 
			}
			 else{
				 if(!wordtype.equals("pair")){
					 pairStr=word+":"+type;
					 found=true;
				 }
				 else
					 pairStr="NotFound";
			 }
		}

		System.out.println("pair str...ၓၓၓၓၓၓၓၓၓၓၓၓၓၓ.............."+pairStr);
		return pairStr;
	}
	
	String[] GetRSData(ResultSet rs) throws SQLException{
		String[] str=new String[6];
		
		str[1] =rs.getString(2); //MMWOrd
		str[2]=rs.getString(3); //POSType
		str[3]=rs.getString(4);//EWord
		str[4]=rs.getString(5);//WordType
		str[5]=rs.getString(6);//POssiblePairs	
		return str;
	}
	
	String FindUntaggedPhrase(String sent){
				String untaggedphrase="";
				String[] phrases=sent.split("\\s");
			   for(String str:phrases){
				   str=str.trim();
				   if(!str.equals("") && !str.equals(null)){
					   if(!str.contains("[")){
						   untaggedphrase+=str;
					   }
				   }
		   }
		return untaggedphrase;
}
	
	//ဥပမာ မြေးကိုအဖိုးကရိုက်သည်။ ကရိုက် ဆိုတာကို ရှာမတွေ့ဘူးဖြစ်နေမှာ က က မခွဲထားလို့ အဲလိုမခွဲတဲ့ဟာအတွက် ခွဲပေးတာ။
	public String FindDBToTest(String word) throws SQLException{
		String sqlstr="select * from framenettable  where MMWord=convert('"+word.trim()+"' using binary)";
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery(sqlstr);
		if(rs.next()){						
				//intentionally leave blank
		}//end of rs.next if
		else{
			if(word.startsWith("က") && word.length()>1)
				word=word.substring(1);
		}	
		return word;
	}
	
	public boolean isAllTagged(String sent){
		boolean alltagged=false;
		if(sent.contains("[")){
			StringTokenizer sentences = new StringTokenizer(sent,"\\s");
			int totalcount=0;
			int count=0;
			ArrayList<String> tempArr=new ArrayList<String>();
			while (sentences.hasMoreTokens()) {
				tempArr.add(sentences.nextToken());
				++count;
			}		
			count=0;
			totalcount=count;
			for(String str:tempArr){
				if(tempArr.contains("["))
					++count;
			}			
			if(count==totalcount){
				alltagged=true;
			}
		}
		return alltagged;
	} 
	
	 String SplitWordfromSent(String sent){
			POST pos;
			String sentWithComma="";
				if(!sent.isEmpty()){
					sent=sent.replace(" ", "");
					pos=new POST(sent);
					ArrayList<String> sentArr=pos.getAllSegmentedWord();
					String oneSentence=sentArr.toString();
					//replace ခွင့် င့် က ကွဲနေတာကို ပြန်စိ မယ် အ ကိုလဲ ပြန် စိမယ်
					oneSentence=oneSentence.replace(" + ", ",");
					oneSentence=oneSentence.substring(1,oneSentence.length()-1);
					
					//khwint
					oneSentence=oneSentence.replace(",င့်", "င့်");
					
					//ah
					oneSentence=oneSentence.replace("အ,", "အ");		
					
					sentWithComma=oneSentence;
				}
				
				return sentWithComma;
		}
	
}
