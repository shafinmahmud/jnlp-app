package net.shafin.nlp.pos;

import net.shafin.common.util.*;
import net.shafin.nlp.corpus.CorpusIO;
import net.shafin.common.model.Document;
import net.shafin.nlp.db.VerbsDao;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class InflattedVerbIndexer {

    private final VerbsDao DAO;

    private final String CORPUS_DIRECTORY;
    private final String EXTENSION = ".json";
    private Set<Integer> EXPLORED;

    private final PosTagger tagger;

    public InflattedVerbIndexer(String dir) throws ClassNotFoundException, IOException {
        this.DAO = new VerbsDao(SQLiteDBHandler.getSQLiteDBConn());
        this.CORPUS_DIRECTORY = dir;
        this.EXPLORED = new HashSet<>();

        this.tagger = new PosTagger();
        DAO.createTable();

        loadHistory();
    }

    private void loadHistory() throws IOException {
        String path = CORPUS_DIRECTORY + "expl.data";
        File his = new File(path);
        if (his.exists()) {
            List<String> lines = FileUtil.readFile(path);
            for (String line : lines) {
                EXPLORED.add(Integer.valueOf(line.trim()));
            }
        } else {
            his.createNewFile();
        }
    }

    public void indexDocs() throws IOException {
        CorpusIO io = new CorpusIO(CORPUS_DIRECTORY, EXTENSION, true);
        Iterator<String> iter = io.getDocumentPaths();

        int doc = 0;
        while (iter.hasNext()) {

            int initialCount = DAO.countVerb();
            Set<String> VERB_SET = new HashSet<>();

            String path = iter.next();
            JsonProcessor processor = new JsonProcessor(new File(path));
            Document document = (Document) processor.convertToModel(Document.class);

            String fileName = FileUtil.getFileNameFromPathString(path);
            int docID = Integer.valueOf(RegexUtil.getFirstMatch(fileName, "[0-9]+"));

            if (!EXPLORED.contains(docID)) {
                String article = StringUtil.cleanPunctuation(document.getArticle());
                tagger.setTEXT(article);
                List<String> verbs = tagger.findVerbTaggedTokens();

                VERB_SET.addAll(verbs);

                DAO.insertVerbsBatch(VERB_SET);
                EXPLORED.add(docID);
                FileUtil.appendFile(CORPUS_DIRECTORY + "expl.data", docID + "\n");

                int lastCount = DAO.countVerb();
                Logger.print(doc++ + ": CONTRIBUTION  : " + (lastCount - initialCount));
            } else {
                Logger.print(doc++ + ": EXPLORED PREVIOUSLY");
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String dir = "D:/home/dw/json/QUALIFIED/";
        InflattedVerbIndexer indexer = new InflattedVerbIndexer(dir);
        indexer.indexDocs();
    }
}
