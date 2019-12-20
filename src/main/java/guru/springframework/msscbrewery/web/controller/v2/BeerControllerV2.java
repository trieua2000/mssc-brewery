package guru.springframework.msscbrewery.web.controller.v2;


import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v2/beer")
@RestController
public class BeerControllerV2 {
    private final BeerServiceV2 beerServiceV2;

    @GetMapping({"/{beerId}"})
    public ResponseEntity<BeerDtoV2> getBeer(@PathVariable("beerId") UUID beerId) {

        return new ResponseEntity<>(beerServiceV2.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping    // POST - create new beer
    public ResponseEntity handlePost(@Valid @RequestBody BeerDtoV2 beerDtoV2) {

        log.debug("in handle post...");

        val saveDto = beerServiceV2.saveNewBeer(beerDtoV2);

        val headers = new HttpHeaders();
        // TODO: add hostname to url
        // technically, the headerValue should contain the full url as in http://localhost:8080/api/...
        headers.add("Location", "/api/v2/beer/" + saveDto.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);

    }

    @PutMapping({"/{beerId}"})
    public ResponseEntity handleUpdate(@PathVariable("beerId") UUID beerId, @Valid @RequestBody BeerDtoV2 beerDtoV2) {

        beerServiceV2.updateBeer(beerId, beerDtoV2);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping({"/{beerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("beerId") UUID beerId) {
        beerServiceV2.deleteById(beerId);

    }

    /*@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List> validationErrorHandler(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>(e.getConstraintViolations().size());

        e.getConstraintViolations().forEach(constraintViolation -> {
            errors.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }*/

}
