package org.accurate.grid;

import java.lang.reflect.Field;
import java.util.UUID;


public class Job implements Runnable, java.io.Serializable {
    private Runnable target=null;
    private String name="UnnamedJob";
    private JobCard myJobCard=null;

    private final UUID uuid = UUID.randomUUID();

    public void run() {    }
    
    
    /**************** C O N S T R U C T O R S ****************************/
    public Job (){
        super();
        this.target=this;
        this.name=this.toString();
    }

    public Job(String name) {
        super();
        this.target = this;
        this.name = name;
    }
    
    //-----------------------------------------
    public Job(Runnable target) {
        super();
        this.target=target;
        this.name = target.toString();
    }
    
    //-----------------------------------------
    public Job(Runnable target, String name) {
        super();
        this.target=target;
        this.name=name;
    }

    /*********************************************************************/
    public void start() {
        myJobCard=ClientAgent.postJob(this);
    }
    
            
    /*********************************************************************/
    public Job join() {
        while( true ) {
            try { Thread.sleep(500); } catch( InterruptedException e) { }

            try {
                //new Worker().work();
            } catch (Exception e) { }

            try {
                Job executedSelf = ClientAgent.collectJob(myJobCard);

                /* Now try to mutate the target with data from
                 * my executed-self */
                Class myClass=this.target.getClass();
                Class myExecutedSelfsClass=executedSelf.target.getClass();

                Field[] myFields=myClass.getDeclaredFields();
                Field[] myExecutedSelfsFields=myExecutedSelfsClass.getDeclaredFields();

                /*for each of my fields */
                for(int i=0; i<myFields.length; i++) {

                    /* set my and my-executed-self's fields accessible */
                    myFields[i].setAccessible(true);
                    myExecutedSelfsFields[i].setAccessible(true);

                    /*now mutate me*/
                    try {
                        myFields[i].set(this.target, myExecutedSelfsFields[i].get(executedSelf.target));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
                return executedSelf;
            } catch (java.util.NoSuchElementException e) { }
        }
    }

    
    public String UniqueID() {
        return uuid.toString();
    }
    
    
    /*********************************************************************/
    public void ExecuteNow() {
        target.run();
    }
            
    
    /*********************************************************************/
    public Runnable getTarget() {
        return target;
    }
    
    
    
    /*********************************************************************/
    public String getName() {
        return this.name;
    }
    
    //-----------------------------------------
    public void setName(String name) {
        this.name=name;
    }

}
