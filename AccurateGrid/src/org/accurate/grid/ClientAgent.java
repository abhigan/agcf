/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;

/**
 *
 * @author gangs
 */
public class ClientAgent implements IRemoteClientAgent {
    
    private static ClientAgent mySoleInstance=null;
    private static IRemoteClientAgent myStub=null;
    private static PrintStream myOutputPrinter=null;
    private static String managerAddress="localhost";
    private static IRemoteGridManager coordinator=null;
    private static ClassLoader chainedCl=null;

    private static ArrayList<Job> localJobPool = new ArrayList<Job>();
    private static ArrayList<Job> executingPool = new ArrayList<Job>();
    private static ArrayList<Job> donePool = new ArrayList<Job>();
    
    private ClientAgent() {
        myOutputPrinter=System.out;
        chainedCl = Thread.currentThread().getContextClassLoader();
    }

    private static void instantiate() {
        if(mySoleInstance==null) {
            mySoleInstance = new ClientAgent();

            try {
                myStub=(IRemoteClientAgent)UnicastRemoteObject.exportObject(mySoleInstance,0);
                Registry registry = LocateRegistry.getRegistry(managerAddress);
                coordinator = (IRemoteGridManager)registry.lookup("GridManager");
            } catch (RemoteException e) {
                myOutputPrinter.print(e.toString());
                System.exit(-1);
            } catch (NotBoundException e) {
                myOutputPrinter.print(e.toString());
                System.exit(-1);
            }
        }
    }

    public static void setChainedClassLoader(ClassLoader cl) {
        chainedCl=cl;
    }

    public static void setManagerAddress(String address) {
        managerAddress = address;
    }

    public synchronized static JobCard postJob(Job job) {
        instantiate();
        JobCard jobCard = new JobCard(job, myStub);
        localJobPool.add(job);
        try {
            coordinator.postJob(jobCard);
        } catch (RemoteException e) {
            myOutputPrinter.println(e.toString());
        }
        return jobCard;
    }

    public synchronized static Job collectJob(JobCard jobCard) {
        Iterator itr = donePool.iterator();
        while(itr.hasNext()) {
            Job job = (Job)itr.next();
            if(job.UniqueID().equals(jobCard.getJobID())) {
                itr.remove();
                return job;
            }
        }
        throw new java.util.NoSuchElementException("job with ID:" + jobCard.getJobID() + " has not been completed yet");
    }


    

    public void print(String message, WorkerDetails wd) throws RemoteException {
        String _message;
        _message = "[" + wd.getAddress() + "] " + message;
        myOutputPrinter.println(_message);
    }

    public synchronized Job getJob(JobCard jobCard, WorkerDetails wd) throws RemoteException {
        Iterator itr=localJobPool.iterator();
        while(itr.hasNext()) {
            Job job = (Job)itr.next();
            if(job.UniqueID().equals(jobCard.getJobID())) {
                localJobPool.remove(job);
                executingPool.add(job);
                return job;
            }
        }
        Exception e=new Exception("Job with ID:" + jobCard.getJobID() + " not found");
        throw new RemoteException("Job Not Found", e);
    }

    public synchronized void doneJob(Job job, WorkerDetails wd) throws RemoteException {
        donePool.add(job);
        Iterator itr = executingPool.iterator();
        while(itr.hasNext()) {
            Job job1 = (Job)itr.next();
            if(job1.UniqueID().equals(job.UniqueID())) {
                itr.remove();
            }
        }
    }

    public byte[] getResource(String name, WorkerDetails wd) throws RemoteException {
        byte[] classDef = null;
        InputStream ins=chainedCl.getResourceAsStream(name);

        if(ins==null) {
            String currentAddress="-UnknownLocation-";
            try {
                currentAddress = java.net.Inet4Address.getLocalHost().getHostAddress();
            } catch (java.net.UnknownHostException e) { }
            throw new RemoteException("Failed to locate resource [" + name + "]@[" + currentAddress + "]");
        }

        ByteArrayOutputStream bout=new ByteArrayOutputStream();
        int i;
        try {
            while(true) {
                i=ins.read();
                if(i==-1)
                    break;

                bout.write(i);
            }
            ins.close();
            classDef=bout.toByteArray();
            bout.close();
            return classDef;
        } catch(IOException e) {
            throw new RemoteException("Failed to open resource [" + name + "]", e );
        }
    }
}
