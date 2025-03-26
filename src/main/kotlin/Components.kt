package com.veryphy

import web.html.HTMLInputElement
import react.FC
import react.Props
import react.ReactNode
import react.dom.events.ChangeEvent
import react.dom.events.FormEvent
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul
import web.cssom.ClassName
import web.html.InputType

// Common Components
interface ButtonProps : Props {
    var onClick: () -> Unit
    var disabled: Boolean?
    var className: String?
    var children: ReactNode
}

val Button = FC<ButtonProps> { props ->
    button {
        onClick = { props.onClick() }
        disabled = props.disabled ?: false
        className = ClassName(props.className ?: "btn")
        +props.children
    }
}

interface CardProps : Props {
    var title: String?
    var className: String?
    var children: ReactNode
}

val Card = FC<CardProps> { props ->
    div {
        className = ClassName("card ${props.className ?: ""}")
        props.title?.let {
            h3 {
                className = ClassName("card-title")
                +it
            }
        }
        div {
            className = ClassName("card-content")
            +props.children
        }
    }
}

interface StatCardProps : Props {
    var title: String
    var value: String
}

val StatCard = FC<StatCardProps> { props ->
    Card {
        className = "stat-card"
        title = props.title
        div {
            className = ClassName("stat-value")
            +props.value
        }
    }
}

interface InputFieldProps : Props {
    var id: String
    var label: String
    var type: String
    var value: String
    var onChange: (String) -> Unit
    var required: Boolean?
    var placeholder: String?
}

val InputField = FC<InputFieldProps> { props ->
    div {
        className = ClassName("form-group")
        label {
            htmlFor = props.id
            +props.label
        }
        input {
            id = props.id
            //type = InputType(props.type)
            value = props.value
            onChange = { e: ChangeEvent<web.html.HTMLInputElement> ->
                props.onChange(e.target.value)
            }
            required = props.required ?: false
            placeholder = props.placeholder ?: ""
            className = ClassName("form-control")
        }
    }
}

interface FileInputProps : Props {
    var id: String
    var label: String
    var onChange: (HTMLInputElement) -> Unit
    var accept: String?
}

// Modify your FileInput component to use the correct type path
val FileInput = FC<FileInputProps> { props ->
    div {
        className = ClassName("form-group")
        label {
            htmlFor = props.id
            +props.label
        }
        input {
            id = props.id
            //type = InputType("file")
            // Use the fully qualified path for the event type
            onChange = { e: ChangeEvent<HTMLInputElement> ->
                props.onChange(e.target)
            }
            accept = props.accept ?: ""
            className = ClassName("form-control")
        }
    }
}

interface AlertProps : Props {
    var type: String
    var message: String
}

val Alert = FC<AlertProps> { props ->
    div {
        className = ClassName("alert alert-${props.type}")
        +props.message
    }
}

// Layout Components
interface DashboardLayoutProps : Props {
    var title: String
    var children: ReactNode
    var onLogout: () -> Unit
    var username: String
    var role: UserRole
}

val DashboardLayout = FC<DashboardLayoutProps> { props ->
    div {
        className = ClassName("dashboard-container")
        header {
            className = ClassName("header")
            h1 {
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
                className = ClassName("sidebar")
                h3 {
                    +"Navigation"
                }
                Sidebar {
                    role = props.role
                }
            }
            div {
                className = ClassName("main-content")
                +props.children
            }
        }
    }
}

interface SidebarProps : Props {
    var role: UserRole
}

val Sidebar = FC<SidebarProps> { props ->
    ul {
        className = ClassName("nav-menu")
        li {
            a {
                href = "#"
                +"Dashboard"
            }
        }

        when (props.role) {
            UserRole.UNIVERSITY -> {
                li {
                    a {
                        href = "#"
                        +"Register Degree"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"View Degrees"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Account"
                    }
                }
            }

            UserRole.EMPLOYER -> {
                li {
                    a {
                        href = "#"
                        +"Verify Degree"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Verification History"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Account"
                    }
                }
            }

            UserRole.ADMIN -> {
                li {
                    a {
                        href = "#"
                        +"Universities"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Employers"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"System Stats"
                    }
                }
                li {
                    a {
                        href = "#"
                        +"Settings"
                    }
                }
            }
        }
    }
}

// Table Components
interface DataTableProps<T> : Props {
    var data: List<T>
    var columns: List<Column<T>>
    var emptyMessage: String?
}

data class Column<T>(
    val header: String,
    val render: (T) -> ReactNode
)

fun <T> DataTable(props: DataTableProps<T>) {/*
    table {
        className = ClassName("data-table")
        thead {
            tr {
                props.columns.forEach { column ->
                    th {
                        +column.header
                    }
                }
            }
        }
        tbody {
            if (props.data.isEmpty()) {
                tr {
                    td {
                        colSpan = props.columns.size
                        className = ClassName("text-center")
                        +(props.emptyMessage ?: "No data available")
                    }
                }
            } else {
                props.data.forEach { item ->
                    tr {
                        props.columns.forEach { column ->
                            td {
                                +column.render(item)
                            }
                        }
                    }
                }
            }
        }
    }*/
}

// Form Components
interface FormProps : Props {
    var onSubmit: (FormEvent<*>) -> Unit
    var children: ReactNode
}

val Form = FC<FormProps> { props ->
    div {
        className = ClassName("form")
        onSubmit = { e ->
            e.preventDefault()
            props.onSubmit(e)
        }
        +props.children
    }
}