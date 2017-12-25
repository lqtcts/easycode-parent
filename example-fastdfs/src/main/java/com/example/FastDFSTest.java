package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.DownloadCallback;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

public class FastDFSTest {
    public static void main(String[] args) throws Exception {
        //加载配置文件的方式
        String configFileName = "fdfs_client.conf";
        try {
            ClientGlobal.init(configFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = "C:\\Users\\0\\Desktop\\a.jpg";
        File file = new File(path);
        //返回储存路径:M00/00/00/wKgB1loyOaKAPsmdAAABEmKjEk440.conf
        String[] files = uploadFile(file);
        System.out.println(Arrays.asList(files));
//        String fileId = uploadFileRetureFileId(file);
//        System.out.println(fileId);
//        download("Z3JvdXAxL00wMC8wMC8wMC93S2dCMWxvM1pqaUFjMjNyQUFBQlBwREJtZ1EucHJvcGVy");

//        String ddd = getToken("wKgB1lo3aPWAa_4sAAABPpDBmgQ.proper", "ddd");
//        System.out.println(ddd);
    }

    /**
     * 上传文件
     */
    public static String[] uploadFile(File file) throws IOException {
        String uploadFileName = file.getName();
        byte[] fileBuff = IOUtils.toByteArray(new FileInputStream(file));
        ;
        String[] files = null;
        String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);

        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient client = new StorageClient(trackerServer, storageServer);

        // 设置元信息
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", uploadFileName);
        metaList[1] = new NameValuePair("fileExtName", fileExtName);
        metaList[2] = new NameValuePair("fileLength", String.valueOf(file.length()));

        // 上传文件
        try {
            files = client.upload_file(fileBuff, fileExtName, metaList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            trackerServer.close();
        }
        return files;
    }

    public static String uploadFileRetureFileId(File file) throws IOException {
        String uploadFileName = file.getName();
        byte[] fileBuff = IOUtils.toByteArray(new FileInputStream(file));
        String fileId = null;
        String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);

        // 建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);

        // 设置元信息
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", uploadFileName);
        metaList[1] = new NameValuePair("fileExtName", fileExtName);
        metaList[2] = new NameValuePair("fileLength", String.valueOf(file.length()));

        try {
            fileId = client.upload_file1(fileBuff, fileExtName, metaList);
            fileId = FileId.getFileIdByPath(fileId);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return fileId;
    }


    public static void download(String fileId) throws FileNotFoundException {
        String path="F:\\project\\mypro\\easycode-parent\\example-fastdfs\\src\\main\\resource\\";
        FileOutputStream fileOutputStream = new FileOutputStream(path+"tep");
        System.out.println();
        TrackerClient tracker = null;
        TrackerServer trackerServer = null;
        try {
            //解码fileId
            fileId = FileId.getPathByFileId(fileId);
            // 建立连接 fastDFS方式
            tracker = new TrackerClient();
            trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            //响应内容设置
//            response.setContentType("image/png");
            //查询fdfs
            client.download_file1(fileId, new DownloadCallback() {
                /**
                 * @parem arg0 文件大小
                 * @parem arg1 數據流
                 * @parem arg2每次返回的大小
                 */
                public int recv(long arg0, byte[] arg1, int arg2) {
                    try {
                        //返回数据流
                        fileOutputStream.write(arg1, 0, arg2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                trackerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取访问服务器的token，拼接到地址后面
     *
     * @param filepath 文件路径 group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
     * @param httpSecretKey 密钥
     * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
     */
    public static String getToken(String filepath, String httpSecretKey){
        // unix seconds
        int ts = (int) Instant.now().getEpochSecond();
        // token
        String token = "null";
        try {
            token = ProtoCommon.getToken(filepath, ts, httpSecretKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("token=").append(token);
        sb.append("&ts=").append(ts);

        return sb.toString();
    }
}