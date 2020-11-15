package lucene;

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
