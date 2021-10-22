/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import com.util.bean.FileBean;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import com.util.result.Result;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class FileUtils {
    
    private static final Object LOCK = new Object();
    private static FileUtils instance;
    private static HashMap<String, FileUtils> instances;
    private ILogger logger;

    private FileUtils() {
        initialization();
    }

    private FileUtils(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    public static FileUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new FileUtils();
            }
            return instance;
        }
    }

    public static FileUtils getInstance(ILogger logger) {
        if (logger == null) {
            return getInstance();
        }
        String name = logger.getName();
        if (name == null) {
            return getInstance();
        }
        name = name.trim();
        if (name.isEmpty()) {
            return getInstance();
        }
        synchronized (LOCK) {
            if (instances == null) {
                instances = new HashMap<String, FileUtils>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new FileUtils(logger));
            }
            return instances.get(name);
        }
    }

    private void initialization() {
        if (logger == null) {
            logger = LoggerImpl.getInstance(this.getClass());
        }
    }

    public void createFolder(String path) {
        File folder = new File(path);
        if (folder.exists()) {
            return;
        }
        folder.mkdirs();
    }

    public String catchPath(String path, Date runTime, String pattern, String separator) {
        return catchPath(path, DateTimeUtil.getInstance().format(runTime, pattern), separator);
    }

    public String catchPath(String path, String subPath, String separator) {
        path = toPath(path, separator);
        if (subPath.startsWith(separator)) {
            subPath = subPath.substring(1);
        }
        return StringUtils.defaultString(path) + subPath;
    }

    public String toPath(String path, String separator) {
        if (StringUtils.isEmpty(path)) {
            return path;
        }
        path = replaceSeparator(path, separator);
        if (!path.endsWith(separator)) {
            path += separator;
        }
        return path;
    }

    public String replaceSeparator(String path, String separator) {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(separator)) {
            return path;
        }

        try {
            path = path.replaceAll("\\\\", separator);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        try {
            path = path.replaceAll("/", separator);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return path;
    }

    public String catchPath(String path, String separator) {
        int index = replaceSeparator(path, separator).lastIndexOf(separator);
        if (index < 0) {
            return path;
        }
        return path.substring(0, index + 1);
    }

    public String catchName(String path, String separator) {
        int index = replaceSeparator(path, separator).lastIndexOf(separator);
        if (index < 0) {
            return "";
        }
        return path.substring(index + 1);
    }

    public String catchName(String path) {
        int index = path.lastIndexOf(".");
        if (index < 0) {
            return "";
        }
        return path.substring(0, index);
    }

    public String catchExtension(String path) {
        int index = path.lastIndexOf(".");
        if (index < 0) {
            return "";
        }
        return path.substring(index + 1);
    }

    public boolean isFile(String path) {
        File file = new File(path);
        return file.isFile() || !StringUtils.isEmpty(catchExtension(path));
    }

    public boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory() || StringUtils.isEmpty(catchExtension(path));
    }

    //<editor-fold defaultstate="collapsed" desc="read">
    public byte[] readBytes(String filePath) throws FileNotFoundException {
        byte[] result = null;
        FileInputStream is = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return result;
            }
            int fileSize = (int) file.length();
            if (file.length() == 0) {
                return result;
            }
            is = new FileInputStream(file);
            result = new byte[fileSize];
            is.read(result, 0, fileSize);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            closes(is);
        }
        return result;
    }

    public List<String> read(String filePath) {
        List<String> result = new ArrayList();
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(filePath));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            if (br == null) {
                br = new BufferedReader(new FileReader(new File(filePath).getCanonicalPath()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    result.add(line);
                }
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            closes(br);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="write">
    public void write(String filePath, List<String> lines, String separator) {
        FileWriter writer = null;
        try {
            if (lines != null) {
                createFolder(catchPath(filePath, separator));
                writer = new FileWriter(filePath);
                String joiner = "\n";
                write(writer, lines, joiner);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            closes(writer);
        }
    }

    private void write(FileWriter writer, List<String> lines, String joiner) throws IOException {
        if (lines == null) {
            return;
        }
        int size = lines.size();
        if (size == 0) {
            return;
        }
        int i = 0;
        while (i < size - 1) {
            String line = lines.get(i++);
            write(writer, line, joiner);
        }
        String line = lines.get(i);
        write(writer, line);
    }

    private void write(FileWriter writer, String line, String joiner) throws IOException {
        if (line != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                writer.write(line);
                writer.write(joiner);
            }
        }
    }

    private void write(FileWriter writer, String line) throws IOException {
        if (line != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                writer.write(line);
            }
        }
    }

    public void write(String path, byte[] data) throws Exception {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(path));
            os.write(data);
        } catch (Exception ex) {
            throw ex;
        } finally {
            closes(os);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="copy">
    public Result copy(String sourcePath, String targetPath, String separator, String language) {
        Result result = new Result(true, "R0000", "R0000");
        boolean folder = false;
        try {
            //<editor-fold defaultstate="collapsed" desc="trim">
            sourcePath = replaceSeparator(sourcePath, separator);
            targetPath = replaceSeparator(targetPath, separator);
            //</editor-fold>
            File source = new File(sourcePath);
            if (!source.exists()) {
                result = new Result(false, "E0020", "E0020", "source");
                return result;
            }
            if (source.isFile()) {
                String fileName = catchName(sourcePath, separator);
                if (isDirectory(targetPath)) {
                    createFolder(targetPath);
                    targetPath = catchPath(targetPath, fileName, separator);
                    copy(source, new File(targetPath));
                    return result;
                }
                createFolder(catchPath(targetPath, separator));
                copy(source, new File(targetPath));
                return result;
            }
            folder = true;
            if (isDirectory(targetPath)) {
                createFolder(targetPath);
                File[] files = source.listFiles();
                if (files == null || files.length == 0) {
                    result.put("empty", true);
                    return result;
                }
                List<Result> results = new ArrayList<Result>();
                for (File file : files) {
                    if (file == null) {
                        continue;
                    }
                    Result one = copy(sourcePath, file, targetPath, separator, language);
                    results.add(one);
                }
                result = toResult(results);
                return result;
            }
            result = new Result(false, "E0009", "E0009", "target");
        } catch (Exception ex) {
            result = new Result(false, "E0000", "E0000", ex, ex.getMessage());
        } finally {
            result.put("folder", folder);
            result.setMessage(result.getMessage(language));
            logger.info("FileUtils.copy:[" + sourcePath + "] to [" + targetPath + "];result=" + GsonUtil.getInstance(logger).to(result));
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="toResult">
    private Result toResult(List<Result> results) {
        return toResult(results, "results");
    }

    private Result toResult(List<Result> results, String resultsKey) {
        Result result = new Result(true, "R0000", "R0000");
        if (results == null || results.isEmpty()) {
            return result;
        }
        //<editor-fold defaultstate="collapsed" desc="build">
        int total = results.size();
        int success = 0;
        int i = 0;
        while (i < total) {
            Result r = results.get(i);
            if (r == null) {
                results.remove(i);
                total--;
                continue;
            }
            if (r.isSuccess()) {
                success++;
            }
            i++;
        }
        //</editor-fold>
        if (total == 0) {
            return result;
        }
        if (total == 1) {
            result = results.get(0);
            return result;
        }
        if (resultsKey == null || resultsKey.isEmpty()) {
            resultsKey = "results";
        }
        if (success == 0) {
            result = new Result(false, "R0001", "R0001");
            result.put("success", success);
            result.put("total", total);
            result.put(resultsKey, results);
            return result;
        }
        if (success == total) {
            result.put("success", success);
            result.put("total", total);
            result.put(resultsKey, results);
            return result;
        }
        result = new Result(true, "R0002", "R0002", success, total);
        result.put("success", success);
        result.put("total", total);
        result.put(resultsKey, results);
        return result;
    }
    //</editor-fold>

    private Result copy(String sourcePath, File child, String targetPath, String separator, String language) throws Exception {
        Result result = new Result(true, "R0000", "R0000");
        boolean folder = false;
        try {
            if (child.isFile()) {
                String fileName = child.getName();
                targetPath = catchPath(targetPath, fileName, separator);
                createFolder(catchPath(targetPath, separator));
                copy(child, new File(targetPath));
                return result;
            }
            folder = true;
            String name = child.getName();
            sourcePath = catchPath(sourcePath, name, separator);
            targetPath = catchPath(targetPath, name, separator);
            createFolder(targetPath);
            File[] files = child.listFiles();
            if (files == null || files.length == 0) {
                result.put("empty", true);
                return result;
            }
            List<Result> results = new ArrayList<Result>();
            for (File file : files) {
                if (file == null) {
                    continue;
                }
                Result one = copy(sourcePath, file, targetPath, separator, language);
                results.add(one);
            }
            result = toResult(results);
        } catch (Exception ex) {
            result = new Result(false, "E0000", "E0000", ex, ex.getMessage());
        } finally {
            result.put("folder", folder);
            result.setMessage(result.getMessage(language));
            result.put("sourcePath", sourcePath);
            result.put("child", (child == null ? null : child.getName()));
            result.put("targetPath", targetPath);
            logger.info("FileUtils.copyOne:result=" + GsonUtil.getInstance(logger).to(result));
        }
        return result;
    }

    private void copy(File source, File target) throws Exception {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            closes(is, os);
        }
    }

    public void copy(InputStream is, OutputStream os) throws Exception {
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            closes(is, os);
        }
    }

    public void closes(Object... objs) {
        if (objs != null) {
            for (Object obj : objs) {
                close(obj);
            }
        }
    }

    private Object close(Object obj) {
        try {
            if (obj == null) {
                return obj;
            }
            if (obj instanceof FileInputStream) {
                ((FileInputStream) obj).close();
            }
            if (obj instanceof InputStream) {
                ((InputStream) obj).close();
            }
            if (obj instanceof OutputStream) {
                ((OutputStream) obj).close();
            }
            if (obj instanceof ByteArrayOutputStream) {
                ((ByteArrayOutputStream) obj).close();
            }
            if (obj instanceof BufferedReader) {
                ((BufferedReader) obj).close();
            }
            if (obj instanceof FileWriter) {
                ((FileWriter) obj).close();
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            obj = null;
        }
        return obj;
    }
    //</editor-fold>

    public String getExecutionPath() {
        return System.getProperty("user.dir");
    }

    public List<String> catchAllFiles(List<String> paths, String separator) {
        List<String> result = new ArrayList<String>();
        if (paths == null || paths.isEmpty()) {
            return result;
        }
        List<String> values = new ArrayList<String>();
        for (String path : paths) {
            path = StringUtils.trim(path);
            if (StringUtils.isEmpty(path)) {
                continue;
            }
            path = FileUtils.getInstance(logger).replaceSeparator(path, separator);
            if (StringUtils.isEmpty(path)) {
                continue;
            }
            if (values.contains(path)) {
                continue;
            }
            values.add(path);
            List<String> files = catchAllFiles(path, separator);
            if (files != null) {
                result.addAll(files);
            }
        }
        return result;
    }

    private List<String> catchAllFiles(String path, String separator) {
        List<String> result = new ArrayList<String>();
        path = StringUtils.trim(path);
        if (StringUtils.isEmpty(path)) {
            return result;
        }
        path = replaceSeparator(path, separator);
        if (StringUtils.isEmpty(path)) {
            return result;
        }
        File file = new File(path);
        if (!file.exists()) {
            return result;
        }
        if (file.isFile()) {
            result.add(path);
            return result;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return result;
        }
        for (File child : files) {
            if (child == null) {
                continue;
            }
            List<String> children = catchAllFiles(child.getPath(), separator);
            if (children == null || children.isEmpty()) {
                continue;
            }
            result.addAll(children);
        }
        return result;
    }

    public Result download(String fileName, String filePath) {
        Result result = new Result(true, "R0000", "R0000");
        ByteArrayOutputStream out = null;
        FileInputStream in = null;
        FileBean fileBean = null;
        try {
            fileBean = new FileBean();
            out = new ByteArrayOutputStream();
            File file = new File(filePath + fileName);
            in = new FileInputStream(file);
            byte[] bytesIn = new byte[1024];
            int i;
            while ((i = in.read(bytesIn)) >= 0) {
                out.write(bytesIn, 0, i);
            }
            fileBean.setFileName(fileName);
            fileBean.setFileData(out.toByteArray());
        } catch (Throwable ex) {
            result = new Result(false, "E0000", "E0000", ex, ex.getMessage());
        } finally {
            FileUtils.getInstance(logger).closes(in, out);
            result.put("fileBean", fileBean);
        }
        return result;
    }

    public void delete(String filePath) throws Exception {
        try {
            File tempFile = new File(filePath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    
    public static int getBase64StringToFile(String filePath, String content) {
        try {
            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(content.getBytes());
            FileOutputStream stream = new FileOutputStream(filePath);
            try {
                stream.write(bytes);
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public static String fileToString(String filePath) throws IOException {
        String base64File = null;
        File file = new File(filePath);
        FileInputStream imageInFile = new FileInputStream(file);
        try {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = new BASE64Encoder().encode(fileData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }
        return base64File;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static int deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.delete()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
