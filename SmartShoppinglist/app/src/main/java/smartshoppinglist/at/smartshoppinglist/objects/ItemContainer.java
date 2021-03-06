package smartshoppinglist.at.smartshoppinglist.objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ItemContainer implements Comparable<ItemContainer>, Serializable {
    private transient static String defaultUnit = "Stk";
    private transient static int defaultCount = 1;
    private Item item;
    private int count;
    private String unit;
    private boolean ticked;


    public ItemContainer(Item item, int count, String unit){
        this.item = item;
        this.count = count;
        this.unit = unit;
    }
    public ItemContainer(Item item, int count){
        this(item, count, defaultUnit);
    }
    public ItemContainer(Item item, String unit){
        this(item, defaultCount, unit);
    }
    public ItemContainer(Item item){
        this(item, defaultCount);
    }

    public int getCount() {
        return count;
    }

    public Item getItem() {
        return item;
    }

    public String getUnit() {
        return unit;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int compareTo(ItemContainer ic) {
        return item.compareTo(ic.item);
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

    public static int getDefaultCount() {
        return defaultCount;
    }

    public static String getDefaultUnit() {
        return defaultUnit;
    }

    public static void setDefaultUnit(String defaultUnit) {
        defaultUnit = defaultUnit;
    }

    public static void setDefaultCount(int defaultCount) {
        defaultCount = defaultCount;
    }
}
