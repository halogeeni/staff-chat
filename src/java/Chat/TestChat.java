/*
 * The MIT License
 *
 * Copyright 2016 Oskar Gusgård, Aleksi Rasio, Joel Vainikka, Joona Vainikka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

// THIS IS REDUNDANT -
// WAS USED IN DEVELOPMENT TO POPULATE THE CHAT INSTANCE

package Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestChat {

    private static TestChat chat;

    public static TestChat getInstance() {
        if (chat == null) {
            chat = new TestChat();
        }

        return chat;
    }

    private final ChatServer backlog;

    public ChatServer getBacklog() {
        return backlog;
    }

    public TestChat() {
        backlog = ChatServer.getInstance();

        Group admins = new Group("Administrators", true);
        Group standard = new Group("Standard users", true);
        Group staff = new Group("Staff", true);

        // 0
        backlog.getGroups().add(admins);
        // 1
        backlog.getGroups().add(standard);
        // 2
        backlog.getGroups().add(staff);

        List<Group> adminAndStaffGroupList = new ArrayList<>();
        List<Group> standardGroupList = new ArrayList<>();
        List<Group> staffGroupList = new ArrayList<>();

        adminAndStaffGroupList.add(backlog.getGroups().get(0));
        adminAndStaffGroupList.add(backlog.getGroups().get(1));
        standardGroupList.add(backlog.getGroups().get(1));
        staffGroupList.add(backlog.getGroups().get(2));

        // create users
        User user1 = new User("Aleksi", "Rasio", "halogeeni", "Overlord", adminAndStaffGroupList, true);
        User user2 = new User("Joona", "Vainikka", "empurdia", "CEO", standardGroupList, true);
        User user3 = new User("Oskar", "Gusgård", "tunkio", "Programmer", staffGroupList, true);
        User user4 = new User("Joel", "Vainikka", "pulla", "Visual Designer", standardGroupList, true);

        // add users to users-list (server side)
        backlog.getUsers().add(user1);
        backlog.getUsers().add(user2);
        backlog.getUsers().add(user3);
        backlog.getUsers().add(user4);

        // add users to groups (server side)
        
        backlog.getGroups().get(0).getUsers().add(user1);
        backlog.getGroups().get(0).getUserIds().add(user1.getUserId());
        
        backlog.getGroups().get(2).getUsers().add(user1);
        backlog.getGroups().get(2).getUserIds().add(user1.getUserId());
        
        backlog.getGroups().get(1).getUsers().add(user2);
        backlog.getGroups().get(1).getUserIds().add(user2.getUserId());
        
        backlog.getGroups().get(1).getUsers().add(user4);
        backlog.getGroups().get(1).getUserIds().add(user4.getUserId());
        
        backlog.getGroups().get(2).getUsers().add(user3);
        backlog.getGroups().get(2).getUserIds().add(user3.getUserId());
        
        // register observers
        try {
            backlog.register(user1);
            backlog.register(user2);
            backlog.register(user3);
            backlog.register(user4);
        } catch (Exception e) {
            System.err.println(e);
        }
        
        // create messages
        Message message1
                = new Message(user1, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Hei kaikki! Hejsan!"));
        backlog.addMessage(message1);

        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        Message message2
                = new Message(user2, Channel.CHANNEL_GROUP, null, standard,
                        new MessageBody("Lissu hei! Tuo kahvia T: MARTTA"));
        backlog.addMessage(message2);

        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        Message message3
                = new Message(user3, Channel.CHANNEL_PRIVATE, user2, null,
                        new MessageBody("Martta! Kahvit loppu toista päivää -LISSU"));
        backlog.addMessage(message3);

        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        Message message4
                = new Message(user4, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Ostakaa joku kahvia, pliis."));
        backlog.addMessage(message4);

        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        Message message5
                = new Message(user4, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Missä se kahvi viipyy! Pitäs olla jo!"));
        backlog.addMessage(message5);

        try {
            Thread.sleep(2);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        Message message6
                = new Message(user1, Channel.CHANNEL_GROUP, null, staff,
                        new MessageBody("Kahvia!!! HETI! T: MARTTA"));
        backlog.addMessage(message6);

    }

}
