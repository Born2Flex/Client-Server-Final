import React from 'react';
import { Form } from 'react-router-dom';
import styles from './LoginForm.module.css';

function LoginForm() {
    return (
        <div className={styles.container}>
            <Form method="POST" className={styles.form}>
                <h2 className={styles.title}>Login</h2>
                <input type="text" placeholder="Username" name='username' className={styles.input} />
                <input type="password" placeholder="Password" name='password' className={styles.input} />
                <button type="submit" className={styles.button}>Submit</button>
            </Form>
        </div>
    );
}

export default LoginForm;
