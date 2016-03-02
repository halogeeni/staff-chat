/*
 * The MIT License
 *
 * Copyright 2016 Oskar.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Oskar
 */
public class ChatServerTest {

    public ChatServerTest() {

    }

    @BeforeClass
    public static void setUpClass() {

        System.out.println("In class set up");
        ChatServer backlog = ChatServer.getInstance();

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
        backlog.getGroups().get(2).getUsers().add(user1);
        backlog.getGroups().get(1).getUsers().add(user2);
        backlog.getGroups().get(1).getUsers().add(user4);
        backlog.getGroups().get(2).getUsers().add(user3);

        // register observers
        try {
            backlog.register(user1);
            backlog.register(user2);
            backlog.register(user3);
            backlog.register(user4);

        } catch (ObserverException ex) {
            Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
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
                        new MessageBody("Täällä on joku pappa maassa; tulkaapi nostamaan! T: MARTTA"));
        backlog.addMessage(message6);

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of register method, of class ChatServer.
     */
    @Test(expected = ObserverException.class)
    public void testRegisterFalse() throws ObserverException {
        System.out.println("register");
        ChatServer instance = ChatServer.getInstance();
        User user = instance.getSingleUser(3);
        Observer observer = user;

        //observer will be added even though the user is already an observer (arraylist)
        instance.register(observer);

    }

    @Test
    public void testRegisterTrue() {
        System.out.println("register");

        List<Group> adminAndStaffGroupList = new ArrayList<>();
        ChatServer instance = ChatServer.getInstance();
        List<Observer> observers = instance.getObservers();
        adminAndStaffGroupList.add(ChatServer.getInstance().getGroups().get(0));
        int observerCount = observers.size();

        User user = new User("Jorma", "Jormanen", "asdfgh", "intern", adminAndStaffGroupList, true);

        Observer observer = user;
        System.out.println("Amount of observers: " + observers.size());

        try {
            //observer will be added even though the user is already an observer (arraylist)
            instance.register(observer);
        } catch (ObserverException ex) {
            Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("Amount after registering one observer: " + observers.size());

        int result = observers.size();

        assertEquals(observerCount + 1, result);

    }

    /**
     * Test of unregister method, of class ChatServer.
     */
    @Test
    public void testUnregisterTrue() {
        System.out.println("unregister");
        ChatServer instance = ChatServer.getInstance();
        Observer observer = instance.getSingleUser(0);
        List<Observer> observers = instance.getObservers();
        int observerCount = observers.size();

        System.out.println("Amount of observers: " + observers.size());

        try {
            instance.unregister(observer);
        } catch (ObserverException ex) {
            Logger.getLogger(ChatServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Amount after unregistering one observer: " + observers.size());

        int result = observers.size();

        assertEquals(observerCount - 1, result);
    }

    /**
     * Test of addMessage method, of class ChatServer.
     */
    @Test
    public void testAddMessage() {
        System.out.println("addMessage");
        User usr = ChatServer.getInstance().getSingleUser(1);

        Message msg = new Message(usr, Channel.CHANNEL_BROADCAST, null, null,
                new MessageBody("Martta! Kahvit loppu toista päivää -LISSU"));
        ChatServer instance = ChatServer.getInstance();
        instance.addMessage(msg);
        assertEquals(msg, ChatServer.getInstance().getSingleMessage(msg.getMessageId()));
    }

    /**
     * Test of getFullBacklog method, of class ChatServer.
     */
    @Test
    public void testGetFullBacklog() {
        System.out.println("getFullBacklog");
        ChatServer instance = ChatServer.getInstance();
        //List<Message> expResult = instance.getFullBacklog();
        // List<Message> result =
        int expResult = instance.getFullBacklog().size();
        int result = 8;
        /*
         for(int i=0;i<=instance.getFullBacklog().size()-1;i++){
             System.out.println(instance.getFullBacklog().get(i));
             System.out.println(instance.getFullBacklog().get(i).getBody().getText());
         }
         */

        assertEquals(expResult, result);
    }

    /**
     * Test of getObservers method, of class ChatServer.
     */
    @Test
    public void testGetObservers() {
        System.out.println("getObservers");
        ChatServer instance = ChatServer.getInstance();
        //List<Observer> expResult = null;
        // List<Observer> result = instance.getObservers();

        //All the users are added to the observers arraylist, so they should have the same size
        //One user is created in the tests, so we need to -1 from the expResult
        int expResult = instance.getUsers().size() - 1;
        int result = instance.getObservers().size();
        assertEquals(expResult, result);

    }

    /**
     * Test of getUsers method, of class ChatServer.
     */
    @Test
    public void testGetUsers() {
        System.out.println("getUsers");
        ChatServer instance = ChatServer.getInstance();
        //List<User> expResult = null;
        //List<User> result = instance.getUsers();
        int expResult = 5;
        int result = instance.getUsers().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGroups method, of class ChatServer.
     */
    @Test
    public void testGetGroups() {
        System.out.println("getGroups");
        ChatServer instance = ChatServer.getInstance();
        //List<Group> expResult = instance.getGroups().size();
        int expResult = instance.getGroups().size();
        // List<Group> result = instance.getGroups().size;
        int result = 4;
        assertEquals(expResult, result);
    }

    /**
     * Test of getSingleUser method, of class ChatServer.
     */
    @Test
    public void testGetSingleUser() {
        System.out.println("getSingleUser");
        ChatServer instance = ChatServer.getInstance();

        List<Group> standardGroupList = new ArrayList<>();

        standardGroupList.add(instance.getGroups().get(1));

        User usr = new User("Testi", "Useri", "testuser", "intern", standardGroupList, true);
        instance.getUsers().add(usr);
        assertEquals(usr, instance.getSingleUser(usr.getUserId()));
    }

    /**
     * Test of getSingleGroup method, of class ChatServer.
     */
    @Test
    public void testGetSingleGroup() {
        System.out.println("getSingleGroup");
        ChatServer instance = ChatServer.getInstance();

        Group coffeeClub = new Group("Kahvikerho", true);
        // 2
        instance.getGroups().add(coffeeClub);

        List<Group> casual = new ArrayList<>();
        casual.add(instance.getGroups().get(3));

        Group expResult = instance.getGroups().get(3);
        Group result = instance.getSingleGroup(3);
        assertEquals(expResult, result);

    }

    /**
     * Test of getSingleMessage method, of class ChatServer.
     */
    @Test
    public void testGetSingleMessage() {

        System.out.println("getSingleMessage");
        ChatServer instance = ChatServer.getInstance();

        User user = instance.getSingleUser(0);
        Message msg = null;

        msg = new Message(user, Channel.CHANNEL_BROADCAST, null, null,
                new MessageBody("Lissu hei! Tuo kahvia T: MARTTA"));

        instance.addMessage(msg);

        Message expResult = msg;
        Message result = instance.getSingleMessage(msg.getMessageId());
        assertEquals(expResult, result);

    }

    /**
     * Test of getBroadcastBacklog method, of class ChatServer.
     */
    @Test
    public void testGetBroadcastBacklog() {
        System.out.println("getBroadcastBacklog");
        ChatServer instance = ChatServer.getInstance();
        //List<Message> expResult = null;
        // List<Message> result = instance.getBroadcastBacklog();
        int expResult = 4;
        int result = instance.getBroadcastBacklog().size();
        assertEquals(expResult, result);

    }

    /**
     * Test of getGroupBacklog method, of class ChatServer.
     */
    @Test
    public void testGetGroupBacklog() {
        System.out.println("getGroupBacklog");
        ChatServer instance = ChatServer.getInstance();
        Group group = instance.getSingleGroup(1);
        List<Message> expResult = group.getGroupBacklog();
        List<Message> result = instance.getGroupBacklog(1);
        assertEquals(expResult, result);

    }

    /**
     * Test of getMessagesByUserID method, of class ChatServer.
     */
    @Test
    public void testGetMessagesByUserID() {
        System.out.println("getMessagesByUserID");

        ChatServer instance = ChatServer.getInstance();
        User user = instance.getSingleUser(0);
        List<Message> expResult = user.getUserBacklog();
        List<Message> result = instance.getMessagesByUserID(0);
        assertEquals(expResult, result);

    }

}
