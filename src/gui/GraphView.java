package gui;

import model.Context;
import model.Line2d;
import model.Point2d;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.util.List;

/**
 * Created by vviital on 17/09/15.
 */
public class GraphView extends JPanel {

    int x0 = 50, y0 = 50;

    int gwidth = 700;

    int scale = 30;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public Context context;

    public int getGwidth() {
        return gwidth;
    }

    public void setGwidth(int gwidth) {
        this.gwidth = gwidth;
    }

    public int getGheight() {
        return gheight;
    }

    public void setGheight(int gheight) {
        this.gheight = gheight;
    }

    int gheight = 500;

    public GraphView(){
        setLocation(x0, y0);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setClip(x0 - 10, y0 - 10, gwidth + 20, gheight + 20);
        Color was = g2d.getColor();

        g2d.setColor(new Color(255, 255, 224));
        g2d.fillRect(x0, y0, gwidth, gheight);
        g2d.setColor(was);
        drawAxes(g2d);
        drawLines(g2d);
        drawConvexHull(g2d);
        drawGradient(g2d);
        drawPoint(g2d, getMinimum());
        drawPoint(g2d, getMaximum());
    }

    private void drawAxes(Graphics2D g){
        GeneralPath path = new GeneralPath();
        path.moveTo(x0, y0);
        path.lineTo(x0, y0 + gheight);
        path.lineTo(x0 + gwidth, y0 + gheight);
        g.draw(path);
        g.setPaint(new Color(133, 133, 133));
        for(int i = 1; i <= (gwidth - 1) / scale; ++i){
            g.drawLine(x0 + i * scale, y0, x0 + i * scale, y0 + gheight + 5);
            g.drawString(Integer.toString(i), x0 + i * scale, y0 + gheight + 10);
        }
        for(int i = 1; i <= (gheight - 1) / scale; ++i){
            g.drawLine(x0 - 5, y0 + gheight - i * scale, x0 + gwidth, y0 + gheight - i * scale);
            g.drawString(Integer.toString(i), x0 - 12, y0 + gheight - i * scale);
        }
    }

    private void drawLines(Graphics2D g){
        if (this.context != null && this.context.getLine2ds() != null) {
            g.setPaint(Color.red);
            for (Line2d line : this.context.getLine2ds()) {
                java.util.List<Point2d> curPoints = new ArrayList();
                for(Line2d b : this.context.getBoundaries()){
                    if (b.isParallel(line)) continue;
                    Point2d temp = b.intersect(line);
                    if (temp.getX() >= 0 && temp.getX() <= 40)
                        if (temp.getY() >= 0 && temp.getY() <= 40)
                            curPoints.add(b.intersect(line));
                }
                for(int i = 0; i < curPoints.size(); ++i)
                    for(int j = i + 1; j < curPoints.size(); ++j){
                        g.drawLine((int)(curPoints.get(i).getX() * scale) + x0, mirror((int)(curPoints.get(i).getY() * scale)),
                                (int)(curPoints.get(j).getX() * scale) + x0, mirror((int)(curPoints.get(j).getY() * scale)));
                    }
            }
        }
        g.setColor(Color.black);
    }

    private void drawConvexHull(Graphics2D g){
        if (this.context != null && this.context.getConvexHull() != null && this.context.getConvexHull().size() != 0) {
            g.setPaint(Color.green);
            List<Point2d> points = this.context.getConvexHull();
            GeneralPath path = new GeneralPath();
            path.moveTo((int) (points.get(points.size() - 1).getX() * scale + x0), mirror((int) points.get(points.size() - 1).getY() * scale));
            for (Point2d point : points) {
                path.lineTo((int) (point.getX() * scale + x0), mirror((int) (point.getY() * scale)));
            }
            g.draw(path);
        }
    }

    private int mirror(int x){
       return -x + gheight + y0;
    }

    public Point2d getMinimum(){
        if (this.context == null || this.context.getConvexHull() == null || this.context.getConvexHull().size() == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
        List<Point2d> hull = this.context.getConvexHull();
        Point2d best = new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
        double bestValue = Double.MAX_VALUE;
        for(int i = 0; i < hull.size() - 1; ++i){
            int toi = (i + 1);
            Point2d cur = this.context.TernarySearch(hull.get(i), hull.get(toi), -1);
            double value = this.context.getFunction().getValue(cur);
            if (Line2d.cmp(bestValue, value) > 0){
                bestValue = value;
                best = cur;
            }
        }
        return best;
//        if (this.context.getConvexHull() == null || this.context.getConvexHull().size() == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
//        Point2d best = new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
//        double value = Double.MAX_VALUE;
//        for(Point2d point : this.context.getConvexHull()){
//            if (Line2d.cmp(this.context.getFunction().getValue(point), value) < 0){
//                best = point;
//                value = this.context.getFunction().getValue(point);
//            }
//        }
//        return best;
    }

    public Point2d getMaximum(){
        if (this.context == null || this.context.getConvexHull() == null || this.context.getConvexHull().size() == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
        List<Point2d> hull = this.context.getConvexHull();
        Point2d best = new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
        double bestValue = -Double.MAX_VALUE;
        for(int i = 0; i < hull.size() - 1; ++i){
            int toi = (i + 1);
            Point2d cur = this.context.TernarySearch(hull.get(i), hull.get(toi), 1);
            double value = this.context.getFunction().getValue(cur);
            if (Line2d.cmp(bestValue, value) < 0){
                bestValue = value;
                best = cur;
            }
        }
        Point2d cond = this.context.GradientDescent();
        System.out.println("cond = " + cond);
        System.out.println("\" + this.context.getFunction().getValue(cond) = " + this.context.getFunction().getValue(cond));
        return best;
//        if (this.context.getConvexHull() == null || this.context.getConvexHull().size() == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
//        Point2d best = new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
//        double value = Double.MIN_VALUE;
//        for(Point2d point : this.context.getConvexHull()){
//            if (Line2d.cmp(this.context.getFunction().getValue(point), value) > 0){
//                best = point;
//                value = this.context.getFunction().getValue(point);
//            }
//        }
//        return best;
    }

    public void drawGradient(Graphics2D g){
        if (this.context != null && this.context.getLine2ds() != null && this.context.getFunction() != null) {
            g.setPaint(Color.magenta);
            for (int i = 0; i <= this.gwidth / scale; ++i)
                for (int j = 0; j <= this.gheight; ++j) {
                    Point2d beg = new Point2d(i, j);
                    Point2d point = this.context.getFunction().getGradient(beg);
                    point = point.setLength(0.5).add(beg);
                    g.drawLine((int) (beg.getX() * scale + x0), mirror((int) (beg.getY() * scale)),
                            (int) (point.getX() * scale + x0), mirror((int) (point.getY() * scale)));
                    g.fillOval((int) (point.getX() * scale + x0 - 2), mirror((int) (point.getY() * scale) - 2), 4, 4);
                }
            g.setPaint(Color.black);
        }
    }



    public void drawPoint(Graphics2D g, Point2d point){
        if (point.getX() != Double.MAX_VALUE) {
            g.setPaint(Color.ORANGE);
            double x = point.getX();
            double y = point.getY();
            g.fillOval((int) (x * scale + x0) - 5, mirror((int)(y * scale)) - 5, 10, 10);
            g.setPaint(Color.black);
        }
    }
}
