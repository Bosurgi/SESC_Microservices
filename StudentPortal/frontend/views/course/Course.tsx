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
    // The new list of modules after filtering
    const [filteredModules, setFilteredModules] = useState<Module[]>([])

    // Fetch the modules from the backend and filter them
    useEffect(() => {
        getModules().then((modules) => {
            const newModules = modules.map((module) => ({
                ...module,
                title: module.title,
                description: module.description,
                fee: module.fee,
            }));
            setModuleList(newModules);
            setFilteredModules(newModules);
        })
    }, []);

    return (
        <>
            <VerticalLayout className="p-8">
                <TextField
                    placeholder="Search"
                    style={{width: '30%'}}
                    // Filtering the modules based on the search term
                    onValueChanged={(e) => {
                        const searchTerm = (e.detail.value.trim() || '').trim().toLowerCase();
                        setFilteredModules(
                            // Using only the title to filter the modules
                            moduleList.filter(
                                ({title}) =>
                                    !searchTerm ||
                                    title?.toLowerCase().includes(searchTerm)
                            ));
                    }}
                >
                    <Icon slot="prefix" icon="vaadin:search"/>
                </TextField>

                <Grid items={filteredModules}>
                    <GridSortColumn path="title" header="Title" autoWidth resizable/>

                    {/* TODO: On click display the module details */}

                    <GridSortColumn path="description" header="Description" autoWidth resizable/>

                    <GridSortColumn path="fee" header="Fee" autoWidth>
                        {/* If the fee is not set, display "Free" else add £ symbol*/}
                        {({item}) => (item.fee ? `£${item.fee}` : 'Free')}
                    </GridSortColumn>
                </Grid>
            </VerticalLayout>
        </>
    )
}
