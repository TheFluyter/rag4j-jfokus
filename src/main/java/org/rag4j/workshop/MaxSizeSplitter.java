package org.rag4j.workshop;

import org.rag4j.domain.Chunk;
import org.rag4j.domain.InputDocument;
import org.rag4j.indexing.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * A splitter to transform the InputDocument into chunks of max size. A chunk should never be split in the middle of
 * a word. If a word is longer than the max size, it should be added to the next chunk. A chunk should end with a
 * space.TIP: Use the available unit test to verify your implementation.
 */
public class MaxSizeSplitter implements Splitter {
    private final int maxSize;

    public MaxSizeSplitter(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Splits the input document into chunks of max size.
     *
     * @param inputDocument the input document to split
     * @return the list of chunks
     */
    @Override
    public List<Chunk> split(InputDocument inputDocument) {
        List<Chunk> chunks = new ArrayList<>();
        // TODO: implement this method
        // beginning of solution
        // end of solution

        return chunks;
    }
}
