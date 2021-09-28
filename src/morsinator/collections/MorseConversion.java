package morsinator.collections;

import morsinator.reader.*;

public interface MorseConversion {
    public void addRow(char letter, String morse) throws AddException;
    public char getLetter(String morse);
}
