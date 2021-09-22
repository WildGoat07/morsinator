package morsinator.collections;

public class MorsiList<E> {
    private Node<E> _first;
    private Node<E> _last;
    private int count;

    public static class Node<E> {
        public Node(E value, Node<E> previous, Node<E> next) {
            _value = value;
            _next = next;
            _previous = previous;
        }

        private E _value;
        protected Node<E> _next;
        protected Node<E> _previous;

        public E getValue() {
            return _value;
        }

        public void setValue(E value) {
            _value = value;
        }

        public Node<E> getNext() {
            return _next;
        }

        public Node<E> getPrevious() {
            return _previous;
        }
    }

    public Node<E> getFirst() {
        return _first;
    }

    public Node<E> getLast() {
        return _last;
    }

    public Node<E> add(E item) {
        Node<E> added;

        if (_first == null) {
            added = _first = _last = new Node<E>(item, null, null);
        } else {
            added = _last = _last._next = new Node<E>(item, _last, null);
        }

        ++count;
        return added;
    }

    public void clear() {
        _first = _last = null;
        count = 0;
    }

    public boolean contains(E item) {
        for (Node<E> n = _first; n != null; n = n._next)
            if (n._value.equals(item))
                return true;

        return false;
    }

    public boolean remove(Node<E> node) {
        if (_first == node)
            _first = node._next;
        if (_last == node)
            _last = node._previous;
        if (node._previous != null)
            node._previous._next = node._next;
        if (node._next != null)
            node._next._previous = node._previous;

        --count;
        return true;
    }

    public Node<E> insertBefore(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == _first) {
            node._previous = _first = new Node<E>(item, null, node);
        } else {
            node._previous = node._previous._next = new Node<E>(item, node._previous, node);
        }

        return node._previous;
    }

    public Node<E> insertAfter(Node<E> node, E item) {
        if (node == null) {
            throw new NullPointerException("node ne peut pas être null");
        } else if (node == _last) {
            node._next = _last = new Node<E>(item, node, null);
        } else {
            node._next = node._next._previous = new Node<E>(item, node, node._next);
        }

        return node._next;
    }
}
