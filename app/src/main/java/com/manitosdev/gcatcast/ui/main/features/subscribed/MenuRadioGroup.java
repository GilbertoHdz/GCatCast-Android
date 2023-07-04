package com.manitosdev.gcatcast.ui.main.features.subscribed;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.manitosdev.gcatcast.R;
import java.util.List;


public class MenuRadioGroup extends RadioGroup {
	/**
	 * Used to convert the RadioButton id to an index the player can use
	 */
	private SparseIntArray mIdToIndexMap;
	/**
	 * Used to convert the index from the player into an id we can use to update the selected item
	 * Mostly for the first selection
	 */
	private SparseIntArray mIndexToIdMap;

	/**
	 * Callback that notifies which item was selected. The index is relative to the List<String> that was provided as a data source
	 */
	private MenuSelectionChangedListener mSelectionChangeListener;

	/**
	 * The specific menu type this RadioGroup is rendering
	 */
	private MenuType mMenuType;

	public MenuRadioGroup(Context context) {
		super(context);
		initView();
	}

	public MenuRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mIdToIndexMap = new SparseIntArray();
		mIndexToIdMap = new SparseIntArray();
	}

	/**
	 * This gets called each time the ViewModel is updated by our BindingAdapter
	 */
	public void setDataSource(MenuType menuType,
							  List<String> data,
							  MenuSelectionChangedListener listener,
							  Integer selectedIndex) {
		// Clear the current state so we don't add new things on top of it
		clearState();

		mMenuType = menuType;

		// The SelectionChangeListener is essentially the player, it needs to know which item was selected
		mSelectionChangeListener = listener;

		// inflating layouts allows us to easily customize the appearance of the view with xml resources
		LayoutInflater inflater = LayoutInflater.from(getContext());
		for (int i = 0; i < data.size(); i++) {
			addItem(data.get(i), i, inflater, i == selectedIndex);
		}

		// Notifies the listener when a new RadioButton is selected
		this.setOnCheckedChangeListener((group, checkedId) -> {
			if (mSelectionChangeListener != null) {
				mSelectionChangeListener
						.onMenuItemSelected(mMenuType, mIdToIndexMap.get(checkedId), checkedId);
			}
		});
	}

	/**
	 * Clears previous data
	 */
	private void clearState() {
		this.removeAllViews();
		mIdToIndexMap.clear();
		mIndexToIdMap.clear();
	}

	/**
	 * Only called by {@link #setDataSource(MenuType, List, MenuSelectionChangedListener, Integer)}
	 * Allows us to fill the RadioGroup dynamically with new data
	 */
	private void addItem(String label, int index, LayoutInflater inflater, boolean isSelected) {
		// unique positive id works best
		int id = Math.abs(label.hashCode() + (index * 11));
		RadioButton radioButton = (RadioButton)inflater
				.inflate(R.layout.menu_radio_button, this, false);
		radioButton.setText(label);
		radioButton.setId(id);
		radioButton.setChecked(isSelected);
		mIdToIndexMap.put(id, index);
		mIndexToIdMap.put(index, id);
		addView(radioButton);
	}

}
