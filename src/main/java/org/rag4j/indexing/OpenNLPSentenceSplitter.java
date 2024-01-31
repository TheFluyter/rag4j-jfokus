package org.rag4j.indexing;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.rag4j.domain.Chunk;
import org.rag4j.domain.InputDocument;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This sentence splitter makes of the OpenNLP sentence splitter. It does not work really good if you ask me. We might
 * need a better one that knows how to distill a title using spaces or enters for instance.
 */
public class OpenNLPSentenceSplitter implements Splitter {
    public List<Chunk> split(InputDocument inputDocument) {
        String text = inputDocument.getText();
        SentenceDetectorME sentenceDetector = createSentenceDetector();
        String[] sentences = sentenceDetector.sentDetect(text);

        List<Chunk> chunks = new ArrayList<>();
        for (int i = 0; i < sentences.length; i++) {
            chunks.add(Chunk.builder()
                    .documentId(inputDocument.getDocumentId())
                    .chunkId(i)
                    .totalChunks(sentences.length)
                    .text(sentences[i])
                    .properties(inputDocument.getProperties())
                    .build());
        }

        return chunks;
    }

    private SentenceDetectorME createSentenceDetector() {
        String sentenceModelFilePath = "/opennlp/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";
        try (InputStream is = getClass().getResourceAsStream(sentenceModelFilePath)) {
            if (is == null) {
                throw new FileNotFoundException("Could not find file: " + sentenceModelFilePath);
            }
            return new SentenceDetectorME(new SentenceModel(is));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
