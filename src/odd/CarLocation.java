package odd;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CarLocation {
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    private int carId;
    private double x;
    private double y;

    @Override
    public String toString() {
        return "CarLocation{" +
                "carId=" + carId +
                ", x=" + x +
                ", y=" + y +
                ", theta=" + theta +
                ", timestamp=" + timestamp +
                '}';
    }

    private double theta;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CarLocation(int id, double x, double y, double theta) {
        this.carId = id;
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.timestamp = System.currentTimeMillis();
    }
    public CarLocation(int id, double x, double y, double theta, long timestamp) {
        this.carId = id;
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.timestamp = timestamp;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


}
