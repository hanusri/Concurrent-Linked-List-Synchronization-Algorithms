import java.util.Random;

/**
 * Created by Srikanth&Giri on 10/23/2016.
 */
public class ApplicationThread implements Runnable {
	private int threadId;
	private IConcurrentList list;
	private int operations;
	private int[] distribution;
	private int maxSize;
	private Random r;
	Logger log;

	public ApplicationThread(IConcurrentList list, int threadId, int operations, int[] distribution, int maxSize) {
		this.list = list;
		this.threadId = threadId;
		this.operations = operations;
		this.distribution = distribution;
		this.maxSize = maxSize;
		this.r = new Random();
		log = new Logger(threadId + ".log");
	}

	@Override
	public void run() {
		for (int i = 0; i < operations; i++) {
			int p = r.nextInt(100);
			int k = r.nextInt(maxSize);
			if (p < distribution[0]) {
				log.println(threadId + ": Search: " + k);
				list.contains(k, log);
			} else if (p < distribution[1]) {
				log.println(threadId + ": Insert: " + k);
				list.add(k, log);
			} else {
				log.println(threadId + ": Delete: " + k);
				list.remove(k, log);
			}
		}
		log.close();
	}
}
