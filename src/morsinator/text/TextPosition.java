package morsinator.text;

public class TextPosition {
    private int row;
    private int col;
    private int pos;

    public TextPosition() {
        row = 1;
        col = 1;
        pos = 1;
    }

    protected void newChar() {
        col++;
        pos++;
    }

    protected void lineFeed() {
        row++;
        col = 1;
        pos++;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPos() {
        return pos;
    }
}