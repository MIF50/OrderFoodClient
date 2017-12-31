package common;

import model.User;

/**
 * Created by mohamed on 11/21/17.
 */

public class Common {
    public static User currentUser;
    /*this method convert status to String placed , On my way ,shipped*/
    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "placed";
        else if (status.equals("1"))
            return  "On My way";
        else
            return "shipped";
    }
}
