package morsinator.collections.generics;

/**
 * Implémentation personnalisée de liste chaînée
 */
public class MorsiList<E> {
    private Node<E> first;
    private Node<E> last;

    /**
     * Noeud de la liste chaînée
     */
    public static class Node<E> {
        /**
         * Constructeur
         * 
         * @param value    valeur du noeud
         * @param previous noeud précédent
         * @param next     noeud suivant
         */
        public Node(E value, Node<E> previous, Node<E> next) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }

        private E value;
        protected Node<E> next;
        protected Node<E> previous;

        /**
         * Renvoie la valeur du noeud
         * 
         * @return la valeur du noeud
         */
        public E getValue() {
            return value;
        }

        /**
         * Change la valeur du neud
         * 
         * @param value nouvelle valeur du noeud
         */
        public void setValue(E value) {
            this.value = value;
        }

        /**
         * Renvoie le noeud suivant ou null s'il n'y a pas de noeud suivant
         * 
         * @return le noeud suivant ou null s'il n'y a pas de noeud suivant
         */
        public Node<E> getNext() {
            return next;
        }

        /**
         * Renvoie le noeud précédent ou null s'il n'y a pas de noeud précédent
         * 
         * @return le noeud précédent ou null s'il n'y a pas de noeud précédent
         */
        public Node<E> getPrevious() {
            return previous;
        }
    }

    /**
     * Renvoie le premier noeud ou null si la liste est vide
     * 
     * @return le premier noeud ou null si la liste est vide
     */
    public Node<E> getFirst() {
        return first;
    }

    /**
     * Renvoie le dernier noeud ou null si la liste est vide
     * 
     * @return le dernier noeud ou null si la liste est vide
     */
    public Node<E> getLast() {
        return last;
    }

    /**
     * Ajoute un élément à la fin de la liste
     * 
     * @param item nouvel élément à ajouter à la liste
     * @return le nouveau noeud créé par l'ajout de l'élément à la liste
     */
    public Node<E> add(E item) {
        Node<E> added;

        if (first == null) {
            // si la liste est vide, on change le premier et dernier noeud
            added = first = last = new Node<E>(item, null, null);
        } else {
            // sinon on change uniquement le dernier noeud en passant celui ci comme noeud
            // précédent
            added = last = last.next = new Node<E>(item, last, null);
        }

        return added;
    }

    /**
     * Vide la liste
     */
    public void clear() {
        first = last = null;
    }

    /**
     * Renvoie vrai si la liste contiens la valeur donnée
     * 
     * @param item la valeur à vérifier
     * @return vrai si la liste contiens la valeur donnée
     */
    public boolean contains(E item) {
        for (Node<E> n = first; n != null; n = n.next)
            // pour chaque élément on vérifie s'il est égal
            if (n.value.equals(item))
                return true;

        return false;
    }

    /**
     * Supprime un noeud de la liste
     * 
     * @param node noeud de la liste à supprimer
     * @return vrai si la suppression est réussie
     */
    public boolean remove(Node<E> node) {
        if (first == node)
            // si on supprime le premier noeud, il faut changer la référence
            first = node.next;
        if (last == node)
            // si on supprime le dernier noeud, il faut changer la référence
            last = node.previous;
        if (node.previous != null)
            // si le noeud possède un précédent, on change la référence du précédent
            node.previous.next = node.next;
        if (node.next != null)
            // si le noeud possède un suivant, on change la référence du suivant
            node.next.previous = node.previous;

        return true;
    }

    /**
     * Insère un élement avant un noeud
     * 
     * @param node position de l'insertion
     * @param item valeur à insérer
     * @return le nouveau noeud créé
     */
    public Node<E> insertBefore(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == first) {
            // si on ajoute avant le premier, on change la référence
            node.previous = first = new Node<E>(item, null, node);
        } else {
            // sinon on change aussi la référence du noeud précédent
            node.previous = node.previous.next = new Node<E>(item, node.previous, node);
        }

        return node.previous;
    }

    /**
     * Insère un élément après un noeud
     * 
     * @param node position de l'insertion
     * @param item élément à insérer
     * @return le nouveau noeud créé
     */
    public Node<E> insertAfter(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == last) {
            // si on ajoute après le dernier, on change la référence
            node.next = last = new Node<E>(item, node, null);
        } else {
            // sinon on change aussi la référence du noeud suivant
            node.next = node.next.previous = new Node<E>(item, node, node.next);
        }

        return node.next;
    }
}
