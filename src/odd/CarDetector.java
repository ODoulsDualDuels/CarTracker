package odd;

import april.jcam.FrameData;
import april.jcam.ImageConvert;
import april.jcam.ImageSource;
import april.jcam.ImageSourceFormat;
import april.jmat.LinAlg;
import april.jmat.Matrix;
import april.tag.CameraUtil;
import april.tag.TagDetection;
import april.tag.TagDetector;
import april.tag.TagFamily;
import april.util.ReflectUtil;
import april.util.Tic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class CarDetector {

    static final String TAG_FAMILY = "april.tag.Tag16h5";
    static final ArrayList<Integer> GOOD_IDS = new ArrayList<Integer>();
    ImageSource is;

    TagFamily tf;
    TagDetector detector;

    Matrix affine;

    TrackerServer server;

    public CarDetector(ImageSource is, Matrix affine) {
        this.is = is;
        this.affine = affine;
        GOOD_IDS.add(0);
        GOOD_IDS.add(1);

        server = new TrackerServer();

        this.tf = (TagFamily) ReflectUtil.createObject(TAG_FAMILY);

        detector = new TagDetector(tf);
    }

    //each iteration processes webcam frame and determines locations of tags on cars.
    //reports center of car and orientation theta relative to the X-axis of the arena
    public void startDetecting(long pauseMillis) {

        while (true) {
            FrameData frmd = is.getFrame();
            if (frmd == null)
                continue;

            BufferedImage im = ImageConvert.convertToImage(frmd);

            ArrayList<TagDetection> detections = detector.process(im, new double[] {im.getWidth()/2.0, im.getHeight()/2.0});

            for (TagDetection d : detections) {
                //skip false detections
                if (!GOOD_IDS.contains(d.id)) {
                    continue;
                }
                //determine
                double topLeft[] = convertToRealWorld(d.interpolate(-1, 1));
                double topRight[] = convertToRealWorld(d.interpolate(1, 1));

                double offsetX = topRight[0] - topLeft[0];
                double offsetY = topRight[1] - topLeft[1];

                double theta = Math.atan2(offsetY, offsetX);

                //center of tag
                double center[] = d.cxy;
                double centerRealWord[] = convertToRealWorld(center);

                CarLocation cl = new CarLocation(d.id, centerRealWord[0], centerRealWord[1], theta);
                server.sendLocation(cl);

            }
        }

    }
    //converts pixel coordinates to real-world coordinates using affine matrix
    private double[] convertToRealWorld(double[] pixelCoords) {
        double [] point_raw = {pixelCoords[0], pixelCoords[1], 1};
        double [] point = LinAlg.matrixAB(affine.copyArray(), point_raw);
        return new double[] {point[0], point[1]};
    }
}
