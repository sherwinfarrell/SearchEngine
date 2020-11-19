package lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;
import java.util.List;

public class CustomAnalyzer extends Analyzer {

    // Lucenes Default Stop Word set with added "." term

    List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
            "for", "if", "in", "into", "is", "it",
            "no", "not", "of", "on", "or", "such",
            "that", "the", "their", "then", "there", "these",
            "they", "this", "to", "was", "will", "with", "."
    );
    CharArraySet stopSet = new CharArraySet(stopWords, false);
    // Standard tokenizer has been recommended in Lucene in Action as it acts like the standard analyzer
    // Then extra English language related filters are applied. This is because the English Analyzer does the best, much better than the standard
    // analyzer.
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        final StandardTokenizer standardToken = new StandardTokenizer();

        TokenFilter filter = new LowerCaseFilter(standardToken);
        filter = new StopFilter(filter,stopSet );
        filter =  new PorterStemFilter(filter);
        //filter = new EnglishMinimalStemFilter(filter);


        return new TokenStreamComponents(standardToken, filter);
    }

}
