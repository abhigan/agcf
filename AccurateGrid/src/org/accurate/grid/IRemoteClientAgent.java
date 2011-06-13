package org.accurate.grid;

import java.rmi.RemoteException;

public interface IRemoteClientAgent extends java.rmi.Remote {
    /*worker comes asking for a jobwith a job
     card acquired from the grid coordinator */
    Job getJob(JobCard jobCard, WorkerDetails wd) throws RemoteException;

    /*working is asking for a resource. mostly
     the GridClassLoader will ask for class files
     to instantiate them remotely */
    byte[] getResource(String name, WorkerDetails wd) throws RemoteException;

    /*the worker is returning a completed job*/
    void doneJob(Job job, WorkerDetails wd) throws RemoteException;

    /*the job in its execution process might
     send something to print on std-out like
     System.out.println("helloWorld")
     The message should end up on the Client's
     mechine*/
    void print(String message, WorkerDetails wd) throws RemoteException;
}
