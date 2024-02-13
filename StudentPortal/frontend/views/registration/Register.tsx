import {useState} from "react";
import {FormLayout} from "@hilla/react-components/FormLayout";
import {TextField} from "@hilla/react-components/TextField";
import {Button} from "@hilla/react-components/Button.js";

export default function Register() {
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirmation, setPasswordConfirmation] = useState('');

    const responsiveSteps = [
        {minWidth: '0', columns: 1},
        {minWidth: '400px', columns: 1},
    ];

    const maxWidth = '500px';

    return (
        <div className="flex flex-col items-center justify-center p-4">
            <div className="grid grid-cols-1 gap-8 place-content-center place-items-center">
                <FormLayout>
                    <TextField
                        label="First Name"
                        // style={{maxWidth}}
                    />
                    <TextField label="Last Name"/>
                    <TextField label="Email"/>
                    <TextField label="Username"/>
                    <TextField label="Password"/>
                    <TextField label="Confirm Password"/>


                </FormLayout>
            </div>
            <Button theme="primary"
                    onClick={() => handleRegistration()}>Register</Button>
        </div>
    );

    function handleRegistration() {
        console.log('Registering');
        // TODO: Implement registration
    }
}