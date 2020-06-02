package eng.chin.translate.lexicon;

public class PhraseTranslate {
	boolean active=true;
	public PhraseTranslate(){
		
	}
	public String DoProcess(String str){
		String pp=str;
		String wholeSent="";
		String[] phr=pp.split("#");
		for(int i=0;i<phr.length;i++){
			String temp=phr[i];
			
			if(!temp.equals("") && temp!=null){
				String nameonly=temp.substring(0,temp.indexOf("["));
				if(i<2)
				if(i==1 && phr[i+1].contains("VB_P")){
					temp=temp.replace(nameonly, "Object");
				}
				if(i==1 && phr[i+1].contains("VB_A")){
					temp=temp.replace(nameonly, "Subject");
				}
				
				else 
				{
					if(temp.contains("VB_P")){
						active=false;
					}
					else{
						if(CheckTime(temp)==true){
							temp=temp.replace(nameonly, "NP_Time");
						}
						else if(nameonly.contains("Place")){
							if(temp.contains("by") && active==false)
							{
								temp=temp.replace(nameonly, "Subject");
							}
							else if(temp.contains("with") && active==true)
							{
								temp=temp.replace(nameonly, "NP");
							}
							else if(CheckPlace(temp)==true)
								temp=temp.replace(nameonly, "Place");
						}
						else if(temp.contains("RB") && !temp.contains("o'clock"))
							temp=temp.replace(nameonly, "Manner");
						else{
							  if(i>1){
								if(nameonly.equals("NP") && phr[i-1].contains("VB")){
									if(active==true)
										temp=temp.replace(nameonly, "Object");
									
								}
							}
						}
					}
					
				}
				wholeSent+="#"+temp;
			}
			
			
		
		}
		System.out.println("Whole Sent......"+wholeSent);
		return wholeSent;
	}
	
	boolean CheckTime(String temp){
		boolean rvalue=false;
		String[] tmp={"today","tomorrow","yesterday","evening","when","time","morning","evening","night","tonight"
				,"day","hour","minute","second"};
		for(int i=0;i<tmp.length;i++)
		{
			if(temp.contains(tmp[i]))
				rvalue=true;
		}
		
		return rvalue;
	}
	boolean CheckPlace(String temp){
		boolean rvalue=false;
		String[] tmp={"to","where","from","here","there"};
		for(int i=0;i<tmp.length;i++)
		{
			if(temp.contains(tmp[i]))
				rvalue=true;
		}
		
		return rvalue;
	}
}
