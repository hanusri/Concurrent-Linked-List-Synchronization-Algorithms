import java.util.concurrent.locks.*;

/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public class Node {
	private int key;
	private Node next;
	private Lock lock;
	private boolean marked;

	public Node(int key) {
		this.key = key;
		lock = new ReentrantLock();
		marked = false;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public void lock() {
		lock.lock();
	}

	public void unlock() {
		lock.unlock();
	}
}
