package org.springframework.samples.mvc.springone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class MapReduceResults {

	private static final Log log = LogFactory.getLog(MapReduceResults.class);

	public static TreeSet<NameCountData> getResults(String fileName) {
		TreeSet<NameCountData> results = new TreeSet<NameCountData>();
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = StringUtils.tokenizeToStringArray(line, " \r\t\n");
				if (tokens.length == 2) {
					String hashTag = tokens[0].replaceAll("^\"|\"$", "");
					if (hashTag.length() != 0) {
						//System.out.println("count = " + tokens[1].trim());
						
						Integer count = Integer.valueOf(tokens[1].trim());
						if (count != null) {
							//
							//results.put(hashTag, count);
							NameCountData ncd = new NameCountData(hashTag, count);
							results.add(ncd);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("file not found", e);
			results.add(new NameCountData("filenotfound", 1));
		}
		return results;
		
	}


}
