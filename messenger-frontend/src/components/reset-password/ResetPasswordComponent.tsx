import React from "react"
import { Box, Card, CardContent, Grid, TextField, Typography } from "@mui/material"
import { generateColorMode, generateLinkColorMode } from "../common/EnableThemeMode"
import { useThemeContext } from "../../context/ThemeContext"
import { Link } from "react-router-dom"

export function ResetPasswordComponent(): React.JSX.Element {
  const { theme } = useThemeContext()

  return <div className={generateColorMode(theme)}
    style={{ height: "100%", display: "flex", alignItems: "center", justifyContent: "center" }}
  >
    <Grid sx={{ m: 2 }} container style={{ justifyContent: "center" }}  >
      <Card variant="outlined" style={{ textAlign: "center" }}>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            Welcome to Message
          </Typography>

          <Box sx={{ display: "flex" }}>
            <Box m={2}>
              <Typography variant="h6" color="inherit">
                Forgot your password ? Type your mail below. We will send to you a recovery mail.
              </Typography>
              <TextField variant={"outlined"} fullWidth={true} label={"You mail"} />
              <Link className={"lnk"}
                style={{ color: generateLinkColorMode(theme) }}
                to={"/login"}>
                Already have an account? Sign in
              </Link>
              <Link className={"lnk"}
                style={{ color: generateLinkColorMode(theme) }}
                to={"/register"}>
                 /Sign up
              </Link>
            </Box>
          </Box>
          {/* <FooterComponent/> */}
        </CardContent>
      </Card>
    </Grid>
  </div>
}