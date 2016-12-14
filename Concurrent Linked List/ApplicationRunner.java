import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Srikanth on 10/23/2016.
 */
public class ApplicationRunner {
	private static int maxThreads;
	private static int operations;
	private static int[] distribution;
	private static ExecutorService executor;
	private static int maxSize = 1000;
	private static Logger log;

	private static int phase = 0;
	private static long startTime, endTime, elapsedTime;

	public static void main(String[] args) {
		if (args.length < 1)
			maxThreads = 4;
		else
			maxThreads = Integer.parseInt(args[0]);

		if (args.length < 2)
			operations = 1000000;
		else
			operations = Integer.parseInt(args[1]);

		if (args.length < 3)
			distribution = new int[] { 50, 75, 100 };
		else {
			String[] tokens = args[2].split(",");
			int prev = 0;
			distribution = new int[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				distribution[i] = Integer.parseInt(tokens[i]) + prev;
				prev = distribution[i];
			}
		}

		log = new Logger(System.out);
		for (int i = 2; i <= maxThreads; i += 2) {
			StringBuilder sb = new StringBuilder();
			sb.append("RunTime " + i + ": ");
			for (int j = 0; j < 3; j++) {
				IConcurrentList list;
				switch (j) {
				case 0:
					list = new CoarseGrainedList();
					break;
				case 1:
					list = new FineGrainedList();
					break;
				default:
					list = new LockFreeList();
					break;
				}
				sb.append(runCase(i, list) + ",");
			}
			log.println(sb.toString());
		}
		log.close();
	}

	public static long runCase(int threads, IConcurrentList list) {
		setup(list);
		List<Callable<Object>> calls = new ArrayList<Callable<Object>>();
		for (int i = 0; i < threads; i++)
			calls.add(Executors.callable(new ApplicationThread(list, i, operations, distribution, maxSize)));
		executor = Executors.newFixedThreadPool(maxThreads);
		timer();
		try {
			executor.invokeAll(calls);
			executor.shutdown();
			long time = timer();
			list.checkList(log);
			return time;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public static void setup(IConcurrentList list) {
		Random r = new Random();
		Logger log = new Logger("setup.log");
		for (int i = 0; i < maxSize / 2; i++)
			list.add(r.nextInt(maxSize), log);
		log.close();
	}

	/**
	 * Timer to calculate the running time
	 */
	public static long timer() {
		if (phase == 0) {
			startTime = System.currentTimeMillis();
			phase = 1;
		} else {
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			phase = 0;
		}
		return elapsedTime;
	}
}