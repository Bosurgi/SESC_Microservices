import {Dialog} from "@hilla/react-components/Dialog";
import {Button} from "@hilla/react-components/Button";

/**
 * The props for the ErrorDialog component
 */
type ErrorDialogProps = {
    message: string;
    dialogOpened: boolean;
    setDialogOpen: (isOpen: boolean) => void;
}

/**
 * Error Dialog component to show a specified error message
 * @param message the message to show to the user
 * @param dialogOpened the state of the dialog if open or not
 * @param setDialogOpen the function to set the state of the dialog
 * @constructor
 */
export function ErrorDialog({message, dialogOpened, setDialogOpen}: ErrorDialogProps) {
    return (
        <Dialog opened={dialogOpened}>
            <div className="p-4">
                <h1 className="text-2xl font-bold">Error</h1>
                <p>
                    {message}
                </p>
                <Button className="flex" onClick={() => {
                    setDialogOpen(false)
                }}>
                    Close
                </Button>
            </div>
        </Dialog>
    )
}


