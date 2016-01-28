package APIResources;

import Sports.SportsWorld;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/leagues")
public class LeaguesResource {

    private final SportsWorld world;

    public LeaguesResource() {
        this.world = SportsWorld.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getLeaguesXML() {
        return Response.ok().entity(world.getLeagues()).build();
    }
    
    @Path("/{leagueid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getLeagueXML(@PathParam("leagueid") int leagueid) {
        return Response.ok().entity(world.getLeagues().get(leagueid)).build();
    }

    /*
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getLeaguesXML() {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<leagues>";
        for (int i = 0; i < world.getLeagues().size(); i++) {
            result += "<leagueid>" + i + "</leagueid>";
        }
        result += "</leagues>";
        return result;
    }

    @Path("/{leagueid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getLeagueXML(@PathParam("leagueid") int leagueid) {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<league>";
        result += "<name>";
        result += world.getLeagues().get(leagueid).getName();
        result += "</name>";
        result += "<teams>";
        for (int i = 0; i < world.getLeagues().get(leagueid).getTeams().size(); i++) {
            result += "<teamid>" + i + "</teamid>";
        }
        result += "</teams>";
        result += "</league>";
        return result;
    }

    */
}
