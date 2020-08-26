package kdevelop;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppCtr {
    //static Logger logger = LoggerFactory.getLogger(AppCtr.class);


    public static void main(String[] args) {
        String connectionURI = "mongodb+srv://admin:admin@mongojavacluster.f3t3e.mongodb.net/<dbname>?retryWrites=true&w=majority";

        try(MongoClient mongoClient = MongoClients.create(connectionURI)){
            MongoIterable<String> strings = mongoClient.listDatabaseNames();
            MongoCursor<String> cursor = strings.cursor();

            printDatabases(mongoClient);

            createDocuments(mongoClient);

            printDocument(mongoClient);
        }
    }

    private static void createDocuments(MongoClient mongoClient) {

        List<String> ingredients = Arrays.asList("flour", "eggs", "chocolate", "sugar", "red coloring food");
        
        MongoCollection<Document> cookies = mongoClient.getDatabase("cook").getCollection("cookies");

        List<Document> liste = new ArrayList<>();
        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "blue")
                    .append("ingredients", ingredients));
        }

        cookies.deleteMany(new Document());

        cookies.insertMany(liste);
    }

    private static void printDatabases(MongoClient mongoClient) {
        List<Document> dbDocuments = mongoClient.listDatabases().into(new ArrayList<>());
        dbDocuments.forEach(document -> System.out.println(document.toJson()));
    }

    public static void printDocument(MongoClient mongoClient){
        MongoCursor<Document> documents =  mongoClient.getDatabase("cook").getCollection("cookies").find().cursor();
        while(documents.hasNext()) System.out.println(documents.next().toJson());
    }
}
