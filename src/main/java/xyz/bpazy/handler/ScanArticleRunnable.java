package xyz.bpazy.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import xyz.bpazy.helper.HttpDownload;
import xyz.bpazy.models.Application;
import xyz.bpazy.models.RunParameter;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ziyuan.
 * 2016/11/5 21:22
 */
public class ScanArticleRunnable implements Runnable {

    private ArrayBlockingQueue<String> queue;
    private String pageArg = "&search=&page=";
    private RunParameter prm;

    public ScanArticleRunnable(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    public ScanArticleRunnable(ArrayBlockingQueue<String> queue, RunParameter prm) {
        this.queue = queue;
        this.prm = prm;
    }

    public ScanArticleRunnable setParameter(RunParameter parameter) {
        prm = parameter;
        return this;
    }


    @Override
    public void run() {
        String body = HttpDownload.getDefault().get(prm.baseUrl + "index.php");
        Document doc = Jsoup.parse(body);
        Elements region = doc.select("tbody#cate_1 h2 a");
        for (int i = 0; i < prm.pageSize; i++) {
            int finalI = i;
            region.forEach(element1 -> {
                String extraRegion = HttpDownload.getDefault().get(prm.baseUrl + element1.attr("href") + pageArg +
                        (finalI + 1));
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
        }
        Application.finish = true;
    }
}