package edu.sentise.db;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.sentise.SentiSE;

public class GerritSentiment {

	private static ArrayList<ReviewComment> getSubList(ArrayList<ReviewComment> origList, int start, int end) {
		ArrayList<ReviewComment> sublist = new ArrayList<ReviewComment>();

		for (int i = start; i < end; i++) {
			sublist.add(new ReviewComment(origList.get(i)));
		}

		return sublist;
	}

	private static final int STEP = 500;
	private static final int PER_THREAD = 40;

	public static void main(String args[]) {

		DbConnector connection = new DbConnector("gerrit_android");
		SentiSE sentimentAnalyzer = new SentiSE();

		ArrayList<ReviewComment> inlineComments = connection.getInlineComments();

		System.out.println(inlineComments.size());
		BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(50);

		ThreadPoolExecutor executor = new ThreadPoolExecutor(15, 50, 3600, TimeUnit.SECONDS, blockingQueue);
		executor.prestartAllCoreThreads();

		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				do {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}

				} while (executor.getActiveCount() >= 15);
				executor.execute(r);
			}
		});

		int numElements = inlineComments.size();
		int taskCount = 1;

		for (int start = 0, end = PER_THREAD; start < numElements; start += PER_THREAD, end += PER_THREAD) {

			System.out.println(start + ", " + end);
			if (end >= numElements) {
				end = numElements - 1;

			}

			ParallelEvaluator evaluator = new ParallelEvaluator(connection, getSubList(inlineComments, start, end),
					sentimentAnalyzer, taskCount++);

			do {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}
			} while (executor.getActiveCount() >= 15);
			executor.execute(evaluator);
			evaluator=null;
			System.gc();

		}

		executor.shutdown();

	}
}
