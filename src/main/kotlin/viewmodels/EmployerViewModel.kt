package com.veryphy

import com.veryphy.models.*
import kotlinx.coroutines.delay
import viewmodels.ViewModel
import kotlin.properties.Delegates

class EmployerViewModel : ViewModel() {
    var degreeHash: String by Delegates.observable("") { _, _, _ -> }
    var certificateFile: web.file.File? by Delegates.observable(null) { _, _, _ -> }
    var certificateBytes: ByteArray? by Delegates.observable(null) { _, _, _ -> }
    var currentVerification: VerificationRequest? by Delegates.observable(null) { _, _, _ -> }
    var verificationHistory: List<VerificationRequest> by Delegates.observable(emptyList()) { _, _, _ -> }
    var currentPage: Int = 0
    var pageSize: Int = 10
    var hasMoreVerifications: Boolean = false

    fun verifyDegreeByHash(onSuccess: (VerificationRequest) -> Unit, onError: (String) -> Unit) {
        if (degreeHash.isBlank()) {
            onError("Degree hash is required")
            return
        }

        launchWithLoading {
            try {
                val result = apiService.verifyDegreeByHash(degreeHash)
                result.fold(
                    onSuccess = { response ->
                        // Create verification request from response
                        val verification = VerificationRequest(
                            id = response.id,
                            employerId = "current", // This will be replaced with the actual employerId
                            degreeId = response.degreeHash,
                            studentName = response.studentId,
                            universityName = response.universityName,
                            requestDate = response.timestamp,
                            result = if (response.verified) VerificationResult.AUTHENTIC else VerificationResult.FAILED
                        )

                        currentVerification = verification

                        // Update verification history
                        verificationHistory = listOf(verification) + verificationHistory

                        // Clear hash
                        degreeHash = ""

                        onSuccess(verification)
                    },
                    onFailure = { error ->
                        onError(error.message ?: "Failed to verify degree")
                    }
                )
            } catch (e: Exception) {
                console.error("Verify degree error", e)
                onError(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun loadVerificationHistory(employerId: String, resetPage: Boolean = false) {
        if (resetPage) {
            currentPage = 0
        }

        launchWithLoading {
            try {
                val result = apiService.getVerificationHistory(employerId, currentPage, pageSize)
                result.fold(
                    onSuccess = { verifications ->
                        if (currentPage == 0) {
                            verificationHistory = verifications
                        } else {
                            verificationHistory = verificationHistory + verifications
                        }

                        // Check if there might be more verifications to load
                        hasMoreVerifications = verifications.size >= pageSize

                        // Increment page for next load
                        currentPage++
                    },
                    onFailure = { error ->
                        errorMessage = error.message ?: "Failed to load verification history"
                    }
                )
            } catch (e: Exception) {
                console.error("Load verification history error", e)
                errorMessage = e.message ?: "An unexpected error occurred"
            }
        }
    }

    // For demo mode when there's no backend
    fun demoVerifyDegree(onSuccess: (VerificationRequest) -> Unit) {
        launchWithLoading {
            // Simulate network delay
            delay(1500)

            // Create mock verification
            val verification = VerificationRequest(
                id = "ver-" + (2000..2999).random(),
                employerId = "emp-123",
                degreeId = "deg-1001",
                studentName = "John Smith",
                universityName = "University of Technology",
                requestDate = "2024-12-15",
                result = VerificationResult.AUTHENTIC
            )

            currentVerification = verification

            // Update verification history
            verificationHistory = listOf(verification) + verificationHistory

            onSuccess(verification)
        }
    }

    // For demo mode when there's no backend
    fun demoLoadVerificationHistory() {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            verificationHistory = listOf(
                VerificationRequest(
                    id = "ver-2001",
                    employerId = "emp-123",
                    degreeId = "deg-1001",
                    studentName = "John Smith",
                    universityName = "University of Technology",
                    requestDate = "2024-12-10",
                    result = VerificationResult.AUTHENTIC
                ),
                VerificationRequest(
                    id = "ver-2002",
                    employerId = "emp-123",
                    degreeId = "deg-fake",
                    studentName = "Fake Student",
                    universityName = "Diploma Mill University",
                    requestDate = "2024-12-09",
                    result = VerificationResult.FAILED
                ),
                VerificationRequest(
                    id = "ver-2003",
                    employerId = "emp-123",
                    degreeId = "deg-1002",
                    studentName = "Sarah Johnson",
                    universityName = "University of Technology",
                    requestDate = "2024-12-08",
                    result = VerificationResult.PENDING
                )
            )
        }
    }

    fun handleCertificateUpload(inputElement: web.html.HTMLInputElement) {
        val files = inputElement.files
        if (files != null && files.length > 0) {
            certificateFile = files[0]
            console.log("Certificate file selected: ${files[0].name}")
        }
    }
}
