package db.handler.pos.phrases;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SRLProcessing {
	
	static Connection con;
	static float exactSimilar=1.0f;
	static float subsetSimilar=0.5f;
	static float lexicalUnitSimilar=1.0f;
	static float coreSimilar=0.7f;
	static float noncoreSimilar=0.3f;
	static float notEqual=0.0f;
	static ArrayList<String > intersetArr=new ArrayList<String>();
	static ArrayList<String > unionArr=new ArrayList<String>();
	static float structurealpha=0.7f;
	//static float structurealpha=1.0f;
	static float ngrambeta=0.3f;
//	static float ngrambeta=0.0f;
	float jaccardsimithreshold=0.75f;
	public static ArrayList<String > synonymList=new ArrayList<String>();
	public static ArrayList<String > subsetList=new ArrayList<String>();
	public static ArrayList<String> lexicalUnitArr=new ArrayList<String>();

		public SRLProcessing(){
		
		}
		
		public static void openConnection() throws ClassNotFoundException, SQLException
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/nlpdb?useUnicode=yes&characterEncoding=UTF-8","root","root");
		}
		public static void closeConnection() throws SQLException
		{
			con.close();
		}
		
		public HashMap<Integer,String> FindPhrases(HashMap<Integer,String> taggedSentList) throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
			HashMap<Integer,String> srlphrases=new HashMap<Integer,String>();
			String sentStr="";
			ArrayList<String> wordPair=null;
			for(Integer key : taggedSentList.keySet())
			{
							sentStr=taggedSentList.get(key);
							sentStr=GetSpacesBetweenWord(sentStr);
							System.out.println("@@@@@@@@"+sentStr);
							byte[] sentBytes=sentStr.getBytes("UTF-8");
							String sentStr1=new String(sentBytes,"UTF-8");
							
								wordPair=wordTypePair();
								for(String wrPr:wordPair){
										String[] values=wrPr.split(":");
										String wordname=values[0].trim();
										String wordtype=values[1].trim();
										byte[] wordbyte=wordname.getBytes("UTF-8");
										String wordname1=new String(wordbyte,"UTF-8");
										
										
										//System.out.println(" In SRL , "+sentStr1+"..........."+wordname1+".........type="+wordtype);
										if(sentStr1.contains(wordname1) ){
										//	System.out.println("Sentence....Found>>>>>>>>>>>>>>>>>>..."+sentStr+" and replace word="+wordname+"{"+wordtype+"}");										
											
											
											if(wordname1.contains("စွာ")){
												if(!sentStr.contains("[Adv]"))
														sentStr=sentStr.replace(wordname, wordname+"[Adv]{"+wordtype+"}  ");
												else
													sentStr=sentStr.replace(wordname, wordname+"{"+wordtype+"}  ");	
											}
											else
												sentStr=sentStr.replace(wordname, " "+wordname+"["+wordtype+"] ");
										//	sentStr=sentStr.replace(wordname+wordname1, wordname+"["+wordname1+"]{"+wordtype+"} ");
										}
										if(sentStr.contains("။") && !sentStr.contains("EndSent"))
											sentStr=sentStr.replace("။", "။{EndSent}");
										
									//	System.out.println("After word table searched............@@@@@"+sentStr);
										sentStr=CheckOtherWord(sentStr);
									
								}//ned wordPair for		
									System.out.println("After ............@@@@@"+sentStr);
								sentStr=CheckOtherWord(sentStr);
								srlphrases.put(key, sentStr);				
				
			}//end for Integer key:taggedSentList.keySet()
			
			return srlphrases;
		}
		
		String CheckOtherWord(String sentstr){
			String str="";
			String[] phr=sentstr.split("\\s");
			for(int i=0;i<phr.length;i++){
				String phrase=phr[i];
				if(!phrase.trim().contains("[") && !phrase.trim().contains("{") && !phrase.trim().equals("") && !phrase.trim().isEmpty()){
					if(phrase.trim().equals("သည်") && !sentstr.contains("Subject"))
						phrase=" "+phrase+"{Subject} ";
					str+=phrase+" ";
				}
				else if(phrase.trim().contains("ကား[N]")){
					phrase=phrase.trim();
					int nextindex=i+1;
					String nextword=phr[nextindex];
					while(nextword.equals("")){
						nextindex+=1;
						nextword=phr[nextindex];
					}
					
					/*int previousindex=i+1;
					String previousword=phr[previousindex];
					while(previousword.equals("")){
						previousindex-=1;
						previousword=phr[previousindex];
					}
					*/
				//	System.out.println("Current word......."+phrase+"......nextword="+nextword);
					if(nextword.contains("ဖြင့်{How,withWhat}") || nextword.contains("Conj") || nextword.contains("How")||nextword.equals("ဖြင့်") || nextword.equals("နှင့်"))
					{
						
					}
					else{
						if(!sentstr.contains("Subject"))
								phrase=phrase.replace("ကား[N]", " ကား{Subject} ");
						//System.out.println("PHrase............"+phrase);
					}
						
					str+=phrase+" ";
				}
				else
					str+=phrase+" ";
			}
			
			return str;
		}
		
		String GetSpacesBetweenWord(String sentences){
			if(sentences.contains("]")){
				sentences=sentences.replace("]", "] ");
			}
			
			return sentences;
		}
		
		static ArrayList<String> wordTypePair() throws ClassNotFoundException, SQLException{
			ArrayList<String> wordPair=new ArrayList<String>();
			openConnection();
			String sqlstr="select * from words";
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(sqlstr);
			while(rs.next()){	
				String mmword=rs.getString(2);
				String wordtype=rs.getString(3);
				String temp=mmword+":"+wordtype;
				wordPair.add(temp);
			}
			closeConnection();
			return wordPair;
		}
		
	
		static String CheckSpecialToMakeSpace(String input){
			String[] signalWord={"သည်","မှာ","၌","၌","အား","ကို","လည်း","ပြီးနောက်","မှ","ဖြင့်","သို့","စွာ","သည်","ရန်","ရန်အလို့ဌာ","ကြောင့်"};
			for(String str:signalWord){
			//	System.out.println("စွာ။။။။။။။။။။။။။။။။။before "+input+".............."+str);
				if(input.contains(str) && !input.contains(str+"[")){
			//		System.out.println("စွာ။။။။။။။။။။။။။။။။။before "+input);
					input=input.replace(str, str+" ");
			//		System.out.println("စွာ။။။။။။။။။။။။။။။။။after"+input);
				}
			}
			
			return input;
		}
		public static  HashMap<String,Float>  NGramWordCompare(HashMap<Integer,HashMap<String,String>> inputPhrArr,HashMap<Integer,HashMap<String,String>> articlePhrArr) throws SQLException, ClassNotFoundException{		
			HashMap<String,Float> jaccardSimi=new HashMap<String,Float>();
			ArrayList<String> unionArr=new ArrayList<String>();
			ArrayList<String> intersetArr=new ArrayList<String>();
			HashMap<String,Float> simiPhrForASentence=new HashMap<String,Float>();
			
			ArrayList<String> inputStringArr=new ArrayList<String>();
			ArrayList<String> articleStringArr=new ArrayList<String>();
			
			
			for(Integer inputLine:inputPhrArr.keySet()){
				String temp="";
				HashMap<String,String> inputSentence=inputPhrArr.get(inputLine);		
				for(String inputPhrases:inputSentence.keySet()){
					String pp=CheckSpecialToMakeSpace(inputSentence.get(inputPhrases));
					temp+=pp;
				}
				inputStringArr.add(temp);
			}//end inputLine for
		
				for(Integer articleLine:articlePhrArr.keySet()){
					String temp="";
						HashMap<String,String> articleSentence=articlePhrArr.get(articleLine);
						for(String articlePhrases:articleSentence.keySet()){
							temp+=articleSentence.get(articlePhrases);
						}//end articlePhrases for
						articleStringArr.add(temp);		
				}//end articleLine for
				
				int inputLine=0;
				int articleLine=0;
				
				for(String input:inputStringArr){
					input=input.replace("။", "");
					input=input.replace("၊","");
					String[] inputValues=input.split("\\s+");
					inputValues=RemoveSpaceData(inputValues);
					++inputLine;
					articleLine=0;
					float similarityvalue=0.0f;
					
					for(String article:articleStringArr){
						article=article.replace("။", " ");
						article=article.replace("၊ "," ");
						String[] articleValues=article.split("\\s+");
						articleValues=RemoveSpaceData(articleValues);
						++articleLine;
						//input array
						similarityvalue=0.0f;
						int unionSize=inputValues.length+articleValues.length;
						for(String inputStr:inputValues){//for 1
								for(String articleStr:articleValues){//for 2								
									if(inputStr.equals(articleStr.trim())){
										similarityvalue+=1;
										--unionSize;
										break;
									}
									else{
									
									}
									
								}//end for 2
								
						}//end for1
						float simiValue=(float)similarityvalue/unionSize;
						//System.out.println("One sentence......article...Unionsize"+unionSize+".........value="+simiValue);
						jaccardSimi.put("Input "+inputLine+"&Article "+articleLine, simiValue);
						unionArr.clear();
						intersetArr.clear();
						lexicalUnitArr.clear();
						simiPhrForASentence.clear();
					}//article for
				}//input for
			
			return jaccardSimi;
			}
		
		private static void simiPhrForASentence(String inputStr,
				float exactSimilar2) {
			// TODO Auto-generated method stub
			
		}

		public static String[] RemoveSpaceData(String[] input){
			ArrayList<String> in=new ArrayList<String>();
			for(String str:input){
				str=str.trim();
				if(!str.isEmpty() && !str.equals(""))
					in.add(str);
			}
			String[] returnArr=new String[in.size()];
			int count=0;
			for(String str:in){
				returnArr[count++]=str;
			}
			
			return returnArr;
			
		}
			
		public static HashMap<String,Float> FindMostSimilarSentence(HashMap<String,Float> input,int inputLineCount){
			String inputLine="";
			String articleLine="";
			String foundAt="";
			HashMap<String,Float> inputPara=new HashMap<String,Float>();
			for(int i=1;i<=inputLineCount;i++){
				String name="Input "+i;
				float max=0.0f;
				for(String str:input.keySet()){
					if(str.contains(name)){
						if(max<input.get(str)){
							max=input.get(str);
							foundAt=str.substring(str.indexOf("&"));
						}
					}
					
				}//end for
				max=max*100;
				inputPara.put("Sentence "+i+"'s highest plarigism is detected at "+foundAt, max);
			}
			
			return inputPara;
		}
		
		
		public static HashMap<String,Float> calculateTotalJaccardScore(HashMap<String,Float> structureSimi,HashMap<String,Float> ngramSimi){
			HashMap<String,Float> allSimiValue=new HashMap<String,Float>();
			float totalscore=0.0f;
			
			for(String input:structureSimi.keySet()){
				for(String article:ngramSimi.keySet()){
						if(input.trim().equals(article.trim())){
							
							float structurevalue=structureSimi.get(input);
							float ngramvalue=ngramSimi.get(article);
							totalscore=structurevalue*structurealpha+ngramvalue*ngrambeta;
							System.out.println(input+"............."+totalscore);
							allSimiValue.put(input, totalscore);
						}
				}
			}
			
			return allSimiValue;
		}
		
		public static void PrintJaccardSimilarityValue(HashMap<String,Float> simi){
			for(String str:simi.keySet()){
				System.out.println(str+" Simi Value= "+simi.get(str));
			}
		}
		
		
		public static float checkEachPhrase(String phr1,String phr2){
			float equalType=0.0f;
			String[] str1=phr1.split("\\s");
			phr2=phr2.trim();
			
			ArrayList<String> inputArr=new ArrayList<String>();	
			Set<String> hs=new HashSet<String>();
			for(String str:str1){
				str=str.trim();
				if(!str.isEmpty() && !str.equals("") ){
					inputArr.add(str);
				}
			}
			
			hs.addAll(inputArr);
			inputArr.clear();
			inputArr.addAll(hs);
			
			int count=0;
			for(String str:inputArr){
					if(phr2.contains(str.trim()))
						++count;
			}
			if(count==inputArr.size())
				equalType=exactSimilar;
			else if(count!=0)
				equalType=subsetSimilar;
			
			
			return equalType;
		}
		
		public static  HashMap<String,Float>  StructureCompare(HashMap<Integer,HashMap<String,String>> inputPhrArr,HashMap<Integer,HashMap<String,String>> articlePhrArr) throws SQLException, ClassNotFoundException{		
			HashMap<String,Float> jaccardSimi=new HashMap<String,Float>();
			ArrayList<String> unionArr=new ArrayList<String>();
			ArrayList<String> intersetArr=new ArrayList<String>();
			HashMap<String,Float> simiPhrForASentence=new HashMap<String,Float>();
			ArrayList<String> lexicalUnitArr=new ArrayList<String>();
			
			for(Integer inputLine:inputPhrArr.keySet()){
				HashMap<String,String> inputSentence=inputPhrArr.get(inputLine);
				for(Integer articleLine:articlePhrArr.keySet()){
						HashMap<String,String> articleSentence=articlePhrArr.get(articleLine);
						for(String inputPhrases:inputSentence.keySet()){
							System.out.println("INPUT PHRSES>>>>>>>>>>>>>>"+inputPhrases);
									for(String articlePhrases:articleSentence.keySet()){
										System.out.println("ARTICLE PHRSES>>>>>>>>>>>>>>"+articlePhrases);
											
											unionArr.add(articlePhrases);
									}//end articlePhrases for
									unionArr.add(inputPhrases);
						}//end inputPhrases for
						
						unionArr=RemoveDuplicate(unionArr);
					
					//	System.out.println("Lexical............"+lexicalUnitArr.size());
						Print(lexicalUnitArr);
						//System.out.println("Similarity..............."+simiPhrForASentence.size());
						Float simiValue=PrintSimilarityValue(simiPhrForASentence,unionArr.size());
						//System.out.println("Union size="+unionArr.size()+"..........and interset size="+intersetArr.size());
						//System.out.println("Simivalue for ........InputLine="+inputLine+" and ArticleLine="+articleLine+" ======>"+simiValue);
						jaccardSimi.put("Input "+inputLine+ "&Article "+articleLine, simiValue);
						unionArr.clear();
						intersetArr.clear();
						lexicalUnitArr.clear();
						simiPhrForASentence.clear();
				}//end articleLine for
				
			}//end inputLine for
			
			return jaccardSimi;
			}

		
	/*public static  HashMap<String,Float>  StructureCompare(HashMap<Integer,HashMap<String,String>> inputPhrArr,HashMap<Integer,HashMap<String,String>> articlePhrArr) throws SQLException, ClassNotFoundException{		
		HashMap<String,Float> jaccardSimi=new HashMap<String,Float>();
		ArrayList<String> unionArr=new ArrayList<String>();
		ArrayList<String> intersetArr=new ArrayList<String>();
		HashMap<String,Float> simiPhrForASentence=new HashMap<String,Float>();
		ArrayList<String> lexicalUnitArr=new ArrayList<String>();
		
		for(Integer inputLine:inputPhrArr.keySet()){
			HashMap<String,String> inputSentence=inputPhrArr.get(inputLine);
			for(Integer articleLine:articlePhrArr.keySet()){
					HashMap<String,String> articleSentence=articlePhrArr.get(articleLine);
					for(String inputPhrases:inputSentence.keySet()){
						System.out.println("INPUT PHRSES>>>>>>>>>>>>>>"+inputPhrases);
								for(String articlePhrases:articleSentence.keySet()){
									System.out.println("ARTICLE PHRSES>>>>>>>>>>>>>>"+articlePhrases);
										if(inputPhrases.equals(articlePhrases)){//if 1
												String str1=inputSentence.get(inputPhrases);
												String str2=articleSentence.get(articlePhrases);
												
												
												if(str1.equals(str2)){
													simiPhrForASentence.put(inputPhrases, exactSimilar);
													intersetArr.add(inputPhrases+"...."+str1);
												}
												else if(str1.contains(str2) || str2.contains(str1)){
												
													
													simiPhrForASentence.put(inputPhrases, subsetSimilar);
													intersetArr.add(inputPhrases+"...."+str1);
													subsetList.add(str1+"(ႈInput ), "+str2+"(Article)");
													System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC............."+str1+"(ႈInput ) , "+str2+"(Article)");
												}
												else{//not exact match else 1
													String value1="",type1="",value2="",type2="";
														if(str1.contains("[")){
															value1=str1.substring(0,str1.indexOf("["));
															type1=str1.substring(str1.indexOf("["),str1.indexOf("]")+1);
															type1=type1.replace("[", "").trim();
															type1=type1.replace("]", "").trim();
														}// end if str1.contains("[")){
														if(str2.contains("[")){
															value2=str2.substring(0,str2.indexOf("["));
															type2=str2.substring(str2.indexOf("["),str2.indexOf("]")+1);
															type2=type2.replace("[", "").trim();
															type2=type2.replace("]", "").trim();
														}// end if str1.contains("[")){
													
														System.out.println("Str1>>>>"+str1);
														System.out.println("Str2>>>>"+str2);
														System.out.println("TYPE1>>>"+type1+"...value1="+value1+">.........TYPE2="+type2+".....value2="+value2);
														if(type1.equals(type2)){
																			if(value1.equals(value2)){//if 1.2
																				simiPhrForASentence.put(inputPhrases, exactSimilar);
																				intersetArr.add(inputPhrases+"...."+str1);
																			}
																			else{//else 1.2
																				//Find LU
																				LexicalUnitProcess pp=new LexicalUnitProcess();
																				String str1LUSet=pp.FindLexicalUnitFromMMWord(type1,value1);
																				if(!str1LUSet.equals("null")){//if 33
																					if(str1LUSet.contains(value2)){
																						intersetArr.add(inputPhrases+"...."+str1);
																						simiPhrForASentence.put(inputPhrases+"LU", lexicalUnitSimilar);
																						lexicalUnitArr.add(str1+"(Input).........."+str2+"(Article)");
																						synonymList.add(str1+"(Input).........."+str2+"(Article)");																	
																					}
																					else{//else 4
																						if(str1LUSet.isEmpty() && str1LUSet.equals("")){//if 3.1
																							String str1LUSsets=pp.FindLexicalUnitSet(type1,value1);
																							if(str1LUSsets.contains(value2)){
																								intersetArr.add(inputPhrases+"...."+str1);
																								simiPhrForASentence.put(inputPhrases+"LU", lexicalUnitSimilar);
																								lexicalUnitArr.add(str1+"(Input).........."+str2+"(Article)");
																								synonymList.add(str1+"(Input).........."+str2+"(Article)");
																								
																							}
																							else{
																								//to check again
																							//	System.out.println("SYNONYM FOUND>>>>>>>>>>>>>>>>>>>>"+str1+"(Input).........."+str2+"(Article)");
																							}//if 3.1
																						}	//else 4
																				}//end if 33
																				
																				}//else 4
																			}//end else 1.2
																			
														}//end if 1.2
												}//end else 1
												
											
										}//end if 1
										else{
											//System.out.println("SYNONYM FOUND>>>>>>>>>>>>>>>>>>>>");
										}
										unionArr.add(articlePhrases);
								}//end articlePhrases for
								unionArr.add(inputPhrases);
					}//end inputPhrases for
					
					unionArr=RemoveDuplicate(unionArr);
				//	System.out.println("Union size "+unionArr.size());
				//	Print(unionArr);
				//	System.out.println("Interset size "+intersetArr.size());
			//		Print(intersetArr);
					System.out.println("Lexical............"+lexicalUnitArr.size());
					Print(lexicalUnitArr);
					//System.out.println("Similarity..............."+simiPhrForASentence.size());
					Float simiValue=PrintSimilarityValue(simiPhrForASentence,unionArr.size());
					//System.out.println("Union size="+unionArr.size()+"..........and interset size="+intersetArr.size());
					//System.out.println("Simivalue for ........InputLine="+inputLine+" and ArticleLine="+articleLine+" ======>"+simiValue);
					jaccardSimi.put("Input "+inputLine+ "&Article "+articleLine, simiValue);
					unionArr.clear();
					intersetArr.clear();
					lexicalUnitArr.clear();
					simiPhrForASentence.clear();
			}//end articleLine for
			
		}//end inputLine for
		
		return jaccardSimi;
		}
*/	
	static Float  PrintSimilarityValue(HashMap<String,Float> arr,int count){
		Float totalSimi=0.0f;
		for(String str:arr.keySet()){
			totalSimi+=arr.get(str);
			System.out.println(str+".....value="+arr.get(str));
		}
		totalSimi=(float)totalSimi/count;
		return totalSimi;
	}
	static void Print(ArrayList<String>  str){
		for(String temp:str){
			System.out.println(str);
		}
	}
	public static ArrayList<String> RemoveDuplicate(ArrayList<String> arr){
		Set<String> hs=new HashSet<String>();
		hs.addAll(arr);
		arr.clear();
		arr.addAll(hs);
		return arr;
	}

	
	static float GetsimilairtyPerSentence(HashMap<String,Float> similarity){
		float sum=0.0f;
		for(String str:similarity.keySet()){
			sum+=similarity.get(str);
		}
		
		return sum;
	}
	
		public static void PrintData(ArrayList<String> Arr){
			for(String str:Arr){ 
				System.out.println(str);
			}
		}
		
		//Subject á€¡á€–á€½á€¬á€¸[N] á€�á€¯á€�á€º[N] ....
	/*	public static HashMap<Integer,HashMap<String,HashMap<String,String>>> GetSinglePhraseWithSuperPhrase(HashMap<Integer,HashMap<String,String>> inputParagraph){
			HashMap<String,HashMap<String,String>> inputPara=new HashMap<String,HashMap<String,String>>();	
			HashMap<Integer,HashMap<String,HashMap<String,String>>> returnArr=new HashMap<Integer,HashMap<String,HashMap<String,String>>>();
			
			for(Integer pp:inputParagraph.keySet()){
						inputPara=new HashMap<String,HashMap<String,String>>();	
								HashMap<String,String> input=inputParagraph.get(pp);
								for(String phrasesInput:input.keySet()){
									String key=phrasesInput;
									String value=input.get(phrasesInput);
									String[] phrases=value.split("\\s");
									String wholestr="";
									HashMap<String,String> temp=new HashMap<String,String>();
									for(String str:phrases){
											if(!str.isEmpty() && !str.equals("") && str.contains("[")){
												String value1=str.substring(0,str.indexOf("["));
												String value2=str.substring(str.indexOf("[")+1,str.indexOf("]"));
												temp.put(value2, value1);
											}//check str is not empty
									}//inner for
									inputPara.put(key,temp);
							}//inner for
							returnArr.put(pp, inputPara);
			}//outer for					
			return returnArr;
		}
		*/
		public static HashMap<Integer,HashMap<String,String>> tagsPerPhrase(HashMap<Integer,HashMap<String,String>> inputParagraph){
			HashMap<String,String> inputPara=new HashMap<String,String>();	
			HashMap<Integer,HashMap<String,String>> returnArr=new HashMap<Integer,HashMap<String,String>>();
			
		//	System.out.println("INPUT PARA>>>>>>>>>>>>>>>"+inputParagraph);
			
			for(Integer kk:inputParagraph.keySet()){
				inputPara=new HashMap<String,String>();	
				HashMap<String,String> input=inputParagraph.get(kk);
						for(String phrasesInput:input.keySet()){
							String key=phrasesInput;
							String value=input.get(phrasesInput);
							String[] phrases=value.split("\\s");
							String wholestr="";
						
							for(String str:phrases){
							//	System.out.println("STR>>>>>>>>>>>>>>>>>"+str);
									if(!str.isEmpty() && !str.equals("") && str.contains("[")){
										wholestr+=str+" ";
									}//check str is not empty
							}//inner for
							inputPara.put(key,wholestr);
						//	System.out.println("KEY="+key+"...........str="+wholestr);
					}//outer for
						
						returnArr.put(kk, inputPara);
			}		
			return returnArr;
		}
		
		public  HashMap<Integer,HashMap<String,String>>  SRLProcess(HashMap<Integer,String> taggedSentList) throws ClassNotFoundException, SQLException{
			HashMap<Integer,HashMap<String,String>> Para=new HashMap<Integer,HashMap<String,String>>();
			HashMap<String,String> phrPerSentWithPhrName;
			
			
			for(Integer key : taggedSentList.keySet())
			{
				 	String strTemp=taggedSentList.get(key);
				 //	System.out.println("BEOFRE<<<<<<<<<<<<<<<"+strTemp);
				 	phrPerSentWithPhrName=new HashMap<String,String>();
				 	int count=0;
				 	if(strTemp.contains("{")){
				 			while(strTemp.contains("{") ){
				 				
				 						String phrases=strTemp.substring(0,strTemp.indexOf("{"));
							 			String phraseName=strTemp.substring(strTemp.indexOf("{")+1,strTemp.indexOf("}"));
					 		
							 			strTemp=strTemp.replace(phrases, "");
							 			strTemp=strTemp.replace("{"+phraseName+"}", "");
							 			if(phraseName.equals("EndSent"))
							 				phraseName="MainVerb";
							 			
							 		//	System.out.println(key+"...........phraseName="+phraseName+"..$$$$$$$$$$...and phrases="+phrases);
							 			phrPerSentWithPhrName.put(phraseName, phrases);							 			
				 			}//while
				 	}//if				 	
				 	Para.put(key, phrPerSentWithPhrName);				   
			}//outer for
			return Para;		
		}
		
	
		public void CompareStructure(HashMap<Integer,String> inputPara,HashMap<Integer,String> articlePara){
			
					//Input
					for(Integer inputIT:inputPara.keySet()){//for 1
								String  eachInputSent=inputPara.get(inputIT);
								String[] inputArr=eachInputSent.split("}");
								System.out.println("Input"+eachInputSent+"........its size="+inputArr.length);
								for(Integer articleIT:articlePara.keySet()){//for2
										String eachArticleSent=articlePara.get(articleIT);
										String[] articleArr=eachArticleSent.split("}");
										System.out.println("Article="+eachArticleSent+"..........its size="+articleArr.length);
										
										
										// compare sub phrases of each input and article sentences
										for(String inputPhr:inputArr){// for 3
											String oneInputPhr=inputPhr.trim();
												for(String articlePhr:articleArr){//for 4
																String oneArticlePhr=articlePhr.trim();
																System.out.println("Input="+oneInputPhr+".............article="+oneArticlePhr);
																if(oneInputPhr.contains("{") || oneArticlePhr.contains("{"))
																{// if 5
																				String type1=oneInputPhr.substring(oneInputPhr.indexOf("{"),oneInputPhr.indexOf("}"));
																				String type2=oneArticlePhr.substring(oneArticlePhr.indexOf("{"),oneArticlePhr.indexOf("}"));
																				
																				if(type1.equals(type2)){//type 6
																					String structureInput=oneInputPhr.replace(type1, "").trim();
																					structureInput=structureInput.replace("{", "").trim();
																					structureInput=structureInput.replace("}", "").trim();
																					String structureArticle=oneArticlePhr.replace(type2, "").trim();
																					structureArticle=structureArticle.replace("{", "").trim();
																					structureArticle=structureArticle.replace("}", "").trim();
																					// [N] [N] {Subject}
																					String compareResult=CompareStructureInExactSubsetSyno(structureInput,structureArticle);
																					
																				}//type 6
																}//end if 5
												}//end for 4
										}//end for 3
										
								}//for 2
						
						
					}//end for1	
		}
		
		public String CompareStructureInExactSubsetSyno(String inputPhr,String articlePhr){
			String compareresult=null;
			String[] inputArr=inputPhr.split("\\s");
			String[] articleArr=articlePhr.split("\\s");
			for(String input:inputArr){
					for(String article:articleArr){
						 String iPhr=input.trim();
						 String aPhr=article.trim();
						 System.out.println("INPUT PHR>>>>>>>"+iPhr);
						 System.out.println("Article PHR>>>>>>>"+aPhr);
								/* if(iPhr.equals(aPhr)){
									 compareresult="Exact";
								 }
								 else{
									    
								 }*/
					}
			}
			
			
			return compareresult;
		}
		public HashMap<Integer,String>  SplitParaIntoSent(String paraInput){
			
			HashMap<Integer,String> para=new HashMap<Integer,String>();
			String[] tempArr=paraInput.split("EndSent}");
			for(int i=0;i<tempArr.length;i++){
				String str=tempArr[i].trim();
				if(str.contains("။{")){
					str=str.replace("။{", "။");
					para.put(i+1, str);
				}
				else if(!str.trim().isEmpty()){
					if(str!=null){
						para.put(i+1, str);
					}
				}
				
			}
			//System.out.println("Para size="+para.size());
			//PrintResult(para);
			return para;
			
		}
		public static void PrintResult(HashMap<Integer,String> phraseSent){
			String strtemp="";
			for(Integer key:phraseSent.keySet()){
				System.out.println("Sentence No:"+key+".....sent="+phraseSent.get(key));
			}		
		}
}



