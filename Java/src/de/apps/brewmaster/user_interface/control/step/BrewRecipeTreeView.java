package de.apps.brewmaster.user_interface.control.step;

import de.apps.brewmaster.model.recipe.persistence.object.BrewStepID;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class BrewRecipeTreeView extends TreeView<BrewStepID> {

	public BrewRecipeTreeView() {
		setShowRoot(false);
		final TreeItem<BrewStepID> root = new TreeItem<>();
		setRoot(root);

		final ChangeListener<Boolean> expandedListener = (obs, wasExpanded, isNowExpanded) -> {
			if (isNowExpanded) {
				final ReadOnlyProperty<?> expandedProperty = (ReadOnlyProperty<?>) obs;
				final Object itemThatWasJustExpanded = expandedProperty.getBean();
				for (final TreeItem<BrewStepID> item : getRoot().getChildren()) {
					if (item != itemThatWasJustExpanded) {
						item.setExpanded(false);
					}
				}
			}
		};

		for (final BrewStepID step : BrewStepID.values()) {
			final TreeItem<BrewStepID> item = new TreeItem<>(step);
			item.expandedProperty().addListener(expandedListener);
			root.getChildren().add(item);
			// for (int j = 1; j <= 4; j++) {
			// final TreeItem<BrewStep> subItem = new TreeItem<>();
			// item.getChildren().add(subItem);
			// }
		}

		final PseudoClass subElementPseudoClass = PseudoClass.getPseudoClass("sub-tree-item");

		setCellFactory(tv -> {
			final TreeCell<BrewStepID> cell = new TreeCell<BrewStepID>() {
				@Override
				public void updateItem(final BrewStepID item, final boolean empty) {
					super.updateItem(item, empty);
					setDisclosureNode(null);

					if (empty) {
						setText("");
						setGraphic(null);
					} else {
						setText(item.toString()); // appropriate text for item
					}
				}

			};
			cell.treeItemProperty().addListener((obs, oldTreeItem, newTreeItem) -> {
				cell.pseudoClassStateChanged(subElementPseudoClass,
						newTreeItem != null && newTreeItem.getParent() != cell.getTreeView().getRoot());
			});
			return cell;
		});
	}
}
