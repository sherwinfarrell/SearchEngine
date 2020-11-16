package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Indexer {

    public static String INDEX_DIRECTORY = "";
    public static String datasetPath = "";
    public static String queryPath = "";
    public static String resultPath = "";
    public static int MAX_RESULTS = 500;
    public static String similarityFlag = "";
    public static String analyzerFlag = "";



    public static void main(String Args[]) throws IOException, ParseException {
        long start = System.currentTimeMillis();
        if(Args.length ==0 ){
            System.out.println("The following arguments can be used\n" +
                    "       -INDEX_DIRECTORY Index/\n       -queryPath Corpus/cran.qry\n       -datasetPath Corpus/cran.all.1400 -MAX_RESULTS 30\n       -resultPath Corpus/results.txt -Similarity 2\n       -Analyzer 2\n" +
                    "Similarites range from 1 to 3 and Analzers from 1 to 5. Refer to the readme for the options.\n" +
                    "Default Values will be considered from the config.properties file otherwise :)\n\n");
        }

        //Get all the variable values from the resources/config.properties file
        Setup properties = new Setup();
        datasetPath  = properties.getConfig("datasetPath");
        INDEX_DIRECTORY = properties.getConfig("INDEX_DIRECTORY");


        // For loop to get the command line optional arguments to change the configuration of the Search Engine
        int i =0;
        for(String arg : Args){
            if(arg.contains( "-queryPath")){
                queryPath = Args[i+1];
            }
            if(arg.contains("-datasetPath")){
                datasetPath = Args[i+1];
            }
            if(arg.contains("-INDEX_DIRECTORY")){
                INDEX_DIRECTORY = Args[i+1];
            }
            if(arg.contains("-resultPath")){
                resultPath = Args[i+1];
            }
            if(arg.contains("-MAX_RESULTS")){
                MAX_RESULTS = Integer.parseInt(Args[i+1]);
            }
            if(arg.contains("-Similarity")){
                similarityFlag = Args[i+1];
            }
            if(arg.contains("-Analyzer")){
                analyzerFlag = Args[i+1];
            }
            i++;
        }



        ArrayList<Model>  docs ;

        //List of analyser that can be chosen from the command line

        Analyzer analyzer = new EnglishAnalyzer();
        if(analyzerFlag.equals("2")){analyzer = new StandardAnalyzer();}
        if(analyzerFlag.equals("3")){ analyzer = new CustomAnalyzer();}
        if(analyzerFlag.equals("4")){ analyzer = new SimpleAnalyzer();}
        if(analyzerFlag.equals("5")){ analyzer = new WhitespaceAnalyzer();}




        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);



        config.setSimilarity(new BM25Similarity(2f, 0.88f));
//        if (similarityFlag.equals( "2")) {        config.setSimilarity(new ClassicSimilarity()); }
//        if (similarityFlag.equals( "3")) {        config.setSimilarity(new BooleanSimilarity()); }

        config.setRAMBufferSizeMB(1024);


        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config);

        docs = Extraction.extractDataset(datasetPath);

        System.out.println("Successfully Parsed all the documents ..... ");

        for(Model val: docs ){
            Document doc = new Document();
            doc.add(new StringField("id", val.id, Field.Store.YES));

            doc.add(new TextField("title", val.title, Field.Store.YES));
            doc.add(new TextField("bib", val.bib, Field.Store.YES));
            doc.add(new TextField("author", val.author, Field.Store.YES));
            doc.add(new TextField("words", val.words, Field.Store.YES));


//            doc.add(new Field("title", val.title, ft));
//            doc.add(new Field("bib", val.bib, ft));
//            doc.add(new Field("author", val.author, ft));
//            doc.add(new Field("words", val.words, ft));

            iwriter.addDocument(doc);


        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Corpus/IndexedContent"));

        for (Model val : docs){
            bufferedWriter.write("id ---> " + val.id+ "\n");
            bufferedWriter.write("title\n" + val.title+ "\n");
            bufferedWriter.write("author\n" + val.author+ "\n");
            bufferedWriter.write("bib\n" + val.bib+ "\n");
            bufferedWriter.write("words\n" + val.words+ "\n\n\n");

        }
        bufferedWriter.close();

        System.out.println("Successfully Indexed all the documents ..... ");



        iwriter.close();
        directory.close();

        Searcher.search();

        long end = System.currentTimeMillis();

        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");


    }
}



// To tokenize the dataset, doesn't improve the query as tested
//Document doc = null ;


//        FieldType ft = new FieldType(TextField.TYPE_STORED);
//        ft.setTokenized(true); //done as default
//        ft.setStoreTermVectors(true);
//        ft.setStoreTermVectorPositions(true);
//        ft.setStoreTermVectorOffsets(true);
//        ft.setStoreTermVectorPayloads(true);