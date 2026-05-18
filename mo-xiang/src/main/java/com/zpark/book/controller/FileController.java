package com.zpark.book.controller;

import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.service.FileService;
import com.zpark.book.utils.IdFactoryUtil;
import com.zpark.book.utils.QiniuUtils;
import com.zpark.book.vo.ResultVo;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileController {

    @Value("${userFilePath}")
    private String userFilePath;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private FileService fileService;

    @PostMapping("/file")
    public ResultVo uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            byte[] fileData = multipartFile.getBytes();
            String url = QiniuUtils.uploadFile(fileData);
            return ResultVo.success(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
        }
    }

    @GetMapping("/image")
    public ResultVo getImage(@RequestParam("imageName") String imageName) {
        // 由于文件已经上传到七牛云，直接返回七牛云的访问 URL
        // 这里假设 imageName 就是七牛云存储的文件名
        String url = QiniuUtils.DO_MAIN + imageName;
        return ResultVo.success(url);
    }
}