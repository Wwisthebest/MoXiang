package com.zpark.book.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuUtils {
    public static final String ACCESS_KEY = "cuBDlauPFG_2KGYITyRjXI-gMde-Dg3lg4-LuSgc";
    public static final String SECRET_KEY = "77I4XO5V5M1ie0zI73mDuRgV5WY_94CHA61vIYhX";
    public static final String BUCKET = "1128qianxi";
    public static final String DO_MAIN = "http://sx737w976.hn-bkt.clouddn.com/";
    public static final Region REGION = Region.region2();

    /**
     * 获取上上传凭证
     * @return
     */
    public static String getToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET);
    }


    /**
     * 服务端直传文件数据
     * @param fileData 文件字节数组
     * @return url web 访问路径
     */
    public static String uploadFile(byte[] fileData) throws QiniuException {
        UploadManager uploadManager = getUploadManager();
        Response response = uploadManager.put(fileData, null, getToken());
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return DO_MAIN+putRet.key;
    }

    private static UploadManager getUploadManager() {
        Configuration configuration = new Configuration(REGION);
        return new UploadManager(configuration);
    }

}
