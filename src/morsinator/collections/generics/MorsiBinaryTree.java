package morsinator.collections.generics;

import java.util.function.Function;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

/**
 * Arbre binaire
 * 
 * @param <E> type de la clé
 * @param <F> type de la valeur
 */
public class MorsiBinaryTree<E, F> {
    /**
     * Noeud de l'arbre binaire
     * 
     * @param <E> type de la valeur
     */
    private static class Node<E> {
        public Node<E> leftNode;
        public Node<E> rightNode;
        public E value;
        public boolean hasValue;
    }

    public MorsiBinaryTree(Function<? super E, ? extends Iterable<Boolean>> converter) {
        baseNode = new Node<>();
        this.converter = converter;
    }

    /**
     * Le noeud de départ pour les branches gauche et droite
     */
    private final Node<F> baseNode;
    /**
     * Le convertisseur de la clé. Il permet de transformer la clé passée en une
     * route dans l'abre : soit à gauche, soit à droite. La gauche et la droite sont
     * symbolisé par un booléen
     */
    private final Function<? super E, ? extends Iterable<Boolean>> converter;

    public void clear() {
        baseNode.leftNode = null;
        baseNode.rightNode = null;
    }

    public void set(E key, F value) {
        // Le noeud actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);

        if(route == null)
            throw new IllegalArgumentException();

        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // on créé la branche si elle n'existait pas
                    currentNode.leftNode = new Node<>();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // on créé la branche si elle n'existait pas
                    currentNode.rightNode = new Node<>();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }
        currentNode.value = value;
        currentNode.hasValue = true;
    }

    public boolean containsKey(E key) {
        // Le noeud actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);

        if(route == null)
            throw new IllegalArgumentException();

        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return false;
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    return false;
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }
        // on est arrivés au bout, c'est que la valeur existe
        return currentNode.hasValue;
    }

    public void remove(E key) {
        // Le noeud actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);

        if (route == null)
            throw new IllegalArgumentException();

        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    throw new NoSuchElementException();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    throw new NoSuchElementException();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }

        if (currentNode.hasValue) {
            currentNode.value = null;
            currentNode.hasValue = false;
        } else {
            throw new NoSuchElementException();
        }
    }

    public F get(E key) {
        // Le noeud actuel pour la recherche dans l'abre
        Node<F> currentNode = baseNode;
        Iterable<Boolean> route = converter.apply(key);

        if(route == null)
            throw new IllegalArgumentException();

        // Pour chaque direction à prendre dans l'arbre...
        for (Boolean state : route) {
            if (state) {
                // si il faut prendre la branche de gauche
                if (currentNode.leftNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    throw new NoSuchElementException();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.leftNode;
            } else {
                // sinon on prend la branche de droite
                if (currentNode.rightNode == null)
                    // si la branche n'existe pas, ça sert à rien de continuer
                    throw new NoSuchElementException();
                // on change le noeud actuel pour continuer la recherche
                currentNode = currentNode.rightNode;
            }
        }

        if(currentNode.hasValue) {
            return currentNode.value;
        } else {
            throw new NoSuchElementException();
        }
    }
}
