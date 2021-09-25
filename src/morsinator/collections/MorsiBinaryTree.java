package morsinator.collections;

import java.util.function.Function;
import java.util.*;

/**
 * Arbre binaire
 * 
 * @param <E> type de la clé
 * @param <F> type de la valeur
 */
public class MorsiBinaryTree<E, F> {
    /**
     * Simple délégué pour convertir une clé en morse en une route
     * 
     * @param key clé d'un valeur d'un arbre binaire
     * @return route vers la valeur
     */
    public final static Function<String, List<Boolean>> morseConvert = (key) -> {
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

    /**
     * Noeux de l'arbre binaire
     * 
     * @param <E> type de la valeur
     */
    private static class Node<E> {
        public Node<E> leftNode;
        public Node<E> rightNode;
        public E value;
    }

    public MorsiBinaryTree(Function<? super E, ? extends Iterable<Boolean>> converter) {
        baseNode = new Node<>();
        this.converter = converter;
    }

    /**
     * Le noeux de départ pour les branches gauche et droite
     */
    private final Node<F> baseNode;
    /**
     * Le convertisseur de la clé. Il permet de transformer la clé passée en une
     * route dans l'abre : soit à gauche, soit à droite. La gauche et la droite sont
     * symbolisé par un booléen
     */
    private final Function<? super E, ? extends Iterable<Boolean>> converter;

    public void set(E key, F value) {
        // Le noeux actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);
        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // on créé la branche si elle n'existait pas
                    currentNode.leftNode = new Node<>();
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // on créé la branche si elle n'existait pas
                    currentNode.rightNode = new Node<>();
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }
        currentNode.value = value;
    }

    public boolean containsKey(E key) {
        // Le noeux actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);
        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return false;
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return false;
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }
        // on est arrivés au bout, c'est que la valeur existe
        return true;
    }

    public F get(E key) {
        // Le noeux actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);
        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return null;
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return null;
                // on change le noeux actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }
        return currentNode.value;
    }
}
