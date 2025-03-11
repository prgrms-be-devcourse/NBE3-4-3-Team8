package com.ll.nbe342team8.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class Ut {

    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = new ObjectMapper();

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }


    public static class file {



        public static void downloadByHttp(String url, String dirPath) {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                // 먼저 헤더만 가져오기 위한 HEAD 요청
                HttpResponse<Void> headResponse = client.send(
                        HttpRequest.newBuilder(URI.create(url))
                                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                                .build(),
                        HttpResponse.BodyHandlers.discarding()
                );

                // 실제 파일 다운로드
                HttpResponse<Path> response = client.send(request,
                        HttpResponse.BodyHandlers.ofFile(
                                createTargetPath(url, dirPath, headResponse)
                        ));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("다운로드 중 오류 발생: " + e.getMessage(), e);
            }
        }

        private static Path createTargetPath(String url, String dirPath, HttpResponse<?> response) {
            // 디렉토리가 없으면 생성
            Path directory = Path.of(dirPath);
            if (!Files.exists(directory)) {
                try {
                    Files.createDirectories(directory);
                } catch (IOException e) {
                    throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
                }
            }

            // 파일명 생성
            String filename = getFilenameFromUrl(url);
            String extension = getExtensionFromResponse(response);

            return directory.resolve(filename + extension);
        }

        private static String getFilenameFromUrl(String url) {
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

        private static String getExtensionFromResponse(HttpResponse<?> response) {
            return response.headers()
                    .firstValue("Content-Type")
                    .map(contentType -> {
                        // MIME 타입에 따른 확장자 매핑
                        return switch (contentType.split(";")[0].trim().toLowerCase()) {
                            case "application/json" -> ".json";
                            case "text/plain" -> ".txt";
                            case "text/html" -> ".html";
                            case "image/jpeg" -> ".jpg";
                            case "image/png" -> ".png";
                            case "application/pdf" -> ".pdf";
                            case "application/xml" -> ".xml";
                            case "application/zip" -> ".zip";
                            default -> "";
                        };
                    })
                    .orElse("");
        }


    }

    public class cmd {
        public static void runAsync(String cmd) {
            new Thread(() -> {
                run(cmd);
            }).start();
        }

        //        public static void run(String cmd) {
//            try {
//                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", cmd);
//                Process process = processBuilder.start();
//                process.waitFor(1, TimeUnit.MINUTES);
//                System.out.println("정상@@@".repeat(200));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        public static void run(String cmd) {
            try {
                ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
                pb.redirectErrorStream(true); // 에러 출력 리다이렉트

                Process process = pb.start();

                // 출력 내용 읽기
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())
                );
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[CMD] " + line); // 로그 추가
                }

                int exitCode = process.waitFor();
                System.out.println("Exit Code: " + exitCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class date {
        public static String getCurrentDateFormatted(String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(new Date());
        }

        public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
            return localDateTime.atZone(zoneId).toInstant();
        }

        // 기본 시간대를 사용하는 오버로드 메서드
        public static Instant toInstant(LocalDateTime localDateTime) {
            return toInstant(localDateTime, ZoneId.systemDefault());
        }

    }
}
