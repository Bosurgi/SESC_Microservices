import {HiOutlineMenu} from "react-icons/hi";
import {Button} from "@hilla/react-components/Button";
import {NavLink, useNavigate} from 'react-router-dom';

export default function NavBar() {

    const navigate = useNavigate();
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
            <Button
                theme={"primary"}
                onClick={handleSignIn}
                // className="hidden bg-blue-500 text-white text-xl px-6 py-3 rounded-lg font-semibold hover:bg-blue-600 active:bg-blue-700 md:flex"
            >
                Sign In
            </Button>
        </div>
    );
}