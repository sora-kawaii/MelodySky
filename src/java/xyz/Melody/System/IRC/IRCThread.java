package xyz.Melody.System.IRC;

import xyz.Melody.Client;

public class IRCThread extends Thread {
   public void run() {
      Client.instance.irc.connect();

      while(!Client.instance.ircExeption && !Client.stopping && !Client.instance.irc.shouldThreadStop) {
         Client.instance.irc.handleInput();
      }

   }
}
