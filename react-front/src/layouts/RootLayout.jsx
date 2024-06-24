import React from 'react'
import { Outlet, useLoaderData, redirect } from 'react-router-dom';
import NavBar from '../components/NavBar'
import styles from './RootLayout.module.css';

function RootLayout() {
    const { token } = useLoaderData();
    return (
        <div className={styles.root}>
            <NavBar token={token} />
            <main className={styles.main}>
                <Outlet />
            </main>
        </div>
    )
}

export default RootLayout

export async function loader() {
    const token = localStorage.getItem('token');
    if (!token) {
        return { token: null };
    }
    return { token };
}