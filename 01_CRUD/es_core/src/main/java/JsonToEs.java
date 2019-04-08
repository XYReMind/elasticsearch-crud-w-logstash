import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;


public class JsonToEs {

    public static void main(String[] args) throws Exception{
        String filesPath = IoProperties.GetValueByKey("src/main/resources/config.properties", "file_path_url");
        ArrayList<String> iconNameList = new ArrayList<String>();
        for (int i =0; i < getFilePaths(filesPath).size(); i++){
            String curpath = getFilePaths(filesPath).get(i).getPath();
            iconNameList.add(curpath.substring(curpath.lastIndexOf(" "+filesPath+"\\")+1));
        }
        for(int j =0; j < iconNameList.size(); j++){
            String s = fileRead(iconNameList.get(j));
            JSONObject jsonObject = new JSONObject(s);
            jsonToEs(EsDAO.SetupClient(), jsonObject, j);
        }
        EsDAO.SetupClient().close();
    }

    public static void jsonToEs(RestHighLevelClient client, JSONObject file, int IdNumber) throws Exception{
        try {
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
                    new IndexRequest("es_json","try",Integer.toString(IdNumber+1))
                    .source(jsonMap));
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                System.out.println("Failures!");
                for(BulkItemResponse bulkItemResponse :bulkResponse){
                    if(bulkItemResponse.isFailed()){
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        System.out.println(failure);
                    }
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<File> getFilePaths(String path){
        ArrayList<File> fileList = new ArrayList<>();

        File file = new File(path);
        if(file.isDirectory()){
            File []files = file.listFiles();
            for(File fileIndex: files){
                if(fileIndex.isDirectory()){
                    getFilePaths(fileIndex.getPath());
                }else{
                    fileList.add(fileIndex);
                }
            }
        }else{
            System.out.println("Failure");
        }
        return fileList;
    }

    public static String fileRead(String filePath) throws IOException {

        File file = new File(filePath);
        BufferedReader bReader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String s = "";
        while ((s=bReader.readLine()) != null){
            sb.append(s + "\n");
        }
        bReader.close();
        String str = sb.toString();
        return str;
    }

}
