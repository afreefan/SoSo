package com.netease.nim.demo.main.model;

import com.netease.nim.demo.R;
import com.netease.nim.demo.main.fragment.ChatRoomListFragment;
import com.netease.nim.demo.main.fragment.ContactListFragment;
import com.netease.nim.demo.main.fragment.MainTabFragment;
import com.netease.nim.demo.main.fragment.MapFragment;
import com.netease.nim.demo.main.fragment.SessionListFragment;
import com.netease.nim.demo.main.reminder.ReminderId;

public enum MainTab {
    RECENT_CONTACTS(1, ReminderId.SESSION, SessionListFragment.class, R.string.main_tab_session, R.layout.session_list),
    CONTACT(2, ReminderId.CONTACT, ContactListFragment.class, R.string.main_tab_contact, R.layout.contacts_list),
    //CHAT_ROOM(0, ReminderId.INVALID, ChatRoomListFragment.class, R.string.chat_room, R.layout.chat_room_tab);
    CHAT_ROOM(0, ReminderId.INVALID, MapFragment.class, R.string.chat_room, R.layout.map);


    public final int tabIndex;

    public final int reminderId;

    public final Class<? extends MainTabFragment> clazz;

    public final int resId;

    public final int fragmentId;

    public final int layoutId;

    MainTab(int index, int reminderId, Class<? extends MainTabFragment> clazz, int resId, int layoutId) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.layoutId = layoutId;
    }

    public static final MainTab fromReminderId(int reminderId) {
        for (MainTab value : MainTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }

        return null;
    }

    public static final MainTab fromTabIndex(int tabIndex) {
        for (MainTab value : MainTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}
