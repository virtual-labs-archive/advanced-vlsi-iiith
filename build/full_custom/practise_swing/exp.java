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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class exp extends JApplet
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
/*		JFrame frame = new JFrame("BoxLayoutDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MyPanel demo = new MyPanel();
       // demo.populateContentPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);*/

                   MyPanel myPane = new MyPanel();
                   //myPane.setOpaque(true);
                  //setContentPane(myPane);
           }
	public class MyPanel  extends JFrame // implements MouseListener,MouseMotionListener
	{
		 Image my_gif[]=new Image[20];
          int xpos=20;
          int ypos=20;
		int x_click=333;
		int y_click;
          URL base;
          MediaTracker mt;
          JPanel leftPanel=new JPanel();
		 protected JTextArea textArea;
    protected String newline = "\n";
    static final private String PREVIOUS = "previous";
    static final private String UP = "up";
    static final private String NEXT = "next";
    static final private String SOMETHING_ELSE = "other";
    static final private String TEXT_ENTERED = "text";

	protected JSplitPane split;  
          public MyPanel()
          {
//	super("Simple Split Pane");
	//	super(new BorderLayout());
    setSize(400, 400);
    getContentPane().setLayout(new BorderLayout());

    JSplitPane spLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JPanel(), new JPanel());
    spLeft.setDividerSize(8);
    spLeft.setContinuousLayout(true);

    JSplitPane spRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JPanel(), new JPanel());
    spRight.setDividerSize(8);
    spRight.setContinuousLayout(true);

    split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spLeft, spRight);
    split.setContinuousLayout(false);
    split.setOneTouchExpandable(true);

    getContentPane().add(split, BorderLayout.CENTER);
/*		super(new BorderLayout());
 leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
  leftPanel.setBorder(BorderFactory.createLineBorder(Color.red));
*/
	add(leftPanel,BorderLayout.CENTER);

        //Create the toolbar.
  /*      JToolBar toolBar = new JToolBar("Still draggable");
        addButtons(toolBar);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
*/
        //Create the text area used for output.  Request
        //enough space for 5 rows and 30 columns.
/*        textArea = new JTextArea(5, 30);
        textArea.setEditable(true);
        textArea.append("hello");
        JScrollPane scrollPane = new JScrollPane(textArea);
*/
        //Lay out the main panel.
  /*      setPreferredSize(new Dimension(45, 13));
      //  add(toolBar, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
	leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));

                  leftPanel.setLayout(new BorderLayout());
                  leftPanel.setMinimumSize(new Dimension(900 , 1000)); // for fixing size
  
  */
/*                  addMouseListener(this);
                  addMouseMotionListener(this);
                  mt=new MediaTracker(this);
                  try{
                          base=getDocumentBase();
                  }
                  catch(Exception e){}
                  for(int k=0;k<10;k++)
                  {
                          my_gif[k]=getImage(base,"comp"+k+".gif");
                          mt.addImage(my_gif[k],k);
                  }
  
          }*/
	/*protected void addButtons(JToolBar toolBar) {
        JButton button = null;

        //first button
        button = makeNavigationButton("comp0", PREVIOUS,
                                      "Back to previous something-or-other",
                                      "Previous");
        toolBar.add(button);

        //second button
        button = makeNavigationButton("comp1", UP,
                                      "Up to something-or-other",
                                      "Up");
        toolBar.add(button);

        //third button
        button = makeNavigationButton("Forward24", NEXT,
                                      "Forward to something-or-other",
                                      "Next");
        toolBar.add(button);

        //separator
        toolBar.addSeparator();

        //fourth button
        button = new JButton("Another button");
        button.setActionCommand(SOMETHING_ELSE);
        button.setToolTipText("Something else");
        button.addActionListener(this);
        toolBar.add(button);

        //fifth component is NOT a button!
        JTextField textField = new JTextField("A text field");
        textField.setColumns(10);
        textField.addActionListener(this);
        textField.setActionCommand(TEXT_ENTERED);
        toolBar.add(textField);*/
    }
/*	 protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText) {
        //Look for the image.
        String imgLocation =  imageName+ ".gif";
        URL imageURL = MyPanel.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: "
                               + imgLocation);
        }

        return button;
    }

public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        if (PREVIOUS.equals(cmd)) { //first button clicked
            description = "taken you to the previous <something>.";
        } else if (UP.equals(cmd)) { // second button clicked
            description = "taken you up one level to <something>.";
        } else if (NEXT.equals(cmd)) { // third button clicked
            description = "taken you to the next <something>.";
        } else if (SOMETHING_ELSE.equals(cmd)) { // fourth button clicked
            description = "done something else.";
        } else if (TEXT_ENTERED.equals(cmd)) { // text field
            JTextField tf = (JTextField)e.getSource();
            String text = tf.getText();
            tf.setText("");
            description = "done something with this text: "
                          + newline + "  \""
                          + text + "\"";
        }

        displayResult("If this were a real app, it would have "
                        + description);
    }

    protected void displayResult(String actionDescription) {
        textArea.append(actionDescription + newline);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
*/
/*	  public void paint(Graphics g)
          {
		
/*                  for(int i=1;i<13;i=i+2)
                  {
                  g.drawImage(my_gif[i],10,30*i,this);
                  g.drawImage(my_gif[i+1],60,30*i,this);
                  }
     //           g.drawString("("+xpos+","+ypos+")",xpos,ypos);
               // g.drawString("("+x_click+","+y_click+")",x_click,y_click);
			g.setColor(Color.green);
		  g.fillRect(200,200,1000,1000); 		
		if(x_click < 50 )
		{
			if(y_click < 70)
                g.drawImage(my_gif[1],xpos,ypos,this);
			else if( y_click < 140)
                g.drawImage(my_gif[3],xpos,ypos,this);
			else if( y_click <  210)
                g.drawImage(my_gif[5],xpos,ypos,this);
			else if(y_click <  280)
                g.drawImage(my_gif[7],xpos,ypos,this);
			else if(y_click < 350)
                g.drawImage(my_gif[9],xpos,ypos,this);
		else if(y_click < 420)
                g.drawImage(my_gif[11],xpos,ypos,this);
			 
		}
		else if(x_click >=50 && x_click <= 100)
		{
			if(y_click < 70)
                g.drawImage(my_gif[2],xpos,ypos,this);
			else if( y_click < 140)
                g.drawImage(my_gif[4],xpos,ypos,this);
			else if( y_click <  210)
                g.drawImage(my_gif[6],xpos,ypos,this);
			else if(y_click <  280)
                g.drawImage(my_gif[8],xpos,ypos,this);
			else if(y_click < 350)
                g.drawImage(my_gif[10],xpos,ypos,this);
			else if(y_click < 420)
                g.drawImage(my_gif[12],xpos,ypos,this);
			
		}
         //       g.drawImage(my_gif[2],x_click,y_click,this);
          }*/
  
       /*   public void mouseMoved (MouseEvent me) {
          }
          public void mouseClicked(MouseEvent me)
          {
	}
          public void mouseDragged(MouseEvent me){
          }
          public void mousePressed (MouseEvent me) {
		x_click=me.getX();
		y_click=me.getY();
//		repaint();
	}
          public void mouseReleased (MouseEvent me) {
                  xpos = me.getX();
                  ypos = me.getY();
                  repaint();
		}
          public void mouseEntered (MouseEvent me) {
          }
          public void mouseExited (MouseEvent me) {
          }	*/

	}
}


