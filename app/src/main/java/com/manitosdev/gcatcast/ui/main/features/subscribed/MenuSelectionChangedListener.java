package com.manitosdev.gcatcast.ui.main.features.subscribed;

/**
 * Interface that allows us to know which {@link MenuRadioGroup} is passing us data
 */
public interface MenuSelectionChangedListener {

	/**
	 * Provide action listener to menu
	 *
	 * @param menuType
	 * @param index
	 * @param id
	 */
	void onMenuItemSelected(MenuType menuType, int index, int id);
}
