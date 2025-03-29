package com.veryphy.viewmodels

import com.veryphy.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viewmodels.ViewModel
import kotlin.properties.Delegates

class AdminViewModel : ViewModel() {
    var systemStats: SystemStats? by Delegates.observable(null) { _, _, _ -> }
    var universities: List<University> by Delegates.observable(emptyList()) { _, _, _ -> }
    var currentPage: Int = 0
    var pageSize: Int = 10
    var hasMoreUniversities: Boolean = false

    // New university form
    var universityName: String by Delegates.observable("") { _, _, _ -> }
    var universityEmail: String by Delegates.observable("") { _, _, _ -> }
    var universityAddress: String by Delegates.observable("") { _, _, _ -> }
    var stakeAmount: String by Delegates.observable("5000") { _, _, _ -> }

    fun loadSystemStats() {
        launchWithLoading {
            try {
                val result = apiService.getSystemStats()
                result.fold(
                    onSuccess = { stats ->
                        systemStats = stats
                    },
                    onFailure = { error ->
                        errorMessage = error.message ?: "Failed to load system statistics"
                    }
                )
            } catch (e: Exception) {
                console.error("Load system stats error", e)
                errorMessage = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun loadUniversities(resetPage: Boolean = false) {
        if (resetPage) {
            currentPage = 0
        }

        launchWithLoading {
            try {
                val result = apiService.getUniversities(currentPage, pageSize)
                result.fold(
                    onSuccess = { loadedUniversities ->
                        if (currentPage == 0) {
                            universities = loadedUniversities
                        } else {
                            universities = universities + loadedUniversities
                        }

                        // Check if there might be more universities to load
                        hasMoreUniversities = loadedUniversities.size >= pageSize

                        // Increment page for next load
                        currentPage++
                    },
                    onFailure = { error ->
                        errorMessage = error.message ?: "Failed to load universities"
                    }
                )
            } catch (e: Exception) {
                console.error("Load universities error", e)
                errorMessage = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun addUniversity(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (universityName.isBlank() || universityEmail.isBlank()) {
            onError("University name and email are required")
            return
        }

        launchWithLoading {
            try {
                val request = object : UniversityRegistrationRequest {
                    override val name = universityName
                    override val email = universityEmail
                    override val address = universityAddress.ifBlank { null }
                    override val stakeAmount =  5000.0
                }

                val result = apiService.registerUniversity(request)
                result.fold(
                    onSuccess = { response ->
                        // Create university from response
                        val newUniversity = University(
                            id = response.id,
                            name = response.name,
                            email = response.email,
                            address = response.address ?: "",
                            stakeAmount = response.stakeAmount.toDouble(),
                            status = UniversityStatus.valueOf(response.status),
                            joinDate = response.joinDate
                        )

                        // Update universities list
                        universities = listOf(newUniversity) + universities

                        // Clear form
                        clearForm()

                        onSuccess()
                    },
                    onFailure = { error ->
                        onError(error.message ?: "Failed to add university")
                    }
                )
            } catch (e: Exception) {
                console.error("Add university error", e)
                onError(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun blacklistUniversity(universityId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        launchWithLoading {
            try {
                val result = apiService.blacklistUniversity(universityId, "Administrative action")
                result.fold(
                    onSuccess = { response ->
                        // Update university in list
                        universities = universities.map {
                            if (it.id == universityId) {
                                it.copy(status = UniversityStatus.valueOf(response.status))
                            } else {
                                it
                            }
                        }

                        onSuccess()
                    },
                    onFailure = { error ->
                        onError(error.message ?: "Failed to blacklist university")
                    }
                )
            } catch (e: Exception) {
                console.error("Blacklist university error", e)
                onError(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // For demo mode when there's no backend
    fun demoLoadSystemStats() {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            systemStats = SystemStats(
                registeredUniversities = 42,
                totalDegrees = 12567,
                verificationCount = 128,
                successRate = 99.8
            )
        }
    }

    // For demo mode when there's no backend
    fun demoLoadUniversities() {
        launchWithLoading {
            // Simulate network delay
            delay(1000)

            // Load mock data
            universities = listOf(
                University(
                    id = "uni-001",
                    name = "University of Technology",
                    email = "admin@utech.edu",
                    address = "123 Tech Avenue, Innovation City",
                    stakeAmount = 5000.0,
                    status = UniversityStatus.ACTIVE,
                    joinDate = "2023-05-15"
                ),
                University(
                    id = "uni-002",
                    name = "State University",
                    email = "admin@stateuni.edu",
                    address = "456 Knowledge Road, Learning Town",
                    stakeAmount = 5000.0,
                    status = UniversityStatus.ACTIVE,
                    joinDate = "2023-06-21"
                ),
                University(
                    id = "uni-003",
                    name = "Central College",
                    email = "admin@central.edu",
                    address = "789 Education Street, Academy City",
                    stakeAmount = 5000.0,
                    status = UniversityStatus.PENDING,
                    joinDate = "2024-11-30"
                )
            )
        }
    }

    // For demo mode when there's no backend
    fun demoAddUniversity(onSuccess: () -> Unit) {
        if (universityName.isBlank() || universityEmail.isBlank()) {
            errorMessage = "University name and email are required"
            return
        }

        launchWithLoading {
            // Simulate network delay
            delay(1200)

            // Create new university
            val university = University(
                id = "uni-" + (100..999).random(),
                name = universityName,
                email = universityEmail,
                address = universityAddress.ifBlank { "Address pending" },
                stakeAmount = stakeAmount.toDoubleOrNull() ?: 5000.0,
                status = UniversityStatus.PENDING,
                joinDate = "2024-12-15"
            )

            // Update universities list
            universities = listOf(university) + universities

            // Clear form
            clearForm()

            // Call success callback
            onSuccess()
        }
    }

    // For demo mode when there's no backend
    fun demoBlacklistUniversity(universityId: String, onSuccess: () -> Unit) {
        launchWithLoading {
            // Simulate network delay
            delay(1000)

            // Update university status in list
            universities = universities.map {
                if (it.id == universityId) {
                    it.copy(status = UniversityStatus.BLACKLISTED)
                } else {
                    it
                }
            }

            // Call success callback
            onSuccess()
        }
    }

    private fun clearForm() {
        universityName = ""
        universityEmail = ""
        universityAddress = ""
        stakeAmount = "5000"
    }
}