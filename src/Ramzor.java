import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Created on Mimuna 5767  upDate on Tevet 5770 
 */

/**
 * @author לויאן
 */

class Ramzor 
{
	int numOfLights;
	int xP[],yP[];
	int diameter;
	Color colorLight[];

	Ramzor(int num,int dia,int x0,int y0)
	{
		numOfLights=num;
		diameter=dia;
		xP=new int[numOfLights];
		yP=new int[numOfLights];
		colorLight=new Color[numOfLights];
		colorLight[0]=Color.LIGHT_GRAY;
		xP[0]=x0; yP[0]=y0;
	}

	Ramzor(int num,int dia,int x0,int y0,int x1,int y1)
	{
		numOfLights=num;
		diameter=dia;
		xP=new int[numOfLights];
		yP=new int[numOfLights];
		colorLight=new Color[numOfLights];
		colorLight[0]=Color.RED;
		colorLight[1]=Color.GRAY;
		xP[0]=x0; yP[0]=y0;
		xP[1]=x1; yP[1]=y1;
	}

	Ramzor(int num,int dia,int x0,int y0,int x1,int y1,int x2,int y2)
	{
		numOfLights=num;
		diameter=dia;
		xP=new int[numOfLights];
		yP=new int[numOfLights];
		colorLight=new Color[numOfLights];
		colorLight[0]=Color.RED;
		colorLight[1]=Color.LIGHT_GRAY;
		colorLight[2]=Color.LIGHT_GRAY;
		xP[0]=x0; yP[0]=y0;
		xP[1]=x1; yP[1]=y1;
		xP[2]=x2; yP[2]=y2;
	}

	void drow(Graphics page)
	{
		for(int i=0;i<numOfLights;i++)
		{
			page.setColor(colorLight[i]);
			page.fillOval(xP[i],yP[i],diameter,diameter);
		}
	}
}
