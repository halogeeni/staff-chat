/*
 * The MIT License
 *
 * Copyright 2016 aleksirasio.
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

/**
 *
 * @author aleksirasio
 */
public class TestChat {

    private final List<Group> grouplist;
    private final List<User> userlist;
    private static TestChat chat;

    public static TestChat getInstance() {
        if (chat == null) {
            chat = new TestChat();
        }
        
        return chat;
    }
    
    public TestChat() {
        Backlog backlog = Backlog.getInstance();
    
        Group admins = new Group("Administrators");
        Group standard = new Group("Standard users");
        Group staff = new Group("Staff");

        grouplist = new ArrayList<>();
        userlist = new ArrayList<>();
        
        grouplist.add(admins);
        grouplist.add(standard);
        grouplist.add(staff);
        
        User user1 = new User("Aleksi", "Rasio", "halogeeni", (List<Group>) admins);
        User user2 = new User("Joona", "Vainikka", "empurdia", (List<Group>) standard);
        User user3 = new User("Oskar", "Gusgård", "tunkio", (List<Group>) staff);
        User user4 = new User("Joel", "Vainikka", "pulla", (List<Group>) standard);
    
        userlist.add(user1);
        userlist.add(user2);
        userlist.add(user3);
        userlist.add(user4);
        
        backlog.register(user1);
        backlog.register(user2);
        backlog.register(user3);
        backlog.register(user4);
        
        Message message1 = 
                new Message(user1, Channel.CHANNEL_BROADCAST, null, null, 
                        new MessageBody("Hei kaikki! Hejsan!"));
        backlog.addMessage(message1);
        
        Message message2 = 
                new Message(user2, Channel.CHANNEL_GROUP, null, (List<Group>) standard, 
                        new MessageBody("Lissu hei! Tuo kahvia T: MARTTA"));
        backlog.addMessage(message2);
        
        Message message3 = 
                new Message(user3, Channel.CHANNEL_PRIVATE, user2, null, 
                        new MessageBody("Martta! Kahvit loppu toista päivää -LISSU"));
        backlog.addMessage(message3);
        
        Message message4 = 
                new Message(user4, Channel.CHANNEL_BROADCAST, null, null, 
                        new MessageBody("Ostakaa joku kahvia, pliis."));
        backlog.addMessage(message4);
    
    }
    
}
