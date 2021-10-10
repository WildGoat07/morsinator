package morsinator.text;

public class TextPosition {
    private int row;
    private int col;

    public TextPosition() {
        row = 1;
        col = 1;
    }

    protected void newChar() {
        col++;
    }

    protected void lineFeed() {
        row++;
        col = 1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}