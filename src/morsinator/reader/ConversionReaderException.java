package morsinator.reader;

public class ConversionReaderException extends RuntimeException {
    private int row;

    public ConversionReaderException(String message, int row) {
        super(message);
        this.row = row;
    }

    public ConversionReaderException(String message, int row, Throwable cause) {
        super(message, cause);
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}