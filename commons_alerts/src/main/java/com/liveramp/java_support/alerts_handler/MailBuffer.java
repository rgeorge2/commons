package com.liveramp.java_support.alerts_handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
      return Collections.emptyList();
    }
  };

  class ListBuffer implements MailBuffer {

    List<MailOptions> sentMails = new ArrayList<>();

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
