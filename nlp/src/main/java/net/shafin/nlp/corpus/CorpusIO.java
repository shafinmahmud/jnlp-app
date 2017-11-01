package net.shafin.nlp.corpus;

import net.shafin.common.util.FileUtil;

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
    private final boolean RECURSIVE_FILES;

    private final String EXTENSION;

    public CorpusIO(String dir, String ext, boolean isRecursive) {
        if (!new File(dir).isDirectory()) {
            throw new IllegalArgumentException("This is not a Directory!");
        }

        this.CORPUS_DIRECTORY = dir;
        this.RECURSIVE_FILES = isRecursive;
        this.EXTENSION = ext;
    }

    public Iterator<String> getDocumentPaths() throws IOException {
        List<String> paths = null;
        if (RECURSIVE_FILES) {
            paths = FileUtil.getRecursiveFileList(CORPUS_DIRECTORY);
        } else {
            paths = FileUtil.getFileList(CORPUS_DIRECTORY);
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
