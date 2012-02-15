import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.net.URL;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MouseClickExample extends Applet implements MouseListener,MouseMotionListener
{
	Image my_gif[]=new Image[20];
	int xpos=20;
	int ypos=20;
	URL base;
	MediaTracker mt;
	JPanel leftPanel=new JPanel();

	public void init() 
	{
//		super(new BorderLayout());
		leftPanel.setLayout(new BorderLayout());
                leftPanel.setMinimumSize(new Dimension(900 , 1000)); // for fixing size
 

		addMouseListener(this);
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
		
		/*JTextArea textArea=new JTextArea(5,30);
		JScrollPane scrollpane=new JScrollPane(textArea);
		JToolBar toolbar=new JToolBar("Still Dragable");
		add(toolbar,BorderLayout.PAGE_START);
		add(scrollpane,BorderLayout.CENTER);*/
	}

	public void paint(Graphics g) 
	{
		for(int i=1;i<13;i=i+2)
		{
		g.drawImage(my_gif[i],10,30*i,this);
		g.drawImage(my_gif[i+1],60,30*i,this);
		}
		g.drawImage(my_gif[5],xpos,ypos,this);
		g.drawString("("+xpos+","+ypos+")",xpos,ypos);
	}

	public void mouseMoved (MouseEvent me) {
	}
	public void mouseClicked(MouseEvent me)
	{}
	public void mouseDragged(MouseEvent me){
		xpos = me.getX();
		ypos = me.getY();
		repaint();
	}
	public void mousePressed (MouseEvent me) {}
	public void mouseReleased (MouseEvent me) {} 
	public void mouseEntered (MouseEvent me) {
	}
	public void mouseExited (MouseEvent me) {
	} 
}  
