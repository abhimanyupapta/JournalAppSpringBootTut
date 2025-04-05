package net.engineeringdigest.journal.app.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3Service {

      private final S3Client s3Client;

      @Value("${aws.s3.bucketName}")
      private  String bucketName;

    public S3Service(@Value("${aws.accessKey}") String accessKey,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

      public String uploadFile(ObjectId userId, Path filePath) {
          String objectKey = "audios/" + userId + "/" + filePath.getFileName();

          s3Client.putObject(PutObjectRequest.builder()
                          .bucket(bucketName)
                          .key(objectKey)
                          .build(),
                  RequestBody.fromFile(filePath));

          return objectKey;
      }
}
