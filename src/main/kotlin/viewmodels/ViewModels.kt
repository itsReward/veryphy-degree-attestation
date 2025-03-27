package com.veryphy

import com.veryphy.models.*
import kotlinx.coroutines.delay
import viewmodels.ViewModel
import viewmodels.ViewModelWrapper
import kotlin.properties.Delegates

// Modified LoginViewModel with demo login functionality
class LoginViewModel : ViewModel() {
    var username: String by Delegates.observable("") { _, _, _ -> }
    var password: String by Delegates.observable("") { _, _, _ -> }
    var user: User? by Delegates.observable(null) { _, _, _ -> }

    // For demo purposes, this always succeeds
    fun login(role: UserRole, onSuccess: () -> Unit) {
        launchWithLoading {
            // Simulate network delay
            delay(500)

            // Use mock user data
            user = ViewModelWrapper.getMockUser(role)

            // Call success callback
            onSuccess()
        }
    }

    fun logout() {
        user = null
        username = ""
        password = ""
    }
}

// Modified UniversityViewModel to load mock data
class UniversityViewModel : ViewModel() {
    var studentId: String by Delegates.observable("") { _, _, _ -> }
    var studentName: String by Delegates.observable("") { _, _, _ -> }
    var degreeName: String by Delegates.observable("") { _, _, _ -> }
    var issueDate: String by Delegates.observable("") { _, _, _ -> }
    var fileUpload: web.file.File? by Delegates.observable(null) { _, _, _ -> }

    var degrees: List<Degree> by Delegates.observable(emptyList()) { _, _, _ -> }

    fun registerDegree(universityId: String, universityName: String, onSuccess: () -> Unit) {
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
            degrees = degrees + degree

            // Clear form
            clearForm()

            // Call success callback
            onSuccess()
        }
    }

    fun loadDegrees(universityId: String) {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            degrees = ViewModelWrapper.getMockDegrees()
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

// Modified EmployerViewModel to load mock data
class EmployerViewModel : ViewModel() {
    var certificateFile: web.file.File? by Delegates.observable(null) { _, _, _ -> }
    var certificateBytes: ByteArray? by Delegates.observable(null) { _, _, _ -> }
    var currentVerification: VerificationRequest? by Delegates.observable(null) { _, _, _ -> }
    var verificationHistory: List<VerificationRequest> by Delegates.observable(emptyList()) { _, _, _ -> }

    fun verifyDegree(onSuccess: (VerificationRequest) -> Unit) {
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

    fun loadVerificationHistory(employerId: String) {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            verificationHistory = ViewModelWrapper.getMockVerifications()
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

// Modified AdminViewModel to load mock data
class AdminViewModel : ViewModel() {
    var systemStats: SystemStats? by Delegates.observable(null) { _, _, _ -> }
    var universities: List<University> by Delegates.observable(emptyList()) { _, _, _ -> }

    // New university form
    var universityName: String by Delegates.observable("") { _, _, _ -> }
    var universityEmail: String by Delegates.observable("") { _, _, _ -> }
    var stakeAmount: String by Delegates.observable("") { _, _, _ -> }

    fun loadSystemStats() {
        launchWithLoading {
            // Simulate network delay
            delay(800)

            // Load mock data
            systemStats = ViewModelWrapper.getMockSystemStats()
        }
    }

    fun loadUniversities() {
        launchWithLoading {
            // Simulate network delay
            delay(1000)

            // Load mock data
            universities = ViewModelWrapper.getMockUniversities()
        }
    }

    fun addUniversity(onSuccess: () -> Unit) {
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
                address = "Address pending",
                stakeAmount = stakeAmount.toDoubleOrNull() ?: 5000.0,
                status = UniversityStatus.PENDING,
                joinDate = "2024-12-15"
            )

            // Update universities list
            universities = universities + university

            // Clear form
            clearForm()

            // Call success callback
            onSuccess()
        }
    }

    fun blacklistUniversity(universityId: String, onSuccess: () -> Unit) {
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
        stakeAmount = ""
    }
}