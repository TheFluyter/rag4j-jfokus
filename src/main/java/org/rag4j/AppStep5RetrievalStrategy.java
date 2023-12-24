package org.rag4j;

import org.rag4j.domain.RetrievalOutput;
import org.rag4j.workshop.AlphabetEmbedder;
import org.rag4j.indexing.Embedder;
import org.rag4j.indexing.IndexingService;
import org.rag4j.indexing.OpenNLPSentenceSplitter;
import org.rag4j.workshop.DocumentRetrievalStrategy;
import org.rag4j.retrieval.TopNRetrievalStrategy;
import org.rag4j.retrieval.WindowRetrievalStrategy;
import org.rag4j.store.InternalContentStore;

/**
 * The goal for this step is to experiment with a retrieval strategy to obtain a context to be used by an LLM to
 * generate an answer to a question. It must help you understand the difference between a chunk used for similarity
 * matching and the context used by the LLM to answer a question.
 */
public class AppStep5RetrievalStrategy {
    public static void main(String[] args) {
        String question = "Since when was the Vasa available for the public to visit?";

        // TODO: Initialize a retriever using the internal content store and the alphabet embedder.
        //  Use the IndexingService to index the documents in the file vasa-timeline.jsonl.
        // begin solution
        // end solution


        // TODO: Use the TopN retrieval strategy to obtain the context for a question.
        // begin solution
        // end solution

        // TODO: Migrate to the Window Retrieval Strategy to obtain the context for a question.
        // begin solution
        //end solution


        // TODO extra: Can you implement a new retrieval strategy? Think about a Document Retrieval Strategy!
        // begin solution
        // end solution
    }
}
