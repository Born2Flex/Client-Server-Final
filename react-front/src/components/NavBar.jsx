import React from 'react';
import styles from './NavBar.module.css';
import { Form, NavLink } from 'react-router-dom';

function NavBar({ token }) {
    return (
        <div className={styles.navbar}>
            <NavLink to={'/'} className={styles.button}>Home</NavLink>
            <NavLink to={'products'} className={styles.button}>Products</NavLink>
            <NavLink to={'categories'} className={styles.button}>Categories</NavLink>
            {token && (
                <Form action="/logout" method="post">
                    <button className={styles.button}>Logout</button>
                </Form>
            )}
            {!token && <NavLink to={'login'} className={styles.button}>Login</NavLink>}
        </div>
    );
}

export default NavBar;
