/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.agent.chat;

@Configuration
@EnableConfigurationProperties({ ChatModelProperties.class, ChatAuthProperties.class })
public class OpenAiConfiguration {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	@ConditionalOnProperty(ChatModelProperties.PREFIX + ".deployment-name")
	public AzureOpenAiChatModel chatModel(OpenAIClient openAIClient, ChatModelProperties properties) {
		var openAIChatOptions = AzureOpenAiChatOptions.builder()
			.withDeploymentName(properties.getDeploymentName())
			.withTemperature(properties.getTemperature())
			.build();

		var functionCallbackContext = new FunctionCallbackContext();
		functionCallbackContext.setApplicationContext(applicationContext);

		return new AzureOpenAiChatModel(openAIClient, openAIChatOptions, functionCallbackContext);
	}

	@Bean
	@ConditionalOnProperty(ChatAuthProperties.PREFIX + ".client-id")
	public OpenAIClient openAIClientManagedIdentity(ChatAuthProperties properties) {
		return new OpenAIClientBuilder().endpoint(properties.getEndpoint())
			.credential(new DefaultAzureCredentialBuilder().managedIdentityClientId(properties.getClientId()).build())
			.buildClient();
	}

	@Bean
	@ConditionalOnProperty(ChatAuthProperties.PREFIX + ".api-key")
	public OpenAIClient openAIClientApiKey(ChatAuthProperties properties) {
		return new OpenAIClientBuilder().endpoint(properties.getEndpoint())
			.apiKey(properties.getApiKey())
			.buildClient();
	}

}