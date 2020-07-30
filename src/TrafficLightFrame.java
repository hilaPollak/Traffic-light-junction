import javax.swing.JFrame;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

public class TrafficLightFrame extends JFrame 
{
	private final int WIDTH = 800, HEIGHT = 750;
	TrafficLightPanel myPanel;

	public TrafficLightFrame(String h, Ramzor[] ramzorim) 
	{
		super(h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(90, -15);
		myPanel = new TrafficLightPanel(ramzorim);
		add(myPanel);
		pack();
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setVisible(true);
	}
}

