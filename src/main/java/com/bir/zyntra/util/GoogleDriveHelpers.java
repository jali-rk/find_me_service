package com.bir.zyntra.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleDriveHelpers {

    private static final Pattern FOLDER_ID_PATTERN = Pattern.compile("[-\\w]{25,}");

    public static String extractFolderId(String url) {
        Matcher matcher = FOLDER_ID_PATTERN.matcher(url);
        return matcher.find() ? matcher.group() : null;
    }
}
