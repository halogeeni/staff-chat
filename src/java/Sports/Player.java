package Sports;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Player {
    private final String firstname;
    private final String lastname;

    public Player() {
        this.firstname = "";
        this.lastname = "";
        
    }
    
    public Player(String firstName, String lastName) {
        this.firstname = firstName;
        this.lastname = lastName;
    }
    
    // setters & getters

    @XmlElement
    public String getFirstname() {
        return firstname;
    }

    @XmlElement
    public String getLastname() {
        return lastname;
    }
    
}
