package net.shafin.nlp.main;

import net.shafin.common.db.DataSource;
import net.shafin.common.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Shafin Mahmud
 * @since 11/18/2017
 */
public class AppBootProcess {

    public static DataSource dataSource;

    private static final String INDEX_DIR = "_data/_index/";
    public static final String ZERO_FREQ_FILE = INDEX_DIR + "zero_freq_terms.txt";
    public static final String STOP_FILTERED_FILE = INDEX_DIR + "stop_filtered_terms.txt";
    public static final String VERB_SUFFIX_FILTERED_FILE = INDEX_DIR + "verb_suffix_filtered_terms.txt";

    public static void init() {

        try {
            dataSource = new DataSource("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //initialize required files
        FileUtil.createFileIfNotExist(ZERO_FREQ_FILE);
        FileUtil.createFileIfNotExist(STOP_FILTERED_FILE);
        FileUtil.createFileIfNotExist(VERB_SUFFIX_FILTERED_FILE);
    }
}
