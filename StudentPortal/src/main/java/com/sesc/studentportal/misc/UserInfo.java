package com.sesc.studentportal.misc;

import dev.hilla.Nonnull;

import java.util.Collection;
import java.util.Collections;

/**
 * User information used in client-side authentication and authorization.
 * To be saved in browsers’ LocalStorage for offline support.
 */
public class UserInfo {

    @Nonnull
    private String name;
    @Nonnull
    private Collection<@Nonnull String> authorities;

    public UserInfo(String name, Collection<String> authorities) {
        this.name = name;
        this.authorities = Collections.unmodifiableCollection(authorities);
    }

    public String getName() {
        return name;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

}