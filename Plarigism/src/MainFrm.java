

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import property.Property;
import verb.frame.element.SimilarObject;
import db.handler.pos.phrases.CompareSentence;
import db.handler.pos.phrases.SRLProcessing;
import POS.POST;
import nlp.pos.processes.*;
import nlp.property.simi.object.SimiObject;

public class MainFrm extends Shell {
	static Display display = Display.getDefault();
	static MainFrm shell = new MainFrm(display);
	public Text inputTxt,articleTxt;
	Button posBut,srlBut,prlBut,clrBut,closeBut;
	
	 HashMap<Integer,String> sentListForInput=new HashMap<Integer,String>();
	 HashMap<Integer,String> wordListPerSentenceForInput=new HashMap<Integer,String>();
	 HashMap<Integer,String> taggedSentenceListForInput=new HashMap<Integer,String>();
	 
	 HashMap<Integer,String> sentListForArticle=new HashMap<Integer,String>();
	 HashMap<Integer,String> wordListPerSentenceForArticle=new HashMap<Integer,String>();
	 HashMap<Integer,String> taggedSentenceListForArticle=new HashMap<Integer,String>();
	
	 SRLProcessing srlprocess;
	 HashMap<Integer,String> srlphrasesInput=new HashMap<Integer,String>();

	 HashMap<Integer,String> srlphrasesArticle=new HashMap<Integer,String>();
	 HashMap<String,String>  inputCoreType=new HashMap<String,String>();
	 public float ExactSimilarValue=1.0f;
		public float SubsetSimilarValue=0.7f;
		public float SubsetSynonymSimilarValue=0.7f;
		public float SynonymSimilarValue=0.7f;
		public float CoreValue=1.0f;
		public float NonCoreValue=0.7f;
		public float structureValue=0.5f;
		public float ngramValue=0.5f;
		
		FileWriter writer;
		FileWriter simiWriter;
		File dir;
	public static void main(String args[]) {
		try {
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public MainFrm(Display display) {
		super(display, SWT.SHELL_TRIM);
		setBackground(SWTResourceManager.getColor(133,133,133));
		Label inputLbl=new Label(this, SWT.None );
		inputLbl.setBounds(10, 14, 300, 24);
		Font font=new Font(inputLbl.getDisplay(),new FontData("Myanmar3",12,SWT.ITALIC));
		inputLbl.setFont(font);
		inputLbl.setText("စမ်းသပ်လိုသည့် စာပိုဒ်ဝင်ရန်.......................");
		
		inputTxt = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		inputTxt.setFont(SWTResourceManager.getFont("Myanmar3", 12,
				SWT.NORMAL));
		inputTxt.setBounds(10, 50, 800, 200);
		
		Label articlelbl=new Label(this, SWT.None );
		articlelbl.setBounds(10, 280, 400, 24);
		articlelbl.setFont(font);
		articlelbl.setText("တိုက်စစ်လိုသော စာပိုဒ်များထည့်ရန်.......................");
		
		articleTxt = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		articleTxt.setFont(SWTResourceManager.getFont("Myanmar3", 12,
				SWT.NORMAL));
		articleTxt.setBounds(10, 320, 800, 200);
		
		posBut = new Button(this, SWT.NONE);
		posBut.setBounds(30, 550, 120, 20);
		posBut.setText("POS Tagging");
		posBut.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		posBut.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						POSTagger();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			}		
	});

		srlBut = new Button(this, SWT.NONE);
		srlBut.setBounds(190, 550, 120, 20);
		srlBut.setText("SRL labelling");
		srlBut.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		srlBut.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					SRLProcess();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
	});
		
		prlBut = new Button(this, SWT.NONE);
		prlBut.setBounds(360, 550, 120, 20);
		prlBut.setText("Plarigism Test");
		prlBut.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		prlBut.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					try {
						PrlTest();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}		
	});
		
		clrBut = new Button(this, SWT.NONE);
		clrBut.setBounds(520, 550, 120, 20);
		clrBut.setText("Clear");
		clrBut.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		clrBut.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				   inputTxt.setText("");
				   articleTxt.setText("");
				
			}		
	});
		
		closeBut = new Button(this, SWT.NONE);
		closeBut.setBounds(660, 550, 120, 20);
		closeBut.setText("Exit");
		closeBut.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		closeBut.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			
					System.exit(1);;
			
			}		
	});
		
		createContents();
	}

	public void SRLProcess() throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
		 
		 
		srlprocess=new SRLProcessing();
//		srlprocess.synonymList.clear();
		 //For input
		srlphrasesInput=srlprocess.FindPhrases(taggedSentenceListForInput);
		srlphrasesInput=PostSRLProcess(srlphrasesInput);
		 TypeResult(srlphrasesInput,"input");
		 Print(taggedSentenceListForInput);
		 
		//For article
		srlphrasesArticle=srlprocess.FindPhrases(taggedSentenceListForArticle);
		srlphrasesArticle=PostSRLProcess(srlphrasesArticle);
		 TypeResult(srlphrasesArticle,"article");
		 Print(srlphrasesArticle);
		 
		 
	}
	
	public HashMap<Integer,String>  PostProcessPOS(HashMap<Integer,String> sentList){
		String strr="";
		String wholePara="";
		HashMap<Integer,String> returnPost=new HashMap<Integer,String>();
		int count=0;
		
		for(Integer key : sentList.keySet())
		{
						++count;
						strr=sentList.get(key);
						//Split each phrase
						strr=SpaceRemoveInSent(strr);
						String[] phr=strr.split("\\s");
						for(String ph:phr){
								if(ph.equals("\\s") || ph==null){
									strr=SpaceRemoveInSent(strr);
								}
						}
						
						//sentence without space
					//	System.out.println("SENT WITHOUT SPACE<<<<<<<<<<<<<<<<<<<<<<<<<<<"+strr);
						String postProcessSent="";
						phr=strr.split("\\s");
						for(int i=0;i<phr.length;i++){
								String phrase=phr[i];
								if(phrase.contains("နေ") && phrase.contains("[V]")){
										String nextPhr=phr[i+1];
										String prePhr=phr[i-1];
										if(nextPhr.trim().equals("သော"))
										{
											  phrase="နေသော[Ving+Adj] ";
											  ++i;
										}
										else if(nextPhr.trim().equals("သည့်"))
										{
											  phrase="နေသည့်[V+Adj] ";
											  ++i;
										}
										else if(nextPhr.contains("သည်") ||nextPhr.contains("မည်")  || nextPhr.contains("ပေ"))
										{
											   phrase=phrase.replace("[V]", "").trim();
											   phrase=phrase+nextPhr+"{MainVerb}";
											   ++i;
										}
										else if(phrase.contains("မ") && phrase.contains("N+Time")){
											if(prePhr.contains("သူ")){
												phrase=phrase.replace("[N+Time]", "");
												phrase=prePhr+phrase.trim();
											}
											
										}									
										postProcessSent+=phrase+" ";
								}//end if
								
								
								//to continute condition
								else{
									 postProcessSent+=phrase+" ";
								}
						}//for
						
						/*if(postProcessSent.contains("မ[N+Time] နက်တွင်")){
							System.out.println("POST ......................."+postProcessSent);
							postProcessSent=postProcessSent.replace("မ[N+Time] နက်တွင်", "မနက်တွင်[N+Time] ");
						}
						if(postProcessSent.contains("မ[N+Time] နက်")){
							postProcessSent=postProcessSent.replace("မ[N+Time] နက်", "မနက်[N+Time] ");
						}
						if(postProcessSent.contains("သူ[Pronoun] မ")){
							postProcessSent=postProcessSent.replace("သူ[Pronoun] မ", "သူမ[Pronoun] ");
						}*/
						returnPost.put(count, postProcessSent);
		}//outermost for
		
		return returnPost;
	}
	
	public String SpaceRemove(String strr){
		String tempSent="";
		String[] phr=strr.split("\\s");
		for(String ph:phr){
			ph=ph.trim();
			if(!ph.equals("")){
				tempSent+=ph+" ";
			}
		}
		return tempSent.trim();
	}
	public HashMap<Integer,String> PostSRLProcess(HashMap<Integer,String> para){
		String strr="";
		String wholePara="";
		HashMap<Integer,String> returnPost=new HashMap<Integer,String>();
		int count=0;
		
		for(Integer key : para.keySet())
		{
						++count;
						strr=para.get(key);
						strr=SpaceRemoveInSent(strr);
						String tempSent=SpaceRemove(strr);
						
						String postProcessSent="";
						String[] phr=tempSent.split("\\s");
						
						for(int i=0;i<phr.length;i++){
								String phrase=phr[i].trim();							
									 if(phrase.contains("Place")){										
													String nextPhr=phr[i+1];
													if(nextPhr.trim().contains("{Place}"))
													{												
														  phrase=phrase.replace("{Place}", "").trim();
														  phrase=phrase+nextPhr+" ";
														  ++i;
													}
													else if(nextPhr.trim().contains("[Place]"))
													{												
														  phrase=phrase.replace("[Place]", "").trim();
														  phrase=phrase+nextPhr+"{Place} ";
														  ++i;
													}
													else{
														phrase=phrase+"{Place} ";
													}												 
										}									
									  if(phrase.contains("Time") || phrase.contains("N+Time")){//á€”á€¶á€”á€€á€ºá€�á€„á€ºá€¸[Time] á€�á€½á€„á€º[Place] 
										 String nextPhr=phr[i+1];
											if(nextPhr.trim().contains("Place"))
											{												
												nextPhr=nextPhr.replace("[Place]", "");
												phrase=phrase.replace("[Time]","");
												phrase=phrase.replace("[N+Time]","");
												  phrase=phrase+nextPhr+"[Time]{Time}  ";
												  ++i;
											}
											else if(nextPhr.trim().contains("နက်")){								
												phrase=phrase.replace("[N+Time]","");
												phrase=phrase+nextPhr;
												  phrase=phrase+nextPhr+"[Time]{Time}  ";
												  ++i;
											}
									 }
									  //အဖိုးက မှာ က က subject မပါနေတာ
									 if(phrase.contains("N")){
										        String nextphrase="";
												nextphrase=phr[i+1].trim();
												 if(nextphrase.equals("က")){
													  String ppphrase=phrase.substring(0,phrase.indexOf("["));
													  
													  String wordtype=phrase.substring(phrase.indexOf("["),phrase.indexOf("]")+1);
													  phrase=ppphrase+nextphrase+wordtype+"{Subject} ";
													  ++i;
												 }
												/* 
												 if(nextphrase.equals("[N]")){
													 String ppphrase=phrase.substring(0,phrase.indexOf("["));													  
													  String wordtype=phrase.substring(phrase.indexOf("["),phrase.indexOf("]")+1);
													  phrase=ppphrase+nextphrase+wordtype+"{Subject} ";
													  ++i;
												 }*/
									 }
									 if(phrase.contains("Ving+Adj")){
										 String preWord=phr[i-1].trim();
										 String word1=preWord.substring(0,preWord.indexOf("["));
										 String word2=preWord.substring(preWord.indexOf("["),preWord.indexOf("]"));
										 if(preWord.contains("V")){
											 postProcessSent=postProcessSent.replace(preWord, "");
											 phrase=word1+phrase+" ";
											
										 }										 
									 }
									 if(phrase.contains("Pronoun")){
										  String nextphrase="";
											nextphrase=phr[i+1].trim();
											 if(nextphrase.equals("က")){
												  String ppphrase=phrase.substring(0,phrase.indexOf("["));
												  
												  String wordtype=phrase.substring(phrase.indexOf("["),phrase.indexOf("]")+1);
												  phrase=ppphrase+nextphrase+wordtype+"{Subject} ";
												  ++i;
											 }								 
									 }
									 //finding main verb 
									 //check with end sent
									 if(phrase.contains("[V]")){
										 String nextphrase="";
											nextphrase=phr[i+1].trim();
											 if(nextphrase.contains("EndSent")){
												 	phrase+="{MainVerb} ";
												 	++i;
											 }
									 }
									 if(phrase.contains("[Adv]")){
										 String nextphrase="";
											nextphrase=phr[i+1].trim();
											 String preWord=phr[i-1].trim();
											System.out.println("Current="+phrase+"..........next="+nextphrase);
											 if(!nextphrase.contains("{Manner}") && !preWord.contains("{Manner}")){
												 	phrase+="{Manner} ";
												 	//++i;
											 } 
											
									 }
									 postProcessSent+=phrase+" ";							
						}// for i
						
						//if subject contains or not
						if(!postProcessSent.contains("{Subject}")){
							postProcessSent=SpaceRemoveInSent(postProcessSent);
							postProcessSent=SpaceRemove(postProcessSent);
							
							phr=postProcessSent.split("\\s");
							postProcessSent="";
							for(int i=0;i<phr.length;i++){
								String pp=phr[i];
								if(pp.contains("[N]") || pp.contains("[Pronoun]")){
									postProcessSent+=pp+"{Subject} ";
								}
								else
									postProcessSent+=pp;
							}
						}//end of if for subject contains or not
						
						if(postProcessSent.contains("{Subject} သည်") || postProcessSent.contains("{Subject}  က")){
							
							postProcessSent=postProcessSent.replace("{Subject} သည်","သည်{Subject} ");
						}
						if(postProcessSent.contains("{Manner} [Adv]")){
							postProcessSent=postProcessSent.replace("{Manner} [Adv]", "[Adv]{Manner} ");
						}
						if(postProcessSent.contains("[Place]{Place}[N+Time]")){
							postProcessSent=postProcessSent.replace("[Place]{Place}[N+Time]", "[N+Time]{Time} ");
						}
						/*if(postProcessSent.contains("ထဲ[Place]{Place}တွင်[Place]{Place}")){
							postProcessSent=postProcessSent.replace("ထဲ[Place]{Place}တွင်[Place]{Place}","ထဲတွင်[Place]{Place}");
							
						}
						postProcessSent=postProcessSent.replace("ထဲ[Place]{Place}မှာ[Place]{Place}","ထဲမှာ[Place]{Place}");
						postProcessSent=postProcessSent.replace("ထဲ[Place]{Place}  မှာ", "ထဲမှာ[Place]{Place}");*/
					returnPost.put(count, postProcessSent);		 
		}//outer for key
		
		
		
		 return returnPost;	 
	}
	
	public String SpaceRemoveInSent(String setWithSpace){
		String sentWithoutSpace="";
		
		String[] phrases=setWithSpace.split("\\s");
						
		for(String phr:phrases){
			if(!phr.equals("\\s") && phr!=null){
				sentWithoutSpace+=phr+" ";
			}
		}
		sentWithoutSpace=sentWithoutSpace.trim();
		
		return sentWithoutSpace;
	}
	
	public void TypeResult(HashMap<Integer,String> sentList,String txttype){
		String str="";
		for(Integer key : sentList.keySet())
		{
			String temp=sentList.get(key);
			if(temp.contains("{EndSent}{MainVerb}")){
				temp=temp.replace("{EndSent}{MainVerb}","{MainVerb} {EndSent}");
			}
			else if(temp.contains("[Object]")){
				temp=temp.replace("[Object]", "{Object}");
			}
			str+=temp;
			
		}
		if(txttype.equals("input"))
			inputTxt.setText(str);
		else if(txttype.equals("article"))
			articleTxt.setText(str);
		
	}
	public void Print(HashMap<Integer,String> sentList){
		for(Integer key : sentList.keySet())
		{
			System.out.println(key+"..."+sentList.get(key));
		}
	}
	
	public void POSTagger() throws ClassNotFoundException, SQLException{
		String inputPara=inputTxt.getText();
		String articlePara=articleTxt.getText();	
		if((inputPara.isEmpty() || inputPara.equals("")) || (articlePara.isEmpty() || articlePara.equals(""))){
			 
		}
		else{
			
			//For Input
			POSTagger posTag=new POSTagger();
			sentListForInput=posTag.SplitParaIntoSent(inputPara);
			wordListPerSentenceForInput=posTag.SplitWordfromSent(sentListForInput);
			taggedSentenceListForInput=posTag.FindPOS(sentListForInput,wordListPerSentenceForInput);
			taggedSentenceListForInput=PostProcessPOS(taggedSentenceListForInput);
			TypeResult(taggedSentenceListForInput,"input");
			
			sentListForArticle=posTag.SplitParaIntoSent(articlePara);
			wordListPerSentenceForArticle=posTag.SplitWordfromSent(sentListForArticle);
			taggedSentenceListForArticle=posTag.FindPOS(sentListForArticle,wordListPerSentenceForArticle);
			taggedSentenceListForArticle=PostProcessPOS(taggedSentenceListForArticle);
			TypeResult(taggedSentenceListForArticle,"article");
			
		}
		
		
		
	}

	
	public void PrlTest() throws ClassNotFoundException, SQLException, IOException{
	
		String inputParagraph=inputTxt.getText();
		String articleParagraph=articleTxt.getText();
		
		System.out.println("Input="+inputParagraph);
		System.out.println("article="+articleParagraph);
		
		srlprocess=new SRLProcessing();
		
		HashMap<Integer,String> inputPara=srlprocess.SplitParaIntoSent(inputParagraph);
		HashMap<Integer,String> articlePara=srlprocess.SplitParaIntoSent(articleParagraph);
			
	//	srlprocess.CompareStructure(inputPara, articlePara);
		
		CompareSentence compareSet;
		 dir = new File("Similarity");
		if (!dir.exists()) {
			dir.mkdir();
		}
		Property.createInstance(dir.getAbsolutePath());
		String path=dir.getAbsolutePath();
		HashMap<String,Float> structureSimi=new HashMap<String,Float>();
		
		 writer = new FileWriter(path + "SimilarityPhraseTypeAndCore.txt");
		 
		int inputCount=1;
		int articleCount=1;
		
		for(Integer inputSentCount:inputPara.keySet()){//for 1
			String inputSent=inputPara.get(inputSentCount);
			articleCount=1;
			for(Integer articleSentCount:articlePara.keySet())//for 2
			{
					String articleSent=articlePara.get(articleSentCount);
					compareSet=new CompareSentence();			
					try {
						ArrayList<SimilarObject> simiObj=compareSet.CompareStructureofSentence(inputSent, articleSent);
						inputCoreType=compareSet.inputCoreType;
						Float simiValue=PrintingResutlt(inputSent,articleSent,simiObj);		
						structureSimi.put("Input "+inputCount+"&Article "+articleCount, simiValue);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					++articleCount;
			}//for 2
			++inputCount;
		}//for 1
		
		HashMap<Integer,HashMap<String,String>>  phrInput=srlprocess.SRLProcess(srlphrasesInput);		
		HashMap<Integer,HashMap<String,String>>  phrArticle=srlprocess.SRLProcess(srlphrasesArticle);
		
		//for N-gram level
		HashMap<String,Float> ngramSimilarity=srlprocess.NGramWordCompare(phrInput,phrArticle);
		srlprocess.PrintJaccardSimilarityValue(ngramSimilarity);
		
		CalculaeFinalSimiValue(structureSimi,ngramSimilarity);
		
	}
	
	public void CalculaeFinalSimiValue(HashMap<String,Float> stru,HashMap<String,Float> ngram){
		try {
			simiWriter = new FileWriter(dir.getAbsolutePath() + "SimiValues.txt");
			
			for(String str:stru.keySet()){
				float struvalue=stru.get(str);
				float ngramvalue=ngram.get(str);
				simiWriter.write("For "+str+"\n");
				simiWriter.write("\t\tStrcture Value="+struvalue+"*"+structureValue+"(thresholdValue)="+(struvalue*structureValue)+"\n");
				simiWriter.write("\t\tN-Word Value="+ngramvalue+"*"+ngramValue+"(thresholdValue)="+(ngramvalue*ngramValue)+"\n");
				float simivalue=(structureValue*struvalue)+(ngramvalue*ngramValue);
				simiWriter.write("\t\tTotalValue="+simivalue+"\n");
				System.out.println(str+".........simivalue="+simivalue);
			}
			
			simiWriter.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	
	public Float  PrintingResutlt(String input,String article,ArrayList<SimilarObject> simiPhrase) throws IOException{
	
		

		ArrayList<String> phrListOfInput=GettingAllPhraesOfInput(input);
		HashMap<String,String>	foundListOfInput=CheckFoundPhraseFromInputSent(phrListOfInput,simiPhrase);
		HashMap<String,Boolean>	resultEachPhr=new HashMap<String,Boolean>();
				
		
		writer.write("Input Sentence>>"+input+"\n");
		writer.write("ArticleSentence>>"+article+"\n");
		
		for(SimilarObject ss:simiPhrase){
			writer.write("\t\tInput Phrase>>"+ss.foundInputPhrase+"\n");
			writer.write("\t\tArticle Phrase>>"+ss.foundArtcilePhrase+"\n");
			writer.write("\t\tCore Type>>"+ss.getCoreType()+"\n");
			writer.write("\t\tSimilar Type>>"+ss.getSimilarType()+"\n");
			writer.write("\t\tPhraseT Type>>"+ss.getPhraseType()+"\n");
			writer.write("\t\tMatch Result>>"+ss.found+"\n");
			writer.write("..............########......................\n");
			resultEachPhr.put(ss.phraseType, ss.found);
		}
				
		for(String str:foundListOfInput.keySet()){
			writer.write(str+">>"+foundListOfInput.get(str)+"\n");
			System.out.println(str+"........Found......"+foundListOfInput.get(str));
		}
		
		 HashMap<String,Boolean> allFoundPhrResult=FindResult(foundListOfInput,resultEachPhr);
		
		writer.write("\nSimilarity Score..................\n");
		 HashMap<String,Float> phrValue=CalculateSimilarValue(allFoundPhrResult,simiPhrase);
		 for(String str:phrValue.keySet()){
			 writer.write("\t\tPhrase Name>>"+str+"....Match Result="+allFoundPhrResult.get(str)+">>"+phrValue.get(str)+"\n");
			 System.out.println("\t\t"+str+"........"+allFoundPhrResult.get(str)+">>"+phrValue.get(str)+"\n");
		 }
		 writer.write("\n.................................................................\n\n");
		 writer.flush();

		 //calcuate structure value
		 float totalvalue=0.0f;
		 for(String str:phrValue.keySet()){
			 totalvalue=totalvalue+phrValue.get(str);
		 }
		 totalvalue=totalvalue/(float)phrValue.size();
		 
		 return totalvalue;
		 
	}//end void
	
	public HashMap<String,String> CheckFoundPhraseFromInputSent(ArrayList<String> phrListOfInput,ArrayList<SimilarObject> simiPhrase){
		HashMap<String,String> foundListOfInput=new HashMap<String,String>();
		Boolean ff=false;
		for(String str:phrListOfInput){
			ff=false;
			for(SimilarObject ss:simiPhrase){
				if(ss.getPhraseType().equals(str)){
					foundListOfInput.put(str, "Found");
					ff=true;
				}
			}
			if(ff==false)
				foundListOfInput.put(str, "Not Found");
		}
		
		return foundListOfInput;
	}
	public ArrayList<String> GettingAllPhraesOfInput(String input){
		ArrayList<String> containedPhrlist=new ArrayList<String>();
			String[] phrArr;
			
			System.out.println(input);
			String temp=input;
			while(temp.contains("{")){
				String pp=temp.substring(temp.indexOf("{"),temp.indexOf("}")+1);
				temp=temp.replace(pp, "");
				containedPhrlist.add(pp);
				
			}		
			return containedPhrlist;	
		}
	
	public HashMap<String,Float> CalculateSimilarValue(HashMap<String,Boolean> phrList,ArrayList<SimilarObject> simiPhr){
		int allPhraseCount=phrList.size();
		float[] phrSimiValue=new float[20];
		HashMap<String,Float> phrSimivalue=new HashMap<String,Float>();
		int counter=0;
		
System.out.println("Phr listsize="+phrList.size()+".......simiPhr size="+simiPhr.size());
		for(String phrName:phrList.keySet()){
			System.out.println("Phr Name="+phrName+"..........55555555555...."+phrList.get(phrName));
			if(phrList.get(phrName)==true){
					for(SimilarObject ss:simiPhr){
						
							String type=inputCoreType.get(ss.phraseType);
							System.out.println(">>>>>>TYPE="+type);
							System.out.println("Type="+type+"......."+ss.getPhraseType()+"...phrName="+phrName);
							if(type!=null){
								
											if(type.equals("Core") && ss.getPhraseType().equals(phrName) && ss.found==true){
												
												String similartype=ss.getSimilarType();
												System.out.println("SImilar type="+similartype+".......phrName="+phrName);
												System.out.println(similartype);
												if(similartype.equals("Subset Synonym Similar"))
													phrSimiValue[+counter]=SubsetSynonymSimilarValue;
												else if(similartype.equals("Exact Similar"))
													phrSimiValue[+counter]=ExactSimilarValue;
												else if(similartype.equals("Subset Similar"))
													phrSimiValue[+counter]=SubsetSimilarValue;
												else if(similartype.equals("No Similar")){
													if(type.equals("NCore"))
														phrSimiValue[+counter]=1.0f;
													else
														phrSimiValue[+counter]=0.0f;
													
												}
													
												else if(similartype.equals("Synonym Similar"))
													phrSimiValue[++counter]=SynonymSimilarValue;
												
												System.out.println(ss.phraseType+"##########"+phrSimiValue[counter]);
												phrSimivalue.put(ss.phraseType,phrSimiValue[counter]);
										}
										else if(type.equals("NCore")){
											
													phrSimiValue[+counter]=1.0f;
													phrSimivalue.put("No Similar",1.0f);
											
										}
							}
							else
								phrSimivalue.put("No Similar",0.0f);
							
							
						
					}//for simiPhr
							
							
			}//if ==true
			else{
				phrSimiValue[++counter]=0.0f;
				phrSimivalue.put(phrName,phrSimiValue[counter]);
			}
				
		}
		
		return phrSimivalue;
	}
	public HashMap<String,Boolean> FindResult(HashMap<String,String> phrList,HashMap<String,Boolean> resultPair){
		HashMap<String,Boolean> phrResult=new HashMap<String,Boolean>();
		boolean bb=false;
		for(String phrName:phrList.keySet()){
			bb=false;
			if(resultPair.get(phrName)!=null)
			{
				phrResult.put(phrName, resultPair.get(phrName));
			}
			else
				phrResult.put(phrName, false);
			
		}
		
		return phrResult;
	}
	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Plarigism Detection for Myanmar Text Using Framenet and Lexical Unit");
		setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		 setSize(850, 652);
		setBackgroundMode(1);
		//setMaximized(true);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
