import {useAuth} from "Frontend/auth";

export default function Profile() {

    const {state} = useAuth();

    // TODO: Change the roles to be a Type or Enum and implement proper page
    return (
        <div>
            <h1>Profile</h1>
            <p>Name: {state.user?.name}</p>
            <p>Role: {state.user?.authorities.join(", ")}</p>
        </div>
    )
}