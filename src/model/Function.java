package model;

/**
 * Created by vviital on 18/09/15.
 */
public class Function {
    // F(x1, y1) = a * x1 ^ 2 + b * x1 * x2 + c * x2 ^ 2 + d * x1 + e * x2 + f

    private double a;
    private double b;
    private double c;
    private double d;
    private double e;
    private double f;

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

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public Function(){

    }

    public Function(double a, double b, double c, double d, double e, double f){
        this.setA(a);
        this.setB(b);
        this.setC(c);
        this.setD(d);
        this.setE(e);
        this.setF(f);
    }

    public Point2d getGradient(double x1, double x2){
        return new Point2d(getDerivativeByX1(x1, x2), getDerivativeByX2(x1, x2));
    }

    public Point2d getGradient(Point2d point){
        return getGradient(point.getX(), point.getY());
    }

    // F(x1, y1) = a * x1 ^ 2 + b * x1 * x2 + c * x2 ^ 2 + d * x1 + e * x2 + f

    public double getDerivativeByX1(double x1, double x2){
        return 2 * a * x1 + b * x2 + 0 + d + 0 + 0;
    }

    public double getDerivativeByX2(double x1, double x2){
        return 0 + b * x1 + 2 * c * x2 + 0 + e + 0;
    }

    public double getValue(Point2d point){
        return this.getValue(point.getX(), point.getY());
    }

    public double getValue(double x1, double x2){
        return a * x1 * x1 + b * x1 * x2 + c * x2 * x2 + d * x1 + e * x2 + f;
    }

    public int getOrder(){
        int order = 0;
        if (Line2d.cmp(d, 0) != 0 || Line2d.cmp(e, 0) != 0)
            order = 1;
        if (Line2d.cmp(a, 0) != 0 || Line2d.cmp(b, 0) != 0 || Line2d.cmp(c, 0) != 0)
            order = 2;
        return order;
    }

}
