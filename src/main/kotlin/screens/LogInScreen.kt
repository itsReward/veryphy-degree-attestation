package com.veryphy.screens

import com.veryphy.models.UserRole
import react.FC
import react.Props
import react.dom.events.ChangeEvent
import react.dom.events.FormEvent
import react.dom.html.ReactHTML
import react.useState
import web.cssom.ClassName
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

    // Handle form submission
    val handleSubmit = { e: FormEvent<*> ->
        e.preventDefault()

        if (username.isBlank() || password.isBlank()) {
            setErrorMessage("Username and password are required")
        } else {
            setErrorMessage(null)
            props.onLogin(username, password, selectedRole)
        }
    }

    ReactHTML.div {
        className = ClassName("login-container")

        ReactHTML.div {
            className = ClassName("logo-container")
            ReactHTML.img {
                className = ClassName("logo")
                src = "veryphy_logo.svg"
                alt = "VeryPhy Logo"
            }
        }

        ReactHTML.div {
            className = ClassName("login-card")

            // Error message display
            errorMessage?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    +it
                }
            }

            ReactHTML.p {
                +"Secure verification using blockchain and AI"
                style = js("""{ textAlign: "center" }""")
            }

            // Demo mode toggle
            ReactHTML.div {
                className = ClassName("demo-mode-toggle")
                style = js("""{ display: "flex", alignItems: "center", marginBottom: "16px", justifyContent: "center" }""")

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

            ReactHTML.form {
                className = ClassName("login-form")
                onSubmit = handleSubmit

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "username"
                        +"Username"
                    }
                    ReactHTML.input {
                        id = "username"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = username
                        placeholder = "username (demo)"
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            setUsername(e.target.value)
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "password"
                        +"Password"
                    }
                    ReactHTML.input {
                        id = "password"
                        type = InputType.password
                        className = ClassName("form-control")
                        value = password
                        placeholder = "password (demo)"
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            setPassword(e.target.value)
                        }
                    }
                }

                // Role selection
                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        +"Select Role"
                    }

                    ReactHTML.div {
                        className = ClassName("role-selection")

                        ReactHTML.button {
                            type = web.html.ButtonType.button
                            className = ClassName("btn role-btn" + if (selectedRole == UserRole.UNIVERSITY) " active" else "")
                            style = if (selectedRole == UserRole.UNIVERSITY) {
                                js("""{ border: "2px solid #fff" }""")
                            } else js("{}")
                            onClick = {
                                setSelectedRole(UserRole.UNIVERSITY)
                            }
                            +"University"
                        }

                        ReactHTML.button {
                            type = web.html.ButtonType.button
                            className = ClassName("btn role-btn" + if (selectedRole == UserRole.EMPLOYER) " active" else "")
                            style = if (selectedRole == UserRole.EMPLOYER) {
                                js("""{ border: "2px solid #fff" }""")
                            } else js("{}")
                            onClick = {
                                setSelectedRole(UserRole.EMPLOYER)
                            }
                            +"Employer"
                        }

                        ReactHTML.button {
                            type = web.html.ButtonType.button
                            className = ClassName("btn role-btn" + if (selectedRole == UserRole.ADMIN) " active" else "")
                            style = if (selectedRole == UserRole.ADMIN) {
                                js("""{ border: "2px solid #fff" }""")
                            } else js("{}")
                            onClick = {
                                setSelectedRole(UserRole.ADMIN)
                            }
                            +"Admin"
                        }
                    }
                }

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    style = js("""{ width: "100%", marginTop: "20px" }""")
                    type = web.html.ButtonType.submit
                    +"Log In"
                }

                if (props.demoMode) {
                    ReactHTML.p {
                        style = js("""{ marginTop: "16px", fontSize: "13px", textAlign: "center", color: "#666" }""")
                        +"Note: In demo mode, any credentials will work"
                    }
                }
            }
        }
    }
}