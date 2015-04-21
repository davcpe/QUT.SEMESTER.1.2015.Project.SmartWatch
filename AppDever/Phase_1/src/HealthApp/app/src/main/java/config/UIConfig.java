package config;


import com.appdever.healthapp.R;
import models.Menu;
import models.Menu.HeaderType;


public class UIConfig {

    public static Menu MENU_ACTIVITY = new Menu(R.string.menu_activity, R.drawable.ic_activity, R.drawable.ic_activity, HeaderType.HeaderType_CATEGORY);
    public static Menu HEADER_MODE = new Menu(R.string.header_mode, -1, -1, HeaderType.HeaderType_LABEL);
    public static Menu MENU_WALKING = new Menu(R.string.menu_walking, R.drawable.ic_walking, R.drawable.ic_walking, HeaderType.HeaderType_CATEGORY);

    public static Menu MENU_CYCLING = new Menu(R.string.menu_cycling, R.drawable.ic_cycling, R.drawable.ic_cycling, HeaderType.HeaderType_CATEGORY);
    public static Menu MENU_SLEEP = new Menu(R.string.menu_sleep, R.drawable.ic_sleep, R.drawable.ic_sleep, HeaderType.HeaderType_CATEGORY);

    public static Menu HEADER_SOCIAL = new Menu(R.string.header_social, -1, -1, HeaderType.HeaderType_LABEL);
    public static Menu MENU_FRIEND = new Menu(R.string.menu_friend, R.drawable.ic_friend, R.drawable.ic_friend, HeaderType.HeaderType_CATEGORY);
    public static Menu MENU_RANK = new Menu(R.string.menu_rank, R.drawable.ic_rank, R.drawable.ic_rank, HeaderType.HeaderType_CATEGORY);

    public static Menu HEADER_PROFILE = new Menu(R.string.header_profile, -1, -1, HeaderType.HeaderType_LABEL);
    public static Menu MENU_HISTORY = new Menu(R.string.menu_history, R.drawable.ic_history, R.drawable.ic_history, HeaderType.HeaderType_CATEGORY);
    public static Menu MENU_LOGOUT = new Menu(R.string.menu_logout, R.drawable.ic_logout, R.drawable.ic_logout, HeaderType.HeaderType_CATEGORY);


	public static Menu[] MENUS_BAR = {

            HEADER_MODE,
            MENU_WALKING,
            MENU_CYCLING,
            MENU_SLEEP,
            HEADER_SOCIAL,
            MENU_FRIEND,
            MENU_RANK,
            HEADER_PROFILE,
            MENU_HISTORY,
            MENU_LOGOUT
	};


}
