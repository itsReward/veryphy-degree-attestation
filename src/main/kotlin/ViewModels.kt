package com.veryphy

import js.buffer.ArrayBuffer
import js.typedarrays.Int8Array
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

// Base ViewModel class with coroutine support
abstract class ViewModel : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    protected val apiService = ApiService()

    var isLoading: Boolean by Delegates.observable(false) { _, _, _ -> }
    var errorMessage: String? by Delegates.observable(null) { _, _, _ -> }

    protected fun launchWithLoading(block: suspend () -> Unit) {
        launch {
            try {
                isLoading = true
                errorMessage = null
                block()
            } catch (e: Exception) {
                errorMessage = e.message ?: "An unknown error occurred"
                console.error(e)
            } finally {
                isLoading = false
            }
        }
    }
}

class LoginViewModel : ViewModel() {
    var username: String by Delegates.observable("") { _, _, _ -> }
    var password: String by Delegates.observable("") { _, _, _ -> }
    var user: User? by Delegates.observable(null) { _, _, _ -> }

    fun login(role: UserRole, onSuccess: () -> Unit) {
        launchWithLoading {
            apiService.login(username, password, role)
                .onSuccess {
                    user = it
                    onSuccess()
                }
                .onFailure {
                    errorMessage = "Login failed: ${it.message}"
                }
        }
    }

    fun logout() {
        user = null
        username = ""
        password = ""
    }
}

class UniversityViewModel : ViewModel() {
    var studentId: String by Delegates.observable("") { _, _, _ -> }
    var studentName: String by Delegates.observable("") { _, _, _ -> }
    var degreeName: String by Delegates.observable("") { _, _, _ -> }
    var issueDate: String by Delegates.observable("") { _, _, _ -> }
    var fileUpload: web.file.File? by Delegates.observable(null) { _, _, _ -> }

    var degrees: List<Degree> by Delegates.observable(emptyList()) { _, _, _ -> }

    fun registerDegree(universityId: String, universityName: String, onSuccess: () -> Unit) {
        launchWithLoading {
            val degree = Degree(
                id = "",  // Will be assigned by the service
                studentId = studentId,
                studentName = studentName,
                degreeName = degreeName,
                universityId = universityId,
                universityName = universityName,
                issueDate = issueDate,
                degreeHash = "",  // Will be generated
                status = DegreeStatus.PROCESSING
            )

            apiService.registerDegree(degree)
                .onSuccess {
                    clearForm()
                    loadDegrees(universityId)
                    onSuccess()
                }
                .onFailure {
                    errorMessage = "Failed to register degree: ${it.message}"
                }
        }
    }

    fun loadDegrees(universityId: String) {
        launchWithLoading {
            apiService.getUniversityDegrees(universityId)
                .onSuccess {
                    degrees = it
                }
                .onFailure {
                    errorMessage = "Failed to load degrees: ${it.message}"
                }
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

class EmployerViewModel : ViewModel() {
    var certificateFile: web.file.File? by Delegates.observable(null) { _, _, _ -> }
    var certificateBytes: ByteArray? by Delegates.observable(null) { _, _, _ -> }
    var currentVerification: VerificationRequest? by Delegates.observable(null) { _, _, _ -> }
    var verificationHistory: List<VerificationRequest> by Delegates.observable(emptyList()) { _, _, _ -> }

    fun verifyDegree(onSuccess: (VerificationRequest) -> Unit) {
        certificateBytes?.let { bytes ->
            launchWithLoading {
                apiService.verifyDegree(bytes)
                    .onSuccess {
                        currentVerification = it
                        loadVerificationHistory("emp-123") // In a real app, use actual employerId
                        onSuccess(it)
                    }
                    .onFailure {
                        errorMessage = "Verification failed: ${it.message}"
                    }
            }
        } ?: run {
            errorMessage = "Please upload a certificate image first"
        }
    }

    fun loadVerificationHistory(employerId: String) {
        launchWithLoading {
            apiService.getVerificationHistory(employerId)
                .onSuccess {
                    verificationHistory = it
                }
                .onFailure {
                    errorMessage = "Failed to load verification history: ${it.message}"
                }
        }
    }

    fun handleCertificateUpload(inputElement: web.html.HTMLInputElement) {
        val files = inputElement.files
        if (files != null && files.length > 0) {
            val file = files[0]
            certificateFile = file

            // Read file as binary data
            val reader = web.file.FileReader()
            reader.onload = { event ->
                val arrayBuffer = event.target?.result as js.buffer.ArrayBuffer
                certificateBytes = arrayBuffer.toByteArray()
            }
            reader.readAsArrayBuffer(file)
        }
    }

    private fun ArrayBuffer.toByteArray(): ByteArray {
        val bytes = ByteArray(byteLength)
        val int8Array = Int8Array(this)
        for (i in bytes.indices) {
            bytes[i] = int8Array[i]
        }
        return bytes
    }
}

class AdminViewModel : ViewModel() {
    var systemStats: SystemStats? by Delegates.observable(null) { _, _, _ -> }
    var universities: List<University> by Delegates.observable(emptyList()) { _, _, _ -> }

    // New university form
    var universityName: String by Delegates.observable("") { _, _, _ -> }
    var universityEmail: String by Delegates.observable("") { _, _, _ -> }
    var stakeAmount: String by Delegates.observable("") { _, _, _ -> }

    fun loadSystemStats() {
        launchWithLoading {
            apiService.getSystemStats()
                .onSuccess {
                    systemStats = it
                }
                .onFailure {
                    errorMessage = "Failed to load system stats: ${it.message}"
                }
        }
    }

    fun loadUniversities() {
        launchWithLoading {
            apiService.getUniversities()
                .onSuccess {
                    universities = it
                }
                .onFailure {
                    errorMessage = "Failed to load universities: ${it.message}"
                }
        }
    }

    fun addUniversity(onSuccess: () -> Unit) {
        if (universityName.isBlank() || universityEmail.isBlank() || stakeAmount.isBlank()) {
            errorMessage = "All fields are required"
            return
        }

        val stake = stakeAmount.toDoubleOrNull()
        if (stake == null || stake <= 0) {
            errorMessage = "Stake amount must be a positive number"
            return
        }

        launchWithLoading {
            val university = University(
                id = "",  // Will be assigned by the service
                name = universityName,
                email = universityEmail,
                address = "",  // Not collected in this form
                stakeAmount = stake,
                status = UniversityStatus.PENDING,
                joinDate = ""  // Will be assigned by the service
            )

            apiService.addUniversity(university)
                .onSuccess {
                    clearForm()
                    loadUniversities()
                    onSuccess()
                }
                .onFailure {
                    errorMessage = "Failed to add university: ${it.message}"
                }
        }
    }

    fun blacklistUniversity(universityId: String, onSuccess: () -> Unit) {
        launchWithLoading {
            apiService.blacklistUniversity(universityId)
                .onSuccess {
                    loadUniversities()
                    onSuccess()
                }
                .onFailure {
                    errorMessage = "Failed to blacklist university: ${it.message}"
                }
        }
    }

    private fun clearForm() {
        universityName = ""
        universityEmail = ""
        stakeAmount = ""
    }
}