import java.util.ArrayList;
import java.util.HashMap;

import nlp.property.simi.object.SimiObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class PlarigismInfoForm {
	static Display display = Display.getDefault();
	SimiObject simiobj;
	ArrayList<String> synoList=new ArrayList<String>();
	ArrayList<String> subsetList=new ArrayList<String>();
	
	public PlarigismInfoForm(Shell p,SimiObject simiobj,ArrayList<String> synoArr,ArrayList<String>  subsetArr){
		this.simiobj=simiobj;
		this.synoList=synoArr;
		this.subsetList=subsetArr;
		
		final Shell shell = new Shell(p, SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shell.setSize(700, 600);
		shell.setText("Myanmar Pronoun Resolution");
		shell.setImage(SWTResourceManager.getImage("title.png"));
		shell.open();
		//shell.setForeground(new Image(display,"123.jpg"));
	//	shell.setBackgroundImage(SWTResourceManager.getImage("D:\\eclipse\\eclipse\\123.jpg"));
		shell.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		
		final Text resultTxt = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		resultTxt.setFont(SWTResourceManager.getFont("Myanmar3", 12,
				SWT.NORMAL));
		resultTxt.setBounds(10, 50, 700, 400);
		resultTxt.setEditable(false);
		
		resultTxt.setText(GetInfo());
		
		Button btnResolve = new Button(shell, SWT.COLOR_CYAN);
		btnResolve.addSelectionListener(new SelectionAdapter() {
		//	private ConvertToArray cta;
			//private Tagproposition tagpro;
			ArrayList<String> sentencelist = new ArrayList<String>();	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				display.dispose();
				}			
		});
		
		btnResolve.setText("Close");
		btnResolve.setBounds(20, 500, 180, 40);
		btnResolve.setGrayed(true);
		btnResolve.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		btnResolve.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		
		Button btnSynonym = new Button(shell, SWT.COLOR_CYAN);
		btnSynonym.addSelectionListener(new SelectionAdapter() {
		//	private ConvertToArray cta;
			//private Tagproposition tagpro;
			ArrayList<String> sentencelist = new ArrayList<String>();	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String outputStr="";
				if(synoList.size()==0){
					outputStr="There is no synonymn between two paragraphs.\n";
				}else{
					outputStr="Synonyms are found from paragraphs...............\n";
					for(String str:synoList){
						outputStr+=str+"\n";
					}
				}
			
				resultTxt.setText(outputStr);
				}			
		});
		
		btnSynonym.setText("Synonym Detection");
		btnSynonym.setBounds(200, 500, 180, 40);
		btnSynonym.setGrayed(true);
		btnSynonym.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		btnSynonym.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		
		Button btnSubset = new Button(shell, SWT.COLOR_CYAN);
		btnSubset.addSelectionListener(new SelectionAdapter() {
		//	private ConvertToArray cta;
			//private Tagproposition tagpro;
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String outputStr="";
				if(subsetList.size()==0){
					outputStr="There is no subset similar  between two paragraphs.\n";
				}else{
					outputStr="Subset Similarity  is found from paragraphs...............\n";
					for(String str:subsetList){
						outputStr+=str+"\n";
					}
				}
			
				resultTxt.setText(outputStr);
				}			
		});
		
		btnSubset.setText("Subset Similarity");
		btnSubset.setBounds(420, 500, 180, 40);
		btnSubset.setGrayed(true);
		btnSubset.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		btnSubset.setFont(SWTResourceManager.getFont("Times New Roman", 12,
				SWT.NORMAL));
		
	}
	
	String GetInfo(){
		String info="";
		HashMap<String,Float> structureSimilarity=simiobj.getStructureSimilarity();
		HashMap<String,Float> ngramSimilarity=simiobj.getNgramSimilarity();
		HashMap<String,Float> totalFinalScore=simiobj.getTotalFinalScore();
		HashMap<String,Float> plarigismValue=simiobj.getPlarigismValue();
		
		info="Similarity value by structure..............................\n";
		info="Similarity value by structure..............................\n";
		for(String str:structureSimilarity.keySet()){
			info+=str+"\nSimi Value for Structure Comparison= "+structureSimilarity.get(str)+"\n\n";
		}
		
		
		info+="\nSimilarity value by N word..............................\n";
		for(String str:ngramSimilarity.keySet()){
			info+=str+"\nSimi Value for N-word Comparison= "+ngramSimilarity.get(str)+"\n\n";
		}
		
		info+="\nPlarigism Value..............................\n";
		for(String str:plarigismValue.keySet()){
			info+=str+"\nSimi Value= "+plarigismValue.get(str)+"%\n";
		}
		
		return info;
	}
}
