import { hideModal, useAppState } from "../../lib/features/app/appSlice";
import { useAppDispatch } from "../../lib/hooks";
import Modal from "./Modal";

export default function ModalProvider() {
    const dispatch = useAppDispatch();
    const { modalComponent } = useAppState();
    return (
        <Modal
            isOpen={!!modalComponent}
            onClose={() => {
                dispatch(hideModal());
            }}
        >
            {modalComponent}
        </Modal>
    );
}
