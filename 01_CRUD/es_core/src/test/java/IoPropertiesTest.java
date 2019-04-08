import org.junit.Test;
import java.io.IOException;


import static org.junit.Assert.assertEquals;

public class IoPropertiesTest {
    IoProperties ioProperties = new IoProperties();
    String FilePath = "src/main/resources/config.properties";
    String Key = "file_path_url";

    @Test
    public void TestIoProperties() throws IOException {
        assertEquals("src\\data", ioProperties.GetValueByKey(FilePath, Key));
    }

    @Test
    public void TestFileNotFoundException() throws IOException {
        assertEquals(null, ioProperties.GetValueByKey("src/test/data/test.properties","file_path_url"));
    }
}

