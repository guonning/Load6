package xyz.bpazy.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.bpazy.models.Application;
import xyz.bpazy.helper.HttpDownload;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ziyuan.
 * 2016/11/5 21:32
 */
public class ArticleHandlerRunnable implements Runnable {
    private ArrayBlockingQueue<String> queue;
    private String baseUrl;
    private static String magnetHeader = "magnet:?xt=urn:btih:";
    private ExecutorService executorService = Executors.newFixedThreadPool(30);

    // 匹配哈希码
    private Pattern pattern = Pattern.compile("[a-z\\d]{40}");

    public ArticleHandlerRunnable(ArrayBlockingQueue<String> queue, String baseUrl) {
        this.queue = queue;
        this.baseUrl = baseUrl;
    }

    @Override
    public void run() {
        while (true) {
            String href = "";
            try {
                href = queue.poll(1000, TimeUnit.MILLISECONDS);
                if (href == null) {
                    if (Application.finish) {
                        break;
                    }
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (Application.finish) {
                    break;
                }
            }
            final String finalHref = href;
            executorService.submit(() -> {
                try {
                    String article = HttpDownload.getDefault().get(baseUrl + finalHref);
                    Document articleDoc = Jsoup.parse(article);
                    Matcher matcher = pattern.matcher(articleDoc.toString());
                    if (matcher.find()) {
                        System.out.println(magnetHeader + matcher.group() + "\t" + articleDoc.title());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
