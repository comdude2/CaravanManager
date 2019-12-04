package net.comdude2.apps.caravanmanager.util;

import java.io.File;

public class FileSystem {
	
	private Log log = null;
	private String mainPath = null;
	
	//this.log.debug("");
	
	public FileSystem(Log log){
		this.log = log;
		this.log.debug("New " + this.getClass().getName() + " instance.");
		this.findFileSystem();
	}
	
	public String getMainPath(){
		return this.mainPath;
	}
	
	public void findFileSystem(){
		this.log.debug("Attempting to find FileSystem...");
		File f = new File("");
		this.mainPath = f.getAbsolutePath() + "/";
		this.log.debug("Resolved FileSystem: " + mainPath);
	}
	
	public void createSubFileSystem(){
		this.log.debug("Creating SubFileSystem...");
		File f = new File(mainPath + "logs/");
		if (!f.exists()){
			f.mkdir();
		}
		f = new File(mainPath + "downloads/");
		if (!f.exists()){
			f.mkdir();
		}
		f = new File(mainPath + "downloads/app/");
		if (!f.exists()){
			f.mkdir();
		}
		f = new File(mainPath + "downloads/records/");
		if (!f.exists()){
			f.mkdir();
		}
		f = new File(mainPath + "downloads/reports/");
		if (!f.exists()){
			f.mkdir();
		}
		this.log.debug("Finished creating SubFileSystem.");
	}
	
}
