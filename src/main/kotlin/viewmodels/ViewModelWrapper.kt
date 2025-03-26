package viewmodels

import com.veryphy.*

/**
 * A wrapper for ViewModels to provide mock data loading functionality
 * This is useful for demo/testing purposes when no real backend is available
 */
class ViewModelWrapper {
    companion object {
        // Mock user data for login
        fun getMockUser(role: UserRole): User {
            return when (role) {
                UserRole.UNIVERSITY -> User(
                    id = "uni-user-123",
                    name = "University of Technology",
                    email = "admin@university.edu",
                    role = role
                )
                UserRole.EMPLOYER -> User(
                    id = "emp-user-456",
                    name = "Tech Innovations Inc.",
                    email = "hr@techinnovations.com",
                    role = role
                )
                UserRole.ADMIN -> User(
                    id = "adm-user-789",
                    name = "System Administrator",
                    email = "admin@veryphy.com",
                    role = role
                )
            }
        }

        // Mock degrees data
        fun getMockDegrees(): List<Degree> {
            return listOf(
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

        // Mock verification requests
        fun getMockVerifications(): List<VerificationRequest> {
            return listOf(
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

        // Mock universities
        fun getMockUniversities(): List<University> {
            return listOf(
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

        // Mock system stats
        fun getMockSystemStats(): SystemStats {
            return SystemStats(
                registeredUniversities = 42,
                totalDegrees = 12567,
                verificationCount = 128,
                successRate = 99.8
            )
        }
    }
}