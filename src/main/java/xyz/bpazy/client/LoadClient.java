package xyz.bpazy.client;

import xyz.bpazy.handler.ArticleHandlerRunnable;
import xyz.bpazy.handler.ScanArticleRunnable;
import xyz.bpazy.helper.HttpDownload;
import xyz.bpazy.models.RunParameter;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ziyuan.
 * 2016/11/5 17:13
 */
public class LoadClient {
    private String proxyHost;
    private int proxyPort;
    private boolean useProxy;
    private ArrayBlockingQueue<String> articleQueue = new ArrayBlockingQueue<>(200);
    private RunParameter prm;

    public LoadClient(RunParameter prm) {
        this.prm = prm;
        if (!this.prm.proxy.equals("")) {
            setProxy(this.prm.proxy);
        }
    }

    public LoadClient setParameter(RunParameter parameter) {
        prm = parameter;
        if (!prm.proxy.equals("")) {
            setProxy(prm.proxy);
        }
        return this;
    }

    private void setProxy(String proxy) {
        String[] split = proxy.split(":");
        proxyHost = split[0];
        proxyPort = Integer.valueOf(split[1]);
        useProxy = true;
    }

    public void start() {
        if (useProxy) HttpDownload.getDefault().setProxy(proxyHost, proxyPort);
        HttpDownload.getDefault().setTimeOut(5000);
        new Thread(new ScanArticleRunnable(articleQueue, prm)).start();
        new Thread(new ArticleHandlerRunnable(articleQueue, prm)).start();
    }
}
