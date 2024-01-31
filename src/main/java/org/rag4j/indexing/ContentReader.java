package org.rag4j.indexing;

import org.rag4j.domain.InputDocument;

import java.util.stream.Stream;

/**
 * Component used to read the content of a document. It returns a stream of {@link InputDocument}s.
 */
public interface ContentReader {
    Stream<InputDocument> read();
}
