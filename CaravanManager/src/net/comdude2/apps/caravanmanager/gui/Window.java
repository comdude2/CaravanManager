package net.comdude2.apps.caravanmanager.gui;

import javax.swing.JFrame;

import net.comdude2.apps.caravanmanager.util.FileSystem;

public class Window extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -385915640733853266L;
	private Runnable runnable = null;
	private FrameController controller = null;
	private FileSystem fs = null;
	
	public Window(String name, FrameController controller, FileSystem fs){
		super(name);
		this.controller = controller;
		this.fs = fs;
	}
	
	public FrameController getController(){
		return controller;
	}
	
	public Runnable getRunnable(){
		return runnable;
	}
	
	public void setRunnable(Runnable runnable){
		this.runnable = runnable;
	}
	
	public FileSystem getFileSystem(){
		return fs;
	}
	
}
