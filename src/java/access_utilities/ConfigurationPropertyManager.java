package access_utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationPropertyManager {
	private static volatile ConfigurationPropertyManager instance = null;
	private String result = "";
	private InputStream inputStream;
	
	private ConfigurationPropertyManager(){}
	
	public static ConfigurationPropertyManager getConfigurationPropertyManager(){
		if (instance == null) {
            instance = new ConfigurationPropertyManager();
        }

        return instance;
	}	
	
	public String getPropValues(String property_key) throws IOException {
		String result = ""; 
		try {
			Properties prop = new Properties();
			String propFileName = "src/resources/config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			} 
			// get the property value and print it out
			result = prop.getProperty(property_key);
 
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}

}
