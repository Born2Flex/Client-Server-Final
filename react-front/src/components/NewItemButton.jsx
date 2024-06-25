import React, { useState } from 'react';
import styles from './NewItemButton.module.css';
import { Form } from 'react-router-dom';

export function validateMin(e) {
    if (e.target.value === '' || e.target.value === null) {
        return
    }
    if (e.target.value < e.target.min) {
        e.target.value = e.target.min;
    }
}

function NewItemButton({ name, categoriesForDropdown = null }) {
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
                            {
                                categoriesForDropdown && (
                                    <>
                                        <input type="text" name='producer' placeholder={'Product Producer'} className={styles.input} />
                                        <input type="number" name='amount' min={1} onBlur={validateMin} placeholder={'Product Amount'} className={styles.input} />
                                        <input type="number" name='price' min={0.01} step={0.01} onBlur={validateMin} placeholder={'Product Price'} className={styles.input} />

                                        <select name='categoryId' className={styles.input}>
                                            {categoriesForDropdown.map(category => (
                                                <option key={category.id} value={category.id}>{category.name}</option>
                                            ))}
                                        </select>
                                    </>
                                )
                            }
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
