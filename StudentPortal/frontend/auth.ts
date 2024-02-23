import {configureAuth} from "@hilla/react-auth";
import {JpaUserDetailService} from "Frontend/generated/endpoints";

const auth = configureAuth(JpaUserDetailService.getUserInfo);

export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;