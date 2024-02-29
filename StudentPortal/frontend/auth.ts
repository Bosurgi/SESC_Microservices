import {configureAuth} from "@hilla/react-auth";
import {JpaUserDetailService} from "Frontend/generated/endpoints";

const auth = configureAuth(JpaUserDetailService.getUserInfo,
    {getRoles: (user) => user.authorities.map(a => a.toString())});

export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;