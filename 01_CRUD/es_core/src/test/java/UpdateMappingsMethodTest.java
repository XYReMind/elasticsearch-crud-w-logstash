import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import java.io.IOException;

public class UpdateMappingsMethodTest {

    UpdateMappingsMethod up = new UpdateMappingsMethod();
    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));
    @Test
    public void TestUpdateMethod() throws IOException {
        up.updateMethod(client, "1");
    }

    @Test
    public void TestScriptedFiled() throws IOException {
        up.scriptField(client, "1");
    }

    @Test
    public void TestMain(){
        try {
            up.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
