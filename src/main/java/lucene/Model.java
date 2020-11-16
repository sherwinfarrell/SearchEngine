package lucene;

// This class is created to store individual  documents extracted from files tha are in the shape of
// .I, .T, .A, .B, .W
public  class Model {
    String id;
    String title;
    String author;
    String bib;
    String words;
    public Model(String id, String title,  String author, String bib , String words){
        this.id = id;
        this.title = title;
        this.words = words;
        this.author = author;
        this.bib = bib;
    }
}
