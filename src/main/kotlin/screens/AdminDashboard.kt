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

// Admin Dashboard Screen with Swan Song inspired UI
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
            style = js(
                """{ 
                background: "linear-gradient(135deg, #f5f7fa 0%, #e4e7eb 100%)",
                borderRadius: "12px",
                padding: "30px",
                boxShadow: "0 4px 20px rgba(0, 0, 0, 0.05)",
                marginTop: "10px"
            }"""
            )

            // Show API error if any
            viewModel.errorMessage?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    style = js(
                        """{ 
                        background: "rgba(220, 53, 69, 0.1)", 
                        color: "#dc3545", 
                        padding: "15px", 
                        borderRadius: "8px", 
                        marginBottom: "25px",
                        border: "1px solid rgba(220, 53, 69, 0.2)"
                    }"""
                    )
                    +it
                }
            }

            // Show form error if any
            formError?.let {
                ReactHTML.div {
                    className = ClassName("error-message")
                    style = js(
                        """{ 
                        background: "rgba(220, 53, 69, 0.1)", 
                        color: "#dc3545", 
                        padding: "15px", 
                        borderRadius: "8px", 
                        marginBottom: "25px",
                        border: "1px solid rgba(220, 53, 69, 0.2)"
                    }"""
                    )
                    +it
                }
            }

            // Show success message if any
            successMessage?.let {
                ReactHTML.div {
                    className = ClassName("success-message")
                    style = js(
                        """{ 
                        background: "rgba(40, 167, 69, 0.1)", 
                        color: "#28a745", 
                        padding: "15px", 
                        borderRadius: "8px", 
                        marginBottom: "25px",
                        border: "1px solid rgba(40, 167, 69, 0.2)"
                    }"""
                    )
                    +it
                }
            }

            ReactHTML.h2 {
                style = js(
                    """{ 
                    color: "#0C8B44", 
                    fontSize: "26px", 
                    fontWeight: "400",
                    marginBottom: "25px",
                    letterSpacing: "0.5px",
                    borderBottom: "1px solid rgba(12, 139, 68, 0.2)",
                    paddingBottom: "15px"
                }"""
                )
                +"System Analytics"
            }

            if (viewModel.isLoading && viewModel.systemStats == null) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    style = js(
                        """{ 
                        textAlign: "center", 
                        padding: "40px",
                        color: "#0C8B44"
                    }"""
                    )
                    +"Loading system data..."
                }
            } else {
                ReactHTML.div {
                    className = ClassName("stats-container")
                    style = js(
                        """{ 
                        display: "grid", 
                        gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))", 
                        gap: "20px",
                        marginBottom: "40px"
                    }"""
                    )

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        style = js(
                            """{ 
                            background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                            borderRadius: "12px",
                            padding: "25px",
                            boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                            transition: "transform 0.3s, box-shadow 0.3s",
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center"
                        }"""
                        )
                        ReactHTML.h3 {
                            style = js(
                                """{ 
                                fontSize: "16px", 
                                fontWeight: "500", 
                                color: "#6c757d",
                                marginBottom: "10px",
                                textTransform: "uppercase",
                                letterSpacing: "1px"
                            }"""
                            )
                            +"Universities"
                        }
                        ReactHTML.p {
                            className = ClassName("stat-value")
                            style = js(
                                """{ 
                                fontSize: "36px", 
                                fontWeight: "300", 
                                color: "#0C8B44",
                                margin: "0"
                            }"""
                            )
                            +"${viewModel.systemStats?.registeredUniversities ?: 0}"
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        style = js(
                            """{ 
                            background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                            borderRadius: "12px",
                            padding: "25px",
                            boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                            transition: "transform 0.3s, box-shadow 0.3s",
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center"
                        }"""
                        )
                        ReactHTML.h3 {
                            style = js(
                                """{ 
                                fontSize: "16px", 
                                fontWeight: "500", 
                                color: "#6c757d",
                                marginBottom: "10px",
                                textTransform: "uppercase",
                                letterSpacing: "1px"
                            }"""
                            )
                            +"Total Degrees"
                        }
                        ReactHTML.p {
                            className = ClassName("stat-value")
                            style = js(
                                """{ 
                                fontSize: "36px", 
                                fontWeight: "300", 
                                color: "#0C8B44",
                                margin: "0"
                            }"""
                            )
                            +"${viewModel.systemStats?.totalDegrees ?: 0}"
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        style = js(
                            """{ 
                            background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                            borderRadius: "12px",
                            padding: "25px",
                            boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                            transition: "transform 0.3s, box-shadow 0.3s",
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center"
                        }"""
                        )
                        ReactHTML.h3 {
                            style = js(
                                """{ 
                                fontSize: "16px", 
                                fontWeight: "500", 
                                color: "#6c757d",
                                marginBottom: "10px",
                                textTransform: "uppercase",
                                letterSpacing: "1px"
                            }"""
                            )
                            +"Verifications"
                        }
                        ReactHTML.p {
                            className = ClassName("stat-value")
                            style = js(
                                """{ 
                                fontSize: "36px", 
                                fontWeight: "300", 
                                color: "#0C8B44",
                                margin: "0"
                            }"""
                            )
                            +"${viewModel.systemStats?.verificationCount ?: 0}"
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("stat-card")
                        style = js(
                            """{ 
                            background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                            borderRadius: "12px",
                            padding: "25px",
                            boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                            transition: "transform 0.3s, box-shadow 0.3s",
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            justifyContent: "center"
                        }"""
                        )
                        ReactHTML.h3 {
                            style = js(
                                """{ 
                                fontSize: "16px", 
                                fontWeight: "500", 
                                color: "#6c757d",
                                marginBottom: "10px",
                                textTransform: "uppercase",
                                letterSpacing: "1px"
                            }"""
                            )
                            +"Success Rate"
                        }
                        ReactHTML.p {
                            className = ClassName("stat-value")
                            style = js(
                                """{ 
                                fontSize: "36px", 
                                fontWeight: "300", 
                                color: "#0C8B44",
                                margin: "0"
                            }"""
                            )
                            val successRate = viewModel.systemStats?.successRate ?: 0.0
                            val formattedRate = (successRate * 10).toInt() / 10.0 // Round to 1 decimal place
                            +"$formattedRate%"
                        }
                    }
                }
            }

            ReactHTML.h2 {
                style = js(
                    """{ 
                    color: "#0C8B44", 
                    fontSize: "26px", 
                    fontWeight: "400",
                    marginTop: "40px",
                    marginBottom: "25px",
                    letterSpacing: "0.5px",
                    borderBottom: "1px solid rgba(12, 139, 68, 0.2)",
                    paddingBottom: "15px"
                }"""
                )
                +"University Registration"
            }

            ReactHTML.div {
                className = ClassName("form-container")
                style = js(
                    """{ 
                    background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                    borderRadius: "12px",
                    padding: "30px",
                    boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                    marginBottom: "40px"
                }"""
                )

                ReactHTML.div {
                    className = ClassName("form-group")

                    ReactHTML.div {
                        className = ClassName("form-group")
                        style = js("""{ marginBottom: "20px" }""")
                        ReactHTML.label {
                            htmlFor = "universityName"
                            style = js(
                                """{ 
                                display: "block", 
                                marginBottom: "8px", 
                                color: "#495057",
                                fontSize: "14px",
                                fontWeight: "500"
                            }"""
                            )
                            +"University Name"
                        }
                        ReactHTML.input {
                            id = "universityName"
                            type = InputType.text
                            className = ClassName("form-control")
                            style = js(
                                """{ 
                                width: "100%", 
                                padding: "12px 15px", 
                                borderRadius: "8px", 
                                border: "1px solid #ced4da",
                                fontSize: "16px",
                                transition: "border-color 0.3s",
                                backgroundColor: "#f8f9fa"
                            }"""
                            )
                            value = viewModel.universityName
                            onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                viewModel.universityName = e.target.value
                            }
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("form-group")
                        style = js("""{ marginBottom: "20px" }""")
                        ReactHTML.label {
                            htmlFor = "universityEmail"
                            style = js(
                                """{ 
                                display: "block", 
                                marginBottom: "8px", 
                                color: "#495057",
                                fontSize: "14px",
                                fontWeight: "500"
                            }"""
                            )
                            +"Email"
                        }
                        ReactHTML.input {
                            id = "universityEmail"
                            type = InputType.email
                            className = ClassName("form-control")
                            style = js(
                                """{ 
                                width: "100%", 
                                padding: "12px 15px", 
                                borderRadius: "8px", 
                                border: "1px solid #ced4da",
                                fontSize: "16px",
                                transition: "border-color 0.3s",
                                backgroundColor: "#f8f9fa"
                            }"""
                            )
                            value = viewModel.universityEmail
                            onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                viewModel.universityEmail = e.target.value
                            }
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("form-group")
                        style = js("""{ marginBottom: "20px" }""")
                        ReactHTML.label {
                            htmlFor = "universityAddress"
                            style = js(
                                """{ 
                                display: "block", 
                                marginBottom: "8px", 
                                color: "#495057",
                                fontSize: "14px",
                                fontWeight: "500"
                            }"""
                            )
                            +"Address (Optional)"
                        }
                        ReactHTML.input {
                            id = "universityAddress"
                            type = InputType.text
                            className = ClassName("form-control")
                            style = js(
                                """{ 
                                width: "100%", 
                                padding: "12px 15px", 
                                borderRadius: "8px", 
                                border: "1px solid #ced4da",
                                fontSize: "16px",
                                transition: "border-color 0.3s",
                                backgroundColor: "#f8f9fa"
                            }"""
                            )
                            value = viewModel.universityAddress
                            onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                viewModel.universityAddress = e.target.value
                            }
                        }
                    }

                    ReactHTML.div {
                        className = ClassName("form-group")
                        style = js("""{ marginBottom: "30px" }""")
                        ReactHTML.label {
                            htmlFor = "stakeAmount"
                            style = js(
                                """{ 
                                display: "block", 
                                marginBottom: "8px", 
                                color: "#495057",
                                fontSize: "14px",
                                fontWeight: "500"
                            }"""
                            )
                            +"Stake Amount"
                        }
                        ReactHTML.input {
                            id = "stakeAmount"
                            type = InputType.number
                            min = "1000"
                            step = 100.0
                            className = ClassName("form-control")
                            style = js(
                                """{ 
                                width: "100%", 
                                padding: "12px 15px", 
                                borderRadius: "8px", 
                                border: "1px solid #ced4da",
                                fontSize: "16px",
                                transition: "border-color 0.3s",
                                backgroundColor: "#f8f9fa"
                            }"""
                            )
                            value = viewModel.stakeAmount
                            onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                                viewModel.stakeAmount = e.target.value
                            }
                        }
                    }

                    ReactHTML.button {
                        className = ClassName("btn primary-btn")
                        style = if (viewModel.isLoading) {
                            js(
                                """{ 
                                backgroundColor: "#7ac29a",
                                color: "white",
                                padding: "15px 25px",
                                border: "none",
                                borderRadius: "8px",
                                cursor: "not-allowed",
                                fontSize: "16px",
                                fontWeight: "500",
                                width: "100%",
                                letterSpacing: "0.5px",
                                transition: "all 0.3s",
                                boxShadow: "0 4px 6px rgba(12, 139, 68, 0.15)"
                            }"""
                            )
                        } else {
                            js(
                                """{ 
                                backgroundColor: "#0C8B44",
                                color: "white",
                                padding: "15px 25px",
                                border: "none",
                                borderRadius: "8px",
                                cursor: "pointer",
                                fontSize: "16px",
                                fontWeight: "500",
                                width: "100%",
                                letterSpacing: "0.5px",
                                transition: "all 0.3s",
                                boxShadow: "0 4px 6px rgba(12, 139, 68, 0.15)"
                            }"""
                            )
                        }
                        type = web.html.ButtonType.button
                        disabled = viewModel.isLoading
                        onClick = {
                            if (viewModel.universityName.isBlank() || viewModel.universityEmail.isBlank()) {
                                setFormError("University name and email are required")
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
                            +"Processing..."
                        } else {
                            +"Register University"
                        }
                    }
                }
            }

            ReactHTML.h2 {
                style = js(
                    """{ 
                    color: "#0C8B44", 
                    fontSize: "26px", 
                    fontWeight: "400",
                    marginTop: "40px",
                    marginBottom: "25px",
                    letterSpacing: "0.5px",
                    borderBottom: "1px solid rgba(12, 139, 68, 0.2)",
                    paddingBottom: "15px"
                }"""
                )
                +"University Registry"
            }

            if (viewModel.isLoading && viewModel.universities.isEmpty()) {
                ReactHTML.div {
                    className = ClassName("loading-indicator")
                    style = js(
                        """{ 
                        textAlign: "center", 
                        padding: "40px",
                        color: "#0C8B44"
                    }"""
                    )
                    +"Loading universities..."
                }
            } else if (viewModel.universities.isEmpty()) {
                ReactHTML.div {
                    style = js(
                        """{ 
                        background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                        borderRadius: "12px",
                        padding: "30px",
                        textAlign: "center",
                        color: "#6c757d",
                        boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)"
                    }"""
                    )
                    ReactHTML.p {
                        +"No universities registered yet. Add your first university using the form above."
                    }
                }
            } else {
                ReactHTML.div {
                    style = js(
                        """{ 
                        background: "linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)",
                        borderRadius: "12px",
                        padding: "20px",
                        boxShadow: "0 4px 15px rgba(0, 0, 0, 0.05)",
                        overflowX: "auto"
                    }"""
                    )

                    ReactHTML.table {
                        className = ClassName("data-table")
                        style = js(
                            """{ 
                            width: "100%", 
                            borderCollapse: "separate",
                            borderSpacing: "0",
                            fontSize: "14px"
                        }"""
                        )
                        ReactHTML.thead {
                            style = js(
                                """{ 
                                backgroundColor: "rgba(12, 139, 68, 0.1)"
                            }"""
                            )
                            ReactHTML.tr {
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"ID"
                                }
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"Name"
                                }
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"Email"
                                }
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"Status"
                                }
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"Join Date"
                                }
                                ReactHTML.th {
                                    style = js(
                                        """{ 
                                        padding: "15px", 
                                        textAlign: "left", 
                                        fontWeight: "500", 
                                        color: "#0C8B44",
                                        borderBottom: "2px solid rgba(12, 139, 68, 0.2)"
                                    }"""
                                    )
                                    +"Actions"
                                }
                            }
                        }
                        ReactHTML.tbody {
                            viewModel.universities.forEach { university ->
                                ReactHTML.tr {
                                    style = js(
                                        """{ 
                                        transition: "background-color 0.3s"
                                    }"""
                                    )
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef"
                                        }"""
                                        )
                                        +university.id
                                    }
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef",
                                            fontWeight: "500"
                                        }"""
                                        )
                                        +university.name
                                    }
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef"
                                        }"""
                                        )
                                        +university.email
                                    }
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef"
                                        }"""
                                        )
                                        ReactHTML.span {
                                            when (university.status) {
                                                UniversityStatus.ACTIVE -> {
                                                    style = js(
                                                        """{ 
                                                        display: "inline-block",
                                                        padding: "6px 12px",
                                                        borderRadius: "50px",
                                                        fontSize: "12px",
                                                        fontWeight: "500",
                                                        backgroundColor: "rgba(40, 167, 69, 0.1)",
                                                        color: "#28a745"
                                                    }"""
                                                    )
                                                }

                                                UniversityStatus.PENDING -> {
                                                    style = js(
                                                        """{ 
                                                        display: "inline-block",
                                                        padding: "6px 12px",
                                                        borderRadius: "50px",
                                                        fontSize: "12px",
                                                        fontWeight: "500",
                                                        backgroundColor: "rgba(255, 193, 7, 0.1)",
                                                        color: "#ffc107"
                                                    }"""
                                                    )
                                                }

                                                UniversityStatus.BLACKLISTED -> {
                                                    style = js(
                                                        """{ 
                                                        display: "inline-block",
                                                        padding: "6px 12px",
                                                        borderRadius: "50px",
                                                        fontSize: "12px",
                                                        fontWeight: "500",
                                                        backgroundColor: "rgba(220, 53, 69, 0.1)",
                                                        color: "#dc3545"
                                                    }"""
                                                    )
                                                }
                                            }
                                            +university.status.name
                                        }
                                    }
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef"
                                        }"""
                                        )
                                        +university.joinDate
                                    }
                                    ReactHTML.td {
                                        style = js(
                                            """{ 
                                            padding: "15px", 
                                            borderBottom: "1px solid #e9ecef"
                                        }"""
                                        )
                                        ReactHTML.button {
                                            className = ClassName("btn action-btn")
                                            style = if (university.status == UniversityStatus.BLACKLISTED) {
                                                js(
                                                    """{ 
                                                    backgroundColor: "#6c757d", 
                                                    color: "white",
                                                    padding: "8px 15px",
                                                    border: "none",
                                                    borderRadius: "6px",
                                                    cursor: "not-allowed",
                                                    fontSize: "13px",
                                                    opacity: "0.65"
                                                }"""
                                                )
                                            } else {
                                                js(
                                                    """{ 
                                                    backgroundColor: "#dc3545", 
                                                    color: "white",
                                                    padding: "8px 15px",
                                                    border: "none",
                                                    borderRadius: "6px",
                                                    cursor: "pointer",
                                                    fontSize: "13px",
                                                    transition: "all 0.3s"
                                                }"""
                                                )
                                            }
                                            disabled =
                                                university.status == UniversityStatus.BLACKLISTED || viewModel.isLoading
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
                            style = js(
                                """{ 
                                textAlign: "center", 
                                marginTop: "25px" 
                            }"""
                            )

                            ReactHTML.button {
                                className = ClassName("btn")
                                style = js(
                                    """{ 
                                    backgroundColor: "transparent",
                                    color: "#0C8B44",
                                    padding: "10px 20px",
                                    border: "1px solid #0C8B44",
                                    borderRadius: "6px",
                                    cursor: "pointer",
                                    fontSize: "14px",
                                    transition: "all 0.3s"
                                }"""
                                )
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
}