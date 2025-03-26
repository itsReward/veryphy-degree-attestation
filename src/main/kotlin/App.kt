package com.veryphy

import react.FC
import react.Props
import react.create
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import web.dom.Element

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

val App = FC<Props> {
    val appState = AppState()

    when (appState.currentScreen) {
        Screen.LOGIN -> LoginScreen {
            this.appState = appState
        }
        Screen.UNIVERSITY_DASHBOARD -> UniversityDashboard {
            this.appState = appState
        }
        Screen.EMPLOYER_DASHBOARD -> EmployerDashboard {
            this.appState = appState
        }
        Screen.ADMIN_DASHBOARD -> AdminDashboard {
            this.appState = appState
        }
    }
}




fun main() {
    window.onload = {
        val container = document.getElementById("root") as? Element ?: error("Couldn't find container!")
        createRoot(container).render(App.create())
    }
}