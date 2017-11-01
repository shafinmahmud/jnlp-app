package net.shafin.crawler.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 11/1/2017
 */
public class DomainSetup {

    private final String root;
    private String acceptingSubDomainPattern;
    private List<String> excludingDomainTerms;

    public DomainSetup(String root) {
        this.root = root;
    }

    public static DomainSetup init(String root) {
        return new DomainSetup(root);
    }

    public DomainSetup accepting(String acceptingSubDomainPattern) {
        this.acceptingSubDomainPattern = acceptingSubDomainPattern;
        return this;
    }

    public DomainSetup exclude(String... domainTerms) {
        this.excludingDomainTerms = Arrays.asList(domainTerms);
        return this;
    }

    public String getRoot() {
        return root;
    }

    public String getAcceptingSubDomainPattern() {
        return acceptingSubDomainPattern;
    }

    public List<String> getExcludingDomainTerms() {
        return excludingDomainTerms;
    }
}
