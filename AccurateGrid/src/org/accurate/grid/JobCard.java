/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

/**
 *
 * @author gangs
 */
public class JobCard implements java.io.Serializable {

    private String JobID=null;
    private IRemoteClientAgent ClientAgent=null;
    private String jobClassName=null;
    private String jobName=null;

    public JobCard(Job job, IRemoteClientAgent clientAgent) {
        this.JobID=job.UniqueID();
        this.ClientAgent=clientAgent;
        this.jobClassName=job.getClass().getName();
        this.jobName=job.getName();
    }

    public String getJobID() {
        return JobID;
    }

    public IRemoteClientAgent getClientAgent() {
        return this.ClientAgent;
    }

    public String getJobClassName() {
        return this.jobClassName;
    }

    public String getJobName() {
        return this.jobName;
    }


}
