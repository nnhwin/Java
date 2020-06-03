package clustering.form;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class GenerateNum {

	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	
	public float[] GeneratingNum() {
		float[] f=new float[20];
		for(int i=19;i>0;i=i--){	
			df2.setRoundingMode(RoundingMode.DOWN);
			String temp=df2.format(i/10.0f);
			float floatvalue=Float.parseFloat(temp);
			
			f[i]=floatvalue;
			f[--i]=Float.parseFloat((df2.format(Math.abs(1-floatvalue))));
			////System.out.println(f[i]+"........."+i);
		}
		
		/*for(int i=39;i>=20;i--){	
			df2.setRoundingMode(RoundingMode.DOWN);
			String temp=df2.format(i/20.0f);
			float floatvalue=Float.parseFloat(temp);
			
			f[i]=floatvalue;
			f[--i]=Float.parseFloat((df2.format(Math.abs(1-floatvalue))));
			////System.out.println(f[i]+"........."+i);
		}*/
		
	
		/*int k=4;
		float[] floatvalue=GetPair(f,k);
		for(int i=0;i<floatvalue.length;i++)
			//System.out.println("Weight "+(i+1)+" = "+floatvalue[i]);*/
		
		return f;
	}

	public static float[] GetPair(float[] f, int k){
		float[] pairvalue=new float[k];
		float total=0;
		Random random = new Random();
		int max=19;
		int min=1;
		String str="";
		int[] index=new int[k];
		float sum=0.0f;
		
		while(sum!=1.0){
			sum=0.0f;
			int rand=0;
			for(int i=0;i<k;i++){
				rand=random.nextInt(max - min + 1) + min;
				////System.out.println("Rand num: "+rand);
				index[i]=rand;
			}
			for(int j=0;j<k;j++){
				sum+=f[index[j]];
			}
			
		}
		
		if(sum==1.0){
			for(int i=0;i<k;i++){
				pairvalue[i]=f[index[i]];
				////System.out.println(f[index[i]]);
			}
		}
				
		return pairvalue;
	}
	/*public static void main(String[] args) {
		GenerateNum gn=new GenerateNum();
		float[] weights=gn.GeneratingNum();
		
		//k=2
		int k=4;
		float[] weightPair=GetPair(weights,k);
		for(int i=0;i<weightPair.length;i++)
			System.out.print(weightPair[i]+"\t");

	}*/

}
