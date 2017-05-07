import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MyWindow extends JFrame implements ChangeListener,  ActionListener, MouseListener
{
	private JLabel lTime, lZombieSpeed, lZero, lFull, lEng, lSco, lIre, lWal, lTotHea, lTotInf;
	private JSlider sZombieSpeed;
	private int time; 		
	private int zombieSpeed; 		
	private MapPanel panel;
	private Timer timer;
	private LoadingMap map ;
	private int city, freeDays;
	private boolean apocalypseStarted, secondClick, freeze;
	private int[] eng;
	private int[] sco;
	private int[] wal;
	private int[] ire;
        private static boolean[] countryInfection = {false,false,false,false};
        private JButton bFreeze, bContinue, bStart;

	
    public MyWindow()
	{
		super("Zombie Apocalypse Simulation");
                // initial data values
                apocalypseStarted = secondClick = freeze = false;
                freeDays = 0;
                zombieSpeed = 2;
                eng = new int[3];
                sco = new int[3];
                wal = new int[3];
                ire = new int[3];
                map = new LoadingMap(); 
                setSize(1500,1000);
		setLayout(null);
                
                //buttons
                bFreeze = new JButton("Zatrzymaj");
                bFreeze.setBounds(100, 100, 100, 50);
                bFreeze.addActionListener(this);
                add(bFreeze);
                
                bContinue = new JButton("Kontynuuj");
                bContinue.setBounds(210, 100, 100, 50);
                bContinue.addActionListener(this);
                add(bContinue);
                
                
                bStart = new JButton("Reset");
                bStart.setBounds(320, 100, 100, 50);
                bStart.addActionListener(this);
                add(bStart);
               
                
		// labels
		lTime = new JLabel("Dni od pierwszego zarażenia: ");
		lTime.setBounds(100, 350, 200, 50);
		add(lTime);
                
                
		lZombieSpeed = new JLabel("Prędkość zombie[km/h]: " + zombieSpeed);
		lZombieSpeed.setBounds(100,200,200,50);
		add(lZombieSpeed);
                
                lZero = new JLabel("Kolor przy 0% zarażonych");
                lZero.setBounds(1150, 140, 200, 50);  
                add(lZero);
                
                lFull = new JLabel("Kolor przy 100% zarażonych");
                lFull.setBounds(1150, 705, 200, 50);
                add(lFull);
                
                lEng = new JLabel("Zarażeni w Anglii : ");
                lEng.setBounds(100, 400, 400, 50);
                add(lEng);
                
                lSco = new JLabel("Zarażeni w Szkocji : ");
                lSco.setBounds(100, 450, 400, 50);
                add(lSco);
                
                lIre = new JLabel("Zarażeni w Irlandii : ");
                lIre.setBounds(100, 500, 400, 50);
                add(lIre);
                
                lWal = new JLabel("Zarażeni w Walii : ");
                lWal.setBounds(100, 550, 400, 50);
                add(lWal);
                
                lTotHea = new JLabel("Liczba zdrowych osob : ");
                lTotHea.setBounds(100, 600, 200, 50);
                add(lTotHea);
                
                lTotInf = new JLabel("Liczba zarazonych osob : ");
                lTotInf.setBounds(100, 650, 200, 50);
                add(lTotInf);
                
                //sliders
		sZombieSpeed = new JSlider(2,10,2);
		sZombieSpeed.setBounds(100,250,200,75);
		sZombieSpeed.setMajorTickSpacing(2);
		sZombieSpeed.setMinorTickSpacing(1);
		sZombieSpeed.setPaintLabels(true);
		sZombieSpeed.setPaintTicks(true);
		sZombieSpeed.addChangeListener(this);
		add(sZombieSpeed);
                
                // panel
		MapPanel panel = new MapPanel();
		panel.setBounds(400, 100, 800, 800);
		add(panel);
		panel.addMouseListener(this);
                
                //timer    
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Refreshing(),0,500);
	
	}
        
        public static void infectCountry(int index)
        {
            countryInfection[index] = true;
        }
    
        public int[] getEngArray()
        {
            return eng;
        }
        
        public int[] getScoArray()
        {
            return sco;
        }
        
        public int[] getWalArray()
        {
            return wal;
        }
        
        public int[] getIreArray()
        {
            return ire;
        }

        public int getEng(int temp)
        {
            return eng[temp];
        }
        
        public int getSco(int temp)
        {
            return sco[temp];
        }
                
        public int getWal(int temp)
        {
            return wal[temp];
        }
                        
        public int getIre(int temp)
        {
            return ire[temp];
        }	
    
	public void setTime(int timeExternal)
	{
		time = timeExternal;
	}
        
	public int getZombieSpeed()
	{
		return zombieSpeed; 
	}
	public int getCity()
	{
		return city;
	}


	class MapPanel extends JPanel
	{
		private BufferedImage map, gradient;
		
                @Override
		protected void paintComponent(Graphics g)
		{
			
			super.paintComponent(g);
			try
			{
				//map = ImageIO.read(new File("C:\\Users\\jakub\\Documents\\NetBeansProjects\\ZombieApocalypse\\src\\colorized.png"));
				//gradient = ImageIO.read(new File("C:\\Users\\jakub\\Documents\\NetBeansProjects\\ZombieApocalypse\\src\\gradient.png"));
                                map = ImageIO.read(new File("./src/colorized.png"));
				gradient = ImageIO.read(new File("./src/gradient.png"));
			} catch (Exception ex)
			{
				//ex.printStackTrace();
			}
			g.drawImage(map,150,0,null);
			g.drawImage(gradient, 700,60,null);
	
                }
	}

    class Refreshing extends TimerTask
    {
        @Override
        public void run()
        {
            if(!Population.apocalypseFinished)
            { 
            if(!freeze)
            {
            Population.ExecuteAlgorithmGlobal();
            }
            zombiesToColor();
            try{
            map.colorizeMap(getEngArray(),getScoArray(),getWalArray(),getIreArray());
            }
            catch(NullPointerException x) {
                x.printStackTrace();
            }
            updatingGUI(handlingDays(Population.day));
            repaint();
            }
        }
    }
	

	@Override
	public void stateChanged(ChangeEvent e) 
	{
            Object source = e.getSource();
            if(source == sZombieSpeed && !apocalypseStarted)
            {
		zombieSpeed = sZombieSpeed.getValue();
		lZombieSpeed.setText("Prędkosc zombie[km/h]: " + zombieSpeed);
                Area.SetZombieSpeed(zombieSpeed);
            }
	
	}

	@Override
	public void actionPerformed(ActionEvent e) 
        {
                if(e.getSource() == bFreeze)
                {
                    freeze = true;
                }
                
                if(e.getSource() == bContinue)
                {
                    freeze = false;
                }
                
                if(e.getSource() == bStart)
                {
                    apocalypseStarted = secondClick = false;
                    Population.ResetPopulation();
                        zombiesToColor();
                        map.colorizeMap(getEngArray(),getScoArray(),getWalArray(),getIreArray());
                        updatingGUI(handlingDays(Population.day));
                        repaint();     
                }	
	}
	
	public void zombiesToColor()
	{
            eng[2]=sco[2]=ire[2]=wal[2]=0;
            if(countryInfection[0]){
                eng[0] = 255;
                eng[1] = 0;
            }
            else if(Population.infectionPercentage[0]<50)
            {
                eng[0]= 255/50 * Population.infectionPercentage[0];
                eng[1]=255;
                
            } else if(Population.infectionPercentage[0]>50)
            {
                eng[1]= 255/50 * (100-Population.infectionPercentage[0]);
                eng[0]=255;               
            } else if(Population.infectionPercentage[0] == 50)
            {
                eng[0]=255;
                eng[1]=255;
            }
            if(countryInfection[1]){
                sco[0] = 255;
                sco[1] = 0;
            }
            else if(Population.infectionPercentage[1]<50)
            {
                sco[0]= 255/50 * Population.infectionPercentage[1];
                sco[1]=255;
                
            } else if(Population.infectionPercentage[1]>50)
            {
                sco[1]= 255/50 * (100-Population.infectionPercentage[1]);
                sco[0]=255;               
            } else if(Population.infectionPercentage[1] == 50)
            {
                sco[0]=255;
                sco[1]=255;
            }
            if(countryInfection[2]){
                wal[0] = 255;
                wal[1] = 0;
            }           
            else if(Population.infectionPercentage[2]<50)
            {
                wal[0]= 255/50 * Population.infectionPercentage[2];
                wal[1]=255;
                
            } else if(Population.infectionPercentage[2]>50)
            {
                wal[1]= 255/50 * (100-Population.infectionPercentage[2]);
                wal[0]=255;               
            } else if(Population.infectionPercentage[2] == 50)
            {
                wal[0]=255;
                wal[1]=255;
            }
            if(countryInfection[3]){
                ire[0] = 255;
                ire[1] = 0;
            }                       
            else if(Population.infectionPercentage[3]<50)
            {
                ire[0]= 255/50 * Population.infectionPercentage[3];
                ire[1]=255;
                
            } else if(Population.infectionPercentage[3]>50)
            {
                ire[1]= 255/50 * (100-Population.infectionPercentage[3]);
                ire[0]=255;               
            } else if(Population.infectionPercentage[3] == 50)
            {
                ire[0]=255;
                ire[1]=255;
            }
            
            	for (int i = 0; i < 4; i++) {
                    countryInfection[i] = false;
                }
	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int delta = 30;
		int x = e.getX();
		int y = e.getY();

                if(!secondClick)
                {
                    
                    if((x>528 && x<528+delta)&&(y>584 && y<584+delta))
                    {
                            apocalypseStarted = true;
                            city = 0;
                            Population.InfectCity(city);
                    } else if((x>370 && x<370+delta)&&(y>575 && y<575+delta))
                    {
                            apocalypseStarted = true;
                            city = 7;
                            Population.InfectCity(city);
                    } else if((x>420 && x<420+delta)&&(y>512 && y<512+delta))
                    {
                            apocalypseStarted = true;
                            city = 2;
                           Population.InfectCity(city);
                    } else if((x>408 && x<408+delta)&&(y>444 && y<444+delta))
                    {
                            apocalypseStarted = true;
                            city = 1;
                            Population.InfectCity(city);
                    } else if((x>420 && x<420+delta)&&(y>295 && y<295+delta))
                    {
                            apocalypseStarted = true;
                            city = 3;
                            Population.InfectCity(city);
                    } else if((x>345 && x<345+delta)&&(y>260 && y<260+delta))
                    {
                            apocalypseStarted = true;
                            city = 4;
                            Population.InfectCity(city);  
                    } else if((x>310 && x<310+delta)&&(y>270 && y<270+delta))
                    {
                            apocalypseStarted = true;
                            city = 5;
                            Population.InfectCity(city);
                    } else if((x>395 && x<395+delta)&&(y>160 && y<160+delta))
                    {
                            apocalypseStarted = true;
                            city = 6;
                            Population.InfectCity(city);
                    } else if((x>245 && x<245+delta)&&(y>360 && y<360+delta))
                    {
                            apocalypseStarted = true;
                            city = 8;
                            Population.InfectCity(city);
                    }
                }
                if(apocalypseStarted)
                {
                    secondClick = true;
                }
   
                
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
        
        public void updatingGUI( int days)
        {
            lTime.setText("Dni od pierwszego zarażenia: " + days);
            lEng.setText("Angliia : Zarażonych: " + Population.countriesInfection[0] + " ( " + Population.infectionPercentage[0] +
                    " % ) , Zdrowych: " + Population.countriesHealthy[0] );
            lSco.setText("Szkocja : Zarażonych: " + Population.countriesInfection[1] + " ( " + Population.infectionPercentage[1] +
                    " % ) , Zdrowych: " + Population.countriesHealthy[1]);
            lIre.setText("Irlandia : Zarażonych: " + Population.countriesInfection[3] + " ( " + Population.infectionPercentage[3] +
                    " % ) , Zdrowych: " + Population.countriesHealthy[3]);
            lWal.setText("Walia : Zarażonych:  " + Population.countriesInfection[2] + " ( " + Population.infectionPercentage[2] + 
                    " % ) , Zdrowych: " + Population.countriesHealthy[2]);
            lTotHea.setText("Liczba zdrowych : " + Population.totalHealthyPeople);
            lTotInf.setText("Liczba chorych : " + Population.totalInfectedPeople);
        }
        
        public int handlingDays( int days)
        {
            if(!apocalypseStarted)
            {
                freeDays = days;
                return 0;
                
            }else{
                return (days - freeDays);
            }
        }
	
	public static void main(String[] args)
	{
		MyWindow window = new MyWindow();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
              	
	}
	
	
}


	