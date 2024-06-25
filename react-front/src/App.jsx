import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import RootLayout, { loader as rootLoader } from './layouts/RootLayout';
import ErrorPage from './pages/ErrorPage';
import HomePage from './pages/HomePage';
import LoginPage, { loginAction, logOutAction } from './pages/LoginPage';
import ProductsPage, { createProductAction, productsLoader } from './pages/products/ProductsPage';
import ProductPage, { productLoader } from './pages/products/ProductPage';
import CategoriesPage, { createCategoryAction, categoriesLoader } from './pages/categories/CategoriesPage';
import CategoryPage, { categoryLoader } from './pages/categories/CategoryPage';

function App() {
  const router = createBrowserRouter([
    {
      path: '/',
      element: <RootLayout />,
      errorElement: <ErrorPage />,
      loader: rootLoader,
      id: 'root',
      children: [
        { index: true, element: <HomePage /> },
        {
          path: 'login',
          element: <LoginPage />,
          action: loginAction,
        },
        {
          path: 'logout',
          action: logOutAction,
        },
        {
          path: 'products',
          children: [
            {
              index: true,
              element: <ProductsPage />,
              loader: productsLoader,
              action: createProductAction,
            },
            {
              path: ':id',
              element: <ProductPage />,
              loader: productLoader,
            },
          ]
        },
        {
          path: 'categories',
          children: [
            {
              index: true,
              element: <CategoriesPage />,
              loader: categoriesLoader,
              action: createCategoryAction,
            },
            {
              path: ':id',
              element: <CategoryPage />,
              loader: categoryLoader,
            },
          ]
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App