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
import java.awt.geom.AffineTransform;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.ImageIcon.*;
public class exp_2 extends JApplet
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
	private  void createAndShowGUI() {

		MyPanel myPane = new MyPanel();
		myPane.setOpaque(true);
		setContentPane(myPane);
	}
	public class MyPanel  extends JPanel  implements ActionListener//MouseListener,MouseMotionListener
	{
		int exp_type = 0 ; // 0 -> Complementary Inverter , 1 -> Pseudo Inverter 
		/** Work Panel Variables ************************************************************************************************/
		double scale_x = 1 ;  // scaling of work pannel 
		double scale_y = 1 ;
		double trans_x = 0 ;  // tanslating of work pannel 
		double trans_y = 0 ;
		int trans_graph_y = 0,trans_graph_x = 0;
		int	prev_xpos = -1;
		int	prev_ypos = -1;
		
		int pmos_delete_count=0;
		int [] pmos_delete=new int[200];
		int nmos_delete_count=0;
		int [] nmos_delete=new int[200];
		int gnd_delete_count=0;
		int [] gnd_delete=new int[200];
		int vdd_delete_count=0;
		int [] vdd_delete=new int[200];
		int make_circuit=0;		
		int [][]relation=new int[6][6];
		int pmos_count=0;	
		int nmos_count=0;	
		int gnd_count=0;	
		int vdd_count=0;	
		int scroll_value=30;
		int work_x ;
		String circuit;
		int work_y ;
		int circuit_size;
		int enlarge=5;
		int wire_button  = 0 ;// 0 not presed already , 1 -> already pressed 
		int img_button_pressed = -1 ;
		int draw_work = 0 ; // if 1 -> draw the image on work 

		int[][] work_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] end_points_mat ;   // 
		int[][] wire_mat ;   // if -1 => no comp is there on mat .. if i the (i)th comp of node_comp is present
		int[][] wire_points_mat ;   // 

		int work_img_width = 50;
		int work_img_height = 50;
		int work_panel_width = 5000 ;
		int work_panel_height = 5000;
		int start_width_work_panel=0;
		int start_height_work_panel=0;
		int node_drag = -1 ; // it rep the index of comp_node which is selected to be draged 
		int wire_drag = -1 ; // it rep the index of wire which is selected to be extented from its end 
		int wire_drag_end = 1 ; // from which end it should be draged 
		int[] comp_count = new int[20] ; //  comp_count[i] represents the count of ( comp"i".jpg ) component ..
		int total_comp = 0 ;
		//              node[] comp_node = new node[20];
		node[] comp_node = new node[500];

		int total_wire = 0 ;
		              line[] wire = new line[1000];

		// Dialog Box -----------------------------------
		//              myDialog[] dialog = new myDialog[14]  ; //in this exp at max 6 comp can be used  (I assume that comp is used once )
		//              JFrame[] fr = new JFrame[14] ;
		String[] comp_str = {           // This will store what should at the Dialog Box for each component
			"This is shows which component is selected ." ,
			"PMOS ", "Ground Terminal " ," Wire ",  // 1, 2 , 3
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
		JButton enlarge_button ;
		JButton makeCircuit_button ;
		JButton minimize_button ;
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
		

		 public class graph extends JPanel   implements MouseMotionListener,MouseListener
                {
                        String fileToRead = "outfile";

                        StringBuffer strBuff;
                        TextArea txtArea;
                        String myline;
                        JLabel l ;

			 int xpos,ypos;
                        int[][] A = new int[12][64] ;
                        int[][] B = new int[12][64] ;
                        int[] C_in = new int[64] ;
                        int[][] C_out = new int[12][64] ;
                        int[][] S = new int[12][64] ;
                       int numberofBits;
			
                        int no_values = 0 ;
                        //public  graph()
                        public  graph ()
                        {
//			circuit="4";
			int circuit_int;
			circuit_int=Integer.parseInt(circuit);
			numberofBits=circuit_int;
			int xpos,ypos;
			System.out.println("int size is"+circuit_int);
				 int i,j;
                                //System.out.println("xpos ");
                                addMouseMotionListener(this);
                                addMouseListener(this);
                                for(i=0;i<63;i++)
                                {
                                         if(i%4==0 || i % 4==1)
					 {
                                                
						for(j=0;j<circuit_int;j++)
						{
							A[j][i]=1;
						}
					}

                                        else
					{
                                                
						for(j=0;j<circuit_int;j++)
						{
							A[j][i]=0;
						}
					}
                                        if(i%8<4)
					{
                                                
						for(j=0;j<circuit_int;j++)
						{
							B[j][i]=1;
						}
					}
                                        else
					{
                                                
						for(j=0;j<circuit_int;j++)
						{
							B[j][i]=0;
						}
					}
					if(i%16<8)
                                                C_in[i]=1;
                                        else
                                                C_in[i]=0;
                                }

                        }
			public void make_grap()
			{
			}
			   public void mouseMoved(MouseEvent me)
                        {
                                xpos = me.getX();
                                ypos = me.getY();
                        }
                        public void mouseReleased (MouseEvent me) {
			prev_xpos = -1;
				 prev_ypos = -1;
                        }
                        public void mouseEntered (MouseEvent me) {
                        }
                        public void mouseExited (MouseEvent me) {
                        }
                        public void mouseDragged (MouseEvent me) {
				 xpos = me.getX();
                                ypos = me.getY();
				if(prev_xpos != -1 && prev_ypos != -1)
				{
					trans_graph_x =  xpos - prev_xpos + trans_graph_x;
					trans_graph_y =  ypos - prev_ypos + trans_graph_y;
				}
				prev_xpos = xpos;
				prev_ypos = ypos;
				repaint();
                        }
                        public void mousePressed (MouseEvent me) {
                        }
			    public void mouseClicked(MouseEvent me)
                        {
				int k,ycord,j;
                                xpos = me.getX();
                                ypos = me.getY();
			        ypos = ypos - trans_graph_y;
				xpos = xpos - trans_graph_x;
				if(ypos>115 && ypos<140)
				{
					int u;
					int t;
					if(xpos-120<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-120)/80;
					t=8*(u+1);
					}
						for(k=t;k<t+8;k++)
							C_in[k]=0;
				}
				if(ypos<115 && ypos>90)
				{
					int u;
					int t;
					if(xpos-120<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-120)/80;
					t=8*(u+1);
					}
						for(k=t;k<t+8;k++)
							C_in[k]=1;
				}
				for(j = 0 ; j < numberofBits ; j++)
				{	
					ycord = 160+(j*70)+50; 
				if(ypos > (ycord - 50) && ypos < (ycord - 25))
				{
					int u;
					int t;
					if(xpos-80<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-80)/40;
					t=4*(u+1);
					}
						for(k=t;k<t+4;k++)
							B[j][k]=1;
					
				}
				if(ypos < ycord && ypos > (ycord - 25))
				{
					int u;
					int t;
					if(xpos-80<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-80)/40;
					t=4*(u+1);
					}
						for(k=t;k<t+4;k++)
							B[j][k]=0;
				}
				}
				for(j = 0 ; j < numberofBits ; j++)	
				{
					ycord = 160+((numberofBits)*70)+(j*70)+50;
				if(ypos < ycord && ypos > (ycord-25))
				{
					int u;
					int t;
					if(xpos-60<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-60)/20;
					t=2*(u+1);
					}
						for(k=t;k<t+2;k++)
							A[j][k]=0;
				}
				if(ypos < (ycord - 25) && ypos > (ycord - 50))
				{
					int u;
					int t;
					if(xpos-60<=0)
					{
					t=0;
					}
					else
					{
					u=(xpos-60)/20;
					t=2*(u+1);
					}
						for(k=t;k<t+2;k++)
							A[j][k]=1;
				}
				}

				repaint();
                         
                        }

			 public void make_graph()
                        {
                                /*fileToRead ="outfile";
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
                         		            no_values = i ;*/

                                        repaint();

                                        //              System.out.println("Hi I am in the contrct func of the exp1_graph class :)");
                               // }
                                 //       catch(IOException e){
                                   //             e.printStackTrace();
                                     //   }


                        }
				  int MySum(int a,int b,int c)
				  {
					int res;
					res = a+b+c;
					if((res == 0) || (res == 2))
						return 0;
					else
						return 1;
				  }
				   int MyCarry(int a,int b,int c)
				  {
					int res;
					res = a+b+c;
					if((res == 0) || (res == 1))
						return 0;
					else
						return 1;
				  }
				   public void paint(Graphics g)
                                {
int i , j,ycord;
                                        Graphics2D g2d = (Graphics2D)g ;
                                        g2d.setStroke(new BasicStroke(2));
                                        // back ground 
                                        //g2d.setColor(new Color(204 , 255 , 255));
                                        g2d.setColor(Color.lightGray);
                                        g2d.fillRect(0,0,2000,2500);
                                        g2d.setColor(Color.white);
 				 	AffineTransform tx = new AffineTransform();
	 				AffineTransform oldtx = new AffineTransform();
					oldtx = g2d.getTransform();
 					tx.translate(trans_graph_x,trans_graph_y);
				 g2d.setTransform(tx);
					
                                        for ( i = 0 ; i < 3000 ; i += 15 )
                                        {
                                                for (j = 0 ; j < 3000 ; j +=5 )
                                                {
                                                        g2d.fillOval(i , j , 1 , 2);
                                                }
                                        }
                                        for ( i = 0 ; i < 3000 ; i += 5 )
                                        {
                                                for (j = 0 ; j < 3000 ; j +=15 )
                                                {
                                                        g2d.fillOval(i , j , 2 , 1);
                                                }
                                        }
					for(i=0;i<64;i++)
					{
						for(j = 0 ; j < numberofBits ; j++)	
						{
							if(j == 0)
							{
								S[j][i] = MySum(A[j][i] , B[j][i] , C_in[i]);
								C_out[j][i] = MyCarry(A[j][i] , B[j][i] , C_in[i]);
							}
							else
							{
								S[j][i] = MySum(A[j][i] , B[j][i] , C_out[j-1][i]);
								C_out[j][i] = MyCarry(A[j][i] , B[j][i] , C_out[j-1][i]);
							}
						}
						
					}




					         g2d.setColor(Color.red);
					int b;
					int a;
                                        g2d.drawString("CIn ",  5 , 90);
					ycord = 90;
                                        for( i = 0 ; i < 63 ; i++ )
                                        {
						b=(int)Math.round(C_in[i]*50);
						a=(int)Math.round(C_in[i+1]*50);
						if(b- a > 40)
						{
                                                g2d.drawLine(40+10*i , 140-(int)Math.round(C_in[i]*50) , 40 + 10*(i+1) , 140-b );
                                                g2d.drawLine(40+10*(i+1) , 140-b , 40 + 10*(i+1) , 140-a );
						}
						else if(a-b>40)
						{
                                                g2d.drawLine(40+10*i , 140-b , 40 + 10*(i+1) , 140-b );
                                                g2d.drawLine(40+10*(i+1) , 140-b , 40 + 10*(i+1) , 140-a );
						}
						else	
                                                g2d.drawLine(40+10*i , 140-(int)Math.round(C_in[i]*50) , 40 + 10*(i+1) , 140-(int)Math.round(C_in[i+1]*50) );
                                        }
					for(j = 0 ; j < numberofBits ; j ++)
					{
                                        g2d.drawString("B"+j,  5 , (160+j*70) );
					ycord = 160+(j*70)+50; 
                                        for( i = 0 ; i < 63 ; i++ )
                                        {
						if(i<4)
							System.out.println(B[j][i]);
						b=(int)Math.round(B[j][i]*50);
						a=(int)Math.round(B[j][i+1]*50);
						if(b-a > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						 else if(a-b > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						else
                                                g2d.drawLine(40+10*i , ycord-(int)Math.round(B[j][i]*50) , 40 + 10*(i+1) , ycord-(int)Math.round(B[j][i+1]*50) );
                                        }
					}
					for(j = 0 ; j < numberofBits ; j++)
					{
                                        g2d.drawString("A"+j,  5 , ((160+numberofBits*70) + j*70) );
					ycord = 160 + ((numberofBits) * 70) + (j* 70)+ 50;
                                       for( i = 0 ; i < 63 ; i++ )
                                        {
						b=(int)Math.round(A[j][i]*50);
						a=(int)Math.round(A[j][i+1]*50);
						if(b-a > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}

						 else if(a-b > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						else
                                                g2d.drawLine(40+10*i , ycord -(int)Math.round(A[j][i]*50) , 40 + 10*(i+1) , ycord-(int)Math.round(A[j][i+1]*50) );
                                        }
					}
						
                                        g2d.setColor(Color.blue);
                                        g2d.drawString("Cout",  5 , ((160+(2*numberofBits*70))) );
					ycord = 160 + ((2*numberofBits) * 70) + 50;
                                        for( i = 0 ; i < 63 ; i++ )
                                        {

						b=(int)Math.round(C_out[3][i]*50);
						a=(int)Math.round(C_out[3][i+1]*50);
						if(b-a > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						 else if(a-b > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						else
                                                g2d.drawLine(40+10*i , ycord-(int)Math.round(C_out[3][i]*50) , 40 + 10*(i+1) , ycord-(int)Math.round(C_out[3][i+1]*50) );
                                        }
					for(j = 0 ; j < numberofBits ; j++)
					{
					ycord = 160 + ((2*numberofBits) * 70) + (70*j) + 70 + 50;
                                        g2d.drawString("Sum"+j,  3 , ((160+(2*numberofBits*70)) + 70+ (70*j)) );
   					for( i = 0 ; i < 63 ; i++ )
                                        {
				//		ycord = 790 +(j*70)+50;
						b=(int)Math.round(S[j][i]*50);
						a=(int)Math.round(S[j][i+1]*50);
						if(b-a > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						 else if(a-b > 40)
						{
                                                g2d.drawLine(40+10*i , ycord-b , 40 + 10*(i+1) , ycord-b );
                                                g2d.drawLine(40+10*(i+1) , ycord-b , 40 + 10*(i+1) , ycord-a );
						}
						else
                                                g2d.drawLine(40+10*i , ycord-(int)Math.round(S[j][i]*50) , 40 + 10*(i+1) , ycord-(int)Math.round(S[j][i+1]*50) );
                                        }
					}

					

                                        g2d.setColor(Color.black);
                                        g2d.setStroke(new BasicStroke(1));
                                        g2d.drawLine(40 , 20 , 40  , ycord+20 );
                                        g2d.drawLine( 0  , ycord+20 , 650 , ycord+20  );

                                        g2d.drawString("Time --> ",  160 , ycord+ 40 );
                                  //      g2d.drawString("Volt",  10 , 160 );
                                    //    g2d.drawString("Volt",  10 , 360 );

                                        g2d.drawString("WAVEFORM OUTPUT SIMULATION  ",  80 , 20 );
                                        g2d.drawString("OF ONE FULL ADDER - ",  100 , 40 );

				g2d.setTransform(oldtx);

                                 
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
                                  update_mat(-1);
                                  del = true ;
                          }
                  }
		class MyAdjustmentListener implements AdjustmentListener {
    			public void adjustmentValueChanged(AdjustmentEvent e) {
			int w=start_height_work_panel;
		//	start_height_work_panel=w+100;
			//System.out.println(e.getValue());
			int u;
			if(scroll_value < e.getValue())
			{
				u=-10;
			}
			else
			{
				u=10;
			}
			scroll_value=e.getValue();
			int j;
			for(j=0;j<total_comp;j++)
			{
			comp_node[j].node_y+=u;
			}
			for(j=0;j<total_wire;j++)
			{
			wire[j].y1 +=u;
			wire[j].y2 +=u;
			}
	        	workPanel.repaint();
		    	}
		 }
		public MyPanel()
		{
			super(new BorderLayout());
			setLayout(new BorderLayout());
			//	JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL);
			//	JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL,30,40,0,300);
			//	vbar.setUnitIncrement(2);
			//	vbar.setBlockIncrement(1);
			//	 add(hbar, BorderLayout.WEST);
			//	 add(vbar, BorderLayout.EAST);
			//	 vbar.addAdjustmentListener(new MyAdjustmentListener());
			try // geting base URL address of this applet 
			{
				base = getDocumentBase();
			}
			catch( Exception e) {}

			leftPanel.setLayout(new BorderLayout());
			leftPanel.setPreferredSize(new Dimension(900 , 1000)); // for fixing size

			leftSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , toolPanel , workPanel); // spliting left in tool & work
			leftSplitPane.setOneTouchExpandable(true); // this for one touch option 
			leftSplitPane.setDividerLocation(0.2);
			leftPanel.add(leftSplitPane, BorderLayout.CENTER);
			int i;
			/*for ( i = 1 ; i <= 3 ; i ++ )
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
			int j = 0 ;
			for ( i = 1 ; i <= 3 ; i ++ )
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
//			toolPanel.setLayout(new BorderLayout());


			//                      MySelected toolPanelUp = new MySelected();
//			toolPanelUp = new JPanel();
//			toolPanelDown = new JPanel();
//			URL selected_URL = getClass().getResource("comp" + 0 + ".gif");
/*			if (selected_URL != null)
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
			leftTool2.setFloatable(false);*/
			//rightPanel.setLayout(new BorderLayout()); 
			//rightPanel.setBackground(Color.gray); 

			JLabel wave_head = new JLabel ( "<html><FONT COLOR=white SIZE=6 ><B>SIMULATION OF CIRCUIT</B></FONT><br><br></html>", JLabel.CENTER);
                        wave_head.setBorder(BorderFactory.createRaisedBevelBorder( ));


			//waveRightPanel = new graph() ;// = new exp1_graph();
                        //rightPanel.add(waveRightPanel, BorderLayout.CENTER);
                        //rightPanel.add(wave_head, BorderLayout.NORTH);



			splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT , leftPanel , rightPanel);
		//	splitPane.setOneTouchExpandable(true); // this for one touch option 
		//	splitPane.setDividerLocation(0.2);
		//	add(splitPane, BorderLayout.CENTER);
			add(leftPanel, BorderLayout.CENTER);


			  add(topPanel , BorderLayout.NORTH);
                        topPanel.setBackground(Color.gray);
                        JPanel headButton = new JPanel (new FlowLayout(FlowLayout.CENTER , 100 , 10 )) ;
                        JLabel heading = new JLabel (  "<html><FONT COLOR=WHITE SIZE=18 ><B>VLSI EXPERIMENT NO : 1 </B></FONT></html>", JLabel.CENTER);
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
                        makeCircuit_button = new JButton(" MAKE CIRCUIT ");
                        enlarge_button = new JButton(" MAXIMIZE ");
                        minimize_button = new JButton(" MINIMIZE ");
                        icon_simulate.setImageObserver(simulate_button);

                        graph_button = new JButton (" FULL GRAPH " , icon_graph);
                        icon_graph.setImageObserver(graph_button);
                        simulate_button.setToolTipText("For Simulation");


			    headButton.add(simulate_button );
			    headButton.add(enlarge_button );
			    headButton.add(makeCircuit_button );
			    headButton.add(minimize_button );
                       // headButton.add(graph_button );
                   //     headButton.add(exp_list );
                     //   headButton.add(layout_button );


                        simulate_button.addActionListener(this);
                        enlarge_button.addActionListener(this);
                        makeCircuit_button.addActionListener(this);
                        minimize_button.addActionListener(this);
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
		
                         int check_points_value[][] = new int[89][1000] ; // each index will store corresponding  component for points of (comp point no -> index )
                         int i = 0 , j , j1 = 0, k1 = 0 , j2 = 0 , k2 = 0 , l = 0 ;
                         for (  i = 0 ; i < 89 ; i ++ )
                         {
                                 for (  j = 0 ; j < 10 ; j ++ )
                                 {
                                         check_points_value[i][j] = -1 ;
                                 }
                         }
                         for (  i = 0 ; i < 6 ; i ++ )
                         {
                                 for (  j = 0 ; j < 6 ; j ++ )
                                 {
                                         relation[i][j] = 0 ;
                                 }
                         }
			
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
                         for (  i = 0 ; i < 89 ; i ++ ) // for making aal connected point for each end_point in its array ..
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
			int mod;
			int k;
                         // checking 
                         for (  i = 0 ; i < 89 ; i ++ )
                         {
                                 l = 0 ;
				mod=i%3;
				if(i<42)
				{
                                 while ( check_points_value[i][l] != -1 && i!=check_points_value[i][l])
                                 {
					k=check_points_value[i][l];
				//	System.out.println("l"+l);
					if(k>41)
					{
					relation[mod][(k%3)+3]++;
					}
					else
					{
					relation[mod][k%3]++;
					}
					l++;
                                         //System.out.println(check_points_value[i][l++]);
                                 }
				}
				else if(i>41 && i<84)
				{
                                 while ( check_points_value[i][l] != -1 )
                                 {
					k=check_points_value[i][l];
					if(k>41)
					{
					relation[3+mod][(k%3)+3]++;
					}
					else
					{
					relation[3+mod][k%3]++;
					}
					l++;
                                         //System.out.println(check_points_value[i][l++]);
				
                                 }
				}
                         }
                        /* for (  i = 0 ; i < 6 ; i ++ )
                         {
                                 for (  j = 0 ; j < 6 ; j ++ )
                                 {
                                         System.out.println("relation i and j"+i+j+relation[i][j])  ;
                                 }
                         }*/
	//		if ( exp_type == 0 ) // Complementary 
                        // {
                                 for (  i = 0 ; i < 89 ; i ++ )
                                 {
					if((i+1)%3==0)
						continue;
                                         if ( check_points_value[i][0] == -1 )
                                         {
                                                 return false ; // if any end point of component is free then wrong circiut ...
                                         }
                                 }
				for(i=0;i<42;i++)
				{
					 l = 0 ;
                                while (  check_points_value[i][l] != -1 )
                                {
					if((i+1)%3==0)
					{
						
					}
                                        if ( check_points_value[i][l] == 84 || check_points_value[i][l]==85 || check_points_value[i][l]==86)
                                        {
                                                return false ;
                                        }
                                        l++;
                                }
				}
				for(i=42;i<84;i++)
				{
					 l = 0 ;
                                while (  check_points_value[i][l] != -1 )
                                {
                                        if ( check_points_value[i][l] == 87 || check_points_value[i][l]==88 )
                                        {
                                                return false ;
                                        }
                                        l++;
                                }
				}

                                 /*for (  i = 3 ; i < 5 ; i ++ )
                                 {
                                         if ( check_points_value[i][0] == -1 )
                                         {
                                                 return false ; // if any end point of component is free then wrong circiut ...
                                         }
                                 }*/
					
                          /*       l = 0 ;
                                /* while (  check_points_value[0][l] != -1 )
                                 {
                                         if ( check_points_value[0][l] != 9 && check_points_value[0][l] != 0)
                                        {
                                                 return false ;
                                         }
                                        l++;
                                 }
			}*/
			return true;
			}
		public void actionPerformed(ActionEvent e)
		{
			  if(e.getSource() == simulate_button )
			{
				if(make_circuit > 0)
				{
				graphDialog dialog;
                                dialog = new graphDialog( new JFrame() ,"yes",1,1);
                                                          dialog.setVisible(true);
				/*  waveRightPanel.make_graph();// Read file OUTFILE and draw the 
                                waveRightPanel.repaint();
                                waveRightPanel.setVisible(true);*/
                                simulate_flag = true ;
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Make The Circuit First By Pressing Make Circuit Button :)");
				}

			}
			if(e.getSource() ==  makeCircuit_button)
			{
			make_circuit++;
				makeCircuitDialog dialog;
				dialog=new makeCircuitDialog(new JFrame());
				
                                dialog.setVisible(true);
			}
			  if(e.getSource() == enlarge_button )
			  {
			  	if(enlarge < 21)
				{
			  	enlarge=enlarge+2;
				//System.out.println("enlarge"+enlarge);
				repaint();
				}
			  }
			  if(e.getSource() == minimize_button )
			  {
			  	if(enlarge > 3)
				{
			  	enlarge=enlarge-2;
				//System.out.println("minimize"+enlarge);
				repaint();
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
			else if (e.getSource() == img_button1[3] ) // Wire :)
			{
				img_button_pressed = 3 ;
				change_selected(3);
			}
	/*		else if (e.getSource() == img_button1[4] ) // Input 
			{
				if ( comp_count[4] == 0 )
				{
					img_button_pressed = 4 ;
					change_selected(4);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You already have this component !! :)");
				}
				//      JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//      img_button_pressed = 4 ;        
				//      change_selected(4);
			}
			else if (e.getSource() == img_button1[5] )
			{
				JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//                              img_button_pressed = 5 ;        
				//                              change_selected(5);
			}
			else if (e.getSource() == img_button1[6] )
			{
				JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//      img_button_pressed = 6 ;        
				//      change_selected(6);
			}*/
			else if (e.getSource() == img_button2[1] )
			{
			//	if ( comp_count[7] == 0 )
			//	{
					img_button_pressed = 7 ;
					change_selected(7);
			//	}
			//	else
			//	{
			//		JOptionPane.showMessageDialog(null, "You already have this component !! :)");
			//	}
			}
			else if (e.getSource() == img_button2[2] )
			{
			//	if ( comp_count[8] == 0 )
			//	{
					img_button_pressed = 8 ;
					change_selected(8);
			//	}
			//	else
			//	{
			//		JOptionPane.showMessageDialog(null, "You already have this component !! :)");
			//	}
			}
			else if (e.getSource() == img_button2[3] )
			{
			//	if ( comp_count[9] == 0 )
			//	{
					img_button_pressed = 9 ;
					change_selected(9);
			//	}
			//	else
			//	{
			//		JOptionPane.showMessageDialog(null, "You already have this component !! :)");
			//	}
			}
		/*	else if (e.getSource() == img_button2[4] )
			{
				if ( comp_count[10] == 0 )
				{
					img_button_pressed = 10 ;
					change_selected(10);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You already have this component !! :)");
				}
				//      JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//      img_button_pressed = 10 ;       
				//      change_selected(10);
			}
			else if (e.getSource() == img_button2[5] )
			{
				JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//      img_button_pressed = 11 ;       
				//      change_selected(11);
			}
			else if (e.getSource() == img_button2[6] )
			{
				JOptionPane.showMessageDialog(null, "For this Exp your don't need this component !! :)");
				//      img_button_pressed = 12 ;       
				//      change_selected(12);
			}*/

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
                                          virtual_w = height;
                                          virtual_h = width  ;
						if(node_y+virtual_h<0)
							node_y=node_y-virtual_h+15;
						if(node_x+virtual_w<0)
							node_x=node_x-virtual_w+15;
                                  }
                                  else if (angle_count == 2 ) // 180 degree
                                  {
                                          virtual_w = -width ;
                                          virtual_h = -height ;
						if(node_y+virtual_h<0)
							node_y=node_y-virtual_h+15;
						if(node_x+virtual_w<0)
							node_x=node_x-virtual_w+15;
                                  }
                                  else if (angle_count == 3 ) // 270 degree
                                  {
                                          virtual_w = height ;
                                          virtual_h = -width ;
						if(node_y+virtual_h<0)
							node_y=node_y-virtual_h+15;
						if(node_x+virtual_w<0)
							node_x=node_x-virtual_w+15;
                                  }
				  make_end_points(comp_node[index].img_no);
                                  update_mat(index); // update the matrix value to work_mat // index is the index of the node_comp matrix
                          }

			public void remove_mat() // delete the previous value from work_mat
                          {
					int i,j;
                                          for ( i = node_x ;  ;  )
                                         {
                                                  if ( virtual_w > 0 && i >= enlarge*node_x + enlarge*virtual_w  ){break;}
                                                 else if( virtual_w < 0 && i <= enlarge*node_x + enlarge*virtual_w  ){break;}
                                                  for ( j = node_y ;  ;  )
                                                  {
                                                          if ( virtual_h > 0 && j >= enlarge*node_y + enlarge*virtual_h  ){break;}
                                                          else if( virtual_h < 0 && j <= enlarge*node_y + enlarge*virtual_h  ){break;}
  
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
                                          for ( int i = enlarge*(end_pointsX[k]) +3 ; i > enlarge*(end_pointsX[k]) -3 && i >= 0; i -- )
                                          {
                                                  for ( int j = enlarge*(end_pointsY[k]) + 3 ; j > enlarge*(end_pointsY[k]) -3 && j>=0; j -- )
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
						for ( int i = enlarge*(end_pointsX[k]) +3 ; i > enlarge*(end_pointsX[k] )-3 && i>=0; i -- )
						{
							for ( int j = enlarge*(end_pointsY[k])+3 ; j > enlarge*(end_pointsY[k] )-3 && j>=0; j -- )
							{
						//		System.out.println("k"+(((pmos_count)*3)+k));
								end_points_mat[i][j] = (((node_number)*3))+k ;
							}
						}
					}
				}
				 else if ( img == 7)// nmos
                                  {
                                          for ( int k = 0 ; k < 3 ; k ++ )
                                          {
                                          for ( int i = enlarge*(end_pointsX[k] +8) ; i > enlarge*(end_pointsX[k] -7) && i>=0; i -- )
                                          {
                                                  for ( int j = enlarge*(end_pointsY[k] +7) ; j > enlarge*(end_pointsY[k] -7) && j>=0; j -- )
						  {
							  end_points_mat[i][j] = (14*3)+((node_number)*3)+k  ; //total number of pmos=14
                                                  }
                                          }
                                          }
                                  }

				else if ( img == 2 || img==9  ) // ground , VDD , INPUT , OUTPUT
                                  {
  
                                          for ( int i = end_pointsX[0] +8  ; i > end_pointsX[0] -7 && i>=0; i -- )
                                          {
                                                  for ( int j = end_pointsY[0] +8 ; j > end_pointsY[0] -7 && j>=0; j -- )
                                                  {
                                                          if ( img == 2 ) // ground
                                                          {
                                                                  end_points_mat[i][j] =  84+node_number; //totalnumber of pmos and nmos are assumed to be 2
                                                          }
                                                          else if ( img == 9 ) //vdd
                                                          {
                                                                  end_points_mat[i][j] =  87+node_number;
                                                          }
                                                  }
                                          }
                                  }

			}
			public void make_end_points(int img )
			{
				if ( img == 1 || img == 7) // C/NMOS 
				{
					count_end_points = 3;

					int a , b , c , d , e , f ;
					a = -width/2 ; b= 0 ; c = width ;d = height ; e = width ; f = -height ;

					if ( angle_count == 1 )
					{
						a = 0 ; b= -width ; c = -height ; d = width ; e = width ; f = width ;
					}
					else if ( angle_count == 2 )
					{
						a = -width ; b= 0 ; c = -width ; d = -height ; e = 0 ; f = -height / 2 ;
					}
					else if ( angle_count == 3 )
					{
						a = 0 ; b= -width ; c = height ; d = -width ; e = height/2 ; f = 0 ;
					}
					end_pointsX[0] = node_x + a ;
					end_pointsY[0] = node_y + b;

					end_pointsX[1] = node_x + c ;
					end_pointsY[1] = node_y + d ;

					end_pointsX[2] = node_x + e  ;
					end_pointsY[2] = node_y +  f ;

				}
				
				else if ( img == 2 || img == 9 ) //terminal
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
				update_end_points_mat(img);
			}
			public void update_mat(int index) // update the matrix value to work_mat // index is the index of the node_comp matrix
                         {
                                  int i , j ;
				//	node_x=enlarge*node_x;				
				//	node_y=enlarge*node_y;	
                                  for ( i = enlarge*node_x ;   ;)
                                  {
				  	if(i < 0)
						break;
                                                  if ( virtual_w >= 0 && i >= enlarge*node_x + enlarge*virtual_w  ){break;}
                                                  else if( virtual_w <= 0 && i <= enlarge*node_x + enlarge*virtual_w  ){break;}
  
                                                  for ( j = enlarge*node_y ;  ;  )
                                                  {
						  	if(j < 0)
								break;
                                                          if ( virtual_h >= 0 && j >= enlarge*node_y + enlarge*virtual_h  ){break;}
                                                          else if( virtual_h <= 0 && j <= enlarge*node_y + enlarge*virtual_h  ){break;}
                                                          work_mat[i][j] =  index ;
				//			 System.out.println("update mat"); 
							  // update the matrix as the img is selected  
                                                          if ( virtual_h >= 0 ){j++;}else{j--;}
                                                  }
  
                                                  if ( virtual_w >= 0 ){i++;}else{i--;}
                                  }
                                  make_end_points(img_no);
                          
			}


		}

		public class graphDialog extends JDialog //implements ActionListener 
		{
                          Container cp;
			graph r;
			public graphDialog(JFrame fr,String comp,int x,int y)
			{
                                  super (fr , "Component Description " , true ); // true to lock the main screen 
  
			//super(new BorderLayout());
                                  cp = getContentPane();
                  //                SpringLayout layout = new SpringLayout();
                    //              cp.setLayout(layout);
                                  	setSize(1000 , 1000);
				r=new graph();
			//	  r.make_graph();// Read file OUTFILE and draw the 
                                r.repaint();
                                r.setVisible(true);
                      //           layout.putConstraint(SpringLayout.WEST , r , 20,   SpringLayout.WEST , cp );
				cp.add(r);
                                simulate_flag = true ;
			}
		}
		public class makeCircuitDialog extends JDialog implements ActionListener
		{
		 JSpinner size;
                          Container cp;
			  JButton ok;
		 public makeCircuitDialog(JFrame fr)
		 {
                      super (fr , "Component Description " , true ); // true to lock the main screen 
                                  cp = getContentPane();
                                  SpringLayout layout = new SpringLayout();
                                  cp.setLayout(layout);
				  setSize(200,100);
                                  SpinnerModel size_model =        new SpinnerNumberModel(4, //initial value
                                                          4, //min
                                                          12, //max
                                                          4);  //step
                                          size = new JSpinner(size_model);
                                          JLabel comp_name = new JLabel("<html><font size=4><b>Select Size</b></font></html>" );//,icon[icon_no],JLabel.CENTER);
                                          layout.putConstraint(SpringLayout.WEST , comp_name , 50,   SpringLayout.WEST , cp );
                                          layout.putConstraint(SpringLayout.NORTH , comp_name , 20,  SpringLayout.NORTH , cp);
					 //layout.putConstraint(SpringLayout.NORTH , ok , 10,  SpringLayout.NORTH , cp);
					//layout.putConstraint(SpringLayout.NORTH , size , 60,  SpringLayout.NORTH , cp);
					//layout.putConstraint(SpringLayout.NORTH , ok , 100,  SpringLayout.NORTH , cp);


					  ok=new JButton("O.K");
                                          cp.add(size);
					  cp.add(ok);
					  ok.addActionListener(this);
		 }

 		public void actionPerformed(ActionEvent e )
                {
                           if(e.getSource() == ok )
                           {
			   	circuit=size.getValue().toString();
				
				System.out.println("sircuit_size"+circuit);
				setVisible(false);
				workPanel.repaint();

				}
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
                                                  pmos_count--;
							pmos_delete[pmos_delete_count++]=comp_node[node_index].node_number;
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
                                          workPanel.repaint();
                                  }
                          }


		}



		public class WorkPanel extends JPanel implements MouseMotionListener,MouseListener
		{
			int mouse_pressed_x;
			int mouse_pressed_y;
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
                                         // int x = (work_x % 15)>7 ? (work_x/15)*15+15 : (work_x/15)*15; // for making good 
                                        //  int y = (work_y % enlarge)>enlarge/2 ? (work_y/enlarge)*enlarge+enlarge : (work_y/enlarge)*enlarge; // accurate wire point around end points 
                                         int x = (work_x % enlarge)>enlarge/2 ? (work_x/enlarge)*enlarge+enlarge : (work_x/enlarge)*enlarge; // for making good 
                                          int y = (work_y % enlarge)>enlarge/2 ? (work_y/enlarge)*enlarge+enlarge : (work_y/enlarge)*enlarge; // accurate wire point around end points 
                                          wire[total_wire-1 ].update2(x/enlarge , y/enlarge);
  //                                      wire[total_wire-1 ].update2((work_x/20)*20 , (work_y/20)*20);
                                          repaint();
                                  }


			}
			//public void mouseReleased(MouseEvent m)
			public void mouseDragged(MouseEvent m)
			{

			}
			public void andGate(int work_x,int work_y)
			{

				int i,x,y;
				x=work_x;
				y=work_y;
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 
                                                          wire[total_wire++] = new line(x+2,y+2 , x+2, y+2);
                                                          wire[total_wire - 1].update2(x+5-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+7,y+2 , x+7, y+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+5,y-2 , x+5, y-2);
                                                          wire[total_wire - 1].update2(x+10 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+10-2,y+2 , x+10-2, y+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+2 , x+10+2, y+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5+2,y+10+2 , x+5+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y+10-2 , x+5, y+10-2);
                                                          wire[total_wire - 1].update2(x+10 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x-2,y+2 , x-2, y+2);
                                                          wire[total_wire - 1].update2(x-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x-2,y+10+2 , x-2, y+10+2);
                                                          wire[total_wire - 1].update2(x+5-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y+5-2 , x+10, y+5-2);
                                                          wire[total_wire - 1].update2(x-3 , y+5-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x-3,y+5-2 , x-3, y+5-2);
                                                          wire[total_wire - 1].update2(x-3 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x-3,y-2 , x-3, y-2);
                                                          wire[total_wire - 1].update2(x , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+5+2 , x+10+2, y+5+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10-2,y+5+2 , x+10-2, y+5+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+5+3 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x,y-2 , x, y-2);
                                                          wire[total_wire - 1].update2(x , y-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y-2 , x+5, y-2);
                                                          wire[total_wire - 1].update2(x+5 , y-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5+2,y+10+2 , x+5+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+5+2 , y+10+4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
				x=8+x;
				for(i=0;i<3;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 
					comp_node[total_comp] = new node(x , y , 1 , 2  , 2 ,pmos_count);
					pmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					y=y+4;
				}
				x=work_x;
				y=work_y;
				for(i=0;i<3;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 

					comp_node[total_comp] = new node(x , y , 7 , enlarge * 2 , 2*enlarge,nmos_count);
					nmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					if(i==0)
						x=x+4;
					if(i==1)
					{
						y=y+8;
					}
				}

			}
			
			public void exorGate(int work_x,int work_y)
			{

				int i,x,y;
				x=work_x;
				y=work_y;
                                                          wire[total_wire++] = new line(x,y-2 , x, y-2);
                                                          wire[total_wire - 1].update2(x+5 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+2,y+2 , x+2, y+2);
                                                          wire[total_wire - 1].update2(x+5-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+5+2,y+2 , x+5+2, y+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          
							  wire[total_wire++] = new line(x+10,y-2 , x+10, y-2);
                                                          wire[total_wire - 1].update2(x+15 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+2 , x+10+2, y+2);
                                                          wire[total_wire - 1].update2(x+15-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+2,y+2 , x+2, y+2);
                                                          wire[total_wire - 1].update2(x+2 , y+3 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+2,y+3 , x+2, y+3);
                                                          wire[total_wire - 1].update2(x+15 , y+5-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y+10-2 , x+10, y+10-2);
                                                          wire[total_wire - 1].update2(x+10 , y+5-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20,y+5-2 , x+20, y+5-2);
                                                          wire[total_wire - 1].update2(x+20+3 , y+15-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20+3,y+15-2 , x+20+3, y+15-2);
                                                          wire[total_wire - 1].update2(x+10 , y+15-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20,y+5-2 , x+20, y+5-2);
                                                          wire[total_wire - 1].update2(x+20 , y-4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20,y-4 , x+20, y-4);
                                                          wire[total_wire - 1].update2(x , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+15-2,y+2 , x+15-2, y+2);
                                                          wire[total_wire - 1].update2(x+15-2 , y-1 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+15-2,y-1 , x+15-2, y-1);
                                                          wire[total_wire - 1].update2(x+20+4 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20+4,y+10-2 , x+20-4, y+10-2);
                                                          wire[total_wire - 1].update2(x+20 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20+4,y+15+3 , x+20+4, y+15+3);
                                                          wire[total_wire - 1].update2(x+20+4 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+20+4,y+15+3 , x+20+4, y+15+3);
                                                          wire[total_wire - 1].update2(x+5-3 , y+15-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y+15-2 , x+5, y+15-2);
                                                          wire[total_wire - 1].update2(x+5-3 , y+15-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y-2 , x+10, y-2);
                                                          wire[total_wire - 1].update2(x+10 , y-3 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y-3 , x+10, y-3);
                                                          wire[total_wire - 1].update2(x-3 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y+10-2 , x+5, y+10-2);
                                                          wire[total_wire - 1].update2(x-3 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y+10-2 , x+5, y+10-2);
                                                          wire[total_wire - 1].update2(x+5 , y+10-1 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5,y+10-1 , x+5, y+10-1);
                                                          wire[total_wire - 1].update2(x+15 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5+2,y+15+2 , x+5+2, y+15+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+15+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+5+2,y+10+2 , x+5+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+15+2 , x+10+2, y+15+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+10+2 , x+10+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+15-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+15+2,y+10+2 , x+15+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+20-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+15+2,y+5+2 , x+15+2, y+5+2);
                                                          wire[total_wire - 1].update2(x+20-2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+15-2,y+10+2 , x+15-2, y+10+2);
                                                          wire[total_wire - 1].update2(x+15-2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x,y-2 , x, y-2);
                                                          wire[total_wire - 1].update2(x , y-6 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y-2 , x+10, y-2);
                                                          wire[total_wire - 1].update2(x+10 , y-6 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10+2,y+15+2 , x+10+2, y+15+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+15+4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
				x=4+x;
				for(i=0;i<6;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 
					comp_node[total_comp] = new node(x , y ,1 , 2  , 2 ,pmos_count);
					pmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					if(i==0)
						x=x+8;
					if(i==1)
						y=y+4;
					if(i==2)
						x=x+4;
					if(i==3)
						y=y+4;
					if(i==4)
						x=x-4;
					//if(i==5)
					//	x=x-10;
				}
				x=work_x;
				y=work_y;
				for(i=0;i<6;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 

					comp_node[total_comp] = new node(x , y ,7 , enlarge * 2 , 2*enlarge,nmos_count);
					nmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					if(i==0)
						x=x+8;
					if(i==1)
					{
						x=x-4;
						y=y+8;
					}
					if(i==2)
					{
						x=x+4;
					}
					if(i==3)
					{
					y=y+4;
					}
					if(i==4)
					x=x-4;
				}

			}
			public void orGate(int work_x,int work_y)
			{

				int i,x,y;
				x=work_x;
				y=work_y;
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 
                                                          wire[total_wire++] = new line(x+5+2,y+2 , x+5+2, y+2);
                                                          wire[total_wire - 1].update2(x+10-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+2,y+2 , x+2, y+2);
                                                          wire[total_wire - 1].update2(x+5-2 , y+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x,y-2 , x, y-2);
                                                          wire[total_wire - 1].update2(x+5 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x-2,y+2 , x-2, y+2);
                                                          wire[total_wire - 1].update2(x-2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+2,y+2 , x+2, y+2);
                                                          wire[total_wire - 1].update2(x+2 , y+5+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x-2,y+5+2 , x+2, y+5+2);
                                                          wire[total_wire - 1].update2(x-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+2,y+10+2 , x+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+5-2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x,y+10-2 , x, y+10-2);
                                                          wire[total_wire - 1].update2(x+5 , y+10-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+10+2,y+2 , x+10+2, y+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+2,y+5+2 , x+2, y+5+2);
                                                          wire[total_wire - 1].update2(x+2 , y+5+3 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+5+2,y+10+2 , x+5+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+10+2 , y+10+2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x,y+5-2 , x, y+5-2);
                                                          wire[total_wire - 1].update2(x+10+3 , y+5-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+10+3,y+5-2 , x+10+3, y+5-2);
                                                          wire[total_wire - 1].update2(x+10+3 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
                                                          wire[total_wire++] = new line(x+10+3,y-2 , x+10+3, y-2);
                                                          wire[total_wire - 1].update2(x+10 , y-2 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x,y-2 , x, y-2);
                                                          wire[total_wire - 1].update2(x , y-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+10,y-2 , x+10, y-2);
                                                          wire[total_wire - 1].update2(x+10 , y-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(x+2,y+10+2 , x+2, y+10+2);
                                                          wire[total_wire - 1].update2(x+2 , y+10+4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
				x=x+4;
				for(i=0;i<3;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 
					comp_node[total_comp] = new node(x , y ,1 , 2  , 2 ,pmos_count);
					pmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					if(i==0)
					{
					x=x+4;
					}
					if(i==1)
					{
					y=y+8;
					x=x-4;
					}
				}
				x=work_x;
				y=work_y;
				for(i=0;i<3;i++)
				{
					x = (x % (enlarge))>enlarge/2 ? (x/(enlarge))*enlarge+enlarge : (x/(enlarge))*enlarge; // for making good 
					y = (y % (enlarge))>enlarge/2 ? (y/(enlarge))*enlarge+enlarge : (y/(enlarge))*enlarge; // accurate wire point around end points 

					comp_node[total_comp] = new node(x , y , 7 , enlarge * 2 , 2*enlarge,nmos_count);
					nmos_count++;
					comp_node[total_comp].update_mat(total_comp);
					comp_node[total_comp].rotate(total_comp);
					total_comp++;
					y=y+4;
				}

			}
			public void mouseClicked(MouseEvent m)
			{
				
			}
			//public void mouseDragged(MouseEvent m)
			public void mouseReleased(MouseEvent m)
			{
			int i,j;
				    work_x = m.getX();
                                  work_y = m.getY();
			int u=work_y - mouse_pressed_y;
			u=u/enlarge;
			int w=work_x- mouse_pressed_x;
			w=w/enlarge;
			System.out.println("W"+w);
			System.out.println("U"+u);
			for(j=0;j<total_comp;j++)
			{
			comp_node[j].node_y+=u;
			comp_node[j].node_x+=w;
			}
			for(j=0;j<total_wire;j++)
			{
			wire[j].y1 +=u;
			wire[j].y2 +=u;
			wire[j].x1 +=w;
			wire[j].x2 +=w;
			}
			System.out.println("before paint");
	        	repaint();
		    	}


			public void mouseEntered(MouseEvent m)
			{
			}
			public void mouseExited(MouseEvent m)
			{
			}
			public void mousePressed(MouseEvent m)
			{
				    work_x = m.getX();
                                  work_y = m.getY();
				mouse_pressed_x=work_x;
				mouse_pressed_y=work_y;
		/*		  if ( work_mat[work_x][work_y] != -1 )
                                 {
                                         node_drag = work_mat[work_x][work_y];   // node is selected for drag
 
                                         comp_node[node_drag].remove_mat();                 // update the matrix as the img is selected , so can be moved 
 
				}*/

			}
			public void paint(Graphics g)
			{
				if(make_circuit== 1)
				{
				System.out.println("Circuit size in paint"+circuit);
				int c=Integer.parseInt(circuit);
						int r,q;
						for(r=0;r< c;r++)
						{
						q=20+(50*r);
						andGate(q,20);
						exorGate(20+q,20);
						
							  wire[total_wire++] = new line(q,20-5 , q, 20-5);
                                                          wire[total_wire - 1].update2(q , 20-7 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(q,20-7 , q, 20-7);
                                                          wire[total_wire - 1].update2(20+q , 20-7 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(q+5,20-5 , q+5, 20-5);
                                                          wire[total_wire - 1].update2(20+q+10 , 20-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(20+q+10+2,20+15+4 , 20+q+10+2, 20+15+4);
                                                          wire[total_wire - 1].update2(40+(50*r) , 50-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
						}
						andGate(20,50);
						for(r=0;r< c ; r++)
						{
						q=40+(50*r);
						exorGate(q,50);
					//	exorGate(90,50);
					//	exorGate(140,50);
					//	exorGate(190,50);
						}
						orGate(20,80);
							  
							  wire[total_wire++] = new line(40,50-5 , 40, 50-5);
                                                          wire[total_wire - 1].update2(20 , 50-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(20+5+2,20+10+4 , 20+5+2, 20+10+4);
                                                          wire[total_wire - 1].update2(20-5 , 20+10+4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(20,80-5 , 20, 80-5);
                                                          wire[total_wire - 1].update2(20-5 , 20+10+4 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
							  
							  wire[total_wire++] = new line(20+5+2,50+10+4 , 20+5+2, 50+10+4);
                                                          wire[total_wire - 1].update2(20+10 , 80-5 ); // -1 bec inder of first wire is 0 
                                                          wire[total_wire - 1 ].update_mat(total_wire - 1); // 
						

				make_circuit=2;
				}
				int i,j;
				Graphics2D g2d = (Graphics2D)g;
				g2d.scale(scale_x , scale_y);
				// back ground ----------------
				g2d.setColor(Color.white);
				g.fillRect(start_width_work_panel,start_height_work_panel,work_panel_width+1000 , work_panel_height+1000);
				g2d.setColor(Color.black);
				g2d.setStroke(new BasicStroke(1));
				int x;
				x=5;
			//	for ( i = start_width_work_panel ; i < work_panel_width +400; i+=enlarge)
				for ( i = start_width_work_panel ; i < 500; i+=1)
				{
					for ( j = start_height_work_panel ; j < 500 ; j+=1 )
					{
						g2d.drawOval(  enlarge*i,enlarge*j , 0 , 0);
					}
				}
				for ( i = 0; i < total_comp ; i++ )
				{
					if ( comp_node[i].del != true )
					{
						if ( comp_node[i].img_no == 1)
						{
							comp_node[i].update_mat(i);
							draw_cmos(g2d , enlarge*comp_node[i].node_x  , enlarge*comp_node[i].node_y , enlarge , comp_node[i].angle);
							g.setColor(Color.blue);
						//	g.drawString(comp_str[1] , enlarge*comp_node[i].node_x - enlarge*3 , enlarge*comp_node[i].node_y );
						}
						else if ( comp_node[i].img_no == 3)
						{
						//	draw_horizontal_wire(g2d , enlarge*comp_node[i].node_x  , enlarge*comp_node[i].node_y , enlarge, comp_node[i].angle);
							g.setColor(Color.blue);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 8)
						{
							draw_vertical_wire(g2d , comp_node[i].node_x  , comp_node[i].node_y , comp_node[i].height , comp_node[i].angle);
							g.setColor(Color.blue);
							//g.drawString(comp_str[1] , comp_node[i].node_x -10 , comp_node[i].node_y + 10 );
						}
						else if ( comp_node[i].img_no == 7)
						{
							draw_nmos(g2d , enlarge*comp_node[i].node_x  , enlarge*comp_node[i].node_y , enlarge , comp_node[i].angle);
							g.setColor(Color.blue);
						//	g.drawString(comp_str[7] , enlarge*(comp_node[i].node_x + -10) , enlarge*(comp_node[i].node_y + 10) );



						}
						  else if ( comp_node[i].img_no == 2)
                                                 {
                                                         draw_ground(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
                                                       g.setColor(Color.blue);
                                                       g.drawString(comp_str[2] , comp_node[i].node_x + 20 , comp_node[i].node_y + 20 );
                                                 }
                                                 else if ( comp_node[i].img_no == 9)
                                                 {
                                                         draw_vdd(g2d , comp_node[i].node_x  , comp_node[i].node_y , 15, comp_node[i].angle);
                                                         g.setColor(Color.blue);
                                                         g.drawString(comp_str[9] , comp_node[i].node_x + 30 , comp_node[i].node_y + 10 );
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
                                                         //g2d.drawLine (wire[i].x[k] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k] );
                                                         //g2d.drawLine (wire[i].x[k+1] , wire[i].y[k] , wire[i].x[k+1] , wire[i].y[k+1] );
							 /*System.out.println("wire_mat x1 "+wire[i].x[k]);
							 System.out.println("wire_mat y1 "+wire[i].y[k]);
							 System.out.println("wire_mat x2 "+wire[i].x[k+1]);
							 System.out.println("wire_mat y2 "+wire[i].y[k+1]);
							 System.out.println("enlarge wire_mat x1 "+enlarge*wire[i].x[k]);
							 System.out.println("en  wire_mat y1 "+enlarge*wire[i].y[k]);
							 System.out.println("en wire_mat x2 "+enlarge*wire[i].x[k+1]);
							 System.out.println("en wire_mat y2 "+enlarge*wire[i].y[k+1]);*/
							// if(enlarge==5)
							 {
                                                       g2d.drawLine (enlarge*wire[i].x1 , enlarge*wire[i].y1 , enlarge*wire[i].x2 , enlarge*wire[i].y1 );
                                                       g2d.drawLine (enlarge*wire[i].x2 , enlarge*wire[i].y1 , enlarge*wire[i].x2 , enlarge*wire[i].y2 );
						       }
                                                 }
 
                                                 g2d.setColor(Color.red);
                                                 g2d.fillRect (enlarge*(wire[i].x1)  , enlarge*(wire[i].y1 ) , enlarge/2 ,enlarge/2);
                                                 g2d.fillRect (enlarge*(wire[i].x2 ) , enlarge*(wire[i].y2 ) , enlarge/2 ,enlarge/2);
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
			void draw_nmos(Graphics2D g , int x , int y , int width , double angle )
                         {
 
                                 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow);
 //                              g.drawRect( x , y , 2*width , 4*width);
 
                                 g.setStroke(new BasicStroke(2));
                                 g.setColor(Color.blue);
                                // g.drawLine(x  , y + 2*width , x + width , y + 2*width);
 				   g.drawLine(x-(2*width),y,x+width/2,y);
                                // g.drawLine(x + width , y + width/4 +width, x +  width , y +(7* width)/4 +width);
                                 g.drawLine(x + width/2 , y -width, x +  width/2 , y  +width);
                                 
				 //g.drawLine(x + width +width/4 , y + width/4 +width, x +  width +width/4, y +(7* width)/4 +width);
				 g.drawLine(x + width  , y - width, x +  width , y +width);
 
                                 //g.drawLine(x + (5*width)/4 , y + width/2+width, x + 2* width , y + width/2+width );
                                 
				 g.drawLine(x + width , y - (4*width)/5, x + 2* width , y -(4*width)/5 );
                                 
				// g.drawLine(x + (5*width)/4 , y +(3* width)/2+width, x +  2*width , y +(3* width)/2+width);
 
				g.drawLine(x + width , y +(4* width)/5, x +  2*width , y +(4* width)/5);
                                
			//	g.drawLine(x + 2*width , y , x + 2*width , y + (3* width)/2);
                                 g.drawLine(x + 2*width , y-(4*width)/5 , x + 2*width , y - (2* width));
                                 
				 //g.drawLine(x + 2*width , y + 4*width , x + 2*width , y + (5* width)/2);
				 g.drawLine(x + 2*width , y + (4*width)/5 , x + 2*width , y + (2* width));
 
                                 g.setStroke(new BasicStroke(1));
                                 // end points 
                                 g.setColor(Color.red);
                             /*    g.fillRect( x - 4, y + 2*width -4, 8 ,8 );
                                 g.fillRect( x + 2*width -4, y - 4 , 8 ,8 );
                                 g.fillRect( x + 2*width -4, y +4 * width - 4 , 8 ,8 );
 				*/
                                 g.fillRect( x - 2*width-enlarge/4, y  -enlarge/4, enlarge/2 ,enlarge/2 );
                                 g.fillRect( x + 2*width -enlarge/4, y - enlarge/4 -(2*width) , enlarge/2 ,enlarge/2 );
                                 g.fillRect( x + 2*width -enlarge/4, y -enlarge/4 +(2*width) , enlarge/2 ,enlarge/2 );
                                 g.setColor(Color.black);
                                 g.rotate(-angle , x , y);
                         }
			void draw_cmos(Graphics2D g , int x , int y , int width , double angle)
                         {
 
				//System.out.println("cmos node_x"+x+"node_y"+y);
                                 g.rotate(angle , x , y);
                                 g.setColor(Color.yellow);
 //                              g.drawRect( x , y , 2*width , 4*width);
 
                                 g.setColor(Color.blue);
                                // g.drawOval( x + width - 6, y + 2*width - 3, 6,6 );
                                 g.drawOval( x , y-width/4 , width/2,width/2 );
                                 g.setStroke(new BasicStroke(2));
                                // g.drawLine(x  , y + 2*width , x + width - 6,y + 2*width);
                                 g.drawLine(x -2*width , y  , x ,y );
 
                                // g.drawLine(x + width , y + width/4 +width, x +  width , y +(7* width)/4 +width);
                                 g.drawLine(x + width/2 , y -width, x +  width/2 , y  +width);
                                 
				 
				 
				// g.drawLine(x + width +width/4 , y + width/4 +width, x +  width +width/4, y +(7* width)/4 +width);
				 g.drawLine(x + width  , y - width, x +  width , y +width);
 
                               //  g.drawLine(x + (5*width)/4 , y + width/2+width, x + 2* width , y + width/2+width );
                                 g.drawLine(x + width , y - (4*width)/5, x + 2* width , y -(4*width)/5 );
                                
			//	g.drawLine(x + (5*width)/4 , y +(3* width)/2+width, x +  2*width , y +(3* width)/2+width);
				g.drawLine(x + width , y +(4* width)/5, x +  2*width , y +(4* width)/5);
 
                                // g.drawLine(x + 2*width , y , x + 2*width , y + (3* width)/2);
                                 g.drawLine(x + 2*width , y-(4*width)/5 , x + 2*width , y - (2* width));
                                 
				// g.drawLine(x + 2*width , y + 4*width , x + 2*width , y + (5* width)/2);
				 g.drawLine(x + 2*width , y + (4*width)/5 , x + 2*width , y + (2* width));
 
                                 g.setStroke(new BasicStroke(1));
                                 // end points 
                                 g.setColor(Color.red);
                                 g.fillRect( x - 2*width-enlarge/4, y  -enlarge/4, enlarge/2 ,enlarge/2 );
                                 g.fillRect( x + 2*width -enlarge/4, y - enlarge/4 -(2*width) , enlarge/2 ,enlarge/2 );
                                 g.fillRect( x + 2*width -enlarge/4, y -enlarge/4 +(2*width) , enlarge/2 ,enlarge/2 );
 
                                g.setColor(Color.black);
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





}
}
}
