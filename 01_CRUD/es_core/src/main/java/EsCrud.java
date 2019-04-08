import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentFactory;

public class EsCrud {

    public static void main(String[] args) throws Exception {

        createIndex(EsDAO.SetupClient());
        //getIndex(EsDAO.SetupClient());
        //updateIndex(EsDAO.SetupClient());
        EsDAO.SetupClient().close();

    }

    public static void createIndex(RestHighLevelClient client) throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.field("postDate","2019-1-31");
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();

        IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
                .source(builder);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.getResult().name());
    }

    public static void getIndex(RestHighLevelClient client) throws Exception {
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    public static void updateIndex(RestHighLevelClient client) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", "2019-02-04");
        jsonMap.put("reason", "daily update");
        UpdateRequest request = new UpdateRequest("posts", "doc", "1")
                .doc(jsonMap);
        System.out.println(request);
    }

    public static void deleteIndex(RestHighLevelClient client) throws Exception{
        DeleteRequest request = new DeleteRequest(
                "posts",
                "doc",
                "1");
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.getResult());
    }
}