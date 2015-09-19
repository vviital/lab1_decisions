package model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by vviital on 17/09/15.
 */
public class Point2d {

    private double x;

    private double y;

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Point2d(){

    }

    public Point2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point2d add(Point2d obj){
        return new Point2d(x + obj.getX(), y + obj.getY());
    }

    public Point2d substract(Point2d obj){
        return new Point2d(x - obj.getX(), y - obj.getY());
    }

    public double scalarProduct(Point2d obj){
        return x * obj.x + y * obj.y;
    }

    public double crossProduct(Point2d obj){
        return x * obj.y - y * obj.x;
    }

    public Point2d multiply(double value){
        return new Point2d(x * value, y * value);
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Point2d){
            Point2d point2d = (Point2d) obj;
            return Line2d.cmp(point2d.getX(), x) == 0 && Line2d.cmp(point2d.getY(), y) == 0;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public String toString(){
        NumberFormat format = new DecimalFormat("0.0000000");
        return format.format(getX()) + " " + format.format(getY());
    }

    public double length(){
        return Math.sqrt(x * x + y * y);
    }

    public Point2d normalize(){
        return new Point2d(x, y).setLength(1);
    }

    public Point2d setLength(double l){
        double len = length();
        if (Line2d.cmp(len, 0) == 0) len = 1;
        return new Point2d(x / len * l, y / len * l);
    }
}
