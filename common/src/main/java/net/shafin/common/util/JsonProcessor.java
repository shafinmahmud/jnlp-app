package net.shafin.common.util;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.*;
import java.util.Map;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class JsonProcessor {

    private String JSON;

    public JsonProcessor() {
    }

    public JsonProcessor(String json) {
        this.JSON = json;
    }

    public JsonProcessor(File file) throws IOException {

        if (file.isDirectory()) {
            throw new IllegalArgumentException("You Provided A Directory Rather than a File for Json Parsing!");
        }

        StringBuilder sb = new StringBuilder();
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = null;
        String line = null;

        try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8")) {
            try {
                br = new BufferedReader(inputStreamReader);
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        this.JSON = sb.toString();
    }

    public String convertToJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(object);
        return json;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToObjectMapper() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(this.JSON, Map.class);
        return map;
    }

    public <T> Object convertToModel(Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.JSON, clazz);
    }
}
