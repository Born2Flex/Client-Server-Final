import React from 'react';
import styles from './SearchInput.module.css';
import { useState } from 'react'
import { Form, useLocation } from 'react-router-dom'

function SearchInput() {
    const location = useLocation();
    const query = new URLSearchParams(location.search);
    const searchValue = query.get('search') || '';
    const [search, setSearch] = useState(searchValue)
    function onSearchChange(e) {
        setSearch(e.target.value)
    }
    return (
        <div className={styles.container}>
            <Form method='GET' className={styles.form}>
                <input type="search" name="name" value={search} onChange={onSearchChange} id="default-search" placeholder="Search..." className={styles.input} />
                <button type="submit" className={styles.button}>Search</button>
            </Form>
        </div>
    );
}

export default SearchInput;
