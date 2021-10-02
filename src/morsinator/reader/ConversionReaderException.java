package morsinator.reader;

public class ConversionReaderException extends Exception {
    private int row;

    public ConversionReaderException(String message) {
        super(message);
    }

    public ConversionReaderException(String message, int row) {
        super(message);
        this.row = row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}