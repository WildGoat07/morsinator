package morsinator.collections;

import morsinator.collections.generics.MorsiBinaryTree;

import java.util.function.*;
import java.util.*;

import morsinator.reader.ConversionReaderException;

public class ConversionBinaryTree extends MorsiBinaryTree<String, Character> implements MorseConversion {
    /**
     * Simple délégué pour convertir une clé en morse en une route
     * 
     * @param key clé d'un valeur d'un arbre binaire
     * @return route vers la valeur
     */
    private final static Function<String, List<Boolean>> morseConvert = (key) -> {
        List<Boolean> result = new ArrayList<>(key.length());
        for (int i = 0; i < key.length(); ++i) {
            char current = key.charAt(i);
            switch (current) {
                case '_':
                case '-':
                    result.add(false);
                    break;
                case '.':
                    result.add(true);
                    break;
                default:
                    throw new RuntimeException("Charactère '" + current + "' invalide");
            }
        }
        return result;
    };

    public ConversionBinaryTree() {
        super(morseConvert);
    }

    public void addRow(char letter, String morse) throws AddException {
        if(get(morse) != null)
            throw new AddException("Code morse de '" + letter + "' d\u00e9j\u00e0 ajout\u00e9 sous la lettre '" + get(morse) + "'");

        try {
            set(morse, letter);
        } catch(RuntimeException e) {
            throw new AddException(e);
        }
    }

    public char getLetter(String morse) {
        return get(morse);
    }
}
