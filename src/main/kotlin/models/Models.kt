package com.veryphy.models

enum class UserRole {
    UNIVERSITY,
    EMPLOYER,
    ADMIN
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)

enum class UniversityStatus {
    ACTIVE,
    PENDING,
    BLACKLISTED
}

data class University(
    val id: String,
    val name: String,
    val email: String,
    val address: String,
    val stakeAmount: Double,
    val status: UniversityStatus,
    val joinDate: String
)

enum class DegreeStatus {
    REGISTERED,
    PROCESSING,
    VERIFIED
}

data class Degree(
    val id: String,
    val studentId: String,
    val studentName: String,
    val degreeName: String,
    val universityId: String,
    val universityName: String,
    val issueDate: String,
    val degreeHash: String,
    val status: DegreeStatus
)

enum class VerificationResult {
    AUTHENTIC,
    FAILED,
    PENDING
}

data class VerificationRequest(
    val id: String,
    val employerId: String,
    val degreeId: String,
    val studentName: String,
    val universityName: String,
    val requestDate: String,
    val result: VerificationResult
)

data class SystemStats(
    val registeredUniversities: Int,
    val totalDegrees: Int,
    val verificationCount: Int,
    val successRate: Double
)