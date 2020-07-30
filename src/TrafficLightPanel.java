import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Addar 5772 
 */

/**
 * @author לויאן
 */

class TrafficLightPanel extends JPanel
{
	Ramzor  ramzorim[];

	public TrafficLightPanel(Ramzor[] ramzorim)
	{
		super();
		setLayout(null);
		setBackground(Color.WHITE);
		this.ramzorim=ramzorim;
	}

	public void paintComponent(Graphics page)
	{
		int i, j;
		int x[]=new int[5];
		int y[]=new int[5];
		super.paintComponent(page);
		setBackground(Color.WHITE);

		page.setColor(Color.BLUE);
		page.draw3DRect(10,10,770,60,true);
		page.setColor(Color.LIGHT_GRAY);
		page.fill3DRect(10,10,770,60,true);


		page.setColor(Color.BLUE);
		page.draw3DRect(10,200,260,100,true);
		page.setColor(Color.LIGHT_GRAY);
		page.fill3DRect(10,200,260,100,true);

		page.setColor(Color.BLUE);
		page.draw3DRect(550,200,230,100,true);
		page.setColor(Color.LIGHT_GRAY);
		page.fill3DRect(550,200,230,100,true);

		x[0]=80 ; x[1]=270 ;x[2]=270 ;
		y[0]=430 ; y[1]=430 ;y[2]=550 ;
		page.setColor(Color.LIGHT_GRAY);
		page.fillPolygon(x,y,3);
		page.setColor(Color.BLUE);
		page.drawPolygon(x,y,3);

		x[0]=550 ; x[1]=700 ;x[2]=550 ;
		y[0]=430 ; y[1]=430 ;y[2]=550 ;
		page.setColor(Color.LIGHT_GRAY);
		page.fillPolygon(x,y,3);
		page.setColor(Color.BLUE);
		page.drawPolygon(x,y,3);

		x[0]=10   ; x[1]=80 ;  x[2]=270 ; x[3]=270 ;x[4]=10 ;
		y[0]=530 ;  y[1]=530 ; y[2]=650 ; y[3]=710 ; y[4]=710 ;
		page.setColor(Color.LIGHT_GRAY);
		page.fillPolygon(x,y,5);
		page.setColor(Color.BLUE);
		page.drawPolygon(x,y,5);

		x[0]=780   ; x[1]=780 ; x[2]=550 ; x[3]=550 ; x[4]=700 ;
		y[0]=530 ;  y[1]=710 ;  y[2]=710 ; y[3]=650 ; y[4]=530 ;
		page.setColor(Color.LIGHT_GRAY);
		page.fillPolygon(x,y,5);
		page.setColor(Color.BLUE);
		page.drawPolygon(x,y,5);

		page.setColor(Color.BLACK);
		for (i=0;i<4;i++)
		{
			page.fillRect(160,78+(i*30),100,25);
			page.fillRect(560,78+(i*30),100,25);
			page.fillRect(160,308+(i*30),100,25);
			page.fillRect(560,308+(i*30),100,25);
			page.fillRect(290+(i*30),440,25,100);
			page.fillRect(410+(i*30),440,25,100);
		}

		for (i=0;i<3;i++)
		{
			x[0]=159-(i*13) ; x[1]=260-(i*13) ;  x[2]=250-(i*13) ; x[3]=150-(i*13) ;
			y[0]=489+(i*20) ;  y[1]=550+(i*20) ; y[2]=565+(i*20) ; y[3]=505+(i*20) ; 
			page.fillPolygon(x,y,4);
		}

		for (i=0;i<3;i++)
		{
			x[0]=159+(i*14)+400 ; x[1]=237+(i*14)+400 ;  x[2]=247+(i*14)+400 ; x[3]=170+(i*14)+400 ;
			y[0]=550+(i*20) ;  y[1]=489+(i*20) ; y[2]=505+(i*20) ; y[3]=565+(i*20) ; 
			page.fillPolygon(x,y,4);
		}

		page.setFont(new Font("Courier New",Font.CENTER_BASELINE,12));
		for(i=0;i<ramzorim.length;i++)
		{
			ramzorim[i].drow(page);
			page.setColor(Color.BLACK);
			page.drawString(Integer.toString(i),ramzorim[i].xP[0]+ramzorim[i].diameter/3,ramzorim[i].yP[0]+ramzorim[i].diameter/2);
		}

		page.setColor(Color.BLACK);
		page.drawRect(710,120,80,15);
		x[0]=680 ; x[1]=710 ;x[2]=710 ;
		y[0]=127 ; y[1]=112 ;y[2]=142 ;
		page.drawPolygon(x,y,3);
		page.drawRect(490+250,135,15,30);
		x[0]=500+250 ; x[1]=483+250 ;x[2]=513 +250;
		y[0]=195 ; y[1]=165 ;y[2]=165 ;
		page.drawPolygon(x,y,3);

		page.drawRect(480,610,15,40);
		page.drawRect(450,610,30,15);
		x[0]=180+240 ; x[1]=210+240 ;x[2]=210+240 ;
		y[0]=127+490 ; y[1]=112+490 ;y[2]=142+490 ;
		page.drawPolygon(x,y,3);

		page.drawRect(40,360,80,15);
		x[0]=150 ; x[1]=120 ;x[2]=120 ;
		y[0]=367 ; y[1]=350 ;y[2]=383 ;
		page.drawPolygon(x,y,3);
	}
}
