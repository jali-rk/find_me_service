package com.bir.zyntra.service.storage;

import com.bir.zyntra.dto.DriveFileInfo;

import java.util.List;

public interface StorageService {
    List<DriveFileInfo> listFilesInFolder(String folderUrl);

    int getNumberOfFilesInFolder(String folderUrl);

//    Path downloadFile(String fileId, Long jobId, Long itemId, String originalName);
}
