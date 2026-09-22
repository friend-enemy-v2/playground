package playground.auth;
import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController public class AuthController {
 @GetMapping("/public") Map<String,String> publicPage(){ return Map.of("message","anyone can see this"); }
 @GetMapping("/me") Map<String,String> me(Principal principal){ return Map.of("user",principal.getName()); }
}
