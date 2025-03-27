package com.veryphy.screens

import com.veryphy.DashboardLayout
import com.veryphy.models.UserRole
import com.veryphy.models.VerificationResult
import react.FC
import react.dom.events.FormEvent
import react.dom.html.ReactHTML
import web.cssom.ClassName
import web.html.InputType

// Employer Dashboard Screen
val EmployerDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.employerViewModel
    val user = props.appState.loginViewModel.user

    DashboardLayout {
        title = "Employer Dashboard"
        username = user?.name ?: "Employer User"
        role = UserRole.EMPLOYER
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
                +"Verify Degree Certificate"
            }

            ReactHTML.form {
                className = ClassName("form-group")
                onSubmit = { e: FormEvent<*> ->
                    e.preventDefault()
                    // For demo purposes, only simulate verification
                    viewModel.verifyDegree { verification ->
                        console.log("Certificate verified successfully: ${verification.result}")
                    }
                }

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "certificateUpload"
                        +"Upload Certificate Image"
                    }
                    ReactHTML.input {
                        id = "certificateUpload"
                        type = InputType.file
                        className = ClassName("form-control")
                        accept = ".jpg,.jpeg,.png,.pdf"
                        onChange = { e ->
                            val input = e.target as web.html.HTMLInputElement
                            viewModel.handleCertificateUpload(input)
                        }
                    }
                }

                ReactHTML.button {
                    className = ClassName("btn primary-btn")
                    type = web.html.ButtonType.submit
                    +"Verify Certificate"
                }
            }

            ReactHTML.h2 {
                +"Verification History"
            }

            if (viewModel.isLoading) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    +"Loading verification history..."
                }
            } else if (viewModel.verificationHistory.isEmpty()) {
                ReactHTML.p {
                    +"No verification history yet. Verify your first certificate using the form above."
                }
            } else {
                ReactHTML.table {
                    className = ClassName("data-table")
                    ReactHTML.thead {
                        ReactHTML.tr {
                            ReactHTML.th { +"ID" }
                            ReactHTML.th { +"Student" }
                            ReactHTML.th { +"University" }
                            ReactHTML.th { +"Date" }
                            ReactHTML.th { +"Result" }
                        }
                    }
                    ReactHTML.tbody {
                        viewModel.verificationHistory.forEach { verification ->
                            ReactHTML.tr {
                                ReactHTML.td { +verification.id }
                                ReactHTML.td { +verification.studentName }
                                ReactHTML.td { +verification.universityName }
                                ReactHTML.td { +verification.requestDate }
                                ReactHTML.td {
                                    ReactHTML.span {
                                        className = ClassName(when(verification.result) {
                                            VerificationResult.AUTHENTIC -> "status-success"
                                            VerificationResult.FAILED -> "status-error"
                                            VerificationResult.PENDING -> "status-pending"
                                        })
                                        +verification.result.name
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