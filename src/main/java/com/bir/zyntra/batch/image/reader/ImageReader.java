package com.bir.zyntra.batch.image.reader;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;

import static com.bir.zyntra.util.GoogleDriveHelpers.extractFolderId;

@Component
@RequiredArgsConstructor
public class ImageReader implements ItemStreamReader<File> {

    private static final String CTX_PAGE_TOKEN = "gdrive.nextPageToken";
    private static final String CTX_NO_MORE_PAGES = "gdrive.noMorePages";
    private static final String CTX_LAST_PROCESSED_FILE_ID = "gdrive.lastProcessedFileId";

    private final Drive drive;
    private final String folderUrl;

    private Iterator<File> fileIterator = Collections.emptyIterator();
    private String nextPageToken = null;
    private boolean noMorePages = false;
    private String lastProcessedFileId;


    @Override
    public void open(ExecutionContext executionContext) {
        this.nextPageToken = executionContext.containsKey(CTX_PAGE_TOKEN) ? executionContext.getString(CTX_PAGE_TOKEN) : null;
        this.noMorePages = executionContext.containsKey(CTX_NO_MORE_PAGES) && (Boolean) executionContext.get(CTX_NO_MORE_PAGES);
        this.lastProcessedFileId = executionContext.containsKey(CTX_LAST_PROCESSED_FILE_ID) ? executionContext.getString(CTX_LAST_PROCESSED_FILE_ID) : null;
    }

    @Override
    public void update(ExecutionContext executionContext) {
        executionContext.putString(CTX_PAGE_TOKEN, nextPageToken);
        executionContext.putString(CTX_NO_MORE_PAGES, String.valueOf(noMorePages));
        executionContext.putString(CTX_LAST_PROCESSED_FILE_ID, lastProcessedFileId);
    }

    @Override
    public File read() throws Exception {
        if (!fileIterator.hasNext() && !noMorePages) {
            fetchNextPage();
        }

        if (fileIterator.hasNext()) {
            File nextFile = fileIterator.next();
            lastProcessedFileId = nextFile.getId();
            return nextFile;
        }

        return null;
    }

    private void fetchNextPage() throws Exception {
        String folderId = extractFolderId(folderUrl);

        FileList result = drive.files().list()
                .setQ("'" + folderId + "' in parents and trashed=false")
                .setFields("files(id, name, mimeType)")
                .setPageToken(nextPageToken)
                .execute();

        fileIterator = result.getFiles().iterator();
        nextPageToken = result.getNextPageToken();

        if (lastProcessedFileId != null) {
            while (fileIterator.hasNext()) {
                File file = fileIterator.next();
                if (file.getId().equals(lastProcessedFileId)) {
                    break;
                }
            }
        }

        if (nextPageToken == null) {
            noMorePages = true;
        }
    }
}
