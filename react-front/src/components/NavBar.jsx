import React from 'react';
import styles from './NavBar.module.css';
import { NavLink } from 'react-router-dom';

function NavBar() {
    return (
        <div className={styles.navbar}>
            <NavLink to={'products'} className={styles.button}>Products</NavLink>
            <NavLink to={'categories'} className={styles.button}>Categories</NavLink>
            <NavLink to={'login'} className={styles.button}>Login</NavLink>
        </div>
    );
}

export default NavBar;
