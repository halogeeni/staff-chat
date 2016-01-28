package APIResources;

import Sports.SportsWorld;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("teams")
public class TeamsResource {

    private final SportsWorld world;

    public TeamsResource() {
        this.world = SportsWorld.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getTeamsXML() {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        result += "<teams>";
        for (int i = 0; i < world.getTeams().size(); i++) {
            result += "\n\t<teamid>" + i + "</teamid>";
        }
        result += "</teams>";
        return result;
        
    }

    @Path("/{teamid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getTeamXML(@PathParam("teamid") int teamid) {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<team>";
        result += "<name>";
        result += world.getTeams().get(teamid).getName();
        result += "</name>";
        result += "<players>";
        for(int i = 0; i < world.getTeams().get(teamid).getPlayers().size(); i++) {
            result += "<playerid>" + i + "</playerid>";
        }
        result += "</players>";
        result += "</team>";
        return result;
    }
    
    @Path("/{teamid}/{playerid}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getTeamPlayerXML(@PathParam("teamid") int teamid, @PathParam("playerid") int playerid) {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<player>";
        result += "<firstname>";
        result += world.getTeams().get(teamid).getPlayers().get(playerid).getFirstName();
        result += "</firstname>";
        result += "<lastname>";
        result += world.getTeams().get(teamid).getPlayers().get(playerid).getLastName();
        result += "</lastname>";
        result += "</player>";
        
        return result;
   
    }
}
