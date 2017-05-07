
public class Population extends Constants{
    
    public static class City{
        int country;
        int distance1;
        int distance2;
        
        public City(int country, int dist1, int dist2){
            this.country = country;
            this.distance1 = dist1;
            this.distance2 = dist2;
        }
    }
    
    protected static int totalPeople = TOTAL_POPULATION*1000000;;
    protected static long totalHealthyPeople = TOTAL_POPULATION*1000000;;
    protected static long totalInfectedPeople = 0;
    protected static long totalDeaths = 0;
    protected static boolean apocalypseStarted = false;
    protected static boolean apocalypseFinished = false; 
    protected static int day = 0;
    
    public static Area countries[] = {
        new Area(0,"England",ENG_POPULATION,ENG_SURFACE,1,2,DIST_ENG_WAL_SCO,DIST_ENG_SCO_WAL),
        new Area(1,"Scotland",SCO_POPULATION,SCO_SURFACE,0,3,DIST_SCO_IRL_ENG,DIST_SCO_ENG_IRL),
        new Area(2,"Wales",WAL_POPULATION,WAL_SURFACE,0,3,DIST_WAL_IRL_ENG,DIST_WAL_ENG_IRL),
        new Area(3,"Northern Ireland",IRL_POPULATION,IRL_SURFACE,1,2,DIST_IRL_WAL_SCO,DIST_IRL_SCO_WAL)
    };
    public static City[] cities = {
        new City(0,DIST_LON_SCO,DIST_LON_WAL), new City(0,DIST_MAN_SCO,DIST_MAN_WAL), new City(0,DIST_BIR_SCO,DIST_BIR_WAL), 
        new City(0,DIST_NEW_SCO,DIST_NEW_WAL), new City(1,DIST_EDE_ENG,DIST_EDE_IRL), new City(1,DIST_GLA_ENG,DIST_GLA_IRL), 
        new City(1,DIST_ABE_ENG,DIST_ABE_IRL), new City(2,DIST_CAR_ENG,DIST_CAR_IRL), new City(3,DIST_BEL_SCO,DIST_BEL_WAL)
    };
    
    public static int infectionPercentage[] ={0,0,0,0};
    public static long countriesInfection[] ={0,0,0,0};
    public static long countriesHealthy[] ={ENG_POPULATION,SCO_POPULATION,WAL_POPULATION,IRL_POPULATION};
    
    public static void ResetPopulation(){
        
        countries[0].ResetArea(ENG_POPULATION);
        countries[1].ResetArea(SCO_POPULATION);
        countries[2].ResetArea(WAL_POPULATION);
        countries[3].ResetArea(IRL_POPULATION);
        
        totalPeople = TOTAL_POPULATION*1000000;;
        totalHealthyPeople = TOTAL_POPULATION*1000000;;
        totalInfectedPeople = 0;
        totalDeaths = 0;
        apocalypseStarted = false;
        apocalypseFinished = false; 
        day = 0;      
    }
    
    public static void ExecuteAlgorithmGlobal(){
        if(totalHealthyPeople == 0) apocalypseFinished = true;  
         
        for (int i = 0; i < 4; i++){
            if(countries[i].areaInfected){
                countries[i].ExecuteAlgorithmLocal();
                Migration(i);                
            }
            infectionPercentage[i] = (int)countries[i].infectionPercent; 
            countriesInfection[i] = countries[i].infectedPeople;
            countriesHealthy[i] = countries[i].healthyPeople;
        }

        totalHealthyPeople = countries[0].healthyPeople + countries[1].healthyPeople + countries[2].healthyPeople + countries[3].healthyPeople;
        totalInfectedPeople = countries[0].infectedPeople + countries[1].infectedPeople + countries[2].infectedPeople + countries[3].infectedPeople;

        day++;
	}

    public static void InfectCity(int index){
        int country=0;
        if(index <4) {
            countries[0].StartApocalypse(cities[index].distance1, cities[index].distance2);
            country = 0;
            MyWindow.infectCountry(0);
        }
        else if(index <7) {
            countries[1].StartApocalypse(cities[index].distance1, cities[index].distance2);
            country = 1;
            MyWindow.infectCountry(1);
        }
        else if(index ==7) {
            countries[2].StartApocalypse(cities[index].distance1, cities[index].distance2);
            country = 2;
            MyWindow.infectCountry(2);
        }
        else if(index==8) {
            countries[3].StartApocalypse(cities[index].distance1, cities[index].distance2);
            country = 3;
            MyWindow.infectCountry(3);
        }
        
        for (int i = 0; i <4; i++) {
            if(countries[i].neighbour1==country) countries[i].infectedNeighbour1 = true;
            else if(countries[i].neighbour2==country) countries[i].infectedNeighbour2 = true;
        }
    }
    
    public static void InfectCountry(int index, int infectedBy){
        
         for (int i = 0; i <4; i++) {
            if(countries[i].neighbour1==index) countries[i].infectedNeighbour1 = true;
            else if(countries[i].neighbour2==index) countries[i].infectedNeighbour2 = true;
        }
        
        if(!countries[index].areaInfected) countries[index].StartApocalypse();  
        MyWindow.infectCountry(index);
        
    }
    
    public static void Migration(int index){
        int migrants1=0;
        int migrants2=0;
        int migrants=0;
        if(!countries[index].infectedNeighbour1 || !countries[index].infectedNeighbour2){            
            if(index!=3){
                migrants = (int)countries[index].healthyPeople/100;
                if(!countries[index].infectedNeighbour1 && !countries[index].infectedNeighbour2){
                    if(index==0){
                        migrants1=migrants/2;
                        migrants2 = migrants - migrants1;
                    }
                    else{
                        migrants2 = migrants/4;
                        migrants1 = migrants - migrants2;
                    }
                }
                else if(!countries[index].infectedNeighbour1){
                    migrants1 = migrants;
                    migrants2 = 0;
                }
                else if(!countries[index].infectedNeighbour2){
                    migrants=migrants/2;
                    migrants2=migrants;
                    migrants1=0;
                }
            }
            else{
                migrants = (int)countries[index].healthyPeople/200;
                if(!countries[index].infectedNeighbour1 && !countries[index].infectedNeighbour2){                    
                        migrants1=migrants/2;
                        migrants2 = migrants - migrants1;
                }
                else if(!countries[index].infectedNeighbour1){
                    migrants1 = migrants;
                    migrants2 = 0;
                }
                else if(!countries[index].infectedNeighbour2){
                    migrants=migrants/2;
                    migrants2=migrants;
                    migrants1=0;
                }
            }            
            countries[index].healthyPeople-=migrants;
            countries[countries[index].neighbour1].healthyPeople+=migrants1;
            countries[countries[index].neighbour2].healthyPeople+=migrants2;             
            
        }  
    }
}
