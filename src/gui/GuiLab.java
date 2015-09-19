package gui;

import model.Context;
import model.Function;
import model.Line2d;
import model.Point2d;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * Created by vviital on 17/09/15.
 */
public class GuiLab {

    private JFrame mainFrame;

    private JPanel panel;

    private GraphView view;

    private JTextField textNumber;

    private int count;

    private int canY;

    java.util.List<Component> userInput;

    public GuiLab(){
        init();
    }

    public void init(){
        this.mainFrame = new JFrame("SimplexMethod");
        this.view = new GraphView();
        view.setBounds(0, 0, 750, 650);
        this.panel = new JPanel();
        panel.setBounds(750, 30, 500, 600);

        JLabel label = new JLabel("Number of semiplanes");
        label.setBounds(20, 0, 200, 20);

        textNumber = new JTextField();
        textNumber.setBounds(210, 0, 200, 20);

        JButton okButton = new JButton("Ok");
        okButton.setBounds(20, 30, 200, 20);
        okButton.setActionCommand("Ok");
        okButton.addActionListener((event) -> {
            String cur = this.textNumber.getText();
            try {
                this.count = Integer.parseInt(cur);
                this.addForms();
                System.out.println("count = " + this.count);
            } catch (Exception e) {

            }
        });

        panel.setLayout(null);
        panel.add(label);
        panel.add(textNumber);
        panel.add(okButton);

        this.mainFrame.setLayout(null);
        this.mainFrame.add(view);
        this.mainFrame.add(panel);
        this.mainFrame.setSize(new DimensionUIResource(1250, 600));
        this.mainFrame.setVisible(true);
        this.mainFrame.setResizable(false);

        this.userInput = new ArrayList();
    }

    public void clearTextArea(){
        for(Component x : this.userInput){
            this.panel.remove(x);
        }
    }

    public void addLabel(String text, int x, int y, int w, int h){
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.setVisible(true);
        this.panel.add(label);
        this.userInput.add(label);
        this.mainFrame.setVisible(true);
    }

    public void addTextField(String defaultValue, int x, int y, int w, int h, int row, int col){
        TextBox textBox = new TextBox(defaultValue, x, y, w, h, row, col);
        this.userInput.add(textBox);
        this.panel.add(textBox);
    }

    public void addButton(String defaultValue, int x, int y, int w, int h){
        JButton button = new JButton(defaultValue);
        button.setBounds(x, y, w, h);
        button.addActionListener((event) -> {
            this.paintGraph();
        });
        this.panel.add(button);
        this.userInput.add(button);
    }

    public void addForms(){
        clearTextArea();
        this.userInput.clear();
        int x = 20, y = 60;
        //this.mainFrame.setSize(100, 100);
        addLabel("x0", x, y, 100, 20);
        addLabel("x1", x + 60, y, 100, 20);
        addLabel("c", x + 150, y, 100, 20);
        y += 30;
        for(int i = 0; i < this.count; ++i){
            addTextField("0", x, y, 50, 20, i, 0);
            addTextField("0", x + 60, y, 50, 20, i, 1);
            addLabel(">=", x + 115, y, 50, 20);
            addTextField("0", x + 150, y, 50, 20, i, 2);
            y += 30;
        }
        addLabel("Target function: ", x, y, 200, 20);
        y += 30;
        addTextField("0", x, y, 40, 20, this.count, 0); x += 45;
        addLabel("x1^2+", x, y, 45, 20); x += 45;
        addTextField("0", x, y, 40, 20, this.count, 1); x += 45;
        addLabel("x1*x2+", x, y, 50, 20); x += 50;
        addTextField("0", x, y, 40, 20, this.count, 2); x += 45;
        addLabel("x2^2+", x, y, 45, 20); x += 50;
        addTextField("0", x, y, 40, 20, this.count, 3); x += 45;
        addLabel("x1+", x, y, 30, 20); x += 30;
        addTextField("0", x, y, 40, 20, this.count, 4); x += 45;
        addLabel("x2+", x, y, 30, 20); x += 30;
        addTextField("0", x, y, 40, 20, this.count, 5);
        x = 20;
        y += 30;
        addButton("Go!!!", x, y, 100, 20);
        this.canY = y + 30;
        this.panel.repaint();

    }

    public void paintGraph(){
        Map<Integer, Line2d> map = new HashMap();
        Function function = new Function();

        for(Component x : this.userInput){
            if (x instanceof TextBox){
                TextBox current = (TextBox) x;
                if (current.getRow() == this.count){
                    double value = Double.parseDouble(current.getText());
                    switch (current.getCol()){
                        case 0 : function.setA(value); break;
                        case 1 : function.setB(value); break;
                        case 2 : function.setC(value); break;
                        case 3 : function.setD(value); break;
                        case 4 : function.setE(value); break;
                        case 5 : function.setF(value); break;
                    }
                    continue;
                }
                if (!map.containsKey(current.getRow()))
                    map.put(current.getRow(), new Line2d());
                Line2d cur = map.get(current.getRow());
                System.out.println(current.getText());
                double value = Double.parseDouble(current.getText());
                switch (current.getCol()){
                    case 0 : cur.setA(value); break;
                    case 1 : cur.setB(value); break;
                    case 2 : cur.setC(-value); break;
                }
            }
        }
        java.util.List<Line2d> lines = new ArrayList(map.values());
        for(Line2d x : lines){
            System.out.println(x.getA() + " " + x.getB() + " " + x.getC());
        }

        lines.add(new Line2d(-1, 0, 40));
        lines.add(new Line2d(1, 0, 0));
        lines.add(new Line2d(0, -1, 40));
        lines.add(new Line2d(0, 1, 0));

        List<Line2d> boundaries = new ArrayList();

        boundaries.add(new Line2d(1, 0, -40));
        boundaries.add(new Line2d(1, 0, 0));
        boundaries.add(new Line2d(0, 1, -40));
        boundaries.add(new Line2d(0, 1, 0));

        this.view.setContext(new Context(lines));
        this.view.getContext().setFunction(function);
        this.view.getContext().setBoundaries(boundaries);
        this.view.repaint();
        this.addAnwer();
    }

    private void addAnwer(){
        NumberFormat format = new DecimalFormat("0.0000000");
        Point2d minPoint = this.view.getMinimum();
        Point2d maxPoint = this.view.getMaximum();
        int y = canY;
        int x = 20;
        addLabel("Answers:", x, y, 200, 20); y += 30;
        addLabel("MinPoint:", x, y, 100, 20);
        if (isBad(minPoint)){
            addLabel("Cannot find answer", x + 100, y, 200, 20);
        } else {
            addLabel(minPoint.toString(), x + 100, y, 200, 20);
            addLabel(format.format(this.view.getContext().getFunction().getValue(minPoint)), x + 300, y, 200, 20);
        }
        y += 30;
        addLabel("MaxPoint:", x, y, 100, 20);
        if (isBad(maxPoint)){
            addLabel("Cannot find answer", x + 100, y, 200, 20);
        } else {
            addLabel(maxPoint.toString(), x + 100, y, 200, 20);
            addLabel(format.format(this.view.getContext().getFunction().getValue(maxPoint)), x + 300, y, 200, 20);
        }
        this.panel.repaint();
    }

    private boolean isBad(Point2d point){
        if (point.getX() < -0.00001 || point.getX() > 30) return true;
        if (point.getY() < -0.00001 || point.getY() > 30) return true;
        return false;
    }
}
