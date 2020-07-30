import java.awt.Color;

import javax.swing.JPanel;
/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

public class ShloshaAvot extends Thread
{
	Ramzor ramzor;
	JPanel panel;
	private boolean sh= false;
	private boolean stop=true;
	Event64 evToGreen, evToRed, evAtRed, evToShabat, evToChol;
	public ShloshaAvot(Ramzor ramzor, JPanel panel, Event64 evToGreen, Event64 evToRed, Event64 evAtRed,
			Event64 evToShabat, Event64 evToChol, int key) {
		this.ramzor = ramzor;
		this.panel = panel;
		this.evToGreen = evToGreen;
		this.evToRed = evToRed;
		this.evAtRed = evAtRed;
		this.evToShabat = evToShabat;
		this.evToChol = evToChol;
				new CarsMaker(panel,this,key);
		start();
	}

	enum OutState {ON_CHOL, ON_SHABAT}
	OutState outState;

	enum InState {ON_RED, ON_GREEN,ON_FLIKER,ON_RED_ORANGE,ON_ORANGE}
	InState inState;

	public ShloshaAvot( Ramzor ramzor,JPanel panel,int key)
	{
		this.ramzor=ramzor;
		this.panel=panel;
		//		new CarsMaker(panel,this,key);
		start();
	}

	public void run()
	{
		outState= OutState.ON_CHOL;
		inState= InState.ON_RED;
		setRed();
		while (true)
		{

			switch(outState)
			{
			case ON_CHOL:
				while(outState==OutState.ON_CHOL)
				{
					switch (inState) {
					case ON_RED:
						while(true)
						{
							if(evToGreen.arrivedEvent()){
								evToGreen.waitEvent();
								setOrangeRed();
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								setGreen();
								inState=InState.ON_GREEN;
								break;
							}
							else if(evToShabat.arrivedEvent())
							{
								evToShabat.waitEvent();
								sh= true;
								setShabat();
								outState=OutState.ON_SHABAT;
								break;
							}
							else
								yield();

						}
						break;
					case ON_GREEN:
						while(true)
						{
							if(evToRed.arrivedEvent()){
								evToRed.waitEvent();
								setFliker();
								setOrange();
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								setRed();
								evAtRed.sendEvent();
								inState=InState.ON_RED;
								break;
							}
							else if(evToShabat.arrivedEvent())
							{
								evToShabat.waitEvent();
								sh= true;
								setShabat();
								outState=OutState.ON_SHABAT;
								break;
							}
							else
								yield();
						}
						break;

					default:
						break;
					}
				}
				break;
			case ON_SHABAT:	
				while(true){
					if(evToChol.arrivedEvent()){
						evToChol.waitEvent();
						sh= false;
						setRed();
						inState=InState.ON_RED;
						outState=OutState.ON_CHOL;
						break;
					}else
						{setShabat();
						yield();
						}
				}
					break;
				default:
					break;
				}


			}

		}
		private void setFliker() {
			setLight(1,Color.GRAY);
			setLight(2,Color.GRAY);
			setLight(3,Color.GRAY);
			int count=0;
			while (count!=3)
			{
				try {
					sleep(700);
					setLight(3,Color.GREEN);
					sleep(700);
					setLight(3,Color.GRAY);
				} catch (InterruptedException e) {
				}
				count++;
			}
		}

		private void setShabat() {
			stop=true;

			setLight(1,Color.GRAY);
			setLight(2,Color.GRAY);
			setLight(3,Color.GRAY);
			
				try {
					sleep(500);
					setLight(2,Color.ORANGE);
					sleep(500);
				} catch (InterruptedException e) {
				}
		

		}

		private void setOrange() {	
			stop=true;
			setLight(1,Color.GRAY);
			setLight(2,Color.YELLOW);
			setLight(3,Color.GRAY);
		}

		private void setOrangeRed() {	
			setLight(1,Color.RED);
			setLight(2,Color.YELLOW);
			setLight(3,Color.GRAY);
		}
		private void setGreen() {
			setLight(3,Color.GREEN);
			setLight(1,Color.GRAY);	
			setLight(2,Color.GRAY);
			stop=false;
		}

		private void setRed() {
			setLight(1,Color.RED);
			setLight(2,Color.GRAY);
			setLight(3,Color.GRAY);
		}

		public void setLight(int place, Color color)
		{
			ramzor.colorLight[place-1]=color;
			panel.repaint();
		}

		public boolean isStop()
		{
			return stop;
		}
	}
