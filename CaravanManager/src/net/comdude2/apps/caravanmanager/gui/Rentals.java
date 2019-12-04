package net.comdude2.apps.caravanmanager.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import net.comdude2.apps.caravanmanager.database.ConnectionException;
import net.comdude2.apps.caravanmanager.database.DatabaseConnector;
import net.comdude2.apps.caravanmanager.util.Log;

public class Rentals implements Runnable{
	
	private Log log = null;
	private Window frame = null;
	private LinkedList <JComponent> buttons = new LinkedList <JComponent> ();
	private int lastSelectedIndex = -1;
	boolean editing = false;
	
	//Lists models
	private DefaultListModel<String> rentalIDModel = new DefaultListModel<String> ();
	private DefaultListModel<String> caravanNameModel = new DefaultListModel<String> ();
	private DefaultListModel<String> renteeNameModel = new DefaultListModel<String> ();
	private DefaultListModel<String> renteeAddressModel = new DefaultListModel<String> ();
	private DefaultListModel<String> renteeEmailModel = new DefaultListModel<String> ();
	private DefaultListModel<String> renteePhoneModel = new DefaultListModel<String> ();
	private DefaultListModel<String> rentalStartModel = new DefaultListModel<String> ();
	private DefaultListModel<String> rentalEndModel = new DefaultListModel<String> ();
	private DefaultListModel<Boolean> rentalInSeasonModel = new DefaultListModel<Boolean> ();
	private DefaultListModel<String> rentalFeeModel = new DefaultListModel<String> ();
	private DefaultListModel<Boolean> rentalPaidModel = new DefaultListModel<Boolean> ();
	
	//Lists
	final JList<String> rentalIDList = new JList<String>(rentalIDModel);
	final JList<String> caravanNameList = new JList<String>(caravanNameModel);
	final JList<String> renteeNameList = new JList<String>(renteeNameModel);
	final JList<String> renteeAddressList = new JList<String>(renteeAddressModel);
	final JList<String> renteeEmailList = new JList<String>(renteeEmailModel);
	final JList<String> renteePhoneList = new JList<String>(renteePhoneModel);
	final JList<String> rentalStartList = new JList<String>(rentalStartModel);
	final JList<String> rentalEndList = new JList<String>(rentalEndModel);
	final JList<Boolean> rentalInSeasonList = new JList<Boolean>(rentalInSeasonModel);
	final JList<String> rentalFeeList = new JList<String>(rentalFeeModel);
	final JList<Boolean> rentalPaidList = new JList<Boolean>(rentalPaidModel);
	
	//Text Boxes
	private JTextField rentalIDText = null;
	private JTextField caravanNameText = null;
	private JTextField renteeNameText = null;
	private JTextField renteeAddressText = null;
	private JTextField renteeEmailText = null;
	private JTextField renteePhoneText = null;
	private JTextField rentalStartText = null;
	private JTextField rentalEndText = null;
	private JComboBox<String> rentalInSeasonText = null;
	private JTextField rentalFeeText = null;
	private JComboBox<String> rentalPaidText = null;
	
	//Buttons
	private JButton backButton = new JButton("Back");
	private JButton updateButton = new JButton("Update");
	private JButton addRecordButton = new JButton("Add Record");
	private JButton selectItemButton = new JButton("Select Item");
	private JButton clearButton = new JButton("Clear");
	private JButton saveChangesButton = new JButton("Save Changes");
	private JButton deleteButton = new JButton("Delete selected");
	
	//Label
	JLabel lastUpdateLabel = null;
	
	//Main
	public Rentals(Log log, Window frame){
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
		
		//Update Button
		
		//JButton updateButton = new JButton("Update");
		
		updateButton.setBounds(500, 0, 100, 25);
		buttons.add(updateButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Update' button clicked.");
				log.debug("Updating Lists.");
				update();
			}
			
		};
		updateButton.addActionListener(listener);
		listener = null;
		
		//Add Record Button
		
		//JButton addRecordButton = new JButton("Add Record");
		
		addRecordButton.setBounds(1000, 385, 100, 25);
		buttons.add(addRecordButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Add Record' button clicked.");
				log.debug("Adding record after validation.");
				addRecord();
			}
			
		};
		addRecordButton.addActionListener(listener);
		listener = null;
		
		//Select Item Button
		
		//JButton selectItemButton = new JButton("Select Item");
		
		selectItemButton.setBounds(1000, 275, 100, 25);
		buttons.add(selectItemButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Select Item' button clicked.");
				log.debug("Selecting item.");
				selectItem();
			}
			
		};
		selectItemButton.addActionListener(listener);
		listener = null;
		
		//Clear Button
		
		//JButton clearButton = new JButton("Clear");
		
		clearButton.setBounds(900, 385, 100, 25);
		buttons.add(clearButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Clear' button clicked.");
				log.debug("Clearing.");
				clearTextBoxes();
			}
			
		};
		clearButton.addActionListener(listener);
		listener = null;
		
		//Save Button
		
		//JButton saveChangesButton = new JButton("Save Changes");
		
		saveChangesButton.setBounds(1000, 410, 100, 25);
		saveChangesButton.setEnabled(false);
		buttons.add(saveChangesButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Save changes' button clicked.");
				log.debug("Saving changes.");
				updateRecord();
			}
			
		};
		saveChangesButton.addActionListener(listener);
		listener = null;
		
		deleteButton.setBounds(1000, 435, 100, 25);
		buttons.add(deleteButton);
		listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				log.debug("'Delete' button clicked.");
				log.debug("Deleting record.");
				deleteRecord();
			}
			
		};
		deleteButton.addActionListener(listener);
		listener = null;
		
		//Last Update label
		lastUpdateLabel = new JLabel("Last Update: ", JLabel.CENTER);
		lastUpdateLabel.setBounds(600, 0, 200, 25);
		buttons.add(lastUpdateLabel);
		
		//Labels for list views
		JLabel rentalListLabel = new JLabel("Rental List", JLabel.CENTER);
		rentalListLabel.setBounds(0, 50, 100, 25);
		buttons.add(rentalListLabel);
		JLabel rentalIDLabel = new JLabel("RentalID", JLabel.CENTER);
		rentalIDLabel.setBounds(0, 75, 100, 25);
		buttons.add(rentalIDLabel);
		JLabel caravanNameLabel = new JLabel("Caravan Name", JLabel.CENTER);
		caravanNameLabel.setBounds(100, 75, 100, 25);
		buttons.add(caravanNameLabel);
		JLabel renteeNameLabel = new JLabel("Name", JLabel.CENTER);
		renteeNameLabel.setBounds(200, 75, 100, 25);
		buttons.add(renteeNameLabel);
		JLabel renteeAddressLabel = new JLabel("Address", JLabel.CENTER);
		renteeAddressLabel.setBounds(300, 75, 100, 25);
		buttons.add(renteeAddressLabel);
		JLabel renteeEmailLabel = new JLabel("Email", JLabel.CENTER);
		renteeEmailLabel.setBounds(400, 75, 100, 25);
		buttons.add(renteeEmailLabel);
		JLabel renteePhoneLabel = new JLabel("PhoneNo", JLabel.CENTER);
		renteePhoneLabel.setBounds(500, 75, 100, 25);
		buttons.add(renteePhoneLabel);
		JLabel rentalStartLabel = new JLabel("Start Date", JLabel.CENTER);
		rentalStartLabel.setBounds(600, 75, 100, 25);
		buttons.add(rentalStartLabel);
		JLabel rentalEndLabel = new JLabel("End Date", JLabel.CENTER);
		rentalEndLabel.setBounds(700, 75, 100, 25);
		buttons.add(rentalEndLabel);
		JLabel rentalInSeasonLabel = new JLabel("In Season", JLabel.CENTER);
		rentalInSeasonLabel.setBounds(800, 75, 100, 25);
		buttons.add(rentalInSeasonLabel);
		JLabel rentalFeeLabel = new JLabel("Fee", JLabel.CENTER);
		rentalFeeLabel.setBounds(900, 75, 100, 25);
		buttons.add(rentalFeeLabel);
		JLabel renteePaidLabel = new JLabel("Paid", JLabel.CENTER);
		renteePaidLabel.setBounds(1000, 75, 100, 25);
		buttons.add(renteePaidLabel);
		
		//Labels for text boxes
		JLabel rentalListLabel2 = new JLabel("Selected Item", JLabel.CENTER);
		rentalListLabel2.setBounds(0, 300, 100, 25);
		buttons.add(rentalListLabel2);
		JLabel rentalIDLabel2 = new JLabel("RentalID", JLabel.CENTER);
		rentalIDLabel2.setBounds(0, 325, 100, 25);
		buttons.add(rentalIDLabel2);
		JLabel caravanNameLabel2 = new JLabel("Caravan Name", JLabel.CENTER);
		caravanNameLabel2.setBounds(100, 325, 100, 25);
		buttons.add(caravanNameLabel2);
		JLabel renteeNameLabel2 = new JLabel("Name", JLabel.CENTER);
		renteeNameLabel2.setBounds(200, 325, 100, 25);
		buttons.add(renteeNameLabel2);
		JLabel renteeAddressLabel2 = new JLabel("Address", JLabel.CENTER);
		renteeAddressLabel2.setBounds(300, 325, 100, 25);
		buttons.add(renteeAddressLabel2);
		JLabel renteeEmailLabel2 = new JLabel("Email", JLabel.CENTER);
		renteeEmailLabel2.setBounds(400, 325, 100, 25);
		buttons.add(renteeEmailLabel2);
		JLabel renteePhoneLabel2 = new JLabel("PhoneNo", JLabel.CENTER);
		renteePhoneLabel2.setBounds(500, 325, 100, 25);
		buttons.add(renteePhoneLabel2);
		JLabel rentalStartLabel2 = new JLabel("Start Date", JLabel.CENTER);
		rentalStartLabel2.setBounds(600, 325, 100, 25);
		buttons.add(rentalStartLabel2);
		JLabel rentalEndLabel2 = new JLabel("End Date", JLabel.CENTER);
		rentalEndLabel2.setBounds(700, 325, 100, 25);
		buttons.add(rentalEndLabel2);
		JLabel rentalInSeasonLabel2 = new JLabel("In Season", JLabel.CENTER);
		rentalInSeasonLabel2.setBounds(800, 325, 100, 25);
		buttons.add(rentalInSeasonLabel2);
		JLabel rentalFeeLabel2 = new JLabel("Fee", JLabel.CENTER);
		rentalFeeLabel2.setBounds(900, 325, 100, 25);
		buttons.add(rentalFeeLabel2);
		JLabel renteePaidLabel2 = new JLabel("Paid", JLabel.CENTER);
		renteePaidLabel2.setBounds(1000, 325, 100, 25);
		buttons.add(renteePaidLabel2);
		
		//Text Boxes
		rentalIDText = new JTextField(15);
		rentalIDText.setBounds(0, 350, 100, 25);
		rentalIDText.setDocument(new JTextFieldLimit(15));
		rentalIDText.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		buttons.add(rentalIDText);
		caravanNameText = new JTextField(5);
		caravanNameText.setBounds(100, 350, 100, 25);
		caravanNameText.setDocument(new JTextFieldLimit(5));
		buttons.add(caravanNameText);
		renteeNameText = new JTextField(30);
		renteeNameText.setBounds(200, 350, 100, 25);
		renteeNameText.setDocument(new JTextFieldLimit(30));
		buttons.add(renteeNameText);
		renteeAddressText = new JTextField(120);
		renteeAddressText.setBounds(300, 350, 100, 25);
		renteeAddressText.setDocument(new JTextFieldLimit(120));
		buttons.add(renteeAddressText);
		renteeEmailText = new JTextField(20);
		renteeEmailText.setBounds(400, 350, 100, 25);
		renteeEmailText.setDocument(new JTextFieldLimit(20));
		buttons.add(renteeEmailText);
		renteePhoneText = new JTextField(11);
		renteePhoneText.setBounds(500, 350, 100, 25);
		renteePhoneText.setDocument(new JTextFieldLimit(11));
		buttons.add(renteePhoneText);
		rentalStartText = new JTextField(10);
		rentalStartText.setBounds(600, 350, 100, 25);
		rentalStartText.setDocument(new JTextFieldLimit(10));
		buttons.add(rentalStartText);
		rentalEndText = new JTextField(10);
		rentalEndText.setBounds(700, 350, 100, 25);
		rentalEndText.setDocument(new JTextFieldLimit(10));
		buttons.add(rentalEndText);
		//TODO Change to boolean
		rentalInSeasonText = new JComboBox<String>();
		rentalInSeasonText.setBounds(800, 350, 100, 25);
		rentalInSeasonText.addItem("true");
		rentalInSeasonText.addItem("false");
		buttons.add(rentalInSeasonText);
		rentalFeeText = new JTextField(10);
		rentalFeeText.setBounds(900, 350, 100, 25);
		rentalFeeText.setDocument(new JTextFieldLimit(10));
		buttons.add(rentalFeeText);
		//TODO Change to boolean
		rentalPaidText = new JComboBox<String>();
		rentalPaidText.setBounds(1000, 350, 100, 25);
		rentalPaidText.addItem("true");
		rentalPaidText.addItem("false");
		buttons.add(rentalPaidText);
		
		//List views
		String [] testArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
		DefaultListModel<String> model = new DefaultListModel<String>();
		ListSelectionListener listSelectionListener = null;
		
		//Rental ID
		for (String item : testArray){
			model.addElement(item);
		}
		//final JList<String> rentalIDList = new JList<String>(rentalIDModel);
		rentalIDList.setVisibleRowCount(Integer.MAX_VALUE);
		DefaultListCellRenderer renderer =  (DefaultListCellRenderer)rentalIDList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller1 = new JScrollPane(rentalIDList);
		scroller1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller1.setBounds(0, 100, 100, 150);
		buttons.add(scroller1);
		
		//Caravan Name
		//final JList<String> caravanNameList = new JList<String>(caravanNameModel);
		caravanNameList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)caravanNameList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller2 = new JScrollPane(caravanNameList);
		scroller2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller2.setBounds(100, 100, 100, 150);
		buttons.add(scroller2);
		
		//Rentee Name
		//final JList<String> renteeNameList = new JList<String>(renteeNameModel);
		renteeNameList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)renteeNameList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.LEFT);
		final JScrollPane scroller3 = new JScrollPane(renteeNameList);
		scroller3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller3.setBounds(200, 100, 100, 150);
		buttons.add(scroller3);
		
		//Rentee Address
		//final JList<String> renteeAddressList = new JList<String>(renteeAddressModel);
		renteeAddressList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)renteeAddressList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.LEFT);
		final JScrollPane scroller4 = new JScrollPane(renteeAddressList);
		scroller4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller4.setBounds(300, 100, 100, 150);
		buttons.add(scroller4);
		
		//Rentee Email
		//final JList<String> renteeEmailList = new JList<String>(renteeEmailModel);
		renteeEmailList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)renteeEmailList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.LEFT);
		final JScrollPane scroller5 = new JScrollPane(renteeEmailList);
		scroller5.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller5.setBounds(400, 100, 100, 150);
		buttons.add(scroller5);
		
		//Rentee Phone
		//final JList<String> renteePhoneList = new JList<String>(renteePhoneModel);
		renteePhoneList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)renteePhoneList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller6 = new JScrollPane(renteePhoneList);
		scroller6.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller6.setBounds(500, 100, 100, 150);
		buttons.add(scroller6);
		
		//Rental Start Date
		//final JList<String> rentalStartList = new JList<String>(rentalStartModel);
		rentalStartList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)rentalStartList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller7 = new JScrollPane(rentalStartList);
		scroller7.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller7.setBounds(600, 100, 100, 150);
		buttons.add(scroller7);
		
		//Rental End Date
		//final JList<String> rentalEndList = new JList<String>(rentalEndModel);
		rentalEndList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)rentalEndList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller8 = new JScrollPane(rentalEndList);
		scroller8.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller8.setBounds(700, 100, 100, 150);
		buttons.add(scroller8);
		
		//Rental In Season
		//final JList<Boolean> rentalInSeasonList = new JList<Boolean>(rentalInSeasonModel);
		rentalInSeasonList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)rentalInSeasonList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller9 = new JScrollPane(rentalInSeasonList);
		scroller9.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller9.setBounds(800, 100, 100, 150);
		buttons.add(scroller9);
		
		//Rental Fee
		//final JList<String> rentalFeeList = new JList<String>(rentalFeeModel);
		rentalFeeList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)rentalFeeList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller10 = new JScrollPane(rentalFeeList);
		scroller10.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller10.setBounds(900, 100, 100, 150);
		buttons.add(scroller10);
		
		//Rental Paid
		//final JList<Boolean> rentalPaidList = new JList<Boolean>(rentalPaidModel);
		rentalPaidList.setVisibleRowCount(Integer.MAX_VALUE);
		renderer =  (DefaultListCellRenderer)rentalPaidList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);
		final JScrollPane scroller11 = new JScrollPane(rentalPaidList);
		scroller11.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller11.setBounds(1000, 100, 100, 150);
		buttons.add(scroller11);
		
		//Add List Listeners
		
		//Add List scroll listeners
		
		AdjustmentListener l = new AdjustmentListener(){

			@Override
			public void adjustmentValueChanged(AdjustmentEvent event) {
				if (event.getSource() instanceof JScrollBar){
					JScrollBar bar = (JScrollBar) event.getSource();
					if (scroller1.getVerticalScrollBar() != bar){
						scroller1.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller2.getVerticalScrollBar() != bar){
						scroller2.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller3.getVerticalScrollBar() != bar){
						scroller3.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller4.getVerticalScrollBar() != bar){
						scroller4.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller5.getVerticalScrollBar() != bar){
						scroller5.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller6.getVerticalScrollBar() != bar){
						scroller6.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller7.getVerticalScrollBar() != bar){
						scroller7.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller8.getVerticalScrollBar() != bar){
						scroller8.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller9.getVerticalScrollBar() != bar){
						scroller9.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller10.getVerticalScrollBar() != bar){
						scroller10.getVerticalScrollBar().setValue(event.getValue());
					}
					if (scroller11.getVerticalScrollBar() != bar){
						scroller11.getVerticalScrollBar().setValue(event.getValue());
					}
				}
			}
			
		};
		scroller1.getVerticalScrollBar().addAdjustmentListener(l);
		scroller2.getVerticalScrollBar().addAdjustmentListener(l);
		scroller3.getVerticalScrollBar().addAdjustmentListener(l);
		scroller4.getVerticalScrollBar().addAdjustmentListener(l);
		scroller5.getVerticalScrollBar().addAdjustmentListener(l);
		scroller6.getVerticalScrollBar().addAdjustmentListener(l);
		scroller7.getVerticalScrollBar().addAdjustmentListener(l);
		scroller8.getVerticalScrollBar().addAdjustmentListener(l);
		scroller9.getVerticalScrollBar().addAdjustmentListener(l);
		scroller10.getVerticalScrollBar().addAdjustmentListener(l);
		scroller11.getVerticalScrollBar().addAdjustmentListener(l);
		
		//Add List selection listeners
		
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalIDList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		caravanNameList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		renteeNameList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		renteeAddressList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		renteeEmailList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		renteePhoneList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalStartList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalEndList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalFeeList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalInSeasonList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalPaidList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalFeeList.addListSelectionListener(listSelectionListener);
		listSelectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				rentalIDList.clearSelection();
				caravanNameList.clearSelection();
				renteeNameList.clearSelection();
				renteeAddressList.clearSelection();
				renteeEmailList.clearSelection();
				renteePhoneList.clearSelection();
				rentalStartList.clearSelection();
				rentalEndList.clearSelection();
				rentalInSeasonList.clearSelection();
				rentalFeeList.clearSelection();
				@SuppressWarnings("rawtypes")
				JList list = (JList) arg0.getSource();
				lastSelectedIndex = list.getSelectedIndex();
			}
			
		};
		rentalPaidList.addListSelectionListener(listSelectionListener);
		
		
		
		
		//Add copy selection buttons
		
		JButton copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(0, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalIDList.getSelectedIndex() != -1){
					copyToClipboard(rentalIDList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(100, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (caravanNameList.getSelectedIndex() != -1){
					copyToClipboard(caravanNameList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(200, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (renteeNameList.getSelectedIndex() != -1){
					copyToClipboard(renteeNameList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(300, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (renteeAddressList.getSelectedIndex() != -1){
					copyToClipboard(renteeAddressList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(400, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (renteeEmailList.getSelectedIndex() != -1){
					copyToClipboard(renteeEmailList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(500, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (renteePhoneList.getSelectedIndex() != -1){
					copyToClipboard(renteePhoneList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(600, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalStartList.getSelectedIndex() != -1){
					copyToClipboard(rentalStartList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(700, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalEndList.getSelectedIndex() != -1){
					copyToClipboard(rentalEndList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(800, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalInSeasonList.getSelectedIndex() != -1){
					copyToClipboard(rentalInSeasonList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(900, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalFeeList.getSelectedIndex() != -1){
					copyToClipboard(rentalFeeList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		copySelectionButton = new JButton("Copy Sel");
		copySelectionButton.setBounds(1000, 250, 100, 25);
		buttons.add(copySelectionButton);
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rentalPaidList.getSelectedIndex() != -1){
					copyToClipboard(rentalPaidList.getSelectedValue().toString());
				}
			}
					
		};
		copySelectionButton.addActionListener(listener);
		listener = null;
		
		//Add buttons to pane
		for (JComponent button : buttons){
			pane.add(button);
		}
		
		//Add pane
		this.frame.add(pane);
		
		//Display
		frame.setSize(1100, 500);
		frame.setResizable(false);
		log.debug("Frame ready for class: " + this.getClass().getName());
		
		//Update
		update();
	}
	
	public void addRecord(){
		if (validate()){
			log.debug("Adding record to Rentals...");
			DatabaseConnector connector = new DatabaseConnector("jdbc:mysql://origin.mcviral.net:3306/", log);
			connector.setupConnection("norreena", "4jeRUbWHUJHaWMXj");
			try {
				connector.connect();
				Connection connection = connector.getConnection();
				PreparedStatement statement = (PreparedStatement) connection.prepareStatement("INSERT INTO `caravanmanager`.`rentals` (`RentalID`, `CaravanName`, `RenteeName`, `RenteeAddress`, `RenteeEmail`, `RenteePhone`, `RentalStart`, `RentalEnd`, `RentalInSeason`, `RentalFee`, `RentalPaid`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				statement.setInt(1, Integer.parseInt(rentalIDText.getText()));
				statement.setString(2, caravanNameText.getText());
				statement.setString(3, renteeNameText.getText());
				statement.setString(4, renteeAddressText.getText());
				statement.setString(5, renteeEmailText.getText());
				statement.setString(6, renteePhoneText.getText());
				statement.setString(7, rentalStartText.getText());
				statement.setString(8, rentalEndText.getText());
				statement.setBoolean(9, Boolean.valueOf(rentalInSeasonText.getSelectedItem().toString()));
				statement.setString(10, rentalFeeText.getText());
				statement.setBoolean(11, Boolean.valueOf(rentalPaidText.getSelectedItem().toString()));
				int worked = statement.executeUpdate();
				log.debug("Worked = " + worked);
				if (worked == 1){
					JOptionPane.showMessageDialog(frame, "Record Added.", "Result", JOptionPane.INFORMATION_MESSAGE);
					clearTextBoxes();
				}else{
					JOptionPane.showMessageDialog(frame, "Failed to add record!", "Result", JOptionPane.ERROR_MESSAGE);
				}
				connector.disconnect();
				log.debug("Row added");
			} catch (ConnectionException e) {
				log.error(e.getMessage(), e);
			} catch (SQLException e) {
				log.severe("Failed to add record: " + e.getMessage());
				log.severe("SQLState: " + e.getSQLState());
				log.severe("Error Code: " + e.getErrorCode());
				log.error(e.getMessage(), e);
			} catch (IllegalStateException e){
				log.severe("Failed to add record: " + e.getMessage());
				log.severe("This is likely to be caused by revokation of access to the database.");
				log.severe("Error cause: " + e.getCause());
				log.severe("Cause Stack Trace: ");
				e.getCause().printStackTrace();
				log.error(e.getMessage(), e);
			} catch (Exception e) {
				log.severe("Failed to add record: " + e.getMessage());
				log.error(e.getMessage(), e);
			}finally{
				try{
					connector.disconnect();
				}catch(Exception e){
					log.warning("Couldn't disconnect from database.");
				}
			}
		}else{
			
		}
	}
	
	private void clearTextBoxes(){
		if (editing){
			editing = false;
			addRecordButton.setEnabled(true);
			saveChangesButton.setEnabled(false);
		}
		caravanNameText.setText("");
		renteeNameText.setText("");
		renteeAddressText.setText("");
		renteeEmailText.setText("");
		renteePhoneText.setText("");
		rentalStartText.setText("");
		rentalEndText.setText("");
		rentalInSeasonText.setSelectedIndex(0);
		rentalFeeText.setText("");
		rentalPaidText.setSelectedIndex(0);
		update();
	}
	
	public boolean validate(){
		boolean allow = true;
		if (isBlank(caravanNameText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Caravan name is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(renteeNameText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rentee name is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(renteeAddressText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rentee address is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(renteeEmailText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rentee email No is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(renteePhoneText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rentee Phone No is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(rentalStartText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rental start date is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(rentalEndText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rental end date is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (isBlank(rentalFeeText.getText())){allow = false;JOptionPane.showMessageDialog(frame, "Rental fee is empty.", "Can't add record", JOptionPane.ERROR_MESSAGE);}
		if (allow != false){
			try{
				Integer.parseInt(renteePhoneText.getText());
			} catch (Exception e){
				allow = false;
				JOptionPane.showMessageDialog(frame, "Rentee Phone No is not a number.", "Can't add record", JOptionPane.ERROR_MESSAGE);
			}
		}
		return allow;
	}
	
	private boolean isBlank(String string){
		if (string.equals(null)){
			return true;
		}else if (string.equals("")){
			return true;
		}else if ((string.replaceAll(" ", "")).equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	//TODO COMPLETE!
	public void updateElements(ResultSet set){
		try {
			//boolean cont = true;
			set.next();
			rentalIDModel.clear();
			caravanNameModel.clear();
			renteeNameModel.clear();
			renteeAddressModel.clear();
			renteeEmailModel.clear();
			renteePhoneModel.clear();
			rentalStartModel.clear();
			rentalEndModel.clear();
			rentalInSeasonModel.clear();
			rentalFeeModel.clear();
			rentalPaidModel.clear();
			int i = 0;
			while(set.getString("RentalID") != null){
				rentalIDModel.addElement(String.valueOf(set.getInt("RentalID")));
				caravanNameModel.addElement(set.getString("CaravanName"));
				renteeNameModel.addElement(set.getString("RenteeName"));
				renteeAddressModel.addElement(set.getString("RenteeAddress"));
				renteeEmailModel.addElement(set.getString("RenteeEmail"));
				renteePhoneModel.addElement(set.getString("RenteePhone"));
				rentalStartModel.addElement(set.getString("RentalStart"));
				rentalEndModel.addElement(set.getString("RentalEnd"));
				rentalInSeasonModel.addElement(set.getBoolean("RentalInSeason"));
				rentalFeeModel.addElement(set.getString("RentalFee"));
				rentalPaidModel.addElement(set.getBoolean("RentalPaid"));
				if (set.getInt("RentalID") > i){
					i = set.getInt("RentalID");
				}
				if (!set.isLast()){
					set.next();
				}else{
					rentalIDText.setEditable(false);
					break;
				}
			}
			rentalIDText.setText(String.valueOf(i + 1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void update(){
		log.debug("Updating results for Rentals...");
		DatabaseConnector connector = new DatabaseConnector("jdbc:mysql://origin.mcviral.net:3306/", log);
		connector.setupConnection("norreena", "4jeRUbWHUJHaWMXj");
		try {
			connector.connect();
			Connection connection = connector.getConnection();
			Statement statement = (Statement) connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM CaravanManager.Rentals");
			statement.closeOnCompletion();
			updateElements(results);
			connector.disconnect();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastUpdateLabel.setText("Last Update: " + sdf.format(new Date()));
			log.debug("Results updated");
		} catch (ConnectionException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.severe("Failed to get database results: " + e.getMessage());
			log.severe("SQLState: " + e.getSQLState());
			log.severe("Error Code: " + e.getErrorCode());
			log.error(e.getMessage(), e);
		} catch (IllegalStateException e){
			log.severe("Failed to get database results: " + e.getMessage());
			log.severe("This is likely to be caused by revokation of access to the database.");
			log.severe("Error cause: " + e.getCause());
			log.severe("Cause Stack Trace: ");
			e.getCause().printStackTrace();
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.severe("Failed get database results: " + e.getMessage());
			log.error(e.getMessage(), e);
		}finally{
			try{
				connector.disconnect();
			}catch(Exception e){
				log.warning("Couldn't disconnect from database.");
			}
		}
	}
	
	private void selectItem(){
		if (this.lastSelectedIndex != -1){
			editing = true;
			addRecordButton.setEnabled(false);
			saveChangesButton.setEnabled(true);
			rentalIDText.setText(rentalIDModel.elementAt(this.lastSelectedIndex));
			caravanNameText.setText(caravanNameModel.elementAt(this.lastSelectedIndex));
			renteeNameText.setText(renteeNameModel.elementAt(this.lastSelectedIndex));
			renteeAddressText.setText(renteeAddressModel.elementAt(this.lastSelectedIndex));
			renteeEmailText.setText(renteeEmailModel.elementAt(this.lastSelectedIndex));
			renteePhoneText.setText(renteePhoneModel.elementAt(this.lastSelectedIndex));
			rentalStartText.setText(rentalStartModel.elementAt(this.lastSelectedIndex));
			rentalEndText.setText(rentalEndModel.elementAt(this.lastSelectedIndex));
			if (rentalInSeasonModel.get(this.lastSelectedIndex).booleanValue()){
				rentalInSeasonText.setSelectedIndex(1);
			}else{
				rentalInSeasonText.setSelectedIndex(0);
			}
			rentalFeeText.setText(rentalFeeModel.elementAt(this.lastSelectedIndex));
			if (rentalPaidModel.get(this.lastSelectedIndex).booleanValue()){
				rentalPaidText.setSelectedIndex(1);
			}else{
				rentalPaidText.setSelectedIndex(0);
			}
		}else{
			JOptionPane.showMessageDialog(frame, "You've not selected an item.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void updateRecord(){
		if (validate()){
			log.debug("Updating record in Rentals...");
			DatabaseConnector connector = new DatabaseConnector("jdbc:mysql://origin.mcviral.net:3306/", log);
			connector.setupConnection("norreena", "4jeRUbWHUJHaWMXj");
			try {
				connector.connect();
				Connection connection = connector.getConnection();
				PreparedStatement statement = (PreparedStatement) connection.prepareStatement("UPDATE `caravanmanager`.`rentals` SET `CaravanName`=?, `RenteeName`=?, `RenteeAddress`=?, `RenteeEmail`=?, `RenteePhone`=?, `RentalStart`=?, `RentalEnd`=?, `RentalInSeason`=?, `RentalFee`=?, `RentalPaid`=? WHERE `RentalID`=?;");
				
				statement.setInt(11, Integer.parseInt(rentalIDText.getText()));
				statement.setString(1, caravanNameText.getText());
				statement.setString(2, renteeNameText.getText());
				statement.setString(3, renteeAddressText.getText());
				statement.setString(4, renteeEmailText.getText());
				statement.setString(5, renteePhoneText.getText());
				statement.setString(6, rentalStartText.getText());
				statement.setString(7, rentalEndText.getText());
				statement.setBoolean(8, Boolean.valueOf(rentalInSeasonText.getSelectedItem().toString()));
				statement.setString(9, rentalFeeText.getText());
				statement.setBoolean(10, Boolean.valueOf(rentalPaidText.getSelectedItem().toString()));
				int worked = statement.executeUpdate();
				log.debug("Worked = " + worked);
				if (worked == 1){
					JOptionPane.showMessageDialog(frame, "Record Updated.", "Result", JOptionPane.INFORMATION_MESSAGE);
					clearTextBoxes();
				}else{
					JOptionPane.showMessageDialog(frame, "Failed to update record!", "Result", JOptionPane.ERROR_MESSAGE);
				}
				connector.disconnect();
				log.debug("Row updated");
			} catch (ConnectionException e) {
				log.error(e.getMessage(), e);
			} catch (SQLException e) {
				log.severe("Failed to update record: " + e.getMessage());
				log.severe("SQLState: " + e.getSQLState());
				log.severe("Error Code: " + e.getErrorCode());
				log.error(e.getMessage(), e);
			} catch (IllegalStateException e){
				log.severe("Failed to update record: " + e.getMessage());
				log.severe("This is likely to be caused by revokation of access to the database.");
				log.severe("Error cause: " + e.getCause());
				log.severe("Cause Stack Trace: ");
				e.getCause().printStackTrace();
				log.error(e.getMessage(), e);
			} catch (Exception e) {
				log.severe("Failed to update record: " + e.getMessage());
				log.error(e.getMessage(), e);
			}finally{
				try{
					connector.disconnect();
				}catch(Exception e){
					log.warning("Couldn't disconnect from database.");
				}
			}
		}else{
			
		}
	}
	
	private void deleteRecord(){
		if (lastSelectedIndex == -1){
			JOptionPane.showMessageDialog(frame, "You need to select a record from the list.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (true){
			log.debug("Deleting record in Rentals...");
			DatabaseConnector connector = new DatabaseConnector("jdbc:mysql://origin.mcviral.net:3306/", log);
			connector.setupConnection("norreena", "4jeRUbWHUJHaWMXj");
			try {
				connector.connect();
				Connection connection = connector.getConnection();
				PreparedStatement statement = (PreparedStatement) connection.prepareStatement("DELETE FROM `caravanmanager`.`rentals` WHERE `RentalID`=?;");
				statement.setInt(1, Integer.parseInt(rentalIDModel.get(this.lastSelectedIndex)));
				int worked = statement.executeUpdate();
				log.debug("Worked = " + worked);
				if (worked == 1){
					JOptionPane.showMessageDialog(frame, "Record Deleted.", "Result", JOptionPane.INFORMATION_MESSAGE);
					update();
				}else{
					JOptionPane.showMessageDialog(frame, "Failed to delete record!", "Result", JOptionPane.ERROR_MESSAGE);
				}
				connector.disconnect();
				log.debug("Row deleted");
			} catch (ConnectionException e) {
				log.error(e.getMessage(), e);
			} catch (SQLException e) {
				log.severe("Failed to delete record: " + e.getMessage());
				log.severe("SQLState: " + e.getSQLState());
				log.severe("Error Code: " + e.getErrorCode());
				log.error(e.getMessage(), e);
			} catch (IllegalStateException e){
				log.severe("Failed to delete record: " + e.getMessage());
				log.severe("This is likely to be caused by revokation of access to the database.");
				log.severe("Error cause: " + e.getCause());
				log.severe("Cause Stack Trace: ");
				e.getCause().printStackTrace();
				log.error(e.getMessage(), e);
			} catch (Exception e) {
				log.severe("Failed to delete record: " + e.getMessage());
				log.error(e.getMessage(), e);
			}finally{
				try{
					connector.disconnect();
				}catch(Exception e){
					log.warning("Couldn't disconnect from database.");
				}
			}
		}
	}
	
	public void copyLast(){
		
	}
	
	public void copyToClipboard(String content){
		StringSelection stringSelection = new StringSelection(content);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}
	
	//Main
	public void close(){
		this.frame.dispose();
	}
	
}
