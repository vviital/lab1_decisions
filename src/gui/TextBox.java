package gui;

import javax.swing.*;

/**
 * Created by vviital on 17/09/15.
 */
public class TextBox extends JTextArea {

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int row, col;

    public TextBox(){

    }

    public TextBox(String value, int x, int y, int w, int h, int row, int col){
        this.setText(value);
        this.setBounds(x, y, w, h);
        this.row = row;
        this.col = col;
    }
}
