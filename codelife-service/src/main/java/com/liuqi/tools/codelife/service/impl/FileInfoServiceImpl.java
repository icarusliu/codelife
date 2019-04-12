package com.liuqi.tools.codelife.service.impl;

import com.liuqi.commons.service.AbstractEntityService;
import com.liuqi.tools.codelife.db.entity.FileInfo;
import com.liuqi.tools.codelife.db.repository.FileInfoRepository;
import com.liuqi.tools.codelife.service.FileInfoService;
import com.liuqi.tools.codelife.service.dto.FileInfoDTO;
import com.liuqi.tools.codelife.service.mapper.FileInfoMapper;
import com.liuqi.tools.codelife.util.FileUtils;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务实现类
 *
 * @author LiuQI 2019/4/10 18:20
 * @version V1.0
 **/
@Service
@Transactional
public class FileInfoServiceImpl
        extends AbstractEntityService<FileInfo, FileInfoDTO, FileInfoRepository, FileInfoMapper>
        implements FileInfoService {
    @Value("${app.file.uploadPath}")
    private String uploadPath;

    public FileInfoServiceImpl(FileInfoRepository repository, FileInfoMapper mapper) {
        super(repository, mapper);
    }

    /**
     * 上传文件
     * @param fileInfo    上传文件对象
     * @param file        文件内容
     */
    @Override
    public FileInfoDTO upload(FileInfoDTO fileInfo, MultipartFile file) throws RestException {
        String fileName = FileUtils.saveToFile(file, uploadPath);
        fileInfo.setName(file.getOriginalFilename())
                .setPath(fileName);
        FileInfoDTO savedFileInfoDTO = this.save(fileInfo);
        savedFileInfoDTO.setUploadDir(uploadPath);
        return savedFileInfoDTO;
    }

    /**
     * 根据模块及项主键查询文件
     *
     * @param module 模块
     * @param itemId 模块项主键
     * @return 文件列表
     */
    @Override
    public List<FileInfoDTO> findByItem(String module, Integer itemId) {
        return findList(new FileInfoDTO()
                .setModule(module)
                .setItemId(itemId));
    }

    /**
     * 更新文件对应的模块ItemID
     *
     * @param id     文件编号
     * @param itemId 模块ItemId
     */
    @Override
    public void updateItemId(Integer id, Integer itemId) {
        this.findOne(id).ifPresent(file -> this.save(file.setItemId(itemId)));
    }

    /**
     * 批量更新文件ItemId
     *
     * @param fileIds 文件主键清单
     * @param itemId  <>
     */
    @Override
    public void batchUpdateItemId(List<Integer> fileIds, Integer itemId) {
        List<FileInfoDTO> fileInfoDTOList = findByIds(fileIds);
        if (!CollectionUtils.isEmpty(fileInfoDTOList)) {
            fileInfoDTOList.forEach(fileInfo -> fileInfo.setItemId(itemId));
            this.save(fileInfoDTOList);
        }
    }

    /**
     * 获取上传路径
     *
     * @return 上传路径
     */
    @Override
    public String getUploadPath() {
        return uploadPath;
    }

    private List<FileInfoDTO> findByIds(List<Integer> fileIds) {
        Specification<FileInfo> specification = Specification.where((Specification<FileInfo>) (root, criteriaQuery, criteriaBuilder)
                -> root.get("id").in(fileIds));
        return mapper.toDto(repository.findAll(specification));
    }
}
