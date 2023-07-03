package com.kevin.server_monitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileIo {
    protected final static Logger logger = LoggerFactory.getLogger(FileIo.class);
    private String fullPath = null;

    public FileIo() {
    	
    }
    public FileIo(final String path) {
        fullPath = path;
    }

    FileReader oneLineReader = null;
    BufferedReader oneLineBufferReader = null;

    public String readOneLine() throws Exception {
        if (oneLineReader == null) {
            oneLineReader = new FileReader(fullPath);
            oneLineBufferReader = new BufferedReader(oneLineReader);
        }
        String oneLine = null;
        oneLine = oneLineBufferReader.readLine();
        if (null == oneLine) {
            oneLineReader.close();
            oneLineBufferReader.close();
            final FileReader oneLineReader = null;
            final BufferedReader oneLineBufferReader = null;
        }
        return oneLine;
    }
}