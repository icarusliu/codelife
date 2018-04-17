package com.liuqi.tools.codelife.util;

import com.liuqi.tools.codelife.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        
        if (!filePath.startsWith("/")) {
            prjPath += "/";
        }
        
        //如果文件夹不存在则先创建文件夹
        File dir = new File(prjPath + filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    
        //如果配置的文件路径最后不包含/，则添加上
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        
        File destFile = new File(prjPath + filePath + fileName);
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                logger.error("Create file failed!", e);
                throw new RestException("创建文件失败，请联系管理员！");
            }
        }
    
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            logger.error("Save file failed!", e);
            throw new RestException("保存文件失败，请联系管理员！", e);
        }
    
        return fileName;
    }
    
    public static final void outputFileContent(String filePath, String fileName,
                                     OutputStream outputStream) throws RestException {
        String prjPath = System.getProperty("user.dir");
    
        if (!filePath.startsWith("/")) {
            prjPath += "/";
        }
    
        //如果配置的文件路径最后不包含/，则添加上
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        
        File file = new File(prjPath + filePath + fileName);
    
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("File does not exist, file: " + file.getName());
            throw new RestException("文件不存在！");
        }
        
        byte[] buffer = new byte[1000];
        
        try {
            while (-1 != inputStream.read(buffer)) {
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            logger.error("Read file failed!", e);
            throw new RestException("读取文件内容失败！");
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
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
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
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(filePath + fileName), StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            logger.error("Write file failed!", e);
            throw new RestException("服务器写文件失败，请联系管理员！", e);
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
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(filePath + fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("File does not exist!", e);
            throw new RestException("文章内容不存在！");
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
        Pattern pattern = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        result = matcher.replaceAll("");
        
        //第二步去掉回车换行等字符
        return Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE).matcher(result).replaceAll("");
    }
    
    private static final String REGEX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";//定义空格回车换行符
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
}
