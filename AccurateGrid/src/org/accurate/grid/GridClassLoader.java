/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;

/**
 *
 * @author gangs
 */
public class GridClassLoader extends ClassLoader {
    IRemoteClientAgent server;
    WorkerDetails wd;

    public GridClassLoader(IRemoteClientAgent server, WorkerDetails wd) {
        this.server = server;
        this.wd = wd;
    }

    @Override
    protected Class findClass(String name) {
        byte[] b=null;
        try {
            b=loadClassData(name);
            return defineClass(name, b, 0, b.length);
        } catch( RemoteException e) {
            throw new Error(e);
        }
    }

    private byte[] loadClassData(String name) throws RemoteException {
        byte[] b=null;
        String fileName=name.replace(".", "/") + ".class";
        b=server.getResource(fileName, wd);
        return b;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        byte[] b=null;
        ByteArrayInputStream bio;
        try {
            b=server.getResource(name, wd);
            bio = new ByteArrayInputStream(b);
        } catch (RemoteException e) {
            bio = null;
        }
        return bio;
    }
}
