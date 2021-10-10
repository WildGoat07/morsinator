package morsinator;

public class MorsinatorParseException extends Exception {
    private int row;

    public MorsinatorParseException(String message, int row) {
        super(message);
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}