package odd;

import april.jcam.FrameData;
import april.jcam.ImageConvert;
import april.jcam.ImageSource;
import april.jcam.ImageSourceFormat;
import april.jmat.Matrix;
import april.util.JImage;
import april.vis.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 12/4/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class CamCalibrater {

    ImageSource is;
    JFrame window;
    JImage jImage;

    int clicks = 0;
    Point [] clickPoints = new Point[4];
    boolean calibrated = false;
    Matrix affine;
    MouseAdapter clickAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            if (!calibrated) {
                Point p = me.getPoint();

                clickPoints[clicks++] = p;
                if (clicks == 4) {
                    determineAffineMatrix();
                    calibrated = true;
                }
            }

        }

    };

    public CamCalibrater(ImageSource is) {
        this.is = is;
        window = new JFrame("Calibrate me");
        window.setLayout(new BorderLayout());

        FrameData frameData = is.getFrame();
        BufferedImage frame = ImageConvert.convertToImage(frameData);

        jImage = new JImage();
        jImage.setImage(frame);

        window.setSize(frame.getWidth(), frame.getHeight());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.add(jImage);
        window.setVisible(true);

        jImage.addMouseListener(clickAdapter);

        new ImageUpdater().start();
    }

    private void determineAffineMatrix() {
        double [][] a_raw = new double[8][6];

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                int calInd = (int) Math.floor(i/2);
                a_raw[i][0] = clickPoints[calInd].x;
                a_raw[i][1] = clickPoints[calInd].y;
                a_raw[i][2] = 1;
                for (int j = 3; j < 6; j++) {a_raw[i][j] = 0;}
            }
            else {
                for (int j = 0; j < 3; j++) {a_raw[i][j] = 0;}
                int calInd = (int) Math.floor(i/2);
                a_raw[i][3] = clickPoints[calInd].x;
                a_raw[i][4] = clickPoints[calInd].y;
                a_raw[i][5] = 1;
            }
        }
        //TODO: CHANGE THIS
        double calLen = 12d * .0254;
        Matrix a = new Matrix(a_raw);
        double [][] b_raw = new double[8][1];
        double [] b_col = {0, calLen, calLen, 0, 0, (calLen * -1), (calLen * -1), 0};
        for (int i = 0; i < b_col.length; i++) {
            b_raw[i][0] = b_col[i];
        }
        Matrix b = new Matrix(b_raw);
        Matrix solution = (a.transpose().times(a)).inverse().times(a.transpose().times(b));

        double [][] affine_raw = {{solution.get(0, 0), solution.get(1, 0), solution.get(2, 0)},
                {solution.get(3, 0), solution.get(4, 0), solution.get(5, 0)},
                {0, 0, 1}};

        affine = new Matrix(affine_raw);
        JOptionPane.showMessageDialog(window, "calibrated!");

        calibrated = true;
    }

    class ImageUpdater extends Thread {
        public void run() {
            while(!calibrated) {
                FrameData frameData = is.getFrame();
                BufferedImage frame = ImageConvert.convertToImage(frameData);
                jImage.setImage(frame);
            }
        }
    }

    public Matrix getAffine() {
        while(!calibrated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return affine;

    }

}
