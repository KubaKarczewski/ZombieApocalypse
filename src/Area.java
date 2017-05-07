public class Area extends Population{	
    private final int countryIndex;
    private int people;
    public long healthyPeople;
    public long infectedPeople;
    public final String name;
    private final long surface;
    public boolean areaInfected;
    public double zombieEncounters;
    public long humanEncounters;
    private double humanCollector;
    private double zombieCollector;
    private double birthCollector;
    private double encounterCollector;
    public double infectionPercent;
    private double infectionRadius;
    private double infectionSpeed;
    
    public final int neighbour1;
    public final int neighbour2;
    private int distToNeighbour1;
    private int distToNeighbour2;
    public boolean infectedNeighbour1;
    public boolean infectedNeighbour2;
    
    protected double biteChance;
    protected double zombieKillChance;
    protected double naturalDeathChance;
    protected double humanKillChance;
    private boolean naturalDeathStability;
    protected double avoidChance;
    
    public int killedPeople;
    public int humanChange;
    public int zombieChange;
    
    private static int zombieSpeed=2;
	
    Area(int index,String name, int people, int surface, int neighbour1, int neighbour2,int distToNeighbour1,int distToNeighbour2){
	this.countryIndex = index;
        this.name = name;
	this.people = people*1000000;
	this.healthyPeople = people*1000000;
	this.infectedPeople = 0;
        areaInfected = false;
	this.surface = surface;
        zombieEncounters = 0;
        humanEncounters = 0;
        humanCollector = 0;
        zombieCollector = 0;
        birthCollector = 0;
        encounterCollector = 0;
        infectionPercent = 0;
        infectionRadius = 1;
        this.neighbour1 = neighbour1;
        this.neighbour2 = neighbour2;
        this.distToNeighbour1=distToNeighbour1;
        this.distToNeighbour2=distToNeighbour2;
        infectedNeighbour1 = false;
        infectedNeighbour2 = false;
        killedPeople=0;
        humanChange=0;
        zombieChange=0;
        infectionSpeed = 0;
        
        avoidChance = START_AVOID_CHANCE;
        naturalDeathStability = false;
        humanKillChance = START_HUMAN_KILL_CHANCE;
        naturalDeathChance = START_NATURAL_DEATH_CHANCE;
        zombieKillChance = START_ZOMBIE_KILL_CHANCE;
        biteChance = START_BITE_CHANCE;
       
    }
    
    public void StartApocalypse(){
        this.areaInfected = true;
        this.infectedPeople = 1;        
    }
    
    public void StartApocalypse(int distCityCountry1, int distCityCountry2){
        this.areaInfected = true;
        this.infectedPeople = 1;
        this.distToNeighbour1=distCityCountry1;
        this.distToNeighbour2=distCityCountry2;
    }
    
    public void ResetArea(int people){
        this.people = people*1000000;
	this.healthyPeople = people*1000000;
	this.infectedPeople = 0;
        areaInfected = false;
        zombieEncounters = 0;
        humanEncounters = 0;
        humanCollector = 0;
        zombieCollector = 0;
        birthCollector = 0;
        encounterCollector = 0;
        infectionPercent = 0;
        infectionRadius = 1;
        killedPeople=0;
        humanChange=0;
        zombieChange=0;
        infectionSpeed = 0;
        avoidChance = START_AVOID_CHANCE;
        naturalDeathStability = false;
        humanKillChance = START_HUMAN_KILL_CHANCE;
        naturalDeathChance = START_NATURAL_DEATH_CHANCE;
        zombieKillChance = START_ZOMBIE_KILL_CHANCE;
        biteChance = START_BITE_CHANCE;
        infectedNeighbour1 = false;
        infectedNeighbour2 = false;
    }
    
    public static void SetZombieSpeed(int zSpeed){
        zombieSpeed = zSpeed;
    }
	
    public void ExecuteAlgorithmLocal(){
        double infectionArea = infectionRadius*infectionRadius*Math.PI;
	zombieEncounters = (double)infectedPeople*healthyPeople*(infectionArea/(surface*1000))/(double)(avoidChance*5000); 
        if(zombieEncounters > 10*infectedPeople) zombieEncounters = 10*infectedPeople;
        if(zombieEncounters > healthyPeople/10.0) zombieEncounters = healthyPeople/10.0;
        
        if (zombieEncounters < 10){
            encounterCollector+=zombieEncounters;
            zombieEncounters+=(int)encounterCollector;
            encounterCollector -=(int)encounterCollector;
        }
	humanEncounters = healthyPeople*healthyPeople/(surface*10000000);
        if(humanEncounters > healthyPeople) humanEncounters = healthyPeople;
                
        double bittenPpl=zombieEncounters*biteChance;
        double bornPpl=healthyPeople*BIRTH_CHANCE;
        double humanKillPpl=humanEncounters*humanKillChance;
        double naturalKillPpl=healthyPeople*naturalDeathChance;
        double killedZombies = zombieEncounters*zombieKillChance;
      
        if(bittenPpl<5 || humanKillPpl<5 || naturalKillPpl<5) humanCollector += ( bittenPpl%1 + humanKillPpl%1 + naturalKillPpl%1);
                
        killedPeople = (int)bittenPpl + (int)humanKillPpl + (int)naturalKillPpl + (int)humanCollector;
                 
        if(bittenPpl<5 || killedZombies<5) zombieCollector += (bittenPpl%1 - killedZombies%1);
        
                 
        if(bornPpl < 5) birthCollector += bornPpl%1;
                
        humanChange = (int)bornPpl + (int)birthCollector - killedPeople;
        zombieChange = (int)bittenPpl - (int)killedZombies + (int)zombieCollector;
                
        healthyPeople +=humanChange;
        infectedPeople += zombieChange;
        totalDeaths+=killedPeople;
        people+=humanChange+zombieChange;

        humanCollector -= (int)humanCollector;
        zombieCollector -= (int)zombieCollector;
        birthCollector -= (int)birthCollector;
        
        infectionPercent = infectedPeople*100/(double)(healthyPeople+infectedPeople);
        
        if(infectionRadius >=distToNeighbour1 && !infectedNeighbour1) {
            Population.InfectCountry(neighbour1, countryIndex);            
        }
        
        if(infectionRadius >=distToNeighbour2 && !infectedNeighbour2) {
            Population.InfectCountry(neighbour2, countryIndex);            
        }
        
        infectionSpeed = (zombieSpeed/2.0 + people/(surface*1000.0*40));
        if(infectionSpeed>zombieSpeed*1.5) infectionSpeed = zombieSpeed*1.5;
        if(infectionSpeed < 2) infectionSpeed = 2;
        if(!infectedNeighbour1||!infectedNeighbour2 || infectionArea < surface*1000) infectionRadius +=infectionSpeed;
        
        if(biteChance > MID_BITE_CHANCE) biteChance += -0.01;
        else if(biteChance > MIN_BITE_CHANCE) biteChance += -0.001;
                
        if(zombieKillChance < MID_ZOMBIE_KILL_CHANCE) zombieKillChance += 0.02;
        else if(zombieKillChance < MAX_ZOMBIE_KILL_CHANCE) zombieKillChance += 0.001;
            
        if(humanKillChance < MID_HUMAN_KILL_CHANCE) humanKillChance += 0.002;
        else if(humanKillChance < MAX_HUMAN_KILL_CHANCE) humanKillChance += 0.0004;
            
        if(naturalDeathChance < MAX_NATURAL_DEATH_CHANCE && !naturalDeathStability) naturalDeathChance += 0.001;
        else if(naturalDeathChance > MIN_NATURAL_DEATH_CHANCE){
            naturalDeathChance += -0.0001;
            naturalDeathStability = true;
        }    
        
        if(avoidChance < MID_AVOID_CHANCE) avoidChance += 0.1;
        else if(avoidChance >= MID_AVOID_CHANCE && avoidChance < MAX_AVOID_CHANCE) avoidChance += 0.02;
        
    }	
}
