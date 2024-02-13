import MainLayout from 'Frontend/views/MainLayout.js';
import {lazy} from 'react';
import {createBrowserRouter, RouteObject} from 'react-router-dom';

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));
const HomeView = lazy(async () => import('Frontend/views/helloworld/HelloWorldView.js'));

export const routes = [
    {
        element: <MainLayout/>,
        handle: {title: 'Main'},
        children: [
            {path: '', element: <HomeView/>, handle: {title: 'Hello World'}},
            {path: '/about', element: <AboutView/>, handle: {title: 'About'}},
        ],
    },
] as RouteObject[];

export default createBrowserRouter(routes);
