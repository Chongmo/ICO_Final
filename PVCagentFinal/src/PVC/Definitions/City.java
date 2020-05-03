package PVC.Definitions;

public class City {
    private static final float EARTH_EQUATORIAL_RADIUS=6378.1370f;
    private static final float CONVERT_DEGREES_TO_RADIANS=(float) (Math.PI/180);
    private static final float CONVERT_KMS_TO_MILES=(float) 0.621371;

    private float longitude;
    private float latitude;
    private String name;

    public City(float longitude, float latitude, String name) {
		/*
		 * this.longitude = longitude; this.latitude = latitude; this.name = name;
		 */
		
		  this.longitude = longitude*CONVERT_DEGREES_TO_RADIANS; this.latitude =
		  latitude*CONVERT_DEGREES_TO_RADIANS; this.name = name;
		 
    }
    public City(float longitude, float latitude, String name,boolean degree) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("{%s|%f|%f}", this.name,this.longitude,this.latitude);
    }
    public float measureDistance(City city){
        float deltaLatitude = city.getLatitude() - this.getLatitude();
        float deltaLongitude = city.getLongitude()-this.getLongitude();
        float a=(float) (Math.pow(Math.sin(deltaLatitude/2D), 2D)
                +Math.cos(this.getLatitude())*Math.cos(city.getLatitude())*Math.pow(Math.sin(deltaLongitude/2D), 2D));
        return (float) (EARTH_EQUATORIAL_RADIUS*2D*Math.atan2(Math.sqrt(a), Math.sqrt(1D-a)));
    }
}
