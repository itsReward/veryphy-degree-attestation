package com.veryphy

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.br
import react.dom.events.ChangeEvent
import react.dom.events.FormEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import web.cssom.ClassName

// Interface for AppState props
interface AppProps : Props {
    var appState: AppState
}


// Extended LoginProps with callback
interface LoginProps : Props {
    var onLogin: (UserRole) -> Unit
    var appState: AppState
}

// Updated LoginScreen with proper props
val LoginScreen = FC<LoginProps> { props ->
    val viewModel = props.appState.loginViewModel

    div {
        className = ClassName("login-container")
        h1 {
            +"Degree Attestation System"
        }
        p {
            +"Secure verification using blockchain and AI"
        }

        div {
            className = ClassName("login-card")
            h2 {
                +"Login"
            }

            if (viewModel.isLoading) {
                div {
                    className = ClassName("loading-indicator")
                    +"Logging in..."
                }
            }

            viewModel.errorMessage?.let {
                div {
                    className = ClassName("error-message")
                    +it
                }
            }

            form {
                onSubmit = { e: FormEvent<*> ->
                    e.preventDefault()
                }
                className = ClassName("login-form")

                div {
                    className = ClassName("form-group")
                    label {
                        htmlFor = "username"
                        +"Username"
                    }
                    input {
                        id = "username"
                        asDynamic().type = "text"
                        className = ClassName("form-control")
                        value = viewModel.username
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.username = e.target.value
                        }
                    }
                }

                div {
                    className = ClassName("form-group")
                    label {
                        htmlFor = "password"
                        +"Password"
                    }
                    input {
                        id = "password"
                        asDynamic().type = "password"
                        className = ClassName("form-control")
                        value = viewModel.password
                        onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                            viewModel.password = e.target.value
                        }
                    }
                }

                div {
                    className = ClassName("role-selection")
                    +"Login as: "

                    button {
                        className = ClassName("btn role-btn")
                        onClick = { _ ->
                            MainScope().launch {
                                viewModel.login(UserRole.UNIVERSITY) {
                                    props.onLogin(UserRole.UNIVERSITY)
                                    // Initialize university dashboard data
                                    props.appState.universityViewModel.loadDegrees("uni-001")
                                }
                            }
                        }
                        disabled = viewModel.isLoading
                        +"University"
                    }

                    button {
                        className = ClassName("btn role-btn")
                        onClick = { _ ->
                            MainScope().launch {
                                viewModel.login(UserRole.EMPLOYER) {
                                    props.onLogin(UserRole.EMPLOYER)
                                    // Initialize employer dashboard data
                                    props.appState.employerViewModel.loadVerificationHistory("emp-123")
                                }
                            }
                        }
                        disabled = viewModel.isLoading
                        +"Employer"
                    }

                    button {
                        className = ClassName("btn role-btn")
                        onClick = { _ ->
                            MainScope().launch {
                                viewModel.login(UserRole.ADMIN) {
                                    props.onLogin(UserRole.ADMIN)
                                    // Initialize admin dashboard data
                                    props.appState.adminViewModel.loadSystemStats()
                                    props.appState.adminViewModel.loadUniversities()
                                }
                            }
                        }
                        disabled = viewModel.isLoading
                        +"Admin"
                    }
                }
            }
        }
    }
}


// Dashboard component props with logout callback
interface DashboardProps : AppProps {
    var onLogout: () -> Unit
}

// Updated DashboardLayout props with callback
interface UpdatedDashboardLayoutProps : Props {
    var title: String
    var children: react.ReactNode
    var onLogout: () -> Unit
    var username: String
    var role: UserRole
}

// University Dashboard Screen
val UniversityDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.universityViewModel

    react.dom.html.ReactHTML.div {
        className = ClassName("dashboard-container")

        DashboardLayout {
            title = "University Dashboard"
            username = props.appState.loginViewModel.user?.name ?: "User"
            role = UserRole.UNIVERSITY
            onLogout = props.onLogout

            div {
                className = ClassName("dashboard-content")

                viewModel.errorMessage?.let {
                    div {
                        className = ClassName("error-message")
                        +it
                    }
                }

                h2 {
                    +"Register New Degree"
                }

                form {
                    className = ClassName("form-group")
                    onSubmit = { e: FormEvent<*> ->
                        e.preventDefault()
                        MainScope().launch {
                            viewModel.registerDegree(
                                universityId = "uni-001", // In a real app, get from logged-in user
                                universityName = "University of Technology",
                                onSuccess = {
                                    // Show success notification
                                }
                            )
                        }
                    }

                    InputField {
                        id = "studentId"
                        label = "Student ID"
                        type = "text"
                        value = viewModel.studentId
                        onChange = { viewModel.studentId = it }
                        required = true
                    }

                    InputField {
                        id = "studentName"
                        label = "Student Name"
                        type = "text"
                        value = viewModel.studentName
                        onChange = { viewModel.studentName = it }
                        required = true
                    }

                    InputField {
                        id = "degreeName"
                        label = "Degree Name"
                        type = "text"
                        value = viewModel.degreeName
                        onChange = { viewModel.degreeName = it }
                        required = true
                    }

                    InputField {
                        id = "issueDate"
                        label = "Issue Date"
                        type = "date"
                        value = viewModel.issueDate
                        onChange = { viewModel.issueDate = it }
                        required = true
                    }

                    FileInput {
                        id = "degreeFile"
                        label = "Upload Transcript (PDF)"
                        onChange = { viewModel.handleFileUpload(it) }
                        accept = ".pdf"
                    }

                    button {
                        className = ClassName("btn primary-btn")
                        asDynamic().type = "submit"
                        disabled = viewModel.isLoading
                        if (viewModel.isLoading) {
                            +"Processing..."
                        } else {
                            +"Register Degree"
                        }
                    }
                }

                h2 {
                    +"Recent Registrations"
                }

                if (viewModel.isLoading && viewModel.degrees.isEmpty()) {
                    div {
                        className = ClassName("loading-indicator")
                        +"Loading degrees..."
                    }
                } else {
                    table {
                        className = ClassName("data-table")
                        thead {
                            tr {
                                th { +"Student ID" }
                                th { +"Name" }
                                th { +"Degree" }
                                th { +"Issue Date" }
                                th { +"Status" }
                            }
                        }
                        tbody {
                            if (viewModel.degrees.isEmpty()) {
                                tr {
                                    td {
                                        colSpan = 5
                                        asDynamic().style.textAlign = "center"
                                        +"No degrees registered yet"
                                    }
                                }
                            } else {
                                viewModel.degrees.forEach { degree ->
                                    tr {
                                        td { +degree.studentId }
                                        td { +degree.studentName }
                                        td { +degree.degreeName }
                                        td { +degree.issueDate }
                                        td { +degree.status.name }
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

// Employer Dashboard Screen
val EmployerDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.employerViewModel

    div {
        className = ClassName("dashboard-container")

        DashboardLayout {
            title = "Employer Dashboard"
            username = props.appState.loginViewModel.user?.name ?: "User"
            role = UserRole.EMPLOYER
            onLogout = props.onLogout

            div {
                className = ClassName("dashboard-content")

                viewModel.errorMessage?.let {
                    div {
                        className = ClassName("error-message")
                        +it
                    }
                }

                h2 {
                    +"Verify Degree Certificate"
                }

                form {
                    className = ClassName("form-group")
                    onSubmit = { e: FormEvent<*> ->
                        e.preventDefault()
                        MainScope().launch {
                            viewModel.verifyDegree { verification ->
                                // Show success notification or handle results
                            }
                        }
                    }

                    FileInput {
                        id = "certificateUpload"
                        label = "Upload Certificate Image"
                        onChange = { viewModel.handleCertificateUpload(it) }
                        accept = ".jpg,.jpeg,.png,.pdf"
                    }

                    viewModel.certificateFile?.let {
                        div {
                            className = ClassName("file-info")
                            +"File selected: ${it.name}"
                        }
                    }

                    button {
                        className = ClassName("btn primary-btn")
                        //type = "submit"
                        disabled = viewModel.isLoading || viewModel.certificateFile == null
                        if (viewModel.isLoading) {
                            +"Verifying..."
                        } else {
                            +"Verify Certificate"
                        }
                    }
                }

                // Show current verification result if available
                viewModel.currentVerification?.let { verification ->
                    div {
                        className = ClassName(if (verification.result == VerificationResult.AUTHENTIC)
                            "success-message" else "error-message")
                        h2 {
                            +"Verification Result"
                        }
                        p {
                            +"Student: ${verification.studentName}"
                            br {}
                            +"University: ${verification.universityName}"
                            br {}
                            +"Status: ${verification.result.name}"
                        }
                    }
                }

                h2 {
                    +"Recent Verifications"
                }

                if (viewModel.isLoading && viewModel.verificationHistory.isEmpty()) {
                    div {
                        className = ClassName("loading-indicator")
                        +"Loading verification history..."
                    }
                } else {
                    table {
                        className = ClassName("data-table")
                        thead {
                            tr {
                                th { +"Certificate ID" }
                                th { +"Student Name" }
                                th { +"University" }
                                th { +"Date Verified" }
                                th { +"Status" }
                            }
                        }
                        tbody {
                            if (viewModel.verificationHistory.isEmpty()) {
                                tr {
                                    td {
                                        colSpan = 5
                                        asDynamic().style.textAlign = "center"
                                        +"No verification history yet"
                                    }
                                }
                            } else {
                                viewModel.verificationHistory.forEach { verification ->
                                    tr {
                                        td { +verification.degreeId }
                                        td { +verification.studentName }
                                        td { +verification.universityName }
                                        td { +verification.requestDate }
                                        td {
                                            span {
                                                className = ClassName(when(verification.result) {
                                                    VerificationResult.AUTHENTIC -> "status-success"
                                                    VerificationResult.FAILED -> "status-error"
                                                    VerificationResult.PENDING -> "status-pending"
                                                }
                                                +verification.result.name)
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
}

// Admin Dashboard Screen
val AdminDashboard = FC<DashboardProps> { props ->
    val viewModel = props.appState.adminViewModel

    div {
        className = ClassName("dashboard-container")

        DashboardLayout {
            title = "Admin Dashboard"
            username = props.appState.loginViewModel.user?.name ?: "User"
            role = UserRole.ADMIN
            onLogout = props.onLogout

            div {
                className = ClassName("dashboard-content")

                viewModel.errorMessage?.let {
                    div {
                        className =ClassName("error-message")
                        +it
                    }
                }

                h2 {
                    +"System Statistics"
                }

                if (viewModel.isLoading && viewModel.systemStats == null) {
                    div {
                        className = ClassName("loading-indicator")
                        +"Loading statistics..."
                    }
                } else {
                    div {
                        className = ClassName("stats-container")
                        val stats = viewModel.systemStats

                        StatCard {
                            title = "Registered Universities"
                            value = stats?.registeredUniversities?.toString() ?: "0"
                        }

                        StatCard {
                            title = "Total Degrees"
                            value = stats?.totalDegrees?.toString() ?: "0"
                        }

                        StatCard {
                            title = "Verifications Today"
                            value = stats?.verificationCount?.toString() ?: "0"
                        }

                        StatCard {
                            title = "Success Rate"
                            value = "${stats?.successRate ?: 0}%"
                        }
                    }
                }

                h2 {
                    +"Manage Universities"
                }

                form {
                    className = ClassName("form-group")
                    onSubmit = { e: FormEvent<*> ->
                        e.preventDefault()
                        MainScope().launch {
                            viewModel.addUniversity {
                                // Show success notification
                            }
                        }
                    }

                    InputField {
                        id = "universityName"
                        label = "University Name"
                        type = "text"
                        value = viewModel.universityName
                        onChange = { viewModel.universityName = it }
                        required = true
                    }

                    InputField {
                        id = "universityEmail"
                        label = "Email"
                        type = "email"
                        value = viewModel.universityEmail
                        onChange = { viewModel.universityEmail = it }
                        required = true
                    }

                    InputField {
                        id = "stakeAmount"
                        label = "Stake Amount"
                        type = "number"
                        value = viewModel.stakeAmount
                        onChange = { viewModel.stakeAmount = it }
                        required = true
                    }

                    button {
                        className = ClassName("btn primary-btn")
                        asDynamic().type = "submit"
                        disabled = viewModel.isLoading
                        if (viewModel.isLoading) {
                            +"Adding..."
                        } else {
                            +"Add University"
                        }
                    }
                }

                h2 {
                    +"Registered Universities"
                }

                if (viewModel.isLoading && viewModel.universities.isEmpty()) {
                    div {
                        className = ClassName("loading-indicator")
                        +"Loading universities..."
                    }
                } else {
                    table {
                        className = ClassName("data-table")
                        thead {
                            tr {
                                th { +"ID" }
                                th { +"University" }
                                th { +"Status" }
                                th { +"Staked Amount" }
                                th { +"Actions" }
                            }
                        }
                        tbody {
                            if (viewModel.universities.isEmpty()) {
                                tr {
                                    td {
                                        colSpan = 5
                                        asDynamic().style.textAlign = "center"
                                        +"No universities registered yet"
                                    }
                                }
                            } else {
                                viewModel.universities.forEach { university ->
                                    tr {
                                        td { +university.id }
                                        td { +university.name }
                                        td {
                                            span {
                                                className = ClassName(when(university.status) {
                                                    UniversityStatus.ACTIVE -> "status-success"
                                                    UniversityStatus.PENDING -> "status-pending"
                                                    UniversityStatus.BLACKLISTED -> "status-error"
                                                }
                                                +university.status.name)
                                            }
                                        }
                                        td { +"${university.stakeAmount}" }
                                        td {
                                            if (university.status != UniversityStatus.BLACKLISTED) {
                                                button {
                                                    className = ClassName("btn action-btn")
                                                    onClick = { _ ->
                                                        MainScope().launch {
                                                            viewModel.blacklistUniversity(university.id) {
                                                                // Show success notification
                                                            }
                                                        }
                                                    }
                                                    disabled = viewModel.isLoading
                                                    +"Blacklist"
                                                }
                                            } else {
                                                +"N/A"
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
}

