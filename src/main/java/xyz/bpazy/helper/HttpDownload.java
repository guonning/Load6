package xyz.bpazy.helper;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by Ziyuan.
 * 2016/11/5 16:07
 */
public class HttpDownload {
    private String proxyHost;
    private int proxyPort;
    private int timeOut = 3000;

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    private HttpDownload() {

    }

    public void setProxy(String host, int port) {
        proxyHost = host;
        proxyPort = port;
    }

    public String get(String url) {
        return TryGet(url, 3);
    }

    private String TryGet(String url, int times) {
        for (int i = 0; i < times; i++) {
            try {
                byte[] bytes = HttpRequest.get(url)
                        .useProxy(proxyHost, proxyPort)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like " +
                                "Gecko) " +
                                "Chrome/54.0.2840.71 Safari/537.36")
                        .readTimeout(timeOut)
                        .connectTimeout(timeOut)
                        .bytes();
                return new String(bytes, "GBK");
            } catch (UnsupportedEncodingException | HttpRequest.HttpRequestException e) {
                if (e instanceof HttpRequest.HttpRequestException) {
                    System.out.println("connect " + url + " failed. Try again later.");
                } else {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static class SingletonHandler {
        private static HttpDownload singletonHelper = new HttpDownload();
    }

    public static HttpDownload getDefault() {
        return SingletonHandler.singletonHelper;
    }
}
