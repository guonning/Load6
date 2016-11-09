package xyz.bpazy.main;

import com.beust.jcommander.JCommander;
import xyz.bpazy.helper.RedirectPrintStream;
import xyz.bpazy.models.RunParameter;

/**
 * Created by Ziyuan.
 * 2016/11/5 13:23
 */
public class Main {
    public static void main(String[] args) throws Exception {
        RunParameter para = new RunParameter();
        JCommander jComm = new JCommander(para, args);
        LoadClient client = new LoadClient();
        if (para.help) {
            jComm.usage();
            return;
        }
        System.out.println("crawler running...");
        if (!para.file.equals("")) {
            System.setOut(new RedirectPrintStream(para.file));
        }
        if (!para.proxy.equals("")) {
            client.setProxy(para.proxy);
        }
        client.start();
    }
}
