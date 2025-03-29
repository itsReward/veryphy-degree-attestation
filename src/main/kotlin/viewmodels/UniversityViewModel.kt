package com.veryphy.viewmodels

import com.veryphy.models.*
import kotlinx.coroutines.delay
import viewmodels.ViewModel
import kotlin.js.Date
import kotlin.properties.Delegates

class UniversityViewModel : ViewModel() {
    var studentId: String by Delegates.observable("") { _, _, _ -> }
    var studentName: String by Delegates.observable("") { _, _, _ -> }
    var degreeName: String by Delegates.observable("") { _, _, _ -> }
    var issueDate: String by Delegates.observable("") { _, _, _ -> }
    var fileUpload: web.file.File? by Delegates.observable(null) { _, _, _ -> }

    var degrees: List<Degree> by Delegates.observable(emptyList()) { _, _, _ -> }
    var currentPage: Int = 0
    var pageSize: Int = 10
    var hasMoreDegrees: Boolean = false

    fun registerDegree(universityId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (studentId.isBlank() || studentName.isBlank() || degreeName.isBlank()) {
            onError("Student ID, name, and degree name are required")
            return
        }

        launchWithLoading {
            try {
                // Create registration request
                val dateStr = if (issueDate.isBlank()) {
                    // Use current date as ISO string
                    Date().toISOString()
                } else {
                    issueDate
                }

                val request = object : DegreeRegistrationRequest {
                    override val studentId = this@UniversityViewModel.studentId
                    override val studentName = this@UniversityViewModel.studentName
                    override val degreeName = this@UniversityViewModel.degreeName
                    override val universityId = universityId
                    override val issueDate = dateStr
                }

                val result = apiService.registerDegree(request)
                result.fold(
                    onSuccess = { response ->
                        // Create degree from response and add to list
                        val newDegree = Degree(
                            id = response.id,
                            studentId = response.studentId,
                            studentName = response.studentName,
                            degreeName = response.degreeName,
                            universityId = response.universityId,
                            universityName = response.universityName,
                            issueDate = response.issueDate,
                            degreeHash = response.degreeHash,
                            status = DegreeStatus.valueOf(response.status)
                        )

                        // Update degrees list with the new degree
                        degrees = listOf(newDegree) + degrees

                        // Clear form
                        clearForm()

                        onSuccess()
                    },
                    onFailure = { error ->
                        onError(error.message ?: "Failed to register degree")
                    }
                )
            } catch (e: Exception) {
                console.error("Register degree error", e)
                onError(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun loadDegrees(universityId: String, resetPage: Boolean = false) {
        if (resetPage) {
            currentPage = 0
        }

        launchWithLoading {
            try {
                val result = apiService.getUniversityDegrees(universityId, currentPage, pageSize)
                result.fold(
                    onSuccess = { loadedDegrees ->
                        if (currentPage == 0) {
                            degrees = loadedDegrees
                        } else {
                            degrees = degrees + loadedDegrees
                        }

                        // Check if there might be more degrees to load
                        hasMoreDegrees = loadedDegrees.size >= pageSize

                        // Increment page for next load
                        currentPage++
                    },
                    onFailure = { error ->
                        errorMessage = error.message ?: "Failed to load degrees"
                    }
                )
            } catch (e: Exception) {
                console.error("Load degrees error", e)
                errorMessage = e.message ?: "An unexpected error occurred"
            }
        }
    }

    // For demo mode, when there's no backend
    fun demoRegisterDegree(universityId: String, universityName: String, onSuccess: () -> Unit) {
        launchWithLoading {
            // Simulate network delay
            delay(1000)

            // Create mock degree
            val degree = Degree(
                id = "deg-" + (1000..9999).random(),
                studentId = studentId,
                studentName = studentName,
                degreeName = degreeName,
                universityId = universityId,
                universityName = universityName,
                issueDate = issueDate.ifEmpty { "2024-12-15" },
                degreeHash = "0x" + (100000..999999).random().toString(16),
                status = DegreeStatus.REGISTERED
            )

            // Update degrees list by adding the new degree
            degrees = listOf(degree) + degrees

            // Clear form
            clearForm()

            // Call success callback
            onSuccess()
        }
    }

    // For demo mode, when there's no backend
    fun demoLoadDegrees() {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            degrees = listOf(
                Degree(
                    id = "deg-1001",
                    studentId = "ST10023",
                    studentName = "John Smith",
                    degreeName = "Bachelor of Computer Science",
                    universityId = "uni-001",
                    universityName = "University of Technology",
                    issueDate = "2024-12-01",
                    degreeHash = "0x1a2b3c4d5e6f",
                    status = DegreeStatus.VERIFIED
                ),
                Degree(
                    id = "deg-1002",
                    studentId = "ST10045",
                    studentName = "Sarah Johnson",
                    degreeName = "Master of Business Administration",
                    universityId = "uni-001",
                    universityName = "University of Technology",
                    issueDate = "2024-11-15",
                    degreeHash = "0x7e8f9g0h1i2j",
                    status = DegreeStatus.PROCESSING
                ),
                Degree(
                    id = "deg-1003",
                    studentId = "ST10067",
                    studentName = "Michael Brown",
                    degreeName = "Bachelor of Engineering",
                    universityId = "uni-001",
                    universityName = "University of Technology",
                    issueDate = "2024-12-05",
                    degreeHash = "0x3k4l5m6n7o8p",
                    status = DegreeStatus.REGISTERED
                )
            )
        }
    }

    fun handleFileUpload(inputElement: web.html.HTMLInputElement) {
        val files = inputElement.files
        if (files != null && files.length > 0) {
            fileUpload = files[0]
        }
    }

    private fun clearForm() {
        studentId = ""
        studentName = ""
        degreeName = ""
        issueDate = ""
        fileUpload = null
    }
}