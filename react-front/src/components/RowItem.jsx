import React, { useState } from 'react';
import styles from './RowItem.module.css';
import { Link, NavLink } from 'react-router-dom';

function RowItem({ category }) {
    const [isEditing, setIsEditing] = useState(false);
    const [categoryData, setCategoryData] = useState(category);

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
        setCategoryData(category);
    };

    const handleDeleteClick = () => {
        try {
            fetch(`/api/categories/${categoryData.id}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('token')
                }
            });
            console.log(`Delete category with ID: ${categoryData.id}`);
            window.location.reload();

        } catch (error) {
            console.error('Error deleting category:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        setCategoryData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSaveClick = () => {

        try {
            const { id, ...dataWithoutId } = categoryData;
            fetch(`/api/categories/${categoryData.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify(dataWithoutId)
            });
            console.log('Saved category:', categoryData);
            setIsEditing(false);
        }
        catch (error) {
            console.error('Error saving category:', error);
        }

    };

    return (
        <tr className={styles.row}>
            {isEditing ? (
                <>
                    <td>
                        <input name="id" value={categoryData.id} onChange={handleChange} readOnly hidden />
                        {categoryData.id}
                    </td>
                    <td><input name="name" value={categoryData.name} onChange={handleChange} /></td>
                    <td><input name="description" value={categoryData.description} onChange={handleChange} /></td>
                    <td>
                        <button className={styles.saveButton} onClick={handleSaveClick}>Save</button>
                        <button className={styles.cancelButton} onClick={handleCancelClick}>Cancel</button>
                        <button className={styles.deleteButton} onClick={handleDeleteClick}>Delete</button>
                    </td>
                </>
            ) : (
                <>
                    <td>{categoryData.id}</td>
                    <td><Link to={`${categoryData.id}`} className={styles.link}>{categoryData.name}</Link></td>
                    <td>{categoryData.description}</td>
                    <td>
                        <button className={styles.editButton} onClick={handleEditClick}>Edit</button>
                        {/* <Link to={`${categoryData.id}`} className={styles.editButton}>View</Link> */}
                    </td>
                </>
            )}
        </tr>
    );
}

export default RowItem;
