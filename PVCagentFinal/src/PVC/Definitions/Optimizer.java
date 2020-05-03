package PVC.Definitions;

import java.util.ArrayList;
import java.util.LinkedList;

public class Optimizer {
    private ArrayList<Parametre> params=new ArrayList<>();
    private LinkedList<Float> meilleureDists=new LinkedList<>();
    private float lr=0.9f;
    private int flag=1;
    private int counter=0;
    private boolean END=false;
    private int actualnumber;
    public Optimizer(ArrayList<Parametre> params,float meilleureDist){
        this.params=params;
        this.meilleureDists.add(meilleureDist);
        this.actualnumber=0;
    }
    public ArrayList<Parametre> getParam(){
        return this.params;
    }
    public boolean checkEnd(float dist){
        if(this.meilleureDists.size()<2) return false;
        else return (this.meilleureDists.get(this.meilleureDists.size() - 2) - dist)<0.01;
    }

    public ArrayList <Parametre> Evalue(float dist){
    	if(this.END) {
    		return this.getParam();
    	}
        if(!this.checkEnd(dist)){
            this.counter++;
            this.lr=(1.f/this.counter);
            System.out.println(meilleureDists);
            System.out.println(dist);

            if(Math.abs(dist-this.meilleureDists.getLast())<0.01){
                this.flag=-1*this.flag;
            }
            this.meilleureDists.add(dist);
            Parametre actualParam=this.params.get(this.actualnumber);
            float delta=actualParam.getGrad()*this.flag*this.lr;
            actualParam.evalue(delta);
            this.params.set(actualnumber,actualParam);
            return this.getParam();
        }else{
            this.actualnumber++;
            
            if (this.actualnumber>=this.params.size()){
                this.END=true;
                System.out.println("Optimize finished");
                return this.getParam();
            }
            this.counter=1;
            this.lr=(1.f/this.counter);
            this.flag=1;

            Parametre actualParam=this.params.get(this.actualnumber);
            float delta=(float)actualParam.grad*this.flag*this.lr;
            actualParam.evalue(delta);
            this.params.set(actualnumber,actualParam);
            return this.getParam();
        }
    }
}
