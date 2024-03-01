import {useAuth} from "Frontend/auth";
import {useEffect, useState} from "react";
import {UserEndpoint, UserService} from "Frontend/generated/endpoints";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";

export default function Profile() {

    const {state} = useAuth();

    const [user, setUser] = useState<User>();

    const [student, setStudent] = useState<Student>();

    useEffect(() => {
        UserService.getUserByUsername(state.user?.name).then((user) => {
            setUser(user);
            console.log(user);
        });
    }, [state]);


    useEffect(() => {
        if (user && user.userId) {
            // TODO: Need to call endpoint if not undefined
            UserEndpoint.getStudentByUser(user).then((student) => {
                setStudent(student);
                console.log(student);
            })
        }
    }, [user]);

    // TODO: Change the roles to be a Type or Enum and implement proper page
    return (
        <div>
            <h1>Profile</h1>
            <p>Name: {state.user?.name}</p>
            <p>Role: {state.user?.authorities.join(", ")}</p>
        </div>
    )
}