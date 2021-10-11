package morsinator.collections;

import java.util.Set;
import java.util.Map.Entry;

import morsinator.table.*;

public interface TextConversion {
    public void addRow(char letter, String morse);

    public void removeRow(char letter, String morse);

    public Set<Entry<Character, String>> getRows();

    public String getMorse(char letter);

    public void clear();
}
