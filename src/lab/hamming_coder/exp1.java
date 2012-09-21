//import java.applet.Applet;
//import java.util.*;
//import java.util.PropertyPermission.*;
import java.awt.*;
//import java.awt.Color.*;
//import java.awt.MediaTracker.*;
import java.awt.event.*;
//import java.text.*;
//import java.awt.datatransfer.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.*;
//import java.net.URLEncoder.*;
import java.io.*;
//import java.io.File.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
//import javax.swing.tree.*;
//import javax.swing.table.*;
//import javax.swing.ImageIcon.*;


/*
import java.applet.Applet;
import java.util.*;
//import java.util.PropertyPermission.*;
import java.awt.*;
import java.awt.Color.*;
import java.awt.MediaTracker.*;
import java.awt.event.*;
import java.text.*;
import java.awt.datatransfer.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.*;
import java.net.URLEncoder.*;
import java.io.*;
import java.io.File.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.ImageIcon.*;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
*/

public class exp1 extends JApplet
{

	public int number=8,s=0,img1=0,ww=15,hh=15,flag=0,flag1=0,flag_order=0,set_output=0;
        public int a0=0,a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0,a9=0,a10=0,a11=0,a12=0,a13=0,a14=0,a15=0;
        int [] coeff = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public JFrame frame, frame1;
	public  void init()
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				Panel myPane1 = new Panel();
				setSize( 1400, 650 );
				setContentPane(myPane1);
				JButton manButton = new JButton("MANUAL CIRCUIT");
				JButton autoButton = new JButton("VERILOG CODING");
				JPanel labelPane = new JPanel(new GridLayout(1,1));
				//		JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 50 , 5 )) ;
						add(labelPane, BorderLayout.CENTER);
						labelPane.add(manButton);
						labelPane.add(autoButton);
						
				manButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent se){
						createAndShowGUI();							
						}
				});
				
				autoButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent se){
						verilogEditor();
						}
				});
			}
		});
	}
	public void verilogEditor() {
		JPanel cp = new JPanel();
		add(cp, BorderLayout.CENTER);
		JLabel head = new JLabel ( "<html><FONT COLOR=black SIZE=7 ><B>VERILOG DESIGN CODE EDITOR</B></FONT><br><br></html>", JLabel.CENTER);
		 head.setBorder(BorderFactory.createRaisedBevelBorder( ));
		 cp.add(head,BorderLayout.CENTER);
		final JTextArea t = new JTextArea(35,120);	
		 Border brd = BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK);
		 t.setBorder(brd);
		 head.setBorder(brd);
		 cp.add(t);
		 JScrollPane scrollPane = new JScrollPane(t);
		 cp.add(scrollPane, BorderLayout.CENTER);
		 JButton s= new JButton("NEXT");
		 cp.add(s);
		 setSize(1400,650);
		 setContentPane(cp);
		 s.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent as){
				 try	{
					 String str = t.getText(); 
					 if(str.length()==0) {
			            JOptionPane.showMessageDialog(null, "File is empty", "ERROR MESSAGE", 1);
					//	verilogTestBenchEditor();
					 }
					 else{
	
						File f = new File("lfsr.v");
						FileWriter fw = new FileWriter(f);
						fw.write(str);
						fw.close();
						JOptionPane.showMessageDialog(null, "VERILOG File is saved", "MESSAGE", 1);
					//	verilogTestBenchEditor();
						File fr = new File("lfsr.v");
						BufferedReader freader = new BufferedReader(new FileReader(fr));
						String s;
						int flag=0,flag1=0,flagc=0,flag2=0;
						
						String[] deg=null;
						while ((s = freader.readLine()) != null) {

						String[] st = s.split(" ");
						for(int i=0;i<st.length-1;i++)
						{
							if(st[i].equalsIgnoreCase("module"))
							{
								flag=1;
								break;
							}
						}
						if(flag==1)
						{
							for(int i=0;i<st.length-1;i++)
							{
								if(st[i].equalsIgnoreCase("output"))
								{
										flag2=1;
										break;
								}
							}
						}
						if(flag2==1)
						{
							for(int i=0;i<st.length-1;i++)
							{
								if(st[i].equalsIgnoreCase("input"))
								{
										flag1=1;
										break;
								}
							}
						}
						if(flag1==1)
						{
							for(int i=0; i< st.length;i++)
							{
								if(st[i].equals("^"))
								{
									flagc=1;
									break;
								}
								
							}
							if(flagc == 1)
								break;
						}
						}
						int degree =0;
					/*	if(deg[0].length() > 0)
						{
							System.out.println("Degree"+deg[0].charAt(1));
							degree = deg[0].charAt(1) - 47;
							System.out.println(degree + " "+ flagc+ " " +flag1);
						}
						else
							System.out.println("Code Not Correct");
					*/	if(flagc == 1 && flag1 == 1)
						{
							System.out.println("Code is Correct");
							ImageIcon xicon ;
							java.net.URL imgURL = getClass().getResource("circuit.png");
							if (imgURL != null)
							{
								xicon =  new ImageIcon(imgURL);
							}
							else
							{
								System.err.println("Couldn't find file: 1" );
								xicon =  null;
							}
							JOptionPane.showMessageDialog(null, null, "Structural Level Diagram", JOptionPane.OK_OPTION, xicon);
							
							java.net.URL imgURL1 = getClass().getResource("schematic.png");
							if (imgURL1 != null)
							{
								xicon =  new ImageIcon(imgURL1);
							}
							else
							{
								System.err.println("Couldn't find file: 2" );
								xicon =  null;
							}
							JOptionPane.showMessageDialog(null, null, "Schematic Level Diagram", JOptionPane.OK_OPTION, xicon);
							
							JPanel lPanel = new JPanel() ;
							add(lPanel, BorderLayout.CENTER);
							lPanel.setLayout(new BorderLayout()); 
							lPanel.setBackground(Color.gray);
							//FormattedTextFieldDemo inp = new FormattedTextFieldDemo();
			frame = new JFrame("8-4 Hamming Encoder");
			frame.add(new FormattedTextField());
			frame.pack();
				frame.setVisible(true);
					
					
					}
					else
						JOptionPane.showMessageDialog(null, "Code Not Correct","Try Again", JOptionPane.OK_OPTION);
						
					 }
					}
					catch(IOException ioe) {
						System.out.println("Exception Caught : " +ioe.getMessage());
					}
			}
		 });
	}
		public class FormattedTextField extends JPanel
			implements PropertyChangeListener {
		
				//Labels to identify the fields
				private JLabel sig_1,sig_2;

				//Strings for the labels
				private String sig1="Input data - 8 bit", sig2="Code Word - 12 bit";

				//Fields for data entry
				private JFormattedTextField x0f,x1f,x2f,x3f,x4f,x5f,x6f,x7f,x8f,x9f,x10f,x11f,x12f,x13f,x14f,x15f,x16f,x17f,x18f,x19f;

				//Coefficients
				private int x0c,x1c,x2c,x3c,x4c,x5c,x6c,x7c,x8c,x9c,x10c,x11c,x12c,x13c,x14c,x15c,x16c,x17c,x18c,x19c;

				public FormattedTextField() {
					super(new BorderLayout(10,30));

					//Create the labels.
					sig_1 = new JLabel(sig1);
					sig_2 = new JLabel(sig2);

					//Create the text fields and set them up.
					try{
						x0f = new JFormattedTextField(new MaskFormatter("*"));
						x0f.setValue(new Integer(0));
						x0f.setColumns(1);
						x0f.addPropertyChangeListener("value", this);
						
						x1f = new JFormattedTextField(new MaskFormatter("*"));
						x1f.setValue(new Integer(0));
						x1f.setColumns(1);
						x1f.addPropertyChangeListener("value", this);
						
						x2f = new JFormattedTextField(new MaskFormatter("*"));
						x2f.setValue(new Integer(0));
						x2f.setColumns(1);
						x2f.addPropertyChangeListener("value", this);
						
						x3f = new JFormattedTextField(new MaskFormatter("*"));
						x3f.setValue(new Integer(0));
						x3f.setColumns(1);
						x3f.addPropertyChangeListener("value", this);
						
						x4f = new JFormattedTextField(new MaskFormatter("*"));
						x4f.setValue(new Integer(0));
						x4f.setColumns(1);
						x4f.addPropertyChangeListener("value", this);
						
						x5f = new JFormattedTextField(new MaskFormatter("*"));
						x5f.setValue(new Integer(0));
						x5f.setColumns(1);
						x5f.addPropertyChangeListener("value", this);
						
						x6f = new JFormattedTextField(new MaskFormatter("*"));
						x6f.setValue(new Integer(0));
						x6f.setColumns(1);
						x6f.addPropertyChangeListener("value", this);
						
						x7f = new JFormattedTextField(new MaskFormatter("*"));
						x7f.setValue(new Integer(0));
						x7f.setColumns(1);
						x7f.addPropertyChangeListener("value", this);
						
						x8f = new JFormattedTextField(new MaskFormatter("*"));
						x8f.setValue(new Integer(0));
						x8f.setColumns(1);
						x8f.setEditable(false);
						x8f.setForeground(Color.red);
						x8f.addPropertyChangeListener("value", this);
						
						x9f = new JFormattedTextField(new MaskFormatter("*"));
						x9f.setValue(new Integer(0));
						x9f.setColumns(1);
						x9f.setEditable(false);
						x9f.setForeground(Color.red);
						x9f.addPropertyChangeListener("value", this);
						
						x10f = new JFormattedTextField(new MaskFormatter("*"));
						x10f.setValue(new Integer(0));
						x10f.setColumns(1);
						x10f.setEditable(false);
						x10f.setForeground(Color.red);
						x10f.addPropertyChangeListener("value", this);
						
						x11f = new JFormattedTextField(new MaskFormatter("*"));
						x11f.setValue(new Integer(0));
						x11f.setColumns(1);
						x11f.setEditable(false);
						x11f.setForeground(Color.red);
						x11f.addPropertyChangeListener("value", this);
						
						x12f = new JFormattedTextField(new MaskFormatter("*"));
						x12f.setValue(new Integer(0));
						x12f.setColumns(1);
						x12f.setEditable(false);
						x12f.setForeground(Color.red);
						x12f.addPropertyChangeListener("value", this);
						
						x13f = new JFormattedTextField(new MaskFormatter("*"));
						x13f.setValue(new Integer(0));
						x13f.setColumns(1);
						x13f.setEditable(false);
						x13f.setForeground(Color.red);
						x13f.addPropertyChangeListener("value", this);
						
						x14f = new JFormattedTextField(new MaskFormatter("*"));
						x14f.setValue(new Integer(0));
						x14f.setColumns(1);
						x14f.setEditable(false);
						x14f.setForeground(Color.red);
						x14f.addPropertyChangeListener("value", this);
						
						x15f = new JFormattedTextField(new MaskFormatter("*"));
						x15f.setValue(new Integer(0));
						x15f.setColumns(1);
						x15f.setEditable(false);
						x15f.setForeground(Color.red);
						x15f.addPropertyChangeListener("value", this);

						x16f = new JFormattedTextField(new MaskFormatter("*"));
						x16f.setValue(new Integer(0));
						x16f.setColumns(1);
						x16f.setEditable(false);
						x16f.setForeground(Color.red);
						x16f.addPropertyChangeListener("value", this);

						x17f = new JFormattedTextField(new MaskFormatter("*"));
						x17f.setValue(new Integer(0));
						x17f.setColumns(1);
						x17f.setEditable(false);
						x17f.setForeground(Color.red);
						x17f.addPropertyChangeListener("value", this);

						x18f = new JFormattedTextField(new MaskFormatter("*"));
						x18f.setValue(new Integer(0));
						x18f.setColumns(1);
						x18f.setEditable(false);
						x18f.setForeground(Color.red);
						x18f.addPropertyChangeListener("value", this);

						x19f = new JFormattedTextField(new MaskFormatter("*"));
						x19f.setValue(new Integer(0));
						x19f.setColumns(1);
						x19f.setEditable(false);
						x19f.setForeground(Color.red);
						x19f.addPropertyChangeListener("value", this);
					}
					catch(Exception e){
					}
	
Container pane = frame.getContentPane();				
			
JButton button1,button;
//JLabel label1, label2;

	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	
//	label1 = new JLabel("Input Data - 8 bits");
	c.weightx = 1.0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
//c.ipady = 60;
	c.insets = new Insets(50,50,0,0);  //top padding
	pane.add(sig_1, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 1;
	c.gridy = 0;
//c.ipady = 0;
	pane.add(x7f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(x6f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 3;
	c.gridy = 0;
	pane.add(x5f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 4;
	c.gridy = 0;
	pane.add(x4f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 5;
	c.gridy = 0;
	pane.add(x3f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 6;
	c.gridy = 0;
	pane.add(x2f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 7;
	c.gridy = 0;
	pane.add(x1f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 8;
	c.gridy = 0;
	pane.add(x0f, c);

	button1 = new JButton("Submit");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 0;
	c.gridy = 1;
	c.insets = new Insets(50,50,0,0);  //top padding
	pane.add(button1, c);

	c.weightx = 1.0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 2;
	c.insets = new Insets(50,50,50,0);  //top padding
	pane.add(sig_2, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 1;
	c.gridy = 2;
	pane.add(x19f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 2;
	c.gridy = 2;
	pane.add(x18f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(x17f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 4;
	c.gridy = 2;
	pane.add(x16f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 5;
	c.gridy = 2;
	pane.add(x15f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 6;
	c.gridy = 2;
	pane.add(x14f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 7;
	c.gridy = 2;
	pane.add(x13f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 8;
	c.gridy = 2;
	pane.add(x12f, c);
	
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 9;
	c.gridy = 2;
	pane.add(x11f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 10;
	c.gridy = 2;
	pane.add(x10f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 11;
	c.gridy = 2;
	pane.add(x9f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 12;
	c.gridy = 2;
c.insets = new Insets(0,50,0,50);  //top padding
	
	pane.add(x8f, c);


/*	button = new JButton("Long-Named Button 4");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 40;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(button, c);

	button = new JButton("5");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 0;       //reset to default
	c.weighty = 1.0;   //request any extra vertical space
	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
	c.insets = new Insets(10,0,0,0);  //top padding
	c.gridx = 1;       //aligned with button 2
	c.gridwidth = 2;   //2 columns wide
	c.gridy = 2;       //third row
	pane.add(button, c);
*/

/*					JButton button1 = new JButton("Submit");
					//button1.setPreferredSize(new Dimension(10,10));
					JPanel buttonPane = new JPanel(new GridLayout(1,1));
					buttonPane.add(button1);
					
					JPanel labelPane = new JPanel(new GridLayout(1,0));
					labelPane.add(sig_1);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					//labelPane.add(button1);

					JPanel labelPane1 = new JPanel(new GridLayout(1,0));
					labelPane1.add(sig_2);
					labelPane1.add(x19f);
					labelPane1.add(x18f);
					labelPane1.add(x17f);
					labelPane1.add(x16f);
					labelPane1.add(x15f);
					labelPane1.add(x14f);
					labelPane1.add(x13f);
					labelPane1.add(x12f);
					labelPane1.add(x11f);
					labelPane1.add(x10f);
					labelPane1.add(x9f);
					labelPane1.add(x8f);
					
					setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
					add(labelPane, BorderLayout.NORTH);
					add(buttonPane, BorderLayout.CENTER);
					add(labelPane1, BorderLayout.SOUTH);
*/

					button1.addActionListener(new ActionListener(){

							public void actionPerformed(ActionEvent ae){
							a3=x7c;a5=x6c;a6=x5c;a7=x4c;a9=x3c;a10=x2c;a11=x1c;a12=x0c;
							a4=0;a8=0;a2=0;a1=0;

							a1=((a3^a5)^(a7^a9))^a11;
							a2=((a3^a5)^(a7^a10))^a11;
							a4=(a5^a6)^(a7^a12);
							a8=(a9^a10)^(a11^a12);

							x19f.setValue(new Integer(a1));
							x18f.setValue(new Integer(a2));
							x17f.setValue(new Integer(a3));
							x16f.setValue(new Integer(a4));
							x15f.setValue(new Integer(a5));
							x14f.setValue(new Integer(a6));
							x13f.setValue(new Integer(a7));
							x12f.setValue(new Integer(a8));
							x11f.setValue(new Integer(a9));
							x10f.setValue(new Integer(a10));
							x9f.setValue(new Integer(a11));
							x8f.setValue(new Integer(a12));
							
							}

					});
				}
				public void propertyChange(PropertyChangeEvent e) {
					Object source = e.getSource();
					if (source == x0f) {
						x0c = ((Number)x0f.getValue()).intValue();
						if(x0c >1)
						{
							x0f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x1f) {
						x1c = ((Number)x1f.getValue()).intValue();
						if(x1c >1)
						{
							x1f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x2f) {
						x2c = ((Number)x2f.getValue()).intValue();
						if(x2c >1)
						{
							x2f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x3f) {
						x3c = ((Number)x3f.getValue()).intValue();
						if(x3c >1)
						{
							x3f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x4f) {
						x4c = ((Number)x4f.getValue()).intValue();
						if(x4c >1)
						{
							x4f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x5f) {
						x5c = ((Number)x5f.getValue()).intValue();
						if(x5c >1)
						{
							x5f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x6f) {
						x6c = ((Number)x6f.getValue()).intValue();
						if(x6c >1)
						{
							x6f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x7f) {
						x7c = ((Number)x7f.getValue()).intValue();
						if(x7c >1)
						{
							x7f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					}
				}
			}
	private void verilogTestBenchEditor() {
		JPanel cp = new JPanel();
		add(cp, BorderLayout.CENTER);
		JLabel head = new JLabel ( "<html><FONT COLOR=black SIZE=7 ><B>VERILOG TEST BENCH CODE EDITOR</B></FONT><br><br></html>", JLabel.CENTER);
		 head.setBorder(BorderFactory.createRaisedBevelBorder( ));
		 cp.add(head,BorderLayout.CENTER);
		final JTextArea t = new JTextArea(35,120);	
		 Border brd = BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK);
		 t.setBorder(brd);
		 head.setBorder(brd);
		 cp.add(t);
		 JScrollPane scrollPane = new JScrollPane(t);
		 cp.add(scrollPane, BorderLayout.CENTER);
		 JButton s= new JButton("RUN");
		 cp.add(s);
		 setSize(1400,650);
		 setContentPane(cp);
		 s.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent as){
				 try	{
					 String str = t.getText(); 
					 if(str.length()==0) {
			            JOptionPane.showMessageDialog(null, "File is empty", "ERROR MESSAGE", 1);
					/*	try 
						{ 
							Process p=Runtime.getRuntime().exec("iverilog -o dsn lfsr_tb.v lfsr.v"); 
							p.waitFor();
							Process pp=Runtime.getRuntime().exec("chmod 777 dsn"); 
							pp.waitFor();
							Process p1=Runtime.getRuntime().exec("vvp dsn"); 
							p1.waitFor();
							Process p1p=Runtime.getRuntime().exec("chmod 777 test.vcd"); 
							p1p.waitFor();
							Process p2=Runtime.getRuntime().exec("gtkwave test.vcd"); 
							p2.waitFor();
						}
						catch(Exception et)
						{
							System.out.println("Exception Caught : " + et.getMessage());
							}
							*/
					 }
					 else{
	
						File f = new File("lfsr_tb.v");
						FileWriter fw = new FileWriter(f);
						fw.write(str);
						fw.close();
						JOptionPane.showMessageDialog(null, "TESTBENCH File is saved", "MESSAGE", 1);
						try 
						{ 
							Process p=Runtime.getRuntime().exec("iverilog -o dsn lfsr_tb.v lfsr.v"); 
							p.waitFor();
							Process p1=Runtime.getRuntime().exec("vvp dsn"); 
							p1.waitFor();
							Process p2=Runtime.getRuntime().exec("gtkwave test.vcd"); 
							p2.waitFor();
						}
						catch(Exception et)
						{
							System.out.println("Exception Caught : " + et.getMessage());
						}
					}
						
				 }
				catch(IOException ioz) {
						System.out.println("Exception Caught : " +ioz.getMessage());
					}
			}
		 });
	}
	private  void createAndShowGUI() {

		MyPanel myPane = new MyPanel();
		myPane.setOpaque(true);
		setContentPane(myPane);
	}
	public class MyPanel  extends JPanel  implements ActionListener//MouseListener,MouseMotionListener
	{
		public class FormattedTextFieldDemo extends JPanel
			implements PropertyChangeListener {
		
				//Labels to identify the fields
				private JLabel sig_1,sig_2;

				//Strings for the labels
				private String sig1="Input data - 8 bit", sig2="Code Word - 12 bit";

				//Fields for data entry
				private JFormattedTextField x0f,x1f,x2f,x3f,x4f,x5f,x6f,x7f,x8f,x9f,x10f,x11f,x12f,x13f,x14f,x15f,x16f,x17f,x18f,x19f;

				//Coefficients
				private int x0c,x1c,x2c,x3c,x4c,x5c,x6c,x7c,x8c,x9c,x10c,x11c,x12c,x13c,x14c,x15c,x16c,x17c,x18c,x19c;

				public FormattedTextFieldDemo() {
					super(new BorderLayout(10,30));

					//Create the labels.
					sig_1 = new JLabel(sig1);
					sig_2 = new JLabel(sig2);

					//Create the text fields and set them up.
					try{
						x0f = new JFormattedTextField(new MaskFormatter("*"));
						x0f.setValue(new Integer(0));
						x0f.setColumns(1);
						x0f.addPropertyChangeListener("value", this);
						
						x1f = new JFormattedTextField(new MaskFormatter("*"));
						x1f.setValue(new Integer(0));
						x1f.setColumns(1);
						x1f.addPropertyChangeListener("value", this);
						
						x2f = new JFormattedTextField(new MaskFormatter("*"));
						x2f.setValue(new Integer(0));
						x2f.setColumns(1);
						x2f.addPropertyChangeListener("value", this);
						
						x3f = new JFormattedTextField(new MaskFormatter("*"));
						x3f.setValue(new Integer(0));
						x3f.setColumns(1);
						x3f.addPropertyChangeListener("value", this);
						
						x4f = new JFormattedTextField(new MaskFormatter("*"));
						x4f.setValue(new Integer(0));
						x4f.setColumns(1);
						x4f.addPropertyChangeListener("value", this);
						
						x5f = new JFormattedTextField(new MaskFormatter("*"));
						x5f.setValue(new Integer(0));
						x5f.setColumns(1);
						x5f.addPropertyChangeListener("value", this);
						
						x6f = new JFormattedTextField(new MaskFormatter("*"));
						x6f.setValue(new Integer(0));
						x6f.setColumns(1);
						x6f.addPropertyChangeListener("value", this);
						
						x7f = new JFormattedTextField(new MaskFormatter("*"));
						x7f.setValue(new Integer(0));
						x7f.setColumns(1);
						x7f.addPropertyChangeListener("value", this);
						
						x8f = new JFormattedTextField(new MaskFormatter("*"));
						x8f.setValue(new Integer(0));
						x8f.setColumns(1);
						x8f.setEditable(false);
						x8f.setForeground(Color.red);
						x8f.addPropertyChangeListener("value", this);
						
						x9f = new JFormattedTextField(new MaskFormatter("*"));
						x9f.setValue(new Integer(0));
						x9f.setColumns(1);
						x9f.setEditable(false);
						x9f.setForeground(Color.red);
						x9f.addPropertyChangeListener("value", this);
						
						x10f = new JFormattedTextField(new MaskFormatter("*"));
						x10f.setValue(new Integer(0));
						x10f.setColumns(1);
						x10f.setEditable(false);
						x10f.setForeground(Color.red);
						x10f.addPropertyChangeListener("value", this);
						
						x11f = new JFormattedTextField(new MaskFormatter("*"));
						x11f.setValue(new Integer(0));
						x11f.setColumns(1);
						x11f.setEditable(false);
						x11f.setForeground(Color.red);
						x11f.addPropertyChangeListener("value", this);
						
						x12f = new JFormattedTextField(new MaskFormatter("*"));
						x12f.setValue(new Integer(0));
						x12f.setColumns(1);
						x12f.setEditable(false);
						x12f.setForeground(Color.red);
						x12f.addPropertyChangeListener("value", this);
						
						x13f = new JFormattedTextField(new MaskFormatter("*"));
						x13f.setValue(new Integer(0));
						x13f.setColumns(1);
						x13f.setEditable(false);
						x13f.setForeground(Color.red);
						x13f.addPropertyChangeListener("value", this);
						
						x14f = new JFormattedTextField(new MaskFormatter("*"));
						x14f.setValue(new Integer(0));
						x14f.setColumns(1);
						x14f.setEditable(false);
						x14f.setForeground(Color.red);
						x14f.addPropertyChangeListener("value", this);
						
						x15f = new JFormattedTextField(new MaskFormatter("*"));
						x15f.setValue(new Integer(0));
						x15f.setColumns(1);
						x15f.setEditable(false);
						x15f.setForeground(Color.red);
						x15f.addPropertyChangeListener("value", this);

						x16f = new JFormattedTextField(new MaskFormatter("*"));
						x16f.setValue(new Integer(0));
						x16f.setColumns(1);
						x16f.setEditable(false);
						x16f.setForeground(Color.red);
						x16f.addPropertyChangeListener("value", this);

						x17f = new JFormattedTextField(new MaskFormatter("*"));
						x17f.setValue(new Integer(0));
						x17f.setColumns(1);
						x17f.setEditable(false);
						x17f.setForeground(Color.red);
						x17f.addPropertyChangeListener("value", this);

						x18f = new JFormattedTextField(new MaskFormatter("*"));
						x18f.setValue(new Integer(0));
						x18f.setColumns(1);
						x18f.setEditable(false);
						x18f.setForeground(Color.red);
						x18f.addPropertyChangeListener("value", this);

						x19f = new JFormattedTextField(new MaskFormatter("*"));
						x19f.setValue(new Integer(0));
						x19f.setColumns(1);
						x19f.setEditable(false);
						x19f.setForeground(Color.red);
						x19f.addPropertyChangeListener("value", this);
					}
					catch(Exception e){
					}
	
Container pane = frame.getContentPane();				
			
JButton button1,button;
//JLabel label1, label2;

	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	
//	label1 = new JLabel("Input Data - 8 bits");
	c.weightx = 1.0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
//c.ipady = 60;
	c.insets = new Insets(50,50,0,0);  //top padding
	pane.add(sig_1, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 1;
	c.gridy = 0;
//c.ipady = 0;
	pane.add(x7f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(x6f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 3;
	c.gridy = 0;
	pane.add(x5f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 4;
	c.gridy = 0;
	pane.add(x4f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 5;
	c.gridy = 0;
	pane.add(x3f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 6;
	c.gridy = 0;
	pane.add(x2f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 7;
	c.gridy = 0;
	pane.add(x1f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 8;
	c.gridy = 0;
	pane.add(x0f, c);

	button1 = new JButton("Submit");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 0;
	c.gridy = 1;
	c.insets = new Insets(50,50,0,0);  //top padding
	pane.add(button1, c);

	c.weightx = 1.0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 2;
	c.insets = new Insets(50,50,50,0);  //top padding
	pane.add(sig_2, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 1;
	c.gridy = 2;
	pane.add(x19f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 2;
	c.gridy = 2;
	pane.add(x18f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(x17f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 4;
	c.gridy = 2;
	pane.add(x16f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 5;
	c.gridy = 2;
	pane.add(x15f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 6;
	c.gridy = 2;
	pane.add(x14f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 7;
	c.gridy = 2;
	pane.add(x13f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 8;
	c.gridy = 2;
	pane.add(x12f, c);
	
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 9;
	c.gridy = 2;
	pane.add(x11f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 10;
	c.gridy = 2;
	pane.add(x10f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 11;
	c.gridy = 2;
	pane.add(x9f, c);

	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.2;
	c.gridx = 12;
	c.gridy = 2;
c.insets = new Insets(0,50,0,50);  //top padding
	
	pane.add(x8f, c);


/*	button = new JButton("Long-Named Button 4");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 40;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(button, c);

	button = new JButton("5");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 0;       //reset to default
	c.weighty = 1.0;   //request any extra vertical space
	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
	c.insets = new Insets(10,0,0,0);  //top padding
	c.gridx = 1;       //aligned with button 2
	c.gridwidth = 2;   //2 columns wide
	c.gridy = 2;       //third row
	pane.add(button, c);
*/

/*					JButton button1 = new JButton("Submit");
					//button1.setPreferredSize(new Dimension(10,10));
					JPanel buttonPane = new JPanel(new GridLayout(1,1));
					buttonPane.add(button1);
					
					JPanel labelPane = new JPanel(new GridLayout(1,0));
					labelPane.add(sig_1);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					//labelPane.add(button1);

					JPanel labelPane1 = new JPanel(new GridLayout(1,0));
					labelPane1.add(sig_2);
					labelPane1.add(x19f);
					labelPane1.add(x18f);
					labelPane1.add(x17f);
					labelPane1.add(x16f);
					labelPane1.add(x15f);
					labelPane1.add(x14f);
					labelPane1.add(x13f);
					labelPane1.add(x12f);
					labelPane1.add(x11f);
					labelPane1.add(x10f);
					labelPane1.add(x9f);
					labelPane1.add(x8f);
					
					setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
					add(labelPane, BorderLayout.NORTH);
					add(buttonPane, BorderLayout.CENTER);
					add(labelPane1, BorderLayout.SOUTH);
*/

					button1.addActionListener(new ActionListener(){

							public void actionPerformed(ActionEvent ae){
							a3=x7c;a5=x6c;a6=x5c;a7=x4c;a9=x3c;a10=x2c;a11=x1c;a12=x0c;
							a4=0;a8=0;a2=0;a1=0;

							a1=((a3^a5)^(a7^a9))^a11;
							a2=((a3^a5)^(a7^a10))^a11;
							a4=(a5^a6)^(a7^a12);
							a8=(a9^a10)^(a11^a12);

							x19f.setValue(new Integer(a1));
							x18f.setValue(new Integer(a2));
							x17f.setValue(new Integer(a3));
							x16f.setValue(new Integer(a4));
							x15f.setValue(new Integer(a5));
							x14f.setValue(new Integer(a6));
							x13f.setValue(new Integer(a7));
							x12f.setValue(new Integer(a8));
							x11f.setValue(new Integer(a9));
							x10f.setValue(new Integer(a10));
							x9f.setValue(new Integer(a11));
							x8f.setValue(new Integer(a12));
							
							}

					});
				}
				public void propertyChange(PropertyChangeEvent e) {
					Object source = e.getSource();
					if (source == x0f) {
						x0c = ((Number)x0f.getValue()).intValue();
						if(x0c >1)
						{
							x0f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x1f) {
						x1c = ((Number)x1f.getValue()).intValue();
						if(x1c >1)
						{
							x1f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x2f) {
						x2c = ((Number)x2f.getValue()).intValue();
						if(x2c >1)
						{
							x2f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x3f) {
						x3c = ((Number)x3f.getValue()).intValue();
						if(x3c >1)
						{
							x3f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x4f) {
						x4c = ((Number)x4f.getValue()).intValue();
						if(x4c >1)
						{
							x4f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x5f) {
						x5c = ((Number)x5f.getValue()).intValue();
						if(x5c >1)
						{
							x5f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x6f) {
						x6c = ((Number)x6f.getValue()).intValue();
						if(x6c >1)
						{
							x6f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					} else if (source == x7f) {
						x7c = ((Number)x7f.getValue()).intValue();
						if(x7c >1)
						{
							x7f.setValue(new Integer(0));
							flag=1;
							JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Binary", 1);
						}
					}
				}
			}

		
		int exp_type = 0 ;  
		/** Work Panel Variables ************************************************************************************************/
		double scale_x = 1 ;  // scaling of work pannel 
		double scale_y = 1 ;
		int xor_delete_count=0;
		int [] xor_delete=new int[200];
		int pmos_delete_count=0;
		int [] pmos_delete=new int[200];
		int nmos_delete_count=0;
		int [] nmos_delete=new int[200];
		int gnd_delete_count=0;
		int [] gnd_delete=new int[200];
		int input_delete_count=0;
		int [] input_delete=new int[200];
		int output_delete_count=0;
		int [] output_delete=new int[200];
		int vdd_delete_count=0;
		int [] vdd_delete=new int[200];

		int pmos_count=0;	
		int xor_count=0;	
		int nmos_count=0;	
		int gnd_count=0;	
		int input_count=0;	
		int output_count=0;	
		int vdd_count=0;

		int work_x ;
		int work_y ;
		int wire_button  = 0 ;// 0 not presed already , 1 -> already pressed 
		int img_button_pressed = -1 ;
		int simulate_button_pressed = -1;
		int draw_work = 0 ; // if 1 -> draw the image on work 

		int[][] work_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] end_points_mat ;   // 
		int[][] wire_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] wire_points_mat ;   // 

		int work_img_width = 50;
		int work_img_height = 50;
		int work_panel_width = 1400;
		int work_panel_height = 1400;

		int node_drag = -1 ; // it rep the index of comp_node which is selected to be draged 
		int wire_drag = -1 ; // it rep the index of wire which is selected to be extented from its end 
		int wire_drag_end = 1 ; // from which end it should be draged 
		int[] comp_count = new int[20] ; //  comp_count[i] represents the count of ( comp"i".jpg ) component ..
		int total_comp = 0 ;
		//              node[] comp_node = new node[20];
		node[] comp_node = new node[200];

		int total_wire = 0 ;
		line[] wire = new line[200];

		// Dialog Box -----------------------------------
		//              myDialog[] dialog = new myDialog[14]  ; //in this exp at max 6 comp can be used  (I assume that comp is used once )
		//              JFrame[] fr = new JFrame[14] ;
		String[] comp_str = {           // This will store what should at the Dialog Box for each component
			"This is shows which component is selected ." ,
			"XOR ", "Ground Terminal " ," Wire ",  // 1, 2 , 3
			"INPUT " ," ", " " , // 4 , 5 ,6

			"NMOS", "Capacitor" ,"Vdd ",  // 7 , 8 , 9 
			"OUTPUT",  " " ,"Inductor ",  // 10, 11 , 12
			"This is CMOS Chip No:1" ,"This is CMOS Chip No:1"};

		//*************************************************************************************************************************************
		//Circuit Component values which need to be send to ngspice ***************************************************************************

		String Pmos_l = "50n";
		String Pmos_w = "100n";

		String Nmos_l = "50n";
		String Nmos_w = "100n";

		String Capacitance = "100";
		String hori_len;
		String veri_len;
		//*************************************************************************************************************************************
		boolean simulate_flag = false ;
		Image img[] = new Image[20] ;
		ImageIcon icon[] = new ImageIcon[20] ;

		ImageIcon icon_simulate ;
		ImageIcon icon_graph ;

		MediaTracker mt ;



		URL base;
		JPanel topPanel = new JPanel () ;
		JButton simulate_button ;
		JButton graph_button ;
		JComboBox exp_list ;
		JButton layout_button ;

		JSplitPane splitPane ; // devides center pane into left and right panel 
		JPanel rightPanel = new JPanel();// = new exp1_graph();
		//      graph waveRightPanel = new graph() ;// = new exp1_graph();
		//graph waveRightPanel ;//= new graph() ;// = new exp1_graph();

		JPanel leftPanel = new JPanel() ;
		JSplitPane leftSplitPane ;  // divides left Panel into ( tool Panel ) and (work panel )...
		JPanel toolPanel = new JPanel ();
		JPanel toolPanelUp ;
		JButton selected ;
		JPanel toolPanelDown ;
		JToolBar leftTool1 = new JToolBar(1);
		JButton img_button1[] = new JButton[10] ;
		JToolBar leftTool2 = new JToolBar(1);
		JButton img_button2[] = new JButton[10] ;
		WorkPanel workPanel = new WorkPanel();

		int start=0;
		public class line
		{
			int x1 , y1 , x2 , y2 ; // end and start point of wire 
			int x[] = new int[200];
			int y[] = new int[200];
			int end_index ;
			boolean del ;
			public line (int a, int b , int c , int d)
			{
				x1 = a ;
				y1 = b ;
				x2 = c ;
				y2 = d ;
				del = false ;
				x[0] = a ; x[1] = c ;
				y[0] = b ; y[1] = d ;
				end_index = 1 ;
			}
			public void update2( int c , int d ) // update end point
			{
				x2 = c ;
				y2 = d ;
				//                              end_index++;
				x[end_index] = c;
				y[end_index] = d;
			}
			public void update1( int c , int d ) // update start point 
			{
				x[0] = x1 = c ;
				y[0] = y1 = d ;
			}
			public void update(int a , int b ) // update middle point 
			{
				end_index++;
				x[end_index] = a;
				y[end_index] = b ;
			}
			public void update_wire_mat(int index )
			{
				int i , j , tx1 , ty1 , tx2 , ty2 ; // local vriables 
				for ( int k = 0 ; k < end_index ; k ++)
				{
					tx1 = x[k] ;    tx2 = x[k + 1] ;
					ty1 = y[k] ;    ty2 = y[k + 1] ;
					for ( i = x1 ;  ;)
					{
						if ( tx2 >= tx1 && i >= tx2  ){break;}
						else if( tx2 <= tx1 && i <= tx2 ){break;}
						for ( j = ty1 - 4 ; j < ty1 + 5 ; j ++)
						{
							wire_mat[i][j] =  index ;   // update the matrix as the img is selected  
						}

						if ( tx2 > tx1 ){i++;}else{i--;}
					}
					for ( i = ty1 ;  ;)
					{
						if ( ty2 >= ty1 && i >= ty2  ){break;}
						else if( ty2 <= ty1 && i <= ty2 ){break;}
						for ( j = tx2 - 4 ; j < tx2 + 5 ; j ++)
						{
							wire_mat[j][i] =  index ;   // update the matrix as the img is selected  
						}

						if ( ty2 > ty1 ){i++;}else{i--;}
					}
				}

			}
			public void update_mat (int index)
			{
				for ( int i = x1 - 4 ; i < x1 +5; i ++ )
				{
					for ( int j = y1 - 4 ; j < y1 + 5; j ++ )
					{
						wire_points_mat[i][j] = index;
					}
				}
				for ( int i = x2 - 4 ; i < x2 +5; i ++ )
				{
					for ( int j = y2 - 4 ; j < y2 + 5; j ++ )
					{
						wire_points_mat[i][j] = index;
					}
				}
				update_wire_mat(index);
			}
			public void del()
			{
				update_mat(-1);
				del = true ;
			}
		}

		public MyPanel()
		{
			super(new BorderLayout());
			try // geting base URL address of this applet 
			{
				base = getDocumentBase();
			}
			catch( Exception e) {}

			/*			leftPanel.setLayout(new BorderLayout());
						leftPanel.setMinimumSize(new Dimension(1000,1100)); // for fixing size*/


			leftSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , toolPanel , workPanel); // spliting left in tool & work
			leftSplitPane.setOneTouchExpandable(true); // this for one touch option 
			leftSplitPane.setDividerLocation(0.2);
			//			leftPanel.add(leftSplitPane, BorderLayout.CENTER);
			add(leftSplitPane, BorderLayout.CENTER);
			int i;
			for ( i = 1 ; i <= 4 ; i ++ )
			{
				if(i==2 || i==1 || i==4)
					continue;
				java.net.URL imgURL = getClass().getResource("comp" + i + ".gif");
				if (imgURL != null)
				{
					icon[i] =  new ImageIcon(imgURL);
					img[i] =  getImage(imgURL);
				}
				else
				{
					System.err.println("Couldn't find file: " );
					icon[i] =  null;
				}


				img_button1[i] = new JButton ( icon[i] );
				img_button1[i].setOpaque(true);
				img_button1[i].setMargin(new Insets (0,0,0,0));
				img_button1[i].addActionListener(this);
				img_button1[i].setBackground(Color.white);
				img_button1[i].setToolTipText(comp_str[i]);// setting name for hovering of mouse

				leftTool1.add(img_button1[i]);
			}
			int j = 0 ;
			/*for ( i = 4 ; i < 5 ; i ++ )
			{
				j = 6 + i ; // for index setting 
				java.net.URL imgURL = getClass().getResource("comp" + j + ".gif");
				if (imgURL != null)
				{
					icon[j] =  new ImageIcon(imgURL);
					img[j] =  getImage(imgURL);
				}
				else
				{
					System.err.println("Couldn't find file: " );
					icon[j] =  null;
				}



				img_button2[i] = new JButton ( icon[j] );
				img_button2[i].setOpaque(true);
				img_button2[i].setMargin(new Insets (0,0,0,0));
				img_button2[i].addActionListener(this);
				img_button2[i].setBackground(Color.white);
				img_button2[i].setToolTipText(comp_str[j]); // setting name at hovering of mouse 

				leftTool2.add(img_button2[i]);
			}*/
			toolPanel.setLayout(new BorderLayout());


			//                      MySelected toolPanelUp = new MySelected();
			toolPanelUp = new JPanel();
			toolPanelDown = new JPanel();
			URL selected_URL = getClass().getResource("comp" + 0 + ".gif");
			if (selected_URL != null)
			{
				icon[0] =  new ImageIcon(selected_URL);
			}
			else
			{
				System.err.println("Couldn't find file: " );
				icon[0] =  null;
			}
			selected = new JButton(icon[0]);

			selected.setBackground(Color.white);
			// selected.setToolTipText(comp_str[0]); // setting name at hovering of mouse 

			toolPanel.add(toolPanelUp , BorderLayout.CENTER);
			toolPanel.add(toolPanelDown , BorderLayout.SOUTH);
			selected.setBorder(BorderFactory.createTitledBorder(" SELECTED ICON "));
			toolPanelDown.setBorder(BorderFactory.createTitledBorder(" AVALIABLE ICONS "));


			toolPanelUp.setLayout(new BorderLayout());
			toolPanelUp.add(selected, BorderLayout.NORTH);
			toolPanelUp.add(new JLabel("<html> <br/><html/>"), BorderLayout.SOUTH);

			toolPanelDown.add(leftTool1);
			toolPanelDown.add(leftTool2);

			leftTool1.setFloatable(false);
			leftTool2.setFloatable(false);
			/*rightPanel.setLayout(new BorderLayout()); 
			  rightPanel.setBackground(Color.gray); 

			  JLabel wave_head = new JLabel ( "<html><FONT COLOR=white SIZE=6 ><B>SIMULATION OF CIRCUIT</B></FONT><br><br></html>", JLabel.CENTER);
			  wave_head.setBorder(BorderFactory.createRaisedBevelBorder( ));


			  waveRightPanel = new graph() ;// = new exp1_graph();
			  rightPanel.add(waveRightPanel, BorderLayout.CENTER);
			  rightPanel.add(wave_head, BorderLayout.NORTH);



			  splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , rightPanel);
			  splitPane.setOneTouchExpandable(true); // this for one touch option 
			  splitPane.setDividerLocation(0.2);
			  add(splitPane, BorderLayout.CENTER);*/


			add(topPanel , BorderLayout.NORTH);
			topPanel.setBackground(Color.gray);
			JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 100 , 10 )) ;
			JLabel heading = new JLabel (  "<html><FONT COLOR=WHITE SIZE=18 ><B>8,4 HAMMING CODER </B></FONT></html>", JLabel.CENTER);
			heading.setBorder(BorderFactory.createEtchedBorder( Color.black , Color.white));


			topPanel.setLayout(new BorderLayout());

			topPanel.add(heading , BorderLayout.CENTER);
			topPanel.add(headButton , BorderLayout.SOUTH);
			java.net.URL imgURL = getClass().getResource("simulate1.png");
			java.net.URL imgURL2 = getClass().getResource("graph.gif");
			if (imgURL != null && imgURL2 != null)
			{
				icon_simulate =  new ImageIcon(imgURL);
				icon_graph =  new ImageIcon(imgURL2);
			}
			else
			{
				System.err.println("Couldn't find file: " );
				icon_simulate =  null;
				icon_graph =  null;
			}
			simulate_button = new JButton("Schematic Diagram" , icon_simulate );
			icon_simulate.setImageObserver(simulate_button);

			graph_button = new JButton (" Simulation" , icon_graph);
			icon_graph.setImageObserver(graph_button);
			simulate_button.setToolTipText("For Simulation");
			

			headButton.add(simulate_button );
			headButton.add(graph_button );
			//     headButton.add(exp_list );
			//   headButton.add(layout_button );


			simulate_button.addActionListener(this);
			graph_button.addActionListener(this);
			// exp_list.addActionListener(this);
			//layout_button.addActionListener(this);
			//-------------------------------------------------------------------------------------------
			//Setting Bottom Panel ========================================================================== 
			JPanel bottom = new JPanel(new FlowLayout());
			add(bottom , BorderLayout.SOUTH);
			JLabel h = new JLabel (  "<html><FONT COLOR=WHITE SIZE=12 ><B>THIS IS VLSI EXPERIMENT</B></FONT></html>", JLabel.CENTER);
			//                      bottom.add(h );

			//================================================================================================
			setBorder(BorderFactory.createLineBorder( Color.black));



		}

		public void change_selected (int no)
		{
			selected.setIcon(icon[no]);
		}
		public boolean circuit_check()
		{

			int check_points_value[][] = new int[54][1000] ; // each index will store corresponding  component for points of (comp point no -> index )
			int i = 0 , j , j1 = 0, k1 = 0 , j2 = 0 , k2 = 0 , l = 0 ;
			for (  i = 0 ; i < 54 ; i ++ )
			{
				for (  j = 0 ; j < 1000 ; j ++ )
				{
					check_points_value[i][j] = -1 ;
				}
			}
			System.out.println(total_wire);
			for (  i = 0 ; i < total_wire ; i ++ )
			{
				j1 = wire[i].x1;
				k1 = wire[i].y1;
				j2 = wire[i].x2;
				k2 = wire[i].y2;

				if (wire[i].del == false &&  end_points_mat[j1][k1] != -1 && end_points_mat[j2][k2] != -1 )
				{

					l = 0 ;
					while ( check_points_value[ end_points_mat[j1][k1] ][l] != -1 )
					{
						l++;
					}
					check_points_value[end_points_mat[j1][k1]][l] =  end_points_mat[j2][k2];

					l = 0 ;
					while ( check_points_value[ end_points_mat[j2][k2] ][l] != -1 )
					{
						l++;
					}
					check_points_value[end_points_mat[j2][k2]][l] =  end_points_mat[j1][k1];
				}


			}
			int temp ;
			for (  i = 0 ; i < 54 ; i ++ ) // for making all connected point for each end_point in its array ..
			{

				l = 0 ;
				while ( check_points_value[i][l] != -1 )
				{
					l++;
				}

				int r = 0 ;
				temp = check_points_value[i][r++];
				while ( temp != - 1 )
				{
					int k = 0 ;
					while ( check_points_value[temp][k] != - 1)
					{
						int flag = 0 ;
						for ( int m = 0 ; m < l ; m ++ )
						{
							if ( check_points_value[temp][k] == check_points_value[i][m] )
							{
								flag = 1 ;
								break ;
							}
						}
						if ( flag == 0 )
						{
							check_points_value[i][l++] = check_points_value[temp][k];
						}
						k++;
					}
					temp = check_points_value[i][r++]; // for each (element) value of i(th) element of array
				}
			}
			// checking 
			for (  i = 0 ; i < 54 ; i ++ )
			{
				//System.out.print("point " + i + " - ");
				l = 0 ;
				while ( check_points_value[i][l] != -1 && check_points_value[i][l]!=i )
				{
				//	System.out.print(" " +check_points_value[i][l]);
					l++;
				}
				//System.out.print("\n");
			}
			
			for (  i = 0 ; i < 54 ; i ++ )
			{
				if ( check_points_value[i][0] == -1 )
				{
					return false ; // if any end point of component is free then wrong circiut ...
				}
			}
			int local_endpoint,local_count_input=0,local_count_output=0;
			int[] flag = new int[10] ;
			for(int fl=0; fl<10; fl++)
				flag[fl] = 0;
			for(i=0;i < 12;i++)
			{
				l = 0 ;
				local_endpoint = check_points_value[i][l];
				if(local_endpoint == 42 )
					flag[0] = 1;
				else if(local_endpoint == 43)
					flag[1] = 1;
				else if(local_endpoint == 45)
					flag[2] = 1;
				else if(local_endpoint == 46)
					flag[3] = 1;
				else if(local_endpoint == 48)
					flag[4] = 1;
				else if(local_endpoint == 2)
					flag[5] = 1;
				else if(local_endpoint == 5)
					flag[6] = 1;
				else if(local_endpoint == 8)
					flag[7] = 1;
				if(local_endpoint == 50 )
				{
					local_count_output++;
				}
			}
			if(flag[0] == 0 || flag[1]==0 || flag[2]==0 || flag[3]==0 || flag[4]==0 || flag[5]==0 || flag[6]==0 || flag[7]==0 || local_count_output!=1)
			{
				System.out.println("1st row");
				return false;
			}
			
			for(int fl=0; fl<10; fl++)
				flag[fl] = 0;
			local_count_output = 0;
			for(i=12;i < 24;i++)
			{
				l = 0 ;
				local_endpoint = check_points_value[i][l];
				if(local_endpoint == 42 )
					flag[0] = 1;
				else if(local_endpoint == 44)
					flag[1] = 1;
				else if(local_endpoint == 45)
					flag[2] = 1;
				else if(local_endpoint == 47)
					flag[3] = 1;
				else if(local_endpoint == 48)
					flag[4] = 1;
				else if(local_endpoint == 14)
					flag[5] = 1;
				else if(local_endpoint == 17)
					flag[6] = 1;
				else if(local_endpoint == 20)
					flag[7] = 1;
				if(local_endpoint == 51 )
				{
					local_count_output++;
				}
			}
			if(flag[0] == 0 || flag[1]==0 || flag[2]==0 || flag[3]==0 || flag[4]==0 || flag[5]==0 || flag[6]==0 || flag[7]==0 || local_count_output!=1)
			{
				System.out.println("2 row");
				return false;
			}


			local_count_output = 0;
			for(int fl=0; fl<10; fl++)
				flag[fl] = 0;
			for(i=24;i < 33;i++)
			{
				l = 0 ;
				local_endpoint = check_points_value[i][l];
				if(local_endpoint == 43 )
					flag[0] = 1;
				else if(local_endpoint == 44)
					flag[1] = 1;
				else if(local_endpoint == 45)
					flag[2] = 1;
				else if(local_endpoint == 49)
					flag[3] = 1;
				else if(local_endpoint == 26)
					flag[4] = 1;
				else if(local_endpoint == 29)
					flag[5] = 1;
				if(local_endpoint == 52 )
				{
					local_count_output++;
				}
			}
			if(flag[0] == 0 || flag[1]==0 || flag[2]==0 || flag[3]==0 || flag[4]==0 || flag[5]==0 || local_count_output!=1)
			{
				System.out.println("3 row");
				return false;
			}

			local_count_output = 0;
			for(int fl=0; fl<10; fl++)
				flag[fl] = 0;
			for(i=33;i < 42;i++)
			{
				l = 0 ;
				local_endpoint = check_points_value[i][l];
				if(local_endpoint == 46 )
					flag[0] = 1;
				else if(local_endpoint == 47)
					flag[1] = 1;
				else if(local_endpoint == 48)
					flag[2] = 1;
				else if(local_endpoint == 49)
					flag[3] = 1;
				else if(local_endpoint == 35)
					flag[4] = 1;
				else if(local_endpoint == 38)
					flag[5] = 1;
				if(local_endpoint == 53 )
				{
					local_count_output++;
				}
			}
			if(flag[0] == 0 || flag[1]==0 || flag[2]==0 || flag[3]==0 || flag[4]==0 || flag[5]==0 || local_count_output!=1)
			{
				System.out.println("4 row");
				return false;
			}
			
			return true;
		}
		public void actionPerformed(ActionEvent e)
		{

			if(e.getSource() == simulate_button )
			{
				//System.out.println("Simulated1");

				//draw_circuit(g2d , 0  , 0, 15 , 0);
				if(simulate_button_pressed == -1)
				{
					if(circuit_check())
					{
					//	flag_order=1;
						img_button_pressed = -1;
						System.out.println("Simulated");
						simulate_button_pressed  =  1;
						workPanel.repaint();
						simulate_button.setText("Start Again");
						// waveRightPanel.make_graph("txt/outfile");// Read file OUTFILE and draw the 
						// waveRightPanel.repaint();
						// waveRightPanel.setVisible(true);
						// simulate_flag = true ;
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Circuit is not Complete , Please Complete it and press simulate again :)");
					}
				}
				else
				{
					simulate_button_pressed  =  -1;
					workPanel.repaint();
					/*	workPanel.repaint();
						waveRightPanel = new graph() ;
						waveRightPanel.make_graph("txt/null.txt");// Read file OUTFILE and draw the 
						waveRightPanel.repaint();
						waveRightPanel.setVisible(true);*/
					simulate_flag = false;
					simulate_button.setText("Schematic Diagram");
				}


			}
			if(e.getSource() ==  graph_button )
			{
				if(simulate_button_pressed == 1)
				{
			frame = new JFrame("8-4 Hamming Encoder");
			frame.add(new FormattedTextFieldDemo());
			frame.pack();
				frame.setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please press Schematic Diagram Button first and then press simulate again :)");
				}
			}

			if(simulate_button_pressed != 1)
			{

				if (e.getSource() == img_button1[1] )
				{
					img_button_pressed = 1 ;
					change_selected(1);
				}
				else if (e.getSource() == img_button1[2] )
				{
					img_button_pressed = 2 ;
					change_selected(2);
				}
				else if (e.getSource() == img_button1[3] ) // Wire :)
				{
					img_button_pressed = 3 ;
					change_selected(3);
				}
				else if (e.getSource() == img_button1[4] ) // Input 
				{
					img_button_pressed = 4 ;
					change_selected(4);
				}
				else if (e.getSource() == img_button2[4] ) //output
				{
					img_button_pressed = 10 ;
					change_selected(10);
				}
			}

		}
		public  class node
		{
			int node_x ;
			int node_y ;
			int img_no ;
			int width ;
			int height ;

			int virtual_w ;
			int virtual_h ;
			int node_number;
			double angle ;
			int angle_count ; // 0-> 0 / 360 degree , 1 -> 90 degree , 2 -> 180 degree , 3 -> 270 degree

			boolean del ;

			// for connection with wire 
			int end_pointsX[] = new int[5];
			int end_pointsY[] = new int[5];
			int count_end_points = 0 ;

			public node (int x , int y , int no , int w , int h,int number)
			{
				node_x = x ;
				node_y = y ;
				if(no==1)
				{
					int i;
					if(xor_delete_count!=0)
					{
						for(i=0;i<xor_delete_count;i++)
						{
							if(xor_delete[i]==-1)
								continue;
							node_number=xor_delete[i];
							xor_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=number;
					}
				}
				else if(no==4)
				{
					int i;
					if(input_delete_count!=0)
					{
						for(i=0;i<input_delete_count;i++)
						{
							if(input_delete[i]==-1)
								continue;
							node_number= input_delete[i];
							input_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=number;
					}
				}
				else if(no==2)         //For Or(Replace gnd with Or)
				{
					int i;
					if(gnd_delete_count!=0)
					{
						for(i=0;i<gnd_delete_count;i++)
						{
							if(gnd_delete[i]==-1)
								continue;
							node_number=gnd_delete[i];
							gnd_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=number;
					}
				}
				else if(no==10)
				{
					int i;
					if(output_delete_count!=0)
					{
						for(i=0;i<output_delete_count;i++)
						{
							if(output_delete[i]==-1)
								continue;
							node_number=output_delete[i];
							output_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=number;
					}
				}

				img_no = no ;
				del = false ;

				virtual_w = width = w ;
				virtual_h = height = h ;
				angle = 0 ;
				angle_count = 0 ;
				make_end_points(no);
			}
			public void rotate(int index )
			{
				remove_mat() ;// delete the previous value from work_mat
				//              angle = angle +  (java.lang.Math.PI/2);
				angle_count = (angle_count + 1 )% 4 ;
				angle = angle_count *  (java.lang.Math.PI/2);
				if ( (angle - 2* java.lang.Math.PI ) > .001 )
				{
					angle = 0 ;
					virtual_w = width ;
					virtual_h = height ;
					if(node_x+virtual_w < 0)
					{
						node_x=node_x-virtual_w;
					}
					if(node_y+virtual_h < 0)
					{
						node_y=node_y-virtual_h;
					}
				}
				if (angle_count == 0 ) // 90 degree
				{
					virtual_w = width;
					virtual_h = height  ;
					if(node_x+virtual_w < 0)
					{
						node_x=node_x-virtual_w;
					}
					if(node_y+virtual_h < 0)
					{
						node_y=node_y-virtual_h;
					}
				}
				else if (angle_count == 1 ) // 90 degree
				{
					virtual_w = -height;
					virtual_h = width  ;
					if(node_x+virtual_w < 0)
					{
						node_x=node_x-virtual_w;
					}
					if(node_y+virtual_h < 0)
					{
						node_y=node_y-virtual_h;
					}
				}
				else if (angle_count == 2 ) // 180 degree
				{
					virtual_w = -width ;
					virtual_h = -height ;
					if(node_x+virtual_w < 0)
					{
						node_x=node_x-virtual_w;
					}
					if(node_y+virtual_h < 0)
					{
						node_y=node_y-virtual_h;
					}

				}
				else if (angle_count == 3 ) // 270 degree
				{
					virtual_w = height ;
					virtual_h = -width ;
				}
				update_mat(index); // update the matrix value to work_mat // index is the index of the node_comp matrix
			}

			public void remove_mat() // delete the previous value from work_mat
			{
				int i,j;	
				for ( i = node_x ;  ;  )
				{
					if ( virtual_w > 0 && i >= node_x + virtual_w  ){break;}
					else if( virtual_w < 0 && i <= node_x + virtual_w  ){break;}
					for ( j = node_y ;  ;  )
					{
						if ( virtual_h > 0 && j >= node_y + virtual_h  ){break;}
						else if( virtual_h < 0 && j <= node_y + virtual_h  ){break;}

						work_mat[i][j] =  -1 ;   // update the matrix as the img is selected  
						if ( virtual_h > 0 ){j++;}else{j--;}
					}
					if ( virtual_w > 0 ){i++;}else{i--;}
				}
				remove_end_points();

			}
			public void remove_end_points()
			{
				for ( int k = 0 ; k < count_end_points ; k ++ )
				{
					for ( int i = end_pointsX[k] +8 ; i > end_pointsX[k] -7  &&i > 0 ; i -- )
					{
						for ( int j = end_pointsY[k] +8 ; j > end_pointsY[k] -7 && j>0; j -- )
						{
							end_points_mat[i][j] = -1;
						}
					}

				}
			}

			public void update_end_points_mat(int img)
			{
				if ( img == 2) 
				{

					for ( int k = 0 ; k < 4 ; k ++ )
					{
						for ( int i = end_pointsX[k] +8 ; i > end_pointsX[k] -7 && i>0; i -- )
						{
							for ( int j = end_pointsY[k] +8 ; j > end_pointsY[k] -7 && j>0; j -- )
							{
								end_points_mat[i][j] = (((node_number)*4))+k ;         //total no. of xor = 2
							}
						}
					}
				}
				else if ( img == 1)// xor
				{
					for ( int k = 0 ; k < 3 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8; i ++ )
						{
							for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] + 8; j ++ )
							{
								end_points_mat[i][j] = (node_number * 3) + k  ; //total number of xor = 14
							}
						}
					}
				}

				else if (  img== 4 || img == 10   ) // INPUT , OUTPUT
				{

					for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
					{
						for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
						{
							if ( img == 4 ) // input
							{
								end_points_mat[i][j] =  42 + node_number; //totalnumber of pmos and nmos are assumed to be 2
							}
							else if ( img == 10 ) //output
							{
								end_points_mat[i][j] =  50 + node_number;
							}
						}
					}
				}

			}
			public void make_end_points(int img )
			{
				/*if ( img == 1 || img == 7) // xor/C/NMOS 
				  {
				  count_end_points = 4;

				  int a , b , c , d , e , f ,g ,h;
				  a =  -width ; b = -width/4;c = a;d = -b ;e = -a  ;f = b ; g = -a ; h = -b; 
				//					a = width ; b= 0 ; c = width ;d = height ; e = 0 ; f = height / 2 ;

				if ( angle_count == 1 )
				{
				a = width/4 ; b= -width ; c = -a ; d = b ; e = a ; f = -b ;g = -a;h = -b;
				}
				else if ( angle_count == 2 )
				{
				a =  width ; b = width/4;c = a;d = -b ;e = -a  ;f = b ; g = -a ; h = -b; 
				}
				else if ( angle_count == 3 )
				{
				a = -width/4 ; b= width ; c = -a ; d = b ; e = a ; f = -b ;g = -a;h = -b;
				}
				end_pointsX[0] = node_x + a ;             //upper left
				end_pointsY[0] = node_y + b;

				end_pointsX[1] = node_x + c ;           //  lower left
				end_pointsY[1] = node_y + d ;

				end_pointsX[2] = node_x + e  ;             //upper right
				end_pointsY[2] = node_y +  f ;

				end_pointsX[3] = node_x + g  ;             //lower right
				end_pointsY[3] = node_y +  h ;
				}

				else if ( img == 9 ) ///terminal  Not required for this exp.
				{
				count_end_points = 3;
				int a , b ,c,d,e,f ;
				a = -width ;b = 0; c = -width  ;d = width  ;e=3*width ;f= width / 2;
				//                                          a = width / 2 ; b= 0 ;

				if ( angle_count == 1 )
				{
				a = 0;b = -width;c = -width;d = -width;e=-width/2;f = 3*width;
				}
				else if ( angle_count == 2 )
				{
				a = width ;b = 0; c = width  ;d = -width  ;e=-3*width ;f= -width / 2;
				}
				else if ( angle_count == 3 )
				{
				a = 0;b = width;c = width;d = width;e=width/2;f = -3*width;
				}
				end_pointsX[0] = node_x + a ;
				end_pointsY[0] = node_y + b;
				end_pointsX[1] = node_x + c ;
				end_pointsY[1] = node_y + d;
				end_pointsX[2] = node_x + e ;
				end_pointsY[2] = node_y + f;
				}
				else*/ if ( img == 1 ) //xor
				{
					count_end_points = 3;
					int a , b ,c,d,e,f ;
					a = 0 ;b = height / 2;c =0 ;d = height/2+height/4;e = height;f = height/2;
					if ( angle_count == 1 ){
						a= -height/2; b = 0; c= -height/2-height/4; d = 0; e = -height/2; f = height;
					}
					else if ( angle_count == 2 )
					{
						a = 0 ; b= -height/2 ; c = 0 ; d = -height/2-height/4 ; e = -height ; f = -height / 2 ;
					}
					else if ( angle_count == 3 )
					{
						a = height/2 ; b= 0 ; c = height/2+height/4 ; d = 0 ; e = height/2 ; f = -height ;
					}



					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;
					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d;
					end_pointsX[2] = node_x + e ;
					end_pointsY[2] = node_y + f;
				}
				else if ( img == 4 || img == 10 )
				{
					count_end_points = 1 ;
					if ( img == 4 )                 // INPUT
					{
						int a,b;
						a = 2*width;b=0;
						if ( angle_count == 1 )
						{
							a = 0;b=2*width;
						}
						else if ( angle_count == 2 )
						{
							a = -2*width;b=0;

						}
						else if ( angle_count == 3 )
						{
							a = 0;b= -2*width;
						}
						end_pointsX[0] = node_x + a;
						end_pointsY[0] = node_y  +b ;
					}
					else     // OUTPUT
					{
						end_pointsX[0] = node_x  ;
						end_pointsY[0] = node_y  ;
					}
				}

				update_end_points_mat(img);
			}
			public void update_mat(int index) // update the matrix value to work_mat // index is the index of the node_comp matrix
			{
				int i , j ;
				for ( i = node_x ;  ;)
				{
					if ( virtual_w >= 0 && i >= node_x + virtual_w  ){break;}
					else if( virtual_w <= 0 && i <= node_x + virtual_w  ){break;}

					for ( j = node_y ;  ;  )
					{
						if ( virtual_h >= 0 && j >= node_y + virtual_h  ){break;}
						else if( virtual_h <= 0 && j <= node_y + virtual_h  ){break;}

						work_mat[i][j] =  index ;   // update the matrix as the img is selected  
						if ( virtual_h >= 0 ){j++;}else{j--;}
					}

					if ( virtual_w >= 0 ){i++;}else{i--;}
				}


				make_end_points(img_no);

			}


		}


		public class myDialog extends JDialog implements ActionListener
		{
			JSpinner length ;
			JSpinner width;
			JSpinner capacitance;
			Container cp;
			JButton del ;
			JButton ok ;
			JButton rotate ;
			int node_index ;
			int image_no;
			public myDialog (JFrame fr , String comp, int img_no,int node_no)
			{
				super (fr , "Graph " , true ); // true to lock the main screen 
				if(img_no == -1)
				{
					cp = getContentPane();
					BorderLayout layout = new BorderLayout();
					cp.setLayout(layout);
					setSize(700 , 700);


					//waveRightPanel = new graph() ;
					//waveRightPanel.graph();
					//waveRightPanel.make_graph("txt/outfile");// Read file OUTFILE and draw the 
					//waveRightPanel.repaint();
					//waveRightPanel.setVisible(true);
					//cp.add(waveRightPanel,layout.CENTER);
					simulate_flag = true ;
				}
				else
				{

					node_index = node_no ;
					image_no=img_no;

					cp = getContentPane();
					SpringLayout layout = new SpringLayout();
					cp.setLayout(layout);
					setSize(350 , 200);
					if ( img_no == 3 || img_no==8 ) // capacitor 
					{
						SpinnerModel capacitance_model =        new SpinnerNumberModel(5, //initial value
								1, //min
								10, //max
								1);  //step
						JLabel comp_name = new JLabel("<html><foet size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);

						layout.putConstraint(SpringLayout.WEST , comp_name , 50,   SpringLayout.WEST , cp );
						layout.putConstraint(SpringLayout.NORTH , comp_name , 20,  SpringLayout.NORTH , cp);

						capacitance = new JSpinner(capacitance_model);
						JLabel c = new JLabel("Select the Length of the Wire :");
						//JLabel c_unit = new JLabel("m");

						del = new JButton("Delete Component");
						ok = new JButton("O.K");
						//		      rotate = new JButton("Rotate");

						layout.putConstraint(SpringLayout.WEST , c , 20,   SpringLayout.WEST , cp );
						layout.putConstraint(SpringLayout.NORTH , c ,60,  SpringLayout.NORTH , cp);

						layout.putConstraint(SpringLayout.WEST , capacitance , 20,   SpringLayout.EAST , c );
						layout.putConstraint(SpringLayout.NORTH , capacitance , 60,  SpringLayout.NORTH , cp);

						//layout.putConstraint(SpringLayout.WEST , c_unit , 10,   SpringLayout.EAST , capacitance );
						//layout.putConstraint(SpringLayout.NORTH , c_unit , 60,  SpringLayout.NORTH , cp);

						layout.putConstraint(SpringLayout.WEST , del , 20,   SpringLayout.WEST , cp );
						layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);

						//       layout.putConstraint(SpringLayout.WEST , rotate , 10,   SpringLayout.EAST , del);
						//      layout.putConstraint(SpringLayout.NORTH , rotate , 100,  SpringLayout.NORTH , cp);

						layout.putConstraint(SpringLayout.WEST , ok , 10,   SpringLayout.EAST , del );
						layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);


						cp.add(comp_name);
						cp.add(c);
						cp.add(capacitance);
						// cp.add(c_unit);
						cp.add(del);
						cp.add(ok);
						//  cp.add(rotate);
						ok.addActionListener(this);
						del.addActionListener(this);
						// rotate.addActionListener(this);
					}
					else
					{
						JLabel comp_name = new JLabel("<html><font size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);
						layout.putConstraint(SpringLayout.WEST , comp_name , 50,   SpringLayout.WEST , cp );
						layout.putConstraint(SpringLayout.NORTH , comp_name , 20,  SpringLayout.NORTH , cp);

						//                         capacitance = new JSpinner(capacitance_model);
						//                       JLabel c = new JLabel("Select the Length of the Wire :");
						//JLabel c_unit = new JLabel("m");

						del = new JButton("Delete Component");
						ok = new JButton("O.K");
						rotate = new JButton("Rotate");

						// layout.putConstraint(SpringLayout.WEST , c , 20,   SpringLayout.WEST , cp );
						// layout.putConstraint(SpringLayout.NORTH , c ,60,  SpringLayout.NORTH , cp);

						//                   layout.putConstraint(SpringLayout.WEST , capacitance , 20,   SpringLayout.EAST , c );
						//layout.putConstraint(SpringLayout.NORTH , capacitance , 60,  SpringLayout.NORTH , cp);

						//                 layout.putConstraint(SpringLayout.WEST , c_unit , 10,   SpringLayout.EAST , capacitance );
						//layout.putConstraint(SpringLayout.NORTH , c_unit , 60,  SpringLayout.NORTH , cp);

						layout.putConstraint(SpringLayout.WEST , del , 20,   SpringLayout.WEST , cp );
						layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);

						layout.putConstraint(SpringLayout.WEST , rotate , 10,   SpringLayout.EAST , del);
						layout.putConstraint(SpringLayout.NORTH , rotate , 100,  SpringLayout.NORTH , cp);
						layout.putConstraint(SpringLayout.WEST , ok , 10,   SpringLayout.EAST , rotate );
						layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);


						cp.add(comp_name);
						//               cp.add(c);
						//             cp.add(capacitance);
						// cp.add(c_unit);
						cp.add(del);
						cp.add(ok);
						cp.add(rotate);
						ok.addActionListener(this);
						del.addActionListener(this);
						rotate.addActionListener(this);


					}
				}
				addWindowListener( new WA());


			}
			String   get_length()
			{
				return capacitance.getValue().toString();
			}

			public void actionPerformed(ActionEvent e )
			{
				if(e.getSource() == ok )
				{
					/* System.out.println("HI ok button is pressed ");
					   if ( comp_node[node_index].img_no  == 1 ) //PMOS
					   {
					   Pmos_l = get_length();
					   Pmos_w = get_width();
					   }
					   else if ( comp_node[node_index].img_no  == 7 ) //NMOS
					   {
					   Nmos_l = get_length();
					   Nmos_w = get_width();
					   }*/
					if ( image_no  == 3 ) //Capacitor 
					{
						hori_len = get_length();
					}
					else if ( image_no  == 8 ) //Capacitor 
					{
						veri_len = get_length();
					}
					setVisible(false);
					workPanel.repaint();
				}
				if(e.getSource() == rotate )
				{

					comp_node[node_index].rotate(node_index);
					workPanel.repaint();
					//                                      System.out.println(comp_node[node_index].angle);
				}

				if(e.getSource() == del )
				{
					comp_node[node_index].del = true;
					comp_count[comp_node[node_index].img_no] -= 1; // for descrising the count to check no of each comp
					int i , j ;

					comp_node[node_index].remove_mat();
					/*      for ( i = comp_node[node_index].node_x ; i < comp_node[node_index].node_x + work_img_height ; i ++ )
						720                                         {
						721                                                 for ( j = comp_node[node_index].node_y ; j < comp_node[node_index].node_y + work_img_width ; j ++ )
						722                                                 {
						723                                                         work_mat[i][j] = -1 ;
						724                                                 }
						725                                         }*/
					// updating values of comp in file -------------------------------
					if ( comp_node[node_index].img_no  == 1 ) //PMOS
					{
						xor_count--;
						xor_delete[xor_delete_count++]=comp_node[node_index].node_number;
						//pmos_count--;
						//	pmos_delete[pmos_delete_count++]=comp_node[node_index].node_number;
					}
					else if ( comp_node[node_index].img_no  == 7 ) //NMOS
					{
						nmos_count--;
						nmos_delete[nmos_delete_count++]=comp_node[node_index].node_number;
					}
					else if ( comp_node[node_index].img_no  == 2 ) //NMOS
					{
						gnd_count--;
						gnd_delete[gnd_delete_count++]=comp_node[node_index].node_number;
					}
					else if ( comp_node[node_index].img_no  == 9 ) //NMOS
					{
						vdd_count--;
						vdd_delete[vdd_delete_count++]=comp_node[node_index].node_number;
					}
					else if ( comp_node[node_index].img_no  == 4 ) //input
					{
						input_count--;
						input_delete[input_delete_count++]=comp_node[node_index].node_number;
					}
					else if ( comp_node[node_index].img_no  == 10 ) //output
					{
						output_count--;
						output_delete[output_delete_count++]=comp_node[node_index].node_number;
					}
					/*          else if ( comp_node[node_index].img_no  == 8 ) //Capacitor 
						    {
						    Capacitance = null;
						    }*/
					setVisible(false);
					//                                      work_panel_repaint();
					workPanel.repaint();
				}

			}
			class WA extends WindowAdapter
			{
				public void windowClosing( WindowEvent ev)
				{
					setVisible(false);
				}
			}

		}




		public class WorkPanel extends JPanel implements MouseMotionListener,MouseListener
		{
			public WorkPanel()
			{
				end_points_mat = new int[work_panel_width][work_panel_height];
				work_mat = new int[work_panel_width][work_panel_height];
				wire_mat = new int[work_panel_width][work_panel_height];
				wire_points_mat = new int[work_panel_width][work_panel_height];

				int i , j ;
				for ( i = 0 ; i < work_panel_width ; i++)
				{
					for ( j = 0 ; j < work_panel_height ; j++ )
					{
						work_mat[i][j] = -1 ;
						end_points_mat[i][j] = -1 ;
						wire_mat[i][j] = -1 ;
						wire_points_mat[i][j] = -1 ;

					}
				}

			if(start==0)
			{
							int x;
							if (xor_count==0)
							{
							for(x=0;x<26;x++)
							{
								// 0 -7 inputs..
								if(x==0)
								{
									work_x = 50;
									work_y = 40;
									img_button_pressed = 4;
								}
								else if(x==1)
								{
									work_x = 50;
									work_y = 100;
									img_button_pressed = 4;
								}
								else if(x==2)
								{
									work_x = 50;
									work_y = 160;
									img_button_pressed = 4;
								}
								else if(x==3)
								{
									work_x = 50;
									work_y = 220;
									img_button_pressed = 4;
								}
								else if(x==4)
								{
									work_x = 50;
									work_y = 280;
									img_button_pressed = 4;
								}
								else if(x==5)
								{
									work_x = 50;
									work_y = 340;
									img_button_pressed = 4;
								}
								else if(x==6)
								{
									work_x = 50;
									work_y = 400;
									img_button_pressed = 4;
								}
								else if(x==7)
								{
									work_x = 50;
									work_y = 460;
									img_button_pressed = 4;
								}
								//22-25 xor..
								else if(x==8)
								{
									work_x = 400;
									work_y = 15;
									img_button_pressed = 1;
								}
								else if(x==9)
								{
									work_x = 525;
									work_y = 15;
									img_button_pressed = 1;
								}
								else if(x==10)
								{
									work_x = 650;
									work_y = 15;
									img_button_pressed = 1;
								}
								else if(x==11)
								{
									work_x = 775;
									work_y = 15;
									img_button_pressed = 1;
								}
								else if(x==12)
								{
									work_x = 400;
									work_y = 140;
									img_button_pressed = 1;
								}
								else if(x==13)
								{
									work_x = 525;
									work_y = 140;
									img_button_pressed = 1;
								}
								else if(x==14)
								{
									work_x = 650;
									work_y = 140;
									img_button_pressed = 1;
								}
								else if(x==15)
								{
									work_x = 775;
									work_y = 140;
									img_button_pressed = 1;
								}
								else if(x==16)
								{
									work_x = 400;
									work_y = 265;
									img_button_pressed = 1;
								}
								else if(x==17)
								{
									work_x = 560;
									work_y = 265;
									img_button_pressed = 1;
								}
								else if(x==18)
								{
									work_x = 720;
									work_y = 265;
									img_button_pressed = 1;
								}
								else if(x==19)
								{
									work_x = 400;
									work_y = 390;
									img_button_pressed = 1;
								}
								else if(x==20)
								{
									work_x = 560;
									work_y = 390;
									img_button_pressed = 1;
								}
								else if(x==21)
								{
									work_x = 720;
									work_y = 390;
									img_button_pressed = 1;
								}
								else if(x==22)
								{
									work_x = 1000;
									work_y = 35;
									img_button_pressed = 10;
								}
								else if(x==23)
								{
									work_x = 1000;
									work_y = 160;
									img_button_pressed = 10;
								}
								else if(x==24)
								{
									work_x = 1000;
									work_y = 285;
									img_button_pressed = 10;
								}
								else if(x==25)
								{
									work_x = 1000;
									work_y = 410;
									img_button_pressed = 10;
								}
								if(work_x<15)
									work_x=15;
								if(work_y<15)
									work_y=15;
								if(img_button_pressed==1)
								{
									if(xor_count >= 14)
									{
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 14 Xor gates ");
									}
									else
									{

										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 4 , 4 * 15,xor_count);
										//	pmos_count++;
										xor_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}

								}
								else if(img_button_pressed==7)
								{
									if(nmos_count>=4)
									{
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 4 NMOS ");
									}
									else
									{
										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 4 * 15,nmos_count);
										nmos_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}

								}
								else if ( img_button_pressed == 2) // or 
								{
									if(gnd_count>=1)
									{
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 1 Or Gate ");
									}
									else
									{
										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15* 4 , 4 * 15,gnd_count);
										gnd_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}
								}
								else if ( img_button_pressed == 4) // input
								{
									if(input_count>=8)
									{
										System.out.println(""+input_count);
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum  8 inputs ");
									}
									else
									{
										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15*2 , 15*2,input_count);
										input_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}
								}
								else if ( img_button_pressed == 10) // ouput  
								{
									if(output_count>=4)
									{
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum 4 outputs ");
									}
									else
									{
										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 15*2,output_count);
										output_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}
								}
								else if (  img_button_pressed == 9 ) // Vdd
								{
									if(vdd_count>=1)
									{
										JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 1 VDD ");
									}
									else
									{
										comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2, 3 * 15,vdd_count);
										vdd_count++;
										comp_node[total_comp].update_mat(total_comp);
										total_comp++;
									}
								}
								comp_count[img_button_pressed]++;	
								img_button_pressed=-1;
								//change_selected(0);
								//repaint();
								if(x==25)
									break;
							}
						}
				start=1;
			}
				addMouseMotionListener(this);
				addMouseListener(this);
			}
			public void mouseMoved(MouseEvent me)
			{
				work_x = me.getX();
				work_y = me.getY();
				if ( img_button_pressed == 3 && wire_button == 1 )
				{
					//      System.out.println("in");
					//      System.out.println(total_wire);
					int x = (work_x % 15)>7 ? (work_x/15)*15+15 : (work_x/15)*15; // for making good 
					int y = (work_y % 15)>7 ? (work_y/15)*15+15 : (work_y/15)*15; // accurate wire point around end points 
					wire[total_wire-1 ].update2(x , y);
					//                                      wire[total_wire-1 ].update2((work_x/20)*20 , (work_y/20)*20);
					repaint();
				}


			}
			public void mouseDragged(MouseEvent me)
			{
				int i,j;
				work_x = me.getX();
				work_y = me.getY();
				if(work_x<15)
					work_x=15;
				if(work_y<15)
					work_y=15;
				for ( i = work_x -30; i < work_x + comp_node[total_comp -1].width +30; i++ )
				{
					for ( j = work_y -30 ; j < work_y + comp_node[total_comp-1].height+30 ; j++ )
					{
						if(i <= 45 || j <= 45 ||i >= work_panel_width || j >= work_panel_height || (work_mat[i][j] != -1 && work_mat[i][j] != node_drag))
						{
							return;
						}
					}
				}
				if (node_drag != -1 )
				{
					comp_node[node_drag].remove_mat();

					comp_node[node_drag].node_x  = (work_x /15 )*15 ;
					comp_node[node_drag].node_y  = ( work_y /15)*15 ;

					comp_node[node_drag].update_mat(node_drag);
				}


				repaint();

			}
			public void mouseClicked(MouseEvent m)
			{
				//	System.out.println("xyz "+img_button_pressed);
				int i,j;
				work_x=m.getX();
				work_y=m.getY();
				if(img_button_pressed==-1)
				{
					myDialog dialog;
					if ( wire_mat[work_x][work_y] != -1 )
					{
						System.out.println("wire_mat[work_x][work_y]");
						System.out.println(wire_mat[work_x][work_y]);
						JFrame wire_f = new JFrame();
						int n = JOptionPane.showConfirmDialog( wire_f, "Do u want to Delete Wire ?","Wire", JOptionPane.YES_NO_OPTION);
						if ( n == 0 )
						{
							//      System.out.println("Deletded ");
							wire[wire_mat[work_x][work_y]].del();
							repaint();
						}
						else
						{
							//      System.out.println("Not Deletded ");
						}

					}
					else if ( work_mat[work_x][work_y]!= -1 ) 
						//	else
					{
						System.out.println("xyz");
						int temp = work_mat[work_x][work_y] ;
						if(temp!=-1)
						{
							int temp1 = comp_node[temp].img_no ; // temp is no img no 
							if (  temp1 == 1 || temp1 == 2 || temp1 == 3 || temp1 == 7 ||  temp1 == 8 || temp1==9 || temp1 == 4 || temp1 == 10)
							{
								//JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");
								//          if ( dialog[temp] == null )
								//        {
								//              fr[temp] = new JFrame(); // bec work_mat will store the index of that comp in mat
								dialog = new myDialog( new JFrame() ,comp_str[temp1] , temp1,temp);
								dialog.setVisible(true);
							}
						}
					}
							//}
						else
						{
						}
							//myDialog dialog;
				}

				else if (img_button_pressed == 3) // i.e line is selected 
				{
					int x = (work_x % 15)>7 ? (work_x/15)*15+15 : (work_x/15)*15; // for making good 
					int y = (work_y % 15)>7 ? (work_y/15)*15+15 : (work_y/15)*15; // accurate wire point around end points 
					System.out.println("X"+work_x);
					System.out.println("y"+work_y);
					System.out.println("wire_button"+wire_button);
					System.out.println("end_point__mat"+end_points_mat[work_x][work_y]);

					if ( wire_button == 0 ) // button is pressed first time 
					{
						//if ( end_points_mat[work_x][work_y] != - 1 ) // if end points r there 
						if ( end_points_mat[x][y] != - 1 ) // if end points r there 
						{

							//        wire[total_wire++] = new line((work_x /20)*20, (work_y/20)*20 , (work_x/20)*20 , (work_y/20)*20);
							wire[total_wire++] = new line(x,y , x, y);
							repaint();
							wire_button = 1 ;
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Wire could start OR end at the componet's connection points only ");
						}
					}
					else
					{
						//if ( end_points_mat[work_x][work_y] != - 1 ) // if end points r there 
						 if ( end_points_mat[x][y] != - 1 ) // if end points r there 
						   {
						//      wire[total_wire - 1].update2((work_x/20)*20 , (work_y/20)*20); // -1 bec inder of first wire is 0 
						wire[total_wire - 1].update2(x , y ); // -1 bec inder of first wire is 0 
						repaint();

						wire[total_wire - 1 ].update_mat(total_wire - 1); // 
						img_button_pressed = -1 ;
						change_selected(0);
						wire_button = 0 ;
						 }
						   else
						   {       // adding more end points 
						   wire[total_wire - 1].update(x , y ); // -1 bec inder of first wire is 0 
						   repaint();
						   }
					}
				}            			

				else
				{	
					//	System.out.println("xyz123");
					if(img_button_pressed!=-1)
					{
						myDialog dialog;
						if(work_x<15)
							work_x=15;
						if(work_y<15)
							work_y=15;
						if(img_button_pressed==1)
						{
							if(xor_count >= 14)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 14 Xor gates ");
							}
							else
							{

								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 4 , 4 * 15,xor_count);
								//	pmos_count++;
								xor_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}

						}
						else if(img_button_pressed==7)
						{
							if(nmos_count>=4)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 4 NMOS ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 4 * 15,nmos_count);
								nmos_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}

						}
						else if ( img_button_pressed == 2) // or 
						{
							if(gnd_count>=1)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 1 Or Gate ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15* 4 , 4 * 15,gnd_count);
								gnd_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}
						}
						else if ( img_button_pressed == 4) // input
						{
							if(input_count>=8)
							{
								System.out.println("!!!!");
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum  8 inputs ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15*2 , 15*2,input_count);
								input_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}
						}
						else if ( img_button_pressed == 10) // ouput  
						{
							if(output_count>=4)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum 4 outputs ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 15*2,output_count);
								output_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}
						}
						else if (  img_button_pressed == 9 ) // Vdd
						{
							if(vdd_count>=1)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 1 VDD ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2, 3 * 15,vdd_count);
								vdd_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}
						}
						comp_count[img_button_pressed]++;	
						img_button_pressed=-1;
						change_selected(0);
						repaint();
					}
				}


			}
			public void mouseReleased(MouseEvent m)
			{
				if ( node_drag != -1 )
				{
					comp_node[node_drag].update_mat(node_drag); // updating the matrix
					/*      for ( i = comp_node[node_drag].node_x ; i < comp_node[node_drag].node_x + comp_node[node_drag].width ; i++ )
						1141                                         {
						1142                                                 for ( j = comp_node[node_drag].node_y ; j < comp_node[node_drag].node_y + comp_node[node_drag].height ; j++ )
						1143                                                 {
						1144                                                         work_mat[i][j] =  node_drag ;   // update the matrix 
						1145                                                 }
						1146                                         }*/
					node_drag = -1;   // node is unseledted to drag
				}

			}
			public void mouseEntered(MouseEvent m)
			{
			}
			public void mouseExited(MouseEvent m)
			{
			}
			public void mousePressed(MouseEvent me)
			{
				int i , j ;
				work_x = me.getX();
				work_y = me.getY();
				if ( work_mat[work_x][work_y] != -1 )
				{
					node_drag = work_mat[work_x][work_y];   // node is selected for drag

					comp_node[node_drag].remove_mat();                 // update the matrix as the img is selected , so can be moved 

				}

			}
			public void paint(Graphics g)
			{
				int i,j;
				String line;
				Graphics2D g2d = (Graphics2D)g;
				g2d.scale(scale_x , scale_y);
				// back ground ----------------
				//	g2d.setColor(Color.green);
				g2d.setColor(Color.white);
				g.fillRect(0,0,work_panel_width+1000 , work_panel_height+1000);
				g2d.setColor(Color.orange);
				g2d.setStroke(new BasicStroke(2));
				for ( i = 0 ; i < work_panel_width +400; i+=15)
				{
					for ( j = 0 ; j < work_panel_height+200 ; j+=15 )
					{
						g2d.drawOval(  i -1,j-1 , 1 , 1);
					}
				}
				if(simulate_button_pressed == 1)
				{
					int local_total_comp = 0,local_x = 0,local_y = 0,local_img_no = 0;
					double local_angle = 0.0;
					String fileToRead ="txt/circuit.txt";
					URL url = null;
					try
					{
						url = new URL(getCodeBase(), fileToRead);
					}
					catch(MalformedURLException e){
						System.out.println("I did't got the outfile to read :( :( So I am very said ");
					}
					//  String line;
					try{
						InputStream in = url.openStream();
						BufferedReader dis = new BufferedReader(new InputStreamReader(in));
						//   strBuff = new StringBuffer();
						// myline = bf.readLine();
						//	File f = new File("http://localhost/RaShip/exp/file.txt");
						//	FileInputStream fis = new FileInputStream(f);
						//	BufferedInputStream bis = new BufferedInputStream(fis);
						//	DataInputStream dis = new DataInputStream(bis);

						line = dis.readLine();
						if(line != null)
							local_total_comp  = Integer.parseInt(line);
						System.out.println("Try"+local_total_comp);
						int x=1, y=1;
						for ( i = 0; i < local_total_comp ; i++ )
						{

							try
							{
								line = dis.readLine();
								if(line.equals("delete"))
									continue;
								if(line != null)
									local_img_no   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_x   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_y   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_angle   = Double.parseDouble(line.trim());
							}
							catch(IOException e){System.out.println(e);}

								if ( local_img_no == 1)
								{
								g.setColor(Color.blue);
								//g.setStroke(new BasicStroke(1));

							//	g2d.drawLine (local_x+20 , local_y , local_x+50, local_y );
							
							//	g2d.fillRect (local_x+20 -4  , local_y -4 , 8 ,8);
							//	g2d.fillRect (local_x+110- 4 , local_y -4 , 8 ,8);
								
							//	g2d.drawLine (local_x+20 , local_y+80 , local_x+50 , local_y+80 );
							
							//	g2d.fillRect (local_x+20 -4  , local_y+80 -4 , 8 ,8);
							//	g2d.fillRect (local_x+110 - 4 , local_y+80 -4 , 8 ,8);
								
							//	g2d.drawLine (local_x+20 , local_y+160 , local_x+50 , local_y+160 );
							
							//	g2d.fillRect (local_x+20 -4  , local_y+160 -4 , 8 ,8);
							//	g2d.fillRect (local_x+110 - 4 , local_y+160 -4 , 8 ,8);
								
								//local_y +=30;
								draw_cmos(g2d , local_x  , local_y-5 , 10, local_angle,1);
								draw_nmos(g2d , local_x  , local_y+40-5 , 10, local_angle,1);
								
								draw_cmos(g2d , local_x+70  , local_y+20-5 , 10, local_angle+1.5707963267948966,0);
								draw_nmos(g2d , local_x+30  , local_y+60-5 ,10 , local_angle-1.5707963267948966,0);

								draw_cmos(g2d , local_x+80  , local_y-40 , 10, local_angle,1);
								draw_nmos(g2d , local_x+80  , local_y ,10 , local_angle,0);
								g.setColor(Color.red);
								g.drawString("Vcc" , local_x+100 , local_y-25 );
								g.setColor(Color.blue);
								//gnd terminal..
								g2d.drawLine (local_x+90 , local_y+40 , local_x+110 , local_y+40 );
								g2d.drawLine (local_x+93 , local_y+42 , local_x+107 , local_y+42 );
								g2d.drawLine (local_x+96 , local_y+44 , local_x+104 , local_y+44 );

								g2d.drawLine (local_x+80 , local_y+20 , local_x+80 , local_y-20 );
								g2d.drawLine (local_x+80 , local_y , local_x-30 , local_y );
								
								g2d.drawLine (local_x+20 , local_y+10 , local_x+20 , local_y );
								
								g2d.drawLine (local_x+100 , local_y , local_x+120 , local_y );
								g2d.drawLine (local_x+120 , local_y+65-5 , local_x+120 , local_y );
								g2d.drawLine (local_x+20 , local_y+65-5 , local_x+120 , local_y+65-5 );

								g2d.drawLine (local_x+50 , local_y+20-5 , local_x+50 , local_y );
/*			        g2d.drawOval( local_x + 100-1, local_y-1 , 3,3 );
				g2d.fillOval( local_x+100-1, local_y-1, 3 ,3 );

			        g2d.drawOval( local_x + 80-1, local_y-1 , 3,3 );
				g2d.fillOval( local_x+80-1, local_y-1, 3 ,3 );

			        g2d.drawOval( local_x + 35-1, local_y+35-1 , 3,3 );
				g2d.fillOval( local_x+35-1, local_y+35-1, 3 ,3 );

			        g2d.drawOval( local_x + 20-1, local_y+35-1 , 3,3 );
				g2d.fillOval( local_x+20-1, local_y+35-1, 3 ,3 );

			        g2d.drawOval( local_x -1, local_y+35-1 , 3,3 );
				g2d.fillOval( local_x-1, local_y+35-1, 3 ,3 );

			        g2d.drawOval( local_x + 45-1, local_y+35-1 , 3,3 );
				g2d.fillOval( local_x+45-1, local_y+35-1, 3 ,3 );
*/
								g2d.drawLine (local_x+50 , local_y+60-5 , local_x+50 , local_y+65-5 );
								g2d.drawLine (local_x+70 , local_y+40-5 , local_x+70 , local_y+75-5);
								
								g2d.drawLine (local_x+70 , local_y+75-5 , local_x-15 , local_y+75-5);
								g2d.drawLine (local_x-15 , local_y+75-5 , local_x-15 , local_y+40-5);

								g2d.drawLine (local_x , local_y+20-5 , local_x , local_y+60-5);
								
								g2d.drawLine (local_x , local_y+40-5 , local_x-30 , local_y+40-5);

								g2d.drawLine (local_x+30 , local_y+40-5 , local_x+20 , local_y+40-5);
								g2d.drawLine (local_x+35 , local_y+40-5 , local_x+35 , local_y+85-5);
								/*g.setColor(Color.red);
								g.drawString("OUT" , local_x+30 , local_y+110);
								g.setColor(Color.blue);*/
			g.setColor(Color.red);
			g.fillRect( local_x-30 - 4, local_y -3, 6 ,6 );
			g.fillRect( local_x-30 - 4, local_y+40-5  -3, 6 ,6 );
			g.fillRect( local_x+35 - 3, local_y+85-5 -3, 6 ,6 );

							/*	draw_cmos(g2d , local_x+70  , local_y+40 ,10 , local_angle+3.141592653589793);
								draw_cmos(g2d , local_x+70  , local_y+80 ,10 , local_angle+3.141592653589793);
								draw_nmos(g2d , local_x+70  , local_y+120 ,10 , local_angle+3.141592653589793);
								draw_nmos(g2d , local_x+70  , local_y+160 ,10 , local_angle+3.141592653589793);
							*/	
								}
								else if(local_img_no==11)
								{
									draw_join_points(g2d);

								}

								else if ( local_img_no == 3)
								{
								draw_horizontal_wire(g2d , local_x  , local_y , comp_node[i].width , comp_node[i].angle);
								g.setColor(Color.yellow);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
							}
							else if ( local_img_no == 8)
							{
							draw_vertical_wire(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].height , comp_node[i].angle);
							g.setColor(Color.yellow);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
							}
							else if ( local_img_no == 7)
							  {
								//draw_cmos(g2d , local_x  , local_y ,10 , local_angle);
							  	//draw_nmos(g2d , local_x  , local_y+40 , 10 , local_angle);

							  g.setColor(Color.yellow);
							  //g.drawString("NMOS" , local_x -10 , local_y + 10 );
							//	g.drawString(comp_str[7] , comp_node[i].node_x + -10 , comp_node[i].node_y + 10 );



							}
							else if ( local_img_no == 2)
							{
							draw_ground(g2d , local_x  , local_y , 15, local_angle);
							g.setColor(Color.yellow);
							g.drawString("Ground" , local_x + 15 , local_y + 15 );
							// g.drawString(comp_str[2] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
							}
							else if ( local_img_no == 9)
							{
							draw_vdd(g2d , local_x  , local_y , 15, local_angle);
							g.setColor(Color.yellow);
							g.drawString("VDD" , local_x + 30 , local_y + 10 );
							// g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
							else if ( local_img_no == 4)
							{
								draw_input(g2d , local_x  , local_y , 15, local_angle, x);
								x++;
								// g.setColor(Color.yellow);
								// g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
							else if ( local_img_no == 10)
							{
								draw_output(g2d , local_x  , local_y , 30, local_angle, y);
								y++;
								// g.setColor(Color.yellow);
								// g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
						

						}	
						g2d.setStroke(new BasicStroke(2));
						int local_total_wire=0,local_end_index=0,local_xk=0,local_yk=0,local_xk1=0,local_yk1=0,local_x1=0,local_y1=0,local_x2=0,local_y2=0;
						local_total_wire   = Integer.parseInt(dis.readLine());
						for ( i = 0 ; i < local_total_wire ; i++ )
						{
							//if ( wire[i].del == false )
							//{

							g2d.setColor(Color.green);
							try{
								line = dis.readLine();
								if(line.equals("delete"))
									continue;
								if(line != null)
								{
									local_end_index = new Integer(line).intValue();
									//				 			local_end_index   = Integer.parseInt(line.trim());
								}
							}
							catch(IOException e){System.out.println(e);}
							for ( int k = 0 ; k < local_end_index ; k ++ )
							{
								try{
									line = dis.readLine();
									if(line != null)
										local_xk   = Integer.parseInt(line.trim());
									line = dis.readLine();
									if(line != null)
										local_yk   = Integer.parseInt(line.trim());
									line = dis.readLine();
									if(line != null)
										local_xk1   = Integer.parseInt(line.trim());
									line = dis.readLine();
									if(line != null)
										local_yk1   = Integer.parseInt(line.trim());
								}
								catch(IOException e){System.out.println(e);}
								//      g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
								g2d.drawLine (local_xk , local_yk , local_xk1 , local_yk );
								g2d.drawLine (local_xk1 , local_yk , local_xk1 , local_yk1 );
								//      g2d.drawLine (wire[i].x1 , wire[i].y1 , wire[i].x2 , wire[i].y1 );
								//      g2d.drawLine (wire[i].x2 , wire[i].y1 , wire[i].x2 , wire[i].y2 );
							}

							g2d.setColor(Color.red);
							/*try
							{
								if((line = dis.readLine()) != null)
									local_x1   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_y1   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_x2   = Integer.parseInt(line.trim());
								line = dis.readLine();
								if(line != null)
									local_y2   = Integer.parseInt(line.trim());
							}
							catch(IOException e){System.out.println(e);}

							g2d.fillRect (local_x1 -4  , local_y1 -4 , 8 ,8);
							g2d.fillRect (local_x2 - 4 , local_y2 -4 , 8 ,8);
							//}*/
						}
						dis.close();
					}
					catch(IOException e){}

				}
				else
				{
					int x=1, y=1, z=1;
					for ( i = 0; i < total_comp ; i++ )
					{
						if ( comp_node[i].del != true )
						{
							if ( comp_node[i].img_no == 1)
							{
								draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle);
								String s = new Integer(x).toString();
								x++;
								g.setColor(Color.black);
								g.drawString(comp_str[1]+s , comp_node[i].node_x -10   , comp_node[i].node_y + 10);
							}
							else if ( comp_node[i].img_no == 3)
							{
								draw_horizontal_wire(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].width , comp_node[i].angle);
								g.setColor(Color.black);
								//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
							}
										else if ( comp_node[i].img_no == 8)
										{
										draw_vertical_wire(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].height , comp_node[i].angle);
										g.setColor(Color.black);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
							}
							else if ( comp_node[i].img_no == 7)
							{
							draw_nmos(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle,0);
							g.setColor(Color.black);
							g.drawString(comp_str[7] , comp_node[i].node_x + -10 , comp_node[i].node_y + 10 );



							}
							/*else if ( comp_node[i].img_no == 2)
							{
							draw_or(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
							g.setColor(Color.green);
							//g.drawString("OR" , comp_node[i].node_x  , comp_node[i].node_y + 15 );
							}*/
							else if ( comp_node[i].img_no == 9)
							{
							draw_vdd(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
							g.setColor(Color.green);
							g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
							                        else if ( comp_node[i].img_no == 4)
							{
								draw_input(g2d , comp_node[i].node_x  , comp_node[i].node_y , 30, comp_node[i].angle, y);
								y++;
								//g.setColor(Color.yellow);
								//g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
							else if ( comp_node[i].img_no == 10)
							{
								draw_output(g2d , comp_node[i].node_x  , comp_node[i].node_y , 30, comp_node[i].angle, z);
								z++;
								// g.setColor(Color.yellow);
								// g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
							}
							else
							{
								g2d.drawImage(img[comp_node[i].img_no] , comp_node[i].node_x ,comp_node[i].node_y, work_img_width , work_img_height,  this);
							}


						}


					}
					g2d.setStroke(new BasicStroke(2));
					for ( i = 0 ; i < total_wire ; i++ )
					{
						if ( wire[i].del == false )
						{
							g2d.setColor(Color.green);
							for ( int k = 0 ; k < wire[i].end_index ; k ++ )
							{
								//      g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
								g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k] );
								g2d.drawLine (wire[i].x[k+1] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
								//      g2d.drawLine (wire[i].x1 , wire[i].y1 , wire[i].x2 , wire[i].y1 );
								//      g2d.drawLine (wire[i].x2 , wire[i].y1 , wire[i].x2 , wire[i].y2 );
							}

							g2d.setColor(Color.red);
							g2d.fillRect (wire[i].x1 -4  , wire[i].y1 -4 , 8 ,8);
							g2d.fillRect (wire[i].x2 - 4 , wire[i].y2 -4 , 8 ,8);
						}
					}
				}

			}
			void draw_horizontal_wire(Graphics2D g , int x , int y , int width , double angle )
			{
				g.setColor(Color.yellow);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				g.drawLine(x  , y  , x + width , y );
				g.setColor(Color.red);
				g.fillRect( x -4, y-4, 8 ,8 );
				g.fillRect( x + width -4 , y -4  , 8 ,8 );
			}
			void draw_vertical_wire(Graphics2D g , int x , int y , int height , double angle )
			{

				g.setColor(Color.yellow);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				g.drawLine(x  , y  , x , y+height );
				g.setColor(Color.red);
				g.fillRect( x -4, y-4, 8 ,8 );
				g.fillRect( x  -4 , y + height -4  , 8 ,8 );
			}
			void draw_circuit(Graphics2D g, int x ,int y ,int height , double angle)
			{

				Graphics2D g2d = (Graphics2D)g ;
				//	draw_pmos(g2d , 15  , 200 , 15 , 0);


			}
			/*			 void draw_or(Graphics2D g , int x , int y , int width , double angle )
						 {
						 g.rotate(angle , x , y);
						 g.setColor(Color.yellow);
						 g.setStroke(new BasicStroke(2));
						 g.setColor(Color.blue);
						 g.drawLine(x,y+2*width,x+width+width/2,y+2*width);
						 g.drawLine(x,y+3*width,x+width+width/2,y+3*width);
						 g.drawArc(x+width-width/2,y+2*width-width/2,width,2*width,90,-180);
			//  g.drawArc(x+width-width/4,y+2*width-width/2,width,2*width,90,-180);
			g.drawLine(x+width,y+2*width-width/2,x+2*width,y+2*width-width/2);
			g.drawLine(x+width,y+3*width+width/2,x+2*width,y+3*width+width/2);
			g.drawArc(x+2*width-width/2,y+2*width-width/2,width,2*width,90,-180);
			g.drawLine(x+3*width-width/2,y+2*width+width/2,x+4*width,y+2*width+width/2);
			g.drawLine(x+4*width,y+2*width,x+4*width,y+2*width+width/2);
			g.setStroke(new BasicStroke(1));
			g.setColor(Color.red);
			g.fillRect( x - 4, y+2*width  -4, 8 ,8 );
			g.fillRect( x - 4, y+3*width  -4, 8 ,8 );
			g.fillRect( x +4*width- 4, y+2*width -4, 8 ,8 );
			g.rotate(-angle , x , y);

			}
			 */
			void draw_xor(Graphics2D g , int x , int y , int width , double angle)
			{
//			String s = new Integer(no).toString();
//			g.drawString(s, x + 25 , y+ width - 5);
				g.rotate(angle , x , y);
				g.setColor(Color.yellow);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				g.drawLine(x,y+2*width,x+width+width/2,y+2*width);
				g.drawLine(x,y+3*width,x+width+width/2,y+3*width);
				g.drawArc(x+width-width/2,y+2*width-width/2,width,2*width,90,-180);
				g.drawArc(x+width-width/2+5,y+2*width-width/2,width,2*width,90,-180);
				//  g.drawArc(x+width-width/4,y+2*width-width/2,width,2*width,90,-180);
				g.drawLine(x+width,y+2*width-width/2,x+2*width,y+2*width-width/2);
				g.drawLine(x+width,y+3*width+width/2,x+2*width,y+3*width+width/2);
				g.drawArc(x+2*width-width/2,y+2*width-width/2,width,2*width,90,-180);
				g.drawLine(x+3*width-width/2,y+2*width+width/2,x+4*width,y+2*width+width/2);
				g.drawLine(x+4*width,y+2*width,x+4*width,y+2*width+width/2);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect( x - 4, y+2*width  -4, 8 ,8 );
				g.fillRect( x - 4, y+3*width  -4, 8 ,8 );
				g.fillRect( x +4*width- 4, y+2*width -4, 8 ,8 );
				g.rotate(-angle , x , y);
			}
			/*void draw_xor(Graphics2D g , int x , int y , int width , double angle)
			  {
			//	g.drawImage(comp2.gif,x,y,observer);
			g.rotate(angle , x , y);
			g.setColor(Color.yellow);
			//				g.drawRect( x , y , 2*width , 4*width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));

			g.drawLine(x-width,y,x,y);
			g.drawLine(x-width,y+width,x,y+width);
			//	g.fillRect(x,y-5,20,30);
			g.drawArc(x - 5  ,y - 5 ,2*width - 5 ,width + 10 ,130,-260);
			g.drawArc(x - 15  ,y - 5 ,width/2+5 ,width + 10 ,50,-110);
			g.drawLine(x+2*width - 7,y+(width/2),x+(3*width),y+(width/2));
			//	g.drawLine(x+40,y,x+40,y+10);

			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			//g.drawRect(x,y,8,8);
			g.fillRect(x-4-width,y-4,8,8);
			g.fillRect(x-4-width,y+width-4,8,8);
			g.fillRect(x-4+(3*width),y+(width/2) - 4,8,8);

			g.setColor(Color.green);
			g.rotate(-angle , x , y);
			}*/
			/*void draw_xor(Graphics2D g , int x , int y , int width , double angle )
			  {

			  int w = width/2;
			  g.rotate(angle , x , y);
			  g.setColor(Color.yellow);
			//                              g.drawRect( x , y , 2*width , 4*width);

			g.setStroke(new BasicStroke(2));
			g.setColor(Color.blue);

			g.drawLine(x- w, y + w ,x + w , y + w);
			g.drawLine(x- w, y + w ,x - w , y - w);
			g.drawLine(x- w, y - w ,x + w , y - w);
			g.drawLine(x + w, y - w ,x + w , y + w);
			g.drawLine(x - width, y - width/4 ,x - width/2 , y - width/4);
			g.drawLine(x - width, y + width/4 ,x - width/2 , y + width/4);
			g.drawLine(x + width, y + width/4 ,x + width/2 , y + width/4);
			g.drawLine(x +  width, y - width/4 ,x + width/2 , y - width/4);
			 */ /*  g.drawLine(x  , y + 2*width , x + width , y + 2*width);

				g.drawLine(x + width , y + width/4 +width, x +  width , y +(7* width)/4 +width);
				g.drawLine(x + width +width/4 , y + width/4 +width, x +  width +width/4, y +(7* width)/4 +width);

				g.drawLine(x + (5*width)/4 , y + width/2+width, x + 2* width , y + width/2+width );
				g.drawLine(x + (5*width)/4 , y +(3* width)/2+width, x +  2*width , y +(3* width)/2+width);

				g.drawLine(x + 2*width , y , x + 2*width , y + (3* width)/2);
				g.drawLine(x + 2*width , y + 4*width , x + 2*width , y + (5* width)/2);*/

			/* g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			g.fillRect( x - width - 4, y - width/4 - 4 , 8 ,8 );
			g.fillRect( x - width - 4, y + width/4 - 4 , 8 ,8 );
			g.fillRect( x + width - 4, y - width/4 - 4 , 8 ,8 );
			g.fillRect( x + width - 4, y + width/4 - 4 , 8 ,8 );

			g.setColor(Color.green);
			g.rotate(-angle , x , y);
			}*/
			void draw_nmos(Graphics2D g , int x , int y , int width , double angle, int flag )
			  {

			  g.rotate(angle , x , y);
			  g.setColor(Color.yellow);
			//                              g.drawRect( x , y , 2*width , 4*width);

		//	g.setStroke(new BasicStroke(2));
			g.setColor(Color.blue);
			g.drawLine(x  , y + 2*width , x + width , y + 2*width);

			g.drawLine(x + width , y + 55*width/100 +width, x +  width , y +245* width/100);
			g.drawLine(x + width +width/4 , y + width/4 +width, x +  width +width/4, y +(7* width)/4 +width);

			g.drawLine(x + (5*width)/4 , y + width/2+width, x + 2* width , y + width/2+width );
			g.drawLine(x + (5*width)/4 , y +(3* width)/2+width, x +  2*width , y +(3* width)/2+width);

			g.drawLine(x + 2*width , y , x + 2*width , y + (3* width)/2);
			if(flag==0)
				g.drawLine(x + 2*width , y + 4*width , x + 2*width , y + (5* width)/2);

			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
/*			g.fillRect( x - 4, y + 2*width -4, 6 ,6 );
			g.fillRect( x + 2*width -4, y - 4 , 6 ,6 );
			g.fillRect( x + 2*width -4, y +4 * width - 4 , 6 ,6 );
*/
			g.setColor(Color.blue);
			g.rotate(-angle , x , y);
			}
			
			void draw_join_points(Graphics2D g)
			{
			g.setStroke(new BasicStroke(4));									
			g.setColor(Color.green);
			g.fillRect( 150 -4, 40 -4, 6 ,6 );
			g.fillRect( 100 -4,220 -4, 6 ,6 );
			
			g.fillRect( 140 -4,268 -4, 6 ,6 );
			
			g.fillRect( 130 -4,100 -4, 6 ,6 );
			g.fillRect( 160 -4,205 -4, 6 ,6 );
			g.fillRect( 95 -4,280 -4, 6 ,6 );
			g.fillRect( 110 -4,340 -4, 6 ,6 );
			
			g.fillRect( 140 -4,400 -4, 6 ,6 );
			
			g.fillRect( 590 -4,430 -4, 6 ,6 );
			g.fillRect( 395 -4,255 -4, 6 ,6 );
			}
			
			void draw_cmos(Graphics2D g , int x , int y , int width , double angle, int flag)
			{

			g.rotate(angle , x , y);
			g.setColor(Color.yellow);
			//                              g.drawRect( x , y , 2*width , 4*width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(1));
			g.drawOval( x + width - 6, y + 2*width - 3, 6,6 );
			//g.setStroke(new BasicStroke(2));
			
			g.drawLine(x  , y + 2*width , x + width - 6,y + 2*width);

			g.drawLine(x + width , y + 55*width/100 +width, x +  width , y +245* width/100);
			//g.drawLine(x + width , y + width/4 +width, x +  width , y +(7* width)/4 +width);
			g.drawLine(x + width +width/4 , y + width/4 +width, x +  width +width/4, y +(7* width)/4 +width);

			g.drawLine(x + (5*width)/4 , y + width/2+width, x + 2* width , y + width/2+width );
			g.drawLine(x + (5*width)/4 , y +(3* width)/2+width, x +  2*width , y +(3* width)/2+width);

			if(flag==0)
				g.drawLine(x + 2*width , y  , x + 2*width , y + (3* width)/2);
			g.drawLine(x + 2*width , y + 4*width , x + 2*width , y + (5* width)/2);

			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
		//	g.fillRect( x - 4, y + 2*width -4, 6 ,6 );
		//	g.fillRect( x + 2*width -4, y - 4 , 6 ,6 );
		//	g.fillRect( x + 2*width -4, y +4 * width - 4 , 6 ,6 );

			//g.setColor(Color.green);
			g.rotate(-angle , x , y);
			}
			void draw_ground(Graphics2D g , int x , int y , int width , double angle)
			{
			g.rotate(angle , x , y);
			g.setColor(Color.yellow);
			//                              g.drawRect( x , y , 2*width , 2*width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));
			g.drawLine(x +width  , y  , x + width ,y + (5*width)/4);
			g.drawLine(x +width/2  , y + (5*width)/4 , x + (3*width)/2 ,y + (5*width)/4);
			g.drawLine(x +(3*width)/4  , y + (6*width)/4 , x + (5*width)/4 ,y + (6*width)/4);
			g.drawLine(x +(7*width)/8  , y + (7*width)/4 , x + (9*width)/8 ,y + (7*width)/4);
			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			g.fillRect( x +width - 4, y  -4, 8 ,8 );
			g.rotate(-angle , x , y);

		}
		void draw_vdd(Graphics2D g , int x , int y , int width , double angle)
		{
			g.rotate(angle , x , y);
			g.setColor(Color.yellow);
			//      g.drawRect( x , y , 2*width , 3*width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));
			g.drawLine(x +width  , y  , x + width ,y + 2*width);
			g.setStroke(new BasicStroke(4));
			g.drawLine(x  , y +(5*width)/2 , x + (4*width)/2 ,y + (3*width)/2);
			//      g.drawLine(x +width/2  , y + (5*width)/4 , x + (3*width)/2 ,y + (5*width)/4);
			//      g.drawLine(x +(3*width)/4  , y + (6*width)/4 , x + (5*width)/4 ,y + (6*width)/4);
			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			g.fillRect( x +width - 4, y  -4, 8 ,8 );
			g.rotate(-angle , x , y);
		}
		void draw_input(Graphics2D g , int x , int y , int width , double angle, int no)
		{
			String s = new Integer(no).toString();
			g.rotate(angle , x , y);	
			g.setColor(Color.black);
			g.drawString("IN"+s, x - 30 , y+ width - 5);
			//	g.drawRect( x , y , 2 *width , width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));
			g.drawLine(x  , y  , x + 2*width ,y );
			g.drawLine(x  , y + width  , x + width ,y + width );
			g.drawLine(x  , y , x , y + width );
			//	g.drawLine(x + width , y , x + (3*width)/2 , y + width/2);
			g.drawLine(x + width , y + width , x + (3*width)/2 , y );
			//	g.drawLine(x + 2*width , y + width /2 , x + (3*width)/2 , y + width/2);
			//	g.drawLine(x +width/2  , y + (5*width)/4 , x + (3*width)/2 ,y + (5*width)/4);
			//	g.drawLine(x +(3*width)/4  , y + (6*width)/4 , x + (5*width)/4 ,y + (6*width)/4);
			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			g.fillRect( x + 2*width - 4, y  -4, 8 ,8 );
			g.rotate(-angle , x , y);
		}
		void draw_output(Graphics2D g , int x , int y , int width , double angle, int no)
		{
			String s = new Integer(no).toString();

			g.rotate(angle , x , y);
			g.setColor(Color.black);
			g.drawString("OUT"+s, x  + width / 2, y+ width - 5);

			//	g.drawRect( x , y , 2 *width , width);

			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));
			g.drawLine(x + width/2 , y  , x + (3*width)/2 ,y );
			g.drawLine(x + width/2 , y + width  , x + (3*width)/2 ,y + width );
			g.drawLine(x + width/2  , y , x + width/2 , y + width );

			g.drawLine(x + (3*width)/2 , y , x + 2*width , y + width/2);
			g.drawLine(x + (3*width)/2, y +	 width , x + 2*width , y + width/2);

			g.drawLine(x , y  , x + width/2 , y );
			//	g.drawLine(x +width/2  , y + (5*width)/4 , x + (3*width)/2 ,y + (5*width)/4);
			//	g.drawLine(x +(3*width)/4  , y + (6*width)/4 , x + (5*width)/4 ,y + (6*width)/4);
			g.setStroke(new BasicStroke(1));
			// end points 
			g.setColor(Color.red);
			g.fillRect( x  - 4, y -4, 8 ,8 );
			g.rotate(-angle , x , y);
		}





		}
		}
	}
