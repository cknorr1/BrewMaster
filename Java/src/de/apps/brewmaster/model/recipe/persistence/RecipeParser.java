package de.apps.brewmaster.model.recipe.persistence;

import java.io.File;

import de.apps.brewmaster.model.recipe.persistence.object.BrewRecipe;

public final class RecipeParser {

	/**
	 * The unique instance of this class.
	 */
	private static RecipeParser instance;

	/**
	 * Returns the unique instance of this class.
	 *
	 * @return the unique instance.
	 */
	public static RecipeParser getInstance() {
		if (null == instance) {
			instance = new RecipeParser();
		}
		return instance;
	}

	/**
	 * Private constructor to restrict access to this class.
	 */
	private RecipeParser() {
	}

	public File parseRecipeToXML(final BrewRecipe recipe) {
		// TODO
		return null;
	}

	public BrewRecipe parseXMLToRecipe(final File xmlFile) {
		// TODO
		return null;
	}
}
