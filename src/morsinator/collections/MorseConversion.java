package morsinator.collections;

import morsinator.table.*;

public interface MorseConversion {
    public void addRow(char letter, String morse);

    public void removeRow(char letter, String morse);

    public char getLetter(String morse);

    public void clear();
}
