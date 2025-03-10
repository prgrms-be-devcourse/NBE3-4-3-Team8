package com.ll.nbe342team8.standard.util.fileuploadutil;

import com.ll.nbe342team8.global.config.AppConfig;
import com.ll.nbe342team8.standard.util.Ut;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


public class FileUploadUtil {

    private static final String ORIGINAL_FILE_NAME_SEPARATOR = "--originalFileName_";

    private static final Map<String, String> MIME_TYPE_MAP = new LinkedHashMap<>() {{
        put("application/json", "json");
        put("text/plain", "txt");
        put("text/html", "html");
        put("text/css", "css");
        put("application/javascript", "js");
        put("image/jpeg", "jpg");
        put("image/png", "png");
        put("image/gif", "gif");
        put("image/webp", "webp");
        put("image/svg+xml", "svg");
        put("application/pdf", "pdf");
        put("application/xml", "xml");
        put("application/zip", "zip");
        put("application/gzip", "gz");
        put("application/x-tar", "tar");
        put("application/x-7z-compressed", "7z");
        put("application/vnd.rar", "rar");
        put("audio/mpeg", "mp3");
        put("audio/wav", "wav");
        put("video/mp4", "mp4");
        put("video/webm", "webm");
        put("video/x-msvideo", "avi");
    }};

    @SneakyThrows
    private static void mkdir(String dirPath) {
        Path path = Path.of(dirPath);

        if (Files.exists(path)) return;

        Files.createDirectories(path);
    }

    @SneakyThrows
    public static void mv(String oldFilePath, String newFilePath) {

        mkdir(Paths.get(newFilePath).getParent().toString());

        Files.move(
                Path.of(oldFilePath),
                Path.of(newFilePath),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private static String getExtensionFromResponse(HttpResponse<?> response) {
        return response.headers()
                .firstValue("Content-Type")
                .map(contentType -> MIME_TYPE_MAP.getOrDefault(contentType, "tmp"))
                .orElse("tmp");
    }

    public static String getExtensionByTika(String filePath) {
        String mineType = AppConfig.getTika().detect(filePath);

        return MIME_TYPE_MAP.getOrDefault(mineType, "tmp");
    }

    private static String getFilenameWithoutExtFromUrl(String url) {
        try {
            String path = new URI(url).getPath();
            String filename = Path.of(path).getFileName().toString();
            // 확장자 제거
            return filename.contains(".")
                    ? filename.substring(0, filename.lastIndexOf('.'))
                    : filename;
        } catch (URISyntaxException e) {
            // URL에서 파일명을 추출할 수 없는 경우 타임스탬프 사용
            return "download_" + System.currentTimeMillis();
        }
    }

    @SneakyThrows
    public static String downloadByHttp(String url, String dirPath) {
        return downloadByHttp(url, dirPath, true);
    }

    @SneakyThrows
    public static String downloadByHttp(String url, String dirPath, boolean uniqueFilename) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
         HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            String tempFilePath = dirPath + "/" + UUID.randomUUID() + ".tmp";

            mkdir(dirPath);

            // 실제 파일 다운로드
            HttpResponse<Path> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofFile(Path.of(tempFilePath))
            );

            // 파일 확장자 추출
            String extension = getExtensionFromResponse(response);

            if (extension.equals("tmp")) {
                extension = getExtensionByTika(tempFilePath);
            }

            // 파일명 추출

            String filename = getFilenameWithoutExtFromUrl(url);
            filename = uniqueFilename
                    ? UUID.randomUUID() + ORIGINAL_FILE_NAME_SEPARATOR + filename
                    : filename;

            String newFilePath = dirPath + "/" + filename + "." + extension;

            mv(tempFilePath, newFilePath);

            return newFilePath;
        }

    public static String getOriginalFileName(String filePath) {
        return Path.of(filePath).getFileName().toString();
    }
    public static String getFileExt(String filePath) {
        String filename = getOriginalFileName(filePath);
        return filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.') + 1)
                : "";
    }

    @SneakyThrows
    public static int getFileSize(String filePath) {
        return (int) Files.size(Path.of(filePath));
    }

    @SneakyThrows
    public static void rm(String filePath) {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) return;

        if (Files.isRegularFile(path)) {
            // 파일이면 바로 삭제
            Files.delete(path);
        } else {
            // 디렉터리면 내부 파일들 삭제 후 디렉터리 삭제
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static String getFileExtTypeCodeFromFileExt(String ext) {
        return switch (ext) {
            case "jpeg", "jpg", "gif", "png", "svg", "webp" -> "img";
            case "mp4", "avi", "mov" -> "video";
            case "mp3" -> "audio";
            default -> "etc";
        };
    }
    public static String getFileExtType2CodeFromFileExt(String ext) {
        return switch (ext) {
            case "jpeg", "jpg" -> "jpg";
            default -> ext;
        };
    }

    public static Map<String, Object> getMetadata(String filePath) {
        String ext = getFileExt(filePath);
        String fileExtTypeCode = getFileExtTypeCodeFromFileExt(ext);
        if (fileExtTypeCode.equals("img")) return getImgMetadata(filePath);
        return Map.of();
    }
    private static Map<String, Object> getImgMetadata(String filePath) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        try (ImageInputStream input = ImageIO.createImageInputStream(new File(filePath))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IOException("지원되지 않는 이미지 형식: " + filePath);
            }
            ImageReader reader = readers.next();
            reader.setInput(input);
            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            metadata.put("width", width);
            metadata.put("height", height);
            reader.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    @SneakyThrows
    public static String toFile(MultipartFile multipartFile, String dirPath) {
        if (multipartFile == null) return "";
        if (multipartFile.isEmpty()) return "";

        String filePath = dirPath + "/" + UUID.randomUUID() + ORIGINAL_FILE_NAME_SEPARATOR + multipartFile.getOriginalFilename();

        FileUploadUtil.mkdir(dirPath);
        multipartFile.transferTo(new File(filePath));

        return filePath;
    }
}
