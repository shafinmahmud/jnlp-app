package net.shafin.nlp.corpus;

import net.shafin.common.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class CorpusIO {

    private final String CORPUS_DIRECTORY;
    private final boolean RECURCIVE_FILES;

    private final String EXTENSION;

    public CorpusIO(String dir, String ext, boolean isRecurcive) {
        if (!new File(dir).isDirectory()) {
            throw new IllegalArgumentException("This is not a Directory!");
        }
        this.CORPUS_DIRECTORY = dir;
        this.RECURCIVE_FILES = isRecurcive;
        this.EXTENSION = ext;
    }

    public Iterator<String> getDocumentPaths() throws IOException {
        List<String> paths = null;
        if (RECURCIVE_FILES) {
            paths = FileHandler.getRecursiveFileList(CORPUS_DIRECTORY);
        } else {
            paths = FileHandler.getFileList(CORPUS_DIRECTORY);
        }

        List<String> valids = new ArrayList<>();
        for (String path : paths) {
            if (path.endsWith(EXTENSION)) {
                valids.add(path);
            }
        }

        return valids.iterator();
    }

    public static void main(String[] args) throws IOException {
        String path = "D:/home/dw/json/";
        CorpusIO io = new CorpusIO(path, ".json", true);
        Iterator<String> iter = io.getDocumentPaths();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
