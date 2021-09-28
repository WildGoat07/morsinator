package morsinator.collections;

import morsinator.reader.*;

public interface TextConversion {
    public void addRow(char letter, String morse);
    public String getMorse(char letter);
}
