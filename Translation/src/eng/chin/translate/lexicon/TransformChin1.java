package eng.chin.translate.lexicon;

public class TransformChin1 {
	public String DoTransformChin(String sentence,String[] pos,String[] chin){
		
		String[] ss=sentence.split("#");
		int count=0;
		String combine="";
		String r_sent="";
		for(int i=0;i<ss.length;i++){
			String str=ss[i].trim();
			if(!str.isEmpty()){
				
				if(str.contains(",")){
					String[] temp_str=str.split(",");
					String temp_var="";
					for(int k=0;k<temp_str.length;k++){
						String pp=temp_str[k].trim();
						//System.out.println(">>>>>>>>>>>>"+pp);
						if(pp.contains("[")){
							String temp1=pp.substring(pp.indexOf("[")+1,pp.indexOf("/"));
							temp1=pp.replace(temp1, chin[count++]);
							//System.out.println("temp1 "+temp1);
							//System.out.println("Chin "+chin[count++]);
							combine+=temp1+",";
						}
						else{
							String temp1=pp.substring(0,pp.indexOf("/"));
							temp1=pp.replace(temp1, chin[count++]);
							combine+=temp1+",";
							//System.out.println(" other temp1 "+temp1);
							//System.out.println("Chin "+chin[count++]);
						}					
					}
				}//if
				else{
					String temp1=str.substring(str.indexOf("[")+1,str.indexOf("/"));
					temp1=str.replace(temp1, chin[count++]);
					combine+=temp1+",";
					/*System.out.println("temp1 "+temp1);
					System.out.println("Chin "+chin[count++]);*/
				}
				combine=combine.trim();
				if(combine.endsWith(","))
					combine=combine.substring(0,combine.length()-1);
				r_sent+=" #"+combine;
				combine="";
			}
		}//for
		/*System.out.println(" I.............."+sentence);
		System.out.println(" R ............."+r_sent);*/
		return r_sent;
	}
	
	
	
}
