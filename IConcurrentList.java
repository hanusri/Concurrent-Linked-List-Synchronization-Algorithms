/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public interface IConcurrentList {
	boolean add(int key, Logger log);

	boolean remove(int key, Logger log);

	boolean contains(int key, Logger log);

	void checkList(Logger log);
}
