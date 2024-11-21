# How to implement your first AI application

This sample is an AI chat assistant based on Retrieval Augmented Generation(RAG), it uses Spring AI SDKs to connect to Azure OpenAI service.

## Concept

Here we introduce some key concept on how to implement an AI application.

1. Spring AI

   [Spring AI](https://spring.io/projects/spring-ai) is an application framework for AI engineering. Its goal is to apply to the AI domain Spring ecosystem design principles such as portability and modular design and promote using POJOs as the building blocks of an application to the AI domain.

   There's another popular AI framework [langchain4j](https://docs.langchain4j.dev/intro), and you may find the samples in [Spring PetClinic With OpenAI and Langchain4j](https://github.com/Azure-Samples/spring-petclinic-langchain4j).

1. RAG in Azure OpenAI

   RAG with Azure OpenAI allows developers to use supported AI chat models that can reference specific sources of information to ground the response. Adding this information allows the model to reference both the specific data provided and its pretrained knowledge to provide more effective responses.

   Azure OpenAI enables RAG by connecting pretrained models to your own data sources. Azure OpenAI on your data utilizes the search ability of Azure AI Search to add the relevant data chunks to the prompt. Once your data is in a AI Search index, Azure OpenAI on your data goes through the following steps:

   - Receive user prompt.
   - Determine relevant content and intent of the prompt.
   - Query the search index with that content and intent.
   - Insert search result chunk into the Azure OpenAI prompt, along with system message and user prompt.
   - Send entire prompt to Azure OpenAI.
   - Return response and data reference (if any) to the user.

   By default, Azure OpenAI on your data encourages, but doesn't require, the model to respond only using your data. This setting can be unselected when connecting your data, which may result in the model choosing to use its pretrained knowledge over your data.

   See more from [Spring AI Chat Client](https://docs.spring.io/spring-ai/reference/api/chatclient.html) and [Implement Retrieval Augmented Generation (RAG) with Azure OpenAI Service ](/training/modules/use-own-data-azure-openai).

## Code implementation

Some introduction to the code for readers to understand the flows of the first AI application.

1. The Rest Controller to talk to ChatClient

   In [PetclinicChatClient](https://github.com/Azure-Samples/spring-petclinic-ai/blob/main/src/main/java/org/springframework/samples/petclinic/genai/PetclinicChatClient.java), we implement the rest function `/chat`. And in this function, we call the chatClient with user inputs.

   ```java
   return this.chatClient.prompt().user(u -> u.text(query)).call().content();
   ```

1. We use [ChatConfiguration](https://github.com/Azure-Samples/spring-petclinic-ai/blob/main/src/main/java/org/springframework/samples/petclinic/genai/ChatConfiguration.java) to customize the chatClient.

   Some key configuration of the chatClient, see function `ChatClientCustomizer`:
   - Client to connect to Azure OpenAI. Both api-key and managed identity supported.
   - ChatModel. Deployment gpt-4o and temperature 0.7 is set in configuration file.
   - VectorStore. The vectorestore is used to store the semantic vectors (embeddings).
   - System Prompt. Customize AI behavior and enhance performance.
   - Functions. Customized functions for OpenAI to interact with business system, these functions define the AI capabilities to your business.
   - Advisors. Provides a flexible and powerful way to intercept, modify, and enhance AI-driven interactions in your Spring applications.

1. Functions

   The bean names of `java.util.Function`s defined in the application context. We implemented some functions in [AIFunctionConfiguration](https://github.com/Azure-Samples/spring-petclinic-ai/blob/main/src/main/java/org/springframework/samples/petclinic/genai/AIFunctionConfiguration.java).

   - The @Description annotations of these functions help the AI models to understand the functions.
   - The function body varies depending on your business requirements.

1. Advisors

   See more from [Spring AI advisors](https://docs.spring.io/spring-ai/reference/api/advisors.html).

   In this simple example, we implement a [ModeledQuestionAnswerAdvisor](https://github.com/Azure-Samples/spring-petclinic-ai/blob/main/src/main/java/org/springframework/samples/petclinic/genai/ModeledQuestionAnswerAdvisor.java). First a call to AI to generate a new user prompt and then use the AI-refined user prompt to retrieve again. We find the two-step user prompts improve the quality of the response.