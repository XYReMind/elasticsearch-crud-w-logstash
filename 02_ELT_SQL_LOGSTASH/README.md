# 02 Elasticsearch with sql and logstash   
### Example files   
 - sample_bin/logstash_default.conf  
 - sample_bin/jdbc-mysql.conf  
 - sample_bin/jdbc-w360-account.conf  
 - sample_bin/jdbc-w360-asset.conf   

### Run -- sample_bin/run_default.bat      
   
## Documnets:    
   
Use Logstash to import data from Mysql to ES:   
1.	Download Logstash and mysql packages (installer is not required), set up the system environment variables properly. Then connect to mysql.  
2.	Download latest version of JDBC plugin, mysql-connector-java-5.1.47.  
3.	Create configuration file in your logstash directory /bin, e.g. default-logstash.conf.  
4.	Create drive file in the same directory, e.g. run_default.bat.   

 
First, in the configuration file, set up the date pipeline and use mysql connector, there is three plugins in the file, input{}: get the data source, filter{} (optional): modify the data and output{}: to the data destination.  
The basic setup looks like:   
```
input{
       jdbc{
		jdbc_driver_library => “the path of the jdbc jar file”
		jdbc_driver_class => “com.mysql.jdbc.Driver”
		jdbc_connection_string => “jdbc:mysql://hostname of mysql: port/specific database”
		jdbc_user => “username of mysql”
		jdbc_password => “your password”
		schedule  =>  “******”
    }
}
output{
      elasticsearch{
		hosts => “elasticsearch host address(localhost:9200)”
		index => “index name”
		document_type => “type name”
		document_id = “%{id}(if id field is existed in table)”
    }
}
```  
Since in the latest version of ES, one index can only have one mapping, if the tables in one database have different mapping, you have to create different conf files. But if the mapping is the same, it is possible to use only one .conf, for example, add type field in input{} and use if loop to select different field if [type] == “xxx”.
Then add  
```
logstash  –f  default-logstash.conf
```  
in run_default.bat   
your_directory/run_default.bat to run logstash   
Test the result in Kibana.   
 
It is also possible to do that by Java…    



