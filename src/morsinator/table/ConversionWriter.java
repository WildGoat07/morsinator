package morsinator.table;

import java.io.IOException;
import java.io.Writer;

import morsinator.collections.TextConversion;

public interface ConversionWriter {
    void save(Writer writer, TextConversion tm) throws IOException;
}
