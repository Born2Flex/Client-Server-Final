import React from 'react'
import NewItemButton from '../../components/NewItemButton'
import SearchInput from '../../components/SearchInput'
import styles from './CategoriesPage.module.css'

function CategoriesPage() {
    return (
        <>
            <div className='dashboard'>
                <SearchInput />
                <NewItemButton name={'Category'} />
            </div>
            <table className='table'>
                <thead>
                    <tr>
                        <th>Category 1</th>
                        <th>Category 2</th>
                        <th>Category 3</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Row 1, Cell 1</td>
                        <td>Row 1, Cell 2</td>
                        <td>Row 1, Cell 3</td>
                    </tr>
                    <tr>
                        <td>Row 2, Cell 1</td>
                        <td>Row 2, Cell 2</td>
                        <td>Row 2, Cell 3</td>
                    </tr>
                    <tr>
                        <td>Row 3, Cell 1</td>
                        <td>Row 3, Cell 2</td>
                        <td>Row 3, Cell 3</td>
                    </tr>
                    <tr>
                        <td>Row 4, Cell 1</td>
                        <td>Row 4, Cell 2</td>
                        <td>Row 4, Cell 3</td>
                    </tr>
                    <tr>
                        <td>Row 5, Cell 1</td>
                        <td>Row 5, Cell 2</td>
                        <td>Row 5, Cell 3</td>
                    </tr>
                </tbody>
            </table></>
    )
}

export default CategoriesPage