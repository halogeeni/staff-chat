package APIResources;

import Sports.SportsWorld;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("players")
public class PlayersResource {
    
    private final SportsWorld world;
    
    public PlayersResource() {
        this.world = SportsWorld.getInstance();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getPlayersXML() {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<players>";
        for (int i = 0; i < world.getPlayers().size(); i++) {
            result += "<playerid>" + i + "</playerid>";
        }
        result += "</players>";
        return result;
    }
    
    @Path("/{playerid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getPlayerXML(@PathParam("playerid") int playerid) {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<player>";
        result += "<firstname>";
        result += world.getPlayers().get(playerid).getFirstName();
        result += "</firstname>";
        result += "<lastname>";
        result += world.getPlayers().get(playerid).getLastName();
        result += "</lastname>";
        result += "</player>";
        return result;
    }
}
