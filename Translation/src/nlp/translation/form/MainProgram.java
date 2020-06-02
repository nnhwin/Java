package nlp.translation.form;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.*;

import com.chaoticity.dependensee.Main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import eng.chin.translate.lexicon.FinalSentenceTranslation;
import eng.chin.translate.lexicon.GetLexiWord;
import eng.chin.translate.lexicon.GetPhrases;
import eng.chin.translate.lexicon.ParsingPhraseProgram;
import eng.chin.translate.lexicon.PhraseTranslate;
import eng.chin.translate.lexicon.ReplaceWord1;
import eng.chin.translate.lexicon.TransformChin1;
import eng.chin.translate.lexicon.WholeSentenceTranslation;
import eng.chin.translate.lexicon.myChinTranslate;

public class MainProgram extends JFrame implements ActionListener{
	
	public JTextArea inputTxt,articleTxt;
	JButton engPoSParse,chinPoSParse,engPhraseParse,chinPhraseParse,engSentChuck,chinSentChuck,chinRuleBut,clearBut,loadBut;
	JButton chinWholeTranslateBut,chinPostProcessBut;
	String phraseText="";
	JTextField loadfield;
	String[] type=null,words=null,chinWords=null;
	HashMap<Integer,String> engNum,chinNum,TypeNum;
	HashMap<String,String> engChinPair=new HashMap<String,String>();
	HashMap<String,String> engTypePair=new HashMap<String,String>();
	ArrayList<String> engSentPOS=new ArrayList<String>();
	
	HashMap<Integer,String[]> engPos=new HashMap<Integer,String[]>();
	HashMap<Integer,String[]> engWord=new HashMap<Integer,String[]>();
	HashMap<Integer,String[]> chinPos=new HashMap<Integer,String[]>();
	HashMap<Integer,String[]> chinWord=new HashMap<Integer,String[]>();
	HashMap<Integer,String> sentPhraseSentEng=new HashMap<Integer,String>();
	HashMap<Integer,String> sentPhraseSentChin=new HashMap<Integer,String>();
	
	HashMap<Integer,String> finalSentEng=new HashMap<Integer,String>();
	HashMap<Integer,String> finalSentChin=new HashMap<Integer,String>();
	HashMap<Integer,String> translated_chin=new HashMap<Integer,String>();
	HashMap<Integer,String> whole_translated_chin=new HashMap<Integer,String>();
	HashMap<Integer,String> final_translated_chin=new HashMap<Integer,String>();
	HashMap<String,String> myEngChinPair=new HashMap<String,String>();
	String[] eng1,pos,chin;
	
	String filename="";
	public MainProgram() {
		Container con=getContentPane();
		JPanel mainPanel=new JPanel();
		//mainPanel.setLayout(new GridLayout(4,1,20,20));
		mainPanel.setLayout(new BorderLayout());
		JPanel loadPanel=new JPanel();
		loadPanel.setLayout(new FlowLayout());
		JPanel inputPanel=new JPanel();
		JPanel translatePanel=new JPanel();
		JPanel buttonPanel=new JPanel();
		JPanel emptyPanel=new JPanel();
		
		loadBut=new JButton("Load English File....");
		loadfield=new JTextField(30);
		loadPanel.add(loadBut,BorderLayout.WEST);
		loadPanel.add(loadfield,BorderLayout.CENTER);
		
		JLabel inputLbl=new JLabel("Input English Language...................");
		inputTxt=new JTextArea();
		inputTxt.setFont(new Font("Times New Roman",Font.PLAIN,24));
		JScrollPane inputSP = new JScrollPane(inputTxt);
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(inputLbl,BorderLayout.BEFORE_FIRST_LINE);
		inputPanel.add(inputSP,BorderLayout.CENTER);
		
		JLabel articlelbl=new JLabel("Translated Chin (Tedim) Language.............");
		articleTxt = new JTextArea();
		articleTxt.setFont(new Font("Times New Roman",Font.PLAIN,24));
		JScrollPane articleSP = new JScrollPane(articleTxt);
		translatePanel.setLayout(new BorderLayout());
		translatePanel.add(articlelbl,BorderLayout.BEFORE_FIRST_LINE);
		translatePanel.add(articleSP,BorderLayout.CENTER);
		
		JPanel twobuttonPanel=new JPanel();
		twobuttonPanel.setLayout(new GridLayout(2,4));
		
		loadBut.addActionListener(this);
		JPanel butP=new JPanel();
		//butP.setLayout(new GridLayout(1,1,10,10));
		//buttonPanel.add(emptyPanel.add(new JLabel("."))); 
		engPoSParse = new JButton("E_POS Parse");
		engPoSParse.addActionListener(this);
		
		chinPoSParse=new JButton("C_POS Parse");
		chinPoSParse.addActionListener(this);
		
		engPhraseParse = new JButton("E_Phrase Group");
		engPhraseParse.addActionListener(this);
		
		chinPhraseParse = new JButton("C_Phrase Group");
		chinPhraseParse.addActionListener(this);
		
		engSentChuck = new JButton("E_Sentence Chunck");
		engSentChuck.addActionListener(this);
		
		chinWholeTranslateBut=new JButton("C_ Sent_Translate");
		chinWholeTranslateBut.addActionListener(this);
		
		chinPostProcessBut=new JButton("Post-Process C_Translate");
		chinPostProcessBut.addActionListener(this);
		
		
		inputTxt.setLineWrap(true);
		articleTxt.setLineWrap(true);
		
		chinSentChuck = new JButton("Chin Sentence Chunck");
		chinSentChuck.addActionListener(this);
			/*	new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//CheckDepedencyGraph(inputTxt.getText());
		}		
		});
*/
		chinRuleBut = new JButton("Chin-Phrase Translation");
		chinRuleBut.addActionListener(this);
	
		clearBut = new JButton("Clear");
		//clearBut.setBounds(520, 550, 120, 20);
		clearBut.setText("Clear");
		clearBut.addActionListener(this);
		
		chinPoSParse.setEnabled(false);
		chinPhraseParse.setEnabled(false);
		chinSentChuck.setEnabled(false);
		chinRuleBut.setEnabled(false);
		chinWholeTranslateBut.setEnabled(false);
		chinPostProcessBut.setEnabled(false);
		
		
		butP.add(chinPoSParse);
		butP.add(chinPhraseParse);
		butP.add(chinSentChuck);		
		butP.add(chinRuleBut);
		butP.add(chinWholeTranslateBut);
		butP.add(chinPostProcessBut);
		
		buttonPanel.add(engPoSParse);
		buttonPanel.add(engPhraseParse);
		buttonPanel.add(engSentChuck);		
		buttonPanel.add(clearBut);
		engPhraseParse.setEnabled(false);
		engSentChuck.setEnabled(false);
		
		twobuttonPanel.add(buttonPanel);
		twobuttonPanel.add(butP);
		
			
		con.add(mainPanel);
		mainPanel.add(loadPanel,BorderLayout.NORTH);
		JPanel mm=new JPanel();
		mm.setLayout(new GridLayout(1,2,20,20));
		mm.add(inputPanel);
		mm.add(translatePanel);
		mainPanel.add(mm,BorderLayout.CENTER);
	//	mainPanel.add(translatePanel);
		mainPanel.add(twobuttonPanel,BorderLayout.AFTER_LAST_LINE);
		
		//mainPanel.add(emptyPanel.add(new JLabel(".")));
		
		setBackground(Color.gray);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Language Translation from English To Chin Lanugage");
		setPreferredSize(new Dimension(1000, 600));
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
		
	}	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==loadBut){
			 JFileChooser fc=new JFileChooser();
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            System.out.println(file.getAbsolutePath());
	            loadfield.setText(file.getAbsolutePath());
	            //This is where a real application would open the file.
	           System.out.println("Opening: " + file.getName() + "." );
	          
	        	Scanner sc;
				try {
					sc = new Scanner(file.getAbsoluteFile());
					String str="";
        		    while (sc.hasNextLine()) 
        		      str+=sc.nextLine(); 
        		    str=str.trim();
        		    inputTxt.setText(str);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
	        		  
	        } else {
	        	System.out.println("Open command cancelled by user." );
	        }
		}
		
		else if(e.getSource()==clearBut){
				inputTxt.setText("");;
				articleTxt.setText("");
				chinPoSParse.setEnabled(false);
				chinPhraseParse.setEnabled(false);
				chinSentChuck.setEnabled(false);
				chinRuleBut.setEnabled(false);
				chinWholeTranslateBut.setEnabled(false);
				chinPostProcessBut.setEnabled(false);
				engPhraseParse.setEnabled(false);
				engSentChuck.setEnabled(false);
				
				
				//Clear all data
				 engPos.clear();
				 chinPos.clear();
				 chinWord.clear();
				 sentPhraseSentEng.clear();
				 sentPhraseSentChin.clear();
				 finalSentEng.clear();
				 finalSentChin.clear();
				 translated_chin.clear();
				 whole_translated_chin.clear();
				 final_translated_chin.clear();
		}
		else if(e.getSource()==engPoSParse){
			ArrayList<String> sent=EngPOS();
			articleTxt.setText("");
			for(int i=0;i<sent.size();i++){
				articleTxt.append("Sent --> "+(i+1)+"  "+sent.get(i)+"\n");
			}
			engSentPOS=sent;
			chinPoSParse.setEnabled(true);
			
		}
		else if(e.getSource()==chinPoSParse){
			String str=ChinPOS(engSentPOS);
			articleTxt.setText(str);
			engPhraseParse.setEnabled(true);
		}
		else if(e.getSource()==engPhraseParse){
			String engPhrase=EngPhrase();
			
			String output="";
			String[] sent=engPhrase.split("@");
			for(int j=0;j<sent.length;j++)
			{
				String stt=sent[j].trim();
				if(!stt.equals("") && stt!=""){
					String[] pos=engPos.get(j);
					String[] eng1=engWord.get(j);
					String transform_sent=TransformEngPhrase(stt,pos,eng1);
					//System.out.println("Sent !!-"+(j)+"--> "+transform_sent);
					output+="Sent -"+(j)+"--> "+transform_sent+"\n";
					sentPhraseSentEng.put(j, transform_sent);
				}
			}		
			articleTxt.setText("");
			articleTxt.setText(output);
			output="";
			chinPhraseParse.setEnabled(true);
		}
		else if(e.getSource()==chinPhraseParse){
			String chinPhrase=EngPhrase();
			//System.out.println("IN CHIN PHRASE "+chinPhrase);
			String output="";
			String[] sent=chinPhrase.split("@");
			for(int j=0;j<sent.length;j++)
			{
				String stt=sent[j].trim();
				if(!stt.equals("") && stt!=""){
					String[] pos=engPos.get(j);
					eng1=chinWord.get(j);
					String transform_sent=TransformEngPhrase(stt,pos,eng1);
					//System.out.println("Sent -"+(j)+"--> "+transform_sent);
					output+="Sent -"+(j)+"--> "+transform_sent+"\n";
					sentPhraseSentChin.put(j, transform_sent);
				}
			}		
			
			articleTxt.setText("");
			articleTxt.setText(output);
			output="";
			engSentChuck.setEnabled(true);
		}
		else if(e.getSource()==engSentChuck){
			//sentPhraseSentEng
			 Iterator iterator = sentPhraseSentEng.entrySet().iterator();
			 String output="";
		     while (iterator.hasNext()) {
		         Map.Entry me2 = (Map.Entry) iterator.next();
		         System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
		         Integer inum=(Integer) me2.getKey();
		         String str=(String) me2.getValue();
		         PhraseTranslate pr=new PhraseTranslate();
		         String r_sent=pr.DoProcess(str);
		         finalSentEng.put(inum, r_sent);
		         output+="Sent-"+inum+" --> "+r_sent+"\n";
		     } 
			articleTxt.setText(output);
			chinSentChuck.setEnabled(true);
		}
		else if(e.getSource()==chinSentChuck){
			ChangeToChin();
			chinRuleBut.setEnabled(true);
		}
		else if(e.getSource()==chinRuleBut){
			//To change places
			String chinrulesent=ChinTranslate();
			articleTxt.setText(chinrulesent);
			chinWholeTranslateBut.setEnabled(true);
		}
		else if(e.getSource()==chinWholeTranslateBut){
			
			String str=WholeSentRephrase();
			articleTxt.setText(str);
			chinPostProcessBut.setEnabled(true);
		}
		else if(e.getSource()==chinPostProcessBut){
			String str=FinalChinTranslation();
			articleTxt.setText(str);
			
		}
	}
	
	String FinalChinTranslation(){
		String sent="";
		System.out.println("Final Chin Sentence Translation................");
		for (Map.Entry<Integer,String> entry : whole_translated_chin.entrySet()) {
			String w_sent=entry.getValue();
			System.out.println("###..."+w_sent);
			FinalSentenceTranslation wst=new FinalSentenceTranslation();
			String pr=wst.DoPerform(w_sent);
			Integer sentNum=entry.getKey();
			sent+="Sent "+sentNum+"-->"+pr+"\n";
			final_translated_chin.put(sentNum,pr);
		}	
		return sent;
	}
	
	
	String WholeSentRephrase(){
		String sent="";
		for (Map.Entry<Integer,String> entry : translated_chin.entrySet()) {
			String w_sent=entry.getValue();
			WholeSentenceTranslation wst=new WholeSentenceTranslation();
			String pr=wst.DoTranslate1(w_sent);
			Integer sentNum=entry.getKey();
			sent+="Sent "+sentNum+"-->"+pr+"\n";
			whole_translated_chin.put(sentNum,pr);
		}	
		return sent;
	}
	
	String ChinTranslate(){
		//finalSentChin
		System.out.println("\n\nChin Translate....................");
		Iterator iterator = finalSentChin.entrySet().iterator();
		//TranslationToChin tchin=new TranslationToChin();
		myChinTranslate mytran=new myChinTranslate();
		String output="";
		
		while (iterator.hasNext()) {
	         Map.Entry me2 = (Map.Entry) iterator.next();
	         //System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
	         String sent=(String)me2.getValue();
	         String trans_chin=mytran.DoTranslate1(sent);
	         System.out.println("Sent-"+me2.getKey()+"-->"+trans_chin);
	         output+="Sent-"+me2.getKey()+"-->"+trans_chin+"\n";
	         int pp=(Integer) me2.getKey();
	         translated_chin.put(pp, trans_chin);
		}
		
		return output;
	}
	
	void ChangeToChin(){
		//finalSentEng
		Iterator iterator = finalSentEng.entrySet().iterator();
		 String output="";
	     while (iterator.hasNext()) {
	         Map.Entry me2 = (Map.Entry) iterator.next();
	         //System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
	         String sentence=(String) me2.getValue();
	         int j=(Integer)me2.getKey();
	         pos=engPos.get(j);
			 chin=chinWord.get(j);
	         String r_sent=TransformChin(sentence,pos,chin);
	         finalSentChin.put(j, r_sent);
	         System.out.println("Sent-"+j+"--> "+r_sent+"\n");
	         output+="Sent-"+j+"--> "+r_sent+"\n";
	     }
	     articleTxt.setText(output);
	}
	
	String TransformChin(String sent,String[] pos,String[] eng){
		String str="";
		TransformChin1 tc=new TransformChin1();
		str=tc.DoTransformChin(sent,pos,eng);	
		return str;
	}
	
	String TransformEngPhrase(String sent,String[] pos,String[] eng){
		ReplaceWord1 rw=new ReplaceWord1();
		String transform_sent=rw.DoPerform(sent,pos,eng);
		return transform_sent;
	}
	
	String ChinPOS(ArrayList<String> engPOS){
		//FindLexiconForChinWords()
		
		String[] engPoSArr;
		String[] engWordArr;
		String returnStr = "";
		for(int i=0;i<engPOS.size();i++){
			String sent=engPOS.get(i).trim();
			String[] str=sent.split(",");
			engPoSArr=new String[str.length];
			engWordArr=new String[str.length]; 
			
			for(int j=0;j<str.length;j++){
				String chunk=str[j].trim();
				chunk=chunk.replace("[", "");
				chunk=chunk.replace("]", "").trim();
				String[] chunkArr=chunk.split("/");
				String word=chunkArr[0];
				String pos=chunkArr[1];
				engPoSArr[j]=pos;
				engWordArr[j]=word;
				//System.out.println("ENG......"+pos+"..........wrod="+word);
			}
			String[] chinWordArr=FindLexiconForChinWords(engWordArr);
			engPos.put(i+1,engPoSArr);
			engWord.put(i+1,engWordArr);
			chinPos.put(i+1,engPoSArr);
			chinWord.put(i+1,chinWordArr);
			
		}
		
		//Print
		/*String pp=PrintHashMap(chinWord);
		System.out.println(pp);*/
		
		String str="";
		String start="[";
		String end="]";
		returnStr="";
		for (Map.Entry<Integer, String[]> ee : engPos.entrySet()) {
			
			String[] words=ee.getValue();
			for (Map.Entry<Integer, String[]> chin : chinWord.entrySet()) {
				if(chin.getKey()==ee.getKey()){
					returnStr+="Sent "+ee.getKey()+"--> "+start;
					String[] cwords=chin.getValue();
					for(int kk=0;kk<cwords.length;kk++){
						
						returnStr+=cwords[kk]+"/"+words[kk]+",";
					}
					break;
				}
			}//chin map
			returnStr=returnStr.substring(0,returnStr.length()-1)+end+"\n";			
		}
		return returnStr;
	}
	
	String PrintHashMap(HashMap<Integer,String[]> chinWord){
		String str="\n";
		
		for (Map.Entry<Integer, String[]> ee : chinWord.entrySet()) {
			String[] ss=ee.getValue();
			
		    System.out.println(ee.getKey());
		    str+=ee.getKey()+"--------->";
		    for(int i=0;i<ss.length;i++)
		    {
		    	str+=ss[i]+"\t";
		    }
		    str+="\n";	
		}
		
		
		return str;
	}
	
	ArrayList<String> EngPOS(){
		String input=inputTxt.getText().trim();
		engSentPOS.clear();
		if(input==null || inputTxt.equals(""))
			JOptionPane.showMessageDialog(null, "Enter input English text to translate");
		else
		{
			String sentences[] = input.split("(?<![DSJ]r|Mrs?)[.?!](?!\\S)");
			
			for(int i=0;i<sentences.length;i++){
				String sent=sentences[i].trim();
				String str=StandfordParse(sent);				
				engSentPOS.add(str);
			}	
			
		}
		return engSentPOS;
	}
	
	String EngPhrase(){
		String PhraseText="";
		String input=inputTxt.getText().trim();
		if(input==null || inputTxt.equals(""))
			JOptionPane.showMessageDialog(null, "Enter input English text to translate");
		else{
			String str=ParseEngPhrases();
			PhraseText=str;
		}		
		return PhraseText;
	}
	
	String ParseEngPhrases(){
		String str="";
		//System.out.println("Size "+chinWord.size());
		for (int i=0;i<chinWord.size();i++) {
		//	System.out.println("SENTENCE>>>>>>>>>>>>>>>>>>>>>"+(i+1)+"........."+chinWord.get(i+1));
			String[] chin=chinWord.get(i+1);
			String[] eng=engWord.get(i+1);
		    String[] pos=engPos.get(i+1);
		    /*System.out.println(chin.length);
		    
		    for(int j=0;j<chin.length;j++){
		    	System.out.println(chin[j]+"...."+eng[j]+"..."+pos[j]);
		    }*/
		    ParsingPhraseProgram engPhrase=new ParsingPhraseProgram(eng,chin,pos);		    
		    if(engPhrase.getProcessedPhr()!=null && !engPhrase.getProcessedPhr().equals(""))
		    	str+="@"+engPhrase.getProcessedPhr();
		}
		return str;
	}
	
	
	
	
	public void GetVerbPhrase(String str,HashMap<String,String> vv){
		GetPhrases gg=new GetPhrases(str,vv);	
	}
	
	void HashMapPrint(HashMap<Integer,String>  data){
		for (Map.Entry<Integer,String> num : data.entrySet())  {
			System.out.println(num.getKey()+"............"+num.getValue());
		}	
	}
	
	HashMap<Integer,String> GetNumArr(String[] ewords)
	{
		HashMap<Integer,String> cwords=new HashMap<Integer,String>();
		for(int i=0;i<ewords.length;i++){
			cwords.put(i, ewords[i]);
		}
		
		return cwords;
	}
	
		
	
	public String[] FindLexiconForChinWords(String[] engWord){
		
		GetLexiWord lexiObj=new GetLexiWord();
		String[] chinWords=new String[engWord.length];
		try {
			chinWords=lexiObj.FindLexicalWordArray(engWord);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chinWords;
	}
	
	public HashMap<String[],String[]> GetWordPOSPair(String str)
	{
		
		System.out.println(str);
		str=str.replace("[", "");
		str=str.replace("]", "]");
		HashMap<String[],String[]> value=new HashMap<String[],String[]>();
		
		
		String[] words=str.split(",");
		String[] eWord=new String[words.length];
		String[] posType=new String[words.length];
		
		for(int i=0;i<words.length;i++){
			String temp=words[i].trim();
			String[] pair=temp.split("/");
			eWord[i]=pair[0];
			posType[i]=pair[1];
		}
		value.put(eWord, posType);
		return value;
		
	}
	public void CheckDepedencyGraph(String inputText){
		JFrame ff=new JFrame("Dependency Graph.............");
		Container con=ff.getContentPane();
		JPanel imagePanel=new JPanel();
		JPanel butPanel=new JPanel();
		imagePanel.setLayout(new GridLayout(1,1));
		JLabel imageCanvas=new JLabel();
		
		GettingGraph(inputText);	
		imageCanvas.setIcon(new ImageIcon("image.png"));
		imagePanel.add(imageCanvas);
		
		JButton closeBut=new JButton("Close");
		butPanel.add(closeBut);
		closeBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		con.setLayout(new BorderLayout());
		con.add(imagePanel,BorderLayout.CENTER);
		con.add(butPanel,BorderLayout.AFTER_LAST_LINE);
		ff.setSize(800,500);
		ff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ff.setVisible(true);
		
	}
	
	public void GettingGraph(String inputText){
		String text = inputText;
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        LexicalizedParser lp = LexicalizedParser.loadModel();
        lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        List<CoreLabel> wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
        Tree tree = lp.apply(wordList);    
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        @SuppressWarnings("deprecation")
       
		Collection<TypedDependency> tdl = gs.allTypedDependencies();
        try {
			Main.writeImage(tree,tdl, "image.png",3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Done.");
	}
	
	public String StandfordParse(String inputText){
		
		 	String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		    LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		    TreebankLanguagePack tlp = lp.getOp().langpack();
		    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		    String outputString="";
		    
		    Iterable<List<? extends HasWord>> sentences;
		   	String mystr=inputText;
		   	String[] sent=mystr.split(" ");
		    List<HasWord> sentence = new ArrayList<HasWord>();
		    for (String word : sent) {
		       sentence.add(new Word(word));
		    }

		      List<List<? extends HasWord>> tmp =
		        new ArrayList<List<? extends HasWord>>();
		      tmp.add(sentence);
		      sentences = tmp;

		    for (List<? extends HasWord> sentence1 : sentences) {
		      Tree parse1 = lp.parse(sentence1);
		      //parse.pennPrint();
		      
		      //my change
		      System.out.println("Tree word............");
		      
		      String my_dependencytre=parse1.pennString();
		      
		      Tree[] td=parse1.children();
		      for(int j=0;j<td.length;j++){
		    	  phraseText+=td[j]+"\n";
		    	  System.out.println(td[j]);
		    	  System.out.println("..............");
		      }
		      
		      System.out.println(my_dependencytre);
		      
		      System.out.println("...........");
		      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse1);
		      List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		      System.out.println(tdl);
		      System.out.println();

		      System.out.println("The words of the sentence:");
		      for (Label lab : parse1.yield()) {
		        if (lab instanceof CoreLabel) {
		          //System.out.println(((CoreLabel) lab).toString(CoreLabel.OutputFormat.VALUE_MAP));
		        } else {
		          System.out.println(lab);
		        }
		      }
		      System.out.println();
		      System.out.println(parse1.taggedYield());
		      outputString+=parse1.taggedYield().toString();
		      System.out.println();

		    }

		 return outputString;  
	}
	public static void main(String args[]) {
		new MainProgram();
	}

	
}
