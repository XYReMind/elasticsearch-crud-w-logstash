import org.junit.Test;

public class EsDAOTest{

    EsDAO esDao = new EsDAO();
    @Test
    public void TestSetupClient(){
        esDao.SetupClient();
    }
}