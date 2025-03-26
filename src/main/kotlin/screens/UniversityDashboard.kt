package com.veryphy.screens

import com.veryphy.DashboardLayout
import com.veryphy.DegreeStatus
import com.veryphy.UserRole
import react.FC
import react.dom.events.ChangeEvent
import react.dom.events.FormEvent
import react.dom.html.ReactHTML
import web.cssom.ClassName
import web.html.InputType

// University Dashboard Screen
val UniversityDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.universityViewModel

    DashboardLayout {
        title = "University Dashboard"
        username = props.appState.loginViewModel.user?.name ?: "University User"
        role = UserRole.UNIVERSITY
        onLogout = props.onLogout

        ReactHTML.div {
            className = ClassName("dashboard-content")

            viewModel.errorMessage?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    +it
                }
            }

            ReactHTML.h2 {
                +"Register New Degree"
            }

            ReactHTML.form {
                className = ClassName("form-group")
                onSubmit = { e: FormEvent<*> ->
                    e.preventDefault()
                    // For demo purposes, only simulate the operation
                    console.log("Registering degree...")
                }

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

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    +"Register Degree"
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
                            }
                        }
                    }
                }
            }
        }
    }
}