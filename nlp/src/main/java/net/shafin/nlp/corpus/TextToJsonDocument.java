package net.shafin.nlp.corpus;

import net.shafin.common.model.Document;
import net.shafin.common.util.JsonProcessor;
import net.shafin.common.util.Logger;
import net.shafin.common.util.ReflectionUtil;
import net.shafin.common.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 * <p>
 * This Converter can map well-formatted Text file to Object Class and then to Json Format.
 * But Prerequisite is to Mapping class can only have Iteger, Long, Float, Long, String and
 * List<String> type fields. Other type of Fields will not be mapped currently and The Txt
 * Docuement Has to have '{FieldName} : {FieldValue}' format per line. No field should have
 * value in multiple lines
 */
public class TextToJsonDocument<T> {

    private final String TEXT_FILES_DIRECTORY;
    private final String JSON_FILES_DIRECTORY;
    private final Class<T> DOCUMENT_CLASS;

    public TextToJsonDocument(Class<T> clazz, String txtDirectory, String jsonDirectory) {
        this.DOCUMENT_CLASS = clazz;
        this.TEXT_FILES_DIRECTORY = txtDirectory;
        this.JSON_FILES_DIRECTORY = jsonDirectory;
    }

    public void process() throws InstantiationException, IllegalAccessException, IOException {
        List<String> filePaths = FileUtil.getRecursiveFileList(TEXT_FILES_DIRECTORY);

        for (String path : filePaths) {
            String fileName = FileUtil.getFileNameFromPathString(path);

            if (path.endsWith(".txt")) {
                T document = mapTextToObject(path);

                JsonProcessor jsonProcessor = new JsonProcessor();
                String json = jsonProcessor.convertToJson(document);

                String filePath = JSON_FILES_DIRECTORY + "/" + fileName + ".json";
                if (!new File(filePath).exists()) {
                    FileUtil.writeFile(filePath, json);
                    Logger.print("CONVERTED : " + filePath);
                } else {
                    Logger.print("EXISTS : " + filePath);
                }
            }
        }

    }

    private T mapTextToObject(final String txtFilePath) throws InstantiationException, IllegalAccessException {
        List<String> lines = FileUtil.readFile(txtFilePath);
        Field[] fields = DOCUMENT_CLASS.getDeclaredFields();
        T document = (T) DOCUMENT_CLASS.newInstance();

        String lastFieldName = "";

        for (String line : lines) {
            line = line.trim();
            boolean fieldMatched = false;

            for (Field field : fields) {
                String fieldName = field.getName();

                if (line.startsWith(fieldName)) {
                    fieldMatched = true;
                    lastFieldName = fieldName;

                    if (field.getType().getSimpleName().equals("List")) {
                        String valueString = line.replaceFirst("(" + fieldName + "[\\s]*:[\\s]*)", "")
                                .replaceAll("\\[|\\]", "");
                        String[] tokens = valueString.split(",");

                        List<String> values = new ArrayList<>();
                        for (String token : tokens) {
                            values.add(token.trim());
                        }
                        ReflectionUtil.setListValue(document, fieldName, values);
                    } else {
                        String value = line.replaceFirst("(" + fieldName + "[\\s]*:[\\s]*)", "");
                        ReflectionUtil.setValue(document, fieldName, value);
                    }
                }
            }

            if (!fieldMatched) {
                Object val = ReflectionUtil.getValue(document, lastFieldName);

                line = " " + line;
                ReflectionUtil.setValue(document, lastFieldName, val + line);
            }

        }
        return document;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
        String directory = "D:/home/dw/data";
        String jsonDir = "D:/home/dw/json";
        TextToJsonDocument<Document> converter = new TextToJsonDocument<Document>(Document.class, directory, jsonDir);
        converter.process();
    }
}
