import {createBrowserRouter, RouteObject} from 'react-router-dom';
import Home from "Frontend/views/home/Home";
import Login from "Frontend/views/login/Login";
import Main from "Frontend/views/Main";
import Register from "Frontend/views/registration/Register";
import Course from "Frontend/views/course/Course";

export const routes = [
    {
        element: <Main/>,
        // handle: {title: 'Main'},
        children: [
            // PATHS
            {path: '/', element: <Home/>, handle: {title: 'Home'}},
            {path: '/login', element: <Login/>, handle: {title: 'Login'}},
            {path: '/register', element: <Register/>, handle: {title: 'Register'}},
            {path: '/courses', element: <Course/>, handle: {title: 'Course'}},
        ],
    },
] as RouteObject[];

export default createBrowserRouter(routes);
