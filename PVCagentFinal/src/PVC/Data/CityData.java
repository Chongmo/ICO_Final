package PVC.Data;

import PVC.Definitions.City;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CityData {
    private int CityNumbers=10;
    private ArrayList<City> cities=new ArrayList<>();

    public ArrayList<City> getCities() {
        return cities;
    }

    public CityData(int cityNumbers) {
        CityNumbers = cityNumbers;
        this.readTxtFileIntoStringArrList();
    }

    public void readTxtFileIntoStringArrList()
    {
        String filePath = "D:\\EclipseJava\\PVCagent0320\\bin\\PVC\\Data\\CITIES.txt";//ou pure_city.txt
        try
        {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int count=0;
                while ((lineTxt = bufferedReader.readLine()) != null && count<this.CityNumbers)
                {
                    count++;
                    String[] term=lineTxt.split(",");
                    String name=term[0];
                    float l1=Float.parseFloat(term[1]);
                    float l2=Float.parseFloat(term[2]);
                    this.cities.add(new City(l1,l2,name));
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("NO FILES");
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR CONTENT");
            e.printStackTrace();
        }
    }
}
