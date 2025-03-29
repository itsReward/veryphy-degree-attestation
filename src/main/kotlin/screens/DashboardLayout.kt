package com.veryphy

import com.veryphy.models.UserRole
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.i
import web.cssom.ClassName
import react.useState
import react.useEffect
import web.window.window

// Define props interface for dashboard layout
external interface DashboardLayoutProps : Props {
    var title: String
    var username: String
    var role: UserRole
    var onLogout: () -> Unit
    var children: ReactNode
}

// Modern Dashboard layout component with collapsible sidebar
val DashboardLayout = FC<DashboardLayoutProps> { props ->
    // State to track if sidebar is collapsed
    val (sidebarCollapsed, setSidebarCollapsed) = useState(false)

    // State to track screen size for responsive behavior
    val (isMobile, setIsMobile) = useState(window.innerWidth < 768)

    // Toggle sidebar collapsed state
    val toggleSidebar = {
        setSidebarCollapsed(!sidebarCollapsed)
    }

    // Effect to handle window resize
    useEffect(dependencies = emptyArray()) {
        // Function that updates the state based on window width
        fun handleResize() {
            setIsMobile(window.innerWidth < 768)
        }

        // Run once on mount to set initial state
        handleResize()

        // Skip event listeners and just poll for window size changes instead
        val intervalId = js("setInterval(function() { handleResize(); }, 500)")

        // Return cleanup function
        cleanup {
            js("clearInterval(intervalId)")
        }
    }

    div {
        className = ClassName("dashboard-container")
        style = js("""{ 
            display: "flex", 
            flexDirection: "column", 
            height: "100vh", 
            background: "#f5f7fa"
        }""")

        header {
            className = ClassName("header")
            style = js("""{ 
                display: "flex", 
                alignItems: "center", 
                padding: "0 20px", 
                height: "64px", 
                background: "linear-gradient(90deg, #0C8B44 0%, #0a7538 100%)", 
                color: "white",
                boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)",
                zIndex: "10"
            }""")

            // Toggle sidebar button
            button {
                className = ClassName("sidebar-toggle")
                style = js("""{ 
                    background: "transparent", 
                    border: "none", 
                    color: "white", 
                    fontSize: "20px", 
                    cursor: "pointer", 
                    marginRight: "15px", 
                    width: "40px", 
                    height: "40px", 
                    display: "flex", 
                    alignItems: "center", 
                    justifyContent: "center", 
                    borderRadius: "50%", 
                    transition: "background-color 0.3s"
                }""")
                onClick = { toggleSidebar() }

                // Icon for toggle button based on sidebar state
                if (sidebarCollapsed) {
                    i {
                        className = ClassName("fas fa-bars")
                        style = js("""{ 
                            width: "20px", 
                            height: "20px", 
                            display: "flex", 
                            alignItems: "center", 
                            justifyContent: "center"
                        }""")
                    }
                } else {
                    i {
                        className = ClassName("fas fa-times")
                        style = js("""{ 
                            width: "20px", 
                            height: "20px", 
                            display: "flex", 
                            alignItems: "center", 
                            justifyContent: "center"
                        }""")
                    }
                }
            }

            // Logo
            div {
                className = ClassName("logo-container")
                style = js("""{ marginRight: "20px" }""")
                img {
                    src = "veryphy_logo.svg"
                    alt = "VeryPhy Logo"
                    style = js("""{ height: "30px" }""")
                }
            }

            h2 {
                style = js("""{ 
                    margin: "0", 
                    flexGrow: "1", 
                    fontSize: "18px", 
                    fontWeight: "500" 
                }""")
                +props.title
            }

            div {
                className = ClassName("header-actions")
                style = js("""{ 
                    display: "flex", 
                    alignItems: "center", 
                    gap: "15px" 
                }""")

                span {
                    style = js("""{ 
                        fontSize: "14px", 
                        opacity: "0.9" 
                    }""")
                    +"Welcome, ${props.username}"
                }

                button {
                    className = ClassName("btn-logout")
                    style = js("""{ 
                        padding: "8px 16px", 
                        backgroundColor: "rgba(255, 255, 255, 0.15)", 
                        color: "white", 
                        border: "none", 
                        borderRadius: "4px", 
                        cursor: "pointer", 
                        fontSize: "14px", 
                        transition: "background-color 0.3s" 
                    }""")
                    onClick = { props.onLogout() }
                    +"Logout"
                }
            }
        }

        div {
            className = ClassName("dashboard-layout")
            style = js("""{ 
                display: "flex", 
                flex: "1", 
                height: "calc(100vh - 64px)", 
                overflow: "hidden" 
            }""")

            // Sidebar with collapsible behavior
            div {
                className = ClassName(if (sidebarCollapsed) "sidebar collapsed" else "sidebar")
                style = if (sidebarCollapsed) {
                    if (isMobile) {
                        js("""{ 
                            width: "0", 
                            overflow: "hidden", 
                            transition: "width 0.3s ease"
                        }""")
                    } else {
                        js("""{ 
                            width: "64px", 
                            background: "white", 
                            boxShadow: "2px 0 10px rgba(0, 0, 0, 0.05)", 
                            zIndex: "5", 
                            transition: "width 0.3s ease", 
                            overflow: "hidden"
                        }""")
                    }
                } else {
                    js("""{ 
                        width: "240px", 
                        background: "white", 
                        boxShadow: "2px 0 10px rgba(0, 0, 0, 0.05)", 
                        zIndex: "5", 
                        transition: "width 0.3s ease", 
                        overflow: "hidden"
                    }""")
                }

                // Sidebar header
                if (!sidebarCollapsed || !isMobile) {
                    div {
                        style = if (sidebarCollapsed) {
                            js("""{ 
                            padding: "20px 15px", 
                            borderBottom: "1px solid rgba(0, 0, 0, 0.05)", 
                            display: "flex", 
                            alignItems: "center", 
                            justifyContent: "center"  
                        }""")
                        } else {
                            js("""{ 
                            padding: "20px 15px", 
                            borderBottom: "1px solid rgba(0, 0, 0, 0.05)", 
                            display: "flex", 
                            alignItems: "center", 
                            justifyContent: "flex-start" 
                        }""")
                        }


                        if (!sidebarCollapsed) {
                            h2 {
                                style = js("""{ 
                                    margin: "0", 
                                    fontSize: "16px", 
                                    fontWeight: "500", 
                                    color: "#0C8B44" 
                                }""")
                                +"Navigation"
                            }
                        } else {
                            i {
                                className = ClassName("fas fa-th-large")
                                style = js("""{ 
                                    color: "#0C8B44", 
                                    fontSize: "20px" 
                                }""")
                            }
                        }
                    }
                }

                // Sidebar navigation - shows icons-only when collapsed
                Sidebar {
                    role = props.role
                    isCollapsed = sidebarCollapsed
                }
            }

            // Main content area
            div {
                className = ClassName(if (sidebarCollapsed) "main-content expanded" else "main-content")
                style = js("""{ 
                    flex: "1", 
                    padding: "20px", 
                    overflow: "auto", 
                    transition: "margin-left 0.3s ease" 
                }""")

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

// Modern Sidebar component
val Sidebar = FC<SidebarProps> { props ->
    ul {
        className = ClassName("nav-menu")
        style = js("""{ 
            listStyle: "none", 
            padding: "0", 
            margin: "0" 
        }""")

        // Dashboard item
        li {
            style = js("""{ margin: "5px 0" }""")
            a {
                href = "#"
                title = "Dashboard"
                style = if (props.isCollapsed) {
                    js("""{ 
                        display: "flex", 
                        justifyContent: "center", 
                        alignItems: "center", 
                        padding: "12px", 
                        color: "#495057", 
                        textDecoration: "none", 
                        borderRadius: "5px", 
                        margin: "0 8px", 
                        transition: "all 0.3s" 
                    }""")
                } else {
                    js("""{ 
                        display: "flex", 
                        alignItems: "center", 
                        padding: "12px 15px", 
                        color: "#495057", 
                        textDecoration: "none", 
                        borderRadius: "5px", 
                        margin: "0 8px", 
                        transition: "all 0.3s" 
                    }""")
                }

                i {
                    className = ClassName("fas fa-tachometer-alt")
                    style = if (props.isCollapsed) {
                        js("""{ 
                            fontSize: "16px", 
                            width: "20px", 
                            marginRight: "0" 
                        }""")
                    } else {
                        js("""{ 
                            fontSize: "16px", 
                            width: "20px", 
                            marginRight: "10px" 
                        }""")
                    }
                }

                if (!props.isCollapsed) {
                    span {
                        +"Dashboard"
                    }
                }
            }
        }

        // Role-specific items
        when (props.role) {
            UserRole.UNIVERSITY -> {
                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Register Degree"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-graduation-cap")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Register Degree"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "View Degrees"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-list")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"View Degrees"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Account Settings"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-user-cog")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Account Settings"
                            }
                        }
                    }
                }
            }

            UserRole.EMPLOYER -> {
                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Verify Degree"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-check-circle")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Verify Degree"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Verification History"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-history")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Verification History"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Account Settings"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-user-cog")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Account Settings"
                            }
                        }
                    }
                }
            }

            UserRole.ADMIN -> {
                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Universities"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s",
                                backgroundColor: "rgba(12, 139, 68, 0.1)",
                                color: "#0C8B44"
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s",
                                backgroundColor: "rgba(12, 139, 68, 0.1)",
                                color: "#0C8B44"
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-university")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Universities"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "System Statistics"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-chart-bar")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"System Statistics"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "User Management"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-users-cog")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"User Management"
                            }
                        }
                    }
                }

                li {
                    style = js("""{ margin: "5px 0" }""")
                    a {
                        href = "#"
                        title = "Settings"
                        style = if (props.isCollapsed) {
                            js("""{ 
                                display: "flex", 
                                justifyContent: "center", 
                                alignItems: "center", 
                                padding: "12px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        } else {
                            js("""{ 
                                display: "flex", 
                                alignItems: "center", 
                                padding: "12px 15px", 
                                color: "#495057", 
                                textDecoration: "none", 
                                borderRadius: "5px", 
                                margin: "0 8px", 
                                transition: "all 0.3s" 
                            }""")
                        }

                        i {
                            className = ClassName("fas fa-cogs")
                            style = if (props.isCollapsed) {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "0" 
                                }""")
                            } else {
                                js("""{ 
                                    fontSize: "16px", 
                                    width: "20px", 
                                    marginRight: "10px" 
                                }""")
                            }
                        }

                        if (!props.isCollapsed) {
                            span {
                                +"Settings"
                            }
                        }
                    }
                }
            }
        }
    }
}