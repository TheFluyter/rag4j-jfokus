package org.rag4j;

import com.azure.ai.openai.OpenAIClient;
import org.rag4j.domain.RetrievalOutput;
import org.rag4j.generation.AnswerGenerator;
import org.rag4j.generation.ObservedAnswerGenerator;
import org.rag4j.indexing.Embedder;
import org.rag4j.openai.OpenAIAnswerGenerator;
import org.rag4j.openai.OpenAIEmbedder;
import org.rag4j.openai.OpenAIFactory;
import org.rag4j.quality.AnswerQuality;
import org.rag4j.quality.AnswerQualityService;
import org.rag4j.retrieval.ObservedRetriever;
import org.rag4j.retrieval.Retriever;
import org.rag4j.retrieval.WindowRetrievalStrategy;
import org.rag4j.tracker.RAGObserver;
import org.rag4j.tracker.RAGTracker;
import org.rag4j.util.KeyLoader;
import org.rag4j.weaviate.WeaviateAccess;
import org.rag4j.weaviate.retrieval.WeaviateRetriever;

import java.util.List;


/**
 * The goal for this step is to put all the pieces together to create a functioning question answering system. This
 * question answering system harnesses the power of an LLM to generate an answer to a question with context you provide.
 * By observing the quality we can make changes to different pieces of the system to generate better and more precise answers.
 */
public class AppStep6AllTogether {
    public static void main(String[] args) {
        KeyLoader keyLoader = new KeyLoader();
        WeaviateAccess weaviateAccess = new WeaviateAccess(keyLoader);

        Embedder embedder = new OpenAIEmbedder(keyLoader);

        Retriever retriever = new WeaviateRetriever(weaviateAccess, embedder);

        List<String> exampleSentences = List.of(
                "How many bolts were replaced?",
                "Since When could people visit the Vasa?",
                "Since when was the Vasa available for the public to visit?",
                "Who was responsible for building the Vasa ship?",
                "Where did the person responsible for building the Vasa ship come from?"
        );

        // TODO: Make the code run.

        // solution
        WindowRetrievalStrategy windowRetrievalStrategy = null;
        AnswerGenerator answerGenerator = null;
        AnswerQualityService answerQuality = null;
        //end of solution

        List<AnswerQuality> overallQuality = exampleSentences.stream().map(question -> {
            RetrievalOutput retrievalOutput = windowRetrievalStrategy.retrieve(question, embedder.embed(question), 1, true);

            String answer = answerGenerator.generateAnswer(question, retrievalOutput.constructContext());
            System.out.printf("Question: %s%nAnswer: %s%n", question, answer);

            RAGObserver observer = RAGTracker.getRAGObserver();
            RAGTracker.cleanup();

            return answerQuality.determineQualityOfAnswer(observer);
        }).toList();

        System.out.printf("Overall quality answer to question: %.3f%n",
                overallQuality.stream().mapToInt(answer -> answer.getAnswerToQuestionQuality().getQuality())
                        .average().orElse(0));
        System.out.printf("Overall quality answer from context: %.3f%n",
                overallQuality.stream().mapToInt(answer -> answer.getAnswerFromContextQuality().getQuality())
                        .average().orElse(0));

        // TODO extra: Play around with the window size and the number of results to retrieve. Choose another retrieval
        //  strategy. Another splitter. Another embedder.
    }
}
