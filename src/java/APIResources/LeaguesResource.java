package APIResources;

import Sports.SportsWorld;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("leagues")
public class LeaguesResource {

    private final SportsWorld world;

    public LeaguesResource() {
        this.world = SportsWorld.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getLeaguesXML() {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<leagues>";
        for (int i = 0; i < world.getLeagues().size(); i++) {
            result += "<league>" + i + "</league>";
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
        result += world.getLeagues().get(leagueid).getName();
        result += "</league>";
        return result;
    }
}
