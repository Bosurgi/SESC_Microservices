import {useEffect, useState} from "react";
import {useAuth} from "Frontend/auth";
import Module from "Frontend/generated/com/sesc/studentportal/model/Module";
import {getUserByUsername} from "Frontend/generated/UserEndpoint";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {getModulesFromEnrolments} from "Frontend/generated/EnrolmentsService";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import Details from "Frontend/components/Details";
import {GridSortColumn} from "@hilla/react-components/GridSortColumn";
import {Grid} from "@hilla/react-components/Grid";

export default function Enrolments() {

    const [currentUser, setCurrentUser] = useState<User>();
    const [enrolledModules, setEnrolledModules] = useState<(Module | undefined)[] | undefined>([])
    const [details, setDetails] = useState<Module[]>([]);
    const [totalFees, setTotalFees] = useState<number>(0);
    const {state} = useAuth();

    useEffect(() => {
        // Fetch the current student from user
        getUserByUsername(state?.user?.name).then(user => {
            setCurrentUser(user)

            getModulesFromEnrolments(user?.student?.studentNumber).then(modules => {
                setEnrolledModules(modules)
                calculateTotalFees(modules)
            })
        })

    }, []);

    /**
     * Calculate the total fees of the modules the student is enrolled in
     * @param modules the list of modules
     */
    const calculateTotalFees = (modules: (Module | undefined)[] | undefined) => {
        if (modules) {
            let totalFee = 0;
            modules.forEach(module => {
                totalFee += module?.fee ? module.fee : 0
            })
            setTotalFees(totalFee)
        }
    };

    return (
        <>
            <div className="flex flex-col justify-center items-center">
                <h1 className="text-2xl font-semibold mb-8">Enrolments</h1>
                <Grid
                    items={enrolledModules}
                    theme="row-stripes"
                    detailsOpenedItems={details}
                    onActiveItemChanged={(e) => {
                        setDetails(e.detail.value ? [e.detail.value] : []);
                    }}
                    rowDetailsRenderer={({item: module}) => (
                        <VerticalLayout>
                            <Details details={module?.description}/>
                        </VerticalLayout>
                    )}>
                    <GridSortColumn path="title" header="Title" autoWidth resizable/>

                    <GridSortColumn path="fee" header="Fee" autoWidth>
                        {/* If the fee is not set, display "Free" else add £ symbol*/}
                        {({item}) => (item.fee ? `£${item.fee}` : 'Free')}
                    </GridSortColumn>
                </Grid>
                <div className="bg-gray-200 rounded-l w-auto font-semibold">
                    Total: £{totalFees}
                </div>
            </div>
        </>
    )

}