package uob.oop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Toolkit {
    public static List<String> listVocabulary = null;
    public static List<double[]> listVectors = null;
    private static final String FILENAME_GLOVE = "glove.6B.50d_Reduced.csv";

    public static final String[] STOPWORDS = {"a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"};

    public void loadGlove() throws IOException {
        BufferedReader myReader = null;
        //TODO Task 4.1 - 5 marks
        try{
            File path = Toolkit.getFileFromResource(FILENAME_GLOVE);
            myReader = new BufferedReader(new FileReader(path));
            String line;
            listVocabulary = new ArrayList<>();
            listVectors = new ArrayList<>();
            while ((line = myReader.readLine()) != null){
                String[] line_list = line.split(",");
                listVocabulary.add(line_list[0]);
                double[] doub_array = new double[line_list.length -1];
                for (int i = 1; i < line_list.length; i++){
                    doub_array[i-1] = Double.parseDouble(line_list[i]);
                }
                listVectors.add(doub_array);
            }

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        finally{
            myReader.close();
        }


    }

    private static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Toolkit.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException(fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    public List<NewsArticles> loadNews() {
        List<NewsArticles> listNews = new ArrayList<>();
        //TODO Task 4.2 - 5 Marks
        // open the News file
        // in news file, if end with htm, read it
        try {
            File news_folder = Toolkit.getFileFromResource("News");
            File[] SIUUU = news_folder.listFiles();
            List<File> file_list = new ArrayList<>(List.of(SIUUU));
            file_list.sort(Comparator.comparing(File::getName));

            for (File file : file_list){
                if (file.getName().endsWith(".htm")){
                    String path = file.getPath();   // Get the file path
                    String html_string = Files.readString(Path.of(path)); //Read the file by putting the path

                    // Call 4 methods from HTML parser and then add to NewsArticles object
                    String html_title = HtmlParser.getNewsTitle(html_string);
                    String html_news_content = HtmlParser.getNewsContent(html_string);
                    NewsArticles.DataType html_data_type = HtmlParser.getDataType(html_string);
                    String html_label = HtmlParser.getLabel(html_string);
                    NewsArticles article = new NewsArticles(html_title, html_news_content, html_data_type, html_label);
                    listNews.add(article);

                }
            }
        }
        catch (Exception ex){
            ex.getMessage();
        }

        return listNews;
    }

    public static List<String> getListVocabulary() {
        return listVocabulary;
    }

    public static List<double[]> getlistVectors() {
        return listVectors;
    }
}
