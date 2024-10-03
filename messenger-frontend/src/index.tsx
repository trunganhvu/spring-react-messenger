import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import * as serviceWorker from "./serviceWorker"
import { createBrowserRouter, redirect, RouterProvider } from "react-router-dom"
import { HttpGroupService } from './service/http-group-service';
import { LoginWrapperComponent } from './components/login/LoginWrapperComponent';
import { LoaderProvider } from './context/LoaderContext';
import { GroupContextProvider } from './context/GroupContext';
import { UserContextProvider } from './context/UserContext';
import { AlertContextProvider } from './context/AlertContext';
import { RegisterUserWrapper } from './components/register/RegisterUserWrapper';
import { ResetPasswordComponent } from './components/reset-password/ResetPasswordComponent';
import { WebSocketMainComponent } from './components/websocket/WebSocketMainComponent';
import { AlertComponent } from './components/layout/AlertComponent';
import { SearchProvider } from './context/SearchContext';

const router = createBrowserRouter([
  {
    path: "/",
    loader: async () => {
      return new HttpGroupService().pingRoute()
        .then(() => redirect("/t/messages"))
        .catch(() => redirect("/login"))
    },
  },
  {
    path: "login",
    element: <LoginWrapperComponent />,
  },
  {
    path: "register",
    element: <RegisterUserWrapper />
  },
  {
    path: "reset/password",
    element: <ResetPasswordComponent />
  },
  {
    path: "t/messages",
    element: <WebSocketMainComponent />
  },
  {
    path: "t/messages/:groupId",
    element: <WebSocketMainComponent/>
  },
])

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    {/* <App /> */}
    <LoaderProvider>
      <GroupContextProvider>
        <UserContextProvider>
          <SearchProvider>
            <AlertContextProvider>
              <RouterProvider router={router} />
              <AlertComponent/>
            </AlertContextProvider>
          </SearchProvider>
        </UserContextProvider>
      </GroupContextProvider>
    </LoaderProvider>
  </React.StrictMode>
);

// Monitoring performance
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals(console.log);


// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.register()