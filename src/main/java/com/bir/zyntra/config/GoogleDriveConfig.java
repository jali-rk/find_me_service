package com.bir.zyntra.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws Exception {
        InputStream credentialsStream =
                getClass().getResourceAsStream("/service-account.json");

        assert credentialsStream != null;
        Credentials credentials = ServiceAccountCredentials.fromStream(credentialsStream)
                .createScoped(List.of("https://www.googleapis.com/auth/drive.readonly"));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        )
                .setApplicationName("image-classification-pipeline")
                .build();
    }
}
