import React from 'react'
import SearchInput from '../../components/SearchInput'
import NewItemButton from '../../components/NewItemButton'

function ProductsPage() {
    return (
        <>
            <div className='dashboard'>
                <SearchInput />
                <NewItemButton name={'Product'} />
            </div>
            <table className='table'>
                <thead>
                    <tr>
                        <th>Product 1</th>
                        <th>Product 2</th>
                        <th>Product 3</th>
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
            </table>
        </>
    )
}

export default ProductsPage