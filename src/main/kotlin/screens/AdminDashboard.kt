package com.veryphy.screens

import com.veryphy.DashboardLayout
import com.veryphy.models.UniversityStatus
import com.veryphy.models.UserRole
import react.FC
import react.dom.events.ChangeEvent
import react.dom.events.FormEvent
import react.dom.html.ReactHTML
import web.cssom.ClassName
import web.html.InputType

// Admin Dashboard Screen
val AdminDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.adminViewModel
    val user = props.appState.loginViewModel.user

    DashboardLayout {
        title = "Admin Dashboard"
        username = user?.name ?: "Admin User"
        role = UserRole.ADMIN
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
                +"System Statistics"
            }

            if (viewModel.isLoading) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    +"Loading statistics..."
                }
            } else {
                ReactHTML.div {
                    className = ClassName("stats-container")

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h2 { +"Universities" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.registeredUniversities ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h2 { +"Total Degrees" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.totalDegrees ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h2 { +"Verifications" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.verificationCount ?: 0}" }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        ReactHTML.h2 { +"Success Rate" }
                        ReactHTML.p { className = ClassName("stat-value"); +"${viewModel.systemStats?.successRate ?: 0}%" }
                    }
                }
            }

            ReactHTML.h2 {
                +"Manage Universities"
            }

            ReactHTML.form {
                className = ClassName("form-group")
                onSubmit = { e: FormEvent<*> ->
                    e.preventDefault()
                    viewModel.addUniversity {
                        console.log("University added successfully")
                    }
                }

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

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    type = web.html.ButtonType.submit
                    +"Add University"
                }
            }

            ReactHTML.h2 {
                +"Registered Universities"
            }

            if (viewModel.isLoading) {
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
                                ReactHTML.td {
                                    ReactHTML.button {
                                        className = ClassName("btn action-btn")
                                        onClick = {
                                            if (university.status != UniversityStatus.BLACKLISTED) {
                                                viewModel.blacklistUniversity(university.id) {
                                                    console.log("University blacklisted: ${university.id}")
                                                }
                                            }
                                        }
                                        if (university.status != UniversityStatus.BLACKLISTED) {
                                            +"Blacklist"
                                        } else {
                                            +"Blacklisted"
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
}