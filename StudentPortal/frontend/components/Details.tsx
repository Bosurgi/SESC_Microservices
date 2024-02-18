import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";

type DetailsProps = {
    details?: string;
}
export default function Details({details}: DetailsProps) {
    return (
        <VerticalLayout className="outline outline-1 outline-blue-200 p-4">
            <h1 className="font-bold">Details</h1>
            <HorizontalLayout className="">
                <p className="p-4">{details}</p>
            </HorizontalLayout>

        </VerticalLayout>
    );
}