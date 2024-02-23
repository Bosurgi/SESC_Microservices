import {LoginForm} from "@hilla/react-components/LoginForm";
import {Navigate, NavLink} from "react-router-dom";
import {useAuth} from "Frontend/auth";
import {useState} from "react";

export default function Login() {
    const {state, login} = useAuth();
    const [hasError, setError] = useState<boolean>();
    const [url, setUrl] = useState<string>();

    if (state.user && url) {
        const path = new URL(url, document.baseURI).pathname;
        return <Navigate to={path} replace/>;
    }

    return (
        <div className="flex flex-col items-center justify-center p-5">
            <LoginForm
                error={hasError}
                no-autofocus
                noForgotPassword={true}
                onLogin={async ({detail: {username, password}}) => {
                    const {defaultUrl, error, redirectUrl} = await login(username, password);
                    if (error) {
                        setError(true);
                    } else {
                        setUrl(redirectUrl ?? defaultUrl ?? '/courses');
                    }
                    console.log(state.user)
                }}
            />

            <NavLink to="/register" className="text-primary-500 underline underline-offset-[8px]">Don't have an account?
                Register
                here</NavLink>

        </div>

    );
}