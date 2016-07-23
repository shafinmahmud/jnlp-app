package shafin.web.crawler;

import java.util.List;

public class Config {

	private final String ROOT_DOMAIN;
	private final String DOMAIN_FILTER_PATTERN;
	private final List<String> EXCLUDE_STRINGS;

	public Config(String ROOT, String PATTERN, List<String> EXCLUDES) {
		this.ROOT_DOMAIN = ROOT;
		this.DOMAIN_FILTER_PATTERN = PATTERN;
		this.EXCLUDE_STRINGS = EXCLUDES;
	}

	public String getROOT_DOMAIN() {
		return ROOT_DOMAIN;
	}

	public String getDOMAIN_FILTER_PATTERN() {
		return DOMAIN_FILTER_PATTERN;
	}

	public List<String> getEXCLUDE_STRINGS() {
		return EXCLUDE_STRINGS;
	}

}
