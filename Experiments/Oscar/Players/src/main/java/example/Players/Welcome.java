package example.Players;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class Welcome {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to Oscar's Example";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Salutations " + name;
    }
}
