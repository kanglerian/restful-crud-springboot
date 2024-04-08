package kanglerian.restful;

import jakarta.validation.ConstraintViolationException;
import kanglerian.restful.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @GetMapping(path = "")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping(path = "")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {
        try {
            validationService.validate(user);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Saved: " + user.getName());
        } catch(ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id){
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findById(id));
            if (user.isPresent()) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping(path = "/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            validationService.validate(user);
            Optional<User> userOptional = Optional.ofNullable(userRepository.findById(id));
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                User userExist = userOptional.get();
                userExist.setEmail(user.getEmail());
                userExist.setName(user.getName());
                userRepository.save(userExist);
                return ResponseEntity.ok("Updated: " + userExist.getName());
            }
        } catch(ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findById(id));
            if(userOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            } else {
                User userExist = userOptional.get();
                userRepository.deleteById(userExist.getId());
                return ResponseEntity.ok("Deleted: " + userExist.getName());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        }
    }
}
