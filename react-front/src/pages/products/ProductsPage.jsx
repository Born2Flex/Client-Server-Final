import React from 'react'
import { useLoaderData, redirect } from 'react-router-dom'
import NewItemButton from '../../components/NewItemButton'
import SearchInput from '../../components/SearchInput'
import RowItem from '../../components/RowItem';

function ProductsPage() {
    const { products, categories } = useLoaderData();
    console.log('products:', products);

    return (
        <>
            <div className='dashboard'>
                <SearchInput />
                <NewItemButton name={'Product'} categoriesForDropdown={categories} />
            </div>
            <table className='table'>
                <thead>
                    <tr>
                        {Object.keys(products[0]).map(key => key != 'categoryId' && (
                            <th key={key}>{key.toUpperCase()}</th>
                        ))}
                        <th>CATEGORY</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <RowItem key={product.id} item={product} categoriesForDropdown={categories} />
                    ))}
                </tbody>
            </table>
        </>
    )
}

export default ProductsPage

export async function createProductAction({ request }) {
    const data = await request.formData();

    const productData = {
        name: data.get('name'),
        description: data.get('description'),
        producer: data.get('producer'),
        price: data.get('price'),
        amount: data.get('amount'),
        categoryId: data.get('categoryId')
    };
    console.log('Product data:', productData);

    try {
        const response = await fetch(`/api/products`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify(productData)
        });
        const responseData = await response.text();
        console.log('Create product response:', responseData);

        if (response.status === 409) {
            console.log("returning error")
            return { error: true, message: responseData };
        }

        if (!response.ok) {
            console.error(`Error ${response.status}: ${responseData}`);
            throw new Error(`Error ${response.status}: ${responseData}`);
        }

        console.log('Product created successfully:', responseData);
    } catch (error) {
        console.error('Error creating product:', error);
    }

    window.location.reload();
    return redirect('/products');
}

export async function productsLoader({ request }) {
    const token = localStorage.getItem('token');
    if (!token) {
        return redirect('/login');
    }

    const url = new URL(request.url);
    const search = url.searchParams.get("name");

    try {
        const productsResponse = await fetch(`/api/products${search ? '?name=' + search : ''}`, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        const responseData = await productsResponse.json();
        console.log('products:', responseData);

        if (!productsResponse.ok) {
            console.error(`Error ${productsResponse.status}: ${responseData}`);
            throw new Error(`Error ${productsResponse.status}: ${responseData}`);
        }

        const categoriesResponse = await fetch(`/api/categories`, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        });
        const categoriesData = await categoriesResponse.json();
        console.log('categories:', categoriesData);

        if (!categoriesResponse.ok) {
            console.error(`Error ${categoriesResponse.status}: ${categoriesData}`);
            throw new Error(`Error ${categoriesResponse.status}: ${categoriesData}`);
        }

        return {
            products: responseData,
            categories: categoriesData
        };
    }
    catch (error) {
        console.error('Error loading products:', error);
    }

    return {
        products: [],
        categories: []
    };
}