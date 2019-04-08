import java.io.*;
import java.util.Properties;

public class IoProperties {

    public static String GetValueByKey(String filepath, String key) throws IOException {
        Properties esProperties = new Properties();
        try{
            InputStream in = new BufferedInputStream(new FileInputStream(filepath));
            esProperties.load(in);
            String value = esProperties.getProperty(key);
            return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
