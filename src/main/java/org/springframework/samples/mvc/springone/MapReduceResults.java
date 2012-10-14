package org.springframework.samples.mvc.springone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MapReduceResults implements ResourceLoaderAware {

	private static final Log log = LogFactory.getLog(MapReduceResults.class);

	private ResourceLoader resourceLoader;
	
	public TreeSet<NameCountData> getResults(String fileName) {
		TreeSet<NameCountData> results = new TreeSet<NameCountData>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceLoader.getResource(fileName).getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = StringUtils.tokenizeToStringArray(line, " \r\t\n");
				if (tokens.length == 2) {
					String hashTag = tokens[0].replaceAll("^\"|\"$", "");
					if (hashTag.length() != 0) {

						Integer count = Integer.valueOf(tokens[1].trim());
						if (count != null) {
							NameCountData ncd = new NameCountData(hashTag, count);
							results.add(ncd);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("error processing map reduce results", e);
			results.add(new NameCountData("filenotfound", 1));
		}
		//log.debug(results);
		return results;

	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}


}
