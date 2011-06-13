
package org.accurate.grid;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;




public class GridManager implements IRemoteGridManager {
    private java.util.LinkedList<JobCard> readyQueue = new java.util.LinkedList<JobCard>();

    public static void main(String[] args) {

        try {
            String name = "GridManager";
            IRemoteGridManager server = new GridManager();
            IRemoteGridManager stub = (IRemoteGridManager)
                 UnicastRemoteObject.exportObject(server,0);
            Registry registry;
            registry = LocateRegistry.createRegistry(1099);
            registry.rebind(name, stub);
            System.out.println("JobServer started \n");
            while(true) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.err.println("JobServer exception:");
            e.printStackTrace();
        }
    }

    
    public synchronized JobCard requestJob(WorkerDetails wd) throws RemoteException {
        JobCard jobCard=null;
        jobCard=readyQueue.removeFirst();
        System.out.println("[" + jobCard.getJobName() + "] dequeued");
        return jobCard;
    }

    public synchronized int postJob(JobCard jobCard) throws RemoteException {
        System.out.println("[" + jobCard.getJobName() + "] enqueued");
        readyQueue.addLast(jobCard);
        return readyQueue.size();
    }


}

