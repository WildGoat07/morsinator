package morsinator.collections;

public class MorsiList<E> {
    private Node<E> first;
    private Node<E> last;
    private int count;

    public static class Node<E> {
        public Node(E value, Node<E> previous, Node<E> next) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }

        private E value;
        protected Node<E> next;
        protected Node<E> previous;

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrevious() {
            return previous;
        }
    }

    public Node<E> getFirst() {
        return first;
    }

    public Node<E> getLast() {
        return last;
    }

    public Node<E> add(E item) {
        Node<E> added;

        if (first == null) {
            added = first = last = new Node<E>(item, null, null);
        } else {
            added = last = last.next = new Node<E>(item, last, null);
        }

        ++count;
        return added;
    }

    public void clear() {
        first = last = null;
        count = 0;
    }

    public boolean contains(E item) {
        for (Node<E> n = first; n != null; n = n.next)
            if (n.value.equals(item))
                return true;

        return false;
    }

    public boolean remove(Node<E> node) {
        if (first == node)
            first = node.next;
        if (last == node)
            last = node.previous;
        if (node.previous != null)
            node.previous.next = node.next;
        if (node.next != null)
            node.next.previous = node.previous;

        --count;
        return true;
    }

    public Node<E> insertBefore(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == first) {
            node.previous = first = new Node<E>(item, null, node);
        } else {
            node.previous = node.previous.next = new Node<E>(item, node.previous, node);
        }

        return node.previous;
    }

    public Node<E> insertAfter(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == last) {
            node.next = last = new Node<E>(item, node, null);
        } else {
            node.next = node.next.previous = new Node<E>(item, node, node.next);
        }

        return node.next;
    }
}
