import {createBrowserRouter, RouteObject} from 'react-router-dom';
import Home from "Frontend/views/home/Home";
import Login from "Frontend/views/login/Login";
import Main from "Frontend/views/Main";

export const routes = [
    {
        element: <Main/>,
        // handle: {title: 'Main'},
        children: [
            // PATHS
            {path: '/', element: <Home/>, handle: {title: 'Home'}},
            {path: '/login', element: <Login/>, handle: {title: 'Login'}},
        ],
    },
] as RouteObject[];

export default createBrowserRouter(routes);
