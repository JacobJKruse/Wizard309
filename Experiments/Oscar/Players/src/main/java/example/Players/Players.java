package example.Players;

public class Players {
    private String username;

    private int id;

    private int health;

    private  int atk;

    public Players(){

    }

    public Players (String username, int id, int health, int atk){
        this.username = username;
        this.id = id;
        this.health = health;
        this.atk = atk;
    }

    public String getUsername(){return this.username;}
    public void setUsername(String username){this.username = username;}

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}

    public int getHealth(){return this.health;}
    public void setHealth(int health){this.health = health;}

    public int getAtk(){return this.atk;}
    public void setAtk(int atk){this.atk = atk;}

    @Override
    public String toString(){
        return "{\n" +
                "Username = " + this.username +
                "\nId = " + this.id +
                "\nHealth = " + this.health +
                "\nAttack = " + this.atk +
                "\n}";
    }





}
