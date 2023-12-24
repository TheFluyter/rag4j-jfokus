package org.rag4j;

import org.rag4j.domain.Chunk;
import org.rag4j.domain.InputDocument;
import org.rag4j.workshop.MaxSizeSplitter;
import org.rag4j.indexing.OpenNLPSentenceSplitter;

import java.util.List;

/**
 * The goal for this step is to understand the impact of a chunking strategy implemented by a splitter. The size of
 * the chunks that are created by the splitter can have a big impact on the precision of the system. If the chunks
 * are too small, the system will have a hard time to find the correct answer. If the chunks are too big, the system
 * will have a hard time to find the correct answer. The goal is to find a good balance between the two.
 */
public class AppStep1ChunkingStrategy {

    /**
     * This method initializes a splitter to split the provided text into chunks. Each chunk represents a piece of text
     * that can be matched against a question from the user.
     * @param inputDocument the document to split into chunks
     * @return the list of Chunks
     */
    public static List<Chunk> run(InputDocument inputDocument) {
        // TODO Use a splitter to split the text into chunks, notice that properties of the document are copied to the chunks
        // begin solution
        // end solution
        return List.of();
    }

    public static void main(String[] args) {
        InputDocument inputDocument = InputDocument.builder()
                .documentId("1")
                .text("The Lion of the North, Gustav II Adolf, is building Sweden into one of the most feared powers " +
                        "in Europe. In January 1625, the Swedish king signs a contract with the Dutch master " +
                        "shipwright Henrik Hybertsson and his business partner, Arendt de Groote. They are to build " +
                        "four new ships. One of them, Vasa, is to be the most powerful warship in the Baltic, if not " +
                        "the world. It is the beginning of one of the most spectacular fiascos in Swedish history.")
                .build();
        List<Chunk> foundChunks = run(inputDocument);

        // TODO check the response, how many chunks are found? Did you expect the chunks that are returned?
        // begin solution
        // end solution

        // TODO EXTRA: try to create a different splitter that creates chunks with a maximum size of 128 characters
        // begin solution
        // end solution
    }
}
