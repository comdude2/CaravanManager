package net.comdude2.apps.caravanmanager.util;

import java.io.IOException;

public class ProgramTools {
	
	public ProgramTools(){
		
	}
	
	public static Process runProgram(String path){
		Process p;
		try {
			p = Runtime.getRuntime().exec(path);
			return p;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
