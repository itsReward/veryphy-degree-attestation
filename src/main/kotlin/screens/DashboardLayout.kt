package com.veryphy

import com.veryphy.models.UserRole
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.i
import web.cssom.ClassName
import react.useState

// Define props interface for dashboard layout
external interface DashboardLayoutProps : Props {
    var title: String
    var username: String
    var role: UserRole
    var onLogout: () -> Unit
    var children: ReactNode
}

// Dashboard layout component
val DashboardLayout = FC<DashboardLayoutProps> { props ->
    // State to track if sidebar is collapsed
    val (sidebarCollapsed, setSidebarCollapsed) = useState(false)

    // Toggle sidebar collapsed state
    val toggleSidebar = {
        setSidebarCollapsed(!sidebarCollapsed)
    }

    div {
        className = ClassName("dashboard-container")

        header {
            className = ClassName("header")
            div {
                className = ClassName("logo-container header-logo")
                img {
                    src = "veryphy_logo.svg"
                    alt = "VeryPhy Logo"
                    style = js("""{ height: '30px' }""")
                }
            }

            // Toggle sidebar button
            button {
                className = ClassName("sidebar-toggle")
                onClick = { toggleSidebar() }
                if (sidebarCollapsed) {
                    i { className = ClassName("fas fa-expand-arrows-alt") }
                } else {
                    i { className = ClassName("fas fa-compress-arrows-alt") }
                }
            }

            h2 {
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
                className = ClassName(if (sidebarCollapsed) "sidebar collapsed" else "sidebar")

                // Always show the navigation elements, but display them differently depending on collapse state
                if (!sidebarCollapsed) {
                    h3 {
                        +"Navigation"
                    }
                }

                // Sidebar navigation - now it will show icons-only when collapsed
                Sidebar {
                    role = props.role
                    isCollapsed = sidebarCollapsed
                }
            }

            div {
                className = ClassName(if (sidebarCollapsed) "main-content expanded" else "main-content")
                +props.children
            }
        }
    }
}

// Sidebar component props
external interface SidebarProps : Props {
    var role: UserRole
    var isCollapsed: Boolean
}

// Sidebar component
val Sidebar = FC<SidebarProps> { props ->
    ul {
        className = ClassName("nav-menu")
        li {
            a {
                href = "#"
                title = "Dashboard"
                i {
                    className = ClassName("fas fa-tachometer-alt")
                }
                if (!props.isCollapsed) {
                    span {
                        +" Dashboard"
                    }
                }
            }
        }

        when (props.role) {
            UserRole.UNIVERSITY -> {
                li {
                    a {
                        href = "#"
                        title = "Register Degree"
                        i {
                            className = ClassName("fas fa-graduation-cap")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Register Degree"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "View Degrees"
                        i {
                            className = ClassName("fas fa-list")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" View Degrees"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "Account Settings"
                        i {
                            className = ClassName("fas fa-user-cog")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Account Settings"
                            }
                        }
                    }
                }
            }

            UserRole.EMPLOYER -> {
                li {
                    a {
                        href = "#"
                        title = "Verify Degree"
                        i {
                            className = ClassName("fas fa-check-circle")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Verify Degree"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "Verification History"
                        i {
                            className = ClassName("fas fa-history")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Verification History"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "Account Settings"
                        i {
                            className = ClassName("fas fa-user-cog")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Account Settings"
                            }
                        }
                    }
                }
            }

            UserRole.ADMIN -> {
                li {
                    a {
                        href = "#"
                        title = "Universities"
                        i {
                            className = ClassName("fas fa-university")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Universities"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "System Statistics"
                        i {
                            className = ClassName("fas fa-chart-bar")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" System Statistics"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "User Management"
                        i {
                            className = ClassName("fas fa-users-cog")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" User Management"
                            }
                        }
                    }
                }
                li {
                    a {
                        href = "#"
                        title = "Settings"
                        i {
                            className = ClassName("fas fa-cogs")
                        }
                        if (!props.isCollapsed) {
                            span {
                                +" Settings"
                            }
                        }
                    }
                }
            }
        }
    }
}