package org.springframework.batch.admin.web.util;

public class WebUtils {

    public static String extractFilenameFromUrlPath(String urlPath) {
        String filename = extractFullFilenameFromUrlPath(urlPath);
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex != -1) {
            filename = filename.substring(0, dotIndex);
        }
        return filename;
    }

    public static String extractFullFilenameFromUrlPath(String urlPath) {
        int end = urlPath.indexOf(';');
        if (end == -1) {
            end = urlPath.indexOf('?');
            if (end == -1) {
                end = urlPath.length();
            }
        }
        int begin = urlPath.lastIndexOf('/', end) + 1;
        return urlPath.substring(begin, end);
    }

}
