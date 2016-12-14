/**
 * @author Srikanth&Giri
 */
public interface IConcurrentQueue<T> {
	public void enque(T key, Logger log);

	public T deque(Logger log);

	public boolean isEmpty(Logger log);

	public void print(Logger log);
}
