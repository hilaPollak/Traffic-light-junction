import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/*
 * Created on Tevet 5770 
 */
//listener to click on screen
/**
 * @author לויאן
 */


public class MyActionListener implements ActionListener
{
	Event64 evShabat, evChol, evRegl;
	
	public MyActionListener(Event64 evShabat, Event64 evChol, Event64 evRegl) {
		super();
		this.evShabat = evShabat;
		this.evChol = evChol;
		this.evRegl = evRegl;
	}

	public void actionPerformed(ActionEvent e) 
	{
		//all the button get here
		JRadioButton butt=(JRadioButton)e.getSource();//give me mofa that couse this event
		if(butt.getName().equals("16"))//shabat & chol
		{
			if (butt.isSelected())//if we select the button of shabat
				evShabat.sendEvent();
			else//if we dont select shabat button
				evChol.sendEvent();
		}
		//to regel
		else
		{
			evRegl.sendEvent(butt.getName());
			butt.setSelected(false);
			
		}
		System.out.println(butt.getName());//print the name of mofa
		//		butt.setEnabled(false);
		//		butt.setSelected(false);
	}

}
