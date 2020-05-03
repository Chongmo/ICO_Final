package PVC.Algorithmes;

import PVC.Data.CityData;
import PVC.Definitions.City;
import PVC.Definitions.Parametre;
import PVC.Definitions.Route;

import java.util.*;

public class GeneticAlgorithm extends Algorithme {

    private static int nbCities;
    private static int generationSize;
    private static ArrayList<City> cities = new ArrayList<City>();
    private float pCrossover;
    private float pMutation;
    private int tournamentSize;
    private int maxGeneration;
    private ArrayList<Route> initialPopulation = new ArrayList<>();
    private ArrayList<Route> population = new ArrayList<>();

    private ArrayList<Float> averageDistanceOfEachGeneration = new ArrayList<>();
    private ArrayList<Float> bestDistanceOfEachGeneration = new ArrayList<>();
    private HashMap<String,Object> Configuration = new HashMap<String,Object>();


    /*
     * public GeneticAlgorithm(){ initialPopulation = generateRandomGeneration();
     * for (Route r:initialPopulation) { population.add(new Route(r)); }
     * maxGeneration = 15; generationSize = 30; tournamentSize = 3; nbCities =
     * getCities().size(); pCrossover = 0.9; pMutation = 0.01;
     *
     * averageDistanceOfEachGeneration = new ArrayList<Float>();
     * bestDistanceOfEachGeneration = new ArrayList<Float>(); }
     */
    public GeneticAlgorithm(Route initr) {
        super(initr);
        maxGeneration = 150;
        generationSize = 30;
        tournamentSize = 3;
        nbCities = initr.getCities().size();
        pCrossover = (float) 0.9;
        pMutation = (float) 0.01;

        averageDistanceOfEachGeneration = new ArrayList<Float>();
        bestDistanceOfEachGeneration = new ArrayList<Float>();
        initialPopulation = generateRandomGeneration(initr);
        for (Route r : initialPopulation) {
            population.add(new Route(r));
        }
    }
    public GeneticAlgorithm(Route actualRoute, ArrayList<Parametre> Conf) {
        super(actualRoute);
        maxGeneration = 150;
        generationSize = 30;
        tournamentSize = 3;
        nbCities = actualRoute.getCities().size();
        
        for (Parametre p: Conf) {
        	this.Configuration.put(p.getName(), (float)p.getValue());
        }
        this.pCrossover = (float)this.Configuration.get("pCrossover");
        this.pMutation = (float)this.Configuration.get("pMutation");
        averageDistanceOfEachGeneration = new ArrayList<Float>();
        bestDistanceOfEachGeneration = new ArrayList<Float>();
        initialPopulation = generateRandomGeneration(actualRoute);
        for (Route r : initialPopulation) {
            population.add(new Route(r));
        }
    }
    

    public GeneticAlgorithm(Route initr, int iter) {
        this(initr);
        maxGeneration = iter;
    }

    public static ArrayList<City> getCities() {
        return cities;
    }

    public static void setCities(ArrayList<City> cities) {
        GeneticAlgorithm.cities = cities;
    }

    private static ArrayList<Route> generateRandomGeneration(Route initr) {

        ArrayList<Route> firstGeneration = new ArrayList<Route>();

        for (int i = 0; i < generationSize; i++) {
            firstGeneration.add(new Route(initr.getCities()));
        }
        return firstGeneration;
    }

    private static float getAverageDistance(ArrayList<Route> population) {
        float sumDistances = 0;

        for (int i = 0; i < generationSize; i++) {
            sumDistances += population.get(i).getTotalDistance();
        }
        return sumDistances / generationSize;
    }

    private static ArrayList<Route> pickNRandomRoute(ArrayList<Route> pop, int n) {
        Random r = new Random();
        ArrayList<Route> temps = new ArrayList<>(pop);
        Collections.shuffle(temps);
        ArrayList<Route> randomRoute = new ArrayList<Route>();
        for (int i = 0; i < n; i++) {
            randomRoute.add(temps.get(i));
        }
        return randomRoute;
    }

    public static void main(String[] args) {
        CityData Data = new CityData(100);
        Route initRoute = new Route(Data.getCities());
        long millis = System.currentTimeMillis();

        GeneticAlgorithm ag = new GeneticAlgorithm(initRoute);
        System.out.println(ag.getBestRoute());

        ag.runtest();
        long milli = System.currentTimeMillis();
	    long executiontime = milli-millis;
	    System.out.println(ag.getBestRoute());
		System.out.println("Distance"+ag.getBestRoute().getTotalDistance());
		System.out.println("Executiontime: "+ executiontime +"ms");
    }

    public void setPopulation(ArrayList<Route> population) {

        initialPopulation = population;
        ArrayList<Route> pop = new ArrayList<Route>();
        for (Route r : initialPopulation) {
            pop.add(new Route(r));
        }
        this.population = pop;
    }

    public void setPMutation(float pMutation) {
        this.pMutation = pMutation;
    }

    public void setPCrossover(float pCrossover) {
        this.pCrossover = pCrossover;
    }

    public void setMaxGeneration(int maxGeneration) {
        this.maxGeneration = maxGeneration;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public ArrayList<Float> getAverageDistanceOfEachGeneration() {
        return averageDistanceOfEachGeneration;
    }

    public ArrayList<Float> getBestDistanceOfEachGeneration() {
        return bestDistanceOfEachGeneration;
    }

    public void run() {
        for (int i = 0; i < maxGeneration; i++) {
            population = createNewGeneration();
            averageDistanceOfEachGeneration.add(getAverageDistance(population));
            bestDistanceOfEachGeneration.add(choseTheBest(population, 1).get(0).getTotalDistance());
            //System.out.println(i);
        }
    }

    private ArrayList<Route> createNewGeneration() {

        ArrayList<Route> newGen = new ArrayList<Route>();
        ArrayList<Route> populationSelected = new ArrayList<Route>();

        for (int i = 0; i < generationSize; i++) {
            populationSelected.add(tournamentSelection());
        }

        int reste = generationSize % 2;
        if (reste == 1) {
            newGen.add(populationSelected.get(populationSelected.size() - 1));
        }
        // System.out.println("NewGen avant cross et mut : \n"+populationSelected);

        // Crossover
        for (int i = 0; i < generationSize; i = i + 2) {

            if (Math.random() < pCrossover) {
                ArrayList<Route> crossover = new ArrayList<Route>(cross(populationSelected.get(i), populationSelected.get(i + 1)));
                newGen.add(crossover.get(0));
                newGen.add(crossover.get(1));
            } else {
                newGen.add(populationSelected.get(i));
                newGen.add(populationSelected.get(i + 1));
            }
        }
        for (int i = 0; i < generationSize; i++) {
        	HashMap<City,Integer>cities=new HashMap<City,Integer>();
        	for (City c : newGen.get(i).getCities()) {
        		cities.put(c, 0);
        	}
        	for (City c: initialPopulation.get(0).getCities()){
        		cities.putIfAbsent(c, 0);
        	}
        	Route newR=new Route(new ArrayList<City>(cities.keySet()),true);
        	newGen.set(i, newR);
        	
        }
        // Mutation
        for (int i = 0; i < generationSize; i++) {
            if (Math.random() < pMutation) {
                Route routei = new Route(newGen.get(i));
                newGen.set(i, mutate(routei));
            }
        }
        // System.out.println("newGen : \n"+newGen);
        return selectNewGeneration(population, newGen);
    }

    private ArrayList<Route> selectNewGeneration(ArrayList<Route> pop, ArrayList<Route> newGen) {

        newGen.addAll(pop);
        // System.out.println("newGen+population : \n"+newGen);
        return choseTheBest(newGen, generationSize);
    }

    private Route mutate(Route route) {

        int nbAleas1 = (int) (Math.random() * nbCities);
        int nbAleas2 = (int) (Math.random() * nbCities);

        while (nbAleas1 == nbAleas2) {
            nbAleas2 = (int) (Math.random() * nbCities);
        }

        ArrayList<City> cities = new ArrayList<City>();

        cities = route.getCities();

        Collections.swap(cities, nbAleas1, nbAleas2);

        return new Route(cities, true);
    }

    private ArrayList<Route> cross(Route route1, Route route2) {

        Random random = new Random();
        int breakpoint = random.nextInt(nbCities);

        ArrayList<Route> children = new ArrayList<Route>();

        ArrayList<City> parent1 = new ArrayList<City>(route1.getCities());
        ArrayList<City> parent2 = new ArrayList<City>(route2.getCities());

        ArrayList<City> child1 = new ArrayList<City>(route1.getCities());
        ArrayList<City> child2 = new ArrayList<City>(route2.getCities());

		/*parent1 = route1.getCities();
		parent2 = route2.getCities();

		child1 = route1.getCities();
		child2 = route2.getCities();*/

        City newCity1, newCity2;

        for (int i = 0; i < breakpoint; i++) {
            newCity1 = parent1.get(i);
            newCity2 = parent2.get(i);
            child1.set(i, newCity2);
            child2.set(i, newCity1);
        }
        LinkedHashSet<City> set1 = new LinkedHashSet<City>(child1);
        LinkedHashSet<City> set2 = new LinkedHashSet<City>(child1);
        for (City c : parent1) {
            set1.add(c);
            set2.add(c);
        }
        children.add(new Route(child1, true));
        children.add(new Route(child2, true));

        return children;
    }

    // Function to select the best route between several route
    private Route tournamentSelection() {

        ArrayList<Route> selected = pickNRandomRoute(this.population, tournamentSize);

        return choseTheBest(selected, 1).get(0);
    }

    private ArrayList<Route> choseTheBest(ArrayList<Route> population, int n) {
        ArrayList<Route> temps = new ArrayList<Route>(population);
        temps.sort(new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                float a = o1.getTotalDistance();
                float b = o2.getTotalDistance();
                return Float.compare(a, b);
            }
        });
        this.bestRoute = temps.get(0);
        return new ArrayList<>(temps.subList(0, n));
    }

    public void reset() {
        for (int i = 0; i < generationSize; i++) {
            population.set(i, initialPopulation.get(i));
        }
        averageDistanceOfEachGeneration = new ArrayList<>();
        bestDistanceOfEachGeneration = new ArrayList<>();
    }

    public void printProperties() {
        System.out.println("-------Genetic Algorithm Properties-------");
        System.out.println("Number of Cities:           	" + nbCities);
        System.out.println("Population Size:    		" + generationSize);
        System.out.println("Max. Generation:		" + maxGeneration);
        System.out.println("Nb. route in 1 tournament :	" + tournamentSize);
        System.out.println("Crossover Rate:     		" + (pCrossover * 100) + "%");
        System.out.println("Mutation Rate:      		" + (pMutation * 100) + "%");
    }

    public void printResults() {

        System.out.println("--------Genetic Algorithm Results---------");
        System.out.println("Average Distance of First Generation:  " +
                getAverageDistanceOfEachGeneration().get(0));

        System.out.println("Average Distance of Last Generation:   " +
                getAverageDistanceOfEachGeneration().get(maxGeneration - 1));

        System.out.println("Best Distance of First Generation:     " +
                getBestDistanceOfEachGeneration().get(0));

        System.out.println("Best Distance of Last Generation:      " +
                getBestDistanceOfEachGeneration().get(maxGeneration - 1));

    }

    @Override
    public void runtest() {
        for (int i = 0; i < maxGeneration; i++) {
            population = createNewGeneration();
            averageDistanceOfEachGeneration.add(getAverageDistance(population));
            bestDistanceOfEachGeneration.add(choseTheBest(population, 1).get(0).getTotalDistance());
            //System.out.println(i);
        }
    }
}
