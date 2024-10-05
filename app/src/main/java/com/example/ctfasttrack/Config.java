package com.example.ctfasttrack;

public class Config {
//    private static String OnlineAddress = "http://bitlancetechhub.com/ctfasttrack/";
    private static String OnlineAddress = "http://192.168.170.190:80/bus_track/";

    public static String urlregister = OnlineAddress + "registration.php";
    public static String urllogin = OnlineAddress + "login.php";
    public static String urlUpdatePassword = OnlineAddress + "updatePassword.php";
    public static String getLocation = OnlineAddress + "select_bus_location.php";
    public static String getStations = OnlineAddress + "sendStations.php";
    public static String getBusesByStation = OnlineAddress + "searchbusbysourcedestination.php";
    public static String getSingleBusLocation = OnlineAddress + "single_bus_current_location.php";
    public static String sendfeedback = OnlineAddress + "submit_feedback.php";
    public static String addhistory = OnlineAddress + "addhistory.php";
    public static String urlchangepassword = OnlineAddress + "change_password.php";
    public static String url_history = OnlineAddress + "showhistory.php";
    public static String url_all_buses = OnlineAddress + "getAllStation.php";
    public static String urlBookMyBus = OnlineAddress + "book_my_bus.php";

    public static final String url_all_booking = OnlineAddress+"get_all_booking.php";

}
