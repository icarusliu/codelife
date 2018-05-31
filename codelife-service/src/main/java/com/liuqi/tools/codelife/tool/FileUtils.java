package com.liuqi.tools.codelife.tool;

import com.liuqi.tools.codelife.exceptions.ErrorCodes;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件操作类
 *
 * @Author: LiuQI
 * @Created: 2018/3/27 14:31
 * @Version: V1.0
 **/
public final class FileUtils {
    /**
     * 将上传的文件写到文件系统中，并返回存储的文件名称
     * @param file
     * @param filePath
     * @return
     */
    public static final String saveToFile(MultipartFile file, String filePath) throws RestException {
        UUID uuid = UUID.randomUUID();
        String oFileName = file.getOriginalFilename();
        String fileName  = uuid + oFileName.substring(oFileName.lastIndexOf("."));
        
        //获取项目部署路径
        String prjPath = System.getProperty("user.dir");
        
        if (!filePath.startsWith(PATH_SPLIT)) {
            prjPath += PATH_SPLIT;
        }
        
        //如果文件夹不存在则先创建文件夹
        File dir = new File(prjPath + filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    
        //如果配置的文件路径最后不包含/，则添加上
        if (!filePath.endsWith(PATH_SPLIT)) {
            filePath = filePath + PATH_SPLIT;
        }
        
        File destFile = new File(prjPath + filePath + fileName);
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                logger.error("Create file failed!", e);
                throw ExceptionTool.getException(ErrorCodes.COMM_FILE_CREATE_FAILED);
            }
        }
    
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            logger.error("Save file failed!", e);
            throw ExceptionTool.getException(ErrorCodes.COMM_FILE_SAVE_FAILED);
        }
    
        return fileName;
    }
    
    public static final void outputFileContent(String filePath, String fileName,
                                     OutputStream outputStream) throws RestException {
        String prjPath = System.getProperty("user.dir");
    
        if (!filePath.startsWith(PATH_SPLIT)) {
            prjPath += PATH_SPLIT;
        }
    
        //如果配置的文件路径最后不包含/，则添加上
        if (!filePath.endsWith(PATH_SPLIT)) {
            filePath = filePath + PATH_SPLIT;
        }
        
        File file = new File(prjPath + filePath + fileName);
    
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("File does not exist, file: " + file.getName());
            throw ExceptionTool.getException(ErrorCodes.COMM_FILE_NOT_EXISTS);
        }
        
        byte[] buffer = new byte[1000];
        
        try {
            while (-1 != inputStream.read(buffer)) {
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            logger.error("Read file failed!", e);
            throw ExceptionTool.getException(e, ErrorCodes.COMM_FILE_READ_FAILED);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("Close input stream failed!", e);
            }
        }
    }
    
    /**
     * 删除文件
     *
     * @param filePath
     * @param fileName
     */
    public static final void deleteFile(String filePath, String fileName) {
        //检测路径是否以/结尾
        if (!filePath.endsWith(PATH_SPLIT)) {
            filePath = filePath + PATH_SPLIT;
        }
        
        File file = new File(filePath + fileName);
        file.delete();
    }
    
    /**
     * 将HTML的文章内容保存到文件中并返回保存的文件名称
     * 文件名为随机生成的文件名称
     *
     * @param content 文件内容
     * @param filePath 文件路径
     * @return
     */
    public static final String saveToFile(String content, String filePath) throws RestException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + ".html";
        
        //检查文件路径是否存在
        saveToFile(content, filePath, fileName);
        
        return fileName;
    }
    
    /**
     * 将文件内容保存到指定的文件中去
     *
     * @param content 文章内容
     * @param filePath 存储路径
     * @param fileName 文件名称
     * @throws RestException 当写文件失败时抛出异常
     */
    public static void saveToFile(String content, String filePath, String fileName) throws RestException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        //如果配置的文件路径最后不包含/，则添加上
        if (!filePath.endsWith(PATH_SPLIT)) {
            filePath = filePath + PATH_SPLIT;
        }
        
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(filePath + fileName), StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            logger.error("Write file failed!", e);
            throw ExceptionTool.getException(e, ErrorCodes.COMM_FILE_WRITE_FAILED);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("Close fileWriter failed!", e);
                }
            }
        }
    }
    
    /**
     * 读取文件内容并返回
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return 返回文件内容
     */
    public static String getFileContent(String fileName, String filePath) throws RestException {
        if (!filePath.endsWith(PATH_SPLIT)) {
            filePath = filePath + PATH_SPLIT;
        }
        
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(filePath + fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("File does not exist!", e);
            throw ExceptionTool.getException(e, ErrorCodes.ARTICLE_CONTENT_NOT_EXISTS);
        }
        
        try {
            return reader.lines().reduce((s1, s2) -> s1 + "\n" + s2).orElse("");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("Close reader failed!", e);
            }
        }
    }
    
    
    /**
     * HTML串转换成字符串，去掉HTML相关标签；
     *
     * @param html
     * @return
     */
    public static final String htmlToText(String html) {
        String result = "";
        //第一步先去掉HTML相关标签
        Matcher matcher = HTML_PATTERN.matcher(html);
        result = matcher.replaceAll("");
        
        //第二步去掉回车换行等字符
        return SPACE_PATTERN.matcher(result).replaceAll("");
    }

    /**
     * 定义HTML标签的正则表达式
     */
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);

    /**
     * 定义空格回车换行符
     */
    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s*|\t|\r|\n", Pattern.CASE_INSENSITIVE);

    private static final String PATH_SPLIT = "/";

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
}
