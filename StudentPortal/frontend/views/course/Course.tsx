import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {Icon} from "@hilla/react-components/Icon";
import {GridSortColumn} from "@hilla/react-components/GridSortColumn";
import {Grid} from "@hilla/react-components/Grid";
import {useEffect, useState} from "react";
import {getModules} from "Frontend/generated/ModuleEndpoint";
import Details from "Frontend/components/Details";
import {GridColumn} from "@hilla/react-components/GridColumn";
import Module from "Frontend/generated/com/sesc/studentportal/model/Module";
import {useAuth} from "Frontend/auth";
import {getUserByUsername, updateRole} from "Frontend/generated/UserService";
import {EnrolmentEndpoint, IntegrationService, JpaUserDetailService} from "Frontend/generated/endpoints";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {Button} from "@hilla/react-components/Button";
import {registerStudent} from "Frontend/generated/StudentEndpoint";
import {Roles} from "Frontend/util/Constants";
import {getStudentByUser} from "Frontend/generated/UserEndpoint";


export default function Course() {

    // The module List to be displayed
    const [moduleList, setModuleList] = useState<Module[] | undefined>([])
    // The new list of modules after filtering
    const [filteredModules, setFilteredModules] = useState<Module[] | undefined>([])

    // Details State for managing the module details
    const [details, setDetails] = useState<Module[]>([]);

    const [currentStudent, setCurrentStudent] = useState<Student>();

    const [currentUser, setCurrentUser] = useState<User>();

    const [modulesEnrolled, setModulesEnrolled] = useState<Module[] | undefined>([]);

    const {state} = useAuth();

// Function to fetch the current student's enrollments
    async function getCurrentStudentEnrolments(student: Student | undefined) {
        try {
            const studentEnrolments = await EnrolmentEndpoint.getModulesFromEnrolments(student?.studentNumber);
            // Ensure studentEnrolments is not undefined
            if (studentEnrolments) {
                // Filter out undefined values if any
                const validEnrolments = studentEnrolments.filter(enrolment => enrolment !== undefined) as Module[];
                setModulesEnrolled(validEnrolments);
            } else {
                // If studentEnrolments is undefined, set modulesEnrolled to undefined
                setModulesEnrolled(undefined);
            }
        } catch (error) {
            console.error("Error fetching student enrolments:", error);
        }
    }

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
        });

        const getCurrentUser = async () => {
            const user = await getUserByUsername(state.user?.name);
            setCurrentUser(user);
            console.log(user);
            // Call getCurrentStudent here after setting currentUser
            await getCurrentStudent(user);
        };

        const getCurrentStudent = async (user: User | undefined) => {
            const student = user?.student;
            setCurrentStudent(student);
            if (!student) {
                console.log("User is not a student");
                return;
            }
            await getCurrentStudentEnrolments(student);
            console.log("CurrentStudent: ", student);
        };
        void getCurrentUser();
    }, [state]);


    return (
        <>
            <VerticalLayout className="flex flex-col l-auto items-center justify-center p-8">
                <h1 className="text-2xl font-semibold mb-8">Courses</h1>
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
                        {({item}) => {
                            if (modulesEnrolled?.find((module) => module.title === item.title)) {
                                return <Button className="primary" disabled>Enrolled</Button>
                            } else {
                                return <Button className="primary" onClick={async () => {
                                    if (!currentUser?.student) {
                                        // TODO: Produce an Invoice to send to the finance endpoint
                                        console.log("User is not a student");
                                        await updateRole(state.user?.name, Roles.student);
                                        await JpaUserDetailService.update(state.user);
                                        await registerStudent(state.user?.name);
                                        const student = await getStudentByUser(currentUser);
                                        // Creating Student in Library Service and Finance Service
                                        await IntegrationService.createStudentAccount(student?.studentNumber);
                                        await EnrolmentEndpoint.createEnrolment(student, item);
                                        // Sending invoice to the Finance Service
                                        const invoice = await EnrolmentEndpoint.createInvoice(student, item);
                                        console.log(invoice)
                                        window.location.reload();
                                    } else {
                                        await EnrolmentEndpoint.createEnrolment(currentStudent, item);
                                        const invoice = await EnrolmentEndpoint.createInvoice(currentStudent, item);
                                        console.log(invoice)
                                        await getCurrentStudentEnrolments(currentStudent);
                                    }
                                }}>
                                    Enroll
                                </Button>
                            }
                        }}

                    </GridColumn>
                </Grid>
            </VerticalLayout>
        </>
    )
}
