# Lucene Assignment 1 
The program can be run using the following command in the **Project root** :  
cd LuceneAssignment  
mvn clean install   
mvn package   
java -jar target/Assignment1-1.0-SNAPSHOT.jar  
To run trec_eval:  
./Corpus/trec_eval/trec_eval-9.0.7/trec_eval Corpus/QRelsCorrectedforTRECeval Corpus/results.txt


### Arguments : 
-INDEX_DIRECTORY Index/ -queryPath Corpus/cran.qry -datasetPath Corpus/cran.all.1400 -MAX_RESULTS 30  -resultPath Corpus/results.txt -Similarity 2 -Analyzer 2  


### The Arguments are : 
```
INDEX_DIRECTORY - Directory of where the index will be located.   
queryPath - Path to the query. For eg: Corpus/cran.qry  
datasetPath - Path to the Cranfield dataset. Eg. Corpus/cran.all.1400  
MAX_RESULTS - The number of results that the IR system outputs for each query  
resultPath - Where the results.txt file will be created. 
``` 

**Similarity - The similarities that can be used**  

        Similarities available are :  
        Option 1) BM25Similarity  
        Option 2) ClassicSimilarity  
        Option 3) BooleanSimilarity  
                
**Analyzer - The Analyzer that can be used :**  
```
   1) EnglishAnalyzer  
   2)StandardAnalyzer  
   3)CustomAnalyzer  
   4)SimpleAnalyzer  
   5)WhitespaceAnalyzer  
```           
# Running trec Eval : 

The trec eval function is located here /home/ubuntu/LuceneAssignment/Corpus/trec_eval/trec_eval-9.0.7

Command can be run from the **project root** directory :  
./Corpus/trec_eval/trec_eval-9.0.7/trec_eval Corpus/QRelsCorrectedforTRECeval Corpus/results.txt


## Refrences 

Custom Analyzer : Lucen In Action Book by Erik Hatcher


MultiField Parser:  
Lucene in Action 
https://lucene.apache.org/core/8.6.3/queryparser/org/apache/lucene/queryparser/classic/MultiFieldQueryParser.html

Boosting scores for a multifield parser 
https://lucene.apache.org/core/8.6.3/queryparser/org/apache/lucene/queryparser/classic/MultiFieldQueryParser.html
https://stackoverflow.com/questions/18260408/lucene-boosting-scoring-with-multiple-columns

BM25 vs TFIDF (Definitions on k1 and b used in Lucene's BM25):  
https://opensourceconnections.com/blog/2015/10/16/bm25-the-next-generation-of-lucene-relevation/

Trec_Eval Scores:
https://trec.nist.gov/pubs/trec15/appendices/CE.MEASURES06.pdf  
https://vtechworks.lib.vt.edu/bitstream/handle/10919/52528/MidtermModuleTeam5-TRECevalFinal.pdf;jsessionid=8CF15EC2503F34A0FBBF0CF45FBA750E?sequence=1  
http://www.rafaelglater.com/en/post/learn-how-to-use-trec_eval-to-evaluate-your-information-retrieval-system
