package xyz.bpazy.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import xyz.bpazy.Application;
import xyz.bpazy.helper.HttpDownload;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ziyuan.
 * 2016/11/5 21:22
 */
public class ScanArticleRunnable implements Runnable {

    private ArrayBlockingQueue<String> queue;
    private String baseUrl;

    public ScanArticleRunnable(ArrayBlockingQueue<String> queue, String baseUrl) {
        this.queue = queue;
        this.baseUrl = baseUrl;
    }

    @Override
    public void run() {
        String body = HttpDownload.getDefault().get(baseUrl + "index.php");
        Document doc = Jsoup.parse(body);
        Elements region = doc.select("tbody#cate_1 h2 a");
        region.forEach(element1 -> {
            String extraRegion = HttpDownload.getDefault().get(baseUrl + element1.attr("href"));
            Document extraDoc = Jsoup.parse(extraRegion);
            Elements select = extraDoc.select("h3 a");
            select.forEach(element2 -> {
                try {
                    String href = element2.attr("href");
                    if (href != null && !href.equals(""))
                        queue.put(href);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        Application.finish = true;
    }
}