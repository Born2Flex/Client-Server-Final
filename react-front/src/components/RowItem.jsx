import React, { useState } from 'react';
import styles from './RowItem.module.css';
import { Link } from 'react-router-dom';

function RowItem({ item, categoriesForDropdown = null }) {
    const [isEditing, setIsEditing] = useState(false);
    const [itemData, setItemData] = useState(item);
    const [error, setError] = useState(null);
    const itemType = categoriesForDropdown ? 'products' : 'categories';

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
        setItemData(item);
    };

    const handleDeleteClick = () => {
        try {
            fetch(`/api/${itemType}/${itemData.id}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('token')
                }
            });
            console.log(`Delete item with ID: ${itemData.id}`);
            window.location.reload();

        } catch (error) {
            console.error('Error deleting item:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        setItemData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const validateMin = (e) => {
        if (e.target.value === '' || e.target.value === null) {
            e.target.value = ''
        }
        else if (Number(e.target.value) < Number(e.target.min)) {
            e.target.value = e.target.min;
        }
        const { name, value } = e.target;

        setItemData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSaveClick = async () => {
        try {
            const { id, ...dataWithoutId } = itemData;
            const response = await fetch(`/api/${itemType}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify(dataWithoutId)
            });
            console.log('Save item response:', response);
            const responseData = await response.text();
            console.log(responseData)

            if (response.status === 409) {
                console.log("returning error")
                setError(responseData);
                return;
            }

            setIsEditing(false);
            window.location.reload();

        }
        catch (error) {
            console.error('Error saving item:', error);
        }
    };

    return (
        <>
            <tr className={styles.row}>
                {isEditing ? (
                    <>
                        <td>
                            <input name="id" value={itemData.id} onChange={handleChange} readOnly hidden />
                            {itemData.id}
                        </td>
                        <td>
                            <input name="name" minLength={1} value={itemData.name} onChange={handleChange} />
                            {error && <div className='error'>{error}</div>}
                        </td>
                        <td><input name="description" minLength={1} value={itemData.description} onChange={handleChange} /></td>

                        {categoriesForDropdown && (
                            <>
                                <td><input name="producer" minLength={1} value={itemData.producer} onChange={handleChange} /></td>
                                <td><input type='number' min={0.01} name="price" value={itemData.price} onChange={validateMin} /></td>
                                <td>
                                    <p className={styles.smallText}>Add/Write off</p>
                                    <input type='number' min={-1 * itemData.amount} name="increment" value={itemData.increment} onChange={validateMin} />
                                </td>
                                <td>
                                    <select name='categoryId' value={itemData.categoryId} onChange={handleChange}>
                                        {categoriesForDropdown.map(category => (
                                            <option key={category.id} value={category.id}>{category.name}</option>
                                        ))}
                                    </select>
                                </td>
                            </>
                        )}
                        <td>
                            <button className={styles.saveButton} onClick={handleSaveClick}>Save</button>
                            <button className={styles.cancelButton} onClick={handleCancelClick}>Cancel</button>
                            <button className={styles.deleteButton} onClick={handleDeleteClick}>Delete</button>
                        </td>
                    </>
                ) : (
                    <>
                        <td>{itemData.id}</td>
                        <td><Link to={`/${itemType}/${itemData.id}`} className={styles.link}>{itemData.name}</Link></td>
                        <td>{itemData.description}</td>
                        {categoriesForDropdown && (
                            <>
                                <td>{itemData.producer}</td>
                                <td>{itemData.price} ₴</td>
                                <td>{itemData.amount}</td>
                                <td>{categoriesForDropdown.find(category => category.id == itemData.categoryId).name}</td>
                            </>
                        )}
                        <td>
                            <button className={styles.editButton} onClick={handleEditClick}>Edit</button>
                        </td>
                    </>
                )}
            </tr>
        </>
    );
}

export default RowItem;
