package morsinator.collections;

import morsinator.reader.ConversionRow;
import morsinator.collections.generics.MorsiList;
import morsinator.MorsinatorParseException;

public class ConversionList extends MorsiList<ConversionRow> implements TextConversion {
    public void addRow(char letter, String morse) {
        add(new ConversionRow(letter, morse));
    }

    public String getMorse(char letter) throws MorsinatorParseException {
        letter = Character.toUpperCase(letter);

        for(Node<ConversionRow> node = getFirst(); node != null; node = node.getNext()) {
            ConversionRow row = node.getValue();

            if(row.letter == letter) {
                return row.morse;
            }
        }

        throw new MorsinatorParseException("La lettre " + letter + " n'a pas de traduction dans la table fournie");
    }
}