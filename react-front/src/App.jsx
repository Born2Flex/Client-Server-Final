import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import RootLayout, { loader as rootLoader } from './layouts/RootLayout';
import ErrorPage from './pages/ErrorPage';
import HomePage from './pages/HomePage';
import LoginPage, { loginAction, logOutAction } from './pages/LoginPage';
import ProductsPage from './pages/products/ProductsPage';
import ProductPage from './pages/products/ProductPage';
import CategoriesPage from './pages/categories/CategoriesPage';
import CategoryPage from './pages/categories/CategoryPage';

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
          element: <ProductsPage />,
          // loader: productsLoader,
          // action: searchProductsAction,
          children: [
            {
              path: ':id',
              element: <ProductPage />,
              // loader: productloader,
            },
            {
              path: 'new',
              // action: createProductAction,
            }
          ]
        },
        {
          path: 'categories',
          element: <CategoriesPage />,
          // loader: categoriesLoader,
          // action: searchCategoryAction,
          children: [
            {
              path: ':id',
              element: <CategoryPage />,
              // loader: categoryLoader,
            },
            {
              path: 'new',
              // action: createCategoryAction,
            }
          ]
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
}

export default App