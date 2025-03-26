// Place this file in src/main/resources/testScript.js

// This will run when the page loads
window.onload = function() {
    console.log("Test script is running!");

    // Try to access the root element
    var rootElement = document.getElementById('root');

    if (rootElement) {
        console.log("Root element found");

        // Replace the loading message with a simple text
        rootElement.innerHTML = "JavaScript is working! If you see this message, your JS is executing properly.";

        // Change background color to visually confirm JS is working
        document.body.style.backgroundColor = "#f0f8ff";
    } else {
        console.error("Root element not found!");
    }
};