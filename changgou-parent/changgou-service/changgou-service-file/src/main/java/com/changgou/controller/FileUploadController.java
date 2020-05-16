package com.changgou.controller;


import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("upload")
@CrossOrigin
public class FileUploadController {


    @PostMapping
    public Result upload(@RequestParam("file")MultipartFile file) throws Exception{
        //封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), //文件名字
                file.getBytes(), //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()) //获取文件扩展名
        );

        //调用FastDFSUtil工具类将文件传入到FastDFS中
        String[] uploads = FastDFSUtil.upload(fastDFSFile);

        //拼接访问地址 url=http://192.168.1.140:8080/group1/M00/02/44/xxxx.jpg
        String url = FastDFSUtil.getTrackerInfo()+"/"+uploads[0]+"/"+uploads[1];

        return new Result(true,StatusCode.OK,"上传成功",url);
    }


}
