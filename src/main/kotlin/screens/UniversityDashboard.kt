package com.veryphy.screens

import com.veryphy.AppState
import com.veryphy.DashboardLayout
import com.veryphy.models.DegreeStatus
import com.veryphy.models.UserRole
import kotlinx.browser.window
import react.FC
import react.Props
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML
import react.useState
import web.cssom.ClassName
import web.html.InputType

// University Dashboard Screen
val UniversityDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.universityViewModel
    val user = props.appState.loginViewModel.user
    val demoMode = props.appState.demoMode

    // State for form error message
    val (formError, setFormError) = useState<String?>(null)

    // State for success message
    val (successMessage, setSuccessMessage) = useState<String?>(null)

    DashboardLayout {
        title = "University Dashboard"
        username = user?.name ?: "University User"
        role = UserRole.UNIVERSITY
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
                +"Register New Degree"
            }

            ReactHTML.div {
                className = ClassName("form-group")

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "studentId"
                        +"Student ID"
                    }
                    ReactHTML.input {
                        id = "studentId"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = viewModel.studentId
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.studentId = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "studentName"
                        +"Student Name"
                    }
                    ReactHTML.input {
                        id = "studentName"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = viewModel.studentName
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.studentName = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "degreeName"
                        +"Degree Name"
                    }
                    ReactHTML.input {
                        id = "degreeName"
                        type = InputType.text
                        className = ClassName("form-control")
                        value = viewModel.degreeName
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.degreeName = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "issueDate"
                        +"Issue Date"
                    }
                    ReactHTML.input {
                        id = "issueDate"
                        type = InputType.date
                        className = ClassName("form-control")
                        value = viewModel.issueDate
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.issueDate = e.target.value
                        }
                    }
                }

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    type = web.html.ButtonType.button
                    disabled = viewModel.isLoading
                    onClick = {
                        // Validate form
                        if (viewModel.studentId.isBlank() || viewModel.studentName.isBlank() || viewModel.degreeName.isBlank()) {
                            setFormError("Student ID, name, and degree name are required")
                        }

                        setFormError(null)

                        // For demo purposes or real API
                        if (demoMode) {
                            viewModel.demoRegisterDegree(user?.id ?: "uni-001", user?.name ?: "University of Technology") {
                                setSuccessMessage("Degree registered successfully!")

                                // Clear success message after 3 seconds
                                window.setTimeout({
                                    setSuccessMessage(null)
                                }, 3000)
                            }
                        } else {
                            // Use real API
                            viewModel.registerDegree(
                                user?.id ?: "",
                                onSuccess = {
                                    setSuccessMessage("Degree registered successfully!")

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
                        +"Registering..."
                    } else {
                        +"Register Degree"
                    }
                }
            }

            ReactHTML.h2 {
                +"Registered Degrees"
            }

            if (viewModel.isLoading) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    +"Loading degrees..."
                }
            } else if (viewModel.degrees.isEmpty()) {
                ReactHTML.p {
                    +"No degrees registered yet. Add your first degree using the form above."
                }
            } else {
                ReactHTML.table {
                    className = ClassName("data-table")
                    ReactHTML.thead {
                        ReactHTML.tr {
                            ReactHTML.th { +"Student ID" }
                            ReactHTML.th { +"Student Name" }
                            ReactHTML.th { +"Degree" }
                            ReactHTML.th { +"Issue Date" }
                            ReactHTML.th { +"Status" }
                            if (!demoMode) {
                                ReactHTML.th { +"Hash" }
                            }
                        }
                    }
                    ReactHTML.tbody {
                        viewModel.degrees.forEach { degree ->
                            ReactHTML.tr {
                                ReactHTML.td { +degree.studentId }
                                ReactHTML.td { +degree.studentName }
                                ReactHTML.td { +degree.degreeName }
                                ReactHTML.td { +degree.issueDate }
                                ReactHTML.td {
                                    ReactHTML.span {
                                        className = ClassName(when(degree.status) {
                                            DegreeStatus.VERIFIED -> "status-success"
                                            DegreeStatus.PROCESSING -> "status-pending"
                                            DegreeStatus.REGISTERED -> "status-pending"
                                        })
                                        +degree.status.name
                                    }
                                }
                                if (!demoMode) {
                                    ReactHTML.td {
                                        "${ +degree.degreeHash.take(10) } " + "..."
                                        title = degree.degreeHash
                                    }
                                }
                            }
                        }
                    }
                }

                // Load more button if there are more degrees
                if (viewModel.hasMoreDegrees) {
                    ReactHTML.div {
                        className = ClassName("load-more")
                        style = js("""{ textAlign: "center", marginTop: "20px" }""")

                        ReactHTML.button {
                            className = ClassName("btn")
                            onClick = {
                                if (demoMode) {
                                    viewModel.demoLoadDegrees()
                                } else {
                                    viewModel.loadDegrees(user?.id ?: "", false)
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