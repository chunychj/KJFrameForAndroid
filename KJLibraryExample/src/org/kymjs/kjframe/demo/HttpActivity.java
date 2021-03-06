package org.kymjs.kjframe.demo;

import java.io.File;
import java.util.Map;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HttpActivity extends KJActivity {

    // 文件上传的PHP后台实现示例
    // <?php
    // if ($_FILES["file"]["error"] > 0)
    // {
    // echo "Return Code: " . $_FILES["file"]["error"] . "<br />";
    // }
    // else
    // {
    // echo "Upload: " . $_FILES["file"]["name"] . "<br />";
    // echo "Type: " . $_FILES["file"]["type"] . "<br />";
    // echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
    // echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br />";
    //
    // if (file_exists("upload/" . $_FILES["file"]["name"]))
    // {
    // echo $_FILES["file"]["name"] . " already exists. ";
    // }
    // else
    // {
    // move_uploaded_file($_FILES["file"]["tmp_name"],
    // "upload/" . $_FILES["file"]["name"]);
    // echo "Stored in: " . "upload/" . $_FILES["file"]["name"];
    // }
    // }
    // ?>

    @BindView(id = R.id.button1, click = true)
    private Button mBtn1;
    @BindView(id = R.id.button2, click = true)
    private Button mBtn2;
    @BindView(id = R.id.button3, click = true)
    private Button mBtn3;
    @BindView(id = R.id.button4, click = true)
    private Button mBtn4;
    @BindView(id = R.id.button5, click = true)
    private Button mBtn5;
    @BindView(id = R.id.button6, click = true)
    private Button mBtn6;

    @Override
    public void setRootView() {
        setContentView(R.layout.http);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mBtn1.setText("GET请求");
        mBtn2.setText("POST请求");
        mBtn3.setText("文件上传");
        mBtn4.setText("JSON提交参数");
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
        case R.id.button1:
            get();
            break;
        case R.id.button2:
            post();
            break;
        case R.id.button3:
            postWithFile();
            break;
        case R.id.button4:
            jsonRequest();
            break;
        }
    }

    /**
     * 清空全部缓存
     */
    private void cleanCache() {
        KJHttp kjh = KJHttp.create();
        kjh.cleanCache();
    }

    /**
     * 移除指定key的缓存（注意，get请求的时候，记得把参数附带上）
     */
    private void removeCache() {
        KJHttp kjh = KJHttp.create();
        String url = "http://www.oschina.net/action/api/team_stickynote_batch_update";
        kjh.removeCache(url);
    }

    /**
     * 读取一条指定缓存，没有会返回空字符串
     */
    private void readCache() {
        KJHttp kjh = KJHttp.create();
        String url = "http://www.oschina.net/action/api/team_stickynote_batch_update";
        toast(new String(kjh.getCache(url)));
    }

    private void get() {
        KJHttp kjh = KJHttp.create();
        HttpParams params = new HttpParams();
        params.put("pageIndex", 0);
        params.put("pageSize", 20);
        kjh.get("http://www.oschina.net/action/api/tweet_list", params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        toast(t);
                    }
                });
    }

    /**
     * 附带有Http请求头的get请求
     */
    private void getWithHeader() {
        KJHttp kjh = KJHttp.create();
        HttpParams params = new HttpParams();
        params.put("uid", 863548);
        params.put("teamid", 12375);
        params.put("pageIndex", 0);
        params.put("pageSize", 20);
        params.put("type", "all");
        params.putHeaders("cookie", "cookie不能告诉你");
        kjh.get("http://team.oschina.net/action/api/team_active_list", params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        toast(t);
                    }
                });
    }

    private void post() {
        KJHttp kjh = KJHttp.create();
        HttpParams params = new HttpParams();
        // params.put("uid", 863548);
        // params.put("msg", "没有网，[发怒]");
        // params.putHeaders("cookie", "cookie不能告诉你");
        // kjh.post("http://www.oschina.net/action/api/tweet_pub", params,
        params.put("username", "kymjs123@gmail.com");
        params.put("pwd", "ZT2995212680");
        kjh.post("http://www.oschina.net/action/api/login_validate", params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(Map<String, String> headers, byte[] t) {
                        super.onSuccess(headers, t);
                        // 获取cookie
                        KJLoger.debug("===" + headers.get("Set-Cookie"));
                        Log.i("kymjs", new String(t));
                    }
                });
    }

    /**
     * 附带有文件的post请求
     */
    private void postWithFile() {
        KJHttp kjh = KJHttp.create();
        HttpParams params = new HttpParams();
        params.put("uid", 863548);
        params.put("msg", "在有图片的时 [发呆]");
        params.put("img", new File("/storage/emulated/0/qrcode.jpg"));
        params.putHeaders("Cookie", "cookie不能告诉你");
        kjh.post("http://www.oschina.net/action/api/tweet_pub", params,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        toast(t);
                    }
                });
    }

    /**
     * 使用JSON提交参数而不是Form表单
     */
    private void jsonRequest() {
        KJHttp kjh = KJHttp.create();
        HttpParams params = new HttpParams();
        params.putHeaders("Cookie", "cookie不能告诉你");
        params.putJsonParams("{\"uid\":863548,\"stickys\":[{\"id\":29058,\"iid\":0,\"content\":\"你好\",\"color\":\"green\",\"createtime\":\"2015-04-16 16:26:17\",\"updatetime\":\"2015-04-16 16:26:17\"}]}");
        kjh.jsonPost(
                "http://www.oschina.net/action/api/team_stickynote_batch_update",
                params, new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        toast(t);
                    }
                });
    }

    KJHttp kjh = KJHttp.create();

    /**
     * 暂停下载
     */
    private void pause() {
        kjh.getDownloadController(FileUtils.getSDCardPath() + "/1.apk",
                "http://music.baidu.com/cms/mobile/static/apk/Baidumusic_yinyuehexzfc.apk")
                .pause();
    }

    /**
     * 下载。有一个陷阱需要你注意：下载方法中的KJHttp对象必须和暂停方法中的KJHttp对象为同一个对象
     */
    private void download() {
        kjh.download(
                FileUtils.getSDCardPath() + "/1.apk",
                "http://music.baidu.com/cms/mobile/static/apk/Baidumusic_yinyuehexzfc.apk",
                new HttpCallBack() {
                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                        KJLoger.debug(count + "====" + current);
                    }

                    @Override
                    public void onSuccess(byte[] t) {
                        super.onSuccess(t);
                        toast("完成");
                    }
                });
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        KJLoger.debug(text);
    }

    // private void download() {
    // kjh.download(mEtDownloadPath.getText().toString(),
    // FileUtils.getSaveFile("KJLibrary", "l.pdf"),
    // new HttpCallBack() {
    // @Override
    // public void onSuccess(File f) {
    // super.onSuccess(f);
    // KJLoger.debug("success");
    // ViewInject.toast("下载成功");
    // mProgress.setProgress(mProgress.getMax());
    // }
    //
    // @Override
    // public void onFailure(Throwable t, int errorNo,
    // String strMsg) {
    // super.onFailure(t, errorNo, strMsg);
    // KJLoger.debug("onFailure");
    // }
    //
    // @Override
    // public void onLoading(long count, long current) {
    // super.onLoading(count, current);
    // mProgress.setMax((int) count);
    // mProgress.setProgress((int) current);
    // KJLoger.debug(count + "------" + current);
    // }
    // });
    // }
}
