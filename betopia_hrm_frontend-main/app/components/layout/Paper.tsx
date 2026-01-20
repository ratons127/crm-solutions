import { PropsWithChildren } from "react";

export default function Paper(props: PropsWithChildren) {
    return (
        <div className="p-6 bg-white rounded-lg shadow max-w-full space-y-3">
            {props.children}
        </div>
    );
}
