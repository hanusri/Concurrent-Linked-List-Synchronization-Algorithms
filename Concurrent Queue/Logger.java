import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Logger {
	private PrintWriter log;

	public Logger(String filename) {
		try {
			log = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Logger(OutputStream stream) {
		log = new PrintWriter(stream);
	}

	public void print(String msg) {
		log.print(msg);
	}

	public void println(String msg) {
		log.println(msg);
	}

	public void close() {
		log.flush();
		log.close();
	}
}
