package PVC.Algorithmes;

import PVC.Data.CityData;
import PVC.Definitions.City;
import PVC.Definitions.Parametre;
import PVC.Definitions.Route;
import PVC.Definitions.Tuple;
import PVC.Definitions.Voisin;

import java.util.ArrayList;
import java.util.HashMap;

public class Tabou extends Algorithme {
    private ArrayList<Tuple<Integer, Integer>> T = new ArrayList<>();
    private HashMap<String,Object> Configuration = new HashMap<String,Object>();
    private float Tlength = 5;
    private float coefficient = 0.8f;
    public Tabou() {
        super();
    }
    public Tabou(Route actualRoute) {
        super(actualRoute);
    }
    public Tabou(Route actualRoute, ArrayList<Parametre> Conf) {
        super(actualRoute);
        for (Parametre p: Conf) {
        	this.Configuration.put(p.getName(), (float)p.getValue());
        }
        this.Tlength = (float)this.Configuration.get("T-length");
        this.coefficient = (float)this.Configuration.get("Coeff");
    }

    public Tabou(ArrayList<City> cities) {
        super(new Route(cities));
    }

    public Tabou(Route actualRoute, int it) {
        super(actualRoute, it);
    }

    public static void main(String[] args) {
        CityData Data = new CityData(100);
        Route initRoute = new Route(Data.getCities());
        long millis = System.currentTimeMillis();

		
		  Tabou t = new Tabou(initRoute, 100); 
		  System.out.println(t.getBestRoute());
		  
		  t.runtest(); 
		  long milli = System.currentTimeMillis();
	      long executiontime = milli-millis;
	        
		  System.out.println(t.getBestRoute());
		  System.out.println("Distance"+t.getBestRoute().getTotalDistance());
		  System.out.println("Executiontime: "+ executiontime +"ms");
		   
		/*
		 * Recuit r = new Recuit(initRoute, 100, 10.0f, 0.5f);
		 * System.out.println(r.getBestRoute());
		 * 
		 * r.runtest();
		 */
		 
        
		/*
		 * GeneticAlgorithm g = new GeneticAlgorithm(initRoute); g.runtest();
		 * System.out.println("The city number: " +
		 * g.getBestRoute().getCities().size()); System.out.println("The Best Route: " +
		 * g.getBestRoute()); System.out.println("Min Distance : " +
		 * g.getBestRoute().getTotalDistance());
		 */
		  }

    @Override
    public void runtest() {
        ArrayList<Double> logger = new ArrayList<>();

        while (this.iter-- > 0) {
            //System.out.println(this.iter);
            //logger.add(this.bestRoute.getTotalDistance());
            this.actualRoute = this.getMinRoute();
            this.upgradeT(this.actualRoute.getTransfert());
            if (this.actualRoute.getTotalDistance() < this.bestRoute.getTotalDistance()) {
                this.bestRoute = this.actualRoute;
            }
        }
    }

    protected void upgradeT(Tuple<Integer, Integer> trans) {
        if (T.size() > (int)this.Tlength) {
            T.remove(0);
        }
        T.add(new Tuple<>(trans));
    }

    private Route getMinRoute() {
        Voisin actualVoisin = new Voisin(this.getActualRoute(),this.coefficient);
        return actualVoisin.getMinRoute(this.getT(), this.getActualRoute());
    }

    public ArrayList<Tuple<Integer, Integer>> getT() {
        return T;
    }
    
}