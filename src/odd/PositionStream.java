package odd;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/5/13
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PositionStream extends Thread {
    private TrackerServer server;
    CarLocation currentPos;
    private long delay;

    public PositionStream(long delay) {
        server = new TrackerServer();
        this.delay = delay;
        currentPos = new CarLocation(0, 0, 0);
    }
    public PositionStream(long delay, CarLocation pos) {
        server = new TrackerServer();
        this.delay = delay;
        currentPos = pos;
    }

    public void setPosition(CarLocation cl) {
        currentPos = cl;
    }

    public void run() {
        while(true) {
            server.sendLocation(currentPos);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.out.println("TOO MUCH COFFEE!!!");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
