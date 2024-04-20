import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {useEffect, useState} from "react";
import {useAuth} from "Frontend/auth";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";
import {getStudentByUser, getUserByUsername} from "Frontend/generated/UserEndpoint";
import {IntegrationService} from "Frontend/generated/endpoints";

export default function Graduation() {

    const [currentUser, setCurrentUser] = useState<User | undefined>();
    const [currentStudent, setCurrentStudent] = useState<Student>();
    const [graduationStatus, setGraduationStatus] = useState<boolean>();
    const {state} = useAuth();

    useEffect(() => {
        // Fetch the current student from user
        const getStudentStatus = async () => {
            const user = await getUserByUsername(state.user?.name);
            setCurrentUser(user);
            const student = await getStudentByUser(user);
            setCurrentStudent(student);
            if (student) {
                const account = await IntegrationService.getStudentPaymentStatus(student.studentNumber);
                if (account) {
                    setGraduationStatus(account.hasOutstandingBalance);
                }
            }
        }
        void getStudentStatus();
    }, [state]);


    return (
        <>
            <div className="flex flex-col justify-center items-center">
                <h1 className="text-2xl font-semibold mb-8">Graduation</h1>
            </div>
            {
                graduationStatus ?
                    <div className="flex flex-col justify-center items-center">
                        <p>You have an outstanding balance. Please pay your fees to graduate.</p>
                    </div> :
                    <div className="flex flex-col justify-center items-center">
                        <p>Congratulations! You are eligible to graduate.</p>
                    </div>
            }
        </>
    )

}