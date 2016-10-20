/*
 * Copyright (C) 2014 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.telegram.gamebot

import java.awt.*
import java.awt.BorderLayout.*
import java.awt.event.InputEvent
import java.lang.Math.*
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel

/**
 *
 */
object LumberJack {
    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame("LumberJack Bot")
        frame.preferredSize = Dimension(600, 300)
        frame.layout = BorderLayout()
        frame.contentPane.add(JLabel("Move the telegram window so that the LubmerJack start button is directly over the arrow."), CENTER)
        frame.contentPane.add(JLabel(ImageIcon(javaClass.getResource("/arrow.png"))), NORTH)
        val start = JButton("Start")
        start.addActionListener { evt ->
            val x = frame.location.x + frame.width / 2
            val y = frame.location.y - 100
            Player(x, y).play()
        }
        frame.contentPane.add(start, SOUTH)
        frame.pack()
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        frame.location = Point((screenSize.width - frame.preferredSize.width) / 2, screenSize.height - frame.preferredSize.height)
        frame.isVisible = true
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

}

class Player(val x: Int, val y: Int) {
    val rob = Robot()
    var left = true

    fun play() {
        rob.delay(1000)
        center(200)
        if (!isWood(x, y)) {
            return println("No start button found")
        }
        center(200)
        hithit(150)
        do {
            if (isWaitBranch(0)) left = !left
            val b1 = isBranch(1)
            val b2 = isBranch(2)
            val b3 = isBranch(3)
            if (!b1) {
                hithit(20)
                if (!b2) {
                    hithit(20)
                    if (!b3) {
                        hithit(20)
                    }
                }
            }
            hithit(30)
        } while (!isWood(x, y))
    }


    fun isWaitBranch(level: Int): Boolean {
        var count = 0
        while (count < 30 && !isWood(x(), levelY(level)) && !isWood(x(false), levelY(level))) {
            rob.delay(2)
            count++
        }
        return isBranch(level)
    }

    fun isBranch(level: Int) = isWood(x(), levelY(level))

    fun levelY(level: Int) = y - 310 - 100 * level

    fun isWood(x: Int, y: Int): Boolean {
        return abs(rob.getPixelColor(x, y).hue() - 32) < 5
    }

    fun Color.hue(): Double {
        val r = red / 255.0
        val g = green / 255.0
        val b = blue / 255.0
        val max = max(max(r, g), b)
        val min = min(min(r, g), b)
        val d = max - min
        return when (max) {
            min -> 0.0
            r -> 60 * ((g - b) / d % 6)
            g -> 60 * ((b - r) / d + 2)
            b -> 60 * ((r - g) / d + 4)
            else -> throw AssertionError()
        }
    }

    fun x(mySide: Boolean = true) = x + if (left == mySide) -50 else 50

    fun hit(delay: Int) {
        rob.click(delay, x(), y)
    }

    fun hithit(delay: Int) {
        hit(delay)
        hit(delay)
    }

    fun center(delay: Int) {
        rob.click(delay, x, y)
    }

    fun Robot.click(delay: Int, x: Int, y: Int) {
        mouseMove(x, y)
        mousePress(InputEvent.BUTTON1_MASK)
        mouseRelease(InputEvent.BUTTON1_MASK)
        delay(delay)
    }

}