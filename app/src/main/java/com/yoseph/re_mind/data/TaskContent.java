package com.yoseph.re_mind.data;

import com.yoseph.re_mind.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class TaskContent {

    public static final int TYPE_GENERAL = 0;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_SHARED = 2;
    public static final int TYPE_LOCATION = 3;
    public static final int TYPE_WEATHER = 4;
    public static final int TYPE_GLOBAL = 5;
    public static int FILTER = -1;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<TaskItem> ITEMS = new ArrayList<>();
    public static final List<TaskItem> STATIC_ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, TaskItem> ITEM_MAP = new HashMap<>();

    static {
        addItem(new TaskItem("0","Groceries", "Shopping List", TYPE_LIST, new CategoryContent.CategoryItem("Shopping List",2,R.drawable.ic_view_list_black_24dp)));
        addItem(new TaskItem("1","Vote, US General Election", "November 3, 2020", TYPE_GLOBAL, new CategoryContent.CategoryItem("Event",1,R.drawable.event)));
        addItem(new TaskItem("2","Take out the trash",  "Shared with Kadin", TYPE_SHARED, new CategoryContent.CategoryItem("Event",1,R.drawable.event)));
        addItem(new TaskItem("3","Bring an Umbrella",  "Chance of Rain Today", TYPE_WEATHER, new CategoryContent.CategoryItem("Event",1,R.drawable.event)));
        addItem(new TaskItem("4","Holiday List",  "Shopping List", TYPE_GENERAL, new CategoryContent.CategoryItem("Shopping List",2,R.drawable.ic_view_list_black_24dp)));
        addItem(new TaskItem("5","Drop off take home midterm",  "Arriving at Keller Hall", TYPE_LOCATION, new CategoryContent.CategoryItem("Homework",0, R.drawable.category)));
    }

    public static void setFilter(int x) {
        FILTER = x;
    }

    public static List<TaskItem> getItems() {
        if (FILTER < 0) {
            return ITEMS;
        } else {
           return filter(FILTER);
        }
    }

    private static List<TaskItem> filter(int x) {
        List<TaskItem> temp = new ArrayList<>();

        for(TaskItem y : ITEMS) {
            if(y.category.getVal() == x) {
                temp.add(y);
            }
        }
        return temp;
    }

    public static void addItem(TaskItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static int getIconForType(int type) {
        switch(type) {
            case TYPE_GLOBAL:
                return R.drawable.globe;
            case TYPE_SHARED:
                return R.drawable.share;
            case TYPE_LOCATION:
                return R.drawable.location;
            case TYPE_WEATHER:
                return R.drawable.umbrella;
            case TYPE_LIST:
                return R.drawable.ic_view_list_black_24dp;
            case TYPE_GENERAL:
            default:
                return R.drawable.arrow_right;
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class TaskItem implements Serializable {
        public String id = "0";
        public String title = "Title";
        public String details = "Details";
        public String dueDate;
        public CategoryContent.CategoryItem category;
        public String repeat;
        public String share;
        public String location;
        public int type = 0;

        public final List<String> subList = new ArrayList<>();

        public TaskItem() {}

        public TaskItem(String id, String title, String details, int type, CategoryContent.CategoryItem category) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.type = type;
            this.category = category;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
        public void setCategory(CategoryContent.CategoryItem category) {
            this.category = category;
        }

        public void setRepeat(String repeat) {
            this.repeat = repeat;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
