package net.shafin.crawler.model;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class EnvSetup {

    private final String outputDirectory;
    private String HOTLINK_PATH;
    private String HISTORY_PATH;
    private String STORAGE_PATH;
    private String FAILED_LINK_PATH;

    public EnvSetup(String outputFolderPath) {
        outputDirectory = outputFolderPath;

        HOTLINK_PATH = outputDirectory + "_hot.txt";
        HISTORY_PATH = outputDirectory + "_stale.txt";
        STORAGE_PATH = outputDirectory + "dw.txt";
        FAILED_LINK_PATH = outputDirectory + "_fail.txt";
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
