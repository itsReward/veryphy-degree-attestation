import kotlinx.browser.document
import kotlinx.browser.window
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import web.dom.Element

fun main() {
    window.onload = {
        val container = document.getElementById("root") as? Element ?: error("Couldn't find container!")
        createRoot(container).render(App.create())
    }
}

val App = FC<Props> {
    div {
        h1 {
            +"Degree Attestation System"
        }
        +"Welcome to the Blockchain-based Degree Attestation System"
    }
}