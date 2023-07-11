package cdu.zch.system.service;

import cdu.zch.system.model.dto.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zch
 * @date 2023/7/11
 **/
public interface OssService {

    /**
     * 上传文件
     * @param file 表单文件对象
     * @return 文件信息
     */
    FileInfo uploadFile(MultipartFile file);

    /**
     * 删除文件
     *
     * @param filePath 文件完整URL
     * @return 删除结果
     */
    boolean deleteFile(String filePath);


}

