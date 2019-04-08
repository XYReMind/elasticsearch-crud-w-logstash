import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import java.io.IOException;


public class EsCrudTest {
    EsCrud esCrud = new EsCrud();
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    @Test
    public void mainTest() throws Exception {
        esCrud.main(null);
    }

    @Test
    public void createIndexTest() throws IOException {
        esCrud.createIndex(client);
    }

    @Test
    public void getIndexTest() throws Exception {
        esCrud.createIndex(client);
        esCrud.getIndex(client);

    }

    @Test
    public void updateIndexTest() throws Exception {
        esCrud.createIndex(client);
        esCrud.updateIndex(client);

    }

    @Test
    public void deleteIndexTest() throws Exception {
        esCrud.createIndex(client);
        esCrud.deleteIndex(client);
    }
}
