package com.veryphy

import com.veryphy.models.UserRole
import com.veryphy.screens.AdminDashboard
import com.veryphy.screens.EmployerDashboard
import com.veryphy.screens.LoginScreen
import com.veryphy.screens.UniversityDashboard
import com.veryphy.viewmodels.AdminViewModel
import com.veryphy.viewmodels.LoginViewModel
import com.veryphy.viewmodels.UniversityViewModel
import kotlinx.browser.document
import kotlinx.browser.window
import react.*
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import web.cssom.ClassName
import web.dom.Element

enum class Screen {
    LOGIN,
    UNIVERSITY_DASHBOARD,
    EMPLOYER_DASHBOARD,
    ADMIN_DASHBOARD
}

class AppState {
    var currentScreen = Screen.LOGIN
    var currentRole: UserRole? = null
    var demoMode = false // Enable/disable demo mode (no backend)

    // ViewModels
    val loginViewModel = LoginViewModel()
    val universityViewModel = UniversityViewModel()
    val employerViewModel = EmployerViewModel()
    val adminViewModel = AdminViewModel()
}

val App = FC<Props> {
    // Use state to track current screen and role
    val (currentScreen, setCurrentScreen) = useState(Screen.LOGIN)
    val (currentRole, setCurrentRole) = useState<UserRole?>(null)

    // State for demo mode - useful for testing without a backend
    var (demoMode, setDemoMode) = useState(true) // Start in demo mode by default

    // Create view models - initialize directly
    val loginViewModel = useState { LoginViewModel() }.component1()
    val universityViewModel = useState { UniversityViewModel() }.component1()
    val employerViewModel = useState { EmployerViewModel() }.component1()
    val adminViewModel = useState { AdminViewModel() }.component1()


    // Function to handle successful login
    val handleSuccessfulLogin = { role: UserRole ->
        // Update state with selected role
        setCurrentRole(role)

        // Navigate to appropriate dashboard
        setCurrentScreen(
            when (role) {
                UserRole.UNIVERSITY -> Screen.UNIVERSITY_DASHBOARD
                UserRole.EMPLOYER -> Screen.EMPLOYER_DASHBOARD
                UserRole.ADMIN -> Screen.ADMIN_DASHBOARD
            }
        )

        // Initialize data based on role
        if (demoMode) {
            // Use demo data loading
            when (role) {
                UserRole.UNIVERSITY -> universityViewModel.demoLoadDegrees()
                UserRole.EMPLOYER -> employerViewModel.demoLoadVerificationHistory()
                UserRole.ADMIN -> {
                    adminViewModel.demoLoadSystemStats()
                    adminViewModel.demoLoadUniversities()
                }
            }
        } else {
            // Use real API data loading
            val userId = loginViewModel.user?.id ?: ""
            when (role) {
                UserRole.UNIVERSITY -> universityViewModel.loadDegrees(userId)
                UserRole.EMPLOYER -> employerViewModel.loadVerificationHistory(userId)
                UserRole.ADMIN -> {
                    adminViewModel.loadSystemStats()
                    adminViewModel.loadUniversities()
                }
            }
        }
    }

    // Function to handle logout
    val handleLogout = {
        loginViewModel.logout()
        setCurrentScreen(Screen.LOGIN)
        setCurrentRole(null)
    }

    // Debug logging
    useEffect(currentScreen, currentRole) {
        console.log("Current screen: $currentScreen")
        console.log("Current role: $currentRole")
        console.log("Demo mode: $demoMode")
    }

    // Create AppState instance
    val appState = useState {
        val state = AppState()
        // Initialize properties
        state.currentScreen = currentScreen
        state.currentRole = currentRole
        state.demoMode = demoMode
        state
    }.component1()

    // Update AppState when screen, role, or demoMode changes
    useEffect(currentScreen, currentRole, demoMode) {
        appState.currentScreen = currentScreen
        appState.currentRole = currentRole
        appState.demoMode = demoMode
    }

    div {
        className = ClassName("app-container")

        when (currentScreen) {
            Screen.LOGIN -> {
                LoginScreen {
                    demoMode = appState.demoMode

                    onToggleDemoMode = {
                        setDemoMode(!demoMode)
                    }

                    onLogin = { username, password, role ->
                        if (demoMode) {
                            // Use demo login
                            loginViewModel.demoLogin(role) {
                                handleSuccessfulLogin(role)
                            }
                        } else {
                            // Use real API login
                            loginViewModel.username = username
                            loginViewModel.password = password

                            loginViewModel.login(
                                role = role,
                                onSuccess = {
                                    handleSuccessfulLogin(role)
                                },
                                onError = { errorMessage ->
                                    // Handle error - could show a toast notification here
                                    console.error("Login error: $errorMessage")
                                }
                            )
                        }
                    }
                }
            }

            Screen.UNIVERSITY_DASHBOARD -> {
                UniversityDashboard {
                    this.appState = appState
                    onLogout = {
                        handleLogout()
                    }
                }
            }

            Screen.EMPLOYER_DASHBOARD -> {
                EmployerDashboard {
                    this.appState = appState
                    onLogout = {
                        handleLogout()
                    }
                }
            }

            Screen.ADMIN_DASHBOARD -> {
                AdminDashboard {
                    this.appState = appState
                    onLogout = {
                        handleLogout()
                    }
                }
            }
        }
    }

}

fun main() {
    window.onload = {
        try {
            // Load CSS styles via JS as a fallback
            StyleLoader.loadStyles()

            val container = document.getElementById("root") as? Element
            if (container != null) {
                createRoot(container).render(App.create())
                console.log("App rendered successfully")
            } else {
                console.error("Couldn't find root element!")
            }
        } catch (e: Exception) {
            console.error("Error during app initialization:", e)
            document.getElementById("debug")?.textContent = "Error: ${e.message}"
        }
    }
}