package xyz.bpazy.helper;

import xyz.bpazy.models.RunParameter;

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

    public RedirectPrintStream(RunParameter par) throws FileNotFoundException {
        super(System.out);
        target.add(new PrintStream(System.out));
        if (!par.file.equals("")) {
            PrintStream p = new PrintStream(par.file);
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
