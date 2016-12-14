import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Srikanth&Giri
 */
public class LockFreeQueue<T> implements IConcurrentQueue<T> {
	private AtomicReference<Node<T>> head, tail;

	public LockFreeQueue() {
		Node<T> sentinel = new Node<>();
		head = new AtomicReference<>(sentinel);
		tail = new AtomicReference<>(sentinel);
	}

	@Override
	public void enque(T key, Logger log) {
		Node<T> node = new Node<>(key);
		while (true) {
			Node<T> last = tail.get();
			Node<T> next = last.next.get();
			if (last == tail.get()) {
				if (next == null) {
					if (last.next.compareAndSet(next, node)) {
						tail.compareAndSet(last, node);
						return;
					}
				} else {
					tail.compareAndSet(last, next);
				}
			}
		}
	}

	@Override
	public T deque(Logger log) {
		while (true) {
			Node<T> first = head.get();
			Node<T> last = tail.get();
			Node<T> next = first.next.get();
			if (first == head.get()) {
				if (first == last) {
					if (next == null) {
						throw new NoSuchElementException("Queue is empty");
					}
					tail.compareAndSet(last, next);
				} else {
					T key = next.key;
					if (head.compareAndSet(first, next))
						return key;
				}
			}
		}
	}

	@Override
	public boolean isEmpty(Logger log) {
		return head.get().next.get() == null;
	}

	@Override
	public void print(Logger log) {
		log.print("LockBasedQueue: ");
		Node<T> curr = head.get().next.get();
		while (curr != null) {
			log.print(curr.key + " -> ");
			curr = curr.next.get();
		}
		log.println("|");
	}

	private static class Node<T> {
		T key;
		AtomicReference<Node<T>> next;

		public Node() {
			this.key = null;
			this.next = new AtomicReference<>(null);
		}

		public Node(T key) {
			this.key = key;
			this.next = new AtomicReference<>(null);
		}
	}
}
