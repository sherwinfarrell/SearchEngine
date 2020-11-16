package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Extraction {


    // Extract dataset method is used to extract file information that are in the form of .I, .T, .A, .B, .W
    public static ArrayList<Model> extractDataset(String fileName) throws IOException {

        // An array list of Model instances is created to store the 1400 individual documents in the cran.all.1400 file
        ArrayList<Model>  docs  = new ArrayList<Model>();

        //Creating a buffered reader to read the dataset
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        // While loop to iterate through the cran.all.1400 dataset, create instances of the Model class and then add it to the docs array list.
        while (line != null) {

            if (line.startsWith(".I")) {

                HashMap<String, String> content = new HashMap<String, String>();
                content.put("id", line.substring(3));


                line = bufferedReader.readLine();
                String contentKey = "";

                while (line != null && !line.startsWith(".I")) {
                    if (line.startsWith(".T")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "title";
                        if (!content.containsKey("title")) {
                            content.put("title", "");
                        }
                    } else if (line.startsWith(".A")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "author";
                        if (!content.containsKey("author")) {
                            content.put("author", "");
                        }
                    } else if (line.startsWith(".B")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "bib";
                        if (!content.containsKey("bib")) {
                            content.put("bib", "");
                        }
                    } else if (line.startsWith(".W")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "words";
                        if (!content.containsKey("words")) {
                            content.put("words", "");
                        }
                    } else {
                        content.put(contentKey, content.get(contentKey) + (" " + line));

                    }
                    line = bufferedReader.readLine();

                }
                docs.add(new Model(content.get("id"), content.get("title"), content.get("author"), content.get("bib"), content.get("words")));


            }
        }

        return  docs;
    }

    // The Method is used to extract individual queries from a query dataset
    // a separate method was create as it is more efficient than search for .T, .A, .B which is not part of the query dataset.

    public static  ArrayList<String> extractQuery(String fileName) throws IOException{
        // A buffered reader has been used to read the dataset again
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        // Array List of queries has been created to save individual queries to be returned.
        ArrayList<String> queries = new ArrayList<String>();

        //While loop to extract individual queries and add it to the queries array
        String currentLine = "";
        while (line != null) {
            if(line.startsWith(".I ")) {
                line = bufferedReader.readLine();
                if (line.equals(".W")) {
                    line = bufferedReader.readLine();
                    while (line != null && !line.startsWith(".I")) {
                        currentLine += line + " ";
                        line = bufferedReader.readLine();
                    }
                    queries.add(currentLine);
                    currentLine = "";
                }
            }
        }
        bufferedReader.close();
        return queries;
    }


    public static void indexDataset(String fileName, IndexWriter iw) throws IOException {

        //Creating a buffered reader to read the dataset
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        // While loop to iterate through the cran.all.1400 dataset, create instances of the Model class and then add it to the docs array list.
        while (line != null) {

            if (line.startsWith(".I")) {

                HashMap<String, String> content = new HashMap<String, String>();
                content.put("id", line.substring(3));


                line = bufferedReader.readLine();
                String contentKey = "";

                while (line != null && !line.startsWith(".I")) {
                    if (line.startsWith(".T")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "title";
                        if (!content.containsKey("title")) {
                            content.put("title", "");
                        }
                    } else if (line.startsWith(".A")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "author";
                        if (!content.containsKey("author")) {
                            content.put("author", "");
                        }
                    } else if (line.startsWith(".B")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "bib";
                        if (!content.containsKey("bib")) {
                            content.put("bib", "");
                        }
                    } else if (line.startsWith(".W")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "words";
                        if (!content.containsKey("words")) {
                            content.put("words", "");
                        }
                    } else {
                        content.put(contentKey, content.get(contentKey) + (" " + line));

                    }
                    line = bufferedReader.readLine();

                }
                Document doc = new Document();
                doc.add(new StringField("id", content.get("id"), Field.Store.YES));
                doc.add(new StringField("path", content.get("id"), Field.Store.YES));
                doc.add(new TextField("title", content.get("title"), Field.Store.YES));
                doc.add(new TextField("author", content.get("author"), Field.Store.YES));
                doc.add(new TextField("bib", content.get("bib"), Field.Store.YES));
                doc.add(new TextField("words", content.get("words"), Field.Store.YES));
                iw.addDocument(doc);

            }
        }


    }
}
