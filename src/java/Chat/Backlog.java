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

public class Backlog {

    // singleton pattern
    private static Backlog backlogInstance = new Backlog();
    // full backlog - contains ALL messages, broadcast, group & private
    private final List<Message> backlog;
    // broadcast message backlog
    private final List<Message> broadcastBacklog;
    private final List<Observer> observers;
    private final List<Group> groups;
    private final List<User> users;

    public static Backlog getInstance() {
        return backlogInstance;
    }

    protected Backlog() {
        this.backlog = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.users = new ArrayList<>();
        this.broadcastBacklog = new ArrayList<>();
    }

    // synchronized is used to enforce thread safety
    public synchronized void register(Observer observer) throws ObserverException {
        if (observers.contains(observer)) {
            throw new ObserverException("Observer already registered");
        } else {
            // notify observers on user login
            for (Observer o : observers) {
                o.update(observer.toString() + " has joined.");
            }
            observers.add(observer);
        }
    }

    // synchronized is used to enforce thread safety
    public synchronized void unregister(Observer observer) throws ObserverException {
        if (observers.contains(observer)) {
            // notify observers on user logout
            for (Observer o : observers) {
                o.update(observer.toString() + " has left.");
            }
            observers.remove(observer);
        } else {
            throw new ObserverException("Observer not registered!");
        }
    }

    // synchronized is used to enforce thread safety
    public synchronized void addMessage(Message msg) {
        // add entry to history
        backlog.add(msg);
        // add entry to user backlog
        getSingleUser(msg.getFromUserId()).getUserBacklog().add(msg);
        // notify observers about a new entry
        switch (msg.getChannel()) {
            case CHANNEL_PRIVATE:
                getSingleUser(msg.getToUserId()).getUserBacklog().add(msg);
                break;
            case CHANNEL_GROUP:
                // first get all groups from the message "header"
                Group group = getSingleGroup(msg.getToGroupId());
                group.addToGroupBacklog(msg);
                // then get all users from the group list
                for (User user : group.getUsers()) {
                    // notify respective users
                    user.update(msg);
                }
                break;
            default:
                // broadcast message
                // add it broadcast backlog
                broadcastBacklog.add(msg);
                // update all observers
                for (Observer o : observers) {
                    o.update(msg);
                }
                break;
        }
    }

    // complete server backlog
    public List<Message> getFullBacklog() {
        return backlog;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public User getSingleUser(int id) {
        for (User u : users) {
            if (u.getUserId() == id) {
                return u;
            }
        }
        return null;
    }

    public Group getSingleGroup(int id) {
        for (Group g : groups) {
            if (g.getGroupId() == id) {
                return g;
            }
        }
        return null;
    }

    public Message getSingleMessage(int id) {
        for (Message m : backlog) {
            if (m.getMessageId() == id) {
                return m;
            }
        }
        return null;
    }

    public synchronized List<Message> getBroadcastBacklog() {
        return broadcastBacklog;
    }

    public List<Message> getGroupBacklog(int groupid) {
        for (Group group : groups) {
            if (group.getGroupId() == groupid) {
                return group.getGroupBacklog();
            }
        }
        return null;
    }

    public List<Message> getMessagesByUserID(int id) {
        for (int i = 0; i < observers.size(); i++) {
            User user = (User) observers.get(i);
            if (user.getUserId() == id) {
                return user.getUserBacklog();
            }
        }
        return null;
    }

}
