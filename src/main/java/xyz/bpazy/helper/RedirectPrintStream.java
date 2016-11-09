package xyz.bpazy.helper;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ziyuan.
 * 2016/11/9 17:47
 */
public class RedirectPrintStream extends PrintStream {
    private List<PrintStream> target = new ArrayList<>();

    public RedirectPrintStream(String... fileName) throws FileNotFoundException {
        super(fileName[0]);
        target.add(new PrintStream(System.out));
        for (String f : fileName) {
            PrintStream p = new PrintStream(f);
            target.add(p);
        }
    }

    @Override
    public void println(String s) {
        for (PrintStream element : target) {
            element.println(s);
        }
        flush();
    }
}
