package main;

import com.util.bean.FtpConfig;
import com.util.func.FtpUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author nguyenpk
 * @since 2022-03-07
 */
public class FtpTest {
    private ILogger logger = LoggerImpl.getInstance(this.getClass());
    private static final String LOCAL_DIR = "G:/Report Data";

    public void main(String[] args) {
        FtpConfig ftpConfig = new FtpConfig();
        FtpUtils ftpUltil = new FtpUtils(ftpConfig);
        if (ftpUltil.connectFtpServer() < 0) {
            logger.error("connect to ftp fail");
            return;
        }
        if (ftpUltil.getFileProcess(LOCAL_DIR) < 0) {
            logger.error("Process download file fail");
            return;
        }
        if (ftpUltil.clearFtpConnect() < 0) {
            logger.error("Clear ftp connection error");
        }
        if (ftpUltil.listFileDownloadSuccess.size() > 0) {
            try {
                for (String filePath : ftpUltil.listFileDownloadSuccess) {
                    try {
                        Path path = Paths.get(filePath);
                        BufferedReader reader = Files.newBufferedReader(path);
                        String line = null;
                        int i = 0;
                        while ((line = reader.readLine()) != null) {
                            logger.info("Info " + "| line | " + i + " | " + " data " + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                logger.info("Finished.");
            }
        }
    }
}
