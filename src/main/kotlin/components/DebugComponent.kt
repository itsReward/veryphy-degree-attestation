package com.veryphy.Components

import com.veryphy.AppState
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.pre
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.button
import web.cssom.ClassName
import web.cssom.Position
import web.cssom.px
import kotlin.js.json

// A debug component to display current application state
external interface DebugProps : Props {
    var appState: AppState
    var visible: Boolean
    var toggleVisibility: () -> Unit
}

val DebugPanel = FC<DebugProps> { props ->
    if (props.visible) {
        div {
            className = ClassName("debug-panel")
            asDynamic().style = json(
                "position" to "fixed",
                "bottom" to "10px",
                "right" to "10px",
                "width" to "300px",
                "maxHeight" to "400px",
                "overflowY" to "auto",
                "backgroundColor" to "rgba(0, 0, 0, 0.8)",
                "color" to "white",
                "padding" to "10px",
                "borderRadius" to "5px",
                "zIndex" to "9999",
                "fontFamily" to "monospace",
                "fontSize" to "12px"
            )

            h3 {
                +"Debug Info"
            }

            pre {
                +"Current Screen: ${props.appState.currentScreen}\n"
                +"Current Role: ${props.appState.currentRole}\n"
                +"User: ${props.appState.loginViewModel.user?.name ?: "Not logged in"}\n"
                +"Loading: ${if (props.appState.loginViewModel.isLoading) "Yes" else "No"}\n"
                +"Error: ${props.appState.loginViewModel.errorMessage ?: "None"}"
            }

            button {
                onClick = { props.toggleVisibility() }
                +"Close"
            }
        }
    }

    // Toggle button
    div {
        asDynamic().style = json(
            "position" to "fixed",
            "bottom" to "10px",
            "right" to "10px",
            "zIndex" to "9999"
        )

        if (!props.visible) {
            button {
                onClick = { props.toggleVisibility() }
                +"Debug"
            }
        }
    }
}

// Add this to your App.kt
// val (debugVisible, setDebugVisible) = useState(false)
//
// DebugPanel {
//     appState = appState
//     visible = debugVisible
//     toggleVisibility = { setDebugVisible(!debugVisible) }
// }