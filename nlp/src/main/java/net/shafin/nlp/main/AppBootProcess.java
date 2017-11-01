package net.shafin.nlp.main;

import net.shafin.common.util.FileUtil;
import net.shafin.nlp.db.SQLiteDBConn;

import java.io.File;
import java.io.IOException;

/**
 * @author Shafin Mahmud
 * @since 11/18/2017
 */
public class AppBootProcess {

    private static final String DATA_DIR = "_data/";
    private static final String INDEX_DIR = DATA_DIR + "_index/";

    public static final String DB_SCHEMA_PATH = DATA_DIR + "db.sqlite";
    public static final String ZERO_FREQ_FILE = INDEX_DIR + "zero_freq_terms.txt";
    public static final String STOP_FILTERED_FILE = INDEX_DIR + "stop_filtered_terms.txt";
    public static final String VERB_SUFFIX_FILTERED_FILE = INDEX_DIR + "verb_suffix_filtered_terms.txt";

    public static void init() {
        //initialize database
        SQLiteDBConn.initializeDB(DB_SCHEMA_PATH);

        //initialize required files
        FileUtil.createFileIfNotExist(ZERO_FREQ_FILE);
        FileUtil.createFileIfNotExist(STOP_FILTERED_FILE);
        FileUtil.createFileIfNotExist(VERB_SUFFIX_FILTERED_FILE);
    }
}
