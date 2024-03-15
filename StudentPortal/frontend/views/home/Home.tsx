import {useAuth} from "Frontend/auth";

export default function Home() {

    const {state} = useAuth();

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
                                <p className="text-lg justify-center">Welcome to the student portal {state.user.name}.
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


                <section
                    className="w-full flex-1 flex gap-8 flex-col items-center justify-center md:grid md:grid-cols-2 lg:grid-cols-3">

                </section>

                <section className="w-full flex justify-center py-4">
                    <a className="underline underline-offset-[8px]" href="https://github.com/Bosurgi">Made by Andrea</a>
                </section>
            </div>
        </>
    );
}
