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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User implements Observer {

    private static int idCounter = 0;

    // unique user id
    private int userId;

    // Personal information
    private String firstname, lastname, username;

    // list of groups the user is involved in
    // REDUNDANT
    private final List<Group> groups;

    // List of groupID's the user is involved in
    private final List<Integer> groupIds;

    // User message backlog
    private final List<Message> userBacklog;

    public User() {
        this.groupIds = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.userBacklog = new ArrayList<>();
    }

    public User(String firstname, String lastname, String username, List<Group> groups) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.userId = idCounter++;
        this.groups = groups;
        this.userBacklog = new ArrayList<>();
        this.groupIds = new ArrayList<>();

        // Adds all groupIDs into array
        for (Group g : groups) {
            groupIds.add(g.getGroupId());
        }
    }

    // Message received
    @Override
    public void update(Message msg) {
        System.out.println("DEBUG: Message received.");
    }

    // Server message received
    @Override
    public void update(String msg) {
        System.out.println("DEBUG - SERVER MESSAGE: " + msg);
    }

    // Setters & getters
    @XmlElement
    public int getUserId() {
        return userId;
    }

    @XmlElement
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @XmlElement
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Group> getGroups() {
        return groups;
    }

    @XmlElement
    public List<Integer> getGroupIds() {
        return groupIds;
    }

    public List<Message> getUserBacklog() {
        return userBacklog;
    }

    // dummy id parameter
    // workaround because of zero-arg constructor
    public void setUserId(int userId) {
        this.userId = idCounter++;
    }

}
