package Sports;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class League {
    private final String name;
    private final List<Team> teams;

    public League() {
        this.name = "";
        teams = new ArrayList<>();
    }

    public League(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public List<Team> getTeams() {
        return teams;
    }

}
