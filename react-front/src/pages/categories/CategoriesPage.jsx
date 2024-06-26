import React from 'react'
import { useLoaderData, redirect } from 'react-router-dom'
import NewItemButton from '../../components/NewItemButton'
import SearchInput from '../../components/SearchInput'
import RowItem from '../../components/RowItem';

function CategoriesPage() {
    const { categories } = useLoaderData();
    console.log('Categories:', categories);

    return (
        <>
            <div className='dashboard'>
                <SearchInput />
                <NewItemButton name={'Category'} />
            </div>
            <table className='table'>
                <thead>
                    <tr>
                        {Object.keys(categories[0]).map(key => (
                            <th key={key}>{key.toUpperCase()}</th>
                        ))}
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {categories.map(category => (
                        <RowItem key={category.id} item={category} />
                    ))}
                </tbody>
            </table>
        </>
    )
}

export default CategoriesPage

export async function createCategoryAction({ request }) {
    const data = await request.formData();

    const categoryData = {
        name: data.get('name'),
        description: data.get('description'),
    };
    console.log('Category data:', categoryData);

    try {
        const response = await fetch(`/api/categories`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify(categoryData)
        });
        const responseData = await response.text();
        console.log('Create category response:', responseData);

        if (response.status === 409) {
            console.log("returning error")
            return { error: true, message: responseData };
        }

        if (!response.ok) {
            console.error(`Error ${response.status}: ${responseData}`);
            throw new Error(`Error ${response.status}: ${responseData}`);
        }

        console.log('Category created successfully:', responseData);
    } catch (error) {
        console.error('Error creating category:', error);
    }

    window.location.reload();
    return redirect('/categories');
}

export async function categoriesLoader({ request }) {

    const token = localStorage.getItem('token');
    if (!token) {
        return redirect('/login');
    }

    const url = new URL(request.url);
    const search = url.searchParams.get("name");

    try {
        const response = await fetch(`/api/categories${search ? '?name=' + search : ''}`, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        const responseData = await response.json();
        console.log('Categories:', responseData);

        if (!response.ok) {
            console.error(`Error ${response.status}: ${responseData}`);
            throw new Error(`Error ${response.status}: ${responseData}`);
        }

        return { categories: responseData };
    }
    catch (error) {
        console.error('Error loading categories:', error);
    }

    return { categories: [] };
}