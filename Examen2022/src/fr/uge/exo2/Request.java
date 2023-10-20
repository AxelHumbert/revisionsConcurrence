package fr.uge.exo2;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Request {

	private final String site;
	private final String item;
	private final Object lock = new Object();
	private boolean started;

	private final static String[] ARRAY_ALL_SITES = { "amazon.fr", "amazon.uk", "darty.fr", "fnac.fr", "boulanger.fr",
			"cdiscount.fr", "tombeducamion.fr", "leboncoin.fr", "ebay.fr", "ebay.com", "laredoute.fr",
			"les3suisses.fr" };
	// private final static String[] ARRAY_ALL_SITES = IntStream.range(0,
	// 1000).mapToObj(String::valueOf).toArray(x->new String[x]);
	private final static Set<String> SET_ALL_SITES = Set.of(ARRAY_ALL_SITES);
	public final static List<String> ALL_SITES = Collections.unmodifiableList(List.of(ARRAY_ALL_SITES));
	public final static int NB_SITES = ARRAY_ALL_SITES.length;

	public Request(String site, String item) {
		if (!SET_ALL_SITES.contains(site)) {
			throw new IllegalStateException();
		}
		if (!validateNoWait(item)) {
			throw new IllegalArgumentException();
		}
		this.site = site;
		this.item = item;
	}

	public String item() {
		return item;
	}

	private static boolean validateNoWait(String item) {
		return Math.abs(item.hashCode()) % 500 < 250;
	}

	/**
	 * Check if an item can be requested
	 *
	 * This method can only be called once. All further calls will throw an
	 * IllegalStateException
	 *
	 *
	 * @param item
	 * @throws InterruptedException
	 */
	public static boolean validate(String item) throws InterruptedException {
		Thread.sleep(ThreadLocalRandom.current().nextInt(5) * 300);
		return validateNoWait(item);
	}

	@Override
	public String toString() {
		return item + "@" + site;
	}

	/**
	 * Performs the request the price for the item on the site waiting at most
	 * timeoutMilli milliseconds. The returned Answered is not guaranteed to be
	 * successful.
	 *
	 * This method can only be called once. All further calls will throw an
	 * IllegalStateException
	 *
	 *
	 * @param timeoutMilli
	 * @throws InterruptedException
	 */
	public Answer request(int timeoutMilli) throws InterruptedException {
		synchronized (lock) {
			if (started) {
				throw new IllegalStateException();
			}
			started = true;
		}
		System.out.println("DEBUG : starting request for " + item + " on " + site);
		if (item.equals("pokeball")) {
			System.out.println("DEBUG : " + item + " is not available on " + site);
			return Answer.empty();
		}
		long hash1 = Math.abs((site + "|" + item).hashCode());
		long hash2 = Math.abs((item + "|" + site).hashCode());
		if ((hash1 % 1000 < 400) || ((hash1 % 1000) * 2 > timeoutMilli)) { // simulating timeout
			Thread.sleep(timeoutMilli);
			System.out.println("DEBUG : Request " + toString() + " has timeout");
			return Answer.empty();
		}
		Thread.sleep((hash1 % 1000) * 2);
		if ((hash1 % 1000 < 500)) {
			System.out.println("DEBUG : " + item + " is not available on " + site);
			return Answer.empty();
		}
		int price = (int) (hash2 % 1000) + 1;
		System.out.println("DEBUG : " + item + " costs " + price + " on " + site);
		return Answer.of(site, item, price);
	}

	public static void main(String[] args) {
		var item = "pikachu";
		try {
			if (Request.validate(item)) {
				var request = new Request("amazon.fr", item);
				var answer = request.request(5_000);
				if (answer.isSuccessful()) {
					System.out.println("The price is " + answer.price());
				} else {
					System.out.println("The price could not be retrieved from the site");
				}
			}
		} catch (InterruptedException e) {
			throw new AssertionError();
		}
	}
}