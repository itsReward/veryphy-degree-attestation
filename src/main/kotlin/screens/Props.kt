package com.veryphy.screens

import com.veryphy.AppState
import react.Props

// Interface for AppState props
external interface AppProps : Props {
    var appState: AppState
}

// Extended dashboard props with logout callback
external interface DashboardProps : Props {
    var appState: AppState
    var onLogout: () -> Unit  // Make sure this is defined as var
}





