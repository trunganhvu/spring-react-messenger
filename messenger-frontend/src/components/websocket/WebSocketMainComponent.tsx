import "./websocketStyle.css"
import React, { useEffect } from "react"
import { useParams } from "react-router-dom"
import { HeaderComponent } from "../layout/HeaderComponent"
import { WebsocketContextProvider } from "../../context/WebsocketContext"
import { WebsocketGroupsComponent } from "./WebsocketGroupsComponent"
import { WebsocketChatWrapperComponent } from "./WebsocketChatWrapperComponent"
import { WebSocketGroupActionComponent } from "./WebSocketGroupActionComponent"

export const WebSocketMainComponent: React.FunctionComponent = (): React.JSX.Element => {
  const { groupId } = useParams()

  useEffect(() => {
    document.title = "Messages"
  }, [])

  return (
    <>
      <HeaderComponent />
      <div style={{
        height: "calc(100% - 64px)",
        display: "flex",
      }}>
        <WebsocketContextProvider>
          <WebsocketGroupsComponent groupUrl={groupId} />
          <WebsocketChatWrapperComponent groupUrl={groupId} />
          <WebSocketGroupActionComponent groupUrl={groupId} />
        </WebsocketContextProvider>
      </div>
    </>
  )
}
