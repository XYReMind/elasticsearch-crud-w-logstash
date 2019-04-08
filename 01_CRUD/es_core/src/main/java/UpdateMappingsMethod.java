import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class UpdateMappingsMethod {
    public static void main(String[] args) throws Exception {
        for(int i = 1; i< 22; i++) {
            String stringNumber = Integer.toString(i);
            updateMethod(EsDAO.SetupClient(), stringNumber);
        }
        for(int i = 1; i< 22; i++) {
            String stringNumber = Integer.toString(i);
            scriptField(EsDAO.SetupClient(), stringNumber);
        }
        EsDAO.SetupClient().close();
    }


    public static boolean updateMethod(RestHighLevelClient client, String idNumber) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("es_json_rest","try_rest", idNumber)
                    .doc(jsonBuilder().startObject().field("total_cost","0").endObject());
        client.update(updateRequest, RequestOptions.DEFAULT);
        return true;
    }

    public static boolean scriptField(RestHighLevelClient client, String idNumber) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(
                "es_json_rest",
                "try_rest",
                idNumber
        );
        updateRequest.script(new Script("ctx._source.total_cost = ctx._source.cost * 2"));
        client.update(updateRequest, RequestOptions.DEFAULT);
        return true;
    }
}


