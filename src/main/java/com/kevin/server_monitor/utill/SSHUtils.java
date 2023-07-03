package com.kevin.server_monitor.utill;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class SSHUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public String sshCommunication(String username, String password, String ip, int port, String command) throws Exception {

        Session session = null;
        ChannelExec channel = null;
        String responseString = "";
        StringBuilder resultStr = new StringBuilder();
        String result = "";

        try {
            session = new JSch().getSession(username, ip, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            responseString = Arrays.toString(responseStream.toByteArray());
            String[] repstr = responseString.replace("[", "").replace("]", "").split(",");
            if (repstr.length != 0) {
                for(String rep : repstr) {
                    rep = rep.trim();
                    int repint = Integer.parseInt(rep);
                    String word = Character.toString(repint);
                    resultStr.append(word);
                }
                result = resultStr.toString();
                logger.info("resultStr : " +resultStr);
            } else {
                result = "nok";
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return result;
    }
}
