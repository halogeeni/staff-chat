package Sports;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Team {
    private final String name;
    private final List<Player> players;

    public Team() {
        this.name = "";
        this.players = new ArrayList<>();
    }
    
    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public List<Player> getPlayers() {
        return players;
    }

}
