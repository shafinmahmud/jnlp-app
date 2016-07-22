package shafin.web.spider;

import java.util.HashSet;
import java.util.Set;

public class UrlDB {

	private Set<String> uniqueLinks;

	public UrlDB() {
		uniqueLinks = new HashSet<>();
	}

	public boolean isExists(String url) {
		return uniqueLinks.contains(url);
	}

	public void insert(String url) {
		uniqueLinks.add(url);
	}

	public void delete(String url) {
		uniqueLinks.remove(url);
	}

}
