import {HiOutlineMenu} from "react-icons/hi";
import {Button} from "@hilla/react-components/Button";
import {NavLink, useNavigate} from 'react-router-dom';
import {useAuth} from "Frontend/auth";

export default function NavBar() {

    const navigate = useNavigate();

    const {state, logout} = useAuth();

    const handleSignIn = () => {
        navigate('/login');
    };

    return (
        <div className="flex justify-between items-center gap-8 p-5">

            <div>
                <h1 className="text-xl font-semibold">Student Portal</h1>
            </div>
            <div className="hidden gap-5 md:flex">
                <NavLink to="/">Home</NavLink>
                <NavLink to="/courses">Courses</NavLink>
                <NavLink to="/enrolments">Enrolled</NavLink>
                <NavLink to="/graduation">Graduation</NavLink>
            </div>
            <button className="hover:bg-neutral-300 p-3 rounded-lg md:hidden">
                <HiOutlineMenu size={24}/>
            </button>
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