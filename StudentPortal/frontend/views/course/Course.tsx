import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {Icon} from "@hilla/react-components/Icon";
import {GridSortColumn} from "@hilla/react-components/GridSortColumn";
import {Grid} from "@hilla/react-components/Grid";
import Module from "Frontend/generated/com/sesc/studentportal/model/Module";
import {useEffect, useState} from "react";
import {getModules} from "Frontend/generated/ModuleEndpoint";


export default function Course() {

    // The module List to be displayed
    const [moduleList, setModuleList] = useState<Module[]>([])
    // The search term to filter the module list
    const [searchTerm, setSearchTerm] = useState<string>('');

    // Calling the function to populate the module list from the Module Endpoint
    useEffect(() => {
        getModules().then((modules) => setModuleList(modules));
    }, []);

    return (
        <>
            <VerticalLayout className="p-8">
                <TextField
                    placeholder="Search"
                    style={{width: '50%'}}
                >
                    <Icon slot="prefix" icon="vaadin:search"/>
                </TextField>

                <Grid items={moduleList}>
                    <GridSortColumn path="title" header="Title"/>
                    <GridSortColumn path="description" header="Description"/>
                    <GridSortColumn path="fee" header="Fee"/>
                </Grid>
            </VerticalLayout>
        </>
    )
}
