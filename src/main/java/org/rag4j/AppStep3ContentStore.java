package org.rag4j;

import org.rag4j.domain.InputDocument;
import org.rag4j.domain.RelevantChunk;
import org.rag4j.indexing.*;
import org.rag4j.quality.QuestionAnswerRecord;
import org.rag4j.quality.RetrievalQuality;
import org.rag4j.quality.RetrievalQualityService;
import org.rag4j.store.InternalContentStore;
import org.rag4j.workshop.AlphabetEmbedder;

import java.util.List;

/**
 * The goal for this step is to learn to work with the {@link org.rag4j.indexing.ContentStore}. You combine the splitter
 * with an embedder to create a content store. What fun is there to store content without retrieving it. You start with
 * the {@link AlphabetEmbedder} and the {@link InternalContentStore} to store and retrieve content. We use an evaluation
 * metric to determine the quality of the retrieval. The metric is called precision.
 */
public class AppStep3ContentStore {
    private static double determineQualityOfOurRetriever(InternalContentStore contentStore, Embedder embedder) {
        RetrievalQualityService retrievalQualityService = new RetrievalQualityService(contentStore);
        List<QuestionAnswerRecord> questionAnswerRecords =
                retrievalQualityService.readQuestionAnswersFromFile("/data/question_answers.text");
        RetrievalQuality retrievalQuality =
                retrievalQualityService.obtainRetrievalQuality(questionAnswerRecords, embedder);
        return retrievalQuality.getPrecision();
    }

    public static void main(String[] args) {
        Embedder embedder = new AlphabetEmbedder();
        InternalContentStore contentStore = new InternalContentStore(embedder);

        InputDocument document = InputDocument.builder()
                .documentId("doc1")
                .text("The Lion of the North, Gustav II Adolf, is building Sweden into one of the most feared powers " +
                        "in Europe. In January 1625, the Swedish king signs a contract with the Dutch master " +
                        "shipwright Henrik Hybertsson and his business partner, Arendt de Groote. They are to build " +
                        "four new ships. One of them, Vasa, is to be the most powerful warship in the Baltic, if not " +
                        "the world. It is the beginning of one of the most spectacular fiascos in Swedish history.")
                .build();

        // TODO: Store the document in the content store.
        // begin solution
        //end solution

        // TODO: Use the retriever part of the content store to find the most relevant chunks for the question.
        String question = "Who did the Swedish king sign a contract with?";
        // begin solution
        // end solution

        // TODO: What is the quality of the retrieval? Use the method above to determine the quality of the retrieval.
        //  Use the IndexingService to index the documents from the file vasa-timeline.jsonl into the internal
        //  content store. Use the OpenNLPSentenceSplitter to split the documents into chunks.
        // begin solution
        // end solution

        // TODO: Do the same things as above (splitting, indexing, quality check), but now use the AllMiniLmL6V2QEmbedder
        //  instead of the AlphabetEmbedder. Did the quality get better, or worse? And why do you think that is?
        // begin solution
        // end solution
    }
}
