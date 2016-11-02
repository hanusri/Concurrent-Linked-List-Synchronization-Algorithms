
/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public class LockFreeList implements IConcurrentList {
	private AtomicNode head;

	public LockFreeList() {
		head = new AtomicNode(Integer.MIN_VALUE);
		head.setNext(new AtomicNode(Integer.MAX_VALUE));
	}

	@Override
	public boolean add(int key, Logger log) {
		while (true) {
			Window window = find(key, log);
			AtomicNode pred = window.getPred(), curr = window.getCurr();
			if (curr.getKey() == key) {
				// log.println("Key already present");
				return false;

			} else {
				AtomicNode node = new AtomicNode(key);
				node.setNext(curr);
				// log.println("Trying to link new node");
				if (pred.compareAndSetNext(curr, node, false, false)) {
					// log.println("New node linked");
					return true;
				}
			}
		}
	}

	@Override
	public boolean remove(int key, Logger log) {
		boolean flag;
		/* infinite loop until either true or false is returned */
		while (true) {
			Window window = find(key, log);
			AtomicNode pred = window.getPred(), curr = window.getCurr();
			if (curr.getKey() != key) {
				// log.println("Key already not present");
				return false;

			} else {
				AtomicNode succ = curr.getNextReference();
				flag = curr.attemptMark(succ, true);
				/*
				 * loop continues running until current node(to be removed) mark
				 * bit is set 1
				 */
				// log.println("Trying mark node");
				if (!flag)
					continue;
				// log.println("Node marked");
				/*
				 * not necessarily compare and set will return true. If it
				 * returns false, then find method has helping mechanism to
				 * change the pred's next
				 */
				pred.compareAndSetNext(curr, succ, false, false);
				return true;
			}
		}
	}

	@Override
	public boolean contains(int key, Logger log) {
		/* use the find method which does helping */
		Window window = find(key, log);
		return window.getCurr().getKey() == key;
	}

	private Window find(int key, Logger log) {
		// log.println("Finding: " + key);
		AtomicNode pred = null;
		AtomicNode curr = null;
		AtomicNode succ = null;
		boolean[] marked = new boolean[] { false };
		boolean flag = true;
		retry: while (true) {
			pred = head;
			curr = pred.getNextReference();
			while (true) {
				succ = curr.getNextMarked(marked);
				while (marked[0]) {
					flag = pred.compareAndSetNext(curr, succ, false, false);
					/*
					 * unlike remove method, here loop continues until
					 * compareAndSet returns true making sure pred next is
					 * change
					 */
					if (!flag)
						continue retry;
					curr = succ;
					succ = curr.getNextMarked(marked);
				}
				if (curr.getKey() >= key) {
					// log.println("Done: " + key);
					return new Window(pred, curr);
				}
				pred = curr;
				curr = succ;
			}
		}
	}

	@Override
	public void checkList(Logger log) {
		AtomicNode pointer = head.getNextReference();
		int prev = Integer.MIN_VALUE;

		log.print("LockFreeList: ");
		while (pointer.getKey() != Integer.MAX_VALUE) {
			log.print(pointer.getKey() + "->");
			pointer = pointer.getNextReference();
			assert pointer.getKey() < prev;
		}
		log.println("|");
	}
}

class Window {
	private AtomicNode pred, curr;

	public Window(AtomicNode prev, AtomicNode curr) {
		this.pred = prev;
		this.curr = curr;
	}

	public AtomicNode getPred() {
		return pred;
	}

	public void setPred(AtomicNode pred) {
		this.pred = pred;
	}

	public AtomicNode getCurr() {
		return curr;
	}

	public void setCurr(AtomicNode curr) {
		this.curr = curr;
	}
}
