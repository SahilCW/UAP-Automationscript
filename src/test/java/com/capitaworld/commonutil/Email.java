package com.capitaworld.commonutil;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sahil Patel
 */
public class Email implements Serializable {

    private static final long serialVersionUID = 3L;

    private String subject, content;
    private List<String> receiver, cc, bcc, filePaths;

    public Email(String subject, String content, List<String> receiver) {
    	if (subject==null || content==null || receiver == null || receiver.isEmpty()) {
            throw new NullPointerException();
        }
        this.subject = subject;
        this.content = content;
        this.receiver = receiver;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCc() {
        return cc;
    }

    public Email setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public Email setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public Email setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
        return this;
    }

    
}
