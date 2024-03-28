
package com.Zeno410Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Zeno410
 */
public class StringWriter extends Receiver<String> {
    BufferedWriter output;
    boolean started = false;

    /** Creates a new instance of StringWriter */
    public StringWriter(File file) throws IOException {
        output = new BufferedWriter(new FileWriter(file));
    }

    public static StringWriter from(File file) {
        try{ return new StringWriter(file);}
        catch (IOException e) {throw new RuntimeException();}
    }

    public static StringWriter from(String fileName) {
        return StringWriter.from(new File(fileName));
    }

    public void accept(String written) {
        try {
            if (started) output.write('\r');
            started = true;
            output.write(written);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void done() {
        try {
            output.flush();
            output.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}