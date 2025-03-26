package screens

import com.veryphy.UserRole
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.style
import web.cssom.ClassName
import web.html.InputType

// Simplified version with only the essential props
external interface LoginScreenProps : Props {
    var onLogin: (UserRole) -> Unit
}

// Simplified LoginScreen with immediate login (no network calls)
val LoginScreen = FC<LoginScreenProps> { props ->
    div {
        className = ClassName("login-container")

        div {
            className = ClassName("logo-container")
            img {
                className = ClassName("logo")
                src = "veryphy_logo.png"
                alt = "VeryPhy Logo"
            }
        }

        div {
            className = ClassName("login-card")

            p {
                +"Secure verification using blockchain and AI"
                style("text-align:center;")
            }

            form {
                className = ClassName("login-form")
                onSubmit = { e ->
                    e.preventDefault()
                }

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
                        placeholder = "username (demo)"
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
                        placeholder = "password (demo)"
                    }
                }

                p {
                    +"Note: For demo purposes, you can log in with any credentials"
                }

                div {
                    className = ClassName("role-selection")
                    button {
                        className = ClassName("btn role-btn")
                        onClick = {
                            // For demo purposes, immediately call onLogin without real authentication
                            props.onLogin(UserRole.UNIVERSITY)
                        }
                        +"Login as University"
                    }

                    button {
                        className = ClassName("btn role-btn")
                        onClick = {
                            props.onLogin(UserRole.EMPLOYER)
                        }
                        +"Login as Employer"
                    }

                    button {
                        className = ClassName("btn role-btn")
                        onClick = {
                            props.onLogin(UserRole.ADMIN)
                        }
                        +"Login as Admin"
                    }
                }
            }
        }
    }
}