package com.ll.nbe342team8.global.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class TempImageFileCleanupTask {

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void cleanupTempFiles() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        cleanDirectory(tempDir, System.currentTimeMillis() - 86400000); // 24시간 이상 된 파일/디렉토리 삭제
    }

    private void cleanDirectory(File directory, long cutoffTime) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    cleanDirectory(file, cutoffTime);
                    if (file.lastModified() < cutoffTime && file.list().length == 0) {
                        file.delete(); // 빈 디렉토리 삭제
                    }
                } else {
                    if (file.lastModified() < cutoffTime) {
                        file.delete();
                    }
                }
            }
        }
    }
}
