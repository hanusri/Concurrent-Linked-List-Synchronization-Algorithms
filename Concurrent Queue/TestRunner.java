import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Srikanth&Giri
 */
public class TestRunner {
	private static int maxThreads;
	private static int operations;
	private static ExecutorService executor;
	private static Logger log;
	public static int enquers;
	public static AtomicInteger enqueing = new AtomicInteger(0);

	public static void main(String[] args) {
		if (args.length < 1)
			maxThreads = 8;
		else
			maxThreads = Integer.parseInt(args[0]);

		if (args.length < 2)
			operations = 1000000;
		else
			operations = Integer.parseInt(args[1]);

		log = new Logger(System.out);
		for (int i = 2; i <= maxThreads; i += 2) {
			for (int j = 0; j < 2; j++) {
				IConcurrentQueue<TestThread.Key> queue;
				switch (j) {
				case 0:
					queue = new LockBasedQueue<>();
					break;
				default:
					queue = new LockFreeQueue<>();
					break;
				}
				log.println("ThreadCount: " + i + ", " + queue.getClass().getName() + " - " + runCase(i, queue, log));
			}
		}
		log.close();
	}

	public static Boolean runCase(int threads, IConcurrentQueue<TestThread.Key> queue, Logger log) {
		List<Callable<Object>> calls = new ArrayList<Callable<Object>>();
		for (int i = 0; i < threads - 1; i++)
			calls.add(Executors.callable(new TestThread.EnqThread(queue, i, operations)));
		enquers = threads - 1;
		enqueing.set(threads - 1);
		calls.add(new TestThread.DeqThread(queue, threads - 1, operations));
		executor = Executors.newFixedThreadPool(maxThreads);
		try {
			List<Future<Object>> results = executor.invokeAll(calls);
			executor.shutdown();
			if ((Boolean) results.get(threads - 1).get())
				return true;
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}
}