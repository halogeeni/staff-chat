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
import org.apache.commons.lang3.StringUtils;

@XmlRootElement
public class Group {

    private static int idCounter = 0;

    private int groupId;
    private String name;

    private final List<User> users;

    private List<Integer> userIds;
    private List<Message> backlog;

    private boolean active;

    public Group() {
        this.users = new ArrayList<>();
        this.userIds = new ArrayList<>();
        this.backlog = new ArrayList<>();
        this.active = true;
    }

    public Group(String name, boolean active) {
        this.name = name;
        this.users = new ArrayList<>();
        this.userIds = new ArrayList<>();
        this.groupId = idCounter++;
        this.backlog = new ArrayList<>();
        this.active = active;
    }

    public Group(String name, List<User> users, boolean active) {
        this.backlog = new ArrayList<>();
        this.name = name;
        this.users = users;
        this.userIds = new ArrayList<>();
        for (User u : users) {
            userIds.add(u.getUserId());
        }
        this.groupId = idCounter++;
        this.active = active;
    }

    // setters & getters  
    @XmlElement(name = "id")
    public int getGroupId() {
        return groupId;
    }

    //@XmlElement(name = "user")
    public List<User> getUsers() {
        return users;
    }

    @XmlElement(name = "userId")
    public List<Integer> getUserIds() {
        return userIds;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.substring(name, 0, 29);
    }

    public void setGroupId(int groupid) {
        this.groupId = idCounter++;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Message> getGroupBacklog() {
        return backlog;
    }

    public void addToGroupBacklog(Message msg) {
        backlog.add(msg);
    }

    @XmlElement
    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
