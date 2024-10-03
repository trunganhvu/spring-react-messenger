import React, {useEffect} from "react"
import {RegisterUserComponent} from "./RegisterUser"
import { RegisterLayoutComponent } from "./RegisterLayoutComponent"

export function RegisterUserWrapper(): React.JSX.Element {

    useEffect(() => {
        document.title = "Register"
    }, [])

    return <>
        <RegisterLayoutComponent>
            <RegisterUserComponent/>
        </RegisterLayoutComponent>
    </>
}