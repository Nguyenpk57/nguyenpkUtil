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
    private static final String FTP_LOCAL_DIR = "G:/Report Data";
    public static final String FTP_IP = "10.121.141.195";
    public static final String FTP_PORT = "22";
    public static final String FTP_USER_NAME = "cm";
    public static final String FTP_PASSWORD = "j#85hf0D2)PL";
    public static final String FTP_REMOTE_DIR = "/home/app/cm/Nguyenpk/test_ftp";
    public static final String FTP_REMOTE_FILE_PARTENT = "SMART_IVR_*\\.TXT";

    public void main(String[] args) {
        FtpConfig ftpConfig = setConfig();
        FtpUtils ftpUltil = new FtpUtils(ftpConfig);
        if (ftpUltil.connectFtpServer() < 0) {
            logger.error("connect to ftp fail");
            return;
        }
        if (ftpUltil.getFileProcess(FTP_LOCAL_DIR) < 0) {
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

    public FtpConfig setConfig() {
        FtpConfig ftpConfig = new FtpConfig();
        ftpConfig.setIp(FTP_IP);
        ftpConfig.setPort(Integer.parseInt(FTP_PORT));
        ftpConfig.setUser(FTP_USER_NAME);
        ftpConfig.setPass(FTP_PASSWORD);
        ftpConfig.setRemoteFolder(FTP_REMOTE_DIR);
        ftpConfig.setFilePartent(FTP_REMOTE_FILE_PARTENT);
        return ftpConfig;
    }

}
