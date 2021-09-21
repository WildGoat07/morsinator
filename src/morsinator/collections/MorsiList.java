package morsinator.collections;

public class MorsiList<E>
{
	private Node<E> _first;
	private Node<E> _last;
	private int count;

	public static class Node<E>
	{
		public Node(E value, Node<E> previous, Node<E> next)
		{
			_value = value;
			_next = next;
			_previous = previous;
		}

		public E _value;
		protected Node<E> _next;
		protected Node<E> _previous;

		public Node<E> next()
		{
			return _next;
		}

		public Node<E> previous()
		{
			return _previous;
		}
	}

	public Node<E> first()
	{
		return _first;
	}
	
	public Node<E> last()
	{
		return _last;
	}

	public Node<E> add(E item) {
		Node<E> added;

		if (_first == null)
		{
			added = _first = _last = new Node<E>(item, null, null);
		}
		else
		{
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
		if(_first == node) _first = node._next;
		if(_last == node) _last = node._previous;
		if(node._previous != null) node._previous._next = node._next;
		if(node._next != null) node._next._previous = node._previous;

		--count;
		return true;
	}

	public Node<E> insertBefore(Node<E> node, E item) {
		Node<E> added;

		if(node == null) {
			if(_first == null) {
				added = _last = _first = new Node<E>(item, null, null);
			} else {
				added = _last = _last._next = new Node<E>(item, _last, null);
			}
		} else if(node == _first) {
			added = node._previous = _first = new Node<E>(item, null, node);
		} else {
			added = node._previous = node._previous._next = new Node<E>(item, node._previous, node);
		}

		return added;
	}

	public Node<E> insertAfter(Node<E> node, E item) {
		Node<E> added;

		if(node == null) {
			if(_first == null) {
				added = _first = _last = new Node<E>(item, null, null);
			} else {
				added = _first = _first._previous = new Node<E>(item, null, _first);
			}
		} else if(node == _last) {
			added = node._next = _last = new Node<E>(item, node, null);
		} else {
			added = node._next = node._next._previous = new Node<E>(item, node, node._next);
		}

		return added;
	}
}
