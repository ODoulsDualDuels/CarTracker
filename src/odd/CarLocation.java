package odd;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CarLocation {
    private double x;
    private double y;
    private double theta;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CarLocation(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.timestamp = System.currentTimeMillis();
    }
    public CarLocation(double x, double y, double theta, long timestamp) {
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
