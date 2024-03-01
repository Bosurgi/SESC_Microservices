import {createBrowserRouter} from 'react-router-dom';
import Home from "Frontend/views/home/Home";
import Login from "Frontend/views/login/Login";
import Main from "Frontend/views/Main";
import Register from "Frontend/views/registration/Register";
import Course from "Frontend/views/course/Course";
import {protectRoutes} from "@hilla/react-auth";
import Profile from "Frontend/views/profile/Profile";
import {Roles} from "Frontend/util/Constants";

export const routes = protectRoutes([
    {
        element: <Main/>,
        // handle: {title: 'Main'},
        children: [
            // PATHS
            {path: '/', element: <Home/>, handle: {title: 'Home'}},
            {
                path: '/courses',
                element: <Course/>,
                handle: {title: 'Course', requiresLogin: true}
            },

            {
                path: '/profile',
                element: <Profile/>,
                handle: {
                    title: 'Profile',
                    rolesAllowed: [Roles.student],
                }
            },
        ],
    },
    {path: '/register', element: <Register/>, handle: {title: 'Register'}},
    {path: '/login', element: <Login/>, handle: {title: 'Login'}},
]);

export default createBrowserRouter(routes);
