package com.bir.zyntra.batch.image;

import com.bir.zyntra.batch.image.processor.ImageProcessor;
import com.bir.zyntra.batch.image.reader.ImageReader;
import com.bir.zyntra.batch.image.writer.ImageWriter;
import com.bir.zyntra.model.Image;
import com.bir.zyntra.repository.ImageRepository;
import com.bir.zyntra.service.storage.s3.S3Service;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class ImageJobConfig {

    @Bean
    @StepScope
    public ImageReader imageReader(
            Drive drive,
            @Value("#{jobParameters['folderUrl']}") String folderUrl
    ) {
        return new ImageReader(drive, folderUrl);
    }

    @Bean
    public Step imageStep(
            JobRepository jobRepository,
            PlatformTransactionManager batchTransactionManager,
            ImageReader reader,
            ImageProcessor processor,
            ImageWriter writer) {

        return new StepBuilder("image-step", jobRepository)
                .<File, Image>chunk(5)
                .transactionManager(batchTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job imageJob(
            JobRepository jobRepository,
            Step imageStep,
            ImageJobListener listener)
    {
        return new JobBuilder("image-job", jobRepository)
                .start(imageStep)
                .listener(listener)
                .build();
    }
}
