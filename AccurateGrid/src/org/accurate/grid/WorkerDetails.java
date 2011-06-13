/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.accurate.grid;

import java.io.Serializable;
import java.net.*;

/**
 *
 * @author gangs
 */
public class WorkerDetails implements Serializable {

    private String _address;

    public WorkerDetails() {
        try {
            _address = java.net.Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            _address = "UnknownIPAddress";
        }
        
    }

    public String getAddress() {
        return _address;
    }

}
