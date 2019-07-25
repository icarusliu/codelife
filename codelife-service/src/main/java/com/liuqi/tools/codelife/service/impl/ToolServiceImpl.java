package com.liuqi.tools.codelife.service.impl;

import com.liuqi.commons.service.AbstractEntityService;
import com.liuqi.tools.codelife.db.entity.FileInfo;
import com.liuqi.tools.codelife.db.entity.Tool;
import com.liuqi.tools.codelife.db.repository.ToolRepository;
import com.liuqi.tools.codelife.service.ToolService;
import com.liuqi.tools.codelife.service.dto.ToolVO;
import com.liuqi.tools.codelife.service.mapper.ToolMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具服务
 *
 * @author LiuQI 2019/4/12 16:57
 * @version V1.0
 **/
@Service
@Transactional
public class ToolServiceImpl
        extends AbstractEntityService<Tool, ToolVO, ToolRepository, ToolMapper>
        implements ToolService {
    public ToolServiceImpl(ToolRepository repository, ToolMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public ToolVO save(ToolVO toolVO) {
        toolVO.setUpdateTime(LocalDateTime.now());

        if (null != toolVO.getId()) {
            this.findOne(toolVO.getId()).ifPresent(dbToolVO -> {
                if (null == toolVO.getFileInfo()) {
                    toolVO.setFileInfo(dbToolVO.getFileInfo());
                }

                List<FileInfo> fileInfoList = dbToolVO.getImages();
                if (null == fileInfoList) {
                    fileInfoList = new ArrayList<>(16);
                }
                if (!CollectionUtils.isEmpty(toolVO.getImages())) {
                    fileInfoList.addAll(toolVO.getImages());
                }

                toolVO.setImages(fileInfoList);
            });
        }

        return super.save(toolVO);
    }
}
