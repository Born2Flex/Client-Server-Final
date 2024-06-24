import React from 'react';
import styles from './LoginForm.module.css';

function LoginForm() {
    return (
        <div className={styles.container}>
            <form className={styles.form}>
                <h2 className={styles.title}>Login</h2>
                <input type="text" placeholder="Login" className={styles.input} />
                <input type="password" placeholder="Password" className={styles.input} />
                <button type="submit" className={styles.button}>Submit</button>
            </form>
        </div>
    );
}

export default LoginForm;
