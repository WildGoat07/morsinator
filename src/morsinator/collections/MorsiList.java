package morsinator.collections;

public class MorsiList<E>
{
	public MorsiList()
	{
		_first = null;
		_last = null;
		count = 0;
	}
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
		protected E _value;
		protected Node<E> _next;
		protected Node<E> _previous;
		public E value()
		{
			return _value;
		}
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
		if (_first == null)
			return false;
		else
			for (Node<E> n = _first;n._next != null;n = n._next)
			if (n._value.equals(item))
				return true;
		return false;
	}

	public boolean remove(Node<E> node) {
		if (_first == node)
			_first = node._next;
		if (_last == node)
			_last = node._previous;
		if (node._next != null)
			node._next._previous = node._previous;
		if (node._previous != null)
			node._previous._next = node._next;
		--count;
		return true;
	}

	public Node<E> insertBefore(Node<E> node, E item) {
		Node<E> added;
		if (node == null)
			added = _first = new Node<E>(item, null, _first);
		else
		{
			added = new Node<E>(item, node._previous, node);
			if (node._previous != null)
				node._previous._next = added;
			node._previous = added;
		}
		return added;
	}
	
	public Node<E> insertAfter(Node<E> node, E item) {
		Node<E> added;
		if (node == null)
			added = _first = new Node<E>(item, null, _first);
		else
		{
			added = new Node<E>(item, node, node._next);
			if (node._next != null)
				node._next._previous = added;
			node._next = added;
		}
		return added;
	}
}
