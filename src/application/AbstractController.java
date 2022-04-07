package application;

import java.io.FileInputStream;
import java.io.File;
import java.net.URI;
import java.util.Properties;


public abstract class AbstractController {
	
	private static Properties properties = null;
	
	

	
	
	
	private static void load_resource_properties() {
		properties = new Properties();
        try {
        	File prop_file = new File("application.properties");
        	if(!prop_file.exists()) {
        		prop_file = new File("application.properties");
        	}
            FileInputStream stream = new FileInputStream(
            		prop_file);
            properties.load(stream);
            stream.close();
        } catch (Exception e) {
            System.out.println(
                    "Unable to load properties from application.properties file, details: " + e.getMessage());
        }
	}
	
	public static Properties getProperties() {
		if(properties == null) {
			load_resource_properties();
		}
		return properties;
	}
	

}
