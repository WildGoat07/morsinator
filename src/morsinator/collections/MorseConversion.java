package morsinator.collections;

import morsinator.reader.*;

public interface MorseConversion {
    public void addRow(char letter, String morse);
    public char getLetter(String morse);
}
