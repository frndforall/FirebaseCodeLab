package retrofit.cybersoft.testretrofit;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by nagendra.sharma on 03-10-2017.
 */

@IgnoreExtraProperties
public class User {
    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
