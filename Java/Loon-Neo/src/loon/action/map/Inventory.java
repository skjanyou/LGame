package loon.action.map;

import loon.utils.ObjectMap;
import loon.utils.ObjectMap.Keys;

public class Inventory {

	private ObjectMap<String, Item> items;

	private int gold;

	public Inventory() {
		super();
		items = new ObjectMap<String, Item>(128);
		gold = 0;
	}

	public void subtractGold(int i) {
		gold -= i;
	}

	public void addGold(int i) {
		gold += i;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int i) {
		gold = i;
	}

	public boolean addItem(String key, Item obj) {
		if (obj == null) {
			return false;
		}
		return items.put(key, obj) == null;
	}

	public boolean removeItem(String key) {
		return items.remove(key) == null;
	}

	public int getItemCount() {
		return items.size;
	}

	public String[] getItemList() {
		Keys<String> keys = items.keys();
		String[] names = new String[items.size];
		int idx = 0;
		for (String name : keys) {
			names[idx++] = name;
		}
		return names;
	}

	public void merge(Inventory i) {
		items.putAll(i.items);
		this.addGold(i.getGold());
	}

	public void clear() {
		items.clear();
	}

}
