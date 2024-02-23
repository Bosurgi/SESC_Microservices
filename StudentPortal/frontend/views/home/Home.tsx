import {useAuth} from "Frontend/auth";

export default function Home() {
    // const [name, setName] = useState('');
    const {state, login} = useAuth();

    console.log(state.user?.authorities)

    return (
        <>
            <div className="flex flex-col min-h-screen">
                <section className="">
                    {/*<NavBar/>*/}
                </section>

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
