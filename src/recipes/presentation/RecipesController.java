package recipes.presentation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.businesslayer.Recipe;
import recipes.businesslayer.RecipeService;
import recipes.businesslayer.User;
import recipes.businesslayer.UserService;
import recipes.exceptions.RecipeNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RecipesController {

    @Autowired
    private final RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        Recipe getRecipe = recipeService.findRecipeById(id);
        if (getRecipe == null) {
            throw new RecipeNotFoundException();
        }
        return getRecipe;
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<Object> addRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        System.out.println(details.getUsername()+"Hi");
        Recipe recipeCreate = recipeService.save(new Recipe(recipe.getName(), recipe.getCategory(),
                recipe.getDescription(), recipe.getIngredients(), recipe.getDirections(), details.getUsername()));
        return new ResponseEntity<>(Map.of("id", recipeCreate.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Object> deleteRecipe(@AuthenticationPrincipal UserDetails details, @PathVariable Long id) {
        String email = details.getUsername();
        Recipe recipe = recipeService.findRecipeById(id);
        if (recipe == null) {
            throw new RecipeNotFoundException();
        }
        String author = recipe.getAuthor();
        if (email.equals(author)) {
            recipeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Object> updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        String email = details.getUsername();
        Recipe recipeFinding = recipeService.findRecipeById(id);
        if (recipeFinding == null) {
            throw new RecipeNotFoundException();
        }
        String author = recipeFinding.getAuthor();
        if (email.equals(author)) {
            recipeService.updateById(recipe, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "/recipe/search/")
    public ResponseEntity<Object> searchByContainingName(@RequestParam Optional<String> name, @RequestParam Optional<String> category) {
        if (name.isPresent() && category.isEmpty()) {
            return new ResponseEntity<>(recipeService.searchByNameContaining(name.get()), HttpStatus.OK);
        }
        if (name.isEmpty() && category.isPresent()) {
            return new ResponseEntity<>(recipeService.searchByCategory(category.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    // register new user
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody User user) {
        String rs = userService.saveUser(new User(user.getEmail(), encoder.encode(user.getPassword())));
        if (rs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
