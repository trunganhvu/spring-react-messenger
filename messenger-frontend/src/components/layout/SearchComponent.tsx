import React, { useContext, useState } from "react"
import { InputAdornment, OutlinedInput } from "@mui/material"
import { HttpGroupService } from "../../service/http-group-service"
import { AlertAction, AlertContext } from "../../context/AlertContext"
import IconButton from "@mui/material/IconButton"
import CloseIcon from "@mui/icons-material/Close"
import { SearchContext } from "../../context/SearchContext"
import "./layoutStyle.css";

export function SearchComponent() {
  const [search, setTextSearch] = useState("")
  const { dispatch } = useContext(AlertContext)!
  const { setSearchText, setSearchResponse } = useContext(SearchContext)!
  const [searchingLoading, setSearchingLoading] = useState<boolean>(false)
  const http = new HttpGroupService()

  function handleChange(event: any) {
    event.preventDefault()
    setTextSearch(event.target.value)
  }

  function resetSearch() {
    setSearchText("")
    setTextSearch("")
  }

  async function startSearch(event: any) {
    if (event.key === undefined || event.key === "Enter") {
      if (search !== "") {
        setSearchingLoading(true)
        setSearchText(event.target.value)
        try {
          const { data } = await http.searchInConversations({ text: search })
          setSearchResponse(data)
        } catch (error) {
          dispatch({
            type: AlertAction.ADD_ALERT,
            payload: {
              alert: "error",
              id: crypto.randomUUID(),
              isOpen: true,
              text: "Cannot perform search in conversations."
            }
          })
        } finally {
          if (searchingLoading) {
            setSearchingLoading(false)
          }
        }
      }
    }
  }

  return <OutlinedInput 
    endAdornment={<InputAdornment position="end" style={{border: "0.1px solid rgb(246 248 252)"}} >
      <IconButton onClick={resetSearch} >
        <CloseIcon />
      </IconButton>
    </InputAdornment>}
    type={"text"}
    onChange={handleChange}
    value={search}
    onKeyDown={startSearch} 
    className="OutlinedInputApp"
    size={"small"}
    label={"Search in conversations"} />
}
