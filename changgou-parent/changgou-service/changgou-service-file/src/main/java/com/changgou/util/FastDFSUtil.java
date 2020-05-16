package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 实现FastDFS文件管理
 *      文件上传
 *      文件删除
 *      文件下载
 *      文件信息获取
 *      tracke信息获取
 *      storage信息获取
 */
public class FastDFSUtil {

    //加载tracker连接信息
    static {
        try {
            //查询classpath（类路径）下的文件路径
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  文件上传
     * @param fastDFSFile 上传的文件信息封装
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception {

        //附加参数
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("author",fastDFSFile.getAuthor());

        TrackerServer trackerServer = getTrackerServer();

        //通过 TrackerServer 的连接信息可以获取 storage 的连接信息，创建 StorageClient 对象存储 storage 的连接信息
        StorageClient storageClient = getStorageClient(trackerServer);

        /**
         * 通过 StorageClient 访问 storage，实现文件上传并且获取文件上传后的存储信息
         * 参数：
         *  1. 上传文件的字节数组
         *  2. 文件的扩展名 如jpg png
         *  3. 附加参数  如 拍摄地址：北京
         *
         *  uploads[]:
         *      uploads[0]:文件上传所存储的storage的组命中 group1
         *      uploads[0]: 文件存储到storage上的文件名字  M00/02/44/xxxx.jpg
         *
         */
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);

        return uploads;
    }

    /**
     * 获取StorageClient的抽取
     * @param trackerServer
     * @return
     */
    public static StorageClient getStorageClient(TrackerServer trackerServer) {
        return new StorageClient(trackerServer,null);
    }


    /**
     * 获取文件信息
     * @param groupName 文件的组名 group1
     * @param remoteFileName 文件的存储路径名字 M00/00/00/wKgBjF6_ofOAddY1AAVEO4QYKDs293.png
     */

    public static FileInfo getFile(String groupName, String remoteFileName) throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer 获取storage信息，创建 storageclient对象存储storage信息
        StorageClient storageClient = getStorageClient(trackerServer);

        //获取文件信息
        return storageClient.get_file_info(groupName,remoteFileName );
    }


    /**
     * 文件下载
     * @param groupName 文件的组名 group1
     * @param remoteFileName 文件的存储路径名字 M00/00/00/wKgBjF6_ofOAddY1AAVEO4QYKDs293.png
     * @return
     * @throws Exception
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer 获取storage信息，创建 storageclient对象存储storage信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //文件下载
        byte[] buffer = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(buffer);
    }

    /**
     * 删除文件
     * @param groupName 文件的组名 group1
     * @param remoteFileName 文件的存储路径名字 M00/00/00/wKgBjF6_ofOAddY1AAVEO4QYKDs293.png
     * @return
     * @throws Exception
     */
    public static void delelteFile(String groupName, String remoteFileName) throws Exception {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过 TrackerClient 获取 TrackerServer 的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer 获取storage信息，创建 storageclient对象存储storage信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //删除文件
        storageClient.delete_file(groupName,remoteFileName );
    }

    /**
     * 获取storage信息
     * @return
     * @throws Exception
     */
    public static StorageServer getStorages() throws Exception {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过 TrackerClient 获取 TrackerServer 的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();

        //获取storage信息
        return trackerClient.getStoreStorage(trackerServer);

    }

    /**
     * 获取storage组的ip和端口信息
     * @return
     * @throws Exception
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws Exception {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过 TrackerClient 获取 TrackerServer 的连接对象
        TrackerServer trackerServer = trackerClient.getConnection();

        //获取storage组的ip和端口信息
        return trackerClient.getFetchStorages(trackerServer,groupName ,remoteFileName );
    }

    /**
     * 获取Tracker信息
     * @return
     * @throws Exception
     */
    public static String getTrackerInfo() throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //Tracker的Ip，Http端口
        String ip = trackerServer.getInetSocketAddress().getHostString();
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();

        String url = "http://"+ip+":"+tracker_http_port;
        return url;
    }

    /**
     * 获取Tracker的抽取
     * @return
     * @throws IOException
     */
    public static TrackerServer getTrackerServer() throws IOException {
        // 创建一个 TrackerClient 对象，通过 TrackerClient 对象访问 TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        //通过 TrackerClient 获取 TrackerServer 的连接对象
        return trackerClient.getConnection();
    }


    public static void main(String[] args) throws Exception{
            /*
            1 --- //获取文件信息
            FileInfo fileInfo = getFile("group1", "M00/00/00/wKgBjF6_ofOAddY1AAVEO4QYKDs293.png");
            System.out.println(fileInfo.getSourceIpAddr());
            System.out.println(fileInfo.getFileSize());
            */

            /*
            2 -- //文件下载
            InputStream is = downloadFile("group1", "M00/00/00/wKgBjF6_ofOAddY1AAVEO4QYKDs293.png");
            //将文件写入到本地磁盘
            FileOutputStream os = new FileOutputStream("D:/temp5/a.png");
            //定义一个缓冲区
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();
            os.close();
            is.close();
            */

           /*
            3 -- //文件删除
            delelteFile("group1", "M00/00/00/wKgBjF6_wpqAXfKGAAFcf3TqvzQ864.jpg");
            */

           /*
            4 -- //获取storage信息
            StorageServer storageServer = getStorages();
            System.out.println(storageServer.getStorePathIndex());
            System.out.println(storageServer.getInetSocketAddress().getHostString()); //ip信息
            */

           /*
            5 -- //获取storage组的ip和端口信息
            ServerInfo[] groups = getServerInfo("group1", "M00/00/00/wKgBjF6_yBuAIdhdAAFcf3TqvzQ989.jpg");
            for (ServerInfo group : groups) {
                System.out.println(group.getIpAddr());
                System.out.println(group.getPort());
            }
            */

           //获取Tracker信息
            String trackerInfo = getTrackerInfo();
            System.out.println(trackerInfo);

        }


}





















