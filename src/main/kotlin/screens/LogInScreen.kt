package com.veryphy.screens

import com.veryphy.models.UserRole
import react.FC
import react.Props
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML
import react.useState
import web.cssom.ClassName
import web.cssom.Color
import web.html.InputType

// Properly define props with 'external'
external interface LoginScreenProps : Props {
    var demoMode: Boolean
    var onToggleDemoMode: () -> Unit
    var onLogin: (String, String, UserRole) -> Unit
}

// Updated LoginScreen with form fields and login functionality
val LoginScreen = FC<LoginScreenProps> { props ->
    // Form state
    val (username, setUsername) = useState("")
    val (password, setPassword) = useState("")
    val (selectedRole, setSelectedRole) = useState(UserRole.UNIVERSITY)
    val (errorMessage, setErrorMessage) = useState<String?>(null)

    // Handle login submission
    val handleLogin = {
        if (username.isBlank() || password.isBlank()) {
            setErrorMessage("Username and password are required")
        } else {
            setErrorMessage(null)
            props.onLogin(username, password, selectedRole)
        }
    }

    ReactHTML.div {
        className = ClassName("login-container")
        style = js("""{ 
            display: "flex", 
            flexDirection: "column", 
            alignItems: "center", 
            justifyContent: "center", 
            height: "100vh", 
            backgroundColor: "#f5f5f5" 
        }""")

        // Logo above login box
        ReactHTML.div {
            className = ClassName("logo-container")
            style = js("""{ 
                marginBottom: "20px", 
                width: "250px", 
                height: "auto",
                textAlign: "center"
            }""")

            ReactHTML.img {
                src = "veryphy_logo.svg"
                alt = "VeryPhy Logo"
                style = js("""{ 
                    width: "100%", 
                    height: "auto" 
                }""")
            }
        }

        ReactHTML.div {
            className = ClassName("login-card")
            style = js("""{ 
                background: "#ffffff", 
                borderRadius: "8px", 
                boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)", 
                padding: "32px", 
                width: "400px", 
                maxWidth: "90%" 
            }""")

            // Error message display
            errorMessage?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    style = js("""{ 
                        background: "#ffebee", 
                        color: "#d32f2f", 
                        padding: "10px", 
                        borderRadius: "4px", 
                        marginBottom: "20px" 
                    }""")
                    +it
                }
            }

            ReactHTML.p {
                +"Secure verification using blockchain and AI"
                style = js("""{ 
                    textAlign: "center", 
                    marginBottom: "24px", 
                    color: "#666666" 
                }""")
            }

            // Demo mode toggle
            ReactHTML.div {
                className = ClassName("demo-mode-toggle")
                style = js("""{ 
                    display: "flex", 
                    alignItems: "center", 
                    marginBottom: "16px", 
                    justifyContent: "center" 
                }""")

                ReactHTML.input {
                    id = "demoMode"
                    type = InputType.checkbox
                    checked = props.demoMode
                    onChange = {
                        props.onToggleDemoMode()
                    }
                }

                ReactHTML.label {
                    htmlFor = "demoMode"
                    style = js("""{ marginLeft: "8px" }""")
                    +"Demo Mode (no backend)"
                }
            }

            ReactHTML.div {
                className = ClassName("login-form")
                style = js("""{ display: "flex", flexDirection: "column", gap: "16px" }""")

                ReactHTML.div {
                    className = ClassName("form-group")
                    style = js("""{ display: "flex", flexDirection: "column", gap: "8px" }""")

                    ReactHTML.label {
                        htmlFor = "username"
                        +"Username"
                        style = js("""{ fontWeight: "bold" }""")
                    }

                    ReactHTML.input {
                        id = "username"
                        type = InputType.text
                        className = ClassName("form-control")
                        style = js("""{ 
                            padding: "12px", 
                            fontSize: "16px", 
                            borderRadius: "4px", 
                            border: "1px solid #ddd" 
                        }""")
                        value = username
                        placeholder = "Enter username"
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            setUsername(e.target.value)
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    style = js("""{ display: "flex", flexDirection: "column", gap: "8px" }""")

                    ReactHTML.label {
                        htmlFor = "password"
                        +"Password"
                        style = js("""{ fontWeight: "bold" }""")
                    }

                    ReactHTML.input {
                        id = "password"
                        type = InputType.password
                        className = ClassName("form-control")
                        style = js("""{ 
                            padding: "12px", 
                            fontSize: "16px", 
                            borderRadius: "4px", 
                            border: "1px solid #ddd" 
                        }""")
                        value = password
                        placeholder = "Enter password"
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            setPassword(e.target.value)
                        }
                    }
                }

                // Role selection dropdown
                ReactHTML.div {
                    className = ClassName("form-group")
                    style = js("""{ display: "flex", flexDirection: "column", gap: "8px" }""")

                    ReactHTML.label {
                        htmlFor = "role"
                        +"Select Role"
                        style = js("""{ fontWeight: "bold" }""")
                    }

                    ReactHTML.select {
                        id = "role"
                        className = ClassName("form-control")
                        style = js("""{ 
                            padding: "12px", 
                            fontSize: "16px", 
                            borderRadius: "4px", 
                            border: "1px solid #ddd" 
                        }""")
                        value = selectedRole.toString()
                        onChange = { e: ChangeEvent<web.html.HTMLSelectElement> ->
                            when (e.target.value) {
                                "UNIVERSITY" -> setSelectedRole(UserRole.UNIVERSITY)
                                "EMPLOYER" -> setSelectedRole(UserRole.EMPLOYER)
                                "ADMIN" -> setSelectedRole(UserRole.ADMIN)
                            }
                        }

                        ReactHTML.option {
                            value = "UNIVERSITY"
                            +"University"
                        }

                        ReactHTML.option {
                            value = "EMPLOYER"
                            +"Employer"
                        }

                        ReactHTML.option {
                            value = "ADMIN"
                            +"Admin"
                        }
                    }
                }

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    style = js("""{ 
                        width: "100%", 
                        marginTop: "20px", 
                        padding: "12px", 
                        fontSize: "16px", 
                        backgroundColor: "#0C8B44", 
                        color: "white", 
                        border: "none", 
                        borderRadius: "4px", 
                        cursor: "pointer" 
                    }""")
                    type = web.html.ButtonType.button
                    onClick = { handleLogin() }
                    +"Log In"
                }

                if (props.demoMode) {
                    ReactHTML.p {
                        style = js("""{ 
                            marginTop: "16px", 
                            fontSize: "13px", 
                            textAlign: "center", 
                            color: "#666" 
                        }""")
                        +"Note: In demo mode, any credentials will work"
                    }
                }
            }
        }
    }
}