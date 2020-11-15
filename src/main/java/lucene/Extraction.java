package lucene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Extraction {

    public static ArrayList<Model> extract(String fileName) throws IOException {
        ArrayList<Model>  docs  = new ArrayList<Model>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

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

    public static  ArrayList<String> extractQuery(String fileName) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();
        ArrayList<String> queries = new ArrayList<String>();
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
        return queries;
    }
}
