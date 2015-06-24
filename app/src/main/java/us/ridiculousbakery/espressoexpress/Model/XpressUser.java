package us.ridiculousbakery.espressoexpress.Model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.DisplayHelper;

/**
 * Created by bkuo on 6/3/15.
 */
@ParseClassName("_User")

public class XpressUser extends ParseUser {
    static public String getGravatarUrl(ParseUser u){
        String b = u.getString("gravatar_url");
        if(b==null || b.length()<1){ return DisplayHelper.getProfileUrlFromEmail(u.getEmail()); };
        return b;
    }

    public void setEmail(String email){
        super.setEmail(email);
        put("gravatar_url", DisplayHelper.getProfileUrlFromEmail(email));
    }
    public String getGravatarUrl(){
        String b = getString("gravatar_url");
        if(b==null || b.length()<1){ return DisplayHelper.getProfileUrlFromEmail(getEmail()); };
        return b;
    }
}
