package com.veryphy

import com.veryphy.Components.DebugPanel
import react.*
import react.dom.client.createRoot
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.html.ReactHTML.div
import web.dom.Element
import web.cssom.ClassName

@JsModule("./styles.css")
@JsNonModule
external val styles: dynamic

enum class Screen {
    LOGIN,
    UNIVERSITY_DASHBOARD,
    EMPLOYER_DASHBOARD,
    ADMIN_DASHBOARD
}

class AppState {
    var currentScreen = Screen.LOGIN
    var currentRole: UserRole? = null

    // ViewModels
    val loginViewModel = LoginViewModel()
    val universityViewModel = UniversityViewModel()
    val employerViewModel = EmployerViewModel()
    val adminViewModel = AdminViewModel()
}

// Complete App component with proper state management and debugging
val App = FC<Props> {
    // Use useState to maintain state across renders
    val (appState, setAppState) = useState { AppState() }
    val (debugVisible, setDebugVisible) = useState(false)

    // Debug effect to log state changes
    useEffect(appState) {
        console.log("AppState updated:", appState)
        console.log("Current screen:", appState.currentScreen)
        console.log("Current role:", appState.currentRole)

        // Load initial data if user is logged in
        appState.currentRole?.let { role ->
            when (role) {
                UserRole.UNIVERSITY -> {
                    appState.universityViewModel.loadDegrees("uni-001")
                }
                UserRole.EMPLOYER -> {
                    appState.employerViewModel.loadVerificationHistory("emp-123")
                }
                UserRole.ADMIN -> {
                    appState.adminViewModel.loadSystemStats()
                    appState.adminViewModel.loadUniversities()
                }
            }
        }
    }

    // Create wrapper functions to update state properly
    val updateState = { newScreen: Screen, newRole: UserRole? ->
        setAppState { prevState ->
            val newState = AppState()
            // Copy view models to preserve data
            newState.loginViewModel.user = prevState.loginViewModel.user
            newState.loginViewModel.username = prevState.loginViewModel.username
            newState.loginViewModel.password = prevState.loginViewModel.password

            // Set new values
            newState.currentScreen = newScreen
            newState.currentRole = newRole
            newState
        }
    }

    // Render the component based on current screen
    div {
        className = ClassName("app-container")

        when (appState.currentScreen) {
            Screen.LOGIN -> LoginScreen {
                this.appState = appState
                this.onLogin = { role ->
                    updateState(
                        when(role) {
                            UserRole.UNIVERSITY -> Screen.UNIVERSITY_DASHBOARD
                            UserRole.EMPLOYER -> Screen.EMPLOYER_DASHBOARD
                            UserRole.ADMIN -> Screen.ADMIN_DASHBOARD
                        },
                        role
                    )
                }
            }
            Screen.UNIVERSITY_DASHBOARD -> UniversityDashboard {
                this.appState = appState
                this.onLogout = {
                    appState.loginViewModel.logout()
                    updateState(Screen.LOGIN, null)
                }
            }
            Screen.EMPLOYER_DASHBOARD -> EmployerDashboard {
                this.appState = appState
                this.onLogout = {
                    appState.loginViewModel.logout()
                    updateState(Screen.LOGIN, null)
                }
            }
            Screen.ADMIN_DASHBOARD -> AdminDashboard {
                this.appState = appState
                this.onLogout = {
                    appState.loginViewModel.logout()
                    updateState(Screen.LOGIN, null)
                }
            }
        }

        // Add debug panel
        DebugPanel {
            this.appState = appState
            this.visible = debugVisible
            this.toggleVisibility = { setDebugVisible(!debugVisible) }
        }
    }
}

fun main() {
    console.log("Application starting...")
    window.onload = {
        try {
            console.log("Window loaded, looking for root element")
            val container = document.getElementById("root") as? Element
            if (container != null) {
                console.log("Root element found, rendering app")
                createRoot(container).render(App.create())
                console.log("App rendered")
            } else {
                console.error("Couldn't find root element!")
            }
        } catch (e: Exception) {
            console.error("Error during app initialization:", e)
        }
    }
}