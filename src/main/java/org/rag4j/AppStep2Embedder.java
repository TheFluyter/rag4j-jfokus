package org.rag4j;

import org.rag4j.localembedder.AllMiniLmL6V2QEmbedder;
import org.rag4j.workshop.AlphabetEmbedder;
import org.rag4j.indexing.Embedder;

/**
 * The goal for this step is to learn to work with different embedders. We will use the {@link AlphabetEmbedder} and
 * the {@link org.rag4j.openai.OpenAIEmbedder} to create embeddings for a chunk of text.
 */
public class AppStep2Embedder {

    /**
     * TODO: Implement the {@link AlphabetEmbedder} and use it to create an embedding for the chunk.
     */
    public static void main(String[] args) {
        String chunk = "The Lion of the North, Gustav II Adolf, is building Sweden into one of the most feared powers in Europe.";

        // TODO: Check the size of the embedding, is it what you expected?
        Embedder embedder = new AlphabetEmbedder();
        System.out.println(embedder.embed(chunk));
        System.out.println(embedder.embed(chunk).size());

        // TODO: Now use the AllMiniLmL6V2QEmbedder to create an embedding for the chunk. What is the size of the embedding?
        // begin solution
        // end solution

        // TODO: Now use the OpenAIEmbedder to create an embedding for the chunk. What is the size of the embedding?
        // begin solution
        // end solution
    }
}
