package com.util.func;

import com.jcraft.jsch.*;
import com.util.bean.FtpConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * @author nguyenpk
 * @since 2022-03-07
 */
public class FtpUtils {
    private FTPClient ftpClient = null;
    private FTPFile[] lstFtpFiles = null;

    Session sessionSftp = null;
    Channel channelSftp = null;
    ChannelSftp cSftp = null;
    JSch jsch = new JSch();
    FtpConfig ftpConfig;
    public List<String> listFileDownloadSuccess = new ArrayList<String>();

    public int fileCount = 0;

    public FtpUtils(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    FTPFile[] getFtpFileList(String sDir, int timeout) {
        try {
            FTPFile[] listResult = null;
            if (!(this.ftpClient.changeWorkingDirectory("/" + sDir))) {

                return null;
            }
            this.ftpClient.setSoTimeout(timeout);
            listResult = this.ftpClient.listFiles();
            return listResult;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        return null;
    }

    public int connectFtpServer() {
        System.out.println("begin connect to ftp");
        boolean isError = false;

        if ((this.sessionSftp == null) || (!(this.sessionSftp.isConnected()))) {
            try {
                this.sessionSftp = this.jsch.getSession(ftpConfig.getUser(), ftpConfig.getIp(), ftpConfig.getPort());

                this.sessionSftp.setPassword(ftpConfig.getPass());

                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                this.sessionSftp.setConfig(config);
                this.sessionSftp.connect();

                this.channelSftp = this.sessionSftp.openChannel("sftp");
                this.channelSftp.connect();
                this.cSftp = ((ChannelSftp) this.channelSftp);
            } catch (Exception ex) {
                ex.printStackTrace();
                isError = true;
                clearFtpConnect();

                return -1;
            }
        }
        return 0;
    }

    public int clearFtpConnect() {
        try {
            if (this.sessionSftp != null) {
                if (this.sessionSftp.isConnected()) {
                    try {
                        this.sessionSftp.disconnect();
                    } catch (Exception ex) {
                    }
                }
                this.sessionSftp = null;
            }
            return 0;
        } catch (Exception ex) {
        }
        return -1;
    }

    private int createFolder(String strFolder) {
        try {
            File inFolder = new File(strFolder);
            if (!(inFolder.exists())) {
                String parentName = inFolder.getParent();
                if (parentName == null) {
                    inFolder.mkdir();
                } else {
                    File foldertomake = new File(parentName);
                    if (foldertomake.exists()) {
                        inFolder.mkdir();
                    } else {
                        if (createFolder(parentName) == -1) {
                            return -1;
                        }
                        if (createFolder(strFolder) == -1) {
                            return -1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public int getFileProcess(String localDir) {
        createFolder(localDir);
        try {
            Vector files = this.cSftp.ls(ftpConfig.getRemoteFolder());

            this.fileCount = 0;

            if (files.isEmpty()) {
                return 0;
            }

            for (int i = 0; i < files.size(); ++i) {

                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) files.get(i);
                if (!(checkFtpFile(lsEntry))) {
                    System.out.println("[Warning] No demanding file: " + lsEntry.getFilename());
                } else {
                    this.fileCount += 1;

                    if (!(this.cSftp.isConnected())) {
                        System.out.println("[Error - getFileProcess]: The connection was droped");
                        break;
                    }

                    if (getOneFile(lsEntry, localDir) < 0) {
                        return -1;
                    }

                    if (ftpBackup(lsEntry, localDir)) {
                        File tempFile = new File(localDir + "/TMP_" + lsEntry.getFilename() + "_bak");

                        if (localRename(tempFile, localDir)) {
                            System.out.println("[Infor] rename success : " + lsEntry.getFilename());
                            listFileDownloadSuccess.add(localDir + "/" + lsEntry.getFilename());
                        } else {
                            System.out.println("[Warning] Add to black list : " + lsEntry.getFilename());
                        }

                    }

                }
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("[Error - getFileProcess]: ");
        }
        return -1;
    }

    boolean checkFtpFile(ChannelSftp.LsEntry lsEntry) {
        try {
            return (checkFileName(lsEntry.getFilename()) >= 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int checkFileName(String fileName) {
        if (fileName == null) {
            return -1;
        }
        if (compareFileNameWildCard(fileName, ftpConfig.getFilePartent())) {
            return 0;
        }
        return -1;
    }

    int getOneFile(ChannelSftp.LsEntry lsEntry, String localDir) {
        try {
            String tempFileName = localDir + "/TMP_" + lsEntry.getFilename() + "_bak";
            try {
                this.cSftp.get(ftpConfig.getRemoteFolder() + lsEntry.getFilename(), tempFileName);
            } catch (SftpException sftpEx) {
                System.out.println("[Error]:Can not download file " + lsEntry.getFilename());
                File f = new File(tempFileName);
                if (f.exists()) {
                    f.delete();
                }
                return -1;
            }

            System.out.println("Complete getting file: " + lsEntry.getFilename());

            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    boolean ftpBackup(ChannelSftp.LsEntry lsEntry, String localDir) {
        try {
            boolean success = false;
            try {
                this.cSftp.cd(ftpConfig.getRemoteFolder());
            } catch (SftpException sftpEx) {
                System.out.println("[Error] - ftpBackup: Not found directory " + ftpConfig.getRemoteFolder() + " in Ftp Server");
                return false;
            }

            int count = 0;
            if (!(success)) {
                try {
                    this.cSftp.rename(ftpConfig.getRemoteFolder() + lsEntry.getFilename(), ftpConfig.getRemoteFolder() + "tmp/" + lsEntry.getFilename());
                    success = true;
                } catch (Exception e) {
                    System.out.println("[1] Cannot process backup file at server. Ex: " + e.toString());
                    try {
                        this.cSftp.rm(lsEntry.getFilename());
                        success = true;
                    } catch (Exception ex) {
                        do {
                            System.out.println("[2] Cannot process backup file at server. Ex: " + e.toString());
                            if (e.getMessage().startsWith("syserr: File exists")) {
                            }
                            File getFile = new File(localDir + "/" + lsEntry.getFilename() + "_temp");
                            getFile = new File(localDir + "/" + lsEntry.getFilename() + "_temp");
                            if (getFile.exists()) {
                                getFile.delete();
                            }

                            ++count;
                        } while (count <= 5);
                    }
                }
            }
            return success;
        } catch (Exception ex) {
            System.out.println("[Error - ftpBackup]: " + ex.toString());
        }
        return false;
    }

    boolean localRename(File file, String localDir) {
        try {
            boolean success = false;
            int count = 0;

            String fileName = file.getName().trim();

            fileName = fileName.substring(0, fileName.length() - 4);
            fileName = fileName.substring(4, fileName.length());

            do {
                if (success) {
                    break;
                }
                success = file.renameTo(new File(localDir, fileName));
                ++count;
            } while (count <= 10);

            return success;
        } catch (Exception ex) {
            System.out.println("[Error - localRename]: " + ex.toString());
        }
        return false;
    }

    public static Boolean compareFileNameWildCard(String fileName, String filePattern) {
        fileName = fileName.replace("\\", "/");
        filePattern = filePattern.replace("//", "/");
        Pattern p = Pattern.compile(filePattern.replace("*", ".*"));
        return p.matcher(fileName).matches();
    }
}
