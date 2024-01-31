package org.rag4j.indexing;

import java.util.List;

/**
 * Component used to create an embedding for a piece of text.
 */
public interface Embedder {
    /**
     * Creates an embedding for the provided text.
     * @param text the text to create an embedding for
     * @return the embedding
     */
    List<Double> embed(String text);
}
