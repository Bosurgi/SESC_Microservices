import {useAuth} from "Frontend/auth";
import {useEffect, useState} from "react";
import {UserEndpoint, UserService} from "Frontend/generated/endpoints";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {Button} from "@hilla/react-components/Button";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";

export default function Profile() {
    const {state} = useAuth();
    const [user, setUser] = useState<User>();
    const [student, setStudent] = useState<Student>();
    const [editing, setEditing] = useState(false);

    const [formData, setFormData] = useState({
        firstname: "",
        surname: "",
        email: "",
    });

    useEffect(() => {
        UserService.getUserByUsername(state.user?.name).then((user) => {
            setUser(user);
            setFormData({
                firstname: user?.firstname || "",
                surname: user?.surname || "",
                email: user?.email || "",
            });
        });
    }, [state]);

    const handleInputChange = (e: any) => {
        const {name, value} = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: any) => {
        e.preventDefault();

        // Updated User with the data passed from the form
        const updatedUser: User = {
            ...user,
            firstname: formData.firstname,
            surname: formData.surname,
            email: formData.email,
        };
        // Update student details
        try {
            // Example update call
            await UserEndpoint.updateUserInformation(updatedUser);
            // Reload user details after update
            UserService.getUserByUsername(state.user?.name).then((user) => {
                setUser(user);
                setFormData({
                    firstname: user?.firstname || "",
                    surname: user?.surname || "",
                    email: user?.email || "",
                });
            });
            setEditing(false);
        } catch (error) {
            console.error("Error updating student details:", error);
        }
    };

    return (
        <div className="flex flex-col items-center justify-center p-8">
            <h1 className="text-2xl font-semibold mb-8">Profile</h1>
            <div className="bg-gray-100 p-8 rounded-lg shadow-lg">
                <p> Username: {state.user?.name}</p>
                {editing ? (
                    <form onSubmit={handleSubmit}>
                        <div className="mb-4">
                            <label className="block mb-2">First Name</label>
                            <input
                                type="text"
                                name="firstname"
                                value={formData.firstname}
                                onChange={handleInputChange}
                                className="border rounded-lg p-2"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block mb-2">Last Name</label>
                            <input
                                type="text"
                                name="surname"
                                value={formData.surname}
                                onChange={handleInputChange}
                                className="border rounded-lg p-2"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block mb-2">Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleInputChange}
                                className="border rounded-lg p-2"
                            />
                        </div>
                        <div className="flex justify-between">
                            <Button onClick={handleSubmit} theme="primary" className="p-4">
                                Save
                            </Button>
                            <Button onClick={() => setEditing(false)} theme="secondary" className="p-4">
                                Cancel
                            </Button>
                        </div>
                    </form>
                ) : (
                    <>
                        <p>First Name: {user?.firstname}</p>
                        <p>Last Name: {user?.surname}</p>
                        <p>Email: {user?.email}</p>
                        <p>Student ID: {user?.student?.studentNumber}</p>
                        <Button onClick={() => setEditing(true)} theme="primary" className="mt-4">
                            Edit Profile
                        </Button>
                    </>
                )}
            </div>
        </div>
    );
}