import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicStrokeTest extends JPanel {

    public void paint(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//
//        BasicStroke stroke = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.1F);
//
//        g2.setPaint(Color.black);
//
//        int x = 5;
//        int y = 7;
//        int xPoints[] = { x, 200, x, 200 };
//        int yPoints[] = { y, 200, 200, y };
//        GeneralPath filledPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
//                xPoints.length);
//        filledPolygon.moveTo(xPoints[0], yPoints[0]);
//        for (int index = 1; index < xPoints.length; index++) {
//            filledPolygon.lineTo(xPoints[index], yPoints[index]);
//        }
//        filledPolygon.closePath();
//
//        g2.draw(stroke.createStrokedShape(filledPolygon));
//        //g2.draw(stroke.createStrokedShape(new Rectangle2D.Float(10, 10, 20, 20)));

        int[] xs = {25,  125, 85,  75, 25, 65, };
        int[] ys = {50,  50, 100,  110, 150, 100};

        BasicStroke traceStroke = new BasicStroke (5);
        Graphics2D gc = (Graphics2D) g.create();
        gc.setStroke(traceStroke);
        gc.setColor(Color.BLUE);
        gc.drawPolyline(xs, ys, 6);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new BasicStrokeTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(20, 20, 500, 500);
        frame.setVisible(true);
    }

}
