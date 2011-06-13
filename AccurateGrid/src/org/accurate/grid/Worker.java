/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author gangs
 */
public class Worker {
    IRemoteGridManager server=null;
    WorkerDetails myDetails=new WorkerDetails();

    public Worker(String serverAddress) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress);
            server = (IRemoteGridManager) registry.lookup("GridManager");
        } catch (Exception e) { }
    }
    
    public static void main(String args[]) {
        
        while(true) {
            String serverAddress;
            if(args.length == 0) {
                serverAddress= "localhost";
            } else {
                serverAddress = args[0];
            }
            Worker w=new Worker(serverAddress);
            w.work();
        }
    }
    
    public void work() {

        try { Thread.sleep(100); } catch( InterruptedException e) { }

        java.io.PrintStream ps = System.out;
        ClassLoader cl=Thread.currentThread().getContextClassLoader();
        
        try {
            JobCard jobCard=server.requestJob(myDetails);
            IRemoteClientAgent clientAgent = jobCard.getClientAgent();
            
            GridClassLoader loader = new GridClassLoader(clientAgent, myDetails);
            Thread.currentThread().setContextClassLoader(loader);
            ClientAgent.setChainedClassLoader(loader);

            GridPrintStream gps=new GridPrintStream(clientAgent, myDetails);
            System.setOut(gps);

            ps.println("[" + jobCard.getJobName() + "] executing... " );
            Job job= clientAgent.getJob(jobCard, myDetails);
            job.ExecuteNow();
            clientAgent.doneJob(job, myDetails);
            ps.println("[" + job.getName() + "] executed" );

        } catch (java.util.NoSuchElementException e) {
            //just ignore if no-element-found @ server's jobQueue
        } catch (RemoteException e) {
            ps.println(e.toString());
        } finally {
            System.setOut(ps);
            Thread.currentThread().setContextClassLoader(cl);
        }

    }
}
