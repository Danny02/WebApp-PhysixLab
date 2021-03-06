/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.server.oauth.accessors;

import de.hofuniversity.iws.server.data.entities.NetworkAccountDBO;
import de.hofuniversity.iws.server.data.entities.UserDBO;
import java.util.ArrayList;

import darwin.annotations.ServiceProvider;

import de.hofuniversity.iws.server.oauth.Providers;
import org.json.*;
import org.scribe.model.Token;

import static de.hofuniversity.iws.server.oauth.OAuthProperties.ACCESSORS;

/**
 *
 * @author Daniel Heinrich <dannynullzwo@gmail.com>
 */
@ServiceProvider(FriendListAccessor.class)
public class FacebookFriends implements FriendListAccessor {

    private static final String FRIENDS_ACCESS_URL = ACCESSORS.getPropertie("FACEBOOK_FRIENDS_URL");

    @Override
    public Iterable<UserDBO> getFriends(Token accessToken, UserDBO currentUser) throws AccessException {
        String requestURL = FRIENDS_ACCESS_URL + "?access_token=" + accessToken.getToken();
        String response = Providers.FACEBOOK.invokeGetRequest(accessToken, requestURL);
        try {
            return parseFriendsJSON(response);
        } catch (JSONException ex) {
            throw new AccessException(ex);
        }
    }

    private Iterable<UserDBO> parseFriendsJSON(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        ArrayList<UserDBO> friends = new ArrayList<UserDBO>();
        JSONArray getArray = json.optJSONArray("data");
        for (int i = 0; i < getArray.length(); i++) {
            UserDBO tmpUser = new UserDBO();
            JSONObject objectInArray = getArray.optJSONObject(i);
            tmpUser.setUserName(objectInArray.optString("name"));
            
            NetworkAccountDBO a = new NetworkAccountDBO();
            a.setAccountIdentificationString(objectInArray.optString("id"));
            a.setNetworkName(Providers.FACEBOOK.name());
            tmpUser.getNetworkAccountList().add(a);
            
            tmpUser.setUserPic("https://graph.facebook.com/" + objectInArray.optString("id") + "/picture&type=normal");
            friends.add(tmpUser);
        }
        return friends;
    }

    @Override
    public Providers supportedProvider() {
        return Providers.FACEBOOK;
    }
}
