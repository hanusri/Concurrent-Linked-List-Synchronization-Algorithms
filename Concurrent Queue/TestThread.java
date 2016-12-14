import java.util.concurrent.Callable;

/**
 * @author Srikanth&Giri
 */
public class TestThread {
	public static class Key {
		public int threadId;
		public int key;

		public Key(int threadId, int key) {
			this.threadId = threadId;
			this.key = key;
		}

		@Override
		public String toString() {
			return "(" + threadId + ", " + key + ")";
		}
	}

	public static class EnqThread implements Runnable {
		private int threadId;
		private IConcurrentQueue<Key> queue;
		private int operations;
		Logger log;

		public EnqThread(IConcurrentQueue<Key> queue, int threadId, int operations) {
			this.queue = queue;
			this.threadId = threadId;
			this.operations = operations;
			log = new Logger(threadId + ".log");
		}

		@Override
		public void run() {
			for (int i = 0; i < operations; i++) {
				Key key = new Key(threadId, i);
				log.println(threadId + ": Enque: " + i);
				queue.enque(key, log);
			}
			queue.print(log);
			TestRunner.enqueing.getAndDecrement();
			log.close();
		}
	}

	public static class DeqThread implements Callable<Object> {
		private int threadId;
		private IConcurrentQueue<Key> queue;
		Logger log;

		public DeqThread(IConcurrentQueue<Key> queue, int threadId, int operations) {
			this.queue = queue;
			this.threadId = threadId;
			log = new Logger(threadId + ".log");
		}

		public Boolean call() {
			int[] counters = new int[TestRunner.enquers];
			for (int i = 0; i < TestRunner.enquers; i++)
				counters[i] = -1;
			Key key;
			Boolean result = true;
			while (TestRunner.enqueing.get() > 0 || !queue.isEmpty(log)) {
				while (!queue.isEmpty(log)) {
					key = queue.deque(log);
					log.println(key.threadId + ": Deque: " + key.key);
					counters[key.threadId]++;
					if (counters[key.threadId] != key.key) {
						log.println(
								key.threadId + ": ERROR: Expected-" + counters[key.threadId] + ", Obtained-" + key.key);
						result = false;
					}
				}
			}
			queue.print(log);
			if (!queue.isEmpty(log)) {
				log.println(threadId + ": ERROR: queue is not empty after all deque");
				result = false;
			}
			log.close();
			return result;
		}
	}
}
