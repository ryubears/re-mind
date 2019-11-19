package com.yoseph.re_mind.data;

import com.yoseph.re_mind.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryContent {
    public static final List<CategoryItem> CATEGORIES = new ArrayList<>();

    static {
        addItem(new CategoryItem("Homework", 0, R.drawable.category));
        addItem(new CategoryItem("Event",1,R.drawable.event));
        addItem(new CategoryItem("Shopping List",2, R.drawable.ic_view_list_black_24dp));
    }

    private static void addItem(CategoryItem item) {
        CATEGORIES.add(item);
    }

    public static String[] getTitles() {
        String[] arr = new String[CATEGORIES.size()];

        for (int i =0; i < CATEGORIES.size(); i++)
            arr[i] = CATEGORIES.get(i).title;

        return arr;
    }

    public static int[] getIcons() {
        int[] arr = new int[CATEGORIES.size()];

        for (int i =0; i < CATEGORIES.size(); i++)
            arr[i] = CATEGORIES.get(i).icon;

        return arr;
    }


    public static class CategoryItem {

        public String title;
        public int val;
        public int icon;

        public CategoryItem() {}

        public CategoryItem(String title, int val, int icon) {
            this.title = title;
            this.val = val;
            this.icon = icon;
        }

        public int getVal() {
            return this.val;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
