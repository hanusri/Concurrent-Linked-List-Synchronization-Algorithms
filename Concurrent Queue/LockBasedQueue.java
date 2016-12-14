import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Srikanth&Giri
 */
public class LockBasedQueue<T> implements IConcurrentQueue<T> {
	private Node<T> head, tail;
	private Lock enqLock, deqLock;

	public LockBasedQueue() {
		head = new Node<>();
		tail = head;
		enqLock = new ReentrantLock();
		deqLock = new ReentrantLock();
	}

	@Override
	public void enque(T key, Logger log) {
		enqLock.lock();
		try {
			Node<T> node = new Node<>(key);
			tail.next = node;
			tail = node;
		} finally {
			enqLock.unlock();
		}
	}

	@Override
	public T deque(Logger log) {
		T key;
		deqLock.lock();
		try {
			if (isEmpty(log)) {
				throw new NoSuchElementException("Queue is empty");
			}
			key = head.next.key;
			head = head.next;
		} finally {
			deqLock.unlock();
		}
		return key;
	}

	@Override
	public boolean isEmpty(Logger log) {
		return head.next == null;
	}

	@Override
	public void print(Logger log) {
		log.print("LockBasedQueue: ");
		Node<T> curr = head.next;
		while (curr != null) {
			log.print(curr.key + " -> ");
			curr = curr.next;
		}
		log.println("|");
	}

	private static class Node<T> {
		T key;
		Node<T> next;

		public Node() {
			this.key = null;
			this.next = null;
		}

		public Node(T key) {
			this.key = key;
			this.next = null;
		}
	}
}
