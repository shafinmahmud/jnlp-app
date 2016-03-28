package shafin.nlp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;


public class JsonProcessor {

	private String JSON;

	public JsonProcessor() {

	}

	public JsonProcessor(String json) {
		this.JSON = json;
	}

	public JsonProcessor(File file) throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String s = null;
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();

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

	public static void main(String[] args) throws IOException {
		//File file = new File("D:\\DOCUMENT\\BP\\পাঁচালি.json");
		//JsonProcessor jsonProcessor = new JsonProcessor(file);
		////BanglapediaDoc doc = (BanglapediaDoc) jsonProcessor.convertToModel(BanglapediaDoc.class);
		//System.out.println(doc.getDocID());
	}
}
