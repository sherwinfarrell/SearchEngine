package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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




public class Indexer {

    public static String INDEX_DIRECTORY = "";
    public static String datasetPath = "";
    public static String queryPath = "";
    public static String resultPath = "";
    public static int MAX_RESULTS = 100;
    public static String similarityFlag = "";
    public static String analyzerFlag = "";



    public static void main(String Args[]) throws IOException, ParseException {

            // start time for program execution time
            long programStart = System.currentTimeMillis();


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




        //List of analyser that can be chosen from the command line

        Analyzer analyzer = new EnglishAnalyzer();
        if(analyzerFlag.equals("2")){analyzer = new StandardAnalyzer();}
        if(analyzerFlag.equals("3")){ analyzer = new CustomAnalyzer();}
        if(analyzerFlag.equals("4")){ analyzer = new SimpleAnalyzer();}
        if(analyzerFlag.equals("5")){ analyzer = new WhitespaceAnalyzer();}



        // Getting the path to the directory and then creating an index writer with the given analyzer
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);


        // Similarity is set here, using the option from th command line, default is the BM25 if no Similarity is given
        config.setSimilarity(new BM25Similarity(2f, 0.88f));
        if (similarityFlag.equals( "2")) {        config.setSimilarity(new ClassicSimilarity()); }
        if (similarityFlag.equals( "3")) {        config.setSimilarity(new BooleanSimilarity()); }





        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config);

        System.out.println("Parsing and indexing the document Simultaenously, Because of speed improvement ..... ");

        // Function that Extracts the dataset Simultaenously
        Extraction.indexDataset(datasetPath,iwriter);
        System.out.println("Finished Parsing and indexing the document..... ");



        iwriter.close();
        directory.close();

        //Searching for the queries in the indexed Dataset
        Searcher.search();

        // Getting the time to calculate the execution time
        long programEnd = System.currentTimeMillis();
        System.out.println("The application took : " + (programEnd - programStart)/1000f + "s to Execute");


    }
}




