package Sports;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Player {
    private final String firstName;
    private final String lastName;

    public Player() {
        this.firstName = "";
        this.lastName = "";
        
    }
    
    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

    }
    
    // setters & getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
}
