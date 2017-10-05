package net.shafin.crawler.spider;

import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class SpiderConfig {

    private final String ROOT_DOMAIN;
    private final String DOMAIN_FILTER_PATTERN;
    private final List<String> EXCLUDE_STRINGS;

    private final String OUTPUT_FOLDER_PATH;
    private String HOTLINK_PATH;
    private String HISTORY_PATH;
    private String STORAGE_PATH;
    private String FAILED_LINK_PATH;

    public SpiderConfig(String rOOT_DOMAIN, String dOMAIN_FILTER_PATTERN, List<String> eXCLUDE_STRINGS,
                        String outpuFolderPath) {
        ROOT_DOMAIN = rOOT_DOMAIN;
        DOMAIN_FILTER_PATTERN = dOMAIN_FILTER_PATTERN;
        EXCLUDE_STRINGS = eXCLUDE_STRINGS;
        OUTPUT_FOLDER_PATH = outpuFolderPath;

        HOTLINK_PATH = OUTPUT_FOLDER_PATH + "dw.hot";
        HISTORY_PATH = OUTPUT_FOLDER_PATH + "dw.q";
        STORAGE_PATH = OUTPUT_FOLDER_PATH + "dw.txt";
        FAILED_LINK_PATH = OUTPUT_FOLDER_PATH + "dw.fail";
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

    public String getHOTLINK_PATH() {
        return HOTLINK_PATH;
    }

    public void setHOTLINK_PATH(String hOTLINK_PATH) {
        HOTLINK_PATH = hOTLINK_PATH;
    }

    public String getHISTORY_PATH() {
        return HISTORY_PATH;
    }

    public void setHISTORY_PATH(String hISTORY_PATH) {
        HISTORY_PATH = hISTORY_PATH;
    }

    public String getSTORAGE_PATH() {
        return STORAGE_PATH;
    }

    public void setSTORAGE_PATH(String sTORAGE_PATH) {
        STORAGE_PATH = sTORAGE_PATH;
    }

    public String getFAILED_LINK_PATH() {
        return FAILED_LINK_PATH;
    }

    public void setFAILED_LINK_PATH(String fAILED_LINK_PATH) {
        FAILED_LINK_PATH = fAILED_LINK_PATH;
    }

}
