import React from 'react'
import LoginForm from '../components/forms/LoginForm'
import { redirect } from 'react-router-dom'

function LoginPage() {
    return (
        <>
            <LoginForm />
        </>
    )
}

export default LoginPage

export async function loginAction({ request }) {
    const data = await request.formData();

    const authData = {
        username: data.get('username'),
        password: data.get('password'),
    };
    console.log('Auth data:', authData);

    try {
        const response = await fetch(`/api/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(authData)
        });
        const responseData = await response.text();
        console.log('Login response:', responseData);

        if (response.status === 401) {
            return { error: true, message: responseData.message };
        }
        if (!response.ok) {
            console.error(`Error ${response.status}: ${responseData}`);
            throw new Error(`Error ${response.status}: ${responseData}`);
        }

        const token = responseData;

        console.log('Logged in successfully:', responseData);

        localStorage.setItem('token', token);
    } catch (error) {
        console.error('Error logging in:', error);
    }

    return redirect('/');
}

export function logOutAction() {
    localStorage.removeItem('token');
    return redirect('/login');
}