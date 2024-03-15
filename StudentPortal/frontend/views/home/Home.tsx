import {useAuth} from "Frontend/auth";
import {useEffect, useState} from "react";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {getUserByUsername} from "Frontend/generated/UserEndpoint";

export default function Home() {

    const {state} = useAuth();
    const [currentUser, setCurrentUser] = useState<User>();

    /**
     * Fetching the current user from the Backend by checking for its username.
     */
    useEffect(() => {
        const getUser = async () => {
            const user = await getUserByUsername(state.user?.name)
            if (user) {

                setCurrentUser(user)
            }
        }
        void getUser();
    }, [state]);


    return (
        <>
            <div className="flex flex-col min-h-screen">

                {!state.user ?
                    (<section className="">
                        <div className="flex flex-col items-center justify-center gap-8 p-4">
                            <h1 className="text-4xl font-semibold">Student Portal</h1>
                            <p className="text-lg justify-center">Welcome to the student portal. Here
                                you
                                can find all
                                the information
                                regarding your courses, your enrolments and your graduation.
                                Please Login to continue.
                            </p>
                        </div>

                    </section>) :
                    (
                        <section className="">
                            <div className="flex flex-col items-center justify-center gap-8 p-4">
                                <h1 className="text-4xl font-semibold">Student Portal</h1>
                                <p className="text-lg justify-center">Welcome to the student
                                    portal {currentUser?.firstname} {currentUser?.surname}.
                                    Here
                                    you
                                    can find all
                                    the information
                                    regarding your courses, your enrolments and your graduation.
                                </p>
                            </div>

                        </section>
                    )
                }

                <section className="w-full flex justify-center py-4">
                    <a className="underline underline-offset-[8px]" href="https://github.com/Bosurgi">Made by Andrea La
                        Fauci</a>
                </section>
            </div>
        </>
    );
}
