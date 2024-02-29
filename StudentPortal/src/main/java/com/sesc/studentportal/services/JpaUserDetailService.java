package com.sesc.studentportal.services;

import com.sesc.studentportal.misc.UserInfo;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.UserRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service which manages the User entity authentication and authorizations.
 * It also takes care of fetching the user authentication information based on the User present in the database.
 */
@BrowserCallable
@AnonymousAllowed
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * It fetches a user by looking for it by its username using the Repository.
     * If the user is not found, it throws a UsernameNotFoundException.
     *
     * @param username the username of the user to be fetched.
     * @return the UserDetails object.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(List.of(user.getRoles().split(","))));
    }

    /**
     * Converts a list of strings into a list of GrantedAuthority objects and returns it as a Collection.
     *
     * @param stringAuthorities the list of strings to be converted in authorities.
     * @return the Collection of GrantedAuthority objects.
     */
    private Collection<? extends GrantedAuthority> getAuthorities(List<String> stringAuthorities) {
        return getGrantedAuthorities(stringAuthorities);
    }

    /**
     * Converts a list of strings into a list of GrantedAuthority objects.
     *
     * @param stringAuthorities the list of strings to be converted in authorities.
     * @return the list of GrantedAuthority objects.
     */

    private List<GrantedAuthority> getGrantedAuthorities(List<String> stringAuthorities) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : stringAuthorities) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    /**
     * Gets the user information from the Security Context and returns it as a UserInfo object.
     *
     * @return the UserInfo object.
     */
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        final List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserInfo(auth.getName(), authorities);
    }

    /**
     * Updates the user information in the Security Context and returns the updated UserInfo object.
     *
     * @param userInfo the current User object which holds the Authorities to be updated.
     * @return the updated UserInfo object.
     */
    public UserInfo update(UserInfo userInfo) {
        UserDetails userDetails = loadUserByUsername(userInfo.getName());

        // Getting the user Authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        // Instantiate the Authentication object with new user authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), authorities);
        // Setting the Authentication object to the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Converting the authorities into a list of strings that can be passed to the new UserInfo Object
        List<String> authorityString = authorities.stream().map(GrantedAuthority::getAuthority).toList();

        return new UserInfo(authentication.getName(), authorityString);
    }

}
