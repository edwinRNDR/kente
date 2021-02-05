import org.openrndr.KEY_BACKSPACE
import org.openrndr.KEY_ENTER
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.shape.Rectangle
import org.openrndr.text.writer

fun main() {
   application {

       configure {
           width = 760
           height = 720
           title = "KENTE"
       }



        program {
            var text = ""
            keyboard.keyDown.listen {
                if (it.key == KEY_BACKSPACE) {
                    text = text.dropLast(1)
                }
                if (it.key == KEY_ENTER) {
                    text = text + "\n"
                }
            }

            keyboard.character.listen {
                text = text + it.character
            }

            extend(ScreenRecorder()) {
                frameRate = 60

            }
            extend {
                drawer.clear(ColorRGBa.GRAY.shade(0.5))
                writer {
                    drawer.fill = ColorRGBa.WHITE
                    box = Rectangle(20.0, 26.0, 200.0, 600.0)
                    this.text(text)
                }
                if (text.length > 0) {
                    val message = Huffman.compress(text)
                    val blackRectangles = mutableListOf<Rectangle>()
                    val whiteRectangles = mutableListOf<Rectangle>()
                    var x = 0
                    var y = 0
                    for (m in message.message) {
                        val m2 = m % 2
                        if (m2 == 0) {
                            blackRectangles.add(Rectangle(x*10.0,y*10.0, 10.0, 10.0))
                        } else {
                            whiteRectangles.add(Rectangle(x*10.0,y*10.0, 10.0, 10.0))
                        }

                        x++
                        if (x> 20) {
                            x = 0
                            y++
                        }
                    }
                    drawer.isolated {
                        drawer.translate(260.0, 20.0)
                        drawer.stroke = null
                        drawer.fill = ColorRGBa.BLACK
                        drawer.rectangles(blackRectangles)
                        drawer.fill = ColorRGBa.WHITE
                        drawer.rectangles(whiteRectangles)
                    }
                }
                if (text.length > 0) {
                    val message = text.flatMap { c ->
                        (0 until 8).map {
                            val mask = c.toInt() and (1 shl it)
                            if (mask == 0) 0 else 1
                        }

                    }
                    val blackRectangles = mutableListOf<Rectangle>()
                    val whiteRectangles = mutableListOf<Rectangle>()
                    var x = 0
                    var y = 0
                    for (m in message) {
                        if (m == 0) {
                            blackRectangles.add(Rectangle(x*10.0,y*10.0, 10.0, 10.0))
                        } else {
                            whiteRectangles.add(Rectangle(x*10.0,y*10.0, 10.0, 10.0))
                        }

                        x++
                        if (x> 20) {
                            x = 0
                            y++
                        }
                    }
                    drawer.isolated {
                        drawer.stroke = null

                        drawer.translate(510.0, 20.0)
                        drawer.fill = ColorRGBa.BLACK
                        drawer.rectangles(blackRectangles)
                        drawer.fill = ColorRGBa.WHITE
                        drawer.rectangles(whiteRectangles)
                    }
                }
            }
        }
    }
}