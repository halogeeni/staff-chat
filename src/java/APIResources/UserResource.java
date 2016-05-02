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

import Chat.ChatServer;
import Chat.User;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    private final ChatServer chatInstance;

    public UserResource() {
        this.chatInstance = ChatServer.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getUsersXML() {
        List<User> users = chatInstance.getUsers();
        GenericEntity<List<User>> list = new GenericEntity<List<User>>(users) {
        };
        return Response.ok(list).build();
    }

    @Path("/{userid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getUserXML(@PathParam("userid") int userid) {
        User usr = chatInstance.getSingleUser(userid);
        if (usr == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(usr).build();
    }

    @Path("/{userid}")
    @DELETE
    @Produces({MediaType.TEXT_HTML})
    public Response inactivateUserXML(@PathParam("userid") int userid) {
        User usr = chatInstance.getSingleUser(userid);

        if (usr == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        usr.setActive(false);
        return Response.status(Response.Status.OK).build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void postUserXML(User user) {

        // add user to the main 'user pool'
        chatInstance.getUsers().add(user);

        for (Integer groupid : user.getGroupIds()) {
            if (groupid != null) {
                chatInstance.getSingleGroup(groupid).getUsers().add(user);
                // add user's id to group's collection of user ids
                chatInstance.getSingleGroup(groupid).getUserIds().add(user.getUserId());
            }
        }
    }

}
