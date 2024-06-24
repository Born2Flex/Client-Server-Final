import React, { useState } from 'react';
import styles from './NewItemButton.module.css';

function NewItemButton({ name }) {
    const [isOpen, setIsOpen] = useState(false);

    const openDialog = () => setIsOpen(true);
    const closeDialog = () => setIsOpen(false);

    return (
        <div>
            <button className={styles.button} onClick={openDialog}>New {name}</button>
            {isOpen && (
                <>
                    <div className={styles.backdrop} onClick={closeDialog}></div>
                    <dialog open className={styles.dialog}>
                        <form className={styles.form}>
                            <h2 className={styles.title}>Create New {name}</h2>
                            <input type="text" placeholder={`${name} Name`} className={styles.input} />
                            <input type="text" placeholder={`${name} Description`} className={styles.input} />
                            <button type="submit" className={styles.submitButton}>Create</button>
                            <button type="button" className={styles.closeButton} onClick={closeDialog}>Close</button>
                        </form>
                    </dialog>
                </>
            )}
        </div>
    );
}

export default NewItemButton;
