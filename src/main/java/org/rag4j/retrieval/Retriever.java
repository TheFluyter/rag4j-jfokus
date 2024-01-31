package org.rag4j.retrieval;

import org.rag4j.domain.Chunk;
import org.rag4j.domain.RelevantChunk;

import java.util.List;

/**
 * This interface is used to retrieve relevant chunks for a given question. Next to the functions to retrieve the
 * answer using the question or the vector representation of the question, it also provides a function to loop over
 * all chunks. Finally it contains a method to get a specific chunk.
 */
public interface Retriever {
    List<RelevantChunk> findRelevantChunks(String question, int maxResults);
    List<RelevantChunk> findRelevantChunks(String question, List<Double> vector, int maxResults);

    Chunk getChunk(String documentId, int chunkId);

    void loopOverChunks(ChunkProcessor chunkProcessor);
}
