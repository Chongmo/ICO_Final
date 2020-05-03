package PVC.Utils;

import java.util.ArrayList;

public class Line {
    private ArrayList<Float> target=new ArrayList<>();
    private String label="label";

    public String getLabel() {
        return label;
    }

    public ArrayList<Float> getTarget() {
        return target;
    }

    public Line(ArrayList<Float> logger, String label) {
        this.target = logger;
        this.label = label;
    }

}
