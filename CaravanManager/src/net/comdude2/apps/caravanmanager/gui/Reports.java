package net.comdude2.apps.caravanmanager.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.comdude2.apps.caravanmanager.util.Log;

public class Reports implements Runnable{
	
	private Log log = null;
	private Window frame = null;
	private LinkedList <JComponent> buttons = new LinkedList <JComponent> ();
	
	//Buttons
	private JButton backButton = new JButton("Back");
	
	//Text area
	private JTextArea text = new JTextArea(5, 1);
	
	//Combo box
	private JComboBox<String> combo = null;
	
	//Reports
	private File[] reports = null;
	
	public Reports(Log log, Window frame){
		this.log = log;
		this.frame = frame;
	}
	
	@Override
	public void run() {
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
		
		JLabel reportLabel = new JLabel("Report", JLabel.CENTER);
		reportLabel.setBounds(0, 50, 100, 25);
		buttons.add(reportLabel);
		
		//Text area
		text.setBounds(10, 75, 470, 350);
		buttons.add(text);
		
		//Combo Box
		String[] array = (String[]) loadReports().toArray();
		combo = new JComboBox<String>(array);
		combo.setBounds(400, 75, 100, 25);
		buttons.add(combo);
		
		//Buttons
		ActionListener listener = null;
		
		//Back button
		
		//JButton backButton = new JButton("Back");
		
		backButton.setBounds(0, 0, 75, 25);
		buttons.add(backButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Back' button clicked.");
				log.debug("Attempting to traverseTo 'Menu'");
				frame.getController().traverseTo("Menu", frame);
			}
			
		};
		backButton.addActionListener(listener);
		listener = null;
		
		//Add buttons to pane
		for (JComponent button : buttons){
			pane.add(button);
		}
		
		//Add pane
		this.frame.add(pane);
		
		//Display
		frame.setSize(500, 500);
		frame.setResizable(false);
		log.debug("Frame ready for class: " + this.getClass().getName());
	}
	
	public void getReport(String name){
		
	}
	
	public LinkedList <String> loadReports(){
		try{
			File f = new File(frame.getFileSystem().getMainPath() + "downloads/reports/");
			reports = f.listFiles();
			LinkedList <String> files = new LinkedList <String> ();
			if (reports.length > 0){
				for (File file : reports){
					files.add(file.getName().replace(".txt", ""));
				}
			}else{
				files.add("None");
			}
			return files;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
