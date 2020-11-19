package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Extraction {


    // Extract dataset method is used to extract file information that are in the form of .I, .T, .A, .B, .W

    public static void indexDataset(String fileName, IndexWriter iw) throws IOException {

        //Creating a buffered reader to read the dataset
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        // While loop to iterate through the cran.all.1400 dataset, create instances of the Model class and then add it to the docs array list.
        while (line != null) {

            if (line.startsWith(".I")) {

                // Using a hash MAP to store the intermediate results for the text under each identifier
                HashMap<String, String> content = new HashMap<String, String>();
                content.put("id", line.substring(3));


                line = bufferedReader.readLine();
                // This is a flag that will store the value of the last identifier parsed  so that we can store the
                // content against this value
                String contentKey = "";

                while (line != null && !line.startsWith(".I")) {
                    if (line.startsWith(".T")) {
//                        System.out.println("----------");
//                        System.out.println(line);
                        contentKey = "title";
                        if (!content.containsKey("title")) {
                            //Creating a key value in the content hash map which willbe used later in the next loop
                            // to add the content against the key. This is done for all the indentifiers
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
                        // If we come across a line where there is no identifier, we just add to value for the
                        // last identifier in the hash map
                        content.put(contentKey, content.get(contentKey) + (" " + line));

                    }
                    line = bufferedReader.readLine();

                }
                // After we come accross Identifier ".I" we stoer all the values in a document and index it
                // This is because identifier .I shows that we are starting to extract the next document
                Document doc = new Document();
                doc.add(new StringField("id", content.get("id"), Field.Store.YES));
                doc.add(new TextField("title", content.get("title"), Field.Store.YES));
                doc.add(new TextField("author", content.get("author"), Field.Store.YES));
                doc.add(new TextField("bib", content.get("bib"), Field.Store.YES));
                doc.add(new TextField("words", content.get("words"), Field.Store.YES));

                //we are indexing the document immediately as it gives a performance improvement.
                iw.addDocument(doc);

            }
        }


    }

    public static  void scoreQuery(String fileName, QueryParser parser, IndexSearcher is, BufferedWriter bw, int mr) throws IOException, ParseException {
        // A buffered reader has been used to read the dataset again
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        int j = 0;

        // Array List of queries has been created to save individual queries to be returned.
        ArrayList<String> queries = new ArrayList<String>();

        //While loop to extract individual queries and add it to the queries array
        // This is similar to the index dataset method, but instead of going through all he identifiers, it just goes
        // through .I and .W to make it efficient.
        // Current query stores the text from the line after it encounters .W indentifier until it indentifies  a .I
        //indentifier
        String currentQuery = "";
        while (line != null) {
            if(line.startsWith(".I ")) {
                line = bufferedReader.readLine();
                if (line.equals(".W")) {
                    j++;
                    line = bufferedReader.readLine();
                    while (line != null && !line.startsWith(".I")) {
                        currentQuery += line + " ";
                        line = bufferedReader.readLine();
                    }
                    // For each current query collected through parsing, it is then parsed using query parser
                    Query query = parser.parse(currentQuery);
                    // The new query object created is then used to searched the index and a List of ScoreDoc objects are created
                    TopDocs results = is.search(query, mr);
                    ScoreDoc[] hits = results.scoreDocs;

                    // Then a for loop goes over the hits array to get the score of the query related to each document in the index along with the document id
                    for(int i=0;i<hits.length;i++){
                        Document hitDoc = is.doc(hits[i].doc);
                        bw.write(j + " Q0 " + hitDoc.get("id") + " " + (i+1) + " " + hits[i].score + " Standard" + "\n" );
                    }

                    currentQuery = "";
                }
            }
        }
        bufferedReader.close();
    }
}


