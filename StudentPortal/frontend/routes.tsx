import {createBrowserRouter, RouteObject} from 'react-router-dom';
import Home from "Frontend/views/home/Home";

export const routes = [
    {
        element: <Home/>,
        handle: {title: 'Main'},
        children: [
            // PATHS
            {path: '/', element: <Home/>, handle: {title: 'Hello World'}},
        ],
    },
] as RouteObject[];

export default createBrowserRouter(routes);
