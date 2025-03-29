package com.veryphy.screens

import com.veryphy.AppState
import com.veryphy.DashboardLayout
import com.veryphy.models.UserRole
import com.veryphy.models.VerificationResult
import kotlinx.browser.window
import react.FC
import react.dom.events.ChangeEvent
import react.dom.html.ReactHTML
import react.useState
import web.cssom.ClassName
import web.html.InputType

// Employer Dashboard Screen
val EmployerDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.employerViewModel
    val user = props.appState.loginViewModel.user
    val demoMode = props.appState.demoMode

    // State for form error message
    val (formError, setFormError) = useState<String?>(null)

    // State for success message
    val (successMessage, setSuccessMessage) = useState<String?>(null)

    // State for showing verification details
    val (showVerificationDetails, setShowVerificationDetails) = useState(false)

    DashboardLayout {
        title = "Employer Dashboard"
        username = user?.name ?: "Employer User"
        role = UserRole.EMPLOYER
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
                +"Verify Degree Certificate"
            }

            // Show verification result if we have one
            if (showVerificationDetails && viewModel.currentVerification != null) {
                val verification = viewModel.currentVerification!!

                ReactHTML.div {
                    className = ClassName("verification-result")
                    style = if (verification.result == VerificationResult.AUTHENTIC) {
                        js("""{ 
                            padding: "16px", 
                            backgroundColor: "rgba(40, 167, 69, 0.1)", 
                            color: "#28a745", 
                            borderRadius: "4px", 
                            marginBottom: "24px" 
                        }""")
                    } else {
                        js("""{ 
                            padding: "16px", 
                            backgroundColor: "rgba(220, 53, 69, 0.1)", 
                            color: "#dc3545", 
                            borderRadius: "4px", 
                            marginBottom: "24px" 
                        }""")
                    }

                    ReactHTML.div {
                        style = js("""{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "12px" }""")

                        ReactHTML.h3 {
                            style = js("""{ margin: 0 }""")
                            +if (verification.result == VerificationResult.AUTHENTIC) "✓ Certificate Verified" else "✗ Certificate Invalid"
                        }

                        ReactHTML.button {
                            className = ClassName("btn")
                            style = js("""{ padding: "4px 8px", fontSize: "12px" }""")
                            onClick = {
                                setShowVerificationDetails(false)
                            }
                            +"Close"
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("verification-details")

                        ReactHTML.div {
                            style = js("""{ margin: "8px 0" }""")
                            ReactHTML.span { style = js("""{ fontWeight: "bold" }"""); +"Student: " }
                            +verification.studentName
                        }

                        ReactHTML.div {
                            style = js("""{ margin: "8px 0" }""")
                            ReactHTML.span { style = js("""{ fontWeight: "bold" }"""); +"University: " }
                            +verification.universityName
                        }

                        ReactHTML.div {
                            style = js("""{ margin: "8px 0" }""")
                            ReactHTML.span { style = js("""{ fontWeight: "bold" }"""); +"Date: " }
                            +verification.requestDate
                        }

                        ReactHTML.div {
                            style = js("""{ margin: "8px 0" }""")
                            ReactHTML.span { style = js("""{ fontWeight: "bold" }"""); +"Verification ID: " }
                            +verification.id
                        }
                    }
                }
            }

            ReactHTML.div {
                className = ClassName("form-group")

                ReactHTML.div {
                    className = ClassName("form-group")
                    ReactHTML.label {
                        htmlFor = "degreeHash"
                        +"Degree Hash"
                    }
                    ReactHTML.input {
                        id = "degreeHash"
                        type = InputType.text
                        className = ClassName("form-control")
                        placeholder = "Enter the degree hash to verify"
                        value = viewModel.degreeHash
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.degreeHash = e.target.value
                        }
                    }
                }

                ReactHTML.div {
                    style = js("""{ display: "flex", gap: "16px" }""")

                    ReactHTML.button {
                        className = ClassName("btn primary-btn")
                        type = web.html.ButtonType.submit
                        disabled = viewModel.isLoading

                        if (viewModel.isLoading) {
                            +"Verifying..."
                        } else {
                            +"Verify by Hash"
                        }
                    }
                }
            }

            ReactHTML.div {
                style = js("""{ margin: "32px 0", borderTop: "1px solid #ddd", paddingTop: "16px" }""")
            }

            ReactHTML.h2 {
                +"File Upload (Certificate Image)"
            }

            ReactHTML.div {
                className = ClassName("form-group")

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
                    type = web.html.ButtonType.button
                    disabled = viewModel.isLoading || viewModel.certificateFile == null
                    onClick = {
                        if (viewModel.certificateFile == null) {
                            setFormError("Please select a certificate file to upload")
                        }

                        setFormError(null)
                        setSuccessMessage("Certificate upload not implemented in this demo")

                        // Clear success message after 3 seconds
                        window.setTimeout({
                            setSuccessMessage(null)
                        }, 3000)
                    }

                    if (viewModel.isLoading) {
                        +"Processing..."
                    } else {
                        +"Verify Certificate"
                    }
                }
            }

            ReactHTML.h2 {
                +"Verification History"
            }

            if (viewModel.isLoading && viewModel.verificationHistory.isEmpty()) {
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
                            ReactHTML.th { +"Actions" }
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
                                ReactHTML.td {
                                    ReactHTML.button {
                                        className = ClassName("btn")
                                        style = js("""{ padding: "4px 8px", fontSize: "12px" }""")
                                        onClick = {
                                            viewModel.currentVerification = verification
                                            setShowVerificationDetails(true)

                                            // Scroll to top
                                            web.dom.document.documentElement?.scrollTo(0.0, 0.0)
                                        }
                                        +"View"
                                    }
                                }
                            }
                        }
                    }
                }

                // Load more button if there are more verifications
                if (viewModel.hasMoreVerifications) {
                    ReactHTML.div {
                        className = ClassName("load-more")
                        style = js("""{ textAlign: "center", marginTop: "20px" }""")

                        ReactHTML.button {
                            className = ClassName("btn")
                            onClick = {
                                if (demoMode) {
                                    viewModel.demoLoadVerificationHistory()
                                } else {
                                    viewModel.loadVerificationHistory(user?.id ?: "", false)
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