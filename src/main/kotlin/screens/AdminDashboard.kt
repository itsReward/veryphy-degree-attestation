package com.veryphy.screens

import com.veryphy.AppState
import com.veryphy.DashboardLayout
import com.veryphy.models.UniversityStatus
import com.veryphy.models.UserRole
import kotlinx.browser.window
import react.FC
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML
import react.useState
import web.cssom.ClassName
import web.html.InputType

// Admin Dashboard Screen
val AdminDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.adminViewModel
    val user = props.appState.loginViewModel.user
    val demoMode = props.appState.demoMode

    // State for form error message
    val (formError, setFormError) = useState<String?>(null)

    // State for success message
    val (successMessage, setSuccessMessage) = useState<String?>(null)

    DashboardLayout {
        title = "Admin Dashboard"
        username = user?.name ?: "Admin User"
        role = UserRole.ADMIN
        onLogout = props.onLogout

        ReactHTML.div {
            className = ClassName("dashboard-content")

            // Show API error if any
            viewModel.errorMessage?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    +it
                }
            }

            // Show form error if any
            formError?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    +it
                }
            }

            // Show success message if any
            successMessage?.let {
                ReactHTML.div {
                    className = ClassName("success-message")
                    style = js("""{ 
                        padding: "16px", 
                        backgroundColor: "rgba(40, 167, 69, 0.1)", 
                        color: "#28a745", 
                        borderRadius: "4px", 
                        marginBottom: "16px" 
                    }""")
                    +it
                }
            }

            ReactHTML.h2 {
                +"System Statistics"
            }

            if (viewModel.isLoading && viewModel.systemStats == null) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    +"Loading statistics..."
                }
            } else {
                ReactHTML.div {
                    className = ClassName("stats-container")

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h3 { +"Universities" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.registeredUniversities ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h3 { +"Total Degrees" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.totalDegrees ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h3 { +"Verifications" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.verificationCount ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h3 { +"Success Rate" }
                        ReactHTML.p {
                            className = ClassName("stat-value")
                            val successRate = viewModel.systemStats?.successRate ?: 0.0
                            val formattedRate = (successRate * 10).toInt() / 10.0 // Round to 1 decimal place
                            +"$formattedRate%"
                        }
                    }
                }
            }

            ReactHTML.h2 {
                +"Register New University"
            }

            ReactHTML.div {
                className = ClassName("form-group")

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "universityName"
                        +"University Name"
                    }
                    ReactHTML.input {
                        id = "universityName"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = viewModel.universityName
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.universityName = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "universityEmail"
                        +"Email"
                    }
                    ReactHTML.input {
                        id = "universityEmail"
                        type = InputType.email
                        className = ClassName("form-control")
                        value = viewModel.universityEmail
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.universityEmail = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "universityAddress"
                        +"Address (Optional)"
                    }
                    ReactHTML.input {
                        id = "universityAddress"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = viewModel.universityAddress
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.universityAddress = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "stakeAmount"
                        +"Stake Amount"
                    }
                    ReactHTML.input {
                        id = "stakeAmount"
                        type = InputType.number
                        min = "1000"
                        step = 100.0
                        className = ClassName("form-control")
                        value = viewModel.stakeAmount
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.stakeAmount = e.target.value
                        }
                    }
                }

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    type = web.html.ButtonType.button
                    disabled = viewModel.isLoading
                    onClick = {
                        if (viewModel.universityName.isBlank() || viewModel.universityEmail.isBlank()) {
                            setFormError("University name and email are required")
                           // return@onClick
                        }

                        setFormError(null)

                        // For demo purposes or real API
                        if (demoMode) {
                            viewModel.demoAddUniversity {
                                setSuccessMessage("University added successfully!")

                                // Clear success message after 3 seconds
                                window.setTimeout({
                                    setSuccessMessage(null)
                                }, 3000)
                            }
                        } else {
                            viewModel.addUniversity(
                                onSuccess = {
                                    setSuccessMessage("University added successfully!")

                                    // Clear success message after 3 seconds
                                    window.setTimeout({
                                        setSuccessMessage(null)
                                    }, 3000)
                                },
                                onError = { errorMessage ->
                                    setFormError(errorMessage)
                                }
                            )
                        }
                    }

                    if (viewModel.isLoading) {
                        +"Adding..."
                    } else {
                        +"Add University"
                    }
                }
            }

            ReactHTML.h2 {
                +"Registered Universities"
            }

            if (viewModel.isLoading && viewModel.universities.isEmpty()) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    +"Loading universities..."
                }
            } else if (viewModel.universities.isEmpty()) {
                ReactHTML.p {
                    +"No universities registered yet. Add your first university using the form above."
                }
            } else {
                ReactHTML.table {
                    className = ClassName("data-table")
                    ReactHTML.thead {
                        ReactHTML.tr {
                            ReactHTML.th { +"ID" }
                            ReactHTML.th { +"Name" }
                            ReactHTML.th { +"Email" }
                            ReactHTML.th { +"Status" }
                            ReactHTML.th { +"Join Date" }
                            ReactHTML.th { +"Actions" }
                        }
                    }
                    ReactHTML.tbody {
                        viewModel.universities.forEach { university ->
                            ReactHTML.tr {
                                ReactHTML.td { +university.id }
                                ReactHTML.td { +university.name }
                                ReactHTML.td { +university.email }
                                ReactHTML.td {
                                    ReactHTML.span {
                                        className = ClassName(when(university.status) {
                                            UniversityStatus.ACTIVE -> "status-success"
                                            UniversityStatus.PENDING -> "status-pending"
                                            UniversityStatus.BLACKLISTED -> "status-error"
                                        })
                                        +university.status.name
                                    }
                                }
                                ReactHTML.td { +university.joinDate }
                                ReactHTML.td {
                                    ReactHTML.button {
                                        className = ClassName("btn action-btn")
                                        style = if (university.status == UniversityStatus.BLACKLISTED) {
                                            js("""{ 
                                                backgroundColor: "#6c757d", 
                                                color: "white",
                                                padding: "4px 8px",
                                                fontSize: "12px"
                                            }""")
                                        } else {
                                            js("""{ 
                                                backgroundColor: "#dc3545", 
                                                color: "white",
                                                padding: "4px 8px",
                                                fontSize: "12px"
                                            }""")
                                        }
                                        disabled = university.status == UniversityStatus.BLACKLISTED || viewModel.isLoading
                                        onClick = {
                                            if (university.status != UniversityStatus.BLACKLISTED) {
                                                if (demoMode) {
                                                    viewModel.demoBlacklistUniversity(university.id) {
                                                        setSuccessMessage("University blacklisted successfully")

                                                        // Clear success message after 3 seconds
                                                        window.setTimeout({
                                                            setSuccessMessage(null)
                                                        }, 3000)
                                                    }
                                                } else {
                                                    viewModel.blacklistUniversity(
                                                        university.id,
                                                        onSuccess = {
                                                            setSuccessMessage("University blacklisted successfully")

                                                            // Clear success message after 3 seconds
                                                            window.setTimeout({
                                                                setSuccessMessage(null)
                                                            }, 3000)
                                                        },
                                                        onError = { errorMessage ->
                                                            setFormError(errorMessage)
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                        if (university.status == UniversityStatus.BLACKLISTED) {
                                            +"Blacklisted"
                                        } else {
                                            +"Blacklist"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Load more button if there are more universities
                if (viewModel.hasMoreUniversities) {
                    ReactHTML.div {
                        className = ClassName("load-more")
                        style = js("""{ textAlign: "center", marginTop: "20px" }""")

                        ReactHTML.button {
                            className = ClassName("btn")
                            onClick = {
                                if (demoMode) {
                                    viewModel.demoLoadUniversities()
                                } else {
                                    viewModel.loadUniversities(false)
                                }
                            }
                            disabled = viewModel.isLoading

                            if (viewModel.isLoading) {
                                +"Loading..."
                            } else {
                                +"Load More"
                            }
                        }
                    }
                }
            }
        }
    }
}