package net.comdude2.apps.caravanmanager.gui;

import java.awt.EventQueue;
import java.util.LinkedList;

import net.comdude2.apps.caravanmanager.util.FileSystem;
import net.comdude2.apps.caravanmanager.util.Log;

public class FrameController {
	
	private Log log = null;
	private LinkedList <Window> windows = new LinkedList <Window> ();
	private FileSystem fs = null;
	
	public FrameController(Log log, FileSystem fs){
		this.log = log;
		this.log.debug("New " + this.getClass().getName() + " instance.");
		this.fs = fs;
		Window window = new Window("Menu", this, this.fs);
		window.setRunnable(new Menu(log, window));
		windows.add(window);
		window = new Window("Rentals", this, this.fs);
		window.setRunnable(new Rentals(log, window));
		windows.add(window);
		window = new Window("Reports", this, this.fs);
		window.setRunnable(new Reports(log, window));
		windows.add(window);
		for (Window w : windows){
			EventQueue.invokeLater(w.getRunnable());
		}
		log.info("Waiting for initialisation................................");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			log.error("Thread was interrupted!", e);
		}
		for (Window w : windows){
			if (w.getRunnable() instanceof Menu){
				w.setVisible(true);
				break;
			}
		}
	}
	
	public void traverseTo(String name, Window from){
		for (Window window : windows){
			if (window.getRunnable().getClass().getName().contains(name)){
				window.setVisible(true);
				from.setVisible(false);;
				break;
			}
		}
	}
	
	public void initialise(){
		
	}
	
}
