package property;
public class Property {
	
	public Property(String workDir) {
		super();
		this.workDir = workDir;
	}

	private final String workDir;
	
	public String getWorkDir() {
		return workDir;
	}

	private static Property p = null;

	public static void createInstance(String workDir) {
		if (p == null)
			p = new Property(workDir);
	}

	public static Property getInstance() {
		return p;
	}

	private Property(String bugFilePath, String sourceCodeDir,String workDir,float alpha,String outputFile, String poj, int offset) {
		
		this.workDir=workDir;
	
       
	}

	
	

}
