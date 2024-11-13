/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.genai;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads the veterinarians data into a vector store for the purpose of RAG functionality.
 *
 * @author Oded Shopen
 */
@Profile({ "!test" })
@Component
public class VectorStoreController {

	private final Logger logger = LoggerFactory.getLogger(VectorStoreController.class);

	private final VectorStore vectorStore;

	public VectorStoreController(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@EventListener
	public void loadVetDataToVectorStoreOnStartup(ApplicationStartedEvent event) throws IOException {
		Resource resource = new ClassPathResource("vectorstore.json");

		// In order to save on AI credits, use a pre-embedded database that was saved
		// to disk based on the current data in the h2 data.sql file
		((SimpleVectorStore) this.vectorStore).load(resource);
		logger.info("Vector store loaded from existing {} file in the classpath", resource.getFilename());
	}

	public Resource convertListToJsonResource(List<Vet> vets) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Convert List<Vet> to JSON string
			String json = objectMapper.writeValueAsString(vets);

			// Convert JSON string to byte array
			byte[] jsonBytes = json.getBytes();

			// Create a ByteArrayResource from the byte array
			return new ByteArrayResource(jsonBytes);
		}
		catch (JsonProcessingException e) {
			logger.error("Problems encountered when generating JSON from the vets list", e);
			return null;
		}
	}

}
