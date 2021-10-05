package morsinator.collections;

import morsinator.MorsinatorParseException;
import morsinator.reader.*;

public interface MorseConversion {
    public void addRow(char letter, String morse) throws MorsinatorParseException;
    public char getLetter(String morse) throws MorsinatorParseException;
}
