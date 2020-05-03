package PVC.Definitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PVC.Data.CityData;

public class Route implements Comparable<Route> {
    private ArrayList<City> cities = new ArrayList<>();
    private Tuple<Integer, Integer> transfert = new Tuple<>(0, 0);

    public Route(ArrayList<City> cities, Boolean noShuffle) {
        this.cities.addAll(cities);
    }

    public Route(ArrayList<City> cities) {
        this.cities.addAll(cities);
        Collections.shuffle(this.cities);
    }

    public Route(Route route) {
        this.cities.addAll(route.getCities());
        this.transfert = route.transfert;
    }

    public Route(Route route, int a, int b) {
        this(route);
        this.cities.set(a, route.getCities().get(b));
        this.cities.set(b, route.getCities().get(a));
        this.transfert = new Tuple<>(a, b);
    }
    
    public Route(String s) {
    	for(String sub:getSubUtil(s)) {
    		String[] sublist=sub.split("\\|");
    		float longe=Float.parseFloat(sublist[1].replace(',', '.'));
    		float lage=Float.parseFloat(sublist[2].replace(',', '.'));
    		String name=sublist[0];
    		this.cities.add(new City(longe,lage,name,true));
    	}
    	
    }

    public static List<String> getSubUtil(String soap){
    	String rgex="\\{(.*?)\\}";
    	List<String> list = new ArrayList<String>();
    	Pattern pattern = Pattern.compile(rgex);// 匹配的模式
    	Matcher m = pattern.matcher(soap);
    	while (m.find()) {
    		int i = 1;
    		list.add(m.group(i));
    		i++;
    		}
    	return list;
    }
    public String toString() {
        return Arrays.toString(cities.toArray());
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public Tuple<Integer, Integer> getTransfert() {
        return transfert;
    }

    public float getTotalDistance() {
        float res = 0;
        int citySize = this.cities.size();
        for (int pointer = 0; pointer < citySize - 1; pointer++) {
            res += this.cities.get(pointer).measureDistance(this.cities.get(pointer + 1));
        }
        res += this.cities.get(citySize - 1).measureDistance(this.cities.get(0));
        return res;
    }
    

    @Override
    public int compareTo(Route o) {
        float a = this.getTotalDistance();
        float b = o.getTotalDistance();
        return Float.compare(b, a);
    }
    public static void main(String[] args) {
    	CityData Data = new CityData(50);
        Route initRoute = new Route(Data.getCities());
        System.out.println(initRoute.toString());
        Route nr=new Route(initRoute.toString());
        System.out.println(nr.toString());
    }
}