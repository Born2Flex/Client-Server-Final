import React from 'react'
import { useLoaderData } from 'react-router-dom';

function ProductPage() {
    const { product } = useLoaderData();
    return (
        <div className='heading-block'>
            <div>
                <h1>{product.name}</h1>
                <p>Description: <span className='target-text'>{product.description}</span></p>
                <p>Producer: <span className='target-text'>{product.producer}</span></p>
                <p>Price: <span className='target-text'>{product.price}₴</span></p>
                <p>Amount: <span className='target-text'>{product.amount}</span></p>
                <p>Category Name: <span className='target-text'>{product.categoryName}</span></p>
                <h2>Total Price: {product.totalPrice}₴</h2>
            </div>
        </div>
    )
}

export default ProductPage

export async function productLoader({ params }) {

    const response = await fetch(`/api/products/${params.id}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    });
    const product = await response.json();
    console.log('Product:', product);

    return { product };
}