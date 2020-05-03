package PVC.Algorithmes;

import PVC.Data.CityData;
import PVC.Definitions.Route;
import PVC.Definitions.Voisin;
import PVC.Utils.Line;
//import PVC.Utils.Plot;
//import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Algorithme {
    protected int iter = 150;
    protected Route bestRoute;
    protected Route actualRoute;
    protected ArrayList<Line> lines = new ArrayList<>();
    public Algorithme() {}
    public Algorithme(Route actualRoute) {
        this.actualRoute = new Route(actualRoute);
        this.bestRoute = new Route(actualRoute);
        //this.iter = 10;
    }

    public Algorithme(Route actualRoute, int it) {
        this(actualRoute);
        this.iter = it;
    }

    public static void main(String[] args) throws IOException {
        CityData Data = new CityData(50);
        Route initRoute = new Route(Data.getCities());
    }

    abstract public void runtest();

    public void plot() throws IOException  {
//        Plot display = new Plot(this.lines, "iterations", "distance", "Tabou");
//        display.show();
    }
    public void setInitRoute(String s) {
    	this.actualRoute=new Route(s);
    	this.bestRoute=new Route(s);
    }

    public Route getActualRoute() {
        return actualRoute;
    }

    public Route getBestRoute() {
        return bestRoute;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    protected Route getRandomRoute() {
        Voisin actualVoisin = new Voisin(this.actualRoute);
        return actualVoisin.getRandomRoute();
    }

    public String getResult() {
        return String.format("Route : %s\nDistance : %f", this.getBestRoute().toString(), this.getBestRoute().getTotalDistance());
    }

}