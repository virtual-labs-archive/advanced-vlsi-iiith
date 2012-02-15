import java.util.*;
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
import netscape.javascript.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.ImageIcon.*;

/*
   Img No.:End point Values:Component
   2	8	CLOCK
   1	0,1,2	AND
   7	3,4,5	T-Flip Flop
   4	9	Input 1
   5	10	Input 2
   10	11	Output


   1		0,1,2				AND
   2,3,4,5,6	3,4,5,6,7			Input
   7,8,9,10,11	8-10,11-13,14-16,17-19,20-22	XOR  <- T Flip Flop
   12,13,14	23,24,25			Output
   15,16		26-30,31-35 			MUX  <- CLOCK



 */
public class exp1 extends JApplet 
{
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

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */

	private  void createAndShowGUI() {
		MyPanel myPane = new MyPanel();
		myPane.setOpaque(true);
		setContentPane(myPane);
	}

	public  class MyPanel extends JPanel  implements ActionListener , MouseMotionListener 
	{
		int exp_type = 0 ; // 0 -> Complementary Inverter , 1 -> Pseudo Inverter 
		int technology=80;
		/** Work Panel Variables ************************************************************************************************/
		double scale_x = 1 ;  // scaling of work pannel 
		double scale_y = 1 ;
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
		int work_panel_width = 1050 ;
		int work_panel_height = 550;

		int node_drag = -1 ; // it rep the index of comp_node which is selected to be draged 
		int wire_drag = -1 ; // it rep the index of wire which is selected to be extented from its end 
		int wire_drag_end = 1 ; // from which end it should be draged 
		int[] comp_count = new int[20] ; //  comp_count[i] represents the count of ( comp"i".jpg ) component ..
		int[] INPUT=new int[5];
		int[] XOR=new int[5];
		int[] OUTPUT=new int[3];
		int[] MUX=new int[2];
		int total_comp = 0 ;

		node[] comp_node = new node[20];

		int total_wire = 0 ;
		line[] wire = new line[100];

		// Dialog Box -----------------------------------
		myDialog[] dialog = new myDialog[100]  ; //in this exp at max 6 comp can be used  (I assume that comp is used once )
		JFrame[] fr = new JFrame[100] ;
		String[] comp_str = {		// This will store what should at the Dialog Box for each component
			"This is shows which component is selected ." ,
			"AND","INPUT 1","INPUT 2","INPUT 3","INPUT 4","INPUT 5","XOR 1","XOR 2",
			"XOR 3","XOR 4","XOR 5","OUTPUT 1","OUTPUT 2","OUTPUT 3","MUX 1","MUX 2",
			"WIRE" ,"This is CMOS Chip No:1"};

		//*************************************************************************************************************************************
		//Circuit Component values which need to be send to ngspice ***************************************************************************

		String Pmos_l = "50n";
		String Pmos_w = "100n";

		String Nmos_l = "50n";
		String Nmos_name = "";
		String Nmos_w = "100n";

		String Capacitance = "100";
		//*************************************************************************************************************************************
		boolean simulate_flag = false ;
		Image img[] = new Image[20] ;
		ImageIcon icon[] = new ImageIcon[20] ;

		ImageIcon icon_simulate ;
		ImageIcon icon_layout ;
		ImageIcon icon_graph ;
		ImageIcon icon_graph1;

		MediaTracker mt ;
		URL base ;

		public 	class node 
		{
			int node_x ;
			int node_y ;
			int img_no ;
			int width ;
			int height ;

			int virtual_w ;
			int virtual_h ;

			double angle ;
			int angle_count ; // 0-> 0 / 360 degree , 1 -> 90 degree , 2 -> 180 degree , 3 -> 270 degree

			boolean del ;

			// for connection with wire 
			int end_pointsX[] = new int[5];   //end point of nmos pmos etc(nmos has 3)
			int end_pointsY[] = new int[5];
			int count_end_points = 0 ;

			public node (int x , int y , int no , int w , int h)
			{
				node_x = x ;    // nearby_xpos
				node_y = y ;
				img_no = no ;    //defined for all image
				del = false ;

				virtual_w = width = w ;
				virtual_h = height = h ;
				angle = 0 ;
				angle_count = 0 ;

				make_end_points(no);
			}
			public void update_end_points_mat(int img)
			{
				if ( img == 1) // AND
				{
					for ( int k = 0 ; k < 3 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 4 ; i < end_pointsX[k] + 5; i ++ )
						{
							for ( int j = end_pointsY[k] - 4 ; j < end_pointsY[k] +5; j ++ )
							{
								end_points_mat[i][j] = k ;     //contains what end_point is stored at what x,y(i,j) coord 
							}
						}
					}
				}
				else if ( (img >= 7)&&(img<=11))//  XOR
				{
					for ( int k = 0 ; k < 3 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 4 ; i < end_pointsX[k] +5; i ++ )
						{
							for ( int j = end_pointsY[k] - 4 ; j < end_pointsY[k] + 5; j ++ )
							{
								end_points_mat[i][j] = 8+((img-7)*3)+k;
							}
						}
					}
				}
				else if ( (img == 15) || (img==16)  ) // MUX 
				{
					for ( int k = 0 ; k < 4 ; k ++ )
					{
						for ( int i = end_pointsX[k] - 4 ; i < end_pointsX[k] +5 ; i ++ )
						{
							for ( int j = end_pointsY[k] - 4 ; j < end_pointsY[k] +5; j ++ )
							{
								end_points_mat[i][j] = 26+((img-15)*4)+k;
							}
						}
					}
				}
				else if ((img>=2)&&(img<=6))//INPUT
				{
					for ( int i = end_pointsX[0] - 4 ; i < end_pointsX[0] +5 ; i ++ )
					{
						for ( int j = end_pointsY[0] - 4 ; j < end_pointsY[0] +5; j ++ )
						{
							end_points_mat[i][j] =  img+1;
						}
					}
				}
				else if((img>=12)&&(img<=14)) // OUTPUT
				{
					for ( int i = end_pointsX[0] - 4 ; i < end_pointsX[0] +5 ; i ++ )
					{
						for ( int j = end_pointsY[0] - 4 ; j < end_pointsY[0] +5; j ++ )
						{
							end_points_mat[i][j] =  23+(img-12);
						}
					}
				}


			}
			public void make_end_points(int img )
			{
				if ( img == 1 || ((img >= 7)&&(img<=11))) // AND,XOR 
				{
					count_end_points = 3;

					int a , b , c , d , e , f ;
					a = -(width/3) ; b= height ; c = -(width/3) ;d = 0 ; e = 2*(width/3) ; f = 0 ;

					//FOR ROTATE
					/*					if ( angle_count == 1 )
										{
										a = -(width/3) ; b= 0 ; c = 0 ; d = 0; e = 0 ; f = 2*(width/3) ;
										}
										else if ( angle_count == 2 )
										{
										a = -width ; b= 0 ; c = -width ; d = -height ; e = 0 ; f = -height / 2 ;
										}
										else if ( angle_count == 3 )
										{
										a = 0 ; b= -width ; c = height ; d = -width ; e = height/2 ; f = 0 ;
										}*/
					end_pointsX[0] = node_x + a;
					end_pointsY[0] = node_y + b;

					end_pointsX[1] = node_x + c;
					end_pointsY[1] = node_y + d;

					end_pointsX[2] = node_x + e;
					end_pointsY[2] = node_y + f;

				}
				else if ( (img == 15)||(img==16)) // MUX
				{
					count_end_points = 4 ;
					int a , b , c , d , e , f , g , h ;
					a = 0 ; b = 2*height ; c = -(width/3) ; d= height ;
					e = -(width/3) ;f = 0 ;g = 2*(width/3) ; h=0 ;				
					//FOR ROTATE
					//		e=;f=;g=;h=;

					/*					if ( angle_count == 1 )
										{
										a = -height/2 ; b= 0 ; c = -height/2 ; d = width ;
					//		e=;f=;g=;h=;
					}
					else if ( angle_count == 2 )
					{
					a = 0 ; b= -height/2 ; c = -width ;d =  -height / 2 ;
					//		e=;f=;g=;h=;
					}
					else if ( angle_count == 3 )
					{
					a = height/2 ; b= 0 ; c = height/2 ; d = -width ;
					//		e=;f=;g=;h=;
					}*/
					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;

					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d ;

					end_pointsX[2] = node_x + e ;
					end_pointsY[2] = node_y + f ;

					end_pointsX[3] = node_x + g ;
					end_pointsY[3] = node_y + h ;
				}
			       	else if ( (img >= 2) && (img <= 6))  //INPUT
				  {
					  count_end_points = 1 ;
					  end_pointsX[0] = node_x + width ;
					  end_pointsY[0] = node_y  ;
				  }
				  else if ( (img >= 12) && (img <= 14))  //OUTPUT
				  {
					  count_end_points = 1 ;
					  end_pointsX[0] = node_x  ;
					  end_pointsY[0] = node_y  ;
				  }
				  update_end_points_mat(img);
			}
			public void del ()
			{
				del = true ;
			}
			public void rotate(int index )
			{
				remove_mat() ;
				//		delete the previous value from work_mat
				//		angle = angle +  (java.lang.Math.PI/2);
				angle_count = (angle_count + 2 )% 4 ;
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
			public void update_mat(int index) 
				// update the matrix value to work_mat // index is the index of the node_comp matrix
				//this is done so that no new items can be placed where already an item
			{
				int i , j ;
				for ( i = node_x ;  ;)
				{
					if ( virtual_w > 0 && i >= node_x + virtual_w  ){break;}
					else if( virtual_w < 0 && i <= node_x + virtual_w  ){break;}

					for ( j = node_y ;  ;  )
					{
						if ( virtual_h > 0 && j >= node_y + virtual_h  ){break;}
						else if( virtual_h < 0 && j <= node_y + virtual_h  ){break;}

						work_mat[i][j] =  index ;   // update the matrix as the img is selected  
						if ( virtual_h > 0 ){j++;}else{j--;} 
					}

					if ( virtual_w > 0 ){i++;}else{i--;} 
				}
				make_end_points(img_no);
			}
			public void remove_mat() // delete the previous value from work_mat
			{
				int i , j ;
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
					for ( int i = end_pointsX[k] - 4 ; i < end_pointsX[k] +5 ; i ++ )
					{
						for ( int j = end_pointsY[k] - 4 ; j < end_pointsY[k] +5; j ++ )
						{
							end_points_mat[i][j] = -1;
						}
					}

				}
			}
		}
		public class line 
		{
			int x1 , y1 , x2 , y2 ; // end and start point of wire 
			int x[] = new int[20];
			int y[] = new int[20];
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
				//				end_index++;
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
			public void update_wire_mat(int index )  //wire mat stores all the cords in the way
			{
				int i , j , tx1 , ty1 , tx2 , ty2 ; // local vriables 
				for ( int k = 0 ; k < end_index ; k ++)
				{ 
					tx1 = x[k] ;	tx2 = x[k + 1] ;
					ty1 = y[k] ;	ty2 = y[k + 1] ;
					for ( i = tx1 ;  ;)
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

		public class myDialog extends JDialog implements ActionListener 
		{
			JSpinner length ;
			JTextField text;
			JSpinner width;
			JSpinner capacitance;
			Container cp;
			JButton del ;
			JButton ok ;
	//		JButton ok1 ;
			JButton rotate ;
			int node_index ;
			public myDialog (JFrame fr , String comp, int node_no)
			{

				super (fr , "Component Description " , true ); // true to lock the main screen 
				node_index = node_no ;
				cp = getContentPane();

			if(node_no==1000)
			{
				setSize(850 , 620);
				ok = new JButton("O.K");
				cp.add(ok);
				cp.add(rightPanel);
			}
			else
			{
				SpringLayout layout = new SpringLayout();
				cp.setLayout(layout);
				setSize(350 , 200);
				if ( (comp_node[node_no].img_no >= 1) &&(comp_node[node_no].img_no <= 16))// terminal
				{
					SpinnerModel length_model =	new SpinnerNumberModel(50, //initial value
							0, //min
							1000, //max
							1);  //step
					SpinnerModel width_model =	new SpinnerNumberModel(100, //initial value
							0, //min
							1000, //max
							1);  //step

					JLabel comp_name = new JLabel("<html><font size=4><b>"+comp+"</b></font></html>" );//,icon[icon_no],JLabel.CENTER);

					layout.putConstraint(SpringLayout.WEST , comp_name , 130,   SpringLayout.WEST , cp );
					layout.putConstraint(SpringLayout.NORTH , comp_name , 20,  SpringLayout.NORTH , cp);

					length = new JSpinner(length_model);
					width = new JSpinner(width_model);
					text = new JTextField(15);
					JLabel w = new JLabel("Select the Width :");
					JLabel w_unit = new JLabel("nanometer");
					del = new JButton("Delete Component");
					ok = new JButton("O.K");
					rotate = new JButton("Rotate");


					layout.putConstraint(SpringLayout.WEST , w , 20,   SpringLayout.WEST , cp );
					layout.putConstraint(SpringLayout.NORTH , w , 50,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , width , 20,   SpringLayout.EAST , w );
					layout.putConstraint(SpringLayout.NORTH , width , 50,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , w_unit , 10,   SpringLayout.EAST , width );
					layout.putConstraint(SpringLayout.NORTH , w_unit , 50,  SpringLayout.NORTH , cp);

					JLabel l = new JLabel("Input name :");
					JLabel l_unit = new JLabel("(Max 15 character)");

					layout.putConstraint(SpringLayout.WEST , l, 35,   SpringLayout.WEST , cp );
					layout.putConstraint(SpringLayout.NORTH , l , 45,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST ,text, 15,   SpringLayout.EAST , l );
					layout.putConstraint(SpringLayout.NORTH ,text , 45,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , l_unit , 95,   SpringLayout.WEST ,l );
					layout.putConstraint(SpringLayout.NORTH , l_unit , 65,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , del , 20,   SpringLayout.WEST , cp );
					layout.putConstraint(SpringLayout.NORTH , del , 100,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , rotate , 10,   SpringLayout.EAST , del);
					layout.putConstraint(SpringLayout.NORTH , rotate , 100,  SpringLayout.NORTH , cp);

					layout.putConstraint(SpringLayout.WEST , ok , 10,   SpringLayout.EAST , rotate );
					layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);


					cp.add(comp_name);
					cp.add(l);
					cp.add(l_unit);
					cp.add(text);
					cp.add(del);
					cp.add(ok);
					//	if(comp_node[node_no].img_no == 2 ||comp_node[node_no].img_no == 1 ||comp_node[node_no].img_no == 7 )
					//	cp.add(rotate);
					
					ok.addActionListener(this);
					del.addActionListener(this);
					rotate.addActionListener(this);
				}}
				addWindowListener( new WA());

			}
			String get_length()
			{
				return length.getValue().toString()+"n";
			}
			String get_width()
			{
				return width.getValue().toString()+"n";
			}
			String get_name()
			{
				return text.getText();
			}
			String get_capacitance()
			{
				return capacitance.getValue().toString();
			}
			public void actionPerformed(ActionEvent e )
			{
				if(e.getSource() == ok )
				{
					System.out.println("HI ok button is pressed ");
					if ( (comp_node[node_index].img_no >= 1)&&(comp_node[node_index].img_no <= 16))// terminal
					{
						Nmos_name=get_name();
						//System.out.println(Nmos_name);
						if(Nmos_name.equals("")!=true)
							comp_str[comp_node[node_index].img_no]=Nmos_name;
					}
					setVisible(false);
					workPanel.repaint();
				}
				if(e.getSource() == del )
				{
					System.out.println("HI  del is pressed ");
					//	System.out.println(node_index);
					comp_node[node_index].del = true;
					//FIX ME

					if((comp_node[node_index].img_no>=2)&&(comp_node[node_index].img_no<=6))   //INPUT
						INPUT[comp_node[node_index].img_no-2]=0;
					if((comp_node[node_index].img_no>=7)&&(comp_node[node_index].img_no<=11))  //XOR
						XOR[comp_node[node_index].img_no-7]=0;
					if((comp_node[node_index].img_no>=12)&&(comp_node[node_index].img_no<=14))  //OUTPUT
						OUTPUT[comp_node[node_index].img_no-12]=0;
					if((comp_node[node_index].img_no>=15)&&(comp_node[node_index].img_no<=16))  //MUX
						MUX[comp_node[node_index].img_no-15]=0;

					//FIX ME
					else
						comp_count[comp_node[node_index].img_no] -= 1; // for descrising the count to check no of each comp
					int i , j ;

					comp_node[node_index].remove_mat();
					// updating values of comp in file -------------------------------
					if ( comp_node[node_index].img_no  == 1 ) //PMOS
					{
						Pmos_l = Pmos_w = null ;
					}
					else if ( comp_node[node_index].img_no  == 7 ) //NMOS
					{
						Nmos_l = Nmos_w = null ;
					}
					else if ( comp_node[node_index].img_no  == 8 ) //Capacitor 
					{
						Capacitance = null;
					}
					setVisible(false);
					//					work_panel_repaint();
					workPanel.repaint();
				}
				if(e.getSource() == rotate )
				{
					comp_node[node_index].rotate(node_index);
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
		public  class WorkPanel extends JPanel  implements  MouseMotionListener , MouseListener   
		{
			public WorkPanel()
			{
				Arrays.fill(comp_count , 0);
				INPUT[0]=0;INPUT[1]=0;INPUT[2]=0;INPUT[3]=0;INPUT[4]=0;
				XOR[0]=0;XOR[1]=0;XOR[2]=0;XOR[3]=0;XOR[4]=0;
				OUTPUT[0]=0;OUTPUT[1]=0;OUTPUT[2]=0;
				MUX[0]=0;MUX[1]=0;

				addMouseMotionListener(this); // whole panel is made to detect 
				addMouseListener(this); // whole panel is made to detect 
				work_mat = new int[work_panel_width][work_panel_height];
				end_points_mat = new int[work_panel_width][work_panel_height];
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

			}

			public void mouseMoved(MouseEvent me) 
			{ 
				work_x = me.getX();
				work_y = me.getY();
				if ( img_button_pressed == 17 && wire_button == 1 ) 
				{
					int x = (work_x % 10)>5 ? (work_x/20)*20+20 : (work_x/20)*20; // for making good 
					int y = (work_y % 10)>5 ? (work_y/20)*20+20 : (work_y/20)*20; // accurate wire point around end points 
					wire[total_wire-1 ].update2(x , y);
					repaint();
				}
			} 
			public void mouseDragged(MouseEvent me)
			{
				int i , j;
				work_x = me.getX();
				work_y = me.getY();

				if ( wire_drag != -1 )
				{
					if( wire_drag_end == 1 )		// if first end is draged
					{
						wire[total_wire-1 ].update1((work_x/20)*20 , (work_y/20)*20);
					}
					else
					{
						wire[total_wire-1 ].update2((work_x/20)*20 , (work_y/20)*20);
					}
					repaint();
				}
				else 
				{
					if ( total_comp > 0 )
					{
						for ( i = work_x -30; i < work_x + comp_node[total_comp -1].width +30; i++ ) 
						{
							for ( j = work_y -30 ; j < work_y + comp_node[total_comp-1].height+30 ; j++ )
							{
								if(i <= 20 || j <= 20||i >= work_panel_width || j >= work_panel_height || (work_mat[i][j] != -1 && work_mat[i][j] != node_drag))
								{
									return;
								}
							}	
						}
						// for boundary check even if rotated (by adding heights bec that could be maxima )
						int t1 = comp_node[total_comp-1].height ;
						if ( comp_node[total_comp-1].width > t1 )
						{
							t1 = comp_node[total_comp-1].width ;
						}

						for ( i = work_x - t1 ; i < work_x + t1 ; i++ ) 
						{
							for ( j = work_y - t1 ; j < work_y + t1 ; j++ )
							{
								if(i <= 20 || j <= 20 || i >= work_panel_width || j >= work_panel_height)
								{
									return;
								}
							}	
						}

					}
					if (node_drag != -1 )
					{
						comp_node[node_drag].remove_mat();

						comp_node[node_drag].node_x  = (work_x /20 )*20 ;
						comp_node[node_drag].node_y  = ( work_y /20)*20 ;

						comp_node[node_drag].update_mat(node_drag);
						//	System.out.println(node_drag ) ;
					}
				}

				repaint();
			}
			public void mouseClicked(MouseEvent me) 
			{
				int i , j ;
				work_x = me.getX();
				work_y = me.getY();

				if ( img_button_pressed == -1 ) // for selecting anything on work panel
				{
					if ( wire_mat[work_x][work_y] != -1 )
					{
						//System.out.println("wire_mat[work_x][work_y]");
						//System.out.println(wire_mat[work_x][work_y]);
						JFrame wire_f = new JFrame();
						int n = JOptionPane.showConfirmDialog( wire_f, "Do u want to Delete Wire ?","Wire", JOptionPane.YES_NO_OPTION);
						if ( n == 0 )
						{
							//	System.out.println("Deletded ");
							wire[wire_mat[work_x][work_y]].del();
							repaint();
						}
						else
						{
							//	System.out.println("Not Deletded ");
						}

					}
					else if ( work_mat[work_x][work_y]!= -1 ) // there is a comp on (work_x , work_y) here temp rep inder of comp_nodearray
					{
						int temp = work_mat[work_x][work_y] ; 
						int temp1 = comp_node[temp].img_no ; // temp is no img no 
						if ( ( temp1 >= 1) &&( temp1 <= 16))
						{
							//JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");
							if ( dialog[temp] == null )
							{
								fr[temp] = new JFrame(); // bec work_mat will store the index of that comp in mat
								dialog[temp] = new myDialog( fr[temp] ,comp_str[temp1] , temp);
							}
							dialog[temp].setVisible(true);
						}
						else if (  temp != 0 )
						{
							JOptionPane.showMessageDialog(null, "You can't change value of this component !! :)");
						}
					}


				}
				else if (img_button_pressed == 17) // i.e line is selected 
				{
					int x = (work_x % 10)>5 ? (work_x/20)*20+20 : (work_x/20)*20; // for making good 
					int y = (work_y % 10)>5 ? (work_y/20)*20+20 : (work_y/20)*20; // accurate wire point around end points 

					if ( wire_button == 0 ) // button is pressed first time 
					{	
						if ( end_points_mat[work_x][work_y] != - 1 ) // if end points r there 
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

						if ( end_points_mat[work_x][work_y] != - 1 ) // if end points r there 
						{
							wire[total_wire - 1].update2(x , y ); // -1 bec inder of first wire is 0 
							repaint();

							wire[total_wire - 1 ].update_mat(total_wire - 1); // 

							img_button_pressed = -1 ;
							change_selected(0);
							wire_button = 0 ;
							System.out.println("Hi I am in want to leave :)");
						}
						else
						{	// adding more end points 
							wire[total_wire - 1].update(x , y ); // -1 bec inder of first wire is 0 
							repaint();
						//	System.out.println("wire[total_wire-1].end_index");
						//	System.out.println(wire[total_wire-1].end_index);
						}
					}
				}
				else  // For adding comp kdjhksahdkahdk 
				{
					if ( img_button_pressed != -1  )
					{
						draw_work = 1;
						//				System.out.println("draw_work set to 1 ");
					}
					//System.out.println(work_x+","+work_y);
					// creating the node for printing each comp 
					if ( img_button_pressed == 1|| ((img_button_pressed >= 7)&&(img_button_pressed<=11))) // and or xor
					{
						comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 20 * 3 , 20);
					}
					else if ( img_button_pressed == 15 || img_button_pressed == 16) // MUX
					{
						comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 20 * 3 , 20);
					}
					else if (  ((img_button_pressed >=2) &&( img_button_pressed <= 6)) || ((img_button_pressed >= 12)&&(img_button_pressed<=14)) ) // Input , Output
					{
						comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 20 * 2, 20);
					}
					else
					{
						comp_node[total_comp] = new node((work_x / 20)*20 , (work_y / 20 )*20 , img_button_pressed , 20 * 3, 2 * 20);
					}
				//	System.out.println("img_button_pressed");
				//	System.out.println(img_button_pressed);


					// if the surrounding have some object -------- -30

					for ( i = work_x -30; i < work_x + comp_node[total_comp].width +30; i++ ) 
					{
						for ( j = work_y -30 ; j < work_y + comp_node[total_comp].height+30 ; j++ )
						{
							if(i <= 20 || j <= 20||i >= work_panel_width || j >= work_panel_height || work_mat[i][j] != -1)
							{
								return;
							}
						}	
					}

					int t1 = comp_node[total_comp].height ;
					if ( comp_node[total_comp].width > t1 )
					{
						t1 = comp_node[total_comp].width ;
					}

					// for boundary check even if rotated (by adding heights bec that could be maxima )
					for ( i = work_x - t1 ; i < work_x + t1 ; i++ ) 
					{
						for ( j = work_y - t1 ; j < work_y + t1 ; j++ )
						{
							if(i <= 20 || j <= 20 || i >= work_panel_width || j >= work_panel_height)
							{
								return;
							}
						}	
					}
					//--------------------------------------------------------------------
					// updating the matrix 
					comp_node[total_comp].update_mat(total_comp);
					comp_count[img_button_pressed]++ ;
					total_comp++;

					repaint();
					img_button_pressed = -1 ;
					draw_work = 0 ;

					change_selected(0);
				}
			}

			public void mouseReleased(MouseEvent me) 
			{
				int i , j ;
				if ( wire_drag != -1 )
				{
					wire[wire_drag].update_mat(wire_drag); // updating the matrix
					wire_drag = -1;   // node is unseledted to drag
				}
				else if ( node_drag != -1 )
				{
					comp_node[node_drag].update_mat(node_drag); // updating the matrix
					node_drag = -1;   // node is unseledted to drag
				}

			}

			public void mouseEntered(MouseEvent me) 
			{
			}
			public void mouseExited(MouseEvent me) 
			{
			}

			public void mousePressed(MouseEvent me) 
			{
				int i , j ;
				work_x = me.getX();
				work_y = me.getY();
			//	System.out.println( work_mat[work_x][work_y]  );
				if ( wire_points_mat[work_x][work_y] != -1 )
				{
					wire_drag = wire_points_mat[work_x][work_y] ;
					wire[wire_points_mat[work_x][work_y]].update_mat(-1);
					// checking wich end is selected 
					wire_drag_end = 1 ;
					for ( i = work_x - 10 ; i < work_x + 11 ; i++ )
					{
						if ( i == wire[wire_drag].x2 )
						{
							wire_drag_end = 2 ;
						}
					}
				}
				else if ( work_mat[work_x][work_y] != -1 )
				{
					node_drag = work_mat[work_x][work_y];   // node is selected for drag
					comp_node[node_drag].remove_mat(); 		   // update the matrix as the img is selected , so can be moved 
				}
			}


			public void paint(Graphics g) 
			{
				int i , j ;
				Graphics2D g2d = (Graphics2D)g;
				g2d.scale(scale_x , scale_y);
				// back ground ----------------
				g2d.setColor(Color.white);
				g.fillRect(0,0,work_panel_width+500 , work_panel_height+500);
				g2d.setColor(Color.yellow);
				g2d.setStroke(new BasicStroke(2));
				g2d.setStroke(new BasicStroke(2));
				g2d.setStroke(new BasicStroke(1));
				for ( i = 0 ; i < work_panel_width +400; i+=20)
				{
					for ( j = 0 ; j < work_panel_height+200 ; j+=20 )
					{
						g2d.fillOval(  i -2,j-2 , 4 , 4);
					}
				}

				//For Images --------------------------------
				for ( i = 0; i < total_comp ; i++ )
				{
					if ( comp_node[i].del != true )
					{
						if ( comp_node[i].img_no == 1)
						{
							draw_and(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20 , comp_node[i].angle);
							g.setColor(Color.blue);
							g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y - 10 );
						}
						else if ( (comp_node[i].img_no >= 7) &&(comp_node[i].img_no <= 11))
						{
							draw_xor(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20 , comp_node[i].angle);
							g.setColor(Color.blue);
							g.drawString(comp_str[comp_node[i].img_no] , comp_node[i].node_x + -10 , comp_node[i].node_y - 10 );

						}
						else if ( (comp_node[i].img_no >= 15)&&(comp_node[i].img_no<=16))
						{
							draw_mux(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20, comp_node[i].angle);
							g.setColor(Color.blue);
							g.drawString(comp_str[comp_node[i].img_no] , comp_node[i].node_x + -15 , comp_node[i].node_y - 15 );
						}
						else if ( (comp_node[i].img_no >= 2)&&(comp_node[i].img_no<=6))
						{
							draw_input(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20, comp_node[i].angle);
							g.setColor(Color.blue);
							g.drawString(comp_str[comp_node[i].img_no] , comp_node[i].node_x - 5 , comp_node[i].node_y - 5 );
						}
						else if ( (comp_node[i].img_no >= 12)&&(comp_node[i].img_no<=14))
						{
							draw_output(g2d , comp_node[i].node_x  , comp_node[i].node_y , 20, comp_node[i].angle);
							g.setColor(Color.blue);
							g.drawString(comp_str[comp_node[i].img_no] , comp_node[i].node_x - 10 , comp_node[i].node_y - 5 );
						}
						else 
						{
							g2d.drawImage(img[comp_node[i].img_no] , comp_node[i].node_x ,comp_node[i].node_y, work_img_width , work_img_height,  this);
						}
					}
				}

				// For Wires ------------------------------------
				g2d.setStroke(new BasicStroke(2));
				for ( i = 0 ; i < total_wire ; i++ )
				{
					if ( wire[i].del == false )
					{
						g2d.setColor(Color.green);
						for ( int k = 0 ; k < wire[i].end_index ; k ++ )
						{
							g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k] );
							g2d.drawLine (wire[i].x[k+1] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
						}

						g2d.setColor(Color.red);
						g2d.fillRect (wire[i].x1 -4  , wire[i].y1 -4 , 8 ,8);
						g2d.fillRect (wire[i].x2 - 4 , wire[i].y2 -4 , 8 ,8);
					}
				}
			}
			void draw_xor(Graphics2D g , int x , int y , int width , double angle )
			{
				g.rotate(angle , x , y);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				g.drawLine(x-width,y,x,y);
				g.drawLine(x-20,y+width,x,y+width);
				g.drawRect(x,y-5,20,30);
				g.drawLine(x+20,y+10,x+40,y+10);
				g.drawLine(x+40,y,x+40,y+10);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect(x-4-20,y-4,8,8);
				g.fillRect(x-4-20,y+20-4,8,8);
				g.fillRect(x-4+40,y-4,8,8);
				g.setColor(Color.black);
				g.rotate(-angle , x , y);
			}
			void draw_and(Graphics2D g , int x , int y , int width , double angle)
			{

				g.rotate(angle , x , y);
				g.setColor(Color.blue);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.drawLine(x-width,y,x,y);
				g.drawLine(x-width,y+width,x,y+width);
				g.drawRect(x,y-5,20,30);
//				g.drawArc(x+10,y-5,20,30,0,90);
				g.setColor(Color.white);
				g.drawRect(x+20,y-5,x+20,y+15);
				g.setColor(Color.blue);
				g.drawArc(x+10,y-5,20,30,260,200);

				g.drawLine(x+30,y+10,x+40,y+10);
				g.drawLine(x+40,y,x+40,y+10);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect(x-4-20,y-4,8,8);
				g.fillRect(x-4-20,y+20-4,8,8);
				g.fillRect(x-4+40,y-4,8,8);
				g.setColor(Color.black);
				g.rotate(-angle , x , y);
			}
			void draw_mux(Graphics2D g , int x , int y , int width , double angle)
			{
				g.rotate(angle , x , y);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.blue);
				g.drawLine(x-width,y,x,y);
				g.drawLine(x-20,y+width,x,y+width);
				g.drawRect(x,y-5,20,30);
				g.drawLine(x+20,y+10,x+40,y+10);
				g.drawLine(x+40,y,x+40,y+10);
				g.drawLine(x+10,y+25,x+10,y+40);
				g.drawLine(x,y+40,x+10,y+40);
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect(x-4-20,y-4,8,8);
				g.fillRect(x-4-20,y+20-4,8,8);
				g.fillRect(x-4+40,y-4,8,8);
				g.fillRect(x-4,y+40-4,8,8);
				g.setColor(Color.black);
				g.rotate(-angle , x , y);
			}
			void draw_input(Graphics2D g , int x , int y , int width , double angle)
			{
				g.rotate(angle , x , y);	
				g.setColor(Color.blue);
				g.drawString("IN", x + 5 , y+ width - 5);
				g.setColor(Color.blue);
				g.setStroke(new BasicStroke(2));
				g.drawLine(x  , y  , x + 2*width ,y );
				g.drawLine(x  , y + width  , x + width ,y + width );
				g.drawLine(x  , y , x , y + width );
				g.drawLine(x + width , y + width , x + (3*width)/2 , y );
				g.setStroke(new BasicStroke(1));
				g.setColor(Color.red);
				g.fillRect( x + 2*width - 4, y  -4, 8 ,8 );
				g.rotate(-angle , x , y);
			}
			void draw_output(Graphics2D g , int x , int y , int width , double angle)
			{

				g.rotate(angle , x , y);
				g.setColor(Color.blue);
				g.drawString("OUT", x  + width / 2, y+ width - 5);
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
		public class graph extends JPanel implements /*ActionListener ,*/ MouseMotionListener , MouseListener
		{
			String fileToRead = "outfile";
			StringBuffer strBuff;
			//			JButton Make;
			TextArea txtArea;
			String myline;
			JLabel l ;
			int x_click,y_click,xpos,ypos;
			int nearby_xpos,nearby_ypos;
			int graph_base_cord_y[]={80,160,240,320,400,120,240,360};            
			int[][] graph = new int[][] { 
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Input 0
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Input 1                                  
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Input 1                                  
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Input 3                                  
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Input 4             
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Output 0                           
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Output 1
					{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Output 2          
		};
			int make_the_graph;
			double[] time = new double[1000] ;
			double[] V_in = new double[1000] ;
			double[] V_out = new double[1000] ;
			int no_values = 0 ;
			public void clear_graph()
			{
				int i,j;
				for(i=0;i<=7;i++)
				{
					for(j=0;j<16;j++)
					{
						graph[i][j]=0;
					}
				}
			}
			public  graph ()
			{
				make_the_graph=-1;
				addMouseMotionListener(this); // whole panel is made to detect 
				addMouseListener(this); // whole panel is made to detect 
			}
			public void mouseReleased (MouseEvent me) {
			}
			public void mouseEntered (MouseEvent me) {
			}
			public void mouseExited (MouseEvent me) {
			}
			public void mouseDragged (MouseEvent me) {
			}
			public void mousePressed (MouseEvent me) {
			}
			public void mouseMoved (MouseEvent me) {
				if(make_the_graph==1)
				{
					xpos = me.getX();
					ypos = me.getY();
					int ygot=0;
					if(xpos>=40 && xpos <=358)
					{
						for(int yi=0;yi<5;yi++)
						{
							if(ypos>=graph_base_cord_y[yi]-60 && ypos<graph_base_cord_y[yi]-20)
							{	
								ypos=graph_base_cord_y[yi]-40;
								ygot=1;
								break;
							}
							else if(ypos>=graph_base_cord_y[yi]-20 && ypos<graph_base_cord_y[yi]+20)
							{
								ypos=graph_base_cord_y[yi];
								ygot=1;
								break;
							}
						}
					}
					if(ygot==0)
						ypos=-100;
					repaint();
				}	
			}
			public void mouseClicked(MouseEvent me)
			{
				if(make_the_graph==1)
				{
					x_click=me.getX();
					y_click=me.getY();

					if(ypos>=0)
					{
						for(int yi=0;yi<5;yi++)
						{
							if(ypos==graph_base_cord_y[yi]-40)
							{               //////FIX ME HERE YI is hardcode to check ==1 or ==2 etc
								graph[yi][(xpos-40)/40]=1;
								break;
							}
							if(ypos==graph_base_cord_y[yi])
							{               //////FIX ME HERE YI is hardcode to check ==1 or ==2 etc
								graph[yi][(xpos-40)/40]=0;
								break;
							}
						}
					}
					repaint();
				}
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
					//	System.out.println("I did't got the outfile to read :( :( So I am very said ");
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
						time[i] = Double.parseDouble(line);
						line = bf.readLine();
						line = bf.readLine();
						V_out[i] = Double.parseDouble(line);
						line = bf.readLine();
						line = bf.readLine();
						V_in[i] = Double.parseDouble(line);
						i++;
					}
					no_values = i ;
				}
				catch(IOException e){
					e.printStackTrace();
				}


			}
			public void paint(Graphics g)
			{
				Graphics2D g2d = (Graphics2D)g ;
				g2d.setStroke(new BasicStroke(2));
				g2d.setColor(new Color(204 , 255 , 255));
				g2d.fillRect(0,0,1000,1500);
				g2d.setColor(Color.lightGray);
				int count=0;
				int i,j;
				for ( i = 0 ; i < 1500 ; i += 20 )
				{
					for (j = 0 ; j < 1500 ; j +=5 )
					{
						g2d.fillOval(i , j , 1 , 2);
					}
				}
				for ( i = 0 ; i < 1500 ; i += 5 )
				{
					for (j = 0 ; j < 1500 ; j +=20 )
					{
						g2d.fillOval(i , j , 2 , 1);
					}
				}
				g2d.setColor(Color.black);
				g2d.setStroke(new BasicStroke(1));
				g2d.drawLine(40 , 40 , 40  , 480 );
				g2d.drawLine( 0  , 440 , 400 , 440 );

				g2d.drawString("Time --> ",  200 , 462 );
				g2d.drawString("Volt",  5 , 240 );
				
				g2d.drawLine(480 , 40 , 480  , 480 );
				g2d.drawLine( 440  , 440 , 840 , 440 );
				
				g2d.drawString("Time --> ",  640 , 462 );
				g2d.drawString("Volt",  445 , 240 );

				g2d.drawString("WAVEFORM OUTPUT SIMULATION  ",  335 , 15 );
				g2d.drawString("OF THE DRAWN CIRCUIT",  355 , 35 );


				g2d.setColor(Color.red);
				//				g2d.setStroke(new BasicStroke(2));
				for ( i = 1 ; i < 9 ; i +=1 )
				{
					g2d.drawString(Integer.toString(i),  40+40*i-4 , 451 );
					g2d.drawString(Integer.toString(i),  480+40*i-4 , 451 );
				}

			//	if(make_the_graph==1)
			//	{
					g2d.setColor(Color.blue);
					g2d.drawString("Input 1",  315 , 100-5 );
					g2d.drawString("Input 2",  315 , 180-5 );
					g2d.drawString("Input 3",  315 , 260-5 );
					g2d.drawString("Input 4",  315 , 340-5 );
					g2d.drawString("Input 5",  315 , 420-5 );
					g2d.setColor(Color.red);
					g2d.setStroke(new BasicStroke(2));
					for(i=0;i<5;i++)
					{
						g2d.drawString("0",  30 , graph_base_cord_y[i]+2 );
						g2d.drawString("1",  30 , graph_base_cord_y[i]-40+5 );
						for(j=0;j<8;j++)
						{
							g2d.drawLine(40+(j*40),graph_base_cord_y[i]-graph[i][j]*40,80+(j*40),graph_base_cord_y[i]-graph[i][j]*40);
							if(j==0&&graph[i][0]==1)
								g2d.drawLine(40+(j*40),graph_base_cord_y[i]-40,40+(j*40),graph_base_cord_y[i]);
							if((j+1<8)&&((graph[i][j]==1&&graph[i][j+1]==0)||(graph[i][j]==0&&graph[i][j+1]==1)))
								g2d.drawLine(80+(j*40),graph_base_cord_y[i]-40,80+(j*40),graph_base_cord_y[i]);
							if(j==7&&graph[i][j]==1)
								g2d.drawLine(80+(j*40),graph_base_cord_y[i]-40,80+(j*40),graph_base_cord_y[i]);
						}
					}

					nearby_xpos=(xpos)%40;
					nearby_ypos=(ypos)%40;

					if(nearby_xpos<40)
						nearby_xpos=xpos-nearby_xpos;
					else
						nearby_xpos=xpos+40-nearby_xpos;

					if(nearby_ypos<20)
						nearby_ypos=ypos-nearby_ypos;
					else
						nearby_ypos=ypos+40-nearby_ypos;

					g2d.setColor(Color.blue);
					g2d.drawLine(nearby_xpos,nearby_ypos,nearby_xpos+40,nearby_ypos);
			//	}
			//	else if(make_the_graph==0)
			//	{
					g2d.setColor(Color.blue);
					g2d.drawString("Output 1",  440+305 , 120-5 );
					g2d.drawString("Output 2",  440+305 , 240-5 );
					g2d.drawString("Output 3",  440+305 , 360-5 );
					g2d.drawString("Delay",  200+45+40+20+60-2 , 480-5-2+15+10 );
					g2d.setColor(Color.red);
					g2d.setStroke(new BasicStroke(2));
					int value=1;
					//open later
					for(j=0;j<8;j++)
					{
						//System.out.println(graph[0][j]+graph[1][j]+graph[2][j]+graph[3][j]+graph[4][j]);

						graph[5][j]=(graph[0][j]+graph[1][j]+graph[2][j]+graph[3][j]+graph[4][j])%2;
						int xor_of_input0123=(graph[0][j]+graph[1][j]+graph[2][j]+graph[3][j])%2;
						int not_xor_of_input0123=(xor_of_input0123+1)%2;
						int xor_of_input01=(graph[0][j]+graph[1][j])%2;
						int not_xor_of_input01=(xor_of_input01+1)%2;

						int p=xor_of_input0123*graph[4][j];
						int q=not_xor_of_input0123*graph[3][j];
						int r=1;
						if((p+q)==0)
							r=0;

						int s=xor_of_input01*graph[2][j];
						int t=not_xor_of_input01*graph[0][j];
						int u=1;
						if((s+t)==0)
							u=0;
						graph[6][j]=(r+u)%2;
						graph[7][j]=(r*u);
					}
					//open later

					for(i=5;i<8;i++)
					{
						g2d.drawString("0",  440+30 , graph_base_cord_y[i]+2 );
						g2d.drawString("1",  440+30 , graph_base_cord_y[i]-40+5 );
						for(j=0;j<8;j++)
						{
							g2d.drawLine(440+40+(j*40),graph_base_cord_y[i]-graph[i][j]*40,440+80+(j*40),graph_base_cord_y[i]-graph[i][j]*40);
							if(j==0&&graph[i][0]==1)
								g2d.drawLine(440+40+(j*40),graph_base_cord_y[i]-40,440+40+(j*40),graph_base_cord_y[i]);
							if((j+1<8)&&((graph[i][j]==1&&graph[i][j+1]==0)||(graph[i][j]==0&&graph[i][j+1]==1)))
								g2d.drawLine(440+80+(j*40),graph_base_cord_y[i]-40,440+80+(j*40),graph_base_cord_y[i]);
							if(j==7&&graph[i][j]==1)
								g2d.drawLine(440+80+(j*40),graph_base_cord_y[i]-40,440+80+(j*40),graph_base_cord_y[i]);
						}
					}
					g2d.drawString(technology+" (picosecs)",200+200,500-2-5+10-2-20+7+10 );

				//	}
				}

			}
			/**==================================================================================================================
			// All Panel Declarations ===========================================================================================
			//==================================================================================================================**/
			JPanel topPanel = new JPanel () ;
			JButton simulate_button ;
			JButton clear_button ;
			JButton example_button ;
			JButton graph_button ;
			//			JButton graph_button1;
			JComboBox exp_list ;
			JButton layout_button ;

			JSplitPane splitPane ; // devides center pane into left and right panel 
			JPanel rightPanel = new JPanel();// = new exp1_graph();
			//	graph waveRightPanel = new graph() ;// = new exp1_graph();
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
			WorkPanel workPanel = new WorkPanel(); // above defines new class WorkPanel
			//==================================================================================================================
			//==================================================================================================================

			public  MyPanel()
			{	
				super(new BorderLayout());
				int i ;
				//--------------------------------------------------------------------------------
				//CREATIE AND SET UP THE CONTENT PAGE .===========================================
				//--------------------------------------------------------------------------------

				try // geting base URL address of this applet 
				{
					base = getDocumentBase();
				}
				catch( Exception e) {}

				//------------------------------------------------------------------------------------
				// Setting Left Pannel Of (Main Center Panel)---------------------------------------------- 
				leftPanel.setLayout(new BorderLayout());
				rightPanel.setLayout(new BorderLayout());
				leftPanel.setMinimumSize(new Dimension(800 , 1000)); // for fixing size
				rightPanel.setMinimumSize(new Dimension(800 , 1000)); // for fixing size

				leftSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , toolPanel , workPanel); // spliting left in tool & work
				leftSplitPane.setOneTouchExpandable(true); // this for one touch option 
				leftSplitPane.setDividerLocation(0.2);  
				leftPanel.add(leftSplitPane, BorderLayout.CENTER);

				// setting work panel -----------------------------------
				//	System.out.println( leftSplitPane.getRightComponent().getSize());

				// setting tool panel --------------------------------------

				for ( i = 1 ; i <= 9 ; i ++ )
				{
					java.net.URL imgURL = getClass().getResource("images/comp" + i + ".gif");
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

				}
				int j = 0 ;
				for ( i = 1 ; i <= 9 ; i ++ )
				{
					j = 9 + i ; // for index setting 
					java.net.URL imgURL = getClass().getResource("images/comp" + j + ".gif");
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

				}
				leftTool1.add(img_button1[1]);
				leftTool1.add(img_button1[2]);
				leftTool1.add(img_button1[7]);
				leftTool2.add(img_button2[3]);
				leftTool2.add(img_button2[6]);
				leftTool2.add(img_button2[8]);


				toolPanel.setLayout(new BorderLayout());


				//			MySelected toolPanelUp = new MySelected();
				toolPanelUp = new JPanel();
				toolPanelDown = new JPanel();

				URL selected_URL = getClass().getResource("images/comp" + 0 + ".gif");
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
				JLabel temp = new JLabel("<html> <FONT color=white size=6 ><b>TOOL BAR<b/><font/><html/>");
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

				//leftPanel.setMaximumSize(new Dimension( 100, 1000)); // for fixing size 

				//------------------------------------------------------------------------------------
				// Setting (((Right Panel))) in center Panel ------------------------------------------------


				rightPanel.setLayout( new BorderLayout() );

				//			JLabel wave_head = new JLabel("This is the wave simulation output");
				JLabel wave_head = new JLabel ( "<html><FONT COLOR=white SIZE=6 ><B>SIMULATION OF CIRCUIT</B></FONT><br><br></html>", JLabel.CENTER);
				wave_head.setBorder(BorderFactory.createRaisedBevelBorder( ));

				rightPanel.setBackground(Color.gray);

				waveRightPanel = new graph() ;// = new exp1_graph();
				rightPanel.add(waveRightPanel, BorderLayout.CENTER);
				rightPanel.add(wave_head, BorderLayout.NORTH);

				// 			waveRightPanel.setVisible(false);



				//rightPanel.addMouseMotionListener(); // whole panel is made to detect 

				//---------------------------------------------------------------------------------
				// Setting Center  Split ============================================================
				splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , null);


				splitPane.setOneTouchExpandable(true); // this for one touch option 


				splitPane.setDividerLocation(0.2);  
				add(splitPane, BorderLayout.CENTER);
				//-------------------------------------------------------------------------------------------
				//Setting Top Panel ========================================================================== 

				add(topPanel , BorderLayout.NORTH);
				topPanel.setBackground(Color.gray);
				JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 100 , 10 )) ;
				//	JPanel headButton1 = new JPanel (new FlowLayout(FlowLayout.CENTER , 100 , 10 )) ;
				JLabel heading = new JLabel (  "<html><FONT COLOR=WHITE SIZE=18 ><B>VLSI EXPERIMENT NO : 5 </B></FONT></html>", JLabel.CENTER);
				heading.setBorder(BorderFactory.createEtchedBorder( Color.black , Color.white));
				//			headButton.setBorder(BorderFactory.createLineBorder( Color.black));
				//			heading.setBorder(BorderFactory.createTitledBorder("."));

				topPanel.setLayout(new BorderLayout());

				topPanel.add(heading , BorderLayout.CENTER);
				topPanel.add(headButton , BorderLayout.SOUTH);
				//	rightPanel.add(headButton1 , BorderLayout.SOUTH);
				java.net.URL imgURL = getClass().getResource("images/simulate1.png");
				java.net.URL imgURL2 = getClass().getResource("images/graph.gif");
				java.net.URL imgURL3 = getClass().getResource("images/graph.gif");
				java.net.URL imgURL4 = getClass().getResource("images/graph.gif");


				//java.net.URL imgURL21 = getClass().getResource("images/graph.gif");

				if (imgURL != null && imgURL2 != null) 
				{
					icon_simulate =  new ImageIcon(imgURL);
					icon_layout =  new ImageIcon(imgURL2);
					icon_graph =  new ImageIcon(imgURL2);
					icon_graph1 =  new ImageIcon(imgURL2);
				}
				else 
				{
					System.err.println("Couldn't find file: " );
					icon_simulate =  null;
					icon_layout =  null;
					icon_graph =  null;
				}
				simulate_button = new JButton(" SIMULATE " , icon_simulate );
				example_button = new JButton(" EXAMPLE " );
				clear_button = new JButton(" CLEAR ");
				icon_simulate.setImageObserver(simulate_button);



				//graph_button1= new JButton (" GENERATE GRAPH " , icon_graph1);
				graph_button = new JButton (" FULL GRAPH " , icon_graph);
				icon_graph.setImageObserver(graph_button);
				//icon_graph1.setImageObserver ( graph_button1 ) ;
				simulate_button.setToolTipText("For Simulation");
				example_button.setToolTipText("For Example");
				clear_button.setToolTipText("For Clearing");

				String[] my_list = {"Complementary CMOS Logic" , "Pseudo NMOS Logic"};
				exp_list = new JComboBox (my_list);
				exp_list.setSelectedIndex(0);

				layout_button = new JButton (" MAKE GRAPH ",icon_layout);
				layout_button.setToolTipText("For making graph");
				icon_layout.setImageObserver(layout_button);
				//				layout_button.setVisible(false);

				String[] techStrings = { "350 nm", "180 nm", "130 nm", "90 nm"};
				JComboBox techList = new JComboBox(techStrings);
				techList.setSelectedIndex(0);
				techList.setToolTipText("For Selecting Technology");

				headButton.add(example_button );
				headButton.add(simulate_button );
				headButton.add(techList );
				headButton.add(clear_button );
				//	headButton.add(graph_button );
				//headButton.add(exp_list );
				//headButton.add(layout_button );

				//	headButton1.add(graph_button1 );


				//	graph_button1.addActionListener(this);
			//	layout.putConstraint(SpringLayout.WEST , techList, 5,   SpringLayout.WEST , cp );
			//	layout.putConstraint(SpringLayout.NORTH , techList , 5,  SpringLayout.NORTH , cp);
				techList.addActionListener(this);

				simulate_button.addActionListener(this);
				graph_button.addActionListener(this);
				example_button.addActionListener(this);
				clear_button.addActionListener(this);
				exp_list.addActionListener(this);
				layout_button.addActionListener(this);
				//-------------------------------------------------------------------------------------------
				//Setting Bottom Panel ========================================================================== 
				JPanel bottom = new JPanel(new FlowLayout());
				add(bottom , BorderLayout.SOUTH);
				JLabel h = new JLabel (  "<html><FONT COLOR=WHITE SIZE=12 ><B>THIS IS VLSI EXPERIMENT</B></FONT></html>", JLabel.CENTER);
				//			bottom.add(h );

				//================================================================================================
				setBorder(BorderFactory.createLineBorder( Color.black));
				//	setBorder(BorderFactory.createTitledBorder("HI"));

			}
			// To change the icon at the selected part ..................



			//This connection array has the values that any index should have for the circuit to be complete.Gor eg in this circuit, index 3 i.e input 0 has to be connected to 8 and 27 for the circuit to be complete
			int[][] connection_array = new int[][] {     
				{29,-1,-1},
					{33,-1,-1},
					{25,-1,-1},
					{8,27,-1},
					{9,-1,-1},
					{11,28,-1},
					{12,31,-1},
					{18,32,-1},
					{3,-1,-1},
					{4,-1,-1},
					{14,26,-1},
					{5,-1,-1},
					{6,-1,-1},
					{15,-1,-1},
					{10,-1,-1},
					{13,-1,-1},
					{17,30,-1},
					{16,-1,-1},
					{7,-1,-1},
					{23,-1,-1},
					{29,-1,-1},
					{33,-1,-1},
					{24,-1,-1},
					{19,-1,-1},
					{22,-1,-1},
					{2,-1,-1},
					{10,-1,-1},
					{3,-1,-1},
					{5,-1,-1},
					{0,20,-1},
					{16,-1,-1},
					{6,-1,-1},
					{7,-1,-1},
					{1,21,-1}
			}; 		
			public boolean simple_check(int index,int check_points_value[][])
			{
				int l ;
				int flag;
				int i;
				int count=0;
				//				print("The indexs connected to 
				l=0;
				//This while loop is checking that all the indices, index is connected to is amongst those allowed by connection_array.
				while (  check_points_value[index][l++] != -1 )
				{
					if(check_points_value[index][l-1]==index)
						continue;
					flag=0;
					for(i=0;connection_array[index][i]!=-1;i++)
					{
						if(check_points_value[index][l-1]==connection_array[index][i])
						{				
							flag=1;
							break;
						}
					}
					if(flag==1)
						continue;
					else
					{
						System.out.println(index + " connected to "+check_points_value[index][l-1] +" which is wrong.");
						return false;
					}
				}
				for(i=0;connection_array[index][i]!=-1;i++)
				{
					l=0;

					while (  check_points_value[index][l++] != -1 )
					{
						if(check_points_value[index][l-1]==index)
							continue;
						else if(check_points_value[index][l-1]==connection_array[index][i])
						{
							count++;
							break;
						}
						}
				}
				if(count==i)
					return true;
				else
				{
					System.out.println(index+" not connected to all");
					return false;
				}

			}
			public boolean circuit_check()
			{
				int check_points_value[][] = new int[34][10] ; // each index will store corresponding  component for points of (comp point no -> index )
				int i = 0 , j , j1 = 0, k1 = 0 , j2 = 0 , k2 = 0 , l = 0 ;
				for (  i = 0 ; i < 34 ; i ++ )
				{
					for (  j = 0 ; j < 10 ; j ++ )
					{
						check_points_value[i][j] = -1 ;
					}
				}
				for (  i = 0 ; i < total_wire ; i ++ )
				{
					j1 = wire[i].x1;
					k1 = wire[i].y1;
					j2 = wire[i].x2;
					k2 = wire[i].y2;
					if(wire[i].del == false &&  end_points_mat[j1][k1] != -1 && end_points_mat[j2][k2] != -1 )
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

				exp_type=0;
				if ( exp_type == 0 ) // Complementary 
				{
					/*for (  i = 0 ; i < 34 ; i ++ )
					{
						System.out.print("The indices connected to "+i+" are :");
						int po=0;
						while( check_points_value[i][po] != -1 )
							System.out.print(check_points_value[i][po++]+",");
						System.out.println(".");
					}*/

					for (  i = 0 ; i < 34 ; i ++ )
					{
						if ( check_points_value[i][0] == -1 )
						{
							System.out.println(i+" not connected to any thing");
							return false ; // if any end point of component is free then wrong circiut ...
						}
						if(simple_check(i,check_points_value)==false)
						{
							System.out.println(i+" wrongly connected");
							return false;
						}
					}
					return true ;
				}
				return true ;
			}
			public void change_selected (int no)
			{
				selected.setIcon(icon[no]);
			}
			public void clear_function()
			{
				int n;
				for(n=0;n<total_comp;n++)
				{
					comp_node[n].del = true;
					comp_node[n].remove_mat();
				}
				total_comp=0;
				INPUT[0]=0;INPUT[1]=0;INPUT[2]=0;INPUT[3]=0;INPUT[4]=0;
				XOR[0]=0;XOR[1]=0;XOR[2]=0;XOR[3]=0;XOR[4]=0;
				OUTPUT[0]=0;OUTPUT[1]=0;OUTPUT[2]=0;
				MUX[0]=0;MUX[1]=0;
				comp_count[1]=0;
				int i,j;
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
				waveRightPanel.clear_graph();
				total_wire=0;
				comp_str[0] = "This is shows which component is selected .";
				comp_str[1] = "AND";
				comp_str[2] = "INPUT 1";
				comp_str[3] = "INPUT 2";
				comp_str[4] = "INPUT 3";
				comp_str[5] = "INPUT 4";
				comp_str[6] = "INPUT 5";
				comp_str[7] = "XOR 1";
				comp_str[8] = "XOR 2";
				comp_str[9] = "XOR 3";
				comp_str[10] = "XOR 4";
				comp_str[11] = "XOR 5";
				comp_str[12] = "OUTPUT 1";
				comp_str[13] = "OUTPUT 2";
				comp_str[14] = "OUTPUT 3";
				comp_str[15] = "MUX 1";
				comp_str[16] = "MUX 2";
				comp_str[17] = "WIRE";
				comp_str[18] = "This is CMOS Chip No:1";
				repaint();
			}
			public void actionPerformed(ActionEvent e )
			{
				if ( e.getSource() == exp_list )
				{
					JComboBox cb = (JComboBox)e.getSource();
					if ( cb.getSelectedIndex() == 1 )
					{
						exp_type = 1 ;
					}
					else 
					{
						exp_type = 0 ;
					}
				}
				else if ( e.getSource() == example_button )
				{      
					clear_function();
					//Delete prev ckt
					//System.out.println("GOING IN");
					total_comp=0;
					comp_node[total_comp++] = new node(140,380,2,40,20);
					comp_node[total_comp++] = new node(140,320,3,40,20);
					comp_node[total_comp++] = new node(140,260,4,40,20);
					comp_node[total_comp++] = new node(140,200,5,40,20);
					comp_node[total_comp++] = new node(140,140,6,40,20);
					comp_node[total_comp++] = new node(340,340,7,60,20);
					comp_node[total_comp++] = new node(340,220,8,60,20);
					comp_node[total_comp++] = new node(520,220,9,60,20);
					comp_node[total_comp++] = new node(520,340,15,60,20);
					comp_node[total_comp++] = new node(680,220,10,60,20);
					comp_node[total_comp++] = new node(680,340,11,60,20);
					comp_node[total_comp++] = new node(680,100,16,60,20);
					comp_node[total_comp++] = new node(680,440,1,60,20);
					comp_node[total_comp++] = new node(820,220,12,40,20);
					comp_node[total_comp++] = new node(820,340,13,40,20);
					comp_node[total_comp++] = new node(820,440,14,40,20);
					int n;
					for(n=0;n<total_comp;n++)
					{
						comp_node[n].update_mat(n);
					}
					INPUT[0]=1;INPUT[1]=1;INPUT[2]=1;INPUT[3]=1;INPUT[4]=1;
					XOR[0]=1;XOR[1]=1;XOR[2]=1;XOR[3]=1;XOR[4]=1;
					OUTPUT[0]=1;OUTPUT[1]=1;OUTPUT[2]=1;
					MUX[0]=1;MUX[1]=1;
					comp_count[1]=1;
					wire[total_wire++] = new line(720,220,820,220);
					wire[total_wire-1].x[0]=720;
					wire[total_wire-1].y[0]=220;
					wire[total_wire-1].x[1]=820;
					wire[total_wire-1].y[1]=220;
					wire[total_wire-1].end_index=1;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(720,340,820,340);
					wire[total_wire-1].x[0]=720;
					wire[total_wire-1].y[0]=340;
					wire[total_wire-1].x[1]=820;
					wire[total_wire-1].y[1]=340;
					wire[total_wire-1].end_index=1;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(720,440,820,440);
					wire[total_wire-1].x[0]=720;
					wire[total_wire-1].y[0]=440;
					wire[total_wire-1].x[1]=820;
					wire[total_wire-1].y[1]=440;
					wire[total_wire-1].end_index=1;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,140,660,100);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=140;
					wire[total_wire-1].x[1]=180;
					wire[total_wire-1].y[1]=100;
					wire[total_wire-1].x[2]=660;
					wire[total_wire-1].y[2]=100;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,140,660,220);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=140;
					wire[total_wire-1].x[1]=180;
					wire[total_wire-1].y[1]=100;
					wire[total_wire-1].x[2]=620;
					wire[total_wire-1].y[2]=220;
					wire[total_wire-1].x[3]=660;
					wire[total_wire-1].y[3]=220;
					wire[total_wire-1].end_index=3;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,200,660,120);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=200;
					wire[total_wire-1].x[1]=240;
					wire[total_wire-1].y[1]=200;
					wire[total_wire-1].x[2]=240;
					wire[total_wire-1].y[2]=120;
					wire[total_wire-1].x[3]=660;
					wire[total_wire-1].y[3]=120;
					wire[total_wire-1].end_index=3;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,200,320,220);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=200;
					wire[total_wire-1].x[1]=320;
					wire[total_wire-1].y[1]=200;
					wire[total_wire-1].x[2]=320;
					wire[total_wire-1].y[2]=220;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,260,320,240);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=260;
					wire[total_wire-1].x[1]=320;
					wire[total_wire-1].y[1]=260;
					wire[total_wire-1].x[2]=320;
					wire[total_wire-1].y[2]=240;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,260,500,340);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=260;
					wire[total_wire-1].x[1]=240;
					wire[total_wire-1].y[1]=260;
					wire[total_wire-1].x[2]=240;
					wire[total_wire-1].y[2]=280;
					wire[total_wire-1].x[3]=440;
					wire[total_wire-1].y[3]=280;
					wire[total_wire-1].x[4]=440;
					wire[total_wire-1].y[4]=340;
					wire[total_wire-1].x[5]=500;
					wire[total_wire-1].y[5]=340;
					wire[total_wire-1].end_index=5;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,320,320,340);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=320;
					wire[total_wire-1].x[1]=320;
					wire[total_wire-1].y[1]=320;
					wire[total_wire-1].x[2]=320;
					wire[total_wire-1].y[2]=340;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,380,320,360);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=380;
					wire[total_wire-1].x[1]=320;
					wire[total_wire-1].y[1]=380;
					wire[total_wire-1].x[2]=320;
					wire[total_wire-1].y[2]=360;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(180,380,500,360);
					wire[total_wire-1].x[0]=180;
					wire[total_wire-1].y[0]=380;
					wire[total_wire-1].x[1]=240;
					wire[total_wire-1].y[1]=380;
					wire[total_wire-1].x[2]=240;
					wire[total_wire-1].y[2]=400;
					wire[total_wire-1].x[3]=440;
					wire[total_wire-1].y[3]=400;
					wire[total_wire-1].x[4]=440;
					wire[total_wire-1].y[4]=360;
					wire[total_wire-1].x[5]=500;
					wire[total_wire-1].y[5]=360;
					wire[total_wire-1].end_index=5;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(380,340,500,240);
					wire[total_wire-1].x[0]=380;
					wire[total_wire-1].y[0]=340;
					wire[total_wire-1].x[1]=420;
					wire[total_wire-1].y[1]=340;
					wire[total_wire-1].x[2]=420;
					wire[total_wire-1].y[2]=240;
					wire[total_wire-1].x[3]=500;
					wire[total_wire-1].y[3]=240;
					wire[total_wire-1].end_index=3;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(380,340,520,380);
					wire[total_wire-1].x[0]=380;
					wire[total_wire-1].y[0]=340;
					wire[total_wire-1].x[1]=380;
					wire[total_wire-1].y[1]=440;
					wire[total_wire-1].x[2]=520;
					wire[total_wire-1].y[2]=440;
					wire[total_wire-1].x[3]=520;
					wire[total_wire-1].y[3]=380;
					wire[total_wire-1].end_index=3;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(380,220,500,220);
					wire[total_wire-1].x[0]=380;
					wire[total_wire-1].y[0]=220;
					wire[total_wire-1].x[1]=500;
					wire[total_wire-1].y[1]=220;
					wire[total_wire-1].end_index=1;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(560,220,680,140);
					wire[total_wire-1].x[0]=560;
					wire[total_wire-1].y[0]=220;
					wire[total_wire-1].x[1]=560;
					wire[total_wire-1].y[1]=160;
					wire[total_wire-1].x[2]=680;
					wire[total_wire-1].y[2]=160;
					wire[total_wire-1].x[3]=680;
					wire[total_wire-1].y[3]=140;
					wire[total_wire-1].end_index=3;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(560,220,660,240);
					wire[total_wire-1].x[0]=560;
					wire[total_wire-1].y[0]=220;
					wire[total_wire-1].x[1]=560;
					wire[total_wire-1].y[1]=240;
					wire[total_wire-1].x[2]=660;
					wire[total_wire-1].y[2]=240;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(560,340,660,460);
					wire[total_wire-1].x[0]=560;
					wire[total_wire-1].y[0]=340;
					wire[total_wire-1].x[1]=560;
					wire[total_wire-1].y[1]=460;
					wire[total_wire-1].x[2]=660;
					wire[total_wire-1].y[2]=460;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(560,340,660,360);
					wire[total_wire-1].x[0]=560;
					wire[total_wire-1].y[0]=340;
					wire[total_wire-1].x[1]=560;
					wire[total_wire-1].y[1]=360;
					wire[total_wire-1].x[2]=660;
					wire[total_wire-1].y[2]=360;
					wire[total_wire-1].end_index=2;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(720,100,660,340);
					wire[total_wire-1].x[0]=720;
					wire[total_wire-1].y[0]=100;
					wire[total_wire-1].x[1]=760;
					wire[total_wire-1].y[1]=280;
					wire[total_wire-1].x[2]=620;
					wire[total_wire-1].y[2]=280;
					wire[total_wire-1].x[3]=620;
					wire[total_wire-1].y[3]=340;
					wire[total_wire-1].x[4]=660;
					wire[total_wire-1].y[4]=340;
					wire[total_wire-1].end_index=4;
					wire[total_wire-1].update_mat(total_wire - 1);  
					wire[total_wire++] = new line(720,100,660,440);
					wire[total_wire-1].x[0]=720;
					wire[total_wire-1].y[0]=100;
					wire[total_wire-1].x[1]=760;
					wire[total_wire-1].y[1]=280;
					wire[total_wire-1].x[2]=620;
					wire[total_wire-1].y[2]=280;
					wire[total_wire-1].x[3]=620;
					wire[total_wire-1].y[3]=440;
					wire[total_wire-1].x[4]=660;
					wire[total_wire-1].y[4]=440;
					wire[total_wire-1].end_index=4;
					wire[total_wire-1].update_mat(total_wire - 1);  
					img_button_pressed = -1 ;
					change_selected(0);

					repaint();
					//				total_comp=0;
				}
				else if ( e.getSource() == clear_button )
				{
					clear_function();
				//	myDialog dialog = new myDialog( fr[3] ,"" , 5000);
				//	dialog.setVisible(true);
				}
				else if(e.getSource() == simulate_button )
				{
					System.out.println("simulate_button");
					if ( /*Pmos_l != null && Pmos_w != null && Nmos_l != null && Nmos_w != null && Capacitance != null && */circuit_check() /*1==1*/)
					{
						//		callJS();
					/*	URL php_file =null ;
						URLConnection c =null;
						String encoded = "comp=" + URLEncoder.encode(Pmos_l+"_"+Pmos_w+"_"+Nmos_l+"_"+Nmos_w+"_"+Capacitance);
						URL CGIurl = null;
						try
						{
							if ( exp_type == 0 )
							{
								php_file = new URL(getDocumentBase(),"exp1_complementary_simulate.php");
								//System.out.println( php_file.toString());

							}
							else if ( exp_type == 1 )
							{
								php_file = new URL(getDocumentBase(),"exp1_pseudo_simulate.php");
								//System.out.println( php_file.toString());
							}

						}
						catch(Exception mye )
						{
						}*/
						//				String theCGI = "http://localhost/VirtualLab/VLSI_VLab/exp1_out.php";

						/*try
						{
							c = php_file.openConnection();
							c.setDoOutput(true);
							c.setUseCaches(false);
							c.setRequestProperty("content-type","application/x-www-form-urlencoded");
							DataOutputStream out = new DataOutputStream(c.getOutputStream());
							out.writeBytes(encoded);
							out.flush(); out.close();

							BufferedReader in =
								new BufferedReader(new InputStreamReader(c.getInputStream()));

						/*	String aLine;
							while ((aLine = in.readLine()) != null) 
							{
								// data from the CGI
								System.out.println(aLine);
							}*/

					/*	}	
						catch(Exception php_e )
						{
							//System.out.println("Can't make connection");
						}*/
						//				waveRightPanel.make_graph() ;// Read file OUTFILE and draw the 
						waveRightPanel.make_the_graph=1;
						waveRightPanel.repaint();
						waveRightPanel.setVisible(true);
						rightPanel.setVisible(true);
						myDialog dialog = new myDialog( fr[2] ,"" , 1000);
						dialog.setVisible(true);
						simulate_flag = true ;
					}
					else
					{
						waveRightPanel.make_the_graph=-1;
						waveRightPanel.repaint();
						JOptionPane.showMessageDialog(null, "Circuit is not Complete , Please Complete it and press simulate again :)");
					}
				}

				else if ( e.getSource() == layout_button )
				{
					System.out.println("layout_button");
					if(waveRightPanel.make_the_graph!=-1)
					{
						waveRightPanel.make_graph() ;// Read file OUTFILE and draw the
						waveRightPanel.make_the_graph=0;
						waveRightPanel.repaint();
						myDialog dialog = new myDialog( fr[2] ,"" , 1000);
						dialog.setVisible(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Circuit is not Simulated , Please Simulate it and press MAKE GRAPH again :)");
					}

					//				if ( simulate_flag == true )
					//		{
					/*		try
							{
					//URL url = new URL("http://localhost/VirtualLab/VLSI_VLab/exp1_out.php?name=Shahsank");
					URL url = new URL(base ,"exp1_fullgraph.php?name=Shahsank");
					getAppletContext().showDocument(url , "Layout Design ");
					}
					catch(Exception ee )
					{}*/
					//		}
					//	else
					//	{
					//		JOptionPane.showMessageDialog(null, "Circuit is not simulated , Please Simulate it and press Full Graph Button again");
					//	}

				}
				else if(e.getSource() == graph_button )
				{
					System.out.println("graph_button");
					if ( simulate_flag == true )
					{
						try
						{
							//URL url = new URL("http://localhost/VirtualLab/VLSI_VLab/exp1_out.php?name=Shahsank");
							URL url = new URL(base ,"exp1_fullgraph.php?name=Shahsank");
							getAppletContext().showDocument(url , "Output Exp 1 ");
						}
						catch(Exception ee )
						{}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Circuit is not simulated , Please Simulate it and press Full Graph Button again");
					}

				}
				else if (e.getSource() == img_button1[1] )    //AND
				{
					if ( comp_count[1] == 0 )
					{
						img_button_pressed = 1 ;	
						change_selected(1);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You already have this component !! :)");
					}
					//	System.out.println("img_button1[1] clicked");
				}
				else if (e.getSource() == img_button1[2] )  //INPUT
				{
					if ( INPUT[0] == 0 )
					{
						INPUT[0]=1;
						img_button_pressed = 2 ;
						change_selected(2);
					}
					else if ( INPUT[1] == 0 )
					{
						INPUT[1]=1;
						img_button_pressed = 3 ;
						change_selected(2);
					}	
					else if ( INPUT[2] == 0 )
					{
						INPUT[2]=1;
						img_button_pressed = 4 ;
						change_selected(2);
					}
					else if ( INPUT[3] == 0 )
					{
						INPUT[3]=1;
						img_button_pressed = 5 ;
						change_selected(2);
					}
					else if ( INPUT[4] == 0 )
					{
						INPUT[4]=1;
						img_button_pressed = 6 ;
						change_selected(2);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You already have this component !! :)");
					}

				}
				else if (e.getSource() == img_button1[7] )  //XOR
				{
					if ( XOR[0] == 0 )
					{
						XOR[0]=1;
						img_button_pressed = 7 ;
						change_selected(7);
					}
					else if ( XOR[1] == 0 )
					{
						XOR[1]=1;
						img_button_pressed = 8 ;
						change_selected(7);
					}	
					else if ( XOR[2] == 0 )
					{
						XOR[2]=1;
						img_button_pressed = 9 ;
						change_selected(7);
					}
					else if ( XOR[3] == 0 )
					{
						XOR[3]=1;
						img_button_pressed = 10;
						change_selected(7);
					}
					else if ( XOR[4] == 0 )
					{
						XOR[4]=1;
						img_button_pressed = 11;
						change_selected(7);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You already have this component !! :)");
					}

				}
				else if (e.getSource() == img_button2[3] )  //OUTPUT
				{
					if ( OUTPUT[0] == 0 )
					{
						OUTPUT[0]=1;
						img_button_pressed = 12 ;
						change_selected(12);
					}
					else if (OUTPUT[1] == 0 )
					{
						OUTPUT[1]=1;
						img_button_pressed = 13 ;
						change_selected(12);
					}	
					else if (OUTPUT[2] == 0 )
					{
						OUTPUT[2]=1;
						img_button_pressed = 14 ;
						change_selected(12);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You already have this component !! :)");
					}

				}
				else if (e.getSource() == img_button2[6] )  //INPUT
				{
					if ( MUX[0] == 0 )
					{
						MUX[0]=1;
						img_button_pressed = 15 ;
						change_selected(15);
					}
					else if ( MUX[1] == 0 )
					{
						MUX[1]=1;
						img_button_pressed = 16 ;
						change_selected(15);
					}	
					else
					{
						JOptionPane.showMessageDialog(null, "You already have this component !! :)");
					}

				}
				else if (e.getSource() == img_button2[8] ) // Wire :)
				{
					img_button_pressed = 17 ;	
					change_selected(17);
				}
				else// if((JComboBox)e.getSource() == techList)
				{
					JComboBox cb = (JComboBox)e.getSource();
					String tech=(String)cb.getSelectedItem();
					if(tech.equals("350 nm"))
						technology=80;
					else if(tech.equals("180 nm"))
						technology=45;
					else if(tech.equals("130 nm"))
						technology=35;
					else if(tech.equals("90 nm"))
						technology=27;
					//System.out.println(technology);
				}


			}
			public void mouseMoved(MouseEvent me) 
			{ 
				//System.out.println("In Right Panel ");

			} 
			public void mouseDragged(MouseEvent e) {}

		}// MyPanel class extends JPanel Class Ends here
		void callJS()
		{

		}
	}


