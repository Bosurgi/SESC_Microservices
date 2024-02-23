package com.sesc.studentportal.services;

import com.sesc.studentportal.misc.UserInfo;
import com.sesc.studentportal.model.UserDetail;
import com.sesc.studentportal.repository.UserRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@BrowserCallable
@AnonymousAllowed
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(username)
                .map(UserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        final List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserInfo(auth.getName(), authorities);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findUserByUsername(username);
//        if(user==null){
//            new UsernameNotFoundException("User not exists by Username");
//        }
//        Set<GrantedAuthority> authorities = user.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toSet());
//        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
//    }

//    public UserDetails getUserInfo() {
//        Authentication auth = SecurityContextHolder.getContext()
//                .getAuthentication();
//
//        final List<String> authorities = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return new UserDetail((User) auth.getPrincipal());

}
