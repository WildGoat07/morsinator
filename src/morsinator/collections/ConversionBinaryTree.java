package morsinator.collections;

import java.util.function.*;
import java.util.*;

public class ConversionBinaryTree extends MorsiBinaryTree<String, Character> {
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
}
