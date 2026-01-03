package com.bir.zyntra.service.storage.gdrive;

import com.bir.zyntra.dto.DriveFileInfo;
import com.bir.zyntra.service.storage.StorageService;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bir.zyntra.util.GoogleDriveHelpers.extractFolderId;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService implements StorageService {

    private final Drive drive;

    @Override
    public List<DriveFileInfo> listFilesInFolder(String folderUrl) {
        String folderId = extractFolderId(folderUrl);
        if (folderId == null) throw new RuntimeException("Invalid folder URL");

        List<DriveFileInfo> results = new ArrayList<>();
        try {
            String pageToken = null;
            do {
                FileList list = drive.files().list()
                        .setQ("'" + folderId + "' in parents and trashed=false")
                        .setFields("nextPageToken, files(id, name, size)")
                        .setPageToken(pageToken)
                        .execute();

                for (File f : list.getFiles()) {
                    results.add(new DriveFileInfo(
                            f.getId(),
                            f.getName(),
                            f.getSize() == null ? 0 : f.getSize()
                    ));
                }
                pageToken = list.getNextPageToken();
            } while (pageToken != null);
        } catch (Exception e) {
            log.error("Failed to list folder", e);
            throw new RuntimeException(e);
        }

        return results;
    }

    @Override
    public int getNumberOfFilesInFolder(String folderUrl) {
        return listFilesInFolder(folderUrl).size();
    }

//    @Override
//    public Path downloadFile(String fileId, Long jobId, Long itemId, String originalName) {
//        try {
//            Path dir = Paths.get("storage/job-" + jobId);
//            Files.createDirectories(dir);
//            Path output = dir.resolve(itemId + "-" + originalName);
//
//            try (OutputStream os = Files.newOutputStream(output)) {
//                drive.files().get(fileId).executeMediaAndDownloadTo(os);
//            }
//
//            return output;
//
//        } catch (Exception e) {
//            log.error("Download failed for fileId={}", fileId, e);
//            throw new RuntimeException(e);
//        }
//    }

}
