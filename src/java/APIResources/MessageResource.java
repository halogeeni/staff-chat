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
package APIResources;

import Chat.Message;
import Chat.TestChat;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringEscapeUtils;

@Path("/messages")
public class MessageResource {

    private final TestChat chatInstance;

    public MessageResource() {
        this.chatInstance = TestChat.getInstance();
    }

    // return ALL messages in backlog
    // this should be secured!
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMessagesXML() {
        List<Message> messages = chatInstance.getBacklog().getFullBacklog();
        GenericEntity<List<Message>> list = 
                new GenericEntity<List<Message>>(messages) {
        };
        return Response.ok(list).build();
    }

    // return broadcast message backlog
    @Path("/broadcast")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getBroadcastMessagesXML() {
        List<Message> messages = chatInstance.getBacklog().getBroadcastBacklog();
        GenericEntity<List<Message>> list = 
                new GenericEntity<List<Message>>(messages) {
        };
        return Response.ok(list).build();
    }
    
    // return group messages made by single group, by id
    @Path("/group/{groupid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getGroupMessagesXML(@PathParam("groupid") int groupid) {
        List<Message> messages = chatInstance.getBacklog().getGroupBacklog(groupid);
        
        // no messages found with id --> return 404
        if(messages == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // messages found --> create list and build response
        GenericEntity<List<Message>> list = 
                new GenericEntity<List<Message>>(messages) {};
        return Response.ok(list).build();
    }

    // create a new message
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void postMessageXML(Message msg) {
        // escape HTML via Apache Commons library
        msg.getBody().setText(StringEscapeUtils.escapeHtml4(msg.getBody().getText()));
        chatInstance.getBacklog().addMessage(msg);
    }

    // get a single message that matches the id given
    @Path("/messageid/{messageid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMessageXML(@PathParam("messageid") int messageid) {
        Message msg = chatInstance.getBacklog().getSingleMessage(messageid);
        
        // no message found with id --> return 404
        if (msg == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok().entity(msg).build();
    }

    // get all messages sent by the user
    @Path("/userid/{userid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMessagesUserXML(@PathParam("userid") int userid) {
        List<Message> messages = chatInstance.getBacklog().getMessagesByUserID(userid);
        
        // no messages found with id --> return 404
        if(messages == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        GenericEntity<List<Message>> list = 
                new GenericEntity<List<Message>>(messages) {};
        return Response.ok(list).build();
    }
    
    @Path("/{userid}/private/{associateduserid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getPrivateMessagesXML(@PathParam("userid") int userid, 
            @PathParam("associateduserid") int associatedUserId) {
        
        // get private messages with associated user
        List<Message> messages = 
                chatInstance.getBacklog().getSingleUser(userid).getPrivateMessages(associatedUserId);

        // no messages found with id --> return 404
        if(messages == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        GenericEntity<List<Message>> list = 
                new GenericEntity<List<Message>>(messages) {};
        return Response.ok(list).build();
    }
}
