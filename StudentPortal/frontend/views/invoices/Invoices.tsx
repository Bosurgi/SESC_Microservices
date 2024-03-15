import React, {useEffect, useState} from "react";
import Student from "Frontend/generated/com/sesc/studentportal/model/Student";
import {useAuth} from "Frontend/auth";
import {getStudentByUser, getUserByUsername} from "Frontend/generated/UserEndpoint";
import User from "Frontend/generated/com/sesc/studentportal/model/User";
import {Item} from "@hilla/react-components/Item";
import {ListBox} from "@hilla/react-components/ListBox";
import {ToastContainer} from "react-toastify";
// Component for rendering a single invoice reference
// const InvoiceItem = ({reference}: { reference: string }) => {
//     return <li className="py-2">{reference}</li>;
// };
//
// // Component for rendering the list of invoice references
// const InvoiceList = ({references}: { references: string[] }) => {
//     return (
//         <div className="border rounded-lg p-4">
//             <ul className="list-none p-0">
//                 {references.map((reference, index) => (
//                     <InvoiceItem key={index} reference={reference}/>
//                 ))}
//             </ul>
//         </div>
//     );
// };

export default function Invoices() {

    const [currentStudent, setCurrentStudent] = useState<Student>();
    const [currentUser, setCurrentUser] = useState<User>();
    const [invoiceReferences, setInvoiceReferences] = useState<string[]>([]);
    const [selectedReferences, setSelectedReferences] = useState<string[]>([]);
    const {state} = useAuth();

    /**
     * Fetch the current student from the user and set the invoice references state.
     */
    useEffect(() => {
        // Fetch the current student from user
        getUserByUsername(state?.user?.name).then(user => {
            setCurrentUser(user)

            getStudentByUser(user).then(student => {
                setCurrentStudent(student);
                // Update invoice references after getting the student
                setInvoiceReferences(student?.invoiceReferenceNumber?.split(",").slice(0, -1) || []);
            })
        });

    }, [state?.user?.name]); // Added state.user.name as dependency

    function onSelectedChanged(reference: string) {
        if (!selectedReferences.includes(reference)) {
            // Adding the selected references to the state
            setSelectedReferences([...selectedReferences, reference]);
        } else {
            // Removing the references if they are already selected
            setSelectedReferences(selectedReferences.filter(ref => ref !== reference));
        }
    }

    function copyToClipboard(reference: string) {
        navigator.clipboard.writeText(reference).then(() => {

            console.log("Copied to clipboard: " + reference);
        });
    }

    // const showToastMessage = () => {
    //     toast("Copied to Clipboard", {
    //         autoClose: 1000,
    //     });
    // };

    return (
        <div className="flex flex-col justify-center items-center">
            <h1 className="text-2xl font-semibold mb-8">Invoices References</h1>
            <p className="p-8">Please use the reference numbers to pay your fees into the Finance portal.</p>
            <ToastContainer/>
            {invoiceReferences.length > 0 ? (
                // TODO: Adding paid selected logic
                <ListBox multiple>
                    {invoiceReferences.map((reference, index) => (
                        <Item onClick={() => {
                            console.log(reference)
                            copyToClipboard(reference)
                            onSelectedChanged(reference);
                            // showToastMessage();
                            console.log(selectedReferences)
                        }} key={index} value={reference}>{reference}</Item>
                    ))}
                </ListBox>
            ) : (
                <p>No invoice references found.</p>
            )}
        </div>
    );
}