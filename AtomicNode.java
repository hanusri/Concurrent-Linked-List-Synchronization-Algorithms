import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Created by Srikanth& Giri on 10/30/2016.
 */
public class AtomicNode {
	private int key;
	private AtomicMarkableReference<AtomicNode> aMRnext;

	public AtomicNode(int key) {
		this.key = key;
		aMRnext = new AtomicMarkableReference<>(null, false);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public AtomicNode getNextReference() {
		return aMRnext.getReference();
	}

	public AtomicNode getNextMarked(boolean[] marked) {
		return aMRnext.get(marked);
	}

	public boolean compareAndSetNext(AtomicNode curr, AtomicNode succ, boolean currFlag, boolean succFlag) {
		return aMRnext.compareAndSet(curr, succ, currFlag, succFlag);
	}

	public void setNext(AtomicNode newNext) {
		aMRnext.set(newNext, false);
	}

	public boolean attemptMark(AtomicNode succ, boolean mark) {
		return aMRnext.attemptMark(succ, mark);
	}
}
