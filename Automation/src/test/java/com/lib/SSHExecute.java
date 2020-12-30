package com.lib;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * This generic class is used to create SSH session with unix server and executes command.
 *
 * @version 0.1  -
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 *
 */
public class SSHExecute {

    private Session session;
    private Channel channel;
    private JSch jsch;
    private UserInfo userInfo;
    private Properties config;
    public int iExitStatus;

    public SSHExecute() {
        jsch = new JSch();
        Session session = null;
        config = new Properties();
        iExitStatus = 0;
    }

    /**
     * Connect to remote machine and establish session.
     *
     * @return if connect successfully, return true, else return false
     */
    public Boolean connect(String sHostName, String sUserName, String sPassword) {
        try {
            session = jsch.getSession(sUserName, sHostName);
            System.out.println("Session initialized with Host [" + sHostName + "] and associated with user ["+sUserName+"] and credential " + sPassword);
            //session.setUserInfo(userInfo);
            session.setPassword(sPassword);
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            System.out.println("SSHExec initialized successfully");
            System.out.println("SSHExec trying to connect " + sUserName + "@" + sHostName);

            session.connect();
            System.out.println("SSH connection established");
            return true;
        }
        catch(Exception e) {
            System.out.println("Connect fails with the following exception: " + e);
            return false;
        }
    }

    /**
     * Disconnect to remote machine and destroy session.
     *
     * @return if disconnect successfully, return true, else return false
     */
    public Boolean disconnect() {
        try {
            System.out.println("Closing SSH connection.");
            session.disconnect();
            session = null;
            iExitStatus = 0;
            System.out.println("SSH connection closed successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("Disconnect fails with the following exception: " + e.toString());
            return false;
        }
    }

    /**
     * Execute task on remote machine
     *
     * @param sCommand
     *            - Task object that extends from CustomCode
     * @throws Exception
     */
    public synchronized String exec(String sCommand)  {
        String sRetVal = "";
        try {
            channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(sCommand);

            channel.setInputStream(null);

            channel.setOutputStream(System.out);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            System.out.println("Connection channel established successfully");
            System.out.println("Start to run command: " + sCommand);

            StringBuilder sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    String str = new String(tmp, 0, i);
                    sb.append(str);
                    System.out.print(str);

                }
                if (channel.isClosed()) {
                    System.out.println("Connection channel closed");
                    //logger.putMsg(Logger.INFO,"Exit-status: " + channel.getExitStatus());
                    System.out.println("Check if exec success or not ... ");
                    iExitStatus = channel.getExitStatus();
                    //r.sysout = sb.toString();
                    if (iExitStatus == 0) {
                        System.out.println("Execute successfully for command: " + sCommand);

                    } else {

                        System.out.println("Execution failed while executing command: " + sCommand);
                        System.out.println("Error message: " + ((ChannelExec) channel).getErrStream());
                    }
                    break;
                }
            }
            channel.disconnect();
            sRetVal = sb.toString();
        }
        catch (Exception e) {
        }
        return sRetVal;
    }

}
