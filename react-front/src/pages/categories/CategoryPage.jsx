import React from 'react'
import { useLoaderData } from 'react-router-dom';
import RowItem from '../../components/RowItem';

function CategoryPage() {
    const { category, categories } = useLoaderData();

    return (
        <>
            <div className='dashboard'>
                <h1>{category.name}</h1>
                <p>Description: <span className='target-text'>{category.description}</span></p>
                <p>Total Price: <span className='target-text'>{category.totalPrice}₴</span></p>
            </div>
            <table className='table'>
                <thead>
                    <tr>
                        {Object.keys(category.products[0]).map(key => key != 'categoryId' && (
                            <th key={key}>{key.toUpperCase()}</th>
                        ))}
                        <th>CATEGORY</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {category.products.map(product => (
                        <RowItem key={product.id} item={product} categoriesForDropdown={categories} />
                    ))}
                </tbody>
            </table>
        </>
    )
}

export default CategoryPage

export async function categoryLoader({ params }) {
    const categoryResponse = await fetch(`/api/categories/${params.id}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    });
    const category = await categoryResponse.json();
    console.log('Category:', category);

    const categoriesResponse = await fetch(`/api/categories`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    });
    const categories = await categoriesResponse.json();
    console.log('Categories:', categories);

    return { category, categories };
}