package PVC.Definitions;

import java.util.ArrayList;
import java.util.Random;

public class Voisin {
    private Route initRoute;
    private float coeff;

    public Voisin(Route r) {
        this.initRoute = r;
        coeff = 0.8f;
    }
    public Voisin(Route r, float coeff) {
        this.initRoute = r;
        this.coeff =coeff;
    }

    public Route getMinRoute(ArrayList<Tuple<Integer, Integer>> T, Route actualRoute) {
        ArrayList<Route> voisins = new ArrayList<>();

        int citySize = this.initRoute.getCities().size();
        for (int i = 0; i < citySize; i++) {
            for (int j = i + 1; j < citySize; j++) {
                voisins.add(new Route(this.initRoute, i, j));
            }
        }
        Route minRoute = new Route(voisins.get(0));
        for (Route r : voisins) {
            if (r.getTotalDistance() < minRoute.getTotalDistance() && (!T.contains(r.getTransfert()) || A(actualRoute, r))) {
                minRoute = new Route(r);
            }
        }
        return minRoute;
    }

    public Route getRandomRoute() {
        Random rand = new Random();
        int citySize = this.initRoute.getCities().size();
        int start = rand.nextInt(citySize - 1);
        int end = start + 1 + rand.nextInt(citySize - 1 - start);
        return new Route(this.initRoute, start, end);
    }

    public boolean A(Route actualRoute, Route candidate) {
        double coe = this.coeff;
        return candidate.getTotalDistance() <= coe * actualRoute.getTotalDistance();
    }

}
