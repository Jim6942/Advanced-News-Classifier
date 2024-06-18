package uob.oop;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.Properties;


public class ArticlesEmbedding extends NewsArticles {
    private int intSize = -1;
    private String processedText = "";

    private INDArray newsEmbedding = Nd4j.create(0);

    public ArticlesEmbedding(String _title, String _content, NewsArticles.DataType _type, String _label) {
        //TODO Task 5.1 - 1 Mark
        super(_title,_content,_type,_label);
    }

    public void setEmbeddingSize(int _size) {
        //TODO Task 5.2 - 0.5 Marks
        intSize = _size;
    }

    public int getEmbeddingSize(){
        return intSize;
    }

    @Override
    public String getNewsContent() {
        //TODO Task 5.3 - 10 Marks
        String content = textCleaning(super.getNewsContent()); // Cleaned
        if(!processedText.isEmpty()){
            return processedText;
        }

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = pipeline.processToCoreDocument(content);

        StringBuilder lemmatized_content = new StringBuilder();
        for (CoreLabel word : document.tokens()){
            lemmatized_content.append(word.lemma()).append(" ");
        }
        String lemmatized_string = lemmatized_content.toString().trim(); // Lemmatized now

        lemmatized_string = removeStopWords(lemmatized_string, Toolkit.STOPWORDS);

        processedText = lemmatized_string.toLowerCase();

        return processedText.trim();
    }

    public INDArray getEmbedding() throws Exception {
        //TODO Task 5.4 - 20 Marks

        if (intSize == -1) {
            throw new InvalidSizeException("Invalid size");
        }
        if (processedText.isEmpty()) {
            throw new InvalidTextException("Invalid text");
        }
        if (!newsEmbedding.isEmpty()) {
            return Nd4j.vstack(newsEmbedding.mean(1));
        }
        String[] my_words_array = processedText.split(" ");
        int word_vector_size = AdvancedNewsClassifier.listGlove.get(0).getVector().getVectorSize();
        newsEmbedding = Nd4j.zeros(intSize, word_vector_size);

        int i = 0;

        for (String word : my_words_array) {
            Glove glove = find_word_in_glove(word);

            if (glove != null) {
                newsEmbedding.putRow(i++, Nd4j.create(glove.getVector().getAllElements()));
            }

            if (i >= intSize) {
                break;
            }
        }

        return Nd4j.vstack(newsEmbedding.mean(1));
    }

    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    private static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();

        for (char c : _content.toLowerCase().toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || Character.isWhitespace(c)) {
                sbContent.append(c);
            }
        }

        return sbContent.toString().trim();
    }

    private Glove find_word_in_glove(String word) {
        for (Glove glove : AdvancedNewsClassifier.listGlove) {
            if (glove.getVocabulary().equals(word)) {
                return glove;
            }
        }
        return null;
    }
    public static String removeStopWords(String _content, String[] _stopWords) {
        StringBuilder sbConent = new StringBuilder();
        //TODO Task 2.3 - 3 marks
        // Split the content
        // Make a two dimensional array, and for each word in content, check for stop words and remove them

        String[] s = _content.split(" ");

        for (int i = 0; i < s.length; i++){
            int match = 0;
            for (int j = 0; j < _stopWords.length; j++){
                if (s[i].equals(_stopWords[j])){
                    match++;
                    break;
                }
            }
            if (match == 0){
                sbConent.append(s[i]);
                sbConent.append(" ");
            }
        }


        return sbConent.toString().trim();
    }
}

