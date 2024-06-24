import React from 'react';
import styles from './SearchInput.module.css';

function SearchInput() {
    return (
        <div className={styles.container}>
            <form className={styles.form}>
                <input type="text" placeholder="Search..." className={styles.input} />
                <button type="submit" className={styles.button}>Search</button>
            </form>
        </div>
    );
}

export default SearchInput;
