package lucene;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Setup {

    // This class gets all the config values like queryPath, INDEX_DIRECTORY,datasetPath, resultPath, MAX_RESULTS
    // from the config.properties file under resources
    InputStream inputStream;


    public  String getConfig(String key) throws IOException {
        Properties prop = new Properties();
        String propFileName = "config.properties";
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        prop.load(inputStream);
        return prop.getProperty(key);
    }

}
