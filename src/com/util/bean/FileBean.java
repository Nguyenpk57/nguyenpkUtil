package com.util.bean;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class FileBean {

    private Long fileId;
    private String fileName;
    private Integer fileSize;
    private String data;
    private byte[] fileData;

    public FileBean() {
    }

    public FileBean(byte[] fileData) {
        this.fileData = fileData;
        this.fileSize = this.fileData == null ? 0 : this.fileData.length;
    }

    public FileBean(String fileName, Integer fileSize, String data) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.data = data;
    }

    public FileBean(Long fileId, String fileName, Integer fileSize, String data, byte[] fileData) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.data = data;
        this.fileData = fileData;
    }

    
    public FileBean(String fileName, byte[] fileData, String contentType) {
        try {
            this.fileName = fileName;
            this.fileData = fileData;
            this.fileSize = this.fileData == null ? 0 : this.fileData.length;
            this.data = StringUtils.join(contentType, ",", String.valueOf(Base64.encodeBase64(this.fileData)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public FileBean(FileBean obj) {
        if (obj == null) {
            return;
        }
        this.fileId = obj.fileId;
        this.fileName = obj.fileName;
        this.fileSize = obj.fileSize;
        this.data = obj.data;
        this.fileData = obj.fileData;
    }

    public void copy(FileBean obj) {
        clear();
        if (obj == null) {
            return;
        }
        this.fileId = obj.fileId;
        this.fileName = obj.fileName;
        this.fileSize = obj.fileSize;
        this.data = obj.data;
        this.fileData = obj.fileData;
    }

    public void clear() {
        this.fileId = null;
        this.fileName = null;
        this.fileSize = 0;
        this.data = null;
        this.fileData = null;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
