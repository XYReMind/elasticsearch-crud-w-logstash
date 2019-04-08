import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;

public class EsToJsonTest {
    EsToJson esToJson = new EsToJson();

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));
    @Test
    public void esToJsonTest() throws IOException {
        esToJson.esToJson(client,"src/data/test.json" );
    }

    @Test
    public void FileNotFoundTest() throws IOException {
        esToJson.esToJson(client, "src/data");
    }
    @Test
    public void mainTest() throws Exception {
        esToJson.main(null);

    }
}
