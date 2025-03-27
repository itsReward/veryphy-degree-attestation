package com.veryphy

import com.veryphy.models.UserRole
import com.veryphy.screens.AdminDashboard
import com.veryphy.screens.EmployerDashboard
import com.veryphy.screens.LoginScreen
import com.veryphy.screens.UniversityDashboard
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

    // Create view models - initialize directly
    val loginViewModel = useState { LoginViewModel() }.component1()
    val universityViewModel = useState { UniversityViewModel() }.component1()
    val employerViewModel = useState { EmployerViewModel() }.component1()
    val adminViewModel = useState { AdminViewModel() }.component1()

    // Debug logging
    useEffect(currentScreen, currentRole) {
        console.log("Current screen: $currentScreen")
        console.log("Current role: $currentRole")
    }

    // Create AppState instance
    val appState = useState {
        val state = AppState()
        // Initialize properties
        state.currentScreen = currentScreen
        state.currentRole = currentRole
        state
    }.component1()

    // Update AppState when screen or role changes
    useEffect(currentScreen, currentRole) {
        appState.currentScreen = currentScreen
        appState.currentRole = currentRole
    }

    div {
        className = ClassName("app-container")

        when (currentScreen) {
            Screen.LOGIN -> {
                LoginScreen {
                    onLogin = { role ->
                        // Update state with selected role
                        setCurrentRole(role)

                        // Mock login
                        loginViewModel.user = viewmodels.ViewModelWrapper.getMockUser(role)

                        // Navigate to appropriate dashboard
                        setCurrentScreen(
                            when(role) {
                                UserRole.UNIVERSITY -> Screen.UNIVERSITY_DASHBOARD
                                UserRole.EMPLOYER -> Screen.EMPLOYER_DASHBOARD
                                UserRole.ADMIN -> Screen.ADMIN_DASHBOARD
                            }
                        )

                        // Initialize data based on role
                        when(role) {
                            UserRole.UNIVERSITY -> universityViewModel.loadDegrees("uni-001")
                            UserRole.EMPLOYER -> employerViewModel.loadVerificationHistory("emp-123")
                            UserRole.ADMIN -> {
                                adminViewModel.loadSystemStats()
                                adminViewModel.loadUniversities()
                            }
                        }
                    }
                }
            }

            Screen.UNIVERSITY_DASHBOARD -> {
                UniversityDashboard {
                    this.appState = appState
                    onLogout = {
                        loginViewModel.logout()
                        setCurrentScreen(Screen.LOGIN)
                        setCurrentRole(null)
                    }
                }
            }

            Screen.EMPLOYER_DASHBOARD -> {
                EmployerDashboard {
                    this.appState = appState
                    onLogout = {
                        loginViewModel.logout()
                        setCurrentScreen(Screen.LOGIN)
                        setCurrentRole(null)
                    }
                }
            }

            Screen.ADMIN_DASHBOARD -> {
                AdminDashboard {
                    this.appState = appState
                    onLogout = {
                        loginViewModel.logout()
                        setCurrentScreen(Screen.LOGIN)
                        setCurrentRole(null)
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