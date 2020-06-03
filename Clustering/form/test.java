package clustering.form;

public class test {

	public test() {
		float vv=getMvalue(3.4f,3);
		System.out.println("VV......"+vv);
	}
	float getMvalue(float base,int power){
		float vv=1.0f;
		for(int i=1;i<=power;i++)
			vv*=base;
		
		return vv;
		
	}
	public static void main(String[] args) {
		new test();

	}

}
