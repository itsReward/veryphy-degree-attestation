/*
package com.veryphy

import react.*
import react.dom.client.createRoot
import kotlinx.browser.document
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.button
import web.dom.Element

*/
/**
 * Minimal React App - stripped down to the absolute basics
 *//*

val MinimalApp = FC<Props> {
    val (count, setCount) = useState(0)

    div {
        h1 {
            +"Hello from React!"
        }
        p {
            +"Count: $count"
        }
        button {
            onClick = { setCount(count + 1) }
            +"Increment"
        }
    }
}

fun main() {
    // We'll use a more basic approach to track execution
    println("Starting application...")
    document.addEventListener("DOMContentLoaded", {
        println("DOM content loaded")

        try {
            // Add a visible message to the debug area
            document.getElementById("debug")?.innerHTML = "JS main function running"

            val container = document.getElementById("root") as Element?
            if (container != null) {
                // Update debug element
                document.getElementById("debug")?.innerHTML += "<br>Root element found"

                try {
                    // Create a React root and render the app
                    createRoot(container).render(MinimalApp.create())
                    document.getElementById("debug")?.innerHTML += "<br>React rendering complete"
                } catch (e: Exception) {
                    // Show any errors in the debug area
                    val errorMsg = "React error: ${e.message}"
                    println(errorMsg)
                    document.getElementById("debug")?.innerHTML += "<br><span style='color:red'>$errorMsg</span>"
                }
            } else {
                val errorMsg = "Root element not found!"
                println(errorMsg)
                document.getElementById("debug")?.innerHTML += "<br><span style='color:red'>$errorMsg</span>"
            }
        } catch (e: Exception) {
            // Catch any other errors
            val errorMsg = "Error: ${e.message}"
            println(errorMsg)
            document.getElementById("debug")?.innerHTML += "<br><span style='color:red'>$errorMsg</span>"
        }
    })
}*/
