package odd;

import april.jcam.ImageSource;
import april.jmat.Matrix;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CarTracker {

    public static void main(String[] args) {

        String url = ImageSource.getCameraURLs().get(0);
//        String url = "v4l2:///dev/video1?fidx=2";
        ImageSource is = null;
        try {
            is = ImageSource.make(url);
        }
        catch (Exception e) {
            System.out.println("can't make image source");
            e.printStackTrace();
        }
        if (is != null) {
            is.start();
            //calibrate the camera
            CamCalibrater cc = new CamCalibrater(is);
            //get affine transformation matrix from calibrated camera
            Matrix affine = cc.getAffine();
            //System.out.println(affine.toString());
            //begin detecting cars
            CarDetector carDetector = new CarDetector(is, affine);
            carDetector.startDetecting(0);
        }
    }
}
