import React from 'react'
import { Outlet, redirect, useLoaderData } from 'react-router-dom';
import NavBar from '../components/NavBar'
import styles from './RootLayout.module.css';

function RootLayout() {
    return (
        <div className={styles.root}>
            <NavBar />
            <main className={styles.main}>
                <Outlet />
            </main>
        </div>
    )
}

export default RootLayout