package org.rag4j;

import com.azure.ai.openai.OpenAIClient;
import org.rag4j.generation.AnswerGenerator;
import org.rag4j.openai.OpenAIAnswerGenerator;
import org.rag4j.openai.OpenAIConstants;
import org.rag4j.openai.OpenAIFactory;
import org.rag4j.quality.AnswerQuality;
import org.rag4j.quality.AnswerQualityService;
import org.rag4j.tracker.RAGObserver;
import org.rag4j.util.KeyLoader;

/**
 * The goal for this step is to understand how it works to create an answer to a question given a context. The
 * {@link OpenAIAnswerGenerator} is used to generate an answer. The {@link OpenAIClient} is used to communicate with the
 * OpenAI API. The {@link KeyLoader} is used to load the OpenAI key from the environment variables. The LLM is also used
 * to determine the quality of the answer. The answer is compared to the question, to see if it answers the question.
 * The answer is also compared to the context, to see if it sticks to the context to prevent hallucination.
 */
public class AppStep4Generator {
    public static void main(String[] args) {
        KeyLoader keyLoader = new KeyLoader();
        AnswerGenerator answerGenerator = new OpenAIAnswerGenerator(keyLoader, OpenAIConstants.DEFAULT_MODEL);
        OpenAIClient openAIClient = OpenAIFactory.obtainsClient(keyLoader.getOpenAIKey());

        String question = "Since when was the Vasa available for the public to visit?";
        String context = "By Friday 16 February 1962, the ship is ready to be displayed to the general public at the " +
                "newly-constructed Wasa Shipyard, where visitors can see Vasa while a team of conservators, " +
                "carpenters and other technicians work to preserve the ship.";

        // TODO: Generate an answer to the question given the context.
        // begin solution
        // end solution

        // TODO: Create a new RAGObserver object and use it with the AnswerQualityService class determine the quality of
        //  the answer in relation to the question and the context.
        // begin solution
        // end solution
    }
}
