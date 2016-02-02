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
package APIResources;

import Chat.Message;
import Chat.TestChat;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Oskar
 */
@Path("/messages")
public class MessageResource {
    
    private final TestChat tc;
    
    public MessageResource() {
        this.tc = TestChat.getInstance();
    }

    //return all messages in the history
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMessagesXML() {
        List<Message> messages = tc.getBacklog().getFullBacklog();
        GenericEntity<List<Message>> list = new GenericEntity<List<Message>>(messages) {};
        return Response.ok(list).build();
    }
    
    //gets only a single message that matches the id given
    @Path("/{messageid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getMessageXML(@PathParam("messageid") int messageid) {
        return Response.ok().entity(tc.getBacklog().getSingleMessage(messageid)).build();
    }
    
}