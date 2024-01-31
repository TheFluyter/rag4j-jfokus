package org.rag4j.retrieval;

import org.rag4j.domain.RelevantChunk;
import org.rag4j.domain.RetrievalOutput;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a basic retrieval strategy that returns the top N relevant chunks.
 */
public class TopNRetrievalStrategy implements RetrievalStrategy {
    private final Retriever retriever;

    public TopNRetrievalStrategy(Retriever retriever) {
        this.retriever = retriever;
    }

    @Override
    public RetrievalOutput retrieve(String question, int topN) {
        List<RelevantChunk> relevantChunks = retriever.findRelevantChunks(question, topN);
        return extractOutputFromRelevantChunks(relevantChunks);
    }

    @Override
    public RetrievalOutput retrieve(String question, List<Double> vector, int topN) {
        List<RelevantChunk> relevantChunks = retriever.findRelevantChunks(question, vector, topN);
        return extractOutputFromRelevantChunks(relevantChunks);
    }

    private static RetrievalOutput extractOutputFromRelevantChunks(List<RelevantChunk> relevantChunks) {
        List<RetrievalOutput.RetrievalOutputItem> retrievalOutputItems = relevantChunks.stream()
                .map(relevantItem -> RetrievalOutput.RetrievalOutputItem.builder()
                        .documentId(relevantItem.getDocumentId())
                        .chunkId(relevantItem.getChunkId())
                        .text(relevantItem.getText())
                        .build())
                .collect(Collectors.toList());

        return RetrievalOutput.builder().items(retrievalOutputItems).build();
    }
}
