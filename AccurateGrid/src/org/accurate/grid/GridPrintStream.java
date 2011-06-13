/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author gangs
 */
public class GridPrintStream extends PrintStream {
    public GridPrintStream(IRemoteClientAgent client, WorkerDetails wd) {
        super(new GridOutputStream(client, wd));
    }
}

class GridOutputStream extends OutputStream {

    private java.io.ByteArrayOutputStream out;
    private IRemoteClientAgent client;
    private WorkerDetails wd;

    public GridOutputStream(IRemoteClientAgent client, WorkerDetails wd) {
        out=new java.io.ByteArrayOutputStream();
        this.client = client;
        this.wd = wd;
    }

    @Override
    public void write(int b) throws IOException {
        if(b == 13) {
            client.print(out.toString(), wd);
            out.reset();
        } else if(b !=10) {
            out.write(b);
        }

    }
}