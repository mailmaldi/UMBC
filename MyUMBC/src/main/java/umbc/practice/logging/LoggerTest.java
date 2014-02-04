package umbc.practice.logging;

import org.slf4j.*;

public class LoggerTest {

	private static final Logger logger = LoggerFactory
			.getLogger(LoggerTest.class);

	public static void main(String[] args) {
		logger.info("hello");
	}

}
