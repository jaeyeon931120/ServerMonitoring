package com.kevin.server_monitor.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class SSHUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    final String slash = Matcher.quoteReplacement(File.separator);

    public String sshControll(String username, String password, String ip, int port, String command) {

        Session session = null;
        ChannelExec channel = null;
        String result = null;

        try {
            session = new JSch().getSession(username, ip, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();

            result = commandLineResult(channel, ip, port);
        } catch (Exception e) {
            logger.error("서버와의 수신중에 오류가 발생했습니다.(실패한 서버 IP : {}, PORT : {})", ip, port);
            Thread.currentThread().interrupt();
            e.printStackTrace();
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

    private String mkdir(ChannelSftp sftpchannel) throws SftpException {
        String infodir = "/check_server_info";
        String[] pathArray = infodir.split("/");
        String currentDirectory = sftpchannel.pwd();
        StringBuilder totPathArray = new StringBuilder();

        for (String s : pathArray) {

            totPathArray.append(s).append("/");
            String currentPath = currentDirectory + totPathArray;
            try {
                sftpchannel.mkdir(currentPath);
                sftpchannel.cd(currentPath);
            } catch (Exception e) {
                sftpchannel.cd(currentPath);
            }
        }
        return currentDirectory + totPathArray;
    }

    public String commandLineResult(Channel channel, String ip, int port) {
        StringBuilder result = new StringBuilder();

        try(BufferedReader commandReader = new BufferedReader(new InputStreamReader(channel.getInputStream()))) {
            String commandLine;

            while ((commandLine = commandReader.readLine()) != null)
            {
                result.append(commandLine).append("\n");
            }
        } catch (Exception e) {
            logger.error("서버의 응답정보를 수신중에 오류가 발생했습니다.(실패한 서버 IP : {}, PORT : {})", ip, port);
            e.printStackTrace();
        }

        return result.toString();
    }

    public void sftpCommunication(String username, String password, String ip, int port, String process, String fileName) {

        JSch jsch = new JSch();
        Channel channel = null;
        ChannelSftp sftpchannel;
        Session session = null;
        String filedir = System.getProperty("user.dir") + "\\src\\main\\resources\\shellfile";
        String OsFilePath = filedir.replace("/", Matcher.quoteReplacement(File.separator));
        String reverseSlashPath = OsFilePath.replaceAll(Matcher.quoteReplacement(File.separator), "/");

        File file = new File(reverseSlashPath + slash + fileName);

        try(InputStream in = new FileInputStream(file)) {
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            sftpchannel = (ChannelSftp) channel;

            if(process.equals("insert")) {
                String infodir = mkdir(sftpchannel);

                sftpchannel.cd(infodir);
                ArrayList<ChannelSftp.LsEntry> filelist = new ArrayList<>(sftpchannel.ls(infodir));

                if(!filelist.isEmpty()) {
                    if(!filelist.toString().contains(fileName)) {
                        sftpchannel.put(in, fileName);
                        sftpchannel.chmod(777, infodir + fileName);
                    }
                } else {
                    sftpchannel.put(in, fileName);
                    sftpchannel.chmod(777, infodir + fileName);
                }
            } else if(process.equals("delete")) {
                sftpchannel.cd("/home/dev/check_server_info/");
                sftpchannel.rm("fileName");
                sftpchannel.cd("..");
                sftpchannel.rmdir("check_server_info");
            }
        } catch (Exception e) {
            logger.error("서버와의 SFTP 통신중에 오류가 발생했습니다.(실패한 서버 IP : {})", ip);
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
