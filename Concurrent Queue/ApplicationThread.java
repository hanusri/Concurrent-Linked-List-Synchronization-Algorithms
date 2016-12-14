import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Srikanth&Giri
 */
public class ApplicationThread implements Runnable {
	private int threadId;
	private IConcurrentQueue<Integer> queue;
	private int operations;
	private int[] distribution;
	private Random r;
	Logger log;

	public ApplicationThread(IConcurrentQueue<Integer> queue, int threadId, int operations, int[] distribution) {
		this.queue = queue;
		this.threadId = threadId;
		this.operations = operations;
		this.distribution = distribution;
		this.r = new Random();
		log = new Logger(threadId + ".log");
	}

	@Override
	public void run() {
		for (int i = 0; i < operations; i++) {
			int p = r.nextInt(100);
			if (p < distribution[0]) {
				log.println(threadId + ": Enque: " + p);
				queue.enque(p, log);

			} else if (p < distribution[1]) {
				log.print(threadId + ": Deque: ");
				try {
					log.println(String.valueOf(queue.deque(log)));
				} catch (NoSuchElementException e) {
					log.println(e.getMessage());
				}

			} else {
				log.print(threadId + ": isEmpty: ");
				log.println(String.valueOf(queue.isEmpty(log)));
			}
		}
		queue.print(log);
		log.close();
	}
}
