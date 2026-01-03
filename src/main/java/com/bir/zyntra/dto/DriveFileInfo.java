package com.bir.zyntra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriveFileInfo {
    private String fileId;
    private String fileName;
    private long fileSize;
}
