import java.util.*;
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
import java.util.PropertyPermission.*;
import java.awt.*;
import java.awt.Color.*;
import java.awt.MediaTracker.*;
import java.awt.event.*;
import java.text.*;
import java.awt.datatransfer.*;
import java.net.*;
import java.net.URLEncoder.*;
import java.io.*;
import java.io.File.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.ImageIcon.*;

public class exp1 extends JApplet
{
	private static final long serialVersionUID = 1;
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
		setContentPane(myPane);
	}
	public class MyPanel  extends JPanel  implements ActionListener//MouseListener,MouseMotionListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1;
		int exp_type = 0 ; // 0 -> Complementary Inverter , 1 -> Pseudo Inverter 
		/** Work Panel Variables ************************************************************************************************/
		double scale_x = 1 ;  // scaling of work pannel 
		double scale_y = 1 ;
		int pmos_delete_count=0;
		int [] pmos_delete=new int[200];
		int nmos_delete_count=0;
		int [] nmos_delete=new int[200];
		int gnd_delete_count=0;
		int [] gnd_delete=new int[200];
		int vdd_delete_count=0;
		int [] vdd_delete=new int[200];
		int input_delete_count=0;
		int [] input_delete=new int[200];
		int output_delete_count=0;
		int [] output_delete=new int[200];
		int xor_delete_count=0;
		int [] xor_delete=new int[200];
		
		int pmos_count=0;	
		int nmos_count=0;	
		int gnd_count=0;	
		int vdd_count=0;
		int input_count=0;	
		int output_count=0;	
		int xor_count=0;	

		int work_x ;
		int work_y ;
		int wire_button  = 0 ;// 0 not pressed already , 1 -> already pressed 
		int img_button_pressed = -1 ;
		int draw_work = 0 ; // if 1 -> draw the image on work 

		int[][] work_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] end_points_mat ;   // 
		int[][] wire_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] wire_points_mat ;   // 

		int work_img_width = 50;
		int work_img_height = 50;
		int work_panel_width = 1200 ;
		int work_panel_height = 1200;

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
			"AND ", "Ground Terminal " ," Wire ",  // 1, 2 , 3
			"INPUT " ,"NAND ", " " , // 4 , 5 ,6

			"OR", "XOR" ,"NOT ",  // 7 , 8 , 9 
			"OUTPUT",  "XNOR " ," ",  // 10, 11 , 12
			"" ,""};
		//String inputport[]={"1","2","3","4"};
		//String outputport[]={"1","2","3","4"};
		//int inputno[]={0,0,0,0};
		//int outputno[]={0,0,0,0};
		//int mycount=0;
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
		ImageIcon icon0[] = new ImageIcon[3] ;
		Image img0[] = new Image[3] ;
		//ImageIcon icon[] = new ImageIcon[20] ;
		Image img2[] = new Image[10] ;
		ImageIcon icon2[] = new ImageIcon[10] ;
		Image img3[] = new Image[10] ;
		ImageIcon icon3[] = new ImageIcon[10] ;
		Image img4[] = new Image[10] ;
		ImageIcon icon4[] = new ImageIcon[10] ;

		URL base;
		JPanel topPanel = new JPanel () ;
		JButton simulate_button ;
		JButton graph_button ;
		JComboBox exp_list ;
		JButton layout_button ;

		JSplitPane splitPane ; // divides center pane into left and right panel 
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
		JButton img_button3[] = new JButton[10] ;
		JButton img_button4[] = new JButton[10] ;		
		JButton img_button0[] = new JButton[3] ;
		int counter=0;
		int output_buffer[] = new int[5];
   		int check_points_value[][] = new int[300][1000] ;


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
                                  int i , j , tx1 , ty1 , tx2 , ty2 ; // local variables 
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
			int i ;
			
//--------------------------------------------------------------------------------
//CREATIE AND SET UP THE CONTENT PAGE .===========================================
//--------------------------------------------------------------------------------

			try // getting base URL address of this applet 
			{
				base = getDocumentBase();
			}
			catch( Exception e) {}

//------------------------------------------------------------------------------------
// Setting Left Pannel Of (Main Center Panel)---------------------------------------------- 
			leftPanel.setLayout(new BorderLayout());
			leftPanel.setMinimumSize(new Dimension(800 , 1000)); // for fixing size
			rightPanel.setMinimumSize(new Dimension(400 , 600)); // for fixing size

			leftSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , toolPanel , workPanel); // spliting left in tool & work
			leftSplitPane.setOneTouchExpandable(true); // this for one touch option 
			leftSplitPane.setDividerLocation(0.2);  
			leftPanel.add(leftSplitPane, BorderLayout.CENTER);
			
			
			for ( i = 1 ; i <= 4 ; i ++ )
			{
				java.net.URL imgURL = getClass().getResource("pics/comp" + i + ".gif");
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
			for ( i = 1 ; i <= 4 ; i ++ )
			{
				j = 6 + i ; // for index setting 
				java.net.URL imgURL = getClass().getResource("pics/comp" + j + ".gif");
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
			}
			
			

			toolPanel.setLayout(new BorderLayout());

			toolPanelUp = new JPanel();
			toolPanelDown = new JPanel();


			URL selected_URL = getClass().getResource("pics/comp" + 0 + ".gif");
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

			selected.setBackground(Color.orange);
			selected.setToolTipText(comp_str[0]); // setting name at hovering of mouse 

			toolPanel.add(toolPanelUp , BorderLayout.CENTER);


			JPanel temp_p = new JPanel();
			temp_p.setLayout(new BorderLayout());

			JPanel temp_pp = new JPanel();
			JLabel temp = new JLabel("<html> <FONT color=white size=4 ><b>Tool Bar<b/><font/><html/>");
			temp_pp.add(temp);
			temp_pp.setBackground(Color.gray);
			temp_pp.setBorder(BorderFactory.createRaisedBevelBorder( ));

			temp_p.add(temp_pp , BorderLayout.NORTH);
			temp_p.add(new JLabel("<html> <br/><br/><html/>") , BorderLayout.CENTER);


			toolPanel.add(temp_p , BorderLayout.NORTH);
			
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
			
			
			
			rightPanel.setLayout( new BorderLayout() );
			//JLabel wave_head = new JLabel ( "<html><FONT COLOR=white SIZE=6 ><B>SIMULATION OF CIRCUIT</B></FONT><br><br></html>", JLabel.CENTER);
			//wave_head.setBorder(BorderFactory.createRaisedBevelBorder( ));
			
			 //rightPanel.setBackground(Color.gray);
			JPanel upPanel = new JPanel ();
			   upPanel.setLayout(new BorderLayout());



			 JPanel downPanel = new JPanel ();
			 JPanel toolPanel = new JPanel ();
			 for ( i = 1 ; i <= 4 ; i ++ )
			{
				java.net.URL imgURL1 = getClass().getResource("pics/button1.gif");
				java.net.URL imgURL2 = getClass().getResource("pics/button2.gif");
				if (imgURL1 != null) 
				{
					icon3[1] =  new ImageIcon(imgURL1);
					img3[1] =  getImage(imgURL1);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon3[1] =  null;
				}
				
				if (imgURL2 != null) 
				{
					icon3[2] =  new ImageIcon(imgURL2);
					img3[2] =  getImage(imgURL2);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon3[2] =  null;
				}

				img_button3[i] = new JButton ( icon3[1] );
				img_button3[i].setOpaque(true);
				img_button3[i].setMargin(new Insets (0,0,0,0));
				img_button3[i].addActionListener(this);
				img_button3[i].setBackground(Color.white);
				img_button3[i].setToolTipText(comp_str[i]);// setting name for hovering of mouse

				toolPanel.add(img_button3[i]);
			}
			upPanel.add(toolPanel,BorderLayout.EAST);
			
			 JLabel pic = new JLabel(new ImageIcon("pics/Untitled1.gif"));
			 downPanel.add(pic,BorderLayout.SOUTH);
			 downPanel.setBackground(Color.gray);
			 downPanel.setBounds(0,0,500,500);
			// downPanel.add(fpga, BorderLayout.NORTH);
			 JSplitPane rightSplitPane ;
			 rightSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT , upPanel , downPanel);
			 rightSplitPane.setOneTouchExpandable(true); // this for one touch option 
			 rightSplitPane.setDividerLocation(0.2);  
			 rightPanel.add(rightSplitPane, BorderLayout.CENTER);
			
//---------------------------------------------------------------------------------
// Setting Center  Split ============================================================
			splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , rightPanel);
			splitPane.setOneTouchExpandable(true); // this for one touch option 
			splitPane.setDividerLocation(0.2);  
			add(splitPane, BorderLayout.CENTER);
//-------------------------------------------------------------------------------------------
//Setting Top Panel ========================================================================== 
			
			add(topPanel , BorderLayout.NORTH);
			topPanel.setBackground(Color.gray);
			JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 100 , 10 )) ;
			JLabel heading = new JLabel (  "<html><FONT COLOR=WHITE SIZE=18 ><B>Realization of system using virtual FPGA </B></FONT></html>", JLabel.CENTER);
			heading.setBorder(BorderFactory.createEtchedBorder( Color.black , Color.white));
						
			topPanel.setLayout(new BorderLayout());

			topPanel.add(heading , BorderLayout.CENTER);
			topPanel.add(headButton , BorderLayout.SOUTH);
			java.net.URL imgURL = getClass().getResource("pics/simulate1.png");
			java.net.URL imgURL2 = getClass().getResource("pics/graph.gif");
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
			
			JPanel bottom = new JPanel(new FlowLayout());
			add(bottom , BorderLayout.SOUTH);
			JLabel h = new JLabel (  "<html><FONT COLOR=WHITE SIZE=12 ><B> FPGA EXPERIMENT</B></FONT></html>", JLabel.CENTER);
			bottom.add(h );
			bottom.add(simulate_button );
			simulate_button.addActionListener(this);
			//*****************************************************************
			for ( i = 1 ; i <= 4 ; i ++ )
			{
				java.net.URL imgURLl = getClass().getResource("pics/button1.gif");
				java.net.URL imgURLr = getClass().getResource("pics/button2.gif");
				if (imgURLl != null) 
				{
					icon4[1] =  new ImageIcon(imgURLl);
					img4[1] =  getImage(imgURLl);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon4[1] =  null;
				}

				if (imgURLr != null) 
				{
					icon4[2] =  new ImageIcon(imgURLr);
					img4[2] =  getImage(imgURLr);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon4[2] =  null;
				}
	
				img_button4[i] = new JButton ( icon4[1] );
				img_button4[i].setOpaque(true);
				img_button4[i].setMargin(new Insets (0,0,0,0));
				img_button4[i].addActionListener(this);
				img_button4[i].setBackground(Color.white);
				img_button4[i].setToolTipText(comp_str[i]);// setting name for hovering of mouse

				bottom.add(img_button4[i],BorderLayout.EAST);
				//belowPanel.add(img_button4[i]);
				
			}
			//bottom.add(belowPanel,BorderLayout.EAST);
			setBorder(BorderFactory.createLineBorder( Color.black));
			

			for ( i = 1 ; i <= 2 ; i ++ )
			{
				java.net.URL imgURL0 = getClass().getResource("pics/button" + i + ".gif");
				if (imgURL0 != null) 
				{
					icon0[i] =  new ImageIcon(imgURL0);
					img0[i] =  getImage(imgURL0);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon0[i] =  null;
				}



				img_button0[i] = new JButton ( icon0[i] );
				img_button0[i].setOpaque(true);
				img_button0[i].setMargin(new Insets (0,0,0,0));
				img_button0[i].addActionListener(this);
				img_button0[i].setBackground(Color.white);
				img_button0[i].setToolTipText(comp_str[i]); 

				//leftTool2.add(img_button2[i]);
			}
		}
		public void change_selected (int no)
		{
			selected.setIcon(icon[no]);
		}
		public void mychange_selected (int no,int i)
		{
			img_button4[i].setIcon(icon0[no]);
		}
		public void mychange_selected2 (int no,int i)
		{
			img_button3[i].setIcon(icon0[no]);
		}
		int mycheck(int flag) {
			flag=flag*(-1);
			return flag;
		}
		int and(int a,int b) {
			if( a==1 && b==1 )
				return 1;
			else
				return 0;
		}
		
		int or(int a,int b ){
			if( a == 0 && b == 0 )
				return 0;
			else
				return 1;
		}

		int not(int a) {
			if( a==1 )
				return 0;	
			else 
				return 1;	
		}
		int xor(int a,int b) {
			if(a == 1 && b == 1)
				return 0;
			else if(a == 0 && b == 0 ) 
				return 0;
			else 
				return 1;	
		}
		int[] flag1= new int[5];
		int[] flag2= new int[5];
		int[] buffer= new int[5];

		 public boolean circuit_check()
                 {
		
                         //int check_points_value[][] = new int[230][1000] ; // each index will store corresponding  component for points of (comp point no -> index )
                         int i = 0 , j , j1 = 0, k1 = 0 , j2 = 0 , k2 = 0 , l = 0 ;
                         for (  i = 0 ; i < 280 ; i ++ )
                         {
                                 for (  j = 0 ; j < 100 ; j ++ )
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
			/*int temp ;
                         for (  i = 0 ; i < 280 ; i ++ ) // for making aal connected point for each end_point in its array ..
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
                         }*/
			for(i=0;i<280;i++) {
				l = 0 ;
                                 while ( check_points_value[i][l] != -1 ) {
                                       l++;
                                 }
				if(l!=0)
				 	check_points_value[i][l] = i;
			}
                         // checking 
                         //for (  i = 0 ; i < 280 ; i ++ )
                        // {
                          //      for(j=0;j<6;j++)
                          //              System.out.print(i+"=" +check_points_value[i][j]+"::");
			//	System.out.println();
                               
                       // }
			int total_component=0;
			int Tcomponent = 0;
			System.out.println("Total component is:"+total_comp);
	//		if ( exp_type == 0 ) // Complementary 
                        // {
                   for (  i = 0 ; i < 60 ; i ++ )
                   {
                         if ( check_points_value[i][0] != -1 )
                          {
                                total_component++;
                          }
					 
                   }
				 if(total_component%3==0)
						Tcomponent+=(total_component/3);
					 else
						return false;
				//System.out.println("*******component 1 is*****:::"+total_component);
				 total_component=0;
				 for (  i = 60 ; i < 120 ; i ++ )
                                 {
                                         if ( check_points_value[i][0] != -1 )
                                         {
                                                 total_component++;
                                         }
					
                                 }
				 if(total_component%3==0)
						Tcomponent+=(total_component/3);
					 else
						return false;
				//System.out.println("*******component 2 is*****:::"+total_component);
				 total_component=0;
				 for (  i = 120 ; i < 180 ; i ++ )
                                 {
                                         if ( check_points_value[i][0] != -1 )
                                         {
                                                 total_component++;
                                         }
					
                                 }
				 if(total_component%3==0)
						Tcomponent+=(total_component/3);
					 else
						return false;
				// System.out.println("*******component 3 is*****:::"+total_component);
				total_component=0;
				 for (  i = 180 ; i < 220 ; i ++ )
                                 {
                                         if ( check_points_value[i][0] != -1 )
                                         {
                                                 total_component++;
                                         }
					 
                                 }
				if(total_component%2==0)
						Tcomponent+=(total_component/2);
					 else
						return false;
				//System.out.println("*******component is*****:::"+total_component);
				 total_component=0;
				for (  i = 220 ; i < 280 ; i ++ )
                                 {
                                        if ( check_points_value[i][0] != -1 )
                                         {
                                            	Tcomponent++;     
                                         }
                                 }
				System.out.println("*******component is*****:::"+Tcomponent);  
				if( (Tcomponent != total_comp)){
					//total_wire = 0;
					return false;
				}
			
			return true;
			}
		public void actionPerformed(ActionEvent e)
		{
			int i;
			System.out.println("****************");
			if(counter == 0 ){
			  for( i=0;i<=4;i++){
				flag1[i]=1;
				flag2[i]=1;
			  }
			counter++;
			}
			for(i=1;i<=input_count;i++) {
				//if(input_count==0 || i<=input_count) {
				if(e.getSource() == img_button4[i]) {
					if(flag1[i]==1){
						mychange_selected(2,i);
						flag1[i]=mycheck(flag1[i]);
						buffer[i-1]=1;
					}
					else if(flag1[i]==-1) {
						mychange_selected(1,i);
						flag1[i]=mycheck(flag1[i]);
						buffer[i-1]=0;
					}			
				//}
				}
				//else {
				//	JOptionPane.showMessageDialog(null, "This is not in use  :)");
				//}
			}
			for(i=1;i<=input_count;i++)
				System.out.println(buffer[i]);
			

			  if(e.getSource() == simulate_button )
			{
				if(circuit_check()) {
				
					//JOptionPane.showMessageDialog(null, "Circuit is Complete  :)");
					
					int k;
					int value[] = new int[300];
					for(i=0;i<280;i++)
						value[i]=-1;
					k=0;
					for(i=0;i<input_count;i++) {
						while(check_points_value[i+240][k]!= i+240) {
							value[check_points_value[i+240][k]] = buffer[i];
							check_points_value[i+240][k]=-1;
							value[i+240]=-2;
							k++;
						}
						
						k=0;
					}
					//for(i=0;i<input_count;i++)
						//System.out.println("i="+buffer[i]);
							
					for(i=0;i<gnd_count;i++) {
						while(check_points_value[i+220][k]!= i+220) {
							value[check_points_value[i+220][k]] = 0;
							check_points_value[i+220][k]=-1;
							value[i+220]=-2;
							k++;
						}
						k=0;
					}
					
					//System.out.println("******************************");
					//for(i=0;i<280;i++)
					//	System.out.println("i="+i+"value="+value[i]);
					//System.out.println("******************************");
					//System.out.println(output_buffer[0]);
					counter=0;
				   while(counter!= output_count) {
					for(i=0;i<240;i++) {
						if(value[i]!= -1 && value[i]!=-2 && check_points_value[i][0]!=-1) {
							k=0;
							
							while( check_points_value[i][k]!= i) {
								if(value[check_points_value[i][k]]!= -2 ) {
								value[check_points_value[i][k]] = value[i];
								//value[i]= -2;
								}
								k++;
							}
							//if(k!=0)
								check_points_value[i][0]= -1; 
						}			
					}
					
					for(i=0;i<60;i=i+3){	
						if(value[i]!= -1 && value[i]!=-2) {
							
							if(i%3 == 0 && value[i+1]!= -1 && value[i]!=-2){ 
								value[i+2]=and(value[i],value[i+1]);
								value[i]=value[i+1]= -2;
							}
							else if(i%3 == 1 && value[i-1]!= -1 && value[i]!=-2) {
								value[i+1]=and(value[i-1],value[i]);
								value[i-1] = value[i]= -2;
							}
						}
					}
					for(i=60;i<120;i=i+3) {
						if(value[i]!= -1 && value[i]!=-2) {
							
							if(i%3 == 0 && value[i+1]!= -1 && value[i]!=-2) { 
								value[i+2]=or(value[i],value[i+1]);
								value[i]=value[i+1]= -2;
							}
							else if(i%3 == 1 && value[i-1]!= -1 && value[i]!=-2) {
								value[i+1]=or(value[i-1],value[i]);
								value[i]=value[i+1]= -2;
							}
						}	
					}
					for(i=120;i<180;i=i+3) {
						if(value[i]!= -1 && value[i]!=-2) {
							
							if(i%3 == 0 && value[i+1]!= -1 && value[i]!=-2) { 
								value[i+2]=xor(value[i],value[i+1]);
								value[i]=value[i+1]= -2;
							}
							else if(i%3 == 1 && value[i-1]!= -1 && value[i]!=-2) {
								value[i+1]=xor(value[i-1],value[i]);
								value[i]=value[i+1]= -2;
							}
						}	
					}
					for(i=180;i<220;i+=2) {
						if(value[i]!=-1 && value[i]!=-2) {
							k=0;
							while(check_points_value[i][k]!= i){
								value[check_points_value[i][k]]=value[i];
								k++;
							}
							value[i+1]=not(value[i]);
							value[i] = -2;		
						}
					}
					for(i=0;i<240;i++) {
						if(value[i]!= -1 && value[i]!=-2 && check_points_value[i][0]!=-1) {
							k=0;
							
							while( check_points_value[i][k]!= i) {
								if(value[check_points_value[i][k]]!= -2 ) {
								value[check_points_value[i][k]] = value[i];
								//value[i]= -2;
								}
								k++;
							}
							//if(k!=0)
								check_points_value[i][0]= -1; 
						}			
					}
					
					//System.out.println("******************************");
					//for(i=0;i<280;i++)
					//	System.out.println("i="+i+"value="+value[i]);
					//System.out.println("******************************");
					//System.out.println(output_buffer[0]);
					//for(i=0;i<5;i++)
						//System.out.println("value="+check_points_value[2][i]);
					for(i=260;i<280;i++) {
						if(value[i]!=-1) {
							output_buffer[counter++]=value[i];
							value[i] =-1;
						}
					}
					//counter=output_count;
					//counter++;
				  }//end of while
				System.out.println("******************************");
					//for(i=0;i<280;i++)
					//	System.out.println("i="+i+"value="+value[i]);
					//System.out.println("******************************");
					//System.out.println(output_buffer[0]);
				for(i=0;i<output_count;i++) {
					//mychange_selected2(output_buffer[i],i);
					System.out.println("output= "+output_buffer[i]);
				}	
					
				for(i=0;i<output_count;i++) {
					if(output_buffer[i]==1){
						mychange_selected2(2,i+1);
					}
					else if(output_buffer[i]==0) {
						mychange_selected2(1,i+1);
						
					}			
				}
				
			
					//System.out.println("********************");			
				}
				//end of if
				else {
					JOptionPane.showMessageDialog(null, "Circuit is not Complete , Please Complete it and press simulate again :)");
				}
				
				
			}

			else if (e.getSource() == img_button1[1] )
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
		
			else if (e.getSource() == img_button2[1] )
			{
					img_button_pressed = 7 ;
					change_selected(7);
			}
			else if (e.getSource() == img_button2[2] )
			{
					img_button_pressed = 8 ;
					change_selected(8);
			}
			else if (e.getSource() == img_button2[3] )
			{
					img_button_pressed = 9 ;
					change_selected(9);
			}
			else if (e.getSource() == img_button2[4] )
			{	
					img_button_pressed = 10 ;
					change_selected(10);	
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
						node_number=number;
					}
				}
				else if(no==7)
				{
					int i;
					if(nmos_delete_count!=0)
					{
					for(i=0;i<nmos_delete_count;i++)
					{
						if(nmos_delete[i]==-1)
							continue;
						node_number=nmos_delete[i];
						nmos_delete[i]=-1;
						break;
					}
					}
					else
					{
						node_number=number;
					}
				}
				else if(no == 8) {
					int i;	
					if(xor_delete_count != 0 ) {
						for(i=0;i<xor_delete_count;i++) {
							if(xor_delete[i]==-1)
								continue;	
							node_number = xor_delete[i];
							xor_delete[i] = -1;	
							break;
						}
					}
					else {
						node_number=number;
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
					node_number=number;
					}
				}
				else if(no==9)
				{
					int i;
					if(vdd_delete_count!=0)
					{
					for(i=0;i<vdd_delete_count;i++)
					{
						if(vdd_delete[i]==-1)
							continue;
						node_number=vdd_delete[i];
						vdd_delete[i]=-1;
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
                                  }
                                 if (angle_count == 0 ) // 90 degree
                                  {
                                          virtual_w = width;
                                          virtual_h = height  ;
                                  }
                                  else if (angle_count == 1 ) // 90 degree
                                  {
                                          virtual_w = -height;
                                          virtual_h = width  ;
                                  }
                                  else if (angle_count == 2 ) // 180 degree
                                  {
                                          virtual_w = -width ;
                                          virtual_h = -height ;
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
				if ( img == 1) // pmos
				{
					
					for ( int k = 0 ; k < 3 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] + 8; i ++ )
						{
							for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] +8; j ++ )
							{
						//		System.out.println("k"+(((pmos_count)*3)+k));
								end_points_mat[i][j] = (((node_number)*3))+k ;
								//System.out.println("end points mrked::"+end_points_mat[i][j] ) ;
							}
						}
					}
				}
				 else if ( img == 7)// nmos
                                  {
                                          for ( int k = 0 ; k < 3 ; k ++ )
                                          {
                                          for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8; i ++ )
                                          {
                                                  for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] + 8; j ++ )
						  {
							  end_points_mat[i][j] = (20*3)+((node_number)*3)+k  ; //total number of pmos=14
                                                  }
                                          }
                                          }
                                  }
				else if( img == 8) {//XOR
					for(int k=0;k<3;k++) {
                                          for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8;i++) {
                                                  for ( int j = end_pointsY[k]-7;j<end_pointsY[k] + 8;j++) {
							  end_points_mat[i][j] = (40*3)+((node_number)*3)+k  ; //total number of pmos=14
                                                  }
                                          }
                                       }
				}
						
				else if(img == 9 ){ //NOT
					for ( int k = 0 ; k < 2 ; k ++ ) {
                                          for ( int i = end_pointsX[k] - 7 ; i < end_pointsX[k] +8; i ++ ) {
                                                  for ( int j = end_pointsY[k] - 7 ; j < end_pointsY[k] + 8; j ++ )
						  {
							  end_points_mat[i][j] = (60*3)+((node_number)*2)+k  ; //total number of pmos=14
                                                  }
                                          }
                                        }
				}

				else if ( img == 2 ) // ground 
                                  {
  
                                          for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
                                          {
                                                  for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
                                                  {
                                                          
                                                           end_points_mat[i][j] =  (220)+node_number; //totalnumber of pmos and nmos are assumed to be 2
                                                  }
                                          }
                                  }
				else if ( img == 4  ) // INPUT
                                  {
  
                                          for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
                                          {
                                                  for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
                                                  {
                                                          
                                                           end_points_mat[i][j] =  (240)+node_number; //totalnumber of pmos and nmos are assumed to be 2
                                                  }
                                          }
                                  }
				else if (  img == 10) // OUTPUT
                                  {
  
                                          for ( int i = end_pointsX[0] - 7 ; i < end_pointsX[0] +8 ; i ++ )
                                          {
                                                  for ( int j = end_pointsY[0] - 7 ; j < end_pointsY[0] +8; j ++ )
                                                  {
                                                          
                                                           end_points_mat[i][j] =  (260)+node_number; //totalnumber of pmos and nmos are assumed to be 2
                                                  }
                                          }
                                  }

			}
			public void make_end_points(int img )
			{
				if ( img == 1 || img == 7 || img == 8) // AND/OR/XOR 
				{
					count_end_points = 3;

					int a , b , c , d , e , f ;
					a = 0; b = height/2; c = 0; d = height/2 + height/4 ; e = 2*width ; f = height/2;  
					if ( angle_count == 1 ){
						a= -width; b = 0; c= -width-width/2; d = 0; e = -width; f = height;  
					}
					else if ( angle_count == 2 )
					{
						a = 0 ; b= -height/2 ; c = 0 ; d = -height/2-height/4 ; e =-2*width ; f = -height / 2 ;
					}
					else if ( angle_count == 3 )
					{
						a = width ; b= 0 ; c = width+width/2 ; d = 0 ; e = width ; f = -height ;
					}
					
					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;

					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d ;

					end_pointsX[2] = node_x + e  ;
					end_pointsY[2] = node_y +  f ;
					
				}
				
				else if ( img == 2 ) //GROUND
                                 {
                                         count_end_points = 1;
                                          int a , b  ;
                                          a = width / 2 ; b= 0 ;
  
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
				  else if ( img == 4 ) //INPUT
                                 {
                                          count_end_points = 1;
                                          int a , b  ;
                                          a = width ; b= 0;
  
                                          if ( angle_count == 1 )
                                          {
                                                  a = 0 ; b= width ;
                                          }
                                          else if ( angle_count == 2 )
                                          {
                                                  a = -width  ; b= 0 ;
                                          }
                                          else if ( angle_count == 3 )
                                          {
                                                  a = 0 ; b= -width ;
                                          }
                                          end_pointsX[0] = node_x + a ;
                                          end_pointsY[0] = node_y + b;
                                  }
				   else if ( img == 10 ) //OUTPUT
                                 {
                                          count_end_points = 1;
                                          int a , b  ;
                                          a = 0 ; b= 0;
  
                                          if ( angle_count == 1 )
                                          {
                                                  a = width ; b= 0 ;
                                          }
                                          else if ( angle_count == 2 )
                                          {
                                                  a = 0  ; b= -width ;
                                          }
                                          else if ( angle_count == 3 )
                                          {
                                                  a = -width ; b= 0;
                                          }
                                          end_pointsX[0] = node_x + a ;
                                          end_pointsY[0] = node_y + b;
                                  }
				else if (img == 9 ) { //NOT
					count_end_points = 2;
					int a,b,c,d;
					a = 0; b = height/2; c = width+width/2 ; d = height/2;   
					if ( angle_count == 1 )
                                          {
                                                  a = -width ; b= 0 ;c = -width; d = 3*(height/4); 
                                          }
                                          else if ( angle_count == 2 )
                                          {
                                                 a = 0; b = -height/2; c = -width-width/2 ; d = -height/2;
                                          }
                                          else if ( angle_count == 3 )
                                          {
                                                  a = width; b = 0; c = width ; d = -3*(height/4);
                                          }
					
                                          end_pointsX[0] = node_x + a ;
                                          end_pointsY[0] = node_y + b;
					  end_pointsX[1] = node_x + c ;
                                          end_pointsY[1] = node_y + d;
					
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


		 public class myDialog extends JDialog implements ActionListener {               
			private static final long serialVersionUID = 1L;
						JSpinner length ;
                          JSpinner width;
                          JSpinner capacitance;
                          Container cp;
                          JButton del ;
                          JButton ok ;
                          JButton rotate ;
                          int node_index ;
				int image_no;
				public myDialog (JFrame fr , String comp, int img_no,int node_no) {
                                  super (fr , "Component Description " , true ); // true to lock the main screen 
                                  node_index = node_no ;
					image_no=img_no;
  
                                  cp = getContentPane();
                                  SpringLayout layout = new SpringLayout();
                                  cp.setLayout(layout);
                                  setSize(350 , 200);
                                  if ( img_no == 3 ) // capacitor 
                                  {
                                          SpinnerModel capacitance_model =        new SpinnerNumberModel(5, //initial value
                                                          1, //min
                                                          10, //max
                                                          1);  //step
                                          JLabel comp_name = new JLabel("<html><font size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);
  
                                          layout.putConstraint(SpringLayout.WEST ,comp_name ,50,SpringLayout.WEST,cp);
                                          layout.putConstraint(SpringLayout.NORTH ,comp_name,20,SpringLayout.NORTH,cp);
  
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
  
                                     
  
                                          layout.putConstraint(SpringLayout.WEST , del , 20,   SpringLayout.WEST , cp );
                                          layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);
  
  
                                          layout.putConstraint(SpringLayout.WEST , ok , 10,   SpringLayout.EAST , del );
                                          layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);
  
  
                                          cp.add(comp_name);
                                          cp.add(c);
                                          cp.add(capacitance);
                                          cp.add(del);
                                          cp.add(ok);
                                          ok.addActionListener(this);
                                          del.addActionListener(this);
                                  }
				else
				{
                                        JLabel comp_name = new JLabel("<html><font size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);
					layout.putConstraint(SpringLayout.WEST , comp_name , 50,   SpringLayout.WEST , cp );
                                        layout.putConstraint(SpringLayout.NORTH , comp_name , 20,  SpringLayout.NORTH , cp);

                                         del = new JButton("Delete Component");
                                         ok = new JButton("O.K");
                                         rotate = new JButton("Rotate");

                                         layout.putConstraint(SpringLayout.WEST , del , 20,   SpringLayout.WEST , cp );
                                         layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);
  
                                         layout.putConstraint(SpringLayout.WEST , rotate , 10,   SpringLayout.EAST , del);
                                         layout.putConstraint(SpringLayout.NORTH , rotate , 100,  SpringLayout.NORTH , cp);
					 layout.putConstraint(SpringLayout.WEST , ok , 10,   SpringLayout.EAST , rotate );
                                         layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);

                                         cp.add(comp_name);
                                         cp.add(del);
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
                                          if ( image_no  == 3 ) //wire 
                                          {
                                                  hori_len = get_length();
                                          }
                                         
                                          setVisible(false);
                                          workPanel.repaint();
                                  }
				if(e.getSource() == rotate )
                                  {
					
                                          comp_node[node_index].rotate(node_index);
                                          workPanel.repaint();
                                  }

				 if(e.getSource() == del )
                                  {
                                          comp_node[node_index].del = true;
                                          comp_count[comp_node[node_index].img_no] -= 1; // for decreasing the count to check no of each comp
                                         
                                          comp_node[node_index].remove_mat();
                           if ( comp_node[node_index].img_no  == 1 ) //AND
                                          {
                                                  pmos_count--;
							pmos_delete[pmos_delete_count++]=comp_node[node_index].node_number;
                                          }
                         else if ( comp_node[node_index].img_no  == 7 ) //OR
                                          {
                                                  nmos_count--;
							nmos_delete[nmos_delete_count++]=comp_node[node_index].node_number;
                                          }
                       else if ( comp_node[node_index].img_no  == 2 ) //GROUND
                                          {
                                                  gnd_count--;
							gnd_delete[gnd_delete_count++]=comp_node[node_index].node_number;
					}
                      else if ( comp_node[node_index].img_no  == 9 ) //NOT
                                          {
                                                  vdd_count--;
							vdd_delete[vdd_delete_count++]=comp_node[node_index].node_number;
						}
					 else if ( comp_node[node_index].img_no  == 4 ) //INPUT
                                          {
                                                  input_count--;
							input_delete[input_delete_count++]=comp_node[node_index].node_number;
						}
					   else if ( comp_node[node_index].img_no  == 10 ) //OUTPUT
                                          {
                                                  output_count--;
							output_delete[output_delete_count++]=comp_node[node_index].node_number;
						}
                        else if ( comp_node[node_index].img_no  == 8 ) //XOR
                                          {
                                                  xor_count--;
							xor_delete[xor_delete_count++]=comp_node[node_index].node_number;
                                                  
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
                                          setVisible(false);
                                  }
                          }


		public class WorkPanel extends JPanel implements MouseMotionListener,MouseListener
		{
			private static final long serialVersionUID = 1L;
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
                                          int x = (work_x % 15)>7 ? (work_x/15)*15+15 : (work_x/15)*15; // for making good 
                                          int y = (work_y % 15)>7 ? (work_y/15)*15+15 : (work_y/15)*15; // accurate wire point around end points 
                                          wire[total_wire-1 ].update2(x , y);
                                          repaint();
                                  }


			}
			public void mouseDragged(MouseEvent me)
			{
                                  work_x = me.getX();
                                  work_y = me.getY();
					if(work_x<15)
						work_x=15;
					if(work_y<15)
						work_y=15;
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
				//int i;
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
                                                        wire[wire_mat[work_x][work_y]].del();
                                                        repaint();
                                                }
                                                else
                                                {
                                                      System.out.println("Not Deletded ");
                                                }

                                        }
                                        else if ( work_mat[work_x][work_y]!= -1 ) 
				//	else
					{
					       int temp = work_mat[work_x][work_y] ;
						if(temp!=-1)
						{
                                                  int temp1 = comp_node[temp].img_no ; // temp is no img no 
                                                  if (  temp1 == 1 || temp1 == 2 || temp1 == 3 || temp1 == 7 ||  temp1 == 8 || temp1==9 || temp1 == 4 || temp1 == 10 )
                                                  {
                                                       dialog = new myDialog( new JFrame() ,comp_str[temp1] , temp1,temp);
                                                       dialog.setVisible(true);
                                                          }
						}
					}
                                 }

				  else if (img_button_pressed == 3) { // i.e line is selected 
                                  
                    int x = (work_x % 15)>7 ? (work_x/15)*15+15 : (work_x/15)*15; // for making good 
                    int y = (work_y % 15)>7 ? (work_y/15)*15+15 : (work_y/15)*15; // accurate wire point around end points 
					System.out.println("X"+work_x);
					System.out.println("y"+work_y);
					System.out.println("wire_button"+wire_button);
					System.out.println("end_point__mat"+end_points_mat[work_x][work_y]);
  
                                 if ( wire_button == 0 ) { // button is pressed first time                                                                                      //if ( end_points_mat[work_x][work_y] != - 1 ) // if end points r there 
                                                  if ( end_points_mat[x][y] != - 1 ) // if end points r there 
                                                  {
                                                          wire[total_wire++] = new line(x,y , x, y);
                                                          repaint();
                                                          wire_button = 1 ;
                                                  }
                                                  else {
                                                       JOptionPane.showMessageDialog(null, "Wire could start OR end at the componet's connection points only ");
                                                  }
                                   }
	                               else { 
                                                 if ( end_points_mat[x][y] != - 1 ) { // if end points r there                                                                                                   
                                                          wire[total_wire - 1].update2(x , y ); // -1 bec inder of first wire is 0 
                                                          repaint();
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          img_button_pressed = -1 ;
                                                          change_selected(0);
                                                          wire_button = 0 ;
                                                  }
                                                  else {                                                  
                                                      wire[total_wire - 1].update(x , y ); // -1 bec inder of first wire is 0 
                                                      repaint();
                                                  }
                                          }
			}            			

				else
				{	
				 if(img_button_pressed!=-1)
				{
					if(work_x<15)
						work_x=15;
					if(work_y<15)
						work_y=15;
					if(img_button_pressed==1) {// AND					
						comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 4 * 15,pmos_count);
						pmos_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;						
					}
					else if(img_button_pressed==7) {	//OR				
						comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 4 * 15,nmos_count);
						nmos_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;						
					}
					 else if ( img_button_pressed == 2 ) { //ground                                    
                                                 comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 2 * 15,gnd_count);
                                        	 gnd_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;						
					}
					else if ( img_button_pressed == 4 ) { //ground                                    
                                                 comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 2 * 15,input_count);
                                        	 input_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;						
					}
					else if ( img_button_pressed == 10 ) { //ground                                    
                        comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2 , 2 * 15,output_count);
                        output_count++;
						comp_node[total_comp].update_mat(total_comp);
						total_comp++;						
					 }
                     else if ( img_button_pressed == 8  ) { // vertical wire  
                    	 	comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed ,15*2,4*15 , xor_count);
                            xor_count++;
                            comp_node[total_comp].update_mat(total_comp);
                            total_comp++;						
                     }
                     else if (  img_button_pressed == 9 ) { // NOT
                        comp_node[total_comp] = new node((work_x / 15)*15 , (work_y / 15 )*15 , img_button_pressed , 15 * 2, 4 * 15,vdd_count);
						 vdd_count++;
						 comp_node[total_comp].update_mat(total_comp);
						 total_comp++;
					 }
                      
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
                                         {
                                                 for ( j = comp_node[node_drag].node_y ; j < comp_node[node_drag].node_y + comp_node[node_drag].height ; j++ )
                                                 {
                                                         work_mat[i][j] =  node_drag ;   // update the matrix 
                                                 }
                                         }*/
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
			//	int i , j ;
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
				int mycount=0;
				int outcount=0;
				Graphics2D g2d = (Graphics2D)g;
				g2d.scale(scale_x , scale_y);
				// back ground ----------------
				g2d.setColor(Color.black);
				g.fillRect(0,0,work_panel_width+1000 , work_panel_height+1000);
				g2d.setColor(Color.white);
				g2d.setStroke(new BasicStroke(1));
				for ( i = 0 ; i < work_panel_width +400; i+=15)
				{
					for ( j = 0 ; j < work_panel_height+200 ; j+=15 )
					{
						g2d.drawOval(  i -1,j-1 , 0 , 0);
					}
				}
				for ( i = 0; i < total_comp ; i++ )
				{
					if ( comp_node[i].del != true )
					{
						if ( comp_node[i].img_no == 1)
						{
							draw_cmos(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle);
							g.setColor(Color.yellow);
							g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 3)
						{
							draw_horizontal_wire(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].width , comp_node[i].angle);
							g.setColor(Color.yellow);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 8)
						{
							draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y ,15, comp_node[i].angle);
							g.setColor(Color.yellow);
							g.drawString(comp_str[8] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 7)
						{
							draw_nmos(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15 , comp_node[i].angle);
							g.setColor(Color.yellow);
							g.drawString(comp_str[7] , comp_node[i].node_x + -10 , comp_node[i].node_y + 10 );



						}
						  else if ( comp_node[i].img_no == 2)
                                                 {
                                                         draw_ground(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
                                                       g.setColor(Color.yellow);
                                                       g.drawString(comp_str[2] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
                                                 }
						else if ( comp_node[i].img_no == 4 )
						{	mycount++;
							draw_input(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle,mycount);
						g.setColor(Color.yellow);
                                                       g.drawString(comp_str[4] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
				
						}
                                                 else if ( comp_node[i].img_no == 9)
                                                 {
                                                         draw_vdd(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
                                                         g.setColor(Color.yellow);
                                                         g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
                                                 }
						 else if ( comp_node[i].img_no == 10 )
						{	outcount++;
							draw_output(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle,outcount);				
							g.setColor(Color.yellow);
                                                         g.drawString(comp_str[10] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );					
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
			void draw_xor(Graphics2D g , int x , int y , int width , double angle )
			{
                                 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow); 
                                 g.setStroke(new BasicStroke(2));
                                 g.setColor(Color.blue);                            
                                 g.drawLine(x,y+2*width,x+width+width/2,y+2*width);
  				 g.drawLine(x,y+3*width,x+width+width/2,y+3*width);
				 g.drawArc(x+width-width/2,y+2*width-width/2,width,2*width,90,-180);
				 g.drawArc(x+width-width/4,y+2*width-width/2,width,2*width,90,-180);
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
			void draw_nmos(Graphics2D g , int x , int y , int width , double angle )
                         {
                                 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow); 
                                 g.setStroke(new BasicStroke(2));
                                 g.setColor(Color.blue);                            
                                 g.drawLine(x,y+2*width,x+width+width/2,y+2*width);
  				 g.drawLine(x,y+3*width,x+width+width/2,y+3*width);
				 g.drawArc(x+width-width/2,y+2*width-width/2,width,2*width,90,-180);
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

			void draw_cmos(Graphics2D g , int x , int y , int width , double angle)
                         {
				 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow);
                                 g.setStroke(new BasicStroke(2));
				 g.setColor(Color.blue);
				 g.drawLine(x,y+2*width,x+width,y+2*width);
  				 g.drawLine(x,y+3*width,x+width,y+3*width);
				 g.drawLine(x+width,y+2*width-width/2,x+width,y+3*width+width/2);
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
			 void draw_ground(Graphics2D g , int x , int y , int width , double angle)
                         {
                                 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow);
 
                                 g.setColor(Color.blue);
                                 g.setStroke(new BasicStroke(2));
                                 g.drawLine(x +width  , y  , x + width ,y + (5*width)/4);
                                 g.drawLine(x +width/2  , y + (5*width)/4 , x + (3*width)/2 ,y + (5*width)/4);
                                 g.drawLine(x +(3*width)/4  , y + (6*width)/4 , x + (5*width)/4 ,y + (6*width)/4);
                                 g.drawLine(x +(7*width)/8  , y + (7*width)/4 , x + (9*width)/8 ,y + (7*width)/4);
                                 g.setStroke(new BasicStroke(1)); 
                                 g.setColor(Color.red);
                                 g.fillRect( x +width - 4, y  -4, 8 ,8 );
                                 g.rotate(-angle , x , y);
 
                         }
			void draw_vdd(Graphics2D g , int x , int y , int width , double angle)
                         {
                                g.rotate(angle , x , y);
				g.setColor(Color.yellow);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.drawLine(x,y+2*width,x+width,y+2*width);
				g.drawLine(x +width,y +2*width-width/2 , x+width ,y +2*width+width/2);
				g.drawLine(x +width , y +2*width-width/2, x +2*width ,y +2*width);
				g.drawLine(x+width,y+2*width+width/2, x+2*width,y+2*width);
				g.drawOval(x+2*width,y+2*width-4,8,8);
				g.drawLine(x+2*width+8,y+2*width,x+3*width,y+2*width);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect( x - 4, y + 2*width -4, 8 ,8 );
				g.fillRect( x + 3*width -4, y +2*width - 4 , 8 ,8 );
				g.rotate(-angle , x , y);
                         }
			void draw_input(Graphics2D g , int x , int y , int width , double angle,int abc)
			{
				String xx;
				xx=""+abc;
				//mycount++;
				//int xx=input_count-1;
				g.rotate(angle , x , y);	
				g.setColor(Color.yellow);
				//xx=input_count.toString();
				g.drawString(xx, x + 5 , y+ width - 5);
			//	g.drawRect( x , y , 2 *width , width);

				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.drawLine(x  , y  , x + 2*width ,y );
				g.drawLine(x  , y + width  , x + width ,y + width );
				g.drawLine(x  , y , x , y + width );
			//	g.drawLine(x + width , y , x + (3*width)/2 , y + width/2);
				g.drawLine(x + width , y + width , x + (3*width)/2 , y );
			
				g.setStroke(new BasicStroke(1));
				// end points 
				g.setColor(Color.red);
				g.fillRect( x + 2*width - 4, y  -4, 8 ,8 );
				g.rotate(-angle , x , y);
			}
			void draw_output(Graphics2D g , int x , int y , int width , double angle,int abc)
			{
				String xx;
				xx=""+abc;
				//outcount++;
				g.rotate(angle , x , y);
				g.setColor(Color.yellow);
				g.drawString(xx, x  + width / 2, y+ width - 5);
			
			//	g.drawRect( x , y , 2 *width , width);

				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.drawLine(x + width/2 , y  , x + (3*width)/2 ,y );
				g.drawLine(x + width/2 , y + width  , x + (3*width)/2 ,y + width );
				g.drawLine(x + width/2  , y , x + width/2 , y + width );

				g.drawLine(x + (3*width)/2 , y , x + 2*width , y + width/2);
				g.drawLine(x + (3*width)/2, y +	 width , x + 2*width , y + width/2);
			
				g.drawLine(x , y  , x + width/2 , y );
			
				g.setStroke(new BasicStroke(1));
				// end points 
				g.setColor(Color.red);
				g.fillRect( x  - 4, y -4, 8 ,8 );
				g.rotate(-angle , x , y);
			}





}
}
}
