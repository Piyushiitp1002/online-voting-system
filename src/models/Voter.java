package models;

public class Voter {
    private String voterId;
    private String name;
    private String password;

    public Voter(String voterId, String name, String password) {
        this.voterId = voterId;
        this.name = name;
        this.password = password;
    }

    public String getVoterId() { return voterId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
}
