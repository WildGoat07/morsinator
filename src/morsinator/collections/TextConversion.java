package morsinator.collections;

import morsinator.reader.*;
import morsinator.MorsinatorParseException;

public interface TextConversion {
    public void addRow(char letter, String morse);
    public String getMorse(char letter) throws MorsinatorParseException;
}
