package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Searcher {

    private static String INDEX_DIRECTORY = "";
    private static int MAX_RESULTS = 500;

    public void search() throws IOException, ParseException {
        System.out.println("Searching is starting ..... ");
        Setup properties = new Setup();

        String queryPath = "";
        String resultFile = "";
        queryPath  = properties.getConfig("queryPath");
        resultFile  = properties.getConfig("resultPath");
        MAX_RESULTS = Integer.parseInt(properties.getConfig("MAX_RESULTS"));
        INDEX_DIRECTORY = properties.getConfig("INDEX_DIRECTORY");

        if(!Indexer.INDEX_DIRECTORY.equals("")){INDEX_DIRECTORY = Indexer.INDEX_DIRECTORY;}
        if(!Indexer.queryPath.equals("")){queryPath = Indexer.queryPath;}
        if(!Indexer.resultPath.equals("")){resultFile = Indexer.resultPath;}
        MAX_RESULTS = Indexer.MAX_RESULTS;

        String similarityFlag = Indexer.similarityFlag;
        String analyzerFlag = Indexer.analyzerFlag;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(queryPath));
        String line = bufferedReader.readLine();


        ArrayList<Model> docs  = new ArrayList<Model>();
        docs = Extraction.extract(queryPath);
//        ArrayList<String> queries = new ArrayList<>();
//        queries = Extraction.extractQuery(queryPath);
        System.out.println("Parsed all the queries ..... ");


        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);


        isearcher.setSimilarity(new BM25Similarity(2f, 0.88f));
        if (similarityFlag.equals( "2")) {        isearcher.setSimilarity(new ClassicSimilarity()); }
        if (similarityFlag.equals( "3")) {        isearcher.setSimilarity(new BooleanSimilarity()); }

        String result  = "";



        int j = 0;
        Analyzer analyzer = new EnglishAnalyzer();
        if(analyzerFlag.equals("2")){analyzer = new StandardAnalyzer();}
        if(analyzerFlag.equals("3")){ analyzer = new CustomAnalyzer();}
        if(analyzerFlag.equals("4")){ analyzer = new SimpleAnalyzer();}
        if(analyzerFlag.equals("5")){ analyzer = new WhitespaceAnalyzer();}


        Map<String, Float> boost = new HashMap<String, Float>();
        boost.put("title", 0.6f);
        boost.put("author", 0.002f);
        boost.put("bib", 0.0002f);
        boost.put("words", 0.99f);

        String[] content = new String[]{ "title","bib", "author", "words"};
        QueryParser parser = new MultiFieldQueryParser(content, analyzer, boost);
        parser.setAllowLeadingWildcard(true);


        //QueryParser parser = new QueryParser("content", analyzer);

        System.out.println("Using Max results " + MAX_RESULTS);
        for(Model val: docs ){
            j++;

            String currentQuery = val.words;

            Query query = parser.parse(currentQuery);
            ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;
            // Print the results
            for (int i = 0; i < hits.length; i++)
            {
                Document hitDoc = isearcher.doc(hits[i].doc);
                result +=  "\n" + j + " 0 " + hitDoc.get("id") + " " + (i+1) + " " + hits[i].score + " Standard";
            }

        }

//        for(String qry: queries ){
//            j++;
//
//            parser.setAllowLeadingWildcard(true);
//
//            Query query = parser.parse(qry);
//            ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;
//            // Print the results
//            for (int i = 0; i < hits.length; i++)
//            {
//                Document hitDoc = isearcher.doc(hits[i].doc);
//                result +=  "\n" + j + " 0 " + hitDoc.get("id") + " " + (i+1) + " " + hits[i].score + " Standard";
//            }
//
//        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile));



        bufferedWriter.write(result.trim());
        bufferedReader.close();
        bufferedWriter.close();
        directory.close();
        ireader.close();

        System.out.println("Results file has been generated..... ");



    }
}
