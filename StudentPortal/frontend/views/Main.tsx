import {useRouteMetadata} from "Frontend/util/routing";
import {useEffect} from "react";
import NavBar from "Frontend/components/NavBar";
import {Outlet} from "react-router-dom";
import {AppLayout} from "@hilla/react-components/AppLayout.js";

export default function Main() {
    const currentTitle = useRouteMetadata()?.title ?? 'My App';
    useEffect(() => {
        document.title = currentTitle;
    }, [currentTitle]);

    return (
        <AppLayout>
            <NavBar/> {/* This is where the navigation links will be rendered */}
            <Outlet/> {/* This is where the child routes will be rendered */}
        </AppLayout>
    );

}