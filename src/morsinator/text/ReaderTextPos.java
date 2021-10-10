package morsinator.text;

import java.io.Reader;
import java.io.IOException;

public class ReaderTextPos {
    private Reader reader;
    private int row;

    public ReaderTextPos(Reader reader) {
        this.reader = reader;
        row = 1;
    }

    public int read() throws IOException {
        int c = reader.read();

        if(c == '\n') {
            row++;
        }

        return c;
    }

    public int getRow() {
        return row;
    }
}