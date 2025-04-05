package net.engineeringdigest.journal.app.service;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class JournalEntryAudioService {

    @Value("${elevenLabs.key}")
    private String apiKey;

    @Value("${elevenLabs.url}")
    private String apiUrl;

    private final String voiceId = "JBFqnCBsd6RMkjVDRZzb?";

    @Autowired
    private RestTemplate restTemplate;

    public byte[] textToSpeech(String text) {
        apiUrl += voiceId;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("xi-api-key", apiKey);
        httpHeaders.set("Content-Type", "application/json");
        String request = "{\n  \"text\": \"" + text + "\",\n  \"model_id\": \"eleven_multilingual_v2\"\n}";

        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<byte[]> response;

        try {
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, byte[].class);
        } catch (Exception e) {
            log.error("Failed while converting to audio", e);
            throw new RuntimeException("failed while converting to audio", e);
        }

        return response.getBody();
    }

    public String textToSpeechAndUpload(ObjectId userId, ObjectId journalEntryId, String text, S3Service s3Service) {
         byte[] audioData = textToSpeech(text);
         String filePath = "output-" + journalEntryId + ".mp3";

        // Save audio file locally
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(audioData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save audio file", e);
        }

         String s3Key = s3Service.uploadFile(userId, Paths.get(filePath));

         try {
             Files.delete(Paths.get(filePath));
         } catch (IOException e) {
              log.error("could not delete the local output audio file", e);
         }

         return s3Key;
    }
}
