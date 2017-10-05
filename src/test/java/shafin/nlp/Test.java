package shafin.nlp;

import java.util.List;

import shafin.nlp.util.FileHandler;

public class Test {

	public static void main(String[] args) {
		
		List<String> geo = FileHandler.readFile("D:\\g.txt");
		for(String string : geo){
			String lat = string.split(",")[0].trim();
			String lon = string.split(",")[1].trim();
			System.out.println(lon+", "+lat+",");
		}
	}
}
