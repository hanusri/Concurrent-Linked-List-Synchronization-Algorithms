import java.util.concurrent.locks.*;

/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public class CoarseGrainedList implements IConcurrentList {
	private Node head;
	private Lock lock = new ReentrantLock();

	public CoarseGrainedList() {
		head = new Node(Integer.MIN_VALUE);
		head.setNext(new Node(Integer.MAX_VALUE));
	}

	@Override
	public boolean add(int key, Logger log) {
		Node pred, curr;
		lock.lock();
		try {
			pred = head;
			curr = pred.getNext();
			while (curr.getKey() < key) {
				pred = curr;
				curr = curr.getNext();
			}
			if (key == curr.getKey()) {
				return false;
			} else {
				Node node = new Node(key);
				node.setNext(curr);
				pred.setNext(node);
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean remove(int key, Logger log) {
		Node pred, curr;
		lock.lock();
		try {
			pred = head;
			curr = pred.getNext();
			while (curr.getKey() < key) {
				pred = curr;
				curr = curr.getNext();
			}
			if (key == curr.getKey()) {
				pred.setNext(curr.getNext());
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean contains(int key, Logger log) {
		Node curr = head;

		while (curr.getKey() < key)
			curr = curr.getNext();

		return curr.getKey() == key;

	}

	@Override
	public void checkList(Logger log) {
		Node pointer = head.getNext();
		int prev = Integer.MIN_VALUE;

		log.print("CoarseGrainedList: ");
		while (pointer.getKey() != Integer.MAX_VALUE) {
			log.print(pointer.getKey() + "->");
			pointer = pointer.getNext();
			assert pointer.getKey() < prev;
		}
		log.println("|");
	}
}
