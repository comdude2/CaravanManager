package net.comdude2.apps.caravanmanager.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.comdude2.apps.caravanmanager.util.Log;

public class Menu implements Runnable{
	
	private Log log = null;
	private Window frame = null;
	private LinkedList <JComponent> buttons = new LinkedList <JComponent> ();
	
	//Main
	public Menu(Log log, Window frame){
		this.log = log;
		this.log.debug("New " + this.getClass().getName() + " instance.");
		this.frame = frame;
	}
	
	//Main
	public void run(){
		log.debug(this.getClass().getName() + "'s Run() method called.");
		JPanel pane = new JPanel();
		this.frame.setLocationByPlatform(true);
		
		//Set look and feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){//Use default
		}
		
		pane.setLayout(null);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Buttons
		ActionListener listener = null;
		
		//Rentals
		JButton rentalButton = new JButton("Rentals");
		//x y, width, height
		rentalButton.setBounds(100, 50, 100, 25);
		buttons.add(rentalButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Rentals' button clicked.");
				log.debug("Attempting to traverseTo 'Rentals'");
				frame.getController().traverseTo("Rentals", frame);
			}
			
		};
		rentalButton.addActionListener(listener);
		listener = null;
		
		//Reports
		JButton reportsButton = new JButton("Reports");
		//x y, width, height
		reportsButton.setBounds(100, 100, 100, 25);
		buttons.add(reportsButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Reports' button clicked.");
				log.debug("Attempting to traverseTo 'Reports'");
				frame.getController().traverseTo("Reports", frame);
			}
			
		};
		reportsButton.addActionListener(listener);
		listener = null;
		
		//Add buttons to pane
		for (JComponent button : buttons){
			pane.add(button);
		}
		
		//Add pane
		this.frame.add(pane);
		
		//Display
		frame.setSize(300, 300);
		frame.setResizable(false);
		log.debug("Frame ready for class: " + this.getClass().getName());
	}
	
	//Main
	public void close(){
		this.frame.dispose();
	}
	
}
