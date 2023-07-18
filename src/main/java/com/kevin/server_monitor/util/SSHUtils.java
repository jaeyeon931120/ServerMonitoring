package com.kevin.server_monitor.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class SSHUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    int keysize = 2048;
    int keytype = com.jcraft.jsch.KeyPair.RSA;
    String keyPath =  System.getProperty("user.dir") + "\\src\\main\\resources\\keyfile";
    String privateName = URLEncoder.encode("priv.pem", StandardCharsets.UTF_8);
    String osKeyPath = keyPath.replace("/", Matcher.quoteReplacement(File.separator));
    String reverseSlashKeyPath = osKeyPath.replaceAll(Matcher.quoteReplacement(File.separator), "/");
    String slash = Matcher.quoteReplacement(File.separator);

    public String sshCommunication(String username, String password, String ip, int port, String command) {

        Session session = null;
        ChannelExec channel = null;
        String result = null;

        logger.info("server_info => username : {}, password : {}, ip : {}, port : {}, command : {}",
                username, password, ip, port, command);

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

    private Map<String, Object> sshKeypairToJsh() throws Exception {
        Map<String, Object> result = new HashMap<>();

        ByteArrayOutputStream bytePrivateKey = new ByteArrayOutputStream(); // 개인키
        ByteArrayOutputStream bytePublicKey = new ByteArrayOutputStream(); // 공개키

        JSch jSch = new JSch();
        String commnet = "";

        com.jcraft.jsch.KeyPair kpair = com.jcraft.jsch.KeyPair.genKeyPair(jSch, keytype, keysize);
        kpair.writePrivateKey(bytePrivateKey);
        kpair.writePublicKey(bytePublicKey, commnet);

        result.put("priv_key", bytePrivateKey);
        result.put("pub_key", bytePublicKey);

        kpair.dispose();

        return result;
    }

    private void createSSHKeyfileJsch(String filePath, String privateKeyString) {
        File file = new File(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(file);

            ByteArrayOutputStream bpriv = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bpriv);
            dos.write(privateKeyString.getBytes());

            bpriv.close();
            bpriv.flush();

            fos.write(bpriv.toByteArray());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String mkdir(ChannelSftp sftpchannel) throws SftpException {
        String infodir = "home/check_server_info";
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

    private void keyCreate() {
        SSHUtils work = new SSHUtils();

        try {
            Map<String, Object> keyMap = work.sshKeypairToJsh();
            String priv_key = keyMap.get("priv_key").toString();
            String pub_key = keyMap.get("pub_key").toString();

            String privKeyFullPath = reverseSlashKeyPath + slash + "priv.pem";
            String pubKeyFullPath = reverseSlashKeyPath + slash + "pub.pem";

            work.createSSHKeyfileJsch(privKeyFullPath, priv_key);
            work.createSSHKeyfileJsch(pubKeyFullPath, pub_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String commandLineResult(ChannelExec channel, String ip, int port) {
        String result = null;

        try(BufferedReader commandReader = new BufferedReader(new InputStreamReader(channel.getInputStream()))) {
            String commandLine = commandReader.readLine();

            logger.info("commandLine : {}", commandLine);
            if (commandLine.length() != 0) {
                result = commandLine;
            } else {
                result = "nok";
            }
        } catch (Exception e) {
            logger.error("서버의 응답정보를 수신중에 오류가 발생했습니다.(실패한 서버 IP : {}, PORT : {})", ip, port);
            e.printStackTrace();
        }

        return result;
    }

    public void sftpCommunication(String username, String password, String ip, int port, String process) {

        keyCreate();
        JSch jsch = new JSch();
        Channel channel = null;
        ChannelSftp sftpchannel;
        Session session = null;
        String filedir = System.getProperty("user.dir") + "\\src\\main\\resources\\shellfile";
        String fileName = URLEncoder.encode("server_monitoring", StandardCharsets.UTF_8);
        String OsFilePath = filedir.replace("/", Matcher.quoteReplacement(File.separator));
        String reverseSlashPath = OsFilePath.replaceAll(Matcher.quoteReplacement(File.separator), "/");

        logger.info("sftp 유저정보 : username: {}, password: {}, ip : {}, port: {}", username, password, ip, port);

        File file = new File(reverseSlashPath + slash + fileName);

        try(InputStream in = new FileInputStream(file)) {
            jsch.addIdentity(reverseSlashKeyPath + slash + privateName);
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            sftpchannel = (ChannelSftp) channel;
            sftpchannel.cd("..");

            if(process.equals("insert")) {
                String infodir = mkdir(sftpchannel);

                logger.info("infodir : {}", infodir);

                sftpchannel.cd(infodir);
                sftpchannel.put(in, fileName);
                sftpchannel.chmod(777, infodir + fileName);
            } else if(process.equals("delete")) {
                sftpchannel.cd("/home/check_server_info");
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
