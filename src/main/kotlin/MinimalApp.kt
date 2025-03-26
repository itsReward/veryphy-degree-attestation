package com.veryphy

import react.*
import react.dom.client.createRoot
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import web.dom.Element
import web.cssom.ClassName

// Simplified app with minimal dashboard screens
/*
val App = FC<Props> {
    // Use state to track current screen 
    val (currentScreen, setCurrentScreen) = useState("login")

    div {
        className = ClassName("app-container")

        when (currentScreen) {
            "login" -> {
                div {
                    className = ClassName("login-container")
                    h1 { +"VeryPhy Degree Attestation System" }

                    div {
                        className = ClassName("login-card")
                        h1 { +"Choose Role" }
                        div {
                            className = ClassName("role-selection")

                            button {
                                className = ClassName("btn role-btn")
                                onClick = { setCurrentScreen("university") }
                                +"University"
                            }

                            button {
                                className = ClassName("btn role-btn")
                                onClick = { setCurrentScreen("employer") }
                                +"Employer"
                            }

                            button {
                                className = ClassName("btn role-btn")
                                onClick = { setCurrentScreen("admin") }
                                +"Admin"
                            }
                        }
                    }
                }
            }

            "university" -> {
                // Simplified university dashboard without using DashboardLayout
                div {
                    className = ClassName("dashboard-container")
                    div {
                        className = ClassName("header")
                        h1 { +"University Dashboard" }
                        button {
                            className = ClassName("btn-logout")
                            onClick = { setCurrentScreen("login") }
                            +"Logout"
                        }
                    }

                    div {
                        className = ClassName("dashboard-content")
                        h1 { +"Register New Degree" }
                        p { +"Form will go here" }

                        button {
                            className = ClassName("btn primary-btn")
                            +"Register Degree"
                        }

                        h1 { +"Registered Degrees" }
                        p { +"List will go here" }
                    }
                }
            }

            "employer" -> {
                // Simplified employer dashboard without using DashboardLayout
                div {
                    className = ClassName("dashboard-container")
                    div {
                        className = ClassName("header")
                        h1 { +"Employer Dashboard" }
                        button {
                            className = ClassName("btn-logout")
                            onClick = { setCurrentScreen("login") }
                            +"Logout"
                        }
                    }

                    div {
                        className = ClassName("dashboard-content")
                        h1 { +"Verify Degree Certificate" }
                        p { +"Form will go here" }

                        button {
                            className = ClassName("btn primary-btn")
                            +"Verify Certificate"
                        }

                        h1 { +"Verification History" }
                        p { +"List will go here" }
                    }
                }
            }

            "admin" -> {
                // Simplified admin dashboard without using DashboardLayout
                div {
                    className = ClassName("dashboard-container")
                    div {
                        className = ClassName("header")
                        h1 { +"Admin Dashboard" }
                        button {
                            className = ClassName("btn-logout")
                            onClick = { setCurrentScreen("login") }
                            +"Logout"
                        }
                    }

                    div {
                        className = ClassName("dashboard-content")
                        h1 { +"System Statistics" }
                        p { +"Stats will go here" }

                        h1 { +"Manage Universities" }
                        p { +"List will go here" }

                        button {
                            className = ClassName("btn primary-btn")
                            +"Add University"
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
        }
    }
}*/
