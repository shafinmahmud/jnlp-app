/*
 */
package shafin.nlp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author SHAFIN
 */
public class FileHandler {

	public static List<String> getFileList(String filePath) {
		List<String> fileNames = new ArrayList<>();

		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileNames.add(listOfFiles[i].getName());
			}
		}
		return fileNames;
	}

	public static List<String> getRecursiveFileList(String path) throws IOException {
		final List<String> fileList = new ArrayList<>();

		class FileRecursion {
			private void listFilesForFolder(File folder) {
				for (File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {
						listFilesForFolder(fileEntry);
					} else {
						fileList.add(fileEntry.getAbsolutePath());
					}
				}
			}
		}

		File folder = new File(path);
		FileRecursion recursion = new FileRecursion();
		recursion.listFilesForFolder(folder);

		return fileList;
	}

	public static List<String> getRecursiveNoneEmptyChildFolders(String path) throws IOException {
		path = getCanonicalPath(path);
		List<String> fileList = getRecursiveFileList(path);
		Set<String> noneEmptyFolders = new HashSet<>();
		for (String file : fileList) {
			String parent = new File(file).getParent();
			if (!parent.equals(path)) {
				noneEmptyFolders.add(parent);
			}
		}
		return new ArrayList<String>(noneEmptyFolders);
	}

	public static String getCanonicalPath(String path){
		path = (path.endsWith("/") | path.endsWith("\\"))
				? path.replaceAll("[\\|\\/]+(?=[^\\\\/]*$)", "") : path;
		return path.replace("/", "\\");
	}
	
	public static List<String> readFile(String filePath) {

		List<String> lines = new ArrayList<>();
		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			return lines;

		} catch (FileNotFoundException e) {
			System.out.println(filePath);
			e.printStackTrace();
		} catch (IOException e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return lines;
	}

	public static List<String> readFileOrCreateIfNotExists(String filePath) {

		List<String> lines = new ArrayList<>();
		BufferedReader br = null;
		String line = null;

		try {

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			return lines;

		} catch (FileNotFoundException e) {
			System.out.println(filePath);
			e.printStackTrace();
		} catch (IOException e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return lines;
	}

	public static String readFileAsSingleString(String filePath) {

		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try {

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			in = new BufferedReader(new FileReader(filePath));

			sb = new StringBuilder();
			String s = null;
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	public static boolean writeFile(String filePath, String textData) {
		try {
			filePath = validateFilePathName(filePath);
			File file = new File(filePath);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
				outputStreamWriter.write(textData);
			}

			return true;

		} catch (IOException e) {
			return false;

		}
	}

	public static boolean appendFile(String filePath, String textData) {
		try {
			filePath = validateFilePathName(filePath);
			File file = new File(filePath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
				textData = textData.replaceAll("\n", System.lineSeparator());
				outputStreamWriter.write(textData);
			}

			return true;

		} catch (IOException e) {
			return false;

		}
	}

	public static boolean writeListToFile(String filePath, List<String> inputList) {
		try {
			filePath = validateFilePathName(filePath);
			File file = new File(filePath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			StringBuilder stringBuilder = new StringBuilder();

			for (String t : inputList) {
				stringBuilder.append(t).append("\n");
			}
			String textData = stringBuilder.toString();

			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
				textData = textData.replaceAll("\n", System.lineSeparator());
				outputStreamWriter.write(textData);
			}
			System.out.println("INFO: list has been written to " + filePath);
			return true;

		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean moveFile(String filePath, String destinationFolder){
		File file = new File(filePath);
		File dir = new File(destinationFolder);
		if (!file.exists() | file.isDirectory() | !dir.isDirectory()) {
			throw new IllegalArgumentException("Provide Valid File Folder in Parameter");
		}
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String newLocation = getCanonicalPath(destinationFolder)+"\\"+file.getName();
		
		File newFile = new File(newLocation);
		if(!newFile.exists()){
			return file.renameTo(newFile);	
		}
		
		return deleteFile(filePath);
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static String validateFilePathName(String filePath) {
		int idx = filePath.replaceAll("\\\\", "/").lastIndexOf("/");
		if (idx >= 0) {
			String fileName = filePath.substring(idx + 1);
			String fileExtension = getFileExtensionFromPathString(filePath);
			fileName = fileName.replace(fileExtension, "");

			String newFileName = getValidFileName(fileName);
			return filePath.replace(fileName, newFileName);
		} else {
			return null;
		}
	}

	public static String getValidFileName(String fileName) {
		String newFileName = fileName.replaceAll("[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "").trim();
		if (newFileName.length() == 0) {
			throw new IllegalStateException("File Name " + fileName + " results in a empty fileName!");
		}
		return newFileName;
	}

	public static String getFileNameFromPathString(String filePath) {
		try {
			int idx = filePath.replaceAll("\\\\", "/").lastIndexOf("/");
			if (idx >= 0) {
				String fileName = filePath.substring(idx + 1);
				String fileExtension = getFileExtensionFromPathString(filePath);
				return fileName.replace(fileExtension, "");
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String getFileNameFromPathStringWithExt(String filePath) {
		try {
			int idx = filePath.replaceAll("\\\\", "/").lastIndexOf("/");
			if (idx >= 0) {
				String fileName = filePath.substring(idx + 1);
				return fileName;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getFileExtensionFromPathString(String filePath) {
		try {
			int idx = filePath.lastIndexOf(".");
			if (idx >= 0) {
				String extension = filePath.substring(idx);
				return extension;
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getDirectoryFromFilePathString(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			return file.getParentFile().getAbsolutePath();
		}
		return path;
	}

	public static void main(String[] args) throws IOException {
		// তেলাপোকার দুধ: ভবিষ্যতের ‘সুপারফুড'?
		// শিশু শরণার্থী: অপরাধীদের সহজ লক্ষ্য
		// ২০১৬ অলিম্পিক: রিও কি প্রস্তুত?
		String filePath = "D:\\home\\dw\\json\\QUALIFIED";
		System.out.println(getRecursiveNoneEmptyChildFolders(filePath));

	}
}
