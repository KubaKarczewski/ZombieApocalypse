import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class LoadingMap {
	
	public static class Point
	{
		private int x,y;
		Point(int pointX, int pointY)
		{
			x = pointX;
			y = pointY;				
		}
	}

	private Vector<Point> wales;
	private Vector<Point> england;
	private Vector<Point> ireland;
	private Vector<Point> scotland;
	private Vector<Point> lines;
	private Vector<Point> cities;
	private int e,s,w,ir,con,ci;

	
	
	LoadingMap()
	{

                //File originalImage = new File("C:\\Users\\jakub\\Documents\\NetBeansProjects\\ZombieApocalypse\\map.png");
                File originalImage = new File("./map.png");
		
		BufferedImage img = null;
		e=0;
		s=0;
		w=0;
		ir=0;
		con=0;
		ci=0;
                wales = new Vector<Point>();
                england = new Vector<Point>();
                ireland = new Vector<Point>();
                scotland = new Vector<Point>();
                lines = new Vector<Point>();
                cities = new Vector<Point>();
		try {
                        img = ImageIO.read(originalImage);

			for(int i=0; i<img.getWidth();i++)
			{
                            for(int j=0; j<img.getHeight(); j++)
                            {

                                Color c = new Color(img.getRGB(i,j));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();

				if(r > 200 && g < 50 && b < 50 )
				{
                                    Point temp = new Point(i,j);
                                    england.addElement(temp);
                                    e++;
				} else if(r < 50 && g < 50 && b > 200)
				{
                                    Point temp = new Point(i,j);
                                    ireland.addElement(temp);
                                    ir++;							
				} else if(r < 50 && g > 200 && b < 50)
				{
                                    Point temp = new Point(i,j);
                                    scotland.addElement(temp);
                                    s++;													
				} else if(r > 200 && g < 50 && b > 200)
				{
                                    Point temp = new Point(i,j);
                                    wales.addElement(temp);
                                    w++;														
				} else if(r < 50 && g < 50 && b < 50)
				{
                                    Point temp = new Point(i,j);
                                    lines.addElement(temp);
                                    con++;			
				} else if( r<50 && g > 200 && b > 200)
				{
                                    Point temp = new Point(i,j);
                                    cities.addElement(temp);
                                    ci++;
				}
                            }
			}
                    } catch (IOException e) 
                    {
                        e.printStackTrace();
                    }			
	}
	
	public void colorizeMap(int[] eng, int[] sco, int[] wal, int[] ire)
	{  
            //File originalImage = new File("C:\\Users\\jakub\\Documents\\NetBeansProjects\\ZombieApocalypse\\map.png");
            File originalImage = new File("./map.png");
            BufferedImage img = null;
            try {
                    img = ImageIO.read(originalImage);
                    BufferedImage colorizedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Color nEnglandC = new Color(eng[0],eng[1],eng[2]);
                    Color nScotlandC = new Color(sco[0],sco[1],sco[2]);
                    Color nIrelandC = new Color(ire[0],ire[1],ire[2]);
                    Color nWalesC = new Color(wal[0],wal[1],wal[2]);
                    Color black = new Color(0,0,0);
                    Color red = new Color(0,0,255);
                    
                    for(int i=0; i<england.size(); i++)
                    {
                        colorizedImage.setRGB(england.elementAt(i).x, england.elementAt(i).y, nEnglandC.getRGB());
                    }
                    for(int i=0; i<scotland.size(); i++)
                    {
                        colorizedImage.setRGB(scotland.elementAt(i).x, scotland.elementAt(i).y, nScotlandC.getRGB());
                    }
                    for(int i=0; i<ireland.size(); i++)
                    {
                        colorizedImage.setRGB(ireland.elementAt(i).x, ireland.elementAt(i).y, nIrelandC.getRGB());
                    }
                    for(int i=0; i<wales.size(); i++)
                    {
                        colorizedImage.setRGB(wales.elementAt(i).x, wales.elementAt(i).y, nWalesC.getRGB());
                    }
                    for(int i=0; i<cities.size(); i++)
                    {
                        colorizedImage.setRGB(cities.elementAt(i).x, cities.elementAt(i).y, red.getRGB());
                    }
										
                    //ImageIO.write(colorizedImage, "png", new File("C:\\Users\\jakub\\Documents\\NetBeansProjects\\ZombieApocalypse\\src\\colorized.png"));
                    ImageIO.write(colorizedImage, "png", new File("./src/colorized.png"));
		} catch (IOException e) 
                {

                    e.printStackTrace();
                }		
	}
}
