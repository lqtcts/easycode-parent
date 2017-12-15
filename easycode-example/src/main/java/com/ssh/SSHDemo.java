package com.ssh;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;


public class SSHDemo{

    private final String IP = "***.***.***.***";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String PATH = "/home/";
    private Connection conn = null;
    private int lastModifyTime = 0;
    
    public boolean copyLogFile(String fileName, long updateTime){
        
        try {
            // connect linux host
            this.conn = new Connection(this.IP);
            this.conn.connect();
            boolean isAuthenticated = this.conn.authenticateWithPassword(this.USERNAME, this.PASSWORD);

            if (isAuthenticated) {
                // get the last modify time of log file
                SFTPv3Client sftpClient = new SFTPv3Client(this.conn);
                this.lastModifyTime = sftpClient.lstat(this.PATH + fileName).mtime;
                
                // copy log file
                SCPClient client = new SCPClient(this.conn);                
                Session sess = this.conn.openSession();
                
                // close connection 
                sess.close();
                this.conn.close();
  
                return true;               
            }             
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return false;
    }  
    
}