[DISCLAIMER]: Github mistakenly shows that the program is written in HTML but it is incorrect. It is programmed entirely in Java.

Advanced News Classifier

This project is an implementation of an advanced news classifier using GloVe embeddings and machine learning techniques. The classifier was built as part of an assignment for the Object Oriented Programming course at the University of Birmingham. The objective is to improve upon traditional TF-IDF and Cosine Similarity methods by leveraging the power of word embeddings and neural networks.

Project Structure

The project is divided into several tasks, each focusing on different aspects of the classifier. Below is a brief overview of each task and its components:


---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 1: Glove.java

This task involves creating a class to handle GloVe embeddings.

Subtasks:

Glove(String _vocabulary, Vector _vector): Initializes the GloVe object with a vocabulary and corresponding vector.

Additional methods to handle various functionalities required for the classifier.


---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 2: NewsArticles.java

This task focuses on handling news articles.

Subtasks:

Task 2.1 - Task 2.7: These methods involve processing and managing news article data, such as loading articles, parsing content, and categorizing data.


---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 3: HtmlParser.java

This task involves parsing HTML content to extract necessary information.

Subtasks:

getDataType(String _htmlCode): Extracts the data type from the HTML code.

getLabel(String _htmlCode): Extracts the label from the HTML code.


---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 4: Toolkit.java

This task involves creating utility methods to load GloVe embeddings and news articles.

Subtasks:

loadGlove(): Loads the GloVe embeddings from the provided file.

loadNews(): Loads news articles from the given dataset.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 5: ArticlesEmbedding

This task focuses on creating embeddings for articles.

Subtasks:

ArticlesEmbedding(String _title, String _content, NewsArticles.DataType _type, String _label): Constructor for creating article embeddings.

setEmbeddingSize(int _size): Sets the embedding size.

getNewsContent(): Retrieves the content of the news article.

getEmbedding(): Generates and returns the embedding for the article.


---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Task 6: AdvancedNewsClassifier

This is the main task where the classifier is constructed and trained.

Subtasks:

createGloveList(): Creates a list of GloVe embeddings.

calculateEmbeddingSize(List<ArticlesEmbedding> _listEmbedding): Calculates the size of embeddings.

populateEmbedding(): Populates embeddings for all articles.

populateRecordReaders(int _numberOfClasses): Prepares the data for the neural network.

predictResult(List<ArticlesEmbedding> _listEmbedding): Predicts the class of given articles.

printResults(): Prints the classification results.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
The following is the instruction to run the code.

Step 1: Clone the repository:

git clone https://github.com/Jim6942/Advanced-News-Classifier.git

cd Advanced-News-Classifier

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Step 2: Set up the environment:

Ensure you have the necessary libraries installed, including Deeplearning4J and NDArray4J.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Step 3: Load the data:

Place the GloVe file and news dataset in the appropriate directories as specified in the Toolkit.java file.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Step 4: Run the tasks:

Execute each class file according to the sequence mentioned in the tasks to ensure proper initialization and processing.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Step 5: Train and test the classifier:

Run the AdvancedNewsClassifier to train the model and test it on the provided dataset.
