package fi.academy.loppuprojekti_rest.Controllers;

import fi.academy.loppuprojekti_rest.Entities.Destination;
import fi.academy.loppuprojekti_rest.Entities.User;
import fi.academy.loppuprojekti_rest.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/travelapp")
public class Controller {

    @Autowired
    private DestinationRepo destinationRepo;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/destinations")
    public Iterable<Destination> findDestinations( /* Authentication authentication */) {
       // Iterable<Destination> iteDestination = destinationRepo.findAllByUser(authentication.getUser().getUsername);
        Iterable<Destination> iteDestination = destinationRepo.findByCountry();
        return iteDestination;
    }

    @GetMapping ("/destinations/{id}")
    public ResponseEntity<Destination> findOneDestination(@PathVariable(name = "id") Integer id /*tänne authentication-parametri*/) {
        Optional<Destination> optDest = destinationRepo.findById(id); //tännekin joku user-hässäkkä
        if (!optDest.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optDest.get());
    }

    @PutMapping("/destinations/{id}")
    public ResponseEntity<?> modifyDestination (@PathVariable(name = "id") Integer id, @RequestBody Destination destination) {
        destination.setId(id);
        destinationRepo.save(destination);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/destinations/{id}")
    public ResponseEntity<?> removeDestination(@PathVariable (name = "id") Integer id) {
        if (!destinationRepo.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        destinationRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDestination(@RequestBody Destination destination) {
        Destination saved = destinationRepo.save(destination);
        String  address = "http://localhost:8080/travelapp/destinations/"+saved.getId();
        return ResponseEntity.created(URI.create(address)).build();
    }

    @GetMapping("/find") //hakusanalla ei löydy mitään -toiminto puuttuu vielä
    public ResponseEntity<?> filterDestinations(@RequestParam(name = "n", required = false) String searchword, User user) {
        Optional<User> u = userRepo.findById("Heidi");
        if (searchword == null)
            return ResponseEntity.ok(destinationRepo.findAllByUser(u.get()));

        return ResponseEntity.ok(destinationRepo.findBySearchWord(searchword, u.get())); //huomioi myös tyhjä tulos frontissa
    }

}





