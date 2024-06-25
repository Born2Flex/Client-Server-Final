import React, { useState } from 'react';
import styles from './NewItemButton.module.css';
import { Form } from 'react-router-dom';

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
                        <Form method='POST' action='new' onSubmit={closeDialog} className={styles.form}>
                            <h2 className={styles.title}>Create New {name}</h2>
                            <input type="text" name='name' placeholder={`${name} Name`} className={styles.input} />
                            <input type="text" name='description' placeholder={`${name} Description`} className={styles.input} />
                            <button type="submit" className={styles.submitButton}>Create</button>
                            <button type="button" className={styles.closeButton} onClick={closeDialog}>Close</button>
                        </Form>
                    </dialog>
                </>
            )}
        </div>
    );
}

export default NewItemButton;
