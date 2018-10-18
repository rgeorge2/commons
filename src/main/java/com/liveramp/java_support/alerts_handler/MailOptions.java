package com.liveramp.java_support.alerts_handler;

import java.util.Arrays;
import java.util.List;

public class MailOptions {
  private static final String defaultContentType = "text/plain";

  private String fromEmail = null;
  private List<String> toEmails;
  private String subject;
  private List<String> tags;
  private String body;
  private String contentType;
  private List<String> ccEmails = null;
  private List<String> bccEmails = null;
  private List<String> attachments = null;

  public MailOptions(List<String> toEmails, String subject, String body, String... tags) {
    this.toEmails = toEmails;
    this.subject = subject;
    this.body = body;
    this.contentType = defaultContentType;
    this.tags = Arrays.asList(tags);
  }

  public MailOptions setFromEmail(String fromEmail) {
    this.fromEmail = fromEmail;
    return this;
  }

  public MailOptions setToEmails(List<String> toEmails) {
    this.toEmails = toEmails;
    return this;
  }

  public MailOptions setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public MailOptions setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public MailOptions setTags(String... tags) {
    this.tags = Arrays.asList(tags);
    return this;
  }

  public MailOptions setMsg(String body) {
    this.body = body;
    return this;
  }

  public MailOptions setMsgContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public MailOptions setCcEmails(List<String> ccEmails) {
    this.ccEmails = ccEmails;
    return this;
  }

  public MailOptions setBccEmails(List<String> bccEmails) {
    this.bccEmails = bccEmails;
    return this;
  }

  public MailOptions setAttachments(List<String> attachments) {
    this.attachments = attachments;
    return this;
  }

  public String getFromEmail() {
    return fromEmail;
  }

  public List<String> getToEmails() {
    return toEmails;
  }

  public String getSubject() {
    return subject;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getMsg() {
    return body;
  }

  public String getMsgContentType() {
    return contentType;
  }

  public List<String> getCcEmails() {
    return ccEmails;
  }

  public List<String> getBccEmails() {
    return bccEmails;
  }

  public List<String> getAttachments() {
    return attachments;
  }

  @Override
  public String toString() {
    return "MailOptions{" +
        "attachments=" + attachments +
        ", from='" + fromEmail + '\'' +
        ", to=" + toEmails +
        ", bcc=" + bccEmails +
        ", cc=" + ccEmails +
        ", subject='" + subject + '\'' +
        ", tags='" + tags + '\'' +
        ", body='" + body + '\'' +
        ", contentType='" + contentType + '\'' +
        '}';
  }
}
