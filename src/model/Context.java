package model;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by vviital on 17/09/15.
 */
public class Context {

    public List<Line2d> getLine2ds() {
        return line2ds;
    }

    public void setLine2ds(List<Line2d> line2ds) {
        this.line2ds = line2ds;
    }

    private List<Line2d> line2ds;

    private List<Point2d> allPoints;

    public List<Point2d> getConvexHull() {
        return convexHull;
    }

    public void setConvexHull(List<Point2d> convexHull) {
        this.convexHull = convexHull;
    }

    private List<Point2d> convexHull;

    public List<Line2d> getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(List<Line2d> boundaries) {
        this.boundaries = boundaries;
    }

    private List<Line2d> boundaries;

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    private Function function;

    public Context(){

    }

    public Context(List<Line2d> line2ds) {
        this.function = new Function();
        this.line2ds = line2ds;
        this.allPoints = ConstractPoints();
        List<Point2d> semiplanes = intersectSemiplane(this.allPoints, this.line2ds);
        this.convexHull = buildConvexHull(semiplanes);
    }

    public List<Point2d> ConstractPoints(){
        List<Point2d> cand = new ArrayList();
        Set<Point2d> set = new HashSet();
        for(Line2d first : line2ds)
            for(Line2d second : line2ds){
                if (first.isParallel(second)) continue;
                Point2d point = first.intersect(second);
                set.add(point);
            }
        cand.addAll(set);
        return cand;
    }

    public List<Point2d> intersectSemiplane(List<Point2d> points, List<Line2d> lines){
        Set<Point2d> set = new HashSet();
        for(Point2d point : points){
            boolean ok = true;
            for(Line2d line : lines){
                double value = line.getValue(point);
                if (Line2d.cmp(value, 0) < 0) {
                    ok = false;
                    break;
                }
            }
            if (ok)
                set.add(point);
        }
        return new ArrayList(set);
    }

    public List<Point2d> buildConvexHull(List<Point2d> points){
        List<Point2d> convexHull = new ArrayList();
        points.sort((a, b) -> {
            if (Line2d.cmp(a.getX(), b.getX()) != 0) return Line2d.cmp(a.getX(), b.getX());
            return Line2d.cmp(a.getY(), b.getY());
        });
        stepConvexHullBuild(convexHull, points);
        Collections.reverse(points);
        stepConvexHullBuild(convexHull, points);
        return convexHull;
    }

    private List<Point2d> stepConvexHullBuild(List<Point2d> convexHull, List<Point2d> points){
        if (convexHull == null || points.size() == 0) return new ArrayList<Point2d>();
        Line2d etal = new Line2d(points.get(0), points.get(points.size() - 1));
        for(Point2d point : points){
            if (Line2d.cmp(etal.getValue(point), 0) < 0) continue;
            while(convexHull.size() >= 2){
                Point2d p1 = convexHull.get(convexHull.size() - 1);
                convexHull.remove(convexHull.size() - 1);
                Point2d p0 = convexHull.get(convexHull.size() - 1);
                double value = (p1.substract(p0)).crossProduct(point.substract(p1));
                if (Line2d.cmp(value, 0) >= 0) {
                    convexHull.add(p1);
                    break;
                }
            }
            convexHull.add(point);
        }
        return convexHull;
    }

    public Point2d getCenter(){
        Point2d res = new Point2d(0, 0);
        for(Point2d x : this.convexHull){
            res.setX(res.getX() + x.getX());
            res.setY(res.getY() + x.getY());
        }
        res.setX(res.getX() / this.convexHull.size());
        res.setY(res.getY() / this.convexHull.size());
        return res;
    }

    public Point2d GradientDescent(){
        double mul = 0.95;
        double curmul = 5;
        Point2d p = getCenter();
        for(int i = 0; i < 1000; ++i){
            Point2d grad = this.function.getGradient(p);
            grad.setLength(curmul);
            p = p.add(grad);
            curmul *= mul;
        }
        return p;
    }

    public Point2d TernarySearch(Point2d a, Point2d b, int sign){
        double l = 0, r = b.substract(a).length();
        Point2d v = b.substract(a);
        for(int iter = 0; iter < 100; ++iter){
            double m1 = l + (r - l) / 3.;
            double m2 = r - (r - l) / 3.;
            double value1 = sign * this.function.getValue(a.add(v.setLength(m1)));
            double value2 = sign * this.function.getValue(a.add(v.setLength(m2)));
            if (Line2d.cmp(value1, value2) < 0){
                l = m1;
            } else {
                r = m2;
            }
        }
        return a.add(v.setLength(l));
    }
}
