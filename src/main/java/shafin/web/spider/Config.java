package shafin.web.spider;

public class Config {

	private final String DOMAIN_FILTER_PATTERN;

	public Config(String PATTERN) {
		this.DOMAIN_FILTER_PATTERN = PATTERN;
	}

	public String getDOMAIN_FILTER_PATTERN() {
		return DOMAIN_FILTER_PATTERN;
	}

}
