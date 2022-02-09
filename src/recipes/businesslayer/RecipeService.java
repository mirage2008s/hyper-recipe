package recipes.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import recipes.businesslayer.Recipe;
import recipes.persistence.RecipeRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

      private final RecipeRepository recipeRepository;

      @Autowired
      public RecipeService(RecipeRepository recipeRepository) {
          this.recipeRepository = recipeRepository;
      }

      public Recipe findRecipeById(Long id) {
          return recipeRepository.findRecipeById(id);
      }

      public Recipe save(Recipe recipe) {
          return recipeRepository.save(recipe);
      }

      @Transactional
      public void deleteById(Long id) {
          recipeRepository.deleteById(id);
      }

      public Recipe updateById(Recipe recipe, Long id) {
          Recipe r = recipeRepository.findRecipeById(id);
          if (r != null) {
              r.setName(recipe.getName());
              r.setCategory(recipe.getCategory());
              r.setDate(LocalDateTime.now());
              r.setDescription(recipe.getDescription());
              r.setIngredients(recipe.getIngredients());
              r.setDirections(recipe.getDirections());
              recipeRepository.save(r);
              System.out.println("updated!");
              return r;
          }
          return null;
      }

      public List<Recipe> searchByCategory(String category) {
          return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
      }

      public List<Recipe> searchByNameContaining(String name) {
          return recipeRepository.findByNameIgnoreCaseContainingOrderByDateDesc(name);
      }
}
