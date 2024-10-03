import React, {useContext} from "react"
import { SearchContext } from "../../context/SearchContext"
import { WebSocketChatComponent } from "./WebSocketChatComponent"
import { SearchMessageComponent } from "../messages/SearchMessageComponent"

interface WebsocketChatWrapperComponentProps {
    groupUrl?: string
}

export function WebsocketChatWrapperComponent({groupUrl}: WebsocketChatWrapperComponentProps) {
    const {searchText} = useContext(SearchContext)!
    console.log('searchText ', searchText)

    return <>
        {
            searchText === "" ? <WebSocketChatComponent groupUrl={groupUrl}/> : <SearchMessageComponent/>
        }
    </>

}
