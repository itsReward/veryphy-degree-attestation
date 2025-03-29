package com.veryphy.services

import com.veryphy.models.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.json

/**
 * API service for communicating with the backend
 */
class ApiService {
    private val baseUrl = "http://localhost:8086/api" // Change this to match your Spring Boot server URL
    private var authToken: String? = null

    // Authentication helper functions
    fun setAuthToken(token: String) {
        authToken = token
    }

    fun clearAuthToken() {
        authToken = null
    }

    fun isAuthenticated(): Boolean {
        return !authToken.isNullOrEmpty()
    }

    // Generic HTTP request functions
    private suspend fun httpGet(endpoint: String): Response {
        val headers = Headers()
        headers.append("Content-Type", "application/json")
        if (authToken != null) {
            headers.append("Authorization", "Bearer $authToken")
        }

        val init = RequestInit(
            method = "GET",
            headers = headers
        )

        return window.fetch("$baseUrl$endpoint", init).await()
    }

    private suspend fun httpPost(endpoint: String, body: Any): Response {
        val headers = Headers()
        headers.append("Content-Type", "application/json")
        if (authToken != null) {
            headers.append("Authorization", "Bearer $authToken")
        }

        val init = RequestInit(
            method = "POST",
            headers = headers,
            body = JSON.stringify(body)
        )

        return window.fetch("$baseUrl$endpoint", init).await()
    }

    private suspend fun httpPut(endpoint: String, body: Any): Response {
        val headers = Headers()
        headers.append("Content-Type", "application/json")
        if (authToken != null) {
            headers.append("Authorization", "Bearer $authToken")
        }

        val init = RequestInit(
            method = "PUT",
            headers = headers,
            body = JSON.stringify(body)
        )

        return window.fetch("$baseUrl$endpoint", init).await()
    }

    // Authentication
    suspend fun login(username: String, password: String, role: UserRole): Result<LoginResponse> {
        try {
            val loginRequest = json(
                "username" to username,
                "password" to password,
                "role" to role.toString()
            )

            val response = httpPost("/auth/login", loginRequest)

            if (response.ok) {
                val loginResponse = response.json().await().unsafeCast<LoginResponse>()
                setAuthToken(loginResponse.token)

                val user = User(
                    id = loginResponse.user.id,
                    name = if (role == UserRole.UNIVERSITY) loginResponse.user.entityId ?: username else loginResponse.user.username,
                    email = loginResponse.user.email,
                    role = loginResponse.user.role.unsafeCast<UserRole>()
                )

                return Result.success(loginResponse)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Authentication failed: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Login error", e)
            return Result.failure(e)
        }
    }

    // University Services
    suspend fun registerDegree(degreeRequest: DegreeRegistrationRequest): Result<DegreeResponse> {
        try {
            val response = httpPost("/degrees", degreeRequest)

            if (response.ok) {
                val degreeResponse = response.json().await().unsafeCast<DegreeResponse>()
                return Result.success(degreeResponse)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to register degree: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Register degree error", e)
            return Result.failure(e)
        }
    }

    suspend fun getUniversityDegrees(universityId: String, page: Int = 0, size: Int = 10): Result<List<Degree>> {
        try {
            val response = httpGet("/degrees/university/$universityId?page=$page&size=$size")

            if (response.ok) {
                val degreesResponse = response.json().await().unsafeCast<Array<DegreeResponse>>()

                val degrees = degreesResponse.map { resp ->
                    Degree(
                        id = resp.id,
                        studentId = resp.studentId,
                        studentName = resp.studentName,
                        degreeName = resp.degreeName,
                        universityId = resp.universityId,
                        universityName = resp.universityName,
                        issueDate = resp.issueDate,
                        degreeHash = resp.degreeHash,
                        status = resp.status.unsafeCast<DegreeStatus>()
                    )
                }

                return Result.success(degrees.toList())
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to get university degrees: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Get university degrees error", e)
            return Result.failure(e)
        }
    }

    // Employer Services
    suspend fun verifyDegreeByHash(degreeHash: String): Result<VerificationResponse> {
        try {
            val response = httpPost("/verifications/hash?degreeHash=$degreeHash", "{}")

            if (response.ok) {
                val verificationResponse = response.json().await().unsafeCast<VerificationResponse>()
                return Result.success(verificationResponse)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to verify degree: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Verify degree error", e)
            return Result.failure(e)
        }
    }

    suspend fun getVerificationHistory(employerId: String, page: Int = 0, size: Int = 10): Result<List<VerificationRequest>> {
        try {
            val response = httpGet("/verifications/employer?page=$page&size=$size")

            if (response.ok) {
                val verificationsResponse = response.json().await().unsafeCast<Array<VerificationResponse>>()

                val verifications = verificationsResponse.map { resp ->
                    VerificationRequest(
                        id = resp.id,
                        employerId = employerId,
                        degreeId = resp.degreeHash,
                        studentName = resp.studentId,
                        universityName = resp.universityName,
                        requestDate = resp.issueDate,
                        result = if (resp.verified) VerificationResult.AUTHENTIC else VerificationResult.FAILED
                    )
                }

                return Result.success(verifications.toList())
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to get verification history: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Get verification history error", e)
            return Result.failure(e)
        }
    }

    // Admin Services
    suspend fun getSystemStats(): Result<SystemStats> {
        try {
            val response = httpGet("/system/stats")

            if (response.ok) {
                val statsResponse = response.json().await().unsafeCast<SystemStatsResponse>()

                val stats = SystemStats(
                    registeredUniversities = statsResponse.registeredUniversities,
                    totalDegrees = statsResponse.totalDegrees,
                    verificationCount = statsResponse.verificationCount,
                    successRate = statsResponse.successRate
                )

                return Result.success(stats)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to get system stats: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Get system stats error", e)
            return Result.failure(e)
        }
    }

    suspend fun getUniversities(page: Int = 0, size: Int = 10): Result<List<University>> {
        try {
            val response = httpGet("/universities?page=$page&size=$size")

            if (response.ok) {
                val universitiesResponse = response.json().await().unsafeCast<Array<UniversityResponse>>()

                val universities = universitiesResponse.map { resp ->
                    University(
                        id = resp.id,
                        name = resp.name,
                        email = resp.email,
                        address = resp.address ?: "",
                        stakeAmount = resp.stakeAmount.toDouble(),
                        status = resp.status.unsafeCast<UniversityStatus>(),
                        joinDate = resp.joinDate
                    )
                }

                return Result.success(universities.toList())
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to get universities: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Get universities error", e)
            return Result.failure(e)
        }
    }

    suspend fun registerUniversity(universityRequest: UniversityRegistrationRequest): Result<UniversityResponse> {
        try {
            val response = httpPost("/universities", universityRequest)

            if (response.ok) {
                val universityResponse = response.json().await().unsafeCast<UniversityResponse>()
                return Result.success(universityResponse)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to register university: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Register university error", e)
            return Result.failure(e)
        }
    }

    suspend fun blacklistUniversity(universityId: String, reason: String): Result<UniversityResponse> {
        try {
            val response = httpPut("/universities/$universityId/blacklist?reason=$reason", "{}")

            if (response.ok) {
                val universityResponse = response.json().await().unsafeCast<UniversityResponse>()
                return Result.success(universityResponse)
            } else {
                val errorText = response.text().await()
                return Result.failure(Exception("Failed to blacklist university: $errorText"))
            }
        } catch (e: Exception) {
            console.error("Blacklist university error", e)
            return Result.failure(e)
        }
    }
}