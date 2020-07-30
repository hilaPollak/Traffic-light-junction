import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Created on Tevet 5770 
 */

/**
 * @author לויאן
 */

public class CarMooving extends Thread
{
	JLabel myLabel;
	JPanel myPanel;
	private ShloshaAvot myRamzor;
	private int key;
	int x, dx;
	int y, dy;
	ImageIcon imageIcon;
	boolean first2=true;
	public CarMooving(JPanel myPanel, ShloshaAvot myRamzor,int key) 
	{
		this.myPanel=myPanel;
		this.myRamzor=myRamzor;
		this.key=key;
		setCarLocationAndMooving();
		imageIcon = getImageIcon();
		myLabel= new JLabel(imageIcon);
		myLabel.setOpaque(false);
		myPanel.add(myLabel);
		setDaemon(true);
		start();
	}
	private void setCarLocationAndMooving() 
	{
		switch (key) 
		{
		case 1:
			x=850;  dx=-100;
			y=70;  dy=0;
			break;
		case 2:
			x=500;  dx=0;
			y=720;  dy=-50;
			break;
		case 3:
			x=-30;  dx=35;
			y=405;  dy=22;
			break;
		case 4:
			x=-30;  dx=50;
			y=390;  dy=0;
			break;

		case 5:
			dx=-50;
			dy=0;
			break;

		default:
			x=900;  dx=-50;
			y=100;  dy=0;
			break;
		}
	}
	public void run()
	{
		myLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());

		while (!finish())
		{
			if (myRamzor.isStop() && toStop())
				;
			else
			{
				x +=dx;
				y +=dy;
				myLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myPanel.repaint();
		}

	}
	private boolean finish() 
	{
		switch (key) 
		{
		case 1:
			return x<-20;
		case 2:
			if (y>180)
				return false;
			else
			{
				key=5;
				setCarLocationAndMooving();
				imageIcon=getImageIcon();
				myLabel.removeAll();
				myLabel.setIcon(imageIcon);
				myLabel.setBounds(x, y, imageIcon.getIconWidth(), imageIcon.getIconHeight());
				return false;
			}
		case 3:
			if (y>600)
			{
				dx=0;
				dy=30;
			}
			return y>800;
		case 4:
			return x>800;
		case 5:
			return x<-20;
		}
		return false;
	}
	private boolean toStop() 
	{
		switch (key) 
		{
		case 1:
			return x>550;
		case 2:
			return y>530; 
		case 3:
			return x<100;
		case 4:
			return x<=150;
		}
		return false;
	}
	private ImageIcon getImageIcon() 
	{
		switch (key) 
		{
		case 1:
			return  new ImageIcon("Images/left.gif");
		case 2:
			return  new ImageIcon("Images/up.gif");
		case 3:
			return  new ImageIcon("Images/right.gif");
		case 4:
			return  new ImageIcon("Images/right.gif");
		case 5:
			return  new ImageIcon("Images/upLeft.gif");
		default:
			break;
		}
		return null;
	}


}
