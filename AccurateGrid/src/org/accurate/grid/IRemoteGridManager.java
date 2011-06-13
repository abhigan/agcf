package org.accurate.grid;

import java.rmi.RemoteException;

public interface IRemoteGridManager extends java.rmi.Remote {
    public JobCard requestJob(WorkerDetails wd) throws RemoteException;
    public int postJob(JobCard jobCard) throws RemoteException;
}