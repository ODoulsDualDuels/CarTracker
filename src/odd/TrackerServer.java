package odd;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;


/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrackerServer {
    SocketIOServer server;

    public TrackerServer() {
        Configuration conf = new Configuration();
        conf.setHostname("localhost");
        conf.setPort(9001);


        server = new SocketIOServer(conf);

        server.start();
    }

    public void sendLocation(CarLocation cl) {
        server.getBroadcastOperations().sendJsonObject(cl);

    }
}
