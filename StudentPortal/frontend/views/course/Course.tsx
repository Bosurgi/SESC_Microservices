import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {Icon} from "@hilla/react-components/Icon";
import {GridSortColumn} from "@hilla/react-components/GridSortColumn";
import {Grid} from "@hilla/react-components/Grid";
import {useEffect, useState} from "react";
import {getModules} from "Frontend/generated/ModuleEndpoint";
import Details from "Frontend/components/Details";
import {GridColumn} from "@hilla/react-components/GridColumn";
import {Button} from "@hilla/react-components/Button";
import Module from "Frontend/generated/com/sesc/studentportal/model/Module";
import {useAuth} from "Frontend/auth";
import {findStudentFromUser, getUserByUsername} from "Frontend/generated/UserService";
import {registerStudent} from "Frontend/generated/StudentEndpoint";
import {updateRole} from "Frontend/generated/UserEndpoint";
import {EnrolmentEndpoint, JpaUserDetailService} from "Frontend/generated/endpoints";
import {Roles} from "Frontend/util/Constants";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";


export default function Course() {

    // The module List to be displayed
    const [moduleList, setModuleList] = useState<Module[] | undefined>([])
    // The new list of modules after filtering
    const [filteredModules, setFilteredModules] = useState<Module[] | undefined>([])

    // Details State for managing the module details
    const [details, setDetails] = useState<Module[]>([]);

    const [student, setStudent] = useState<Student>();

    const {state} = useAuth();

    // Fetch the modules from the backend and filter them
    useEffect(() => {
        getModules().then((modules) => {
            const newModules = modules?.map((module) => ({
                ...module,
                title: module?.title,
                description: module?.description,
                fee: module?.fee,
            }));
            setModuleList(newModules);
            setFilteredModules(newModules);
        })
    }, [state]);

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
                            moduleList?.filter(
                                ({title}) =>
                                    !searchTerm ||
                                    title?.toLowerCase().includes(searchTerm)
                            ));
                    }}
                >
                    <Icon slot="prefix" icon="vaadin:search"/>
                </TextField>

                <Grid items={filteredModules}
                      theme="row-stripes"
                      detailsOpenedItems={details}
                      onActiveItemChanged={(e) => {
                          setDetails(e.detail.value ? [e.detail.value] : []);
                      }}
                      rowDetailsRenderer={({item: module}) => (
                          <VerticalLayout>
                              <Details details={module.description}/>
                          </VerticalLayout>
                      )}
                >
                    <GridSortColumn path="title" header="Title" autoWidth resizable/>

                    {/*<GridSortColumn path="description" header="Description" autoWidth resizable/>*/}

                    <GridSortColumn path="fee" header="Fee" autoWidth>
                        {/* If the fee is not set, display "Free" else add £ symbol*/}
                        {({item}) => (item.fee ? `£${item.fee}` : 'Free')}
                    </GridSortColumn>

                    <GridColumn>
                        {({item}) => (
                            <Button className="primary" onClick={async () => {
                                // Enrol the user to the module here
                                // TODO: Implement the enrolment logic for the backend
                                const user = await getUserByUsername(state.user?.name);
                                const student = await findStudentFromUser(user);
                                setStudent(student);
                                console.log(student)

                                if (!user?.student) {
                                    console.log("User is not a student");
                                    await updateRole(state.user?.name, Roles.student);
                                    await JpaUserDetailService.update(state.user);
                                    const student = await registerStudent(state.user?.name);
                                    console.log(student);
                                    console.log(`Enrolling ${state.user?.name} to ${item.title}`);
                                    console.log(item)
                                    await EnrolmentEndpoint.createEnrolment(student, item)
                                    window.location.reload();
                                } else {
                                    await EnrolmentEndpoint.createEnrolment(student, item)
                                }
                            }}>
                                Enroll
                            </Button>)}
                    </GridColumn>
                </Grid>
            </VerticalLayout>
        </>
    )
}
