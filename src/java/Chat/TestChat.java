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
package Chat;

import java.util.ArrayList;
import java.util.List;

public class TestChat {

    private static TestChat chat;

    public static TestChat getInstance() {
        if (chat == null) {
            chat = new TestChat();
        }

        return chat;
    }

    private final Backlog backlog;

    public Backlog getBacklog() {
        return backlog;
    }

    public TestChat() {
        backlog = Backlog.getInstance();

        Group admins = new Group("Administrators");
        Group standard = new Group("Standard users");
        Group staff = new Group("Staff");

        // 0
        backlog.getGroups().add(admins);
        // 1
        backlog.getGroups().add(standard);
        // 2
        backlog.getGroups().add(staff);

        List<Group> adminGroupList = new ArrayList<>();
        List<Group> standardGroupList = new ArrayList<>();
        List<Group> staffGroupList = new ArrayList<>();


  
        adminGroupList.add(backlog.getGroups().get(0));
        standardGroupList.add(backlog.getGroups().get(1));
        staffGroupList.add(backlog.getGroups().get(2));

        // create users
        User user1 = new User("Aleksi", "Rasio", "halogeeni", adminGroupList);
        User user2 = new User("Joona", "Vainikka", "empurdia", standardGroupList);
        User user3 = new User("Oskar", "Gusgård", "tunkio", staffGroupList);
        User user4 = new User("Joel", "Vainikka", "pulla", standardGroupList);

        // add users to groups (server side)
        backlog.getGroups().get(0).getUsers().add(user1);
        backlog.getGroups().get(1).getUsers().add(user2);
        backlog.getGroups().get(1).getUsers().add(user4);
        backlog.getGroups().get(2).getUsers().add(user3);

        // register observers
        backlog.register(user1);
        backlog.register(user2);
        backlog.register(user3);
        backlog.register(user4);

        // create messages
        Message message1
                = new Message(user1, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Hei kaikki! Hejsan!"));
        backlog.addMessage(message1);

        Message message2
                = new Message(user2, Channel.CHANNEL_GROUP, null, standardGroupList,
                        new MessageBody("Lissu hei! Tuo kahvia T: MARTTA"));
        backlog.addMessage(message2);

        Message message3
                = new Message(user3, Channel.CHANNEL_PRIVATE, user2, null,
                        new MessageBody("Martta! Kahvit loppu toista päivää -LISSU"));
        backlog.addMessage(message3);

        Message message4
                = new Message(user4, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Ostakaa joku kahvia, pliis."));
        backlog.addMessage(message4);

        Message message5
                = new Message(user4, Channel.CHANNEL_BROADCAST, null, null,
                        new MessageBody("Missä se kahvi viipyy! Pitäs olla jo!"));
        backlog.addMessage(message5);

    }

}
