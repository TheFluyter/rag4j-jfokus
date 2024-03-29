package org.rag4j.localembedder;

import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.Vocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.modality.nlp.bert.BertTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ai.onnxruntime.OnnxTensor.createTensor;
import static java.lang.Math.min;
import static java.nio.LongBuffer.wrap;
import static java.util.stream.Collectors.toList;

/**
 * This class is copied from the great Language4J project. We took only what we needed. If you need more, please check
 * out the original project: <a href="https://langchain.4j.org/">Langchain4j</a>
 */
public class OnnxBertBiEncoder {

    private static final String CLS = "[CLS]";
    private static final String SEP = "[SEP]";
    private static final int MAX_SEQUENCE_LENGTH = 510; // 512 - 2 (special tokens [CLS] and [SEP])

    private final OrtEnvironment environment;
    private final OrtSession session;
    private final BertTokenizer tokenizer;
    private final Vocabulary vocabulary;

    public OnnxBertBiEncoder() {
        try {
            InputStream modelInputStream = OnnxBertBiEncoder.class.getResourceAsStream("/onnx/all-minilm-l6-v2-q.onnx");
            this.environment = OrtEnvironment.getEnvironment();
            this.session = environment.createSession(loadModel(modelInputStream));
            this.vocabulary = createVocabularyFrom(getClass().getResource("/onnx/bert-vocabulary-en.txt"));
            this.tokenizer = new BertFullTokenizer(vocabulary, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float[] embed(String text) {

        List<String> wordPieces = tokenizer.tokenize(text);
        List<List<String>> partitions = partition(wordPieces, MAX_SEQUENCE_LENGTH);

        List<float[]> embeddings = new ArrayList<>();
        for (List<String> partition : partitions) {
            long[] tokens = toTokens(partition);
            try (Result result = encode(tokens)) {
                float[] embedding = toEmbedding(result);
                embeddings.add(embedding);
            } catch (OrtException e) {
                throw new RuntimeException(e);
            }
        }

        List<Integer> weights = partitions.stream()
                .map(List::size)
                .collect(toList());

        return normalize(weightedAverage(embeddings, weights));
    }

    private static List<List<String>> partition(List<String> wordPieces, int partitionSize) {
        List<List<String>> partitions = new ArrayList<>();
        for (int from = 0; from < wordPieces.size(); from += partitionSize) {
            int to = min(wordPieces.size(), from + partitionSize);
            List<String> partition = wordPieces.subList(from, to);
            partitions.add(partition);
        }
        return partitions;
    }

    private long[] toTokens(List<String> wordPieces) {
        long[] tokens = new long[wordPieces.size() + 2];

        int i = 0;
        tokens[i++] = vocabulary.getIndex(CLS);
        for (String wordPiece : wordPieces) {
            tokens[i++] = vocabulary.getIndex(wordPiece);
        }
        tokens[i] = vocabulary.getIndex(SEP);

        return tokens;
    }

    private Result encode(long[] tokens) throws OrtException {

        long[] attentionMasks = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            attentionMasks[i] = 1L;
        }

        long[] tokenTypeIds = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            tokenTypeIds[i] = 0L;
        }

        long[] shape = {1, tokens.length};

        try (
                OnnxTensor tokensTensor = createTensor(environment, wrap(tokens), shape);
                OnnxTensor attentionMasksTensor = createTensor(environment, wrap(attentionMasks), shape);
                OnnxTensor tokenTypeIdsTensor = createTensor(environment, wrap(tokenTypeIds), shape)
        ) {
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("input_ids", tokensTensor);
            inputs.put("token_type_ids", tokenTypeIdsTensor);
            inputs.put("attention_mask", attentionMasksTensor);

            return session.run(inputs);
        }
    }

    private float[] toEmbedding(Result result) throws OrtException {
        float[][] vectors = ((float[][][]) result.get(0).getValue())[0];
        return pool(vectors);
    }

    private float[] pool(float[][] vectors) {
        return meanPool(vectors);
    }

    private static float[] meanPool(float[][] vectors) {

        int numVectors = vectors.length;
        int vectorLength = vectors[0].length;

        float[] averagedVector = new float[vectorLength];

        for (float[] vector : vectors) {
            for (int j = 0; j < vectorLength; j++) {
                averagedVector[j] += vector[j];
            }
        }

        for (int j = 0; j < vectorLength; j++) {
            averagedVector[j] /= numVectors;
        }

        return averagedVector;
    }

    private float[] weightedAverage(List<float[]> embeddings, List<Integer> weights) {
        if (embeddings.size() == 1) {
            return embeddings.get(0);
        }

        int dimensions = embeddings.get(0).length;

        float[] averagedEmbedding = new float[dimensions];
        int totalWeight = 0;

        for (int i = 0; i < embeddings.size(); i++) {
            int weight = weights.get(i);
            totalWeight += weight;

            for (int j = 0; j < dimensions; j++) {
                averagedEmbedding[j] += embeddings.get(i)[j] * weight;
            }
        }

        for (int j = 0; j < dimensions; j++) {
            averagedEmbedding[j] /= totalWeight;
        }

        return averagedEmbedding;
    }

    private static float[] normalize(float[] vector) {

        float sumSquare = 0;
        for (float v : vector) {
            sumSquare += v * v;
        }
        float norm = (float) Math.sqrt(sumSquare);

        float[] normalizedVector = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / norm;
        }

        return normalizedVector;
    }

    int countTokens(String text) {
        return tokenizer.tokenize(text).size();
    }

    private byte[] loadModel(InputStream modelInputStream) {
        try (
                InputStream inputStream = modelInputStream;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        ) {
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Vocabulary createVocabularyFrom(URL vocabularyFile) {
        try {
            return DefaultVocabulary.builder()
                    .addFromTextFile(vocabularyFile)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
