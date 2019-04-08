import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class EsToJson {
    public static void main(String[] args) throws Exception{
        String filePath = "src/data/test.json";
        esToJson(EsDAO.SetupClient(), filePath);
        EsDAO.SetupClient().close();
    }

    public static void esToJson(RestHighLevelClient client, String filePath) throws IOException {
        try{
        SearchRequest searchRequest = new SearchRequest("es_json_rest");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueSeconds(6));
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
            long count = 0;
            while(true){
                for(SearchHit hit : searchResponse.getHits().getHits()){
                    String json = hit.getSourceAsString();

                    out.write(json);
                    out.write("\r\n");
                    count++;
                    System.out.println("*********************"+count);

                }
                SearchScrollRequest scrollRequest = new SearchScrollRequest(searchResponse.getScrollId());
                scrollRequest.scroll(TimeValue.timeValueSeconds(6));
                SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                if(searchScrollResponse.getHits().getHits().length==0){
                    break;
                }
            }
            out.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}
