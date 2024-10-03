import React from "react"
import { Box, Card, CardContent, Grid, Typography } from "@mui/material"
import { generateColorMode } from "../common/EnableThemeMode"
import { useThemeContext } from "../../context/ThemeContext"

interface WelcomeComponentProps {
  children: React.ReactNode
}

export function LoginLayoutComponent({ children }: WelcomeComponentProps) {
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
            {children}
          </Box>
          {/* <FooterComponent/> */}
        </CardContent>
      </Card>
    </Grid>
  </div>
}