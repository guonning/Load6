package xyz.bpazy;

import xyz.bpazy.handler.ArticleHandlerRunnable;
import xyz.bpazy.handler.ScanArticleRunnable;
import xyz.bpazy.helper.HttpDownload;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ziyuan.
 * 2016/11/5 17:13
 */
public class LoadClient {
    private String proxyHost;
    private int proxyPort;
    private boolean useProxy;
    private String baseUrl = "http://t66y.com/";
    private ArrayBlockingQueue<String> articleQueue = new ArrayBlockingQueue<>(200);

    public void setProxy(String proxy) {
        String[] split = proxy.split(":");
        proxyHost = split[0];
        proxyPort = Integer.valueOf(split[1]);
        useProxy = true;
    }

    public void start() {
        if (useProxy) HttpDownload.getDefault().setProxy(proxyHost, proxyPort);
        HttpDownload.getDefault().setTimeOut(5000);
        new Thread(new ScanArticleRunnable(articleQueue, baseUrl)).start();
        new Thread(new ArticleHandlerRunnable(articleQueue, baseUrl)).start();
    }
}
