import {HiOutlineMenu} from "react-icons/hi";

export default function NavBar() {
    return (
        <div className="flex justify-between items-center gap-8 p-5">
            <div>
                <h1 className="text-xl font-semibold">Student Portal</h1>
            </div>
            <div className="hidden gap-5 md:flex">
                <a>Home</a>
                <a>Courses</a>
                <a>Enrolled</a>
                <a>Graduation</a>
            </div>
            <button className="hover:bg-neutral-300 p-3 rounded-lg md:hidden">
                <HiOutlineMenu size={24}/>
            </button>
            <button
                className="hidden bg-blue-500 text-white text-xl px-6 py-3 rounded-lg font-semibold hover:bg-blue-600 active:bg-blue-700 md:flex">Sign
                In
            </button>
        </div>
    );
}