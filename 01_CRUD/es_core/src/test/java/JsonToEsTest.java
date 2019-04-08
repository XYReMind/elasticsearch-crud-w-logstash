import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class JsonToEsTest {
    JsonToEs jsonToEs = new JsonToEs();
    String directoryPath = "src/test/data";

    @Test
    public void getFilePathsTest() {
        ArrayList<File> testFilePath = new ArrayList<File>();
        ArrayList<File> testFailureFilePath = new ArrayList<File>();
        testFilePath.add(new File("src/test/data/1.json"));
        testFilePath.add(new File("src/test/data/2.json"));

        assertEquals("Pass!", testFilePath, jsonToEs.getFilePaths(directoryPath));
        assertNotEquals("Pass!", testFailureFilePath, jsonToEs.getFilePaths(directoryPath));
    }

    @Test
    public void fileReadTest() throws IOException {
          BufferedReader testBufferReader = new BufferedReader(
                  new FileReader(
                          new File("src/test/data/1.json")));
          StringBuilder testBuilder = new StringBuilder();
          String testString = "";
          while((testString=testBufferReader.readLine()) != null){
              testBuilder.append(testString+ "\n");
          }
          testBufferReader.close();
          String testResult = testBuilder.toString();
          assertEquals(testResult, jsonToEs.fileRead("src/test/data/1.json"));

    }

    @Test
    public void testBulkRequest() throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        int testIdNumber = 1;
        String testPath = jsonToEs.fileRead("src/data/1.json");
        JSONObject testJsonObject = new JSONObject(testPath);
        jsonToEs.jsonToEs(client, testJsonObject, testIdNumber);
    }

    @Test(expected = FileNotFoundException.class)
    public void testJsonToEs_FileNotFoundException() throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        int testIdNumber = 1;
        JSONObject testJsonObject = new JSONObject(jsonToEs.fileRead("src/data/fail.json"));

        jsonToEs.jsonToEs(client, testJsonObject, testIdNumber);
    }

    @Test
    public void testMain() throws Exception {
        jsonToEs.main(null);

    }

    @Test
    public void test1() throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        int testIdNumber = 1;

        BulkItemResponse bulkItemResponse = Mockito.mock(BulkItemResponse.class);
        Mockito.when(bulkItemResponse.isFailed()).thenReturn(Boolean.TRUE);

        BulkItemResponse []bulkItemResponses = new BulkItemResponse[]{bulkItemResponse};
        BulkResponse bulkResponse = new BulkResponse(bulkItemResponses, 1L);

        //Mockito.doReturn(bulkItemResponse).when(client.bulk(Mockito.any(BulkRequest.class), RequestOptions.DEFAULT));
        String testPath = jsonToEs.fileRead("src/data/1.json");
        JSONObject file = new JSONObject(testPath);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("portfolio",file.getString("portfolio"));
        jsonMap.put("valuation",file.getString("valuation"));
        jsonMap.put("classification",file.getString("classification"));
        jsonMap.put("securityCode",file.getString("securityCode"));
        jsonMap.put("securityName",file.getString("securityName"));
        jsonMap.put("securityCurrency",file.getString("securityCurrency"));
        jsonMap.put("noShares",file.getDouble("noShares"));
        jsonMap.put("price",file.getDouble("price"));
        jsonMap.put("oldPriceInd",file.getString("oldPriceInd"));
        jsonMap.put("unitCost",file.getDouble("unitCost"));
        jsonMap.put("cost",file.getDouble("cost"));
        jsonMap.put("mktValue",file.getDouble("mktValue"));
        jsonMap.put("mktPct",file.getDouble("mktPct"));
        jsonMap.put("mktValChgPct",file.getDouble("mktValChgPct"));
        jsonMap.put( "commentInd",file.getString("commentInd"));
        jsonMap.put("gainLoss",file.getDouble("gainLoss"));
        jsonMap.put("gainLossPct",file.getDouble("gainLossPct"));
        BulkRequest request = new BulkRequest();
        request.add(
                new IndexRequest("es_json","try",Integer.toString(2))
                        .source(jsonMap));

        Mockito.when(client.bulk(request, RequestOptions.DEFAULT))
                .thenReturn(bulkResponse);

        jsonToEs.jsonToEs(client, file, testIdNumber);
    }
}
