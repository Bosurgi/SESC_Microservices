import {useAuth} from "Frontend/auth";
import {useEffect, useState} from "react";
import {UserService} from "Frontend/generated/endpoints";
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

    return (
        <div>
            <h1>Profile</h1>
            <p>Name: {state.user?.name}</p>
            <p>Role: {state.user?.authorities.join(", ")}</p>
            <p>Student ID: {user?.student?.studentNumber}</p>
        </div>
    )
}