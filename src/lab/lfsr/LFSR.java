import java.applet.Applet;
import java.util.*;
import java.util.PropertyPermission.*;
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
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.ImageIcon.*;


public class LFSR extends JApplet {
	public int number=0,s=0,img1=0,ww=15,hh=15,flag=0,flag1=0,flag_order=0,set_output=0,pause_flag=0,jug1=0,f1=0,jug2=0,f2=0;
	public int a0=0,a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0,a9=0,a10=0,a11=0,a12=0,a13=0,a14=0,a15=0;
	int [] seed = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	int [] coeff = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	int stopper=1;
	public JFrame frame, frame1;
	public  void init()
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
	private  void createAndShowGUI() {
		
		MyPanel myPane = new MyPanel();
		myPane.setOpaque(true);
		setSize( 1400, 650 );
		setContentPane(myPane);
	}
	public class MyPanel  extends JPanel  implements ActionListener//MouseListener,MouseMotionListener
	{
		int exp_type = 0 ; // 0 -> Complementary Inverter , 1 -> Pseudo Inverter 
		/** Work Panel Variables ************************************************************************************************/
		double scale_x = 1 ;  // scaling of work pannel 
		double scale_y = 1 ;
		int pmos_delete_count=0;
		int [] pmos_delete=new int[200];
		int nmos_delete_count=0;
		int [] nmos_delete=new int[200];
		int input_delete_count=0;
		int [] input_delete=new int[200];
		int output_delete_count=0;
		int [] output_delete=new int[200];
		int gnd_delete_count=0;
		int [] gnd_delete=new int[200];
		int vdd_delete_count=0;
		int [] vdd_delete=new int[200];

		int pmos_count=0;	
		int nmos_count=0;
		int input_count=0;
		int output_count=0;
		int gnd_count=0;	
		int vdd_count=0;	

		int work_x ;
		int work_y ;
		int wire_button  = 0 ;// 0 not presed already , 1 -> already pressed 
		int img_button_pressed = -1 ;
		int draw_work = 0 ; // if 1 -> draw the image on work 

		int[][] work_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] end_points_mat ;   // 
		int[][] wire_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] wire_points_mat ;   // 

		int work_img_width = 50;
		int work_img_height = 50;
		int work_panel_width = 1200 ;
		int work_panel_height = 540;

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
				"This shows which component is selected ." ,
				"XOR", "D FLIP_FLOP" ," Wire ",  // 1, 2 , 3
				"CLOCK " ," ", " " , // 4 , 5 ,6

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
		graph waveRightPanel ;//= new graph() ;// = new exp1_graph();

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

	

		public class FormattedTextFieldDemo extends JPanel implements PropertyChangeListener{


		
			
			//Labels to identify the fields
			private JLabel x_0,x_1,x_2,x_3,x_4,x_5,x_6,x_7,x_8,x_9,x_10,x_11,x_12,x_13,x_14,x_15,sig1,sig2,sig3,sig4,sig5,sig6,sig7,sig8,sig9,sig10,sig11,sig12,sig13,sig14,sig15;

			//Strings for the labels
			private String x0 = "x^0",x1 = "x^1",x2 = "x^2",x3 = "x^3",x4 = "x^4",x5 = "x^5",x6 = "x^6",x7 = "x^7",x8 = "x^8",x9 = "x^9",x10 = "x^10",x11 = "x^11",x12 = "x^12",x13 = "x^13",x14 = "x^14",x15 = "x^15";
			private String sign=" +";
			//Fields for data entry
			private JFormattedTextField x0f,x1f,x2f,x3f,x4f,x5f,x6f,x7f,x8f,x9f,x10f,x11f,x12f,x13f,x14f,x15f;

			//Coefficients
			private int x0c,x1c,x2c,x3c,x4c,x5c,x6c,x7c,x8c,x9c,x10c,x11c,x12c,x13c,x14c,x15c;

			public FormattedTextFieldDemo() {
				super(new BorderLayout());
				//       setUpFormats();
				//       double payment = computePayment(amount,rate,numPeriods);

				//Create the labels.
				x_0 = new JLabel(x0);
				x_1 = new JLabel(x1);
				x_2 = new JLabel(x2);
				x_3 = new JLabel(x3);
				x_4 = new JLabel(x4);
				x_5 = new JLabel(x5);
				x_6 = new JLabel(x6);
				x_7 = new JLabel(x7);
				x_8 = new JLabel(x8);
				x_9 = new JLabel(x9);
				x_10 = new JLabel(x10);
				x_11 = new JLabel(x11);
				x_12 = new JLabel(x12);
				x_13 = new JLabel(x13);
				x_14 = new JLabel(x14);
				x_15 = new JLabel(x15);
				sig1 = new JLabel(sign);
				sig2 = new JLabel(sign);
				sig3 = new JLabel(sign);
				sig4 = new JLabel(sign);
				sig5 = new JLabel(sign);
				sig6 = new JLabel(sign);
				sig7 = new JLabel(sign);
				sig8 = new JLabel(sign);
				sig9 = new JLabel(sign);
				sig10 = new JLabel(sign);
				sig11 = new JLabel(sign);
				sig12 = new JLabel(sign);
				sig13 = new JLabel(sign);
				sig14 = new JLabel(sign);
				sig15 = new JLabel(sign);

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
					x8f.addPropertyChangeListener("value", this);
					x9f = new JFormattedTextField(new MaskFormatter("*"));
					x9f.setValue(new Integer(0));
					x9f.setColumns(1);
					x9f.addPropertyChangeListener("value", this);
					x10f = new JFormattedTextField(new MaskFormatter("*"));
					x10f.setValue(new Integer(0));
					x10f.setColumns(1);
					x10f.addPropertyChangeListener("value", this);
					x11f = new JFormattedTextField(new MaskFormatter("*"));
					x11f.setValue(new Integer(0));
					x11f.setColumns(1);
					x11f.addPropertyChangeListener("value", this);
					x12f = new JFormattedTextField(new MaskFormatter("*"));
					x12f.setValue(new Integer(0));
					x12f.setColumns(1);
					x12f.addPropertyChangeListener("value", this);
					x13f = new JFormattedTextField(new MaskFormatter("*"));
					x13f.setValue(new Integer(0));
					x13f.setColumns(1);
					x13f.addPropertyChangeListener("value", this);
					x14f = new JFormattedTextField(new MaskFormatter("*"));
					x14f.setValue(new Integer(0));
					x14f.setColumns(1);
					x14f.addPropertyChangeListener("value", this);
					x15f = new JFormattedTextField(new MaskFormatter("*"));
					x15f.setValue(new Integer(0));
					x15f.setColumns(1);
				}
				catch(Exception e){
				}

				//Tell accessibility tools about label/textfield pairs.
				x_0.setLabelFor(x0f);
				x_1.setLabelFor(x1f);
				x_2.setLabelFor(x2f);
				x_3.setLabelFor(x3f);
				x_4.setLabelFor(x4f);
				x_5.setLabelFor(x5f);
				x_6.setLabelFor(x6f);
				x_7.setLabelFor(x7f);
				x_8.setLabelFor(x8f);
				x_9.setLabelFor(x9f);
				x_10.setLabelFor(x10f);
				x_11.setLabelFor(x11f);
				x_12.setLabelFor(x12f);
				x_13.setLabelFor(x13f);
				x_14.setLabelFor(x14f);
				x_15.setLabelFor(x15f);

				JButton button1 = new JButton("Submit");
				//Lay out the labels in a panel.
				JPanel labelPane = new JPanel(new GridLayout(1,1));

				switch(number){
				case 2:
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);

					break;
				case 3:
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 4:
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
										
					break;
				case 5:
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 6:
					
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 7:
					
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 8:
					
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 9:
					
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					
					break;
				case 10:
					
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 11:
					
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 12:
					
					labelPane.add(x11f);
					labelPane.add(x_11);
					labelPane.add(sig11);
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 13:
					
					labelPane.add(x12f);
					labelPane.add(x_12);
					labelPane.add(sig12);
					labelPane.add(x11f);
					labelPane.add(x_11);
					labelPane.add(sig11);
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 14:
					
					labelPane.add(x13f);
					labelPane.add(x_13);
					labelPane.add(sig13);
					labelPane.add(x12f);
					labelPane.add(x_12);
					labelPane.add(sig12);
					labelPane.add(x11f);
					labelPane.add(x_11);
					labelPane.add(sig11);
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 15:
					
					labelPane.add(x14f);
					labelPane.add(x_14);
					labelPane.add(sig14);
					labelPane.add(x13f);
					labelPane.add(x_13);
					labelPane.add(sig13);
					labelPane.add(x12f);
					labelPane.add(x_12);
					labelPane.add(sig12);
					labelPane.add(x11f);
					labelPane.add(x_11);
					labelPane.add(sig11);
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				case 16:
					
					labelPane.add(x15f);
					labelPane.add(x_15);
					labelPane.add(sig15);
					labelPane.add(x14f);
					labelPane.add(x_14);
					labelPane.add(sig14);
					labelPane.add(x13f);
					labelPane.add(x_13);
					labelPane.add(sig13);
					labelPane.add(x12f);
					labelPane.add(x_12);
					labelPane.add(sig12);
					labelPane.add(x11f);
					labelPane.add(x_11);
					labelPane.add(sig11);
					labelPane.add(x10f);
					labelPane.add(x_10);
					labelPane.add(sig10);
					labelPane.add(x9f);
					labelPane.add(x_9);
					labelPane.add(sig9);
					labelPane.add(x8f);
					labelPane.add(x_8);
					labelPane.add(sig8);
					labelPane.add(x7f);
					labelPane.add(x_7);
					labelPane.add(sig7);
					labelPane.add(x6f);
					labelPane.add(x_6);
					labelPane.add(sig6);
					labelPane.add(x5f);
					labelPane.add(x_5);
					labelPane.add(sig5);
					labelPane.add(x4f);
					labelPane.add(x_4);
					labelPane.add(sig4);
					labelPane.add(x3f);
					labelPane.add(x_3);
					labelPane.add(sig3);
					labelPane.add(x2f);
					labelPane.add(x_2);
					labelPane.add(sig2);
					labelPane.add(x1f);
					labelPane.add(x_1);
					labelPane.add(sig1);
					labelPane.add(x0f);
					labelPane.add(x_0);
					break;
				}
				JPanel buttonPane = new JPanel(new GridLayout(0,number-1));
				//        button.setFont(new java.awt.Font("Arial", 1, 25)); 
				buttonPane.add(button1);
				//Layout the text fields in a panel.
				/*	        JPanel fieldPane = new JPanel(new GridLayout(0,1));
	        fieldPane.add(amountField);
	        fieldPane.add(rateField);
	        fieldPane.add(numPeriodsField);
	        fieldPane.add(paymentField);
				 */ 
				 //Put the panels in this panel, labels on left,
				 //text fields on right.
				 setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
				 add(labelPane, BorderLayout.CENTER);
				 add(buttonPane, BorderLayout.SOUTH);
				 
				 button1.addActionListener(new ActionListener(){
					 public void actionPerformed(ActionEvent ax){
						 	 a0=x0c;a1=x1c;a2=x2c;a3=x3c;a4=x4c;a5=x5c;a6=x6c;a7=x7c;a8=x8c;a9=x9c;a10=x10c;a11=x11c;a12=x12c;a13=x13c;a14=x14c;a15=x15c;
							 coeff[0]=a0;coeff[1]=a1;coeff[2]=a2;coeff[3]=a3;coeff[4]=a4;coeff[5]=a5;coeff[6]=a6;coeff[7]=a7;coeff[8]=a8;coeff[9]=a9;
							 coeff[10]=a10;coeff[11]=a11;coeff[12]=a12;coeff[13]=a13;coeff[14]=a14;coeff[15]=a15;
							 for(int i=number-1;i>=0;i--)
							 {
								 System.out.print("*"+coeff[i]+"*");
							 }
							 System.out.println();
							 if(flag==1)
							 {
								 frame.setVisible(true);
								 flag=0;

							 }
							 else
							 {
								 s = a0+a1+a2+a3+a4+a5+a6+a7+a8+a9+a10+a11+a12+a13+a14+a15-1;
								 total_comp=total_comp-nmos_count;
								 nmos_count=0;
								 workPanel.repaint();
								 frame.setVisible(false);
							 }
							 if(coeff[0]==0)
								 set_output=1;
							 else
								 set_output=0;
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
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x1f) {
		            x1c = ((Number)x1f.getValue()).intValue();
		            if(x1c >1)
		        	{
		        		 x1f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x2f) {
		            x2c = ((Number)x2f.getValue()).intValue();
		            if(x2c >1)
		        	{
		        		 x2f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x3f) {
		            x3c = ((Number)x3f.getValue()).intValue();
		            if(x3c >1)
		        	{
		        		 x3f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x4f) {
			            x4c = ((Number)x4f.getValue()).intValue();
			            if(x4c >1)
			        	{
			        		 x4f.setValue(new Integer(0));
			        		 flag=1;
			        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
			        	}
		        } else if (source == x5f) {
		            x5c = ((Number)x5f.getValue()).intValue();
		            if(x5c >1)
		        	{
		        		 x5f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x6f) {
		            x6c = ((Number)x6f.getValue()).intValue();
		            if(x6c >1)
		        	{
		        		 x6f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x7f) {
		            x7c = ((Number)x7f.getValue()).intValue();
		            if(x7c >1)
		        	{
		        		 x7f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x8f) {
		            x8c = ((Number)x8f.getValue()).intValue();
		            if(x8c >1)
		        	{
		        		 x8f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x9f) {
		            x9c = ((Number)x9f.getValue()).intValue();
		            if(x9c >1)
		        	{
		        		 x9f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x10f) {
		            x10c = ((Number)x10f.getValue()).intValue();
		            if(x10c >1)
		        	{
		        		 x10f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x11f) {
		            x11c = ((Number)x11f.getValue()).intValue();
		            if(x11c >1)
		        	{
		        		 x11f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x12f) {
		            x12c = ((Number)x12f.getValue()).intValue();
		            if(x12c >1)
		        	{
		        		 x12f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x13f) {
		            x13c = ((Number)x13f.getValue()).intValue();
		            if(x13c >1)
		        	{
		        		 x13f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x14f) {
		            x14c = ((Number)x14f.getValue()).intValue();
		            if(x14c >1)
		        	{
		        		 x14f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x15f) {
		            x15c = ((Number)x15f.getValue()).intValue();
		            if(x15c >1)
		        	{
		        		 x15f.setValue(new Integer(0));
		        		 flag=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } 
		        
		    }
		}
		
	public class FormattedTextField extends JPanel implements PropertyChangeListener{


		
			
			//Labels to identify the fields
			private JLabel x_0,x_1,x_2,x_3,x_4,x_5,x_6,x_7,x_8,x_9,x_10,x_11,x_12,x_13,x_14,x_15;

			//Strings for the labels
			
			//Fields for data entry
			private JFormattedTextField x0f,x1f,x2f,x3f,x4f,x5f,x6f,x7f,x8f,x9f,x10f,x11f,x12f,x13f,x14f,x15f;

			//Coefficients
			private int x0c,x1c,x2c,x3c,x4c,x5c,x6c,x7c,x8c,x9c,x10c,x11c,x12c,x13c,x14c,x15c;

			public FormattedTextField() {
				super(new BorderLayout());
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
					x8f.addPropertyChangeListener("value", this);
					x9f = new JFormattedTextField(new MaskFormatter("*"));
					x9f.setValue(new Integer(0));
					x9f.setColumns(1);
					x9f.addPropertyChangeListener("value", this);
					x10f = new JFormattedTextField(new MaskFormatter("*"));
					x10f.setValue(new Integer(0));
					x10f.setColumns(1);
					x10f.addPropertyChangeListener("value", this);
					x11f = new JFormattedTextField(new MaskFormatter("*"));
					x11f.setValue(new Integer(0));
					x11f.setColumns(1);
					x11f.addPropertyChangeListener("value", this);
					x12f = new JFormattedTextField(new MaskFormatter("*"));
					x12f.setValue(new Integer(0));
					x12f.setColumns(1);
					x12f.addPropertyChangeListener("value", this);
					x13f = new JFormattedTextField(new MaskFormatter("*"));
					x13f.setValue(new Integer(0));
					x13f.setColumns(1);
					x13f.addPropertyChangeListener("value", this);
					x14f = new JFormattedTextField(new MaskFormatter("*"));
					x14f.setValue(new Integer(0));
					x14f.setColumns(1);
					x14f.addPropertyChangeListener("value", this);
					x15f = new JFormattedTextField(new MaskFormatter("*"));
					x15f.setValue(new Integer(0));
					x15f.setColumns(1);
				}
				catch(Exception e){
				}

				//Tell accessibility tools about label/textfield pairs.
			/*	x_0.setLabelFor(x0f);
				x_1.setLabelFor(x1f);
				x_2.setLabelFor(x2f);
				x_3.setLabelFor(x3f);
				x_4.setLabelFor(x4f);
				x_5.setLabelFor(x5f);
				x_6.setLabelFor(x6f);
				x_7.setLabelFor(x7f);
				x_8.setLabelFor(x8f);
				x_9.setLabelFor(x9f);
				x_10.setLabelFor(x10f);
				x_11.setLabelFor(x11f);
				x_12.setLabelFor(x12f);
				x_13.setLabelFor(x13f);
				x_14.setLabelFor(x14f);
				x_15.setLabelFor(x15f);
*/
				JButton button2 = new JButton("Submit");
				//Lay out the labels in a panel.
				JPanel labelPane = new JPanel(new GridLayout(1,1));

				switch(number){
				case 2:
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 3:
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 4:
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 5:
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 6:
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 7:
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 8:
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 9:
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 10:
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 11:
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 12:
					labelPane.add(x11f);
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 13:
					labelPane.add(x12f);
					labelPane.add(x11f);
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 14:
					labelPane.add(x13f);
					labelPane.add(x12f);
					labelPane.add(x11f);
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 15:
					labelPane.add(x14f);
					labelPane.add(x13f);
					labelPane.add(x12f);
					labelPane.add(x11f);
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				case 16:
					labelPane.add(x15f);
					labelPane.add(x14f);
					labelPane.add(x13f);
					labelPane.add(x12f);
					labelPane.add(x11f);
					labelPane.add(x10f);
					labelPane.add(x9f);
					labelPane.add(x8f);
					labelPane.add(x7f);
					labelPane.add(x6f);
					labelPane.add(x5f);
					labelPane.add(x4f);
					labelPane.add(x3f);
					labelPane.add(x2f);
					labelPane.add(x1f);
					labelPane.add(x0f);
					break;
				}
				JPanel buttonPane = new JPanel(new GridLayout(0,number-1));
				//        button.setFont(new java.awt.Font("Arial", 1, 25)); 
				buttonPane.add(button2);
				//Layout the text fields in a panel.
				/*	        JPanel fieldPane = new JPanel(new GridLayout(0,1));
	        fieldPane.add(amountField);
	        fieldPane.add(rateField);
	        fieldPane.add(numPeriodsField);
	        fieldPane.add(paymentField);
				 */ 
				 //Put the panels in this panel, labels on left,
				 //text fields on right.
				 setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
				 add(labelPane, BorderLayout.CENTER);
				 add(buttonPane, BorderLayout.SOUTH);
				 
				 button2.addActionListener(new ActionListener(){
					 public void actionPerformed(ActionEvent ae){
						 seed[0]=x0c;seed[1]=x1c;seed[2]=x2c;seed[3]=x3c;seed[4]=x4c;seed[5]=x5c;seed[6]=x6c;seed[7]=x7c;seed[8]=x8c;
							 seed[9]=x9c;seed[10]=x10c;seed[11]=x11c;seed[12]=x12c;seed[13]=x13c;seed[14]=x14c;seed[15]=x15c;
						/*	 for(int i=0;i<number;i++)
							 {
								 System.out.print(seed[i]);
							 }*/
							 if(flag1==1)
							 {
								 frame1.setVisible(true);
								 flag1=0;

							 }
							 else
							 {
								 frame1.setVisible(false);
							 }
						
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
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x1f) {
		            x1c = ((Number)x1f.getValue()).intValue();
		            if(x1c >1)
		        	{
		        		 x1f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x2f) {
		            x2c = ((Number)x2f.getValue()).intValue();
		            if(x2c >1)
		        	{
		        		 x2f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x3f) {
		            x3c = ((Number)x3f.getValue()).intValue();
		            if(x3c >1)
		        	{
		        		 x3f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x4f) {
			            x4c = ((Number)x4f.getValue()).intValue();
			            if(x4c >1)
			        	{
			        		 x4f.setValue(new Integer(0));
			        		 flag1=1;
			        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
			        	}
		        } else if (source == x5f) {
		            x5c = ((Number)x5f.getValue()).intValue();
		            if(x5c >1)
		        	{
		        		 x5f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x6f) {
		            x6c = ((Number)x6f.getValue()).intValue();
		            if(x6c >1)
		        	{
		        		 x6f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x7f) {
		            x7c = ((Number)x7f.getValue()).intValue();
		            if(x7c >1)
		        	{
		        		 x7f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x8f) {
		            x8c = ((Number)x8f.getValue()).intValue();
		            if(x8c >1)
		        	{
		        		 x8f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x9f) {
		            x9c = ((Number)x9f.getValue()).intValue();
		            if(x9c >1)
		        	{
		        		 x9f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x10f) {
		            x10c = ((Number)x10f.getValue()).intValue();
		            if(x10c >1)
		        	{
		        		 x10f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x11f) {
		            x11c = ((Number)x11f.getValue()).intValue();
		            if(x11c >1)
		        	{
		        		 x11f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x12f) {
		            x12c = ((Number)x12f.getValue()).intValue();
		            if(x12c >1)
		        	{
		        		 x12f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x13f) {
		            x13c = ((Number)x13f.getValue()).intValue();
		            if(x13c >1)
		        	{
		        		 x13f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x14f) {
		            x14c = ((Number)x14f.getValue()).intValue();
		            if(x14c >1)
		        	{
		        		 x14f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } else if (source == x15f) {
		            x15c = ((Number)x15f.getValue()).intValue();
		            if(x15c >1)
		        	{
		        		 x15f.setValue(new Integer(0));
		        		 flag1=1;
		        		 JOptionPane.showMessageDialog(null,"Value Can only be 0 or 1","Virtual Labs", 1);
		        	}
		        } 
		        
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

		leftPanel.setLayout(new BorderLayout());
		leftPanel.setMinimumSize(new Dimension(900 , 1000)); // for fixing size

		leftSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , toolPanel , workPanel); // spliting left in tool & work
		leftSplitPane.setOneTouchExpandable(true); // this for one touch option 
		leftSplitPane.setDividerLocation(0.1);
		leftPanel.add(leftSplitPane, BorderLayout.CENTER);
		int i;
		for (i=3;i<=3;i++)
		{
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

		selected.setBackground(Color.green);
		// selected.setToolTipText(comp_str[0]); // setting name at hovering of mouse 

		toolPanel.add(toolPanelUp , BorderLayout.CENTER);
		toolPanel.add(toolPanelDown , BorderLayout.SOUTH);
		selected.setBorder(BorderFactory.createTitledBorder(" SELECTED ICON "));
		toolPanelDown.setBorder(BorderFactory.createTitledBorder(" AVAILABLE ICONS "));


		toolPanelUp.setLayout(new BorderLayout());
		toolPanelUp.add(selected, BorderLayout.NORTH);
		toolPanelUp.add(new JLabel("<html><br/><br/><br/><html/>"), BorderLayout.SOUTH);

		toolPanelDown.add(leftTool1);
		toolPanelDown.add(leftTool2);

		leftTool1.setFloatable(false);
		leftTool2.setFloatable(false);
		rightPanel.setLayout(new BorderLayout()); 
		rightPanel.setBackground(Color.gray); 
		
		
		//Scrollbar for graph
		/*JScrollBar r = new JScrollBar();
		rightPanel.add(r,BorderLayout.SOUTH);
		r.setMaximum (100);
		r.setOrientation (JScrollBar.HORIZONTAL);
		
		JScrollBar p = new JScrollBar();
		leftPanel.add(p,BorderLayout.SOUTH);
		p.setMaximum (100);
		p.setOrientation (JScrollBar.HORIZONTAL);
		*/
		JLabel wave_head = new JLabel ( "<html><FONT COLOR=white SIZE=6 ><B>SIMULATION OF CIRCUIT</B></FONT><br><br></html>", JLabel.CENTER);
		wave_head.setBorder(BorderFactory.createRaisedBevelBorder( ));


		waveRightPanel = new graph() ;// = new exp1_graph();
		waveRightPanel.setSize(3000, 500);
		waveRightPanel.setAutoscrolls(true);
		rightPanel.add(waveRightPanel, BorderLayout.CENTER);
		rightPanel.add(wave_head, BorderLayout.NORTH);
		rightPanel.setVisible(false);
		
		
		JButton stopButton = new JButton("PAUSE");
		stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent se){
				if(simulate_flag==false)
				{
					JOptionPane.showMessageDialog(null,"WARNING : First Simulate the Circuit !!!","Virtual Labs", 1);
				}
				else
				{
					pause_flag=1;
					for(double i=0;i < 900000000 ;i++){}

				}
			}
		});
		JButton playButton = new JButton("RESUME");
		playButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent pa){
				if(simulate_flag==false)
				{
					JOptionPane.showMessageDialog(null,"WARNING : First Simulate the Circuit !!!","Virtual Labs", 1);
				}
				else
				{
					pause_flag=0;
				}
			}
		});


		splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , rightPanel);
		splitPane.setOneTouchExpandable(true); // this for one touch option 
		splitPane.setDividerLocation(0.2);
		add(splitPane, BorderLayout.CENTER);


		add(topPanel , BorderLayout.NORTH);
		topPanel.setBackground(Color.gray);
		JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 50 , 5 )) ;
		JLabel heading = new JLabel (  "<html><FONT COLOR=WHITE SIZE=18 ><B>LINEAR FEEDBACK SHIFT REGISTOR</B></FONT></html>", JLabel.CENTER);
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
		simulate_button = new JButton(" SIMULATE " , icon_simulate );
		icon_simulate.setImageObserver(simulate_button);

		JLabel headin = new JLabel ("Select Bits :",JLabel.LEFT);
		String[] my_list = {"2","3","4","5","6","7","8"};
		exp_list = new JComboBox (my_list);
		exp_list.setSelectedIndex(6);
		exp_list.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ee) {
				flag_order=1;
				set_output=0;
				//	JOptionPane.showMessageDialog(null, total_comp+".."+pmos_count+"..."+nmos_count,"Virtual Labs", 1);
				for(int i=0;i<total_comp;i++)
				{
					comp_node[i].remove_mat();
				}
				stopper=1;
				simulate_flag=false;
				pmos_count=nmos_count=input_count=output_count=s=pause_flag=jug1=f1=jug2=f2=flag1=total_comp=total_wire=0;
				number=Integer.parseInt(exp_list.getSelectedItem().toString());
				//	JOptionPane.showMessageDialog(null, "You entered the number : " + number,"Virtual Labs", 1);
				//	wire[wire_mat[work_x][work_y]].del();
				workPanel.repaint();
			}
		});


		graph_button = new JButton (" FULL GRAPH " , icon_graph);
		icon_graph.setImageObserver(graph_button);
		simulate_button.setToolTipText("For Simulation");
		//	JFrame frame = new JFrame("Input Dialog Box Frame");
		JButton button = new JButton("Enter Coeff. of Polynomial:");
		JButton button1 = new JButton("Enter The Seed");
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent az){
				frame1 = new JFrame("FormattedTextField");
				frame1.add(new FormattedTextField());
				frame1.pack();
				if(flag_order==2)
				{
					flag_order=0;
					frame1.setVisible(true);
				}
				else
				{
					frame1.setVisible(false);
					if(flag_order==0)
					{
						JOptionPane.showMessageDialog(null,"WARNING : Select Number of Bits !!!","Virtual Labs", 1);
					}
					else if(flag_order==1)
					{
						JOptionPane.showMessageDialog(null,"WARNING : Enter the Polynomial !!!","Virtual Labs", 1);	
					}
				}
			}
		});
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent aec){
				frame = new JFrame("FormattedTextFieldDemo");
				frame.add(new FormattedTextFieldDemo());
				frame.pack();
				if(flag_order==1)
				{
					flag_order=2;
					frame.setVisible(true);
				}
				else
				{
					frame.setVisible(false);
					JOptionPane.showMessageDialog(null,"WARNING : Select Number of Bits !!!","Virtual Labs", 1);
				}
			}
		});
		JPanel panel = new JPanel();
		panel.add(button);
		JPanel panel1 = new JPanel();
		panel1.add(button1);
		//	frame.add(panel);
		//	frame.setSize(400, 400);
		//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//	frame.setVisible(true);

		headButton.add(headin);
		headButton.add(exp_list );
		headButton.add(button);
		headButton.add(button1);

		headButton.add(simulate_button );
//		headButton.add(graph_button );
		headButton.add(playButton);
		headButton.add(stopButton);

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

			int check_points_value[][] = new int[115][115] ; // each index will store corresponding  component for points of (comp point no -> index )
			int i = 0 , j , j1 = 0, k1 = 0 , j2 = 0 , k2 = 0 , l = 0 ;
			for (  i = 1 ; i <= 113 ; i ++ )
			{
				for (  j = 1 ; j <= 113 ; j ++ )
				{
					check_points_value[i][j] = -1 ;
				}
			}
	//		System.out.println(total_wire);
			for (  i = 0 ; i < total_wire ; i ++ )
			{
				j1 = wire[i].x1;
				k1 = wire[i].y1;
				j2 = wire[i].x2;
				k2 = wire[i].y2;

				if (wire[i].del == false &&  end_points_mat[j1][k1] != -1 && end_points_mat[j2][k2] != -1 )
				{

					l = 1 ;
					while ( check_points_value[ end_points_mat[j1][k1] ][l] != -1 )
					{
						l++;
					}
					check_points_value[end_points_mat[j1][k1]][l] =  end_points_mat[j2][k2];

					l = 1 ;
					while ( check_points_value[ end_points_mat[j2][k2] ][l] != -1 )
					{
						l++;
					}
					check_points_value[end_points_mat[j2][k2]][l] =  end_points_mat[j1][k1];
				}


			}
			// Testing Clock Connections
			int test[]=new int[65];
			for(i=1; i<=number; i++)
			{
				l=1;
				while(check_points_value[i][l]!=-1)
				{
					l++;
				}
				if(l>2)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Clock can be connected to only one D-FLIP FLOP !!!");
					return false;
				}
				if(l==1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Clock has to be connected to the D-FLIP FLOP !!!");
					return false;
				}
				if(check_points_value[i][1] <33 || check_points_value[i][1]>48)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Connect Clock to the proper input terminal of D-FLIP FLOP !!!");
					return false;
				}
				test[check_points_value[i][1]]++;
			}
			for(i=1;i<=64;i++)
			{
				if(test[i]>1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : More than one Clock is connected to input terminal of D-FLIP FLOP !!!");
					return false;
				}
			}
			
			//Testing D-Flip Flop Connections
			int test1[]=new int[115];
			int test2[]=new int[97];
			for(i=1;i< number;i++)
			{
				l=1;
				while(check_points_value[3*16+i][l]!=-1)
				{
					l++;
				}
				if(coeff[number-i]==1)
				{
					if(l>3 || l<=2)
					{
						JOptionPane.showMessageDialog(null, "ERROR : Number "+i+" D-Flip Flop output has to be connected to two input ports !!!");
						return false;
					}
				}
				else
				{
					if(l>2)
					{
						JOptionPane.showMessageDialog(null, "ERROR : Number "+i+" D-Flip Flop output can be connected to only one input port !!!");
						return false;
					}
				}
				if(l==1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Number "+i+" D-FLIP FLOP output is not connected to the input terminal of other D-FLIP FLOP !!!");
					return false;
				}
				if(coeff[number-i]==0)
				{
					if(check_points_value[3*16+i][1] != 17+i)
					{
						JOptionPane.showMessageDialog(null, "ERROR : Connect Number "+i+" D-FLIP FLOP to the proper input terminal of other D-FLIP FLOP !!!");
						return false;
					}
					test1[check_points_value[3*16+i][1]]++;
				}
				else
				{
					if((check_points_value[3*16+i][1] !=17 +i) &&( check_points_value[3*16+i][2] !=17 +i))
					{
						JOptionPane.showMessageDialog(null, "ERROR : Connect Number "+i+" D-FLIP FLOP to the proper input terminal of other D-FLIP FLOP !!!");
						return false;
					}
					if(s!=0)
					{
						if((check_points_value[3*16+i][1] <65 || check_points_value[3*16+i][1]>96) &&( check_points_value[3*16+i][2] <65 || check_points_value[3*16+i][2]>96))
						{
							JOptionPane.showMessageDialog(null, "ERROR : Connect Number "+i+" D-FLIP FLOP to the proper input terminal of XOR Gate  !!!");
							return false;
						}
					}
					else
					{
						if((check_points_value[3*16+i][1] !=17) &&( check_points_value[3*16+i][2] !=17))
						{
							JOptionPane.showMessageDialog(null, "ERROR : Connect Number "+i+" D-FLIP FLOP to the proper input terminal of First D-FLIP FLOP  !!!");
							return false;
						}
					}
					if(check_points_value[3*16+i][1] !=17 + i)
					{
						test1[check_points_value[3*16+i][2]]++;
						test2[check_points_value[3*16+i][1]]++;
					}
					else
					{
						test1[check_points_value[3*16+i][1]]++;
						test2[check_points_value[3*16+i][2]]++;
					}
				}
			}
			l=1;
			i=number;
			while(check_points_value[3*16+i][l]!=-1)
			{
				l++;
			}
			if(coeff[number-i]==1)
			{
				if(l!=2)
				{
					JOptionPane.showMessageDialog(null, "ERROR : EndMost D-Flip Flop output has to be connected to a XOR Gate !!!");
					return false;
				}
			}
			else
			{
				if(l>2)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Endmost D-Flip Flop output can be connected to only one output port !!!");
					return false;
				}
			}
			if(l==1)
			{
				JOptionPane.showMessageDialog(null, "ERROR : EndMost D-FLIP FLOP output is not connected to the output terminal !!!");
				return false;
			}
			if(coeff[number-i]==0)
			{
				if(check_points_value[3*16+i][1] != 113)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Connect EndMost D-FLIP FLOP to the output terminal  !!!");
					return false;
				}
				test1[check_points_value[3*16+i][1]]++;
			}
			else
			{
				if((check_points_value[3*16+i][1] <65 || check_points_value[3*16+i][1]>96))
				{
					JOptionPane.showMessageDialog(null, "ERROR : Connect EndMost D-FLIP FLOP to the proper input terminal of XOR Gate  !!!");
					return false;
				}
				else
				{
					test2[check_points_value[3*16+i][1]]++;
				}
				
			}
			for(i=1;i<=32;i++)
			{
				if(test1[i]>1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : More than one D-FLIP FLOP output is connected to the input terminal of D-FLIP FLOP !!!");
					return false;
				}
			}
			if(test1[113]>1)
			{
				JOptionPane.showMessageDialog(null, "ERROR : More than one D-FLIP FLOP output is connected to the output terminal !!!");
				return false;
			}
			for(i=64;i<=96;i++)
			{
				if(test2[i]>1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : More than one D-FLIP FLOP output is connected to the input terminal of XOR Gate !!!");
					return false;
				}
			}
			
			//Checking for XOR GATES
			int comp_flag=0;
			for(i=1;i<=s;i++)
			{
				l=1;
				while(check_points_value[6*16+i][l]!=-1)
				{
					l++;
				}
				if(l>2)
				{
					JOptionPane.showMessageDialog(null, "ERROR : XOR Gate output can be connected to only one XOR Gate input terminal !!!");
					return false;

				}
				if(l==1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : XOR Gate output is not connected !!!");
					return false;
				}

				if(check_points_value[6*16+i][1] == 17)
				{
					continue;
				}
				if(check_points_value[6*16+i][1] < 65 || check_points_value[6*16+i][1] > 96)
				{
					JOptionPane.showMessageDialog(null, "ERROR : XOR Gate output has to be connected !!!");
					return false;
				}
				
			}
			for(i=1;i<=number;i++)
			{
				if(check_points_value[i][1]==-1 || (check_points_value[16+i][1]==-1 && s!=-1) || check_points_value[16*2+i][1]==-1 || check_points_value[16*3+i][1]==-1 )
				{
					JOptionPane.showMessageDialog(null, "ERROR : Circuit is not complete, Please draw correct circuit and simulate again !!!");
					return false;
				}
			}
			for(i=1;i<=s;i++)
			{
				if(check_points_value[16*4+i][1]==-1 || check_points_value[16*5+i][1]==-1 ||check_points_value[16*6+i][1]==-1)
				{
					JOptionPane.showMessageDialog(null, "ERROR : Circuit is not complete, Please draw correct circuit and simulate again !!!");
					return false;
				}
			}
			for (  i = 1 ; i <= 113 ; i ++ )
			{
				l = 1 ;
				System.out.println("point is" +i+check_points_value[i][1]);
			}
			return true;
		}
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == simulate_button )
			{
				if(circuit_check())
				{
					if(number==0)
					{
						JOptionPane.showMessageDialog(null, "Circuit is not Complete, Please Complete it and press simulate again :)");
						return;
					}
					stopper=1;
					int num=number;
					rightPanel.setVisible(true);
					splitPane.setDividerLocation(0.2);
					rightPanel.scrollRectToVisible(getVisibleRect());
					//splitPane.scrollRectToVisible(getVisibleRect());
					waveRightPanel.make_graph(num);// Read file OUTFILE and draw the 
					waveRightPanel.repaint();
					
					simulate_flag = true ;
				}
				else
				{
				//	JOptionPane.showMessageDialog(null, "Circuit is not Complete , Please Complete it and press simulate again :)");
				}

			}

			else if (e.getSource() == img_button1[1] )
			{
				//	if ( comp_count[1] == 0 )
				//	{
				img_button_pressed = 1 ;
				change_selected(1);
				//	}
				//				else
				//				{
				//					JOptionPane.showMessageDialog(null, "You already have this component !! :)");
				//				}
			}
			else if (e.getSource() == img_button1[2] )
			{
				//			if ( comp_count[2] == 0 )
				//			{
				img_button_pressed = 2 ;
				change_selected(2);
				//			}
				//			else
				//			{
				//				JOptionPane.showMessageDialog(null, "You already have this component !! :)");
				//			}
			}
			
			else if (e.getSource() == img_button1[3] )
			{
				//	if ( comp_count[7] == 0 )
				//	{
				img_button_pressed = 3 ;
				change_selected(3);
				//	}
				//	else
				//	{
				//		JOptionPane.showMessageDialog(null, "You already have this component !! :)");
				//	}
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

			public node (int x , int y , int no , int w , int h,int num)
			{
				node_x = x ;
				node_y = y ;
				if(no==1)
				{
					int i;
					if(pmos_delete_count!=0)
					{
						for(i=0;i<pmos_delete_count;i++)
						{
							if(pmos_delete[i]==-1)
								continue;
							node_number=pmos_delete[i];
							pmos_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=num;
					}
				}
				else if(no==2)
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
						node_number=num;
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
							node_number=input_delete[i];
							input_delete[i]=-1;
							break;
						}
					}
					else
					{
						node_number=num;
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
						node_number=num;
					}
				}
				img_no = no ;
				del = false ;

				virtual_w = width = w  ;
				virtual_h = height = h  ;
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
				}
				if (angle_count == 0 ) // 90 degree
				{
					virtual_w = width;
					virtual_h = height  ;
				}
				else if (angle_count == 1 ) // 90 degree
				{
					virtual_w = height;
					virtual_h = width  ;
				}
				else if (angle_count == 2 ) // 180 degree
				{
					virtual_w = width ;
					virtual_h = height ;
				}
				else if (angle_count == 3 ) // 270 degree
				{
					virtual_w = height ;
					virtual_h = width ;
				}
				update_mat(index); // update the matrix value to work_mat // index is the index of the node_comp matrix
			}

			public void remove_mat() // delete the previous value from work_mat
			{
				int i,j;	
				for ( i = node_x -width/2;  ;  )
				{
					if ( virtual_w > 0 && i >= node_x + virtual_w/2  ){break;}
					else if( virtual_w < 0 && i <= node_x + virtual_w/2  ){break;}
					for ( j = node_y -height/4;  ;  )
					{
						if ( virtual_h > 0 && j >= node_y + virtual_h/4 ){break;}
						else if( virtual_h < 0 && j <= node_y + virtual_h/4  ){break;}

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
					for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8 ; i ++ )
					{
						for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] +8; j ++ )
						{
							end_points_mat[i][j] = -1;
						}
					}

				}
			}

			public void update_end_points_mat(int img)
			{
				if ( img == 2 || img == 1)// Or
				{
					int count=0;
				//	System.out.println("Image No is " + img);
					
					if(img==2)
						count=1;
					else
						count=4;
					
					for ( int k = 0 ; k < 3 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8; i ++ )
						{
							for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] + 8; j ++ )
							{
								end_points_mat[i][j] = (count+k)*16+node_number+1 ; //total number of or = 1
							}
						}
					}
				}
				else if (img == 4 ) // ground , VDD , INPUT , OUTPUT
				{

					for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
					{
						for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
						{
							end_points_mat[i][j] =  node_number+1;
						}
					}
				}
				else if (img == 10 ) // ground , VDD , INPUT , OUTPUT
				{

					for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
					{
						for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
						{
							end_points_mat[i][j] =  113;
						}
					}
				}

			}
			public void make_end_points(int img )
			{
				if (  img == 2 ) //or
				{
					//System.out.println("(Image No is) " + img);
					count_end_points = 3;
					int a , b ,c,d,e,f ;
					a = - height/2  ;b = -height/8; 
					c =- height/2 ;d = height/8;
					e = +height/2;f = 0;
					
					if ( angle_count == 1 ){
						b = - height/2  ;a = -height/8; 
						d =- height/2 ;c = height/8;
						f = +height/2;e = 0;
					}
					
					else if ( angle_count == 2 )
					{
						a =  height/2  ;b = height/8; 
						c = height/2 ;d = -height/8;
						e = -height/2;f = 0;
					}
					
					else if ( angle_count == 3 )
					{
						b =  height/2  ;a = height/8; 
						d = height/2 ;c = -height/8;
						f = -height/2;e = 0;
					}



					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;
					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d;
					end_pointsX[2] = node_x + e ;
					end_pointsY[2] = node_y + f;
				}
				if (  img == 1 ) //or
				{
				//	System.out.println("(Image No is) " + img);
					count_end_points = 3;
					int a , b ,c,d,e,f ;
					a = -height/2 ;b = -height/8; 
					c = -height/2 ;d = height/8;
					e = height/2;f = 0;
					
					if ( angle_count == 1 ){
						b = -height/2 ;a = -height/8; 
						d = -height/2 ;c = height/8;
						f = height/2;e = 0;
					}
					
					else if ( angle_count == 2 )
					{
						a = height/2 ;b = height/8; 
						c = height/2 ;d = -height/8;
						e = -height/2;f = 0;
					}
					
					else if ( angle_count == 3 )
					{
						b = height/2 ;a = height/8; 
						d = height/2 ;c = -height/8;
						f = -height/2;e = 0;
					}



					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;
					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d;
					end_pointsX[2] = node_x + e ;
					end_pointsY[2] = node_y + f;
				}
				
				
				if ( img == 4 )     		// INPUT
				{
				    count_end_points = 1;
                    int a , b  ;
                    a = width / 2; b= 0 ;

                    if ( angle_count == 1 )
                    {
                            a = 0 ; b= width / 2 ;
                    }
                    else if ( angle_count == 2 )
                    {
                            a = -width / 2 ; b= 0 ;
                    }
                    else if ( angle_count == 3 )
                    {
                            a = 0 ; b= -width / 2 ;
                    }
                    end_pointsX[0] = node_x + a ;
                    end_pointsY[0] = node_y + b;

				}
				if ( img == 10 )     		// OUTPUT
				{
				    count_end_points = 1;
                    
                    end_pointsX[0] = node_x ;
                    end_pointsY[0] = node_y ;

				}
	
				update_end_points_mat(img);
			}
			public void update_mat(int index) // update the matrix value to work_mat // index is the index of the node_comp matrix
			{
				int i , j ;
				for ( i = node_x - width/2 ;  ;)
				{
					if ( virtual_w >= 0 && i >= node_x + virtual_w/2  ){break;}
					else if( virtual_w <= 0 && i <= node_x + virtual_w/2  ){break;}

					for ( j = node_y -height/4;  ;  )
					{
						if ( virtual_h >= 0 && j >= node_y +virtual_h/4  ){break;}
						else if( virtual_h <= 0 && j <= node_y +virtual_h/4 ){break;}

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

				super (fr , "Component Description " , true ); // true to lock the main screen 
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
					JLabel comp_name = new JLabel("<html><font size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);

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

					del = new JButton("Transistor Level");
					ok = new JButton("O.K");
					rotate = new JButton("Rotate Component");

					// layout.putConstraint(SpringLayout.WEST , c , 20,   SpringLayout.WEST , cp );
					// layout.putConstraint(SpringLayout.NORTH , c ,60,  SpringLayout.NORTH , cp);

					//                   layout.putConstraint(SpringLayout.WEST , capacitance , 20,   SpringLayout.EAST , c );
					//layout.putConstraint(SpringLayout.NORTH , capacitance , 60,  SpringLayout.NORTH , cp);

					//                 layout.putConstraint(SpringLayout.WEST , c_unit , 10,   SpringLayout.EAST , capacitance );
					//layout.putConstraint(SpringLayout.NORTH , c_unit , 60,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , del , 1,   SpringLayout.WEST , cp );
					layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , rotate , 4,   SpringLayout.EAST , del);
					layout.putConstraint(SpringLayout.NORTH , rotate , 100,  SpringLayout.NORTH , cp);
					layout.putConstraint(SpringLayout.WEST , ok , 4,   SpringLayout.EAST , rotate );
					layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);


					cp.add(comp_name);
					//               cp.add(c);
					//             cp.add(capacitance);
					// cp.add(c_unit);
					if(comp_node[node_index].img_no ==1 || comp_node[node_index].img_no==2)
					{
						cp.add(del);
					}
					cp.add(ok);
					cp.add(rotate);
					ok.addActionListener(this);
					del.addActionListener(this);
					rotate.addActionListener(this);


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
			/*		if ( image_no  == 3 ) //Capacitor 
					{
						hori_len = get_length();
					}
					else if ( image_no  == 8 ) //Capacitor 
					{
						veri_len = get_length();
					}*/
					setVisible(false);
					workPanel.repaint();
				}
				else if(e.getSource() == rotate )
				{

					comp_node[node_index].rotate(node_index);
					workPanel.repaint();
					//                                      System.out.println(comp_node[node_index].angle);
				}

				else if(e.getSource() == del )
				{
					//comp_node[node_index].del = true;
					//comp_count[comp_node[node_index].img_no] -= 1; // for descrising the count to check no of each comp
					int i , j ;

				//	comp_node[node_index].remove_mat();
					
					// updating values of comp in file -------------------------------
					if ( comp_node[node_index].img_no  == 1 ) //PMOS
					{
						ImageIcon xicon;
						java.net.URL imgURL = getClass().getResource("comp" + 11 + ".gif");
						if (imgURL != null)
						{
							xicon =  new ImageIcon(imgURL);
						}
						else
						{
							System.err.println("Couldn't find file: " );
							xicon =  null;
						}
						JOptionPane.showMessageDialog(null, null, "Transistor Level", JOptionPane.OK_OPTION, xicon); 
						
					}
					
					else if ( comp_node[node_index].img_no  == 2 ) //NMOS
					{
						ImageIcon xicon;
						java.net.URL imgURL = getClass().getResource("comp" + 22 + ".gif");
						if (imgURL != null)
						{
							xicon =  new ImageIcon(imgURL);
						}
						else
						{
							System.err.println("Couldn't find file: " );
							xicon =  null;
						}
						JOptionPane.showMessageDialog(null, null, "Transistor Level", JOptionPane.OK_OPTION, xicon); 
					}
								
					
					setVisible(false);
					workPanel.repaint();
				}
				

			}

		}


		class WA extends WindowAdapter
		{
			public void windowClosing( WindowEvent ev)
			{
				setVisible(true);
			}
		}


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
				//System.out.println("delete");
				update_mat(-1);
				del = true ;
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
				work_x = me.getX();
				work_y = me.getY();
				int i,j;
				if ( wire_drag != -1 )
				{
					if( wire_drag_end == 1 )                // if first end is draged
					{
						wire[total_wire-1 ].update1((work_x/15)*15 , (work_y/15)*15);
					}    
					else 
					{    
						wire[total_wire-1 ].update2((work_x/15)*15 , (work_y/15)*15);
					}    
					repaint();
				}    
				else 
				{    
					if ( total_comp > 0 )
					{    
						for ( i = work_x - ww; i < work_x + comp_node[total_comp -1].width -ww ; i++ ) 
						{    
							for ( j = work_y-hh ; j < work_y + comp_node[total_comp-1].height -hh ; j++ )
							{    
								if(i <= 15 || j <= 15||i >= work_panel_width || j >= work_panel_height || (work_mat[i][j] != -1 && work_mat[i][j] != node_drag))
								{    
									return;
								}    
							}     
						}    
						// for boundary check even if rotated (by adding heights bec that could be maxima )
						int t1 = comp_node[total_comp-1].height/4 ;
						if ( comp_node[total_comp-1].width > t1 ) 
						{
							t1 = comp_node[total_comp-1].width ;
						}

						for ( i = work_x - t1 ; i < work_x + t1 ; i++ )
						{
							for ( j = work_y - t1 ; j < work_y + t1 ; j++ )
							{
								if(i <= 15 || j <= 15 || i >= work_panel_width-15 || j >= work_panel_height-15)
								{
									return;
								}
							}
						}

					}

					if (node_drag != -1 )
					{
						comp_node[node_drag].remove_mat();

						comp_node[node_drag].node_x  = ((work_x) /15 )*15 ;
						comp_node[node_drag].node_y  = (( work_y)/15)*15 ;

						comp_node[node_drag].update_mat(node_drag);
					}
				}
				repaint();

			}
			public void mouseClicked(MouseEvent m)
			{
				int i,j;
				work_x=m.getX();
				work_y=m.getY();
				System.out.println(work_x+"****"+work_y);
				if(img_button_pressed==-1)
				{
					myDialog dialog;
					if ( wire_mat[work_x][work_y] != -1 )
					{
						//System.out.println("wire_mat[work_x][work_y]");
						//System.out.println("%%%%%%%%%%%%%%"+wire_mat[work_x][work_y]);
						//JOptionPane.showMessageDialog(null,wire_mat[work_x][work_y] );
						int p=wire_mat[work_x][work_y];
						JFrame wire_f = new JFrame();
						int n1 = JOptionPane.showConfirmDialog( wire_f, "Do u want to Delete Wire ?","Wire", JOptionPane.YES_NO_OPTION);
						if ( n1 == 0 )
						{
							
							wire[p].del();
							repaint();
							return;
							
						}
						else
						{
							//      System.out.println("Not Deletded ");
						}

					}
					else if ( work_mat[work_x][work_y]!= -1 ) 
						//	else
					{
						int temp = work_mat[work_x][work_y] ;
						if(temp!=-1)
						{
							int temp1 = comp_node[temp].img_no ; // temp is no img no 
							if (  temp1 == 1 || temp1 == 2 || temp1 == 3 || temp1 == 4 ||  temp1 == 8 || temp1==10 )
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
					if(img_button_pressed!=-1)
					{
						myDialog dialog;
						if(work_x<15)
							work_x=15;
						if(work_y<15)
							work_y=15;
						if(img_button_pressed==2) //|| img_button_pressed==7)
						{
							if(pmos_count > number)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of "+number+" D-FlipFlops ");
							}
							else
							{

								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , ww * 2 , 4 * hh,pmos_count);
								pmos_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}

						}
						else if(img_button_pressed==1)
						{
							if(nmos_count >= s)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of "+s+" XOR Gates ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , ww * 2 , 4 * hh,nmos_count);
								nmos_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}

						}
						
						/*	else if(img_button_pressed==3)  //horizontal wire
					{
				//	       int temp = work_mat[work_x][work_y] ;
						dialog = new myDialog( new JFrame() ,"wire" ,3,3);
						dialog.setVisible(true);
						comp_node[total_comp]=new node((work_x/20)*20,(work_y/20)*20,img_button_pressed,(Integer.parseInt(hori_len))*20,0);
					}
                                         else if ( img_button_pressed == 8  ) // vertical wire  
                                         {

						dialog = new myDialog( new JFrame() ,"wire" , 8,8);
						dialog.setVisible(true);
                                       comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 0, (Integer.parseInt(veri_len)*20));
                                         }*/
						else if (  img_button_pressed == 9 ) // Vdd
						{
							if(vdd_count>=100)
							{
								JOptionPane.showMessageDialog(null, "Circuit Can have maximum of 1 VDD ");
							}
							else
							{
								comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , ww * 2, 4 * hh,vdd_count);
								vdd_count++;
								comp_node[total_comp].update_mat(total_comp);
								total_comp++;
							}
						}
						else if (  img_button_pressed == 4 ) // Input , Output
						{
							comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , ww * 2, 4*hh,input_count);
							input_count++;
							comp_node[total_comp].update_mat(total_comp);
							total_comp++;
						}
						else if (  img_button_pressed == 10 ) // Input , Output
						{
							comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , ww * 2, 4*hh,output_count);
							output_count++;
							comp_node[total_comp].update_mat(total_comp);
							total_comp++;
						}
						/* else
                                         {
                                                 comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 20 * 3, 2 * 20);
                                         }*/

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
			public void entry()
			{
				//ww = ((5 + ( 16-number)*2)%15)*15;
				//hh=  ((5 + ( 16-number)*2)%15)*15;
				
				
				if(img1!=-1)
				{
					
					if(work_x<15)
						work_x=15;
					if(work_y<15)
						work_y=15;
					if(img1==2) //|| img_button_pressed==7)
					{
						if(pmos_count < number)
						{
					

							comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img1 , ww * 2 , 4* hh,pmos_count);
							pmos_count++;
							comp_node[total_comp].update_mat(total_comp);
							total_comp++;
						}

					}
				else if ( img1 == 1)  
					{
						if(nmos_count < s)
						{
					
							comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img1 , ww * 2 , 4 * hh,nmos_count);
							nmos_count++;
							comp_node[total_comp].update_mat(total_comp);
							comp_node[total_comp].rotate(total_comp);
							comp_node[total_comp].rotate(total_comp);
							total_comp++;
							
						}
					}
				else if ( img1 == 4)  
				{
					
					if(input_count<number)
					{
					
						comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img1 , ww*2  , 2 * hh,input_count);
						input_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;
					}
					
				}
				else if(img1==10)
				{
					if(output_count<1)
					{
						comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img1 , ww*2  , 2 * hh,output_count);
						output_count++;
						comp_node[total_comp].update_mat(total_comp);
						comp_node[total_comp].rotate(total_comp);
						total_comp++;
					}
				}
					
					img1=-1;
					//JOptionPane.showMessageDialog(null, "You entered the number : " + number,"Virtual Labs", 1);
			//		workPanel.repaint();
				}
			}
			
			public void paint(Graphics g)
			{
				int i,j;
				Graphics2D g2d = (Graphics2D)g;
				g2d.scale(scale_x , scale_y);
				// back ground ----------------
				g2d.setColor(Color.black);
				g.fillRect(0,0,work_panel_width, work_panel_height);
				g2d.setColor(Color.white);
				g2d.setStroke(new BasicStroke(1));
				for ( i = 0 ; i < work_panel_width; i+=15)
				{
					for ( j = 0 ; j < work_panel_height ; j+=15 )
					{
						g2d.drawOval(  i -1,j-1 , 0 , 0);
					}
				}
				ww=30;
				hh=30;

				for(i=0;i<number;i++)
				{
					
					img1=2;
					work_x=(ww*5)*i+3*ww;
					if(number==8)
					{
						work_x=ww*5*i+2*ww+15;
					}
					work_y=250;
					entry();
				}
				
				
				
				for(i=0;i<s;i++)
				{		
					
				
					img1=1;
					work_x=(ww*5)*(i)+3*ww;
					work_y=400;
					entry();
				}
				
				for(i=0;i<number;i++)
				{
				
					img1=4;
					work_x=(60+ww*3)*(i)+3*ww/2;
					work_y=90;
					entry();

				}
				if(set_output==1)
				{
					img1=10;
					work_x=(4*ww)*(i)+6*ww;
					work_y=350;
					entry();
				}
			
				try
				{	
			//		FileWriter f = new FileWriter("file.txt");
		//			BufferedWriter out = new BufferedWriter(f);
			//		System.out.println("in Paint Jay\n");	
		//			out.write(""+total_comp+"\n");
					//out.write("\n");
					for ( i = 0; i < total_comp ; i++ )
					{
						//out.write("\n");
	//					if(comp_node[i].del == true)
		//					out.write("delete\n");

						if ( comp_node[i].del != true )
						{
			//				out.write(""+(10+comp_node[i].img_no)+"\n");
				//			out.write(""+comp_node[i].node_x+"\n");
					//		out.write(""+comp_node[i].node_y+"\n");
						//	out.write(""+comp_node[i].angle+"\n");
							if ( comp_node[i].img_no == 1)
							{
								draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y , ww , comp_node[i].angle);
								g.setColor(Color.yellow);
								g.drawString(comp_str[1] , comp_node[i].node_x , comp_node[i].node_y+50 );
							}
							/*						else if ( comp_node[i].img_no == 3)
						{
							//draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].width , comp_node[i].angle);
							draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
							g.setColor(Color.yellow);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}*/
							/*			else if ( comp_node[i].img_no == 8)
						{
							//draw_and(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].height , comp_node[i].angle);
							draw_and(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle);
							g.setColor(Color.yellow);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 7)
						{
							draw_or(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle);
							g.setColor(Color.yellow);
							g.drawString(comp_str[7] , comp_node[i].node_x + -10 , comp_node[i].node_y + 10 );



						}*/
							
							else if ( comp_node[i].img_no == 2)
							{
								draw_and(g2d , comp_node[i].node_x  , comp_node[i].node_y , ww, comp_node[i].angle);
								g.setColor(Color.yellow);
								g.drawString(comp_str[2] , comp_node[i].node_x-ww  , comp_node[i].node_y +ww +15 );
							}
							else if ( comp_node[i].img_no == 4)
							{
								draw_input(g2d , comp_node[i].node_x  , comp_node[i].node_y , ww, comp_node[i].angle);
								g.setColor(Color.yellow);
								g.drawString(comp_str[4] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
							}
							
							else if ( comp_node[i].img_no == 10)
							{
								draw_output(g2d , comp_node[i].node_x  , comp_node[i].node_y , ww, comp_node[i].angle);
								g.setColor(Color.yellow);
								g.drawString(comp_str[10] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
							}
							
							/*  else if ( comp_node[i].img_no == 9)
                                                 {
                                                         draw_vdd(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
                                                         g.setColor(Color.yellow);
                                                         g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
                                                 }
						else if ( comp_node[i].img_no == 4 )
						{
							draw_input(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20, comp_node[i].angle);
						//	g.setColor(Color.blue);
						//	g.fillOval( comp_node[i].end_pointsX[0] - 4, comp_node[i].end_pointsY[0]  -4, 8 ,8 );
						//	g.setColor(Color.green);
						//	g.fillOval( comp_node[i].node_x - 4, comp_node[i].node_y  -4, 8 ,8 );

						}
					else if ( comp_node[i].img_no == 10 )
						{
							draw_output(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20, comp_node[i].angle);
					//		g.setColor(Color.blue);
					//		g.fillOval( comp_node[i].end_pointsX[0] - 4, comp_node[i].end_pointsY[0]  -4, 8 ,8 );
					//		g.setColor(Color.green);
					//		g.fillOval( comp_node[i].node_x - 4, comp_node[i].node_y  -4, 8 ,8 );

						}*/
							else
							{
								g2d.drawImage(img[comp_node[i].img_no] , comp_node[i].node_x ,comp_node[i].node_y, work_img_width , work_img_height,  this);
							}


						}
					}	

					g2d.setStroke(new BasicStroke(2));
					//	out.write("wire\n");
//					out.write(""+total_wire+"\n");
					//	out.write("\n");
					for ( i = 0 ; i < total_wire ; i++ )
					{
		//				if ( wire[i].del == true )
		//					out.write("delete\n");

						if ( wire[i].del == false )
						{
							g2d.setColor(Color.green);
		//					out.write(""+wire[i].end_index+"\n");
							for ( int k = 0 ; k < wire[i].end_index ; k ++ )
							{
	//							out.write(""+wire[i].x[k]+"\n");
		//						out.write(""+wire[i].y[k]+"\n");
			//					out.write(""+wire[i].x[k + 1]+"\n");
								//	out.write(""+wire[i].y[k]+"\n");
								//	out.write(""+wire[i].x[k + 1]+"\n");
								//	out.write(""+wire[i].y[k]+"\n");
								//	out.write(""+wire[i].x[k + 1]+"\n");
				//				out.write(""+wire[i].y[k + 1]+"\n");
								//      g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
								g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k] );
								g2d.drawLine (wire[i].x[k+1] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
								//      g2d.drawLine (wire[i].x1 , wire[i].y1 , wire[i].x2 , wire[i].y1 );
								//      g2d.drawLine (wire[i].x2 , wire[i].y1 , wire[i].x2 , wire[i].y2 );
							}
					//		out.write(""+wire[i].x1+"\n");
						//	out.write(""+wire[i].y1+"\n");
	//						out.write(""+wire[i].x2+"\n");
		//					out.write(""+wire[i].y2+"\n");

							g2d.setColor(Color.red);
							g2d.fillRect (wire[i].x1 -4  , wire[i].y1 -4 , 8 ,8);
							g2d.fillRect (wire[i].x2 - 4 , wire[i].y2 -4 , 8 ,8);
						}
						//	out.write("\n");
					}
			//		out.close();
				}
				catch(Exception e)
				{
					System.out.println(e);
				}

			}
			void draw_and(Graphics2D g , int x , int y , int width , double angle )
			{
				g.rotate(angle , x , y);
				g.setColor(Color.yellow);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.yellow);
				g.drawLine(x-2*width,y-width/2,x-width,y-width/2); //D-input
				g.drawLine(x-2*width,y+width/2,x-width,y+width/2); //clock
				g.drawLine(x-width,y-width,x-width,y+width);    //vertical1
				g.drawLine(x+width,y-width,x+width,y+width);//vertical2
				
				g.drawLine(x-width,y-width,x+width,y-width);//hori1
				g.drawLine(x-width,y+width,x+width,y+width);//hori2
				
				g.drawLine(x+width,y,x+2*width,y); //output
			
				g.setColor(Color.green);
				g.drawLine(x-width,y+width/4,x-width/2,y+width/2); //clock_sym1
				g.drawLine(x-width,y+3*width/4,x-width/2,y+width/2); //clock_sym2
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect( x -2*width -4, y-width/2  -4, 8 ,8 );
				g.fillRect( x -2*width- 4, y+width/2  -4, 8 ,8 );
				g.fillRect( x +2*width- 4, y -4, 8 ,8 );
				g.rotate(-angle , x , y);

			}

			void draw_xor(Graphics2D g , int x , int y , int width , double angle)
			{
				
				g.rotate(angle , x , y);
				g.setColor(Color.yellow);
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.yellow);
				x=x-2*width;
				y=y-5*width/2;
				g.drawLine(x,y+2*width,x+width+width/2,y+2*width);
				g.drawLine(x,y+3*width,x+width+width/2,y+3*width);
				g.drawArc(x+width-width/2,y+2*width-width/2,width,2*width,90,-180);
				g.drawArc(x+width-width/2+5,y+2*width-width/2,width,2*width,90,-180);
				//  g.drawArc(x+width-width/4,y+2*width-width/2,width,2*width,90,-180);
				g.drawLine(x+width,y+2*width-width/2,x+2*width,y+2*width-width/2);
				g.drawLine(x+width,y+3*width+width/2,x+2*width,y+3*width+width/2);
				g.drawArc(x+2*width-width/2,y+2*width-width/2,width,2*width,90,-180);
				g.drawLine(x+3*width-width/2,y+2*width+width/2,x+4*width,y+2*width+width/2);
		//		g.drawLine(x+4*width,y+2*width,x+4*width,y+2*width+width/2);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect( x - 4, y+2*width  -4, 8 ,8 );
				g.fillRect( x - 4, y+3*width  -4, 8 ,8 );
				g.fillRect( x +4*width- 4, y+2*width+width/2 -4, 8 ,8 );
				x=x+2*width;
				y=y+5*width/2;
				g.rotate(-angle , x , y);
			}
			void draw_input(Graphics2D g , int x , int y , int width , double angle)
			{
				   g.rotate(angle , x , y);
                   g.setColor(Color.yellow);
                   g.setColor(Color.blue);
                   g.setStroke(new BasicStroke(2));
                   g.drawLine(x+width,y,(int)(x+0.5*width),y);
                   g.drawRect(x-(int)(0.5*width),(int)(y-0.5*width),width,width);
                   g.drawLine(x-5,y+5,x-1,y+5);
                   g.drawLine(x-1,y+5,x-1,y-5);
                   g.drawLine(x-1,y-5,x+2,y-5);
                   g.drawLine(x+2,y-5,x+2,y+5);
                   g.drawLine(x+2,y+5,x+5,y+5);

                   g.setStroke(new BasicStroke(1));
                   g.setColor(Color.red);
                   //g.drawRect(x,y,8,8);
                   g.fillRect(x-4+width,y-4,8,8);
                   g.rotate(-angle , x , y);

			}
			void draw_output(Graphics2D g , int x , int y , int width , double angle)
			{
				
				g.rotate(angle , x , y);
				g.setColor(Color.yellow);
			//	g.drawString("OUT", x  + width / 2, y+ width - 5);
			
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



		}
	}

	public class graph extends JPanel
	{
	
		int num=0;
		
		public void make_graph(int n)
		{
			num=n;
	/*		fileToRead ="outfile";
			URL url = null;
			try
			{
				url = new URL(getCodeBase(), fileToRead);
			}
			catch(MalformedURLException e){
				System.out.println("I did't got the outfile to read :( :( So I am very said ");
			}
			String line;
			try{
				InputStream in = url.openStream();
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));
				strBuff = new StringBuffer();
				myline = bf.readLine();
				while(!myline.equals("Values:"))
				{
					myline = bf.readLine();
				}
				int i = 0 ;
				while((line = bf.readLine()) != null){
					line = bf.readLine();
					line = bf.readLine();
					A[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					B[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					Sum[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					Carry[i] = Double.parseDouble(line);
					i++;
				}
				no_values = i ;

				repaint();

				//              System.out.println("Hi I am in the contrct func of the exp1_graph class :)");
			}
			catch(IOException e){
				e.printStackTrace();
			}

*/
		}
		
		public void paint(Graphics g)
		{


//			System.out.println("Hi I am in the gpaint func of the exp1_graph class :)");
			int i , j ;
			int lim1,lim2;
			if(num<8)
			{
				lim1=lim2=80+40*(2*(13+1)-1)-40;
			}
			else
			{
				lim1=(int)(80+40*(2*(11.5+1)-1))-20;
				lim2=(int)(80+40*(2*(11.5+1)-1)-40);
			}
			Graphics2D g2d = (Graphics2D)g ;
			g2d.setStroke(new BasicStroke(2));
			// back ground 
			g2d.setColor(new Color(204 , 255 , 255));
			g2d.fillRect(0,0,lim1-20,500);
			g2d.setColor(Color.lightGray);
			
			for ( i = 0 ; i < lim1 ; i += 20 )
			{
				for (j = 0 ; j < 500 ; j +=5 )
				{
					g2d.fillOval(i , j , 1 , 2);
				}
			}
			for ( i = 0 ; i < lim2 ; i += 5 )
			{
				for (j = 0 ; j < 500 ; j +=20 )
				{
					g2d.fillOval(i , j , 2 , 1);
				}
			}

			// graph
			g2d.setColor(Color.blue);
			g2d.setStroke(new BasicStroke(3));
			if(num<8)
			{
				for(i=0;i<stopper-f1;i++)
				{
					g2d.drawLine(80*i, 60,40+80*i, 60);
					g2d.drawLine(40+80*i, 60, 40+80*i, 100);
					g2d.drawString(5*(f1+i+1)+" ms",35+80*i,120);
					g2d.drawLine(40+80*i, 100, 80*(i+1), 100);
					g2d.drawLine(80*(i+1), 100,80*(i+1), 60);


				}
				g2d.drawLine(80*i, 60,40+80*i, 60);
				//	g2d.drawLine(0, 100 , 380 , 100);
				g2d.drawString("CLOCK",  280 , 50 );

				g2d.setColor(Color.red);
				for( i = 0 ; i < stopper-f1 ; i++ )
				{
					g2d.drawLine(40*(2*(i+1)-1) , 300 , 20+40*(2*(i+1)-1) , 260 );
					g2d.drawString(f1+i+1+" cycle",20+40*(2*(i+1)-1),320);
					g2d.drawLine(20+40*(2*(i+1)-1),260,60+40*(2*(i+1)-1),260);
					g2d.drawLine(60+40*(2*(i+1)-1), 260, 80+40*(2*(i+1)-1), 300);
					g2d.drawLine(40*(2*(i+1)-1) , 300 , 20+40*(2*(i+1)-1) , 340 );
					g2d.drawLine(20+40*(2*(i+1)-1),340,60+40*(2*(i+1)-1),340);
					g2d.drawLine(60+40*(2*(i+1)-1), 340, 80+40*(2*(i+1)-1), 300);

				}
			}
			else
			{
				for(i=0;i<stopper-f2;i++)
				{
					g2d.drawLine(2*80*i-40, 60,2*(40+80*i)-40, 60);
					g2d.drawLine(2*(40+80*i)-40, 60, 2*(40+80*i)-40, 100);
					g2d.drawString(5*(f2+i+1)+" ms",2*(40+80*i)-40,120);
					g2d.drawLine(2*(40+80*i)-40, 100, 2*(80*(i+1))-40, 100);
					g2d.drawLine(2*(80*(i+1))-40, 100,2*(80*(i+1))-40, 60);


				}
				//g2d.drawLine(80*i, 60,40+80*i, 60);
				//	g2d.drawLine(0, 100 , 380 , 100);
				g2d.drawString("CLOCK",  280 , 50 );

				g2d.setColor(Color.red);
				for( i = 0 ; i < stopper-f2 ; i++ )
				{
					g2d.drawLine(2*40*(2*(i+1)-1)-40 , 300 , 2*(20+40*(2*(i+1)-1))-40 , 260 );
					g2d.drawString(f2+i+1+" cycle",2*(20+40*(2*(i+1)-1))-40,320);
					g2d.drawLine(2*(20+40*(2*(i+1)-1))-40,260,2*(60+40*(2*(i+1)-1))-40,260);
					g2d.drawLine(2*(60+40*(2*(i+1)-1))-40, 260, 2*(80+40*(2*(i+1)-1))-40, 300);
					g2d.drawLine(2*(40*(2*(i+1)-1))-40 , 300 , 2*(20+40*(2*(i+1)-1))-40 , 340 );
					g2d.drawLine(2*(20+40*(2*(i+1)-1))-40,340,2*(60+40*(2*(i+1)-1))-40,340);
					g2d.drawLine(2*(60+40*(2*(i+1)-1))-40, 340, 2*(80+40*(2*(i+1)-1))-40, 300);

				}
				
			}
			
			g2d.drawString("OUTPUT ",  280 , 250 );
			

			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawLine(40 , 20 , 40  , 480 );
			
			g2d.drawString("WAVEFORM OUTPUT SIMULATION  ",  80 , 20 );
			g2d.drawString("OF THE DRAWN CIRCUIT - ",  100 , 40 );
			
			int [] res= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		    j=0;
		    for(i=0;i<num;i++)
	    	{
	    		if(coeff[i]==1)
	    		{
	    		//	System.out.print(i);
	    			res[j]=i;
	    			j++;
	    		}
	    	}
		   // System.out.println();
		    int store[][]= new int[(int)Math.pow(2,num)][num];
		    
		    int feed[]=new int[num];
		    for(i=0;i<num;i++)
		    {
		    	feed[i]=seed[i];
		    }
		    int xor=0;
		    for(int k=0;k<Math.pow(2,num);k++)
		    {
		    	for(i=0;i<num;i++)
			    {
			    	store[k][i]=feed[i];
			  //  	System.out.print(store[k][i]);
			    }
		    //	System.out.println();
		    	xor=feed[res[0]];
		    	for(i=1;i<j;i++)
		    	{
		    		xor=xor^feed[res[i]];
		    		
		    	}
		    	if(j==0)
		    		xor=0;
		    	for(i=0;i<num-1;i++)
		    	{
		    		feed[i]=feed[i+1];
		    	}
		    	feed[num-1]=xor;
		    	
		    }
		    if(num<8)
		    {
		    	for(int m=0;m<stopper-f1;m++)
		    	{
		    		for(i=0;i<num;i++)
		    		{
		    			g2d.drawString(store[f1+m][num-i-1]+"",  44+10*i+m*80 , 305 );
		    		}
		    	}
		    }
		    else
		    {
		    	for(int m=0;m<stopper-f2;m++)
		    	{
		    		for(i=0;i<num;i++)
		    		{
		    				g2d.drawString(store[f2+m][num-i-1]+"",  44+10*i+m*160 , 305 );
		    				
		    		}
		    	}
		    	
		    }
		    if(stopper%13==0)
			{
				jug1++;
				f1=13*jug1;
			}
		    if(stopper%6==0)
			{
				jug2++;
				f2=6*jug2;
			}
		    try
			{
				if(stopper <  Math.pow(2,num))
				{
					if(pause_flag==0)
					{
						stopper++;
						Thread.sleep(1000);
					}
					repaint();
				}
				
			}
			catch(Exception ee)
			{
			}   
		}

	}




/*	public class graph1 extends JPanel
	{
		String fileToRead = "outfile";

		StringBuffer strBuff;
		TextArea txtArea;
		String myline;
		JLabel l ;

		double[] A = new double[1000] ;
		double[] B = new double[1000] ;
		double[] C_in = new double[1000] ;
		double[] C_out = new double[1000] ;
		double[] S = new double[1000] ;
		int no_values = 0 ;
		//public  graph()
		public  void graph ()
		{
		}
		public void make_grap()
		{
		}
		public void make_graph()
		{
			fileToRead ="outfile";
			URL url = null;
			try
			{
				url = new URL(getCodeBase(), fileToRead);
			}
			catch(MalformedURLException e){
				System.out.println("I did't got the outfile to read :( :( So I am very said ");
			}
			String line;
			try{
				InputStream in = url.openStream();
				BufferedReader bf = new BufferedReader(new InputStreamReader(in));
				strBuff = new StringBuffer();
				myline = bf.readLine();
				while(!myline.equals("Values:"))
				{
					myline = bf.readLine();
				}
				int i = 0 ;
				while((line = bf.readLine()) != null){
					line = bf.readLine();
					line = bf.readLine();
					A[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					B[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					C_in[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					C_out[i] = Double.parseDouble(line);
					line = bf.readLine();
					line = bf.readLine();
					S[i] = Double.parseDouble(line);
					i++;
				}
				no_values = i ;

				repaint();

				//              System.out.println("Hi I am in the contrct func of the exp1_graph class :)");
			}
			catch(IOException e){
				e.printStackTrace();
			}


		}
		public void paint(Graphics g)
		{


			System.out.println("Hi I am in the gpaint func of the exp1_graph class :)");
			int i , j ;
			Graphics2D g2d = (Graphics2D)g ;
			g2d.setStroke(new BasicStroke(2));
			// back ground 
			g2d.setColor(new Color(204 , 255 , 255));
			g2d.fillRect(0,0,1000,1500);
			g2d.setColor(Color.lightGray);
			for ( i = 0 ; i < 1500 ; i += 15 )
			{
				for (j = 0 ; j < 1500 ; j +=5 )
				{
					g2d.fillOval(i , j , 1 , 2);
				}
			}
			for ( i = 0 ; i < 1500 ; i += 5 )
			{
				for (j = 0 ; j < 1500 ; j +=15 )
				{
					g2d.fillOval(i , j , 2 , 1);
				}
			}
*/


			/*       g2d.setColor(Color.red);
                                        for( i = 0 ; i < no_values - 1 ; i++ )
                                        {
                                                g2d.drawLine(40+2*i , 200-(int)Math.round(V_in[i]*100) , 40 + 2*(i+1) , 200-(int)Math.round(V_in[i+1]*100) );
                                        }
                                        g2d.drawString("Input Voltage ",  280 , 60 );

                                        g2d.setColor(Color.blue);
                                        for( i = 0 ; i < no_values - 1 ; i++ )
                                        {
                                                g2d.drawLine(40+2*i , 400-(int)Math.round(V_out[i]*100) , 40 + 2*(i+1) , 400-(int)Math.round(V_out[i+1]*100) );
                                        }
                                        g2d.drawString("Output Voltage ",  280 , 260 );
			 */
			/*

			g2d.setColor(Color.red);

			g2d.drawString("Carry In ",  320 , 90 );
			for( i = 0 ; i < no_values - 1 ; i++ )
			{
				g2d.drawLine(40+5*i , 140-(int)Math.round(C_in[i]*50) , 40 + 5*(i+1) , 140-(int)Math.round(C_in[i+1]*50) );
			}
			g2d.drawString("B Input ",  320 , 160 );
			for( i = 0 ; i < no_values - 1 ; i++ )
			{
				g2d.drawLine(40+5*i , 210-(int)Math.round(B[i]*50) , 40 + 5*(i+1) , 210-(int)Math.round(B[i+1]*50) );
			}
			g2d.drawString("A Input ",  320 , 230 );
			for( i = 0 ; i < no_values - 1 ; i++ )
			{
				g2d.drawLine(40+5*i , 280-(int)Math.round(A[i]*50) , 40 + 5*(i+1) , 280-(int)Math.round(A[i+1]*50) );
			}
			g2d.setColor(Color.blue);
			g2d.drawString("Carry Out ",  320 , 290 );
			for( i = 0 ; i < no_values - 1 ; i++ )
			{
				g2d.drawLine(40+5*i , 350-(int)Math.round(C_out[i]*50) , 40 + 5*(i+1) , 350-(int)Math.round(C_out[i+1]*50) );
			}
			g2d.drawString("Sum ",  320 , 350 );
			for( i = 0 ; i < no_values - 1 ; i++ )
			{
				g2d.drawLine(40+5*i , 420-(int)Math.round(S[i]*50) , 40 + 5*(i+1) , 420-(int)Math.round(S[i+1]*50) );
			}

			 */
/*


			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(1));
			g2d.drawLine(40 , 20 , 40  , 480 );
			g2d.drawLine( 0  , 440 , 380 , 440 );

			g2d.drawString("Time --> ",  160 , 460 );
			//      g2d.drawString("Volt",  10 , 160 );
			//    g2d.drawString("Volt",  10 , 360 );

			g2d.drawString("WAVEFORM OUTPUT SIMULATION  ",  80 , 20 );
			g2d.drawString("OF THE DRAWN CIRCUIT - ",  100 , 40 );

			//      g2d.drawLine(20 , 260 , 20  , 420 );
			//      g2d.drawLine( 20  , 420 , 400 , 420 );
			//      g2d.drawLine(95 , 290 , 1000  , 290 );

		}


	}

*/
}
