package de.apps.brewmaster.model.recipe;

import de.apps.brewmaster.model.recipe.persistence.object.BrewRecipe;

public class BrewRecipeDataModel {

	private final BrewRecipe currentRecipe;

	public BrewRecipeDataModel(final BrewRecipe recipe) {
		currentRecipe = recipe;
	}

	public BrewRecipe getCurrentRecipe() {
		return currentRecipe;
	}
}
