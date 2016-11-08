package xyz.bpazy;

import org.apache.commons.cli.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.bpazy.helper.HttpDownload;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by Ziyuan.
 * 2016/11/5 13:23
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Options op = new Options();
        op.addOption("p", true, "Http proxy address, such as 127.0.0.1:8080");
        op.addOption("h", "help", false, "Help information.");
        CommandLineParser parse = new DefaultParser();
        CommandLine cl = parse.parse(op, args);
        LoadClient client = new LoadClient();
        if (cl.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Help information.", op);
            System.exit(0);
        }
        if (cl.hasOption("p")) {
            String proxy = cl.getOptionValue("p");
            client.setProxy(proxy);
        }
        client.start();
    }
}
