package com.sesc.studentportal.misc;

//import java.util.stream.Collectors;
//
//public class AuthoritiesUpdater {
//
//
//    public UserInfo updateUserAuthorities() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        final List<String> authorities = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return new UserInfo(auth.getName(), authorities);
//    }
//
//    @PermitAll
//    public String echoAuthUser() {
//        return VaadinRequest.getCurrent().getUserPrincipal().toString();
//    }
//
//}
