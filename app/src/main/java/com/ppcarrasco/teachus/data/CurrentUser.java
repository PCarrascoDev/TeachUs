package com.ppcarrasco.teachus.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Pedro on 28-09-2017.
 */

public class CurrentUser {
    private FirebaseUser user;
    private String name = "Unknown";

    public CurrentUser()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            name = user.getDisplayName();
        }
    }

    public String getUid()
    {
        return user.getUid();
    }

    public String getName() {
        return name;
    }

    public String getEmail(){return user.getEmail();}

}
