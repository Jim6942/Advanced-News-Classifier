package uob.oop;

import org.apache.commons.lang3.time.StopWatch;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvancedNewsClassifier {
    public Toolkit myTK = null;
    public static List<NewsArticles> listNews = null;
    public static List<Glove> listGlove = null;
    public List<ArticlesEmbedding> listEmbedding = null;
    public MultiLayerNetwork myNeuralNetwork = null;

    public final int BATCHSIZE = 10;

    public int embeddingSize = 0;
    private static StopWatch mySW = new StopWatch();

    public AdvancedNewsClassifier() throws IOException {
        myTK = new Toolkit();
        myTK.loadGlove();
        listNews = myTK.loadNews();
        listGlove = createGloveList();
        listEmbedding = loadData();
    }

    public static void main(String[] args) throws Exception {
        mySW.start();
        AdvancedNewsClassifier myANC = new AdvancedNewsClassifier();

        myANC.embeddingSize = myANC.calculateEmbeddingSize(myANC.listEmbedding);
        myANC.populateEmbedding();
        myANC.myNeuralNetwork = myANC.buildNeuralNetwork(2);
        myANC.predictResult(myANC.listEmbedding);
        myANC.printResults();
        mySW.stop();
        System.out.println("Total elapsed time: " + mySW.getTime());
    }

    public List<Glove> createGloveList() {
        List<Glove> listResult = new ArrayList<>();
        //TODO Task 6.1 - 5 Marks
        //Get the word list and vector list
        //Loop through it and assign those to Glove object
        //Add the Glove object to the Glove list
        List<String> word_list = Toolkit.getListVocabulary();
        List<double[]> vectors_list = Toolkit.getlistVectors();

        for (int i = 0; i < word_list.size(); i++){
            if (!(is_in_stop_word(word_list.get(i)))){
                Glove my_glove = new Glove(word_list.get(i), new Vector(vectors_list.get(i)));
                listResult.add(my_glove);
            }

        }

        return listResult;
    }


    public static List<ArticlesEmbedding> loadData() {
        List<ArticlesEmbedding> listEmbedding = new ArrayList<>();
        for (NewsArticles news : listNews) {
            ArticlesEmbedding myAE = new ArticlesEmbedding(news.getNewsTitle(), news.getNewsContent(), news.getNewsType(), news.getNewsLabel());
            listEmbedding.add(myAE);
        }
        return listEmbedding;
    }

    public int calculateEmbeddingSize(List<ArticlesEmbedding> _listEmbedding) {
        int intMedian = -1;
        //TODO Task 6.2 - 5 Marks
        List<Integer> length_array = new ArrayList<>();



        for (int i = 0; i < _listEmbedding.size(); i++){
            //int news_content_len = _listEmbedding.get(i).getNewsContent().split(" ").length;
            //length_array.add(news_content_len);
            String[] word_list = _listEmbedding.get(i).getNewsContent().split(" ");
            int len_content = count_matches_in_glove_list(word_list);
            length_array.add(len_content);
        }

        int length_array_size = length_array.size();
        length_array.sort(null);

        if (length_array_size % 2 == 0){
            int right_median = length_array.get((length_array_size / 2) + 1);
            int left_median = length_array.get((length_array_size / 2));
            intMedian = (left_median + right_median) / 2;
        }
        else{
            intMedian = length_array.get((length_array_size + 1)/2);
        }

        return intMedian;
    }

    public void populateEmbedding() {
        //TODO Task 6.3 - 10 Marks
        // I have embedding size and list embedding.
        for (ArticlesEmbedding thing : listEmbedding){
            while (true){
                try {
                    thing.getEmbedding();
                    break;
                }
                catch (Exception e) {
                    if (e.toString().contains("InvalidSizeException")){
                        thing.setEmbeddingSize(embeddingSize);
                    }
                    else if (e.toString().contains("InvalidTextException")) {
                        thing.getNewsContent();
                    }


                }

            }

        }

    }

    public DataSetIterator populateRecordReaders(int _numberOfClasses) throws Exception {
        ListDataSetIterator myDataIterator = null;
        List<DataSet> listDS = new ArrayList<>();
        INDArray inputNDArray = null;
        INDArray outputNDArray = null;

        //TODO Task 6.4 - 8 Marks
        for (ArticlesEmbedding article : listEmbedding){

            if (article.getNewsType().equals(NewsArticles.DataType.Training)){
                outputNDArray = Nd4j.zeros(1, _numberOfClasses);
                int i = Integer.parseInt(article.getNewsLabel());
                outputNDArray.putScalar(i-1, 1);
                inputNDArray = article.getEmbedding();
                DataSet myDataSet = new DataSet (inputNDArray,outputNDArray);
                listDS.add(myDataSet);
            }


        }

        return new ListDataSetIterator(listDS, BATCHSIZE);
    }

    public MultiLayerNetwork buildNeuralNetwork(int _numOfClasses) throws Exception {
        DataSetIterator trainIter = populateRecordReaders(_numOfClasses);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .updater(Adam.builder().learningRate(0.02).beta1(0.9).beta2(0.999).build())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(embeddingSize).nOut(15)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.HINGE)
                        .activation(Activation.SOFTMAX)
                        .nIn(15).nOut(_numOfClasses).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        for (int n = 0; n < 100; n++) {
            model.fit(trainIter);
            trainIter.reset();
        }
        return model;
    }

    public List<Integer> predictResult(List<ArticlesEmbedding> _listEmbedding) throws Exception {
        List<Integer> listResult = new ArrayList<>();
        //TODO Task 6.5 - 8 Marks
        for (ArticlesEmbedding thing : _listEmbedding){
            if (thing.getNewsType().equals(NewsArticles.DataType.Testing)){
                int[] res = (myNeuralNetwork.predict(thing.getEmbedding()));
                listResult.add(res[0]);
                thing.setNewsLabel(String.format("%d", res[0]));
            }

        }


        return listResult;
    }

    public void printResults() {
        //TODO Task 6.6 - 6.5 Marks

        int len = myNeuralNetwork.layerSize(myNeuralNetwork.getnLayers() - 1);

        for (int i = 1; i <= len; i++){
            System.out.println(String.format("Group %d", i));
            for (ArticlesEmbedding my_article : listEmbedding){
                if (my_article.getNewsType().equals(NewsArticles.DataType.Testing)){
                    if (my_article.getNewsLabel().equals(String.format("%d", i-1))) {
                        System.out.println(String.format("%s", my_article.getNewsTitle()));
                    }
                }
            }
        }

    }

    public static boolean is_in_stop_word(String _word){
        for (String word : Toolkit.STOPWORDS){
            if (_word.equals(word)){
                return true;
            }
        }
        return false;
    }

    public static int count_matches_in_glove_list(String[] w) {
        int count = 0;
        for (String word : w){
            for (Glove glove_word: listGlove) {
                if (word.equals(glove_word.getVocabulary())) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

}
