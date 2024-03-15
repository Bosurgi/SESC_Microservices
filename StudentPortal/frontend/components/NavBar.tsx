import {Button} from "@hilla/react-components/Button";
import {NavLink, useNavigate} from 'react-router-dom';
import {useAuth} from "Frontend/auth";
import {Path, Roles} from "Frontend/util/Constants";

export default function NavBar() {

    const navigate = useNavigate();

    const {state, logout} = useAuth();

    const handleSignIn = () => {
        navigate(Path.login);
    };

    return (
        <div className="flex justify-between items-center gap-8 p-5">
            <div>
                <h1 className="text-xl font-semibold">Student Portal</h1>
            </div>
            {
                // Render specific links based on User Role
                state.user?.authorities.includes(Roles.student) ? (
                    <div className="hidden gap-5 md:flex">
                        <NavLink to={Path.home}>Home</NavLink>
                        <NavLink to={Path.profile}>Profile</NavLink>
                        <NavLink to={Path.courses}>Courses</NavLink>
                        <NavLink to={Path.enrolments}>Enrolled</NavLink>
                        <NavLink to={Path.invoices}>Invoices</NavLink>
                        <NavLink to={Path.graduation}>Graduation</NavLink>
                    </div>
                ) : (
                    <div className="hidden gap-5 md:flex">
                        <NavLink to={Path.home}>Home</NavLink>
                        <NavLink to={Path.courses}>Courses</NavLink>
                    </div>
                )
            }

            {/* Render Log In or Log Out Button if user is authenticated*/}

            {!state.user ? (
                    <Button
                        theme={"primary"}
                        onClick={handleSignIn}>
                        Sign In
                    </Button>
                ) :

                (
                    <div className="flex items-center">
                        <p className="mr-4">Welcome, {state.user.name}</p>
                        <Button
                            theme={"primary"}
                            onClick={logout}> Logout
                        </Button>
                    </div>
                )
            }
        </div>
    );
}