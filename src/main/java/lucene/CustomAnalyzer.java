package lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;
import java.util.List;

public class CustomAnalyzer extends Analyzer {

    List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
            "for", "if", "in", "into", "is", "it",
            "no", "not", "of", "on", "or", "such",
            "that", "the", "their", "then", "there", "these",
            "they", "this", "to", "was", "will", "with", "."
    );
    CharArraySet stopSet = new CharArraySet(stopWords, false);

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        final StandardTokenizer src = new StandardTokenizer();

        TokenFilter filter = new LowerCaseFilter(src);
        filter = new StopFilter(filter,stopSet );
        filter =  new PorterStemFilter(filter);
        //filter = new EnglishMinimalStemFilter(filter);


        return new TokenStreamComponents(src, filter);
    }

}
