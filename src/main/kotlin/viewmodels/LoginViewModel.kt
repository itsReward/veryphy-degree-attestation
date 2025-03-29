package com.veryphy.viewmodels

import com.veryphy.models.*
import com.veryphy.services.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viewmodels.ViewModel
import kotlin.properties.Delegates

class LoginViewModel : ViewModel() {
    var username: String by Delegates.observable("") { _, _, _ -> }
    var password: String by Delegates.observable("") { _, _, _ -> }
    var user: User? by Delegates.observable(null) { _, _, _ -> }

    fun login(role: UserRole, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            onError("Username and password are required")
            return
        }

        launchWithLoading {
            try {
                val result = apiService.login(username, password, role)
                result.fold(
                    onSuccess = { response ->
                        // Create user from response
                        user = User(
                            id = response.user.id,
                            name = when (role) {
                                UserRole.UNIVERSITY -> response.user.username // Use a readable name
                                UserRole.EMPLOYER -> response.user.username
                                UserRole.ADMIN -> response.user.username
                            },
                            email = response.user.email,
                            role = role // Use the role from the request
                        )
                        onSuccess()
                    },
                    onFailure = { error ->
                        onError(error.message ?: "Login failed")
                    }
                )
            } catch (e: Exception) {
                console.error("Login error", e)
                onError(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // For demo mode, we'll keep this method to allow quick testing without a backend
    fun demoLogin(role: UserRole, onSuccess: () -> Unit) {
        launchWithLoading {
            // Simulate network delay
            delay(500)

            // Create mock user
            user = User(
                id = when (role) {
                    UserRole.UNIVERSITY -> "uni-001"
                    UserRole.EMPLOYER -> "emp-123"
                    UserRole.ADMIN -> "admin-001"
                },
                name = when (role) {
                    UserRole.UNIVERSITY -> "University of Technology"
                    UserRole.EMPLOYER -> "Tech Innovations Inc."
                    UserRole.ADMIN -> "System Administrator"
                },
                email = "user@example.com",
                role = role
            )

            // Call success callback
            onSuccess()
        }
    }

    fun logout() {
        user = null
        username = ""
        password = ""
        apiService.clearAuthToken()
    }
}