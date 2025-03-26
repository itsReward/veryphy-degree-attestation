package com.veryphy

import kotlinx.browser.document

/**
 * Alternative way to load CSS since the @JsModule approach might be failing
 */
object StyleLoader {
    fun loadStyles() {
        try {
            // Create a style element
            val styleElement = document.createElement("style")
            styleElement.textContent = """
                /* Basic styles */
                body, html {
                    margin: 0;
                    padding: 0;
                    height: 100%;
                    font-family: Arial, Helvetica, sans-serif;
                }
                
                /* Login screen styles */
                .login-container {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    height: 100vh;
                    background-color: #f5f5f5;
                }
                
                .login-card {
                    padding: 32px;
                    background-color: white;
                    border-radius: 8px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                    width: 400px;
                }
                
                .login-form {
                    display: flex;
                    flex-direction: column;
                    gap: 16px;
                }
                
                .form-group {
                    display: flex;
                    flex-direction: column;
                    gap: 8px;
                }
                
                .form-control {
                    padding: 12px;
                    font-size: 16px;
                    border-radius: 4px;
                    border: 1px solid #ddd;
                }
                
                .btn {
                    padding: 12px 24px;
                    font-size: 16px;
                    border: none;
                    border-radius: 4px;
                    background-color: #0078d4;
                    color: white;
                    cursor: pointer;
                    margin-right: 8px;
                }
                
                .btn:hover {
                    background-color: #0062a9;
                }
                
                .btn:disabled {
                    background-color: #cccccc;
                    cursor: not-allowed;
                }
                
                .role-selection {
                    display: flex;
                    gap: 8px;
                    margin-top: 16px;
                }
            """.trimIndent()

            // Append to document head
            document.head?.appendChild(styleElement)
            console.log("Styles loaded via JS")
        } catch (e: Exception) {
            console.error("Failed to load styles via JS:", e)
        }
    }
}