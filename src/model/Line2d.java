package model;

import java.util.DoubleSummaryStatistics;

/**
 * Created by vviital on 17/09/15.
 */
public class Line2d {

    private double a;

    private double b;

    private double c;

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public Line2d(){

    }

    public Line2d(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Line2d(Point2d beg, Point2d end){
        this.a = end.substract(beg).getY();
        this.b = -end.substract(beg).getX();
        this.c = -(a * beg.getX() + b * beg.getY());
    }

    public Point2d intersect(Line2d line){
        if (isParallel(line)) return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
        double det = determinant(a, b, line.a, line.b);
        double det1 = determinant(-c, b, -line.c, line.b);
        double det2 = determinant(a, -c, line.a, -line.c);
        return new Point2d(det1 / det, det2 / det);
    }

    public boolean isParallel(Line2d line){
        return cmp(determinant(a, b, line.a, line.b), 0) == 0;
    }

    public boolean isEqual(Line2d line){
        boolean ok = isParallel(line);
        Point2d point = new Point2d(-a * c / (a * a + b * b), -b * c / (a * a + b * b));
        ok = ok && line.isBelong(point);
        return ok;
    }

    private double determinant(double a11, double a12, double a21, double a22){
        return (new Point2d(a11, a12)).crossProduct(new Point2d(a21, a22));
    }

    private Point2d get(double x){
        if (cmp(b, 0) != 0){
            return new Point2d(x, (-c - a * x) / b);
        }
        return new Point2d(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public boolean isBelong(Point2d point){
        double value = getValue(point);
        return cmp(value, 0) == 0;
    }

    public double getValue(Point2d point){
        return a * point.getX() + b * point.getY() + c;
    }

    public Point2d getBegin(){
        if (cmp(a, 0) == 0 && cmp(b, 0) == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_EXPONENT);
        if (cmp(a, 0) == 0 || cmp(b, 0) == 0){
            if (cmp(a, 0) == 0)
                return new Point2d(0, -c / b);
            return new Point2d(-c / a, 0);
        } else {
            return new Point2d(0, -c / b);
        }
    }

    public Point2d getEnd(){
        if (cmp(a, 0) == 0 && cmp(b, 0) == 0) return new Point2d(Double.MAX_VALUE, Double.MAX_EXPONENT);
        if (cmp(a, 0) == 0 || cmp(b, 0) == 0){
            if (cmp(a, 0) == 0)
                return new Point2d(0, -c / b);
            return new Point2d(-c / a, 0);
        } else {
            return new Point2d(10000, (-c - 10000 * a) / b);
        }
    }

    public static int cmp(double a, double b){
        double eps = 1e-7;
        if (Math.abs(a - b) < eps) return 0;
        if (a > b) return 1;
        return -1;
    }
}
