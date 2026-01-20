import { useDispatch } from "react-redux";
import { hideAlert, useAppState } from "../../lib/features/app/appSlice";
import { cn } from "../../lib/utils/utils";
import Modal from "./Modal";

export interface AlertInfo {
    title: string;
    description: string;
    onClose: () => void;
    buttons: { type: "yes" | "no"; text: string; action: () => void }[];
}

export default function AlertProvider() {
    const { alertInfo } = useAppState();
    const dispatch = useDispatch();

    return (
        <Modal
            isOpen={!!alertInfo}
            onClose={() => {
                dispatch(hideAlert());
                alertInfo?.onClose();
            }}
        >
            {!!alertInfo && (
                <div className=" p-2 rounded-sm  ">
                    <div className=" space-y-3">
                        <h1 className=" text-lg">{alertInfo.title}</h1>
                        <p className="text-sm">{alertInfo.description}</p>

                        <div className="flex flex-row gap-10 items-center mt-10">
                            {alertInfo.buttons?.map((x) => {
                                return (
                                    <button
                                        className={cn(
                                            "w-full px-4 py-2 text-white active:opacity-80 rounded-lg text-sm",
                                            x.type === "yes"
                                                ? "bg-green-400"
                                                : "bg-red-500"
                                        )}
                                        key={x.text}
                                        onClick={x.action}
                                    >
                                        {x.text}
                                    </button>
                                );
                            })}
                        </div>
                    </div>
                </div>
            )}
        </Modal>
    );
}
