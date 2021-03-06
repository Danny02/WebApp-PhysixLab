/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.shared.services;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.*;
import de.hofuniversity.iws.shared.dto.LoginDTO;
import org.scribe.exceptions.OAuthException;

/**
 *
 * @author User
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {  
    
    /**
     * returns the current valid session if present
     * @return
     */
    public Optional<LoginDTO> getLoginData();
    
    /**
     * Method to wait for an OAuth verification after the client opend a login popup of the OAuth Provider
     * @return
     * A valid session ID
     * @throws OAuthException 
     */
    public LoginDTO waitForOAuthVerification() throws LoginException;
    
    /**
     * Get a verification URL of the named OAuth provider
     * @param provider
     * @return 
     */
    public String getOAuthLoginUrl(String provider);
    
    /**
     * invalidates the active session
     */
    public void logout();
}
