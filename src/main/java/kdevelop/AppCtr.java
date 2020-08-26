package kdevelop;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AppCtr {
    //static Logger logger = LoggerFactory.getLogger(AppCtr.class);


    public static void main(String[] args) {
        String connectionURI = "mongodb+srv://admin:admin@mongojavacluster.f3t3e.mongodb.net/<dbname>?retryWrites=true&w=majority";

        try(MongoClient mongoClient = MongoClients.create(connectionURI)){

            MongoCollection<Document> cookies = mongoClient.getDatabase("cook").getCollection("cookies");

            printDatabases(mongoClient);

            deleteAllCookiees(cookies);

            createDocument(cookies);

            deleteDocuments(cookies);

            updateDocument(cookies);

            printDocument(mongoClient);
        }
    }

    private static void deleteAllCookiees(MongoCollection<Document> cookies) {
        cookies.deleteMany(new Document());
    }

    private static void updateDocument(MongoCollection<Document> cookies) {
        Random random = new Random();

        List<Document> listCookies =  cookies.find().into(new ArrayList<>());
        listCookies.forEach( cookie -> {
            Object id = cookie.getObjectId("_id");
            cookies.findOneAndUpdate(new Document("_id", id), Updates.set("calories", random.nextInt(1000)));
        });
    }

    private static void createDocument(MongoCollection<Document> cookies) {

        List<String> ingredients = Arrays.asList("flour", "eggs", "chocolate", "sugar", "red coloring food");

        List<Document> liste = new ArrayList<>();

        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "brown")
                    .append("ingredients", ingredients));
        }


        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "orange")
                    .append("ingredients", ingredients));
        }


        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "yellow")
                    .append("ingredients", ingredients));
        }

        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "orange")
                    .append("ingredients", ingredients));
        }

        for(int i = 1; i <=10 ; i++){
            liste.add(new Document("id", i)
                    .append("color", "blue")
                    .append("ingredients", ingredients));
        }
        cookies.insertMany(liste);
    }

    private static void deleteDocuments(MongoCollection<Document> cookies) {
        cookies.deleteMany(Filters.in("color", Arrays.asList("yellow", "orange", "blue")));
    }

    private static void printDatabases(MongoClient mongoClient) {
        List<Document> dbDocuments = mongoClient.listDatabases().into(new ArrayList<>());
        dbDocuments.forEach(document -> System.out.println(document.toJson()));
    }

    private static void printDocument(MongoClient mongoClient){
        MongoCursor<Document> documents =  mongoClient.getDatabase("cook").getCollection("cookies").find().cursor();
        while(documents.hasNext()) System.out.println(documents.next().toJson());
    }
}
