package com.liuqi.tools.codelife.web.rest;

import com.liuqi.commons.web.rest.BaseEntityController;
import com.liuqi.tools.codelife.service.FileInfoService;
import com.liuqi.tools.codelife.service.dto.FileInfoDTO;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理控制器
 *
 * @author LiuQI 2019/4/10 18:29
 * @version V1.0
 **/
@RestController
@RequestMapping("/file")
@PreAuthorize("isAuthenticated()")
public class FileInfoController extends BaseEntityController<FileInfoDTO, FileInfoService> {
    public FileInfoController(FileInfoService entityService) {
        super(entityService);
    }

    @PostMapping("/upload")
    public FileInfoDTO upload(@RequestParam("file")MultipartFile file,
                              @RequestParam("uploadUser") Integer uploadUser,
                              @RequestParam("module") String module) throws RestException {
        return this.entityService.upload(new FileInfoDTO()
                .setModule(module)
                .setUploadUser(uploadUser), file);
    }

    @PostMapping("/updateItemId")
    public void updateItemId(@RequestParam("id") Integer id,
                             @RequestParam("itemId") Integer itemId) {
        this.entityService.updateItemId(id, itemId);
    }
}
