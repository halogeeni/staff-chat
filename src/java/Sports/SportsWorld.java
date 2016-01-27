package Sports;

import java.util.ArrayList;
import java.util.List;

public class SportsWorld {

    // just a test class to create some leagues, teams & players

    private final List<League> leagues;
    private final List<Team> teams;
    private final List<Player> players;
    private static SportsWorld world;

    public static SportsWorld getInstance() {
        if (world == null) {
            world = new SportsWorld();
        }
        
        return world;
    }
    
    private SportsWorld() {
        this.leagues = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.players = new ArrayList<>();
        populateWorld();
    }
    
    private void populateWorld() {
        // create players
        players.add(new Player("Cristiano", "Ronaldo"));
        players.add(new Player("Lionel", "Messi"));
        players.add(new Player("Jari", "Litmanen"));
        players.add(new Player("Zlatan", "Ibrahimovic"));
        players.add(new Player("Miroslav", "Klose"));
        players.add(new Player("Harry", "Kane"));
        players.add(new Player("Robin", "Van Persie"));
        players.add(new Player("Luis", "Suarez"));
        players.add(new Player("Emmanuel", "Adebayor"));
        players.add(new Player("Seydou", "Keita"));
        players.add(new Player("David", "Villa"));
        players.add(new Player("Samuel", "Eto'o"));
        players.add(new Player("Wayne", "Rooney"));
        players.add(new Player("Angel", "Di Maria"));
        
        // create teams
        teams.add(new Team("Masters FC"));
        teams.add(new Team("Legacy Utd"));
        
        // fill "Masters FC"
        for(int i = 0; i < 7; i++) {
            teams.get(0).getPlayers().add(players.get(i));
        }
        
        // fill "Legacy Utd"
        for(int i = 7; i < 14; i++) {
            teams.get(1).getPlayers().add(players.get(i));
        }
        
        // create a league & add teams to it
        leagues.add(new League("World League"));
        leagues.get(0).getTeams().add(teams.get(0));
        leagues.get(0).getTeams().add(teams.get(1));
    }
    
    // getters

    public List<League> getLeagues() {
        return leagues;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
}
