/*
package com.veryphy

import com.veryphy.Components.DebugPanel
import react.*
import react.dom.client.createRoot
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.html.ReactHTML.div
import web.dom.Element
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.button
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
*/
/*
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
}*//*





// Simplified app that should work
val App = FC<Props> {
    // Use useState for simple state management
    val (screen, setScreen) = useState("login")
    val (role, setRole) = useState<String?>(null)

    div {
        h1 {
            +"Degree Attestation System"
        }

        when (screen) {
            "login" -> div {
                h1 { +"Login Screen" }
                button {
                    onClick = {
                        setScreen("university")
                        setRole("university")
                    }
                    +"Login as University"
                }
                button {
                    onClick = {
                        setScreen("employer")
                        setRole("employer")
                    }
                    +"Login as Employer"
                }
                button {
                    onClick = {
                        setScreen("admin")
                        setRole("admin")
                    }
                    +"Login as Admin"
                }
            }
            "university" -> div {
                h1 { +"University Dashboard" }
                button {
                    onClick = {
                        setScreen("login")
                        setRole(null)
                    }
                    +"Logout"
                }
            }
            "employer" -> div {
                h1 { +"Employer Dashboard" }
                button {
                    onClick = {
                        setScreen("login")
                        setRole(null)
                    }
                    +"Logout"
                }
            }
            "admin" -> div {
                h1 { +"Admin Dashboard" }
                button {
                    onClick = {
                        setScreen("login")
                        setRole(null)
                    }
                    +"Logout"
                }
            }
        }
    }
}

fun main() {
    window.onload = {
        try {
            val container = document.getElementById("root") as? Element
            if (container != null) {
                createRoot(container).render(App.create())
            } else {
                console.error("Couldn't find root element!")
            }
        } catch (e: Exception) {
            console.error("Error during app initialization:", e)
        }
    }
}
*/
package com.veryphy

import react.*
import react.dom.client.createRoot
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.form
import react.dom.events.ChangeEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import react.dom.svg.ReactSVG.image
import web.dom.Element
import web.cssom.ClassName
import web.html.InputType


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

// App component using dashboard layout
val App = FC<Props> {
    // Use useState for state management
    val (appState, setAppState) = useState { AppState() }

    // Debug effect to log state changes
    useEffect(appState) {
        console.log("Current screen:", appState.currentScreen)
        console.log("Current role:", appState.currentRole)
    }

    // Helper function to update state
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

    div {
        className = ClassName("app-container")

        when (appState.currentScreen) {
            Screen.LOGIN -> div {
                className = ClassName("login-container")

                h1 {
                    +"Veryphy Degree Attestation System"
                }

                p {
                    +"Secure verification using blockchain and AI"
                }

                div {
                    className = ClassName("login-card")

                    h1 {
                        +"Login"
                    }

                    if (appState.loginViewModel.isLoading) {
                        div {
                            className = ClassName("loading-indicator")
                            +"Logging in..."
                        }
                    }

                    appState.loginViewModel.errorMessage?.let {
                        div {
                            className = ClassName("error-message")
                            +it
                        }
                    }

                    form {
                        className = ClassName("login-form")
                        onSubmit = { e -> e.preventDefault() }

                        div {
                            className = ClassName("form-group")
                            label {
                                htmlFor = "username"
                                +"Username"
                            }
                            input {
                                id = "username"
                                type = InputType.text
                                className = ClassName("form-control")
                                value = appState.loginViewModel.username
                                onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                    appState.loginViewModel.username = e.target.value
                                }
                            }
                        }

                        div {
                            className = ClassName("form-group")
                            label {
                                htmlFor = "password"
                                +"Password"
                            }
                            input {
                                id = "password"
                                type = InputType.password
                                className = ClassName("form-control")
                                value = appState.loginViewModel.password
                                onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                    appState.loginViewModel.password = e.target.value
                                }
                            }
                        }

                        div {
                            className = ClassName("role-selection")
                            +"Login as: "

                            button {
                                className = ClassName("btn role-btn")
                                onClick = {
                                    // Use the viewModel for login
                                    MainScope().launch {
                                        appState.loginViewModel.login(UserRole.UNIVERSITY) {
                                            updateState(Screen.UNIVERSITY_DASHBOARD, UserRole.UNIVERSITY)
                                            // Initialize university dashboard data
                                            appState.universityViewModel.loadDegrees("uni-001")
                                        }
                                    }
                                }
                                disabled = appState.loginViewModel.isLoading
                                +"University"
                            }

                            button {
                                className = ClassName("btn role-btn")
                                onClick = {
                                    MainScope().launch {
                                        appState.loginViewModel.login(UserRole.EMPLOYER) {
                                            updateState(Screen.EMPLOYER_DASHBOARD, UserRole.EMPLOYER)
                                            // Initialize employer dashboard data
                                            appState.employerViewModel.loadVerificationHistory("emp-123")
                                        }
                                    }
                                }
                                disabled = appState.loginViewModel.isLoading
                                +"Employer"
                            }

                            button {
                                className = ClassName("btn role-btn")
                                onClick = {
                                    MainScope().launch {
                                        appState.loginViewModel.login(UserRole.ADMIN) {
                                            updateState(Screen.ADMIN_DASHBOARD, UserRole.ADMIN)
                                            // Initialize admin dashboard data
                                            appState.adminViewModel.loadSystemStats()
                                            appState.adminViewModel.loadUniversities()
                                        }
                                    }
                                }
                                disabled = appState.loginViewModel.isLoading
                                +"Admin"
                            }
                        }
                    }
                }
            }

            Screen.UNIVERSITY_DASHBOARD -> {
                // Use the dashboard layout
                DashboardLayout {
                    title = "University Dashboard"
                    username = appState.loginViewModel.user?.name ?: "User"
                    role = UserRole.UNIVERSITY
                    onLogout = {
                        appState.loginViewModel.logout()
                        updateState(Screen.LOGIN, null)
                    }

                    // Dashboard content (as children)
                    div {
                        className = ClassName("dashboard-content")

                        h2 {
                            +"Register New Degree"
                        }

                        // Basic form placeholder
                        form {
                            className = ClassName("form-group")

                            div {
                                className = ClassName("form-group")
                                label {
                                    htmlFor = "studentId"
                                    +"Student ID"
                                }
                                input {
                                    id = "studentId"
                                    type = InputType.text
                                    className = ClassName("form-control")
                                }
                            }

                            button {
                                className = ClassName("btn primary-btn")
                                +"Register Degree"
                            }
                        }

                        h2 {
                            +"Recent Registrations"
                        }

                        // Placeholder for degree list
                        if (appState.universityViewModel.degrees.isEmpty()) {
                            p {
                                +"No degrees registered yet"
                            }
                        } else {
                            p {
                                +"You have ${appState.universityViewModel.degrees.size} registered degrees"
                            }
                        }
                    }
                }
            }

            Screen.EMPLOYER_DASHBOARD -> {
                // Use the dashboard layout
                DashboardLayout {
                    title = "Employer Dashboard"
                    username = appState.loginViewModel.user?.name ?: "User"
                    role = UserRole.EMPLOYER
                    onLogout = {
                        appState.loginViewModel.logout()
                        updateState(Screen.LOGIN, null)
                    }

                    // Dashboard content
                    div {
                        className = ClassName("dashboard-content")

                        h2 {
                            +"Verify Degree Certificate"
                        }

                        // Basic form placeholder
                        form {
                            className = ClassName("form-group")

                            div {
                                className = ClassName("form-group")
                                label {
                                    htmlFor = "certificateUpload"
                                    +"Upload Certificate"
                                }
                                input {
                                    id = "certificateUpload"
                                    type = InputType.file
                                    className = ClassName("form-control")
                                }
                            }

                            button {
                                className = ClassName("btn primary-btn")
                                +"Verify"
                            }
                        }

                        h2 {
                            +"Recent Verifications"
                        }

                        // Placeholder for verification history
                        if (appState.employerViewModel.verificationHistory.isEmpty()) {
                            p {
                                +"No verification history yet"
                            }
                        } else {
                            p {
                                +"You have ${appState.employerViewModel.verificationHistory.size} verifications"
                            }
                        }
                    }
                }
            }

            Screen.ADMIN_DASHBOARD -> {
                // Use the dashboard layout
                DashboardLayout {
                    title = "Admin Dashboard"
                    username = appState.loginViewModel.user?.name ?: "User"
                    role = UserRole.ADMIN
                    onLogout = {
                        appState.loginViewModel.logout()
                        updateState(Screen.LOGIN, null)
                    }

                    // Dashboard content
                    div {
                        className = ClassName("dashboard-content")

                        h2 {
                            +"System Statistics"
                        }

                        // Display stats if available
                        appState.adminViewModel.systemStats?.let { stats ->
                            div {
                                className = ClassName("stats-container")

                                p {
                                    +"Universities: ${stats.registeredUniversities}"
                                }
                                p {
                                    +"Total Degrees: ${stats.totalDegrees}"
                                }
                                p {
                                    +"Verifications: ${stats.verificationCount}"
                                }
                                p {
                                    +"Success Rate: ${stats.successRate}%"
                                }
                            }
                        } ?: p {
                            +"Loading statistics..."
                        }

                        h2 {
                            +"Registered Universities"
                        }

                        // Display universities if available
                        if (appState.adminViewModel.universities.isEmpty()) {
                            p {
                                +"No universities registered yet"
                            }
                        } else {
                            p {
                                +"${appState.adminViewModel.universities.size} universities in the system"
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    window.onload = {
        try {
            val container = document.getElementById("root") as? Element
            if (container != null) {
                createRoot(container).render(App.create())
            } else {
                console.error("Couldn't find root element!")
            }
        } catch (e: Exception) {
            console.error("Error during app initialization:", e)
        }
    }
}
