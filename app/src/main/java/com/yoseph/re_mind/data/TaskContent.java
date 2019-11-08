package com.yoseph.re_mind.data;

import com.yoseph.re_mind.R;

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

    private static final int TYPE_GENERAL = 0;
    private static final int TYPE_LIST = 1;
    private static final int TYPE_SHARED = 2;
    private static final int TYPE_LOCATION = 3;
    private static final int TYPE_WEATHER = 4;
    private static final int TYPE_GLOBAL = 5;


    /**
     * An array of sample (dummy) items.
     */
    public static final List<TaskItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, TaskItem> ITEM_MAP = new HashMap<>();

    static {
        addItem(new TaskItem("0","Groceries", "Shopping List", TYPE_LIST));
        addItem(new TaskItem("1","Vote, US General Election", "November 3, 2020", TYPE_GLOBAL));
        addItem(new TaskItem("2","Take out the trash",  "Shared with Kaden", TYPE_SHARED));
        addItem(new TaskItem("3","Bring an Umbrella",  "Chance of Rain Today", TYPE_WEATHER));
        addItem(new TaskItem("4","Holiday List",  "Shopping List", TYPE_GENERAL));
        addItem(new TaskItem("5","Drop off take home midterm",  "Arriving at Keller Hall", TYPE_LOCATION));
    }

    private static void addItem(TaskItem item) {
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

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class TaskItem {
        public final String id;
        public final String title;
        public final String details;
        public String dueDate;
        public CategoryContent.CategoryItem category;
        public String repeat;
        public String share;
        public String location;

        public final int type;


        public TaskItem(String id, String title, String details, int type) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.type = type;
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
