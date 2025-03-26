package com.veryphy

import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.a
import web.cssom.ClassName

// Define props interface for dashboard layout
interface DashboardLayoutProps : Props {
    var title: String
    var username: String
    var role: UserRole
    var onLogout: () -> Unit
    var children: ReactNode
}

// Dashboard layout component
val DashboardLayout = FC<DashboardLayoutProps> { props ->
    div {
        className = ClassName("dashboard-container")

        header {
            className = ClassName("header")
            h1 {
                +props.title
            }
            div {
                className = ClassName("header-actions")
                span {
                    +"Welcome, ${props.username}"
                }
                button {
                    className = ClassName("btn-logout")
                    onClick = { props.onLogout() }
                    +"Logout"
                }
            }
        }

        div {
            className = ClassName("dashboard-layout")

            div {
                className = ClassName("sidebar")
                h3 {
                    +"Navigation"
                }

                // Sidebar navigation
                Sidebar {
                    role = props.role
                }
            }

            div {
                className = ClassName("main-content")
                // Render the children passed to this component
                +props.children
            }
        }
    }
}

// Sidebar component
interface SidebarProps : Props {
    var role: UserRole
}

val Sidebar = FC<SidebarProps> { props ->
    ul {
        className = ClassName("nav-menu")
        li {
            a {
                href = "#"
                +"Dashboard"
            }
        }

        when (props.role) {
            UserRole.UNIVERSITY -> {
                li {
                    a {
                        href = "#"
                        +"Register Degree"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"View Degrees"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Account"
                    }
                }
            }

            UserRole.EMPLOYER -> {
                li {
                    a {
                        href = "#"
                        +"Verify Degree"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Verification History"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Account"
                    }
                }
            }

            UserRole.ADMIN -> {
                li {
                    a {
                        href = "#"
                        +"Universities"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Employers"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"System Stats"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Settings"
                    }
                }
            }
        }
    }
}