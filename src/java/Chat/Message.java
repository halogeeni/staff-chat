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

import java.util.Date;
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
    private Integer toGroupId;
    private Group toGroup;

    private final Date date = new Date();

    // message body (actual content: text, image ...)
    private MessageBody body;

    public Message() {
        this.timestamp = date.getTime();
    }

    public Message(User fromUser, Channel channel, User toUser, Group toGroup, MessageBody body) {
        switch (channel) {
            case CHANNEL_GROUP:
                this.toGroup = toGroup;
                this.toUserId = null;
                break;
            case CHANNEL_BROADCAST:
                this.toGroup = null;
                this.toUserId = null;
                break;
            default:
                this.toGroup = null;
                this.toUserId = toUser.getUserId();
                break;
        }

        // message "publicity"
        this.channel = channel;

        // sender
        this.fromUserId = fromUser.getUserId();

        // the message data itself, text or image
        this.body = body;

        // set timestamp
        this.timestamp = date.getTime();

        // set unique message id and increment counter
        this.messageId = idCounter++;

        if (toGroup == null) {
            this.toGroupId = null;
        } else {
            this.toGroupId = toGroup.getGroupId();
        }

    }

    // getters & setters
    @XmlElement
    public int getMessageId() {
        return messageId;
    }

    // setter workaround for jersey
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

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

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
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Group getToGroup() {
        return toGroup;
    }

    @XmlElement
    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    @XmlElement
    public Integer getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Integer toGroupId) {
        this.toGroupId = toGroupId;
    }

}
