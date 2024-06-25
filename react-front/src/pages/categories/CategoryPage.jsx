import React from 'react'
import { useParams } from 'react-router-dom';

function CategoryPage() {
    const params = useParams();

    return (
        <><div>CategoryPage</div><p>{params.id}</p></>
    )
}

export default CategoryPage