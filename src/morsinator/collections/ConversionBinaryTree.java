package morsinator.collections;

import morsinator.collections.generics.MorsiBinaryTree;

import java.util.function.*;
import java.util.*;

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
                    return null;
            }
        }
        return result;
    };

    public ConversionBinaryTree() {
        super(morseConvert);
    }

    /**
     * Ajoute un couple lettre et code morse à l'arbre de traduction de morse
     * vers texte.
     *
     * @param letter lettre à associer au code morse
     * @param morse code morse à associer à la lettre
     */
    @Override
    public void addRow(char letter, String morse) {
        boolean contains;

        try {
            contains = containsKey(morse);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Le code \"" + morse + "\" n'est pas un code morse valide, impossible de le sauvegarder dans l'arbre");
        }

        if(contains) {
            throw new IllegalArgumentException("Code morse \"" + morse + "\" déjà ajouté sous la lettre '" + get(morse) + "'");
        } else {
            set(morse, letter);
        }
    }

    /**
     * Traduit un code morse en lettre
     *
     * @param morse code morse à traduire
     * @return lettre correspondant au code morse
     */
    @Override
    public char getLetter(String morse) {
        try {
            return get(morse);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Le code \"" + morse + "\" n'est pas un code morse valide");
        } catch(NoSuchElementException e) {
            throw new IllegalArgumentException("Le code morse \"" + morse + "\" n'a pas de traduction dans la table fournie");
        }
    }

    /**
     * Retire un couple lettre et code morse de la table de traduction de morse
     * vers texte
     *
     * @param letter lettre du couple à supprimer
     * @param morse code morse du couple à supprimer
     */
    @Override
    public void removeRow(char letter, String morse) {
        remove(morse);
    }
}
