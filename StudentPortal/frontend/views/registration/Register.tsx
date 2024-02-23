import {useState} from "react";
import {FormLayout} from "@hilla/react-components/FormLayout";
import {TextField} from "@hilla/react-components/TextField";
import {Button} from "@hilla/react-components/Button.js";
import {PasswordField} from "@hilla/react-components/PasswordField";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {ErrorDialog} from "Frontend/components/ErrorDialog";
// import Role from "Frontend/generated/com/sesc/studentportal/model/Role";
import {UserEndpoint} from "Frontend/generated/endpoints";
import {EmailField} from "@hilla/react-components/EmailField";

// TODO: Switch to AUTO FORMS by Hilla later - Allows to update backend automatically
export default function Register() {

    const [user, setUser] = useState<User>(
        {username: '', password: '', roles: 'USER', firstname: '', surname: '', email: ''}
    );
    const [passwordConfirmation, setPasswordConfirmation] = useState<string>('');

    const [dialogOpened, setDialogOpened] = useState(false);

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
                        required
                        errorMessage="First Name Required"
                        autoCapitalize={"words"}
                        label="First Name"
                        onChange={(e) => setUser({...user, firstname: e.target.value})}
                        // style={{maxWidth}}
                    />
                    <TextField
                        required
                        errorMessage="Last Name Required"
                        autoCapitalize={"words"}
                        autocorrect={"on"}
                        label="Last Name"
                        onChange={(e) => setUser({...user, surname: e.target.value})}
                    />
                    <EmailField
                        required
                        label="Email address"
                        errorMessage="Enter a valid email address"
                        helperText="Only email addresses allowed"
                        onChange={(e) => setUser({...user, email: e.target.value})}
                    />
                    <TextField
                        required
                        errorMessage="User Name Required"
                        label="Username"
                        onChange={(e) => setUser({...user, username: e.target.value})}
                    />
                    <PasswordField
                        errorMessage="Password Required"
                        required
                        label="Password"
                        onChange={(e) => setUser({...user, password: e.target.value})}
                    />
                    <PasswordField
                        errorMessage="Password Confirmation"
                        required
                        label="Confirm Password"
                        onChange={(e) => setPasswordConfirmation(e.target.value)}
                    />

                </FormLayout>
            </div>

            <Button theme="primary"
                    onClick={async () => {
                        if (isPasswordSame(user.password, passwordConfirmation)) {
                            await handleRegistration();
                        } else {
                            setDialogOpened(true);
                        }
                    }}>Register
            </Button>

            {/*// Showing the dialog if there is an error*/}
            <ErrorDialog message={"Password not matching"} dialogOpened={dialogOpened} setDialogOpen={setDialogOpened}/>


            {/*{{dialogOpened} && <ErrorDialog isOpen={dialogOpened} message="Password not matching"/>}*/}

            {/*<Dialog opened={dialogOpened}>*/}
            {/*    <div className="p-4">*/}
            {/*        <h1 className="text-2xl font-bold">Error</h1>*/}
            {/*        <p>*/}
            {/*            Password not matching*/}
            {/*        </p>*/}
            {/*        <Button className="flex" onClick={() => {*/}
            {/*            setDialogOpened(false)*/}
            {/*        }}>*/}
            {/*            Close*/}
            {/*        </Button>*/}
            {/*    </div>*/}
            {/*</Dialog>*/}
        </div>
    );

    /**
     * Function to handle the registration of a user to the endpoint in the backend
     */
    async function handleRegistration() {

        console.log('Registering');
        // Use either the Controller or the Endpoint for the registration
        // let userToSend = await UserController.registerUser(user);
        let userToSend = await UserEndpoint.registerUser(user);
        console.log('User sent', userToSend);

    }

    /**
     * Function to check if the password and the password confirmation match
     * @param password the password from the user
     * @param passwordToCheck the confirmation password from the user
     */
    function isPasswordSame(password: string | undefined, passwordToCheck: string): boolean {
        return password === passwordToCheck;
    }
}