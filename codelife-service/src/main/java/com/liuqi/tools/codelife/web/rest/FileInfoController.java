package com.liuqi.tools.codelife.web.rest;

import com.liuqi.commons.web.rest.BaseEntityController;
import com.liuqi.tools.codelife.service.FileInfoService;
import com.liuqi.tools.codelife.service.dto.FileInfoVO;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件管理控制器
 *
 * @author LiuQI 2019/4/10 18:29
 * @version V1.0
 **/
@RestController
@RequestMapping("/file")
public class FileInfoController extends BaseEntityController<FileInfoVO, FileInfoService> {
    private static final Logger logger = LoggerFactory.getLogger(FileInfoController.class);

    public FileInfoController(FileInfoService entityService) {
        super(entityService);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public FileInfoVO upload(@RequestParam(value = "file")MultipartFile file,
                             @RequestParam("uploadUser") Integer uploadUser,
                             @RequestParam("module") String module) throws RestException {
        return this.entityService.upload(new FileInfoVO()
                .setModule(module)
                .setUploadUser(uploadUser), file);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "batchUpload", consumes = "multipart/form-data")
    public List<FileInfoVO> batchUpload(@RequestParam(value = "files") MultipartFile[] files,
                                        @RequestParam("uploadUser") Integer uploadUser,
                                        @RequestParam("module") String module) {
        return Arrays.stream(files).map(file -> this.entityService.upload(new FileInfoVO().setModule(module).setUploadUser(uploadUser), file))
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateItemId")
    public void updateItemId(@RequestParam("id") Integer id,
                             @RequestParam("itemId") Integer itemId) {
        this.entityService.updateItemId(id, itemId);
    }

    @GetMapping(value = "/download/{id}")
    public void download(@PathVariable("id") Integer id,
                         HttpServletResponse response) {
        this.entityService.findOne(id).ifPresent(fileInfo -> {
            String fileName = fileInfo.getName();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-download");
//            try {
//                fileName = URLEncoder.encode(fileName, "utf-8");
//            } catch (UnsupportedEncodingException ignored) {
//            }

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            try (OutputStream outputStream = response.getOutputStream()) {
                FileUtils.outputFileContent(entityService.getUploadPath(), fileInfo.getPath(), outputStream);
                Integer downloadCount = fileInfo.getDownloadCount();
                fileInfo.setDownloadCount(null == downloadCount ? 1 : downloadCount + 1);
                this.entityService.save(fileInfo);
            } catch (IOException e) {
                logger.error("写文件流失败", e);
            }
        });
    }
}
