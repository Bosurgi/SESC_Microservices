import {createBrowserRouter} from 'react-router-dom';
import Home from "Frontend/views/home/Home";
import Login from "Frontend/views/login/Login";
import Main from "Frontend/views/Main";
import Register from "Frontend/views/registration/Register";
import Course from "Frontend/views/course/Course";
import {protectRoutes} from "@hilla/react-auth";
import Profile from "Frontend/views/profile/Profile";
import {Path, Roles} from "Frontend/util/Constants";
import Enrolments from "Frontend/views/enrolments/Enrolments";
import Graduation from "Frontend/views/graduation/Graduation";

export const routes = protectRoutes([
    {
        element: <Main/>,
        children: [
            // PATHS //

            // Home //
            {path: '/', element: <Home/>, handle: {title: 'Home'}},
            {
                path: '/courses',
                element: <Course/>,
                handle: {title: 'Course', requiresLogin: true}
            },

            // Profile //
            {
                path: '/profile',
                element: <Profile/>,
                handle: {
                    title: 'Profile',
                    rolesAllowed: [Roles.student],
                }
            },

            // Enrolments //
            {
                path: Path.enrolments,
                element: <Enrolments/>,
                handle: {
                    title: 'Enrolments',
                    rolesAllowed: [Roles.student],
                }
            },

            // Graduation //
            {
                path: Path.graduation,
                element: <Graduation/>,
                handle: {
                    title: 'Graduation',
                    rolesAllowed: [Roles.student],
                }
            },

        ],
    },

    // LOGIN AND REGISTER //
    {path: '/register', element: <Register/>, handle: {title: 'Register'}},
    {path: '/login', element: <Login/>, handle: {title: 'Login'}},
]);

export default createBrowserRouter(routes);
