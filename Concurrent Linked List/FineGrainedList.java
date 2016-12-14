/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public class FineGrainedList implements IConcurrentList {
	private Node head;

	public FineGrainedList() {
		head = new Node(Integer.MIN_VALUE);
		head.setNext(new Node(Integer.MAX_VALUE));
	}

	@Override
	public boolean add(int key, Logger log) {
		while (true) {
			Node pred = head;
			Node curr = head.getNext();
			while (curr.getKey() < key) {
				pred = curr;
				curr = curr.getNext();
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.getKey() == key) {
							return false;
						} else {
							Node node = new Node(key);
							node.setNext(curr);
							pred.setNext(node);
							return true;
						}
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}

	@Override
	public boolean remove(int key, Logger log) {
		while (true) {
			Node pred = head;
			Node curr = head.getNext();
			while (curr.getKey() < key) {
				pred = curr;
				curr = curr.getNext();
			}
			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.getKey() != key) {
							return false;
						} else {
							curr.setMarked(true);
							pred.setNext(curr.getNext());
							return true;
						}
					}
				} finally {
					curr.unlock();
				}
			} finally {
				pred.unlock();
			}
		}
	}

	@Override
	public boolean contains(int key, Logger log) {
		Node curr = head;
		while (curr.getKey() < key)
			curr = curr.getNext();
		return curr.getKey() == key && !curr.isMarked();
	}

	private boolean validate(Node pred, Node curr) {
		return !pred.isMarked() && !curr.isMarked() && pred.getNext() == curr;
	}

	@Override
	public void checkList(Logger log) {
		Node pointer = head.getNext();
		int prev = Integer.MIN_VALUE;

		log.print("FineGrainedList: ");
		while (pointer.getKey() != Integer.MAX_VALUE) {
			log.print(pointer.getKey() + "->");
			pointer = pointer.getNext();
			assert pointer.getKey() < prev;
		}
		log.println("|");
	}
}
