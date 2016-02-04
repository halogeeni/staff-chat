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

import Chat.Group;
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

@Path("/groups")
public class GroupResource {

    private final TestChat thischat;

    public GroupResource() {
        this.thischat = TestChat.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getGroupsXML() {

        List<Group> groups = thischat.getBacklog().getGroups();
        GenericEntity<List<Group>> list
                = new GenericEntity<List<Group>>(groups) {
        };
        return Response.ok(list).build();
    }

    @Path("/{groupid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getGroupXML(@PathParam("groupid") int groupid) {
        return Response.ok().entity(thischat.getBacklog().getSingleGroup(groupid)).build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void postGroupXML(Group group) {
        thischat.getBacklog().getGroups().add(group);
    }

}
