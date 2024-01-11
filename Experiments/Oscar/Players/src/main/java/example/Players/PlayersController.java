package example.Players;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class PlayersController {

    ArrayList<Players> PlayerList = new ArrayList<>();


    @PostMapping("/post/Username={username}-Id={id}")
    public @ResponseBody String createPlayers(@PathVariable String username, @PathVariable int id){
        Players temp = new Players(username, id, 20, 5 );
        PlayerList.add(temp);
        return "Created player " + username;
    }

    @GetMapping("/Players")
    public @ResponseBody String getPlayers(){
        if(PlayerList.isEmpty()){
            return "No Players exist";
        }
        StringBuilder s = new StringBuilder();
        for(Players p : PlayerList){
            s.append(p.toString() + "<br>" );
        }

        return s.toString();
    }

    @DeleteMapping("/PlayerId={id}")
    public  @ResponseBody String deletePlayers(@PathVariable int id){
        for(Players temp :PlayerList){
            if(temp.getId() == id) {
                PlayerList.remove(temp);

                return "Player Removed";
            }
        }
        return "Id does not exist for any player";
    }

    @PutMapping("/p1={name1}-p2={name2}-Attack")
    public @ResponseBody String updatePlayer(@PathVariable String name1, @PathVariable String name2) {
        Players temp1 = new Players();
        Players temp2 = new Players();
        int index = 0;

        for (int i = 0; i < PlayerList.size(); i++) {
            if (PlayerList.get(i).getUsername().equals(name1))
                temp1 = PlayerList.get(i);

            if (PlayerList.get(i).getUsername().equals(name2)) {
                temp2 = PlayerList.get(i);
                index = i;
            }
        }
        if(temp1.getUsername() == null || temp2.getUsername() == null){
            return "Players do not exist";
        }
        else
        {
            temp2.setHealth(temp2.getHealth() - temp1.getAtk());
            if(temp2.getHealth() <= 0)
                temp2.setHealth(0);
            PlayerList.get(index).setHealth(temp2.getHealth());
            return name1 + " attacked " + name2 ;
        }


    }








}
