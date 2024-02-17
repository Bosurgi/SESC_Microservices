import {LoginForm} from "@hilla/react-components/LoginForm";
import {NavLink} from "react-router-dom";

export default function Login() {
    return (
        <div className="flex flex-col items-center justify-center p-5">
            <LoginForm
                no-autofocus
                noForgotPassword={true}
            />

            <NavLink to="/register" className="text-primary-500 underline underline-offset-[8px]">Don't have an account?
                Register
                here</NavLink>

        </div>

    );
}