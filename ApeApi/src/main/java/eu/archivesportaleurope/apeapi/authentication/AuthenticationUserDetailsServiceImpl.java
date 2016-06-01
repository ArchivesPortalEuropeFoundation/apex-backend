/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.authentication;

import eu.apenet.persistence.vo.ApiKey;
import eu.archivesportaleurope.apeapi.transaction.repository.ApiKeyRepo;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author M.Mozadded
 */
//@SuppressWarnings("deprecation")
public class AuthenticationUserDetailsServiceImpl implements
        AuthenticationUserDetailsService<Authentication> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ApiKeyRepo apiKeyRepo;

    Collection<GrantedAuthority> grantedAuthorities;

    public void setApiKeyRepo(ApiKeyRepo apiKeyRepo) {
        this.apiKeyRepo = apiKeyRepo;
    }
    
    @Override
    public UserDetails loadUserDetails(Authentication token)
            throws UsernameNotFoundException {
        UserDetails userDetails = null;

        String[] credentials = (String[]) token.getCredentials();
        boolean principal = Boolean.parseBoolean(token.getPrincipal().toString());

        if (credentials != null && principal) {
            String apiKey = credentials[0];
            ApiKey apiKeyUser = apiKeyRepo.findByApiKey(apiKey);
            if (apiKeyUser == null) {
                throw new UsernameNotFoundException("Could not load user : "
                        + token.getName());
            }
            this.logger.debug("User Name: " + apiKeyUser.getFirstName()
                    + " email:" + apiKeyUser.getEmailAddress());
            userDetails = assignUserPermission(apiKeyUser.getApiKey());
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("Could not load user : "
                    + token.getName());
        }

        return userDetails;
    }

    private UserDetails assignUserPermission(String apiKey) {
        this.grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(apiKey, "notused", true, true, true, true,
                grantedAuthorities);
    }
}
