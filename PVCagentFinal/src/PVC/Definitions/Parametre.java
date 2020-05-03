package PVC.Definitions;

//import javafx.util.Pair;

public class Parametre {
    protected String name;
    protected String type;
    protected Object value;
    protected Object grad;
    public Parametre(String name,float value,float grad){
        this.name=name;
        this.value=value;
        this.grad=grad;
        this.type="continue";
    }
    public Parametre(String name,int value,int grad){
        this.name=name;
        this.value=value;
        this.grad=grad;
        this.type="discret";
    }
    public void evalue(float delta){
        if(this.type=="continue"){
            this.value=(float)this.getValue()+delta;
        }else{
            delta= delta>0? (int) Math.ceil(delta) : (int)Math.floor(delta);
            this.value=(int)this.getValue()+(int)delta;
        }
    }
	public String getName() {
		return name;
	}
	public float getValue() {
		if(this.type=="continue") {
		return ((Float) value).floatValue();}
		else {
			return ((Integer) value).floatValue();
		}
	}
	public float getGrad() {
		if(this.type=="continue") {
		return ((Float) grad).floatValue();}
		else {
			return ((Integer) grad).floatValue();
		}
	}
	@Override
	public String toString() {
		/*if(this.type=="continue") {
			//return String.format("(%s,%f)", this.name,this.value.floatValue())
		}*/
		return String.format("(%s,%f)", this.name,this.getValue());

	}
    
}
