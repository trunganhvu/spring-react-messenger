import React, {useEffect} from "react"
import {LoginComponent} from "./LoginComponent"
import { LoginLayoutComponent } from "./LoginLayoutComponent"

export function LoginWrapperComponent(): React.JSX.Element {

    useEffect(() => {
        document.title = "Login"
    }, [])

    return <>
        <LoginLayoutComponent>
            <LoginComponent/>
        </LoginLayoutComponent>
    </>
}