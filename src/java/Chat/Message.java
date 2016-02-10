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
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

    // message id counter
    private static int idCounter = 0;
    // unique message id
    // int datatype range ought to be enough!
    private int messageId;
    private final long timestamp;

    // sender, recipient(s) & publicity information
    private Channel channel;
    //private User fromUser, toUser;
    // Integer used instead of int, as broadcast & group messages have no userIds (=null)
    private Integer fromUserId, toUserId;
    private List<Group> toGroups;
    private List<Integer> toGroupIds;
    private final Date date = new Date();

    // message body (actual content: text, image ...)
    private MessageBody body;

    public Message() {
        this.timestamp = date.getTime();
        this.toGroups = new ArrayList<>();
        this.toGroupIds = new ArrayList<>();
    }

    public Message(User fromUser, Channel channel, User toUser, List<Group> groups, MessageBody body) {
        switch (channel) {
            case CHANNEL_GROUP:
                this.toGroups = groups;
                this.toUserId = null;
                //this.toUser = null;
                break;
            case CHANNEL_BROADCAST:
                // should we get all groups available from a singleton server?
                this.toGroups = null;
                this.toUserId = null;
                //this.toUser = null;
                break;
            default:
                this.toGroups = null;
                //this.toUser = toUser;
                this.toUserId = toUser.getUserId();
                break;
        }

        // message "publicity"
        this.channel = channel;

        // sender
        //this.fromUser = fromUser;
        this.fromUserId = fromUser.getUserId();

        // the message data itself, text or image
        this.body = body;

        // set timestamp
        this.timestamp = date.getTime();

        // set unique message id and increment counter
        // TODO synchronization? could two messages get the same id now?
        this.messageId = idCounter++;

        this.toGroupIds = new ArrayList<>();

        if (!(groups == null) && !groups.isEmpty()) {
            for (Group group : groups) {
                this.toGroupIds.add(group.getGroupId());
            }
        }
    }

    // getters & setters
    
    @XmlElement
    public int getMessageId() {
        return messageId;
    }

    // dummy setter workaround for jersey
    public void setMessageId(int messageId) {
        this.messageId = idCounter++;
    }

    @XmlElement
    public long getTimestamp() {
        return timestamp;
    }

    @XmlElement
    public Channel getChannel() {
        return channel;
    }
    public Channel getCHANNEL(){
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /*
    
    public User getFromUser() {
        return fromUser;
    }

    
    public User getToUser() {
        return toUser;
    }
    
    */

    @XmlElement
    public Integer getFromUserId() {
        return fromUserId;
    }
    
    @XmlElement
    public Integer getToUserId() {
        return toUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
        //this.fromUser = Backlog.getInstance().getSingleUser(fromUserId);
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
        //this.toUser = Backlog.getInstance().getSingleUser(toUserId);
    }

    /*

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    */

    public List<Group> getToGroups() {
        return toGroups;
    }

    @XmlElement
    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    @XmlElement
    public List<Integer> getToGroupIds() {
        return toGroupIds;
    }

    public void setToGroupIds(List<Integer> toGroupIds) {
        this.toGroupIds = toGroupIds;
    }

}
