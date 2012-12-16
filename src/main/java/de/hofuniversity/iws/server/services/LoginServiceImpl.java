/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.server.services;

import java.util.UUID;

import de.hofuniversity.iws.server.oauth.*;
import de.hofuniversity.iws.server.oauth.provider.OAuthProvider;
import de.hofuniversity.iws.shared.services.LoginService;
import de.hofuniversity.iws.shared.services.login.LoginException;

import com.google.common.base.*;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javax.servlet.http.*;

/**
 *
 * @author User
 */
@RemoteServiceRelativePath("login")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    public static final String SESSION_ATTRIBUTE = "session";
    public static final String TOKEN_ATTRIBUTE = "token";
    public static final String USER_ATTRIBUTE = "user";
    public static final int TIMEOUT_INTERVALL = 30_000;

    @Override
    public Optional<String> getSessionToken() {
        return getSessionAttribute(TOKEN_ATTRIBUTE);
    }

    @Override
    public String waitForOAuthVerification() throws LoginException {
        Optional<OAuthLogin> login = getSessionAttribute(OAuthCallbackServlet.OAUTH_LOGIN_ATTRIBUTE);
        if (login.isPresent()) {
            OAuthLogin l = login.get();
            synchronized (l) {
                if (!l.successfull) {
                    try {
                        l.wait(TIMEOUT_INTERVALL);
                    } catch (InterruptedException ex) {
                    }
                }
                if (l.successfull) {
                    String token = UUID.randomUUID().toString();
                    storeSessionAttribute(TOKEN_ATTRIBUTE, token);
                    return token;
                } else {
                    throw new LoginException("Login Timeout eceeded or login wasn't succesfull!");
                }
            }
        } else {
            throw new LoginException("No OAuth login was initialized!");
        }
    }

    @Override
    public String getOAuthLoginUrl(String provider) {
        try {
            Providers prov = Providers.valueOf(provider);
            OAuthProvider oauth = prov.getProvider();
            OAuthAccessRequest request = oauth.createRequest();
            storeSessionAttribute(OAuthCallbackServlet.OAUTH_LOGIN_ATTRIBUTE, new OAuthLogin(prov, request));
            return request.getAuthorizeUrl();
        } catch (IllegalArgumentException ex) {
            throw new UnsupportedOperationException("no support for provider: " + provider);
        }
    }

    @Override
    public void logout() {
        getThreadLocalRequest().getSession().invalidate();
    }

    public Optional<HttpSession> getSession() {
        return getSession(getThreadLocalRequest());
    }

    public static Optional<HttpSession> getSession(HttpServletRequest req) {
        return Optional.fromNullable(req.getSession(false));
    }

    public <T> Optional<T> getSessionAttribute(String attributeName) {
        return getSessionAttribute(getThreadLocalRequest(), attributeName);
    }

    public static <T> Optional<T> getSessionAttribute(HttpServletRequest request, final String attributeName) {
        return getSession(request).transform(new Function<HttpSession, T>() {
            @Override
            public T apply(HttpSession session) {
                return (T) session.getAttribute(attributeName);
            }
        });
    }

    public void storeSessionAttribute(String attributeName, Object value) {
        storeSessionAttribute(getThreadLocalRequest(), attributeName, value);
    }

    public static void storeSessionAttribute(HttpServletRequest request, String attributeName, Object value) {
        request.getSession().setAttribute(attributeName, value);
    }
}
