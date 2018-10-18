package com.liveramp.java_support.alerts_handler;

import java.util.List;

import com.google.common.collect.Lists;

public interface MailBuffer {

  void add(MailOptions mail);

  List<MailOptions> get();

  MailBuffer NULL = new MailBuffer() {
    @Override
    public void add(MailOptions mail) {
      //  no-op
    }

    @Override
    public List<MailOptions> get() {
      return Lists.newArrayList();
    }
  };

  class ListBuffer implements MailBuffer {

    List<MailOptions> sentMails = Lists.newArrayList();

    @Override
    public void add(MailOptions mail) {
      //Prevent an overflow if this ever gets to production
      if (sentMails.size() < 1000) {
        sentMails.add(mail);
      } else {
        System.out.println("Reached 1000 emails, MailerHelper will stop keeping emails from now on");
      }
    }

    @Override
    public List<MailOptions> get() {
      return sentMails;
    }
  }

}
