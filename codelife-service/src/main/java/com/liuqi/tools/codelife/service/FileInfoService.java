package com.liuqi.tools.codelife.service;

import com.liuqi.commons.service.EntityService;
import com.liuqi.tools.codelife.service.dto.FileInfoVO;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件操作服务
 *
 * @author LiuQI 2019/4/10 18:20
 * @version V1.0
 **/
public interface FileInfoService extends EntityService<FileInfoVO> {
    /**
     * 上传文件
     * @param fileInfoVO   上传文件对象
     * @param file          文件内容
     */
    FileInfoVO upload(FileInfoVO fileInfoVO, MultipartFile file) throws RestException;

    /**
     * 根据模块及项主键查询文件
     * @param module    模块
     * @param itemId    模块项主键
     * @return          文件列表
     */
    List<FileInfoVO> findByItem(String module, Integer itemId);

    /**
     * 更新文件对应的模块ItemID
     * @param id        文件编号
     * @param itemId    模块ItemId
     */
    void updateItemId(Integer id, Integer itemId);

    /**
     * 批量更新文件ItemId
     * @param fileIds       文件主键清单
     * @param itemId        <>
     */
    void batchUpdateItemId(List<Integer> fileIds, Integer itemId);

    /**
     * 获取上传路径
     * @return  上传路径
     */
    String getUploadPath();
}
