package com.veryphy

import kotlinx.coroutines.delay

// Mock API service for frontend development
class ApiService {
    // Authentication
    suspend fun login(username: String, password: String, role: UserRole): Result<User> {
        // Simulate network delay
        delay(1000)

        // Mock authentication logic
        return if (username.isNotBlank() && password.isNotBlank()) {
            Result.success(
                User(
                    id = "user-123",
                    name = when (role) {
                        UserRole.UNIVERSITY -> "University of Technology"
                        UserRole.EMPLOYER -> "Tech Innovations Inc."
                        UserRole.ADMIN -> "System Administrator"
                    },
                    email = "user@example.com",
                    role = role
                )
            )
        } else {
            Result.failure(Exception("Authentication failed"))
        }
    }

    // University Services
    suspend fun registerDegree(degree: Degree): Result<Degree> {
        delay(1000)

        // Generate a mock hash code for the degree
        val hashCode = "0x" + degree.studentId.hashCode().toString(16) +
                degree.degreeName.hashCode().toString(16)

        return Result.success(
            degree.copy(
                id = "deg-" + (1000..9999).random(),
                hashCode = hashCode,
                status = DegreeStatus.REGISTERED
            )
        )
    }

    suspend fun getUniversityDegrees(universityId: String): Result<List<Degree>> {
        delay(500)

        // Mock degree data
        val degrees = listOf(
            Degree(
                id = "deg-1234",
                studentId = "S123456",
                studentName = "John Doe",
                degreeName = "Bachelor of Computer Science",
                universityId = universityId,
                universityName = "University of Technology",
                issueDate = "2024-12-15",
                hashCode = "0x1a2b3c4d",
                status = DegreeStatus.VERIFIED
            ),
            Degree(
                id = "deg-5678",
                studentId = "S789012",
                studentName = "Jane Smith",
                degreeName = "Master of Business Administration",
                universityId = universityId,
                universityName = "University of Technology",
                issueDate = "2024-12-14",
                hashCode = "0x5e6f7g8h",
                status = DegreeStatus.PROCESSING
            )
        )

        return Result.success(degrees)
    }

    // Employer Services
    suspend fun verifyDegree(certificateImage: ByteArray): Result<VerificationRequest> {
        delay(2000) // Longer delay to simulate AI processing

        // Mock verification result
        return Result.success(
            VerificationRequest(
                id = "ver-" + (1000..9999).random(),
                employerId = "emp-123",
                degreeId = "deg-1234",
                studentName = "Robert Johnson",
                universityName = "University of Technology",
                requestDate = "2024-12-15",
                result = VerificationResult.AUTHENTIC
            )
        )
    }

    suspend fun getVerificationHistory(employerId: String): Result<List<VerificationRequest>> {
        delay(500)

        // Mock verification history
        val history = listOf(
            VerificationRequest(
                id = "ver-1234",
                employerId = employerId,
                degreeId = "deg-1234",
                studentName = "Robert Johnson",
                universityName = "University of Technology",
                requestDate = "2024-12-15",
                result = VerificationResult.AUTHENTIC
            ),
            VerificationRequest(
                id = "ver-5678",
                employerId = employerId,
                degreeId = "deg-5678",
                studentName = "Maria Garcia",
                universityName = "State University",
                requestDate = "2024-12-10",
                result = VerificationResult.FAILED
            )
        )

        return Result.success(history)
    }

    // Admin Services
    suspend fun getSystemStats(): Result<SystemStats> {
        delay(500)

        return Result.success(
            SystemStats(
                registeredUniversities = 42,
                totalDegrees = 12567,
                verificationCount = 128,
                successRate = 99.8
            )
        )
    }

    suspend fun getUniversities(): Result<List<University>> {
        delay(500)

        val universities = listOf(
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

        return Result.success(universities)
    }

    suspend fun addUniversity(university: University): Result<University> {
        delay(1000)

        return Result.success(
            university.copy(
                id = "uni-" + (100..999).random(),
                status = UniversityStatus.PENDING,
                joinDate = "2024-12-15"
            )
        )
    }

    suspend fun blacklistUniversity(universityId: String): Result<Boolean> {
        delay(1000)

        // Simulate blacklisting
        return Result.success(true)
    }
}