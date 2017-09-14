package org.yujoo.test.benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peak.core.net.httpclient.Http;

public class HttpTestBenchmark {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpTestBenchmark.class);

	static AtomicLong atomic = new AtomicLong();

	public static void main(String[] args) throws Exception {
		LOGGER.info("http benchmark...");

		// final String url = "http://localhost:9001/";
		//final String url = "http://localhost:8080/hello/foo";
		final String url = "http://127.0.0.1:8080";

		
		int threadNumber = 64;
		final int roundNumber = 5000;
		Thread[] threads = new Thread[threadNumber];

		final CountDownLatch latch = new CountDownLatch(threadNumber);
		long start = System.currentTimeMillis();

		for (int i = 0; i < threadNumber; i++) {
			threads[i] = new Thread() {
				@Override
				public void run() {

					for (int i = 0; i < roundNumber; i++) {
						Http.get(url);
						atomic.incrementAndGet();
					}

					latch.countDown();
				}
			};
			threads[i].start();
		}

		latch.await();

		long end = System.currentTimeMillis();

		LOGGER.info("总耗时:{}ms", end - start);
		LOGGER.info((long) (((double) (threadNumber * roundNumber) * 1000 / (end - start))) + " QPS");
		LOGGER.info("END");

		LOGGER.info("size:{}", atomic.get());
	}
}
