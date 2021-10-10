package morsinator.text;

public class TextPosition {
    private int row;

    public TextPosition() {
        row = 1;
    }

    protected void lineFeed() {
        row++;
    }

    public int getRow() {
        return row;
    }
}