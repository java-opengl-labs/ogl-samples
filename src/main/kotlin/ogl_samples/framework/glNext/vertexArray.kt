package ogl_samples.framework.glNext

import glm.L
import ogl_samples.framework.VertexAttribute
import ogl_samples.framework.VertexLayout
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import java.nio.IntBuffer
import kotlin.reflect.KClass

/**
 * Created by elect on 18/04/17.
 */


fun glBindVertexArray(vertexArray: IntBuffer) = glBindVertexArray(vertexArray[0])

fun glBindVertexArray() = glBindVertexArray(0)


inline fun initVertexArray(vertexArray: IntBuffer, block: VertexArray.() -> Unit) {
    glGenVertexArrays(vertexArray)
    glBindVertexArray(vertexArray[0])
    VertexArray.block()
    glBindVertexArray(0)
}

inline fun withVertexArray(vertexArray: IntBuffer, block: VertexArray.() -> Unit) = withVertexArray(vertexArray[0], block)
inline fun withVertexArray(vertexArray: Int, block: VertexArray.() -> Unit) {
    glBindVertexArray(vertexArray)
    VertexArray.block()
    glBindVertexArray(0)
}

object VertexArray {

    fun array(array: Int, format: VertexLayout) {
        glBindBuffer(GL15.GL_ARRAY_BUFFER, array)
        for (attr in format.attribute) {
            glEnableVertexAttribArray(attr.index)
            glVertexAttribPointer(attr.index, attr.size, attr.type, attr.normalized, attr.interleavedStride, attr.pointer)
        }
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    fun array(array: Int, format: VertexLayout, vararg offset: Int) {
        glBindBuffer(GL15.GL_ARRAY_BUFFER, array)
        for (i in format.attribute.indices) {
            val attr = format.attribute[i]
            glEnableVertexAttribArray(attr.index)
            glVertexAttribPointer(attr.index, attr.size, attr.type, attr.normalized, 0, offset[i].L)
        }
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    fun element(element: Int) = glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, element)
    fun element(element: IntBuffer) = glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, element[0])
}


inline fun withVertexLayout(buffer: IntBuffer, format: VertexLayout, block: () -> Unit) {
    glBindBuffer(GL_ARRAY_BUFFER, buffer[0])
    for (attr in format.attribute) {
        glEnableVertexAttribArray(attr.index)
        glVertexAttribPointer(attr.index, attr.size, attr.type, attr.normalized, attr.interleavedStride, attr.pointer)
    }
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    block()
    for (attr in format.attribute)
        glDisableVertexAttribArray(attr.index)
}


/** For un-interleaved, that is not-interleaved */
inline fun withVertexLayout(buffer: IntBuffer, format: VertexLayout, vararg offset: Int, block: () -> Unit) {
    glBindBuffer(GL_ARRAY_BUFFER, buffer[0])
    for (i in format.attribute.indices) {
        val attr = format.attribute[i]
        glEnableVertexAttribArray(attr.index)
        glVertexAttribPointer(attr.index, attr.size, attr.type, attr.normalized, 0, offset[i].L)
    }
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    block()
    for (attr in format.attribute)
        glDisableVertexAttribArray(attr.index)
}


fun glEnableVertexAttribArray(layout: VertexLayout) = glEnableVertexAttribArray(layout[0].index)
fun glEnableVertexAttribArray(attribute: VertexAttribute) = glEnableVertexAttribArray(attribute.index)

fun glDisableVertexAttribArray(layout: VertexLayout) = glDisableVertexAttribArray(layout[0].index)
fun glDisableVertexAttribArray(attribute: VertexAttribute) = glDisableVertexAttribArray(attribute.index)


fun glVertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int) =
        glVertexAttribPointer(index, size, type, normalized, stride, 0)

fun glVertexAttribPointer(layout: VertexLayout) = glVertexAttribPointer(layout[0])
fun glVertexAttribPointer(attribute: VertexAttribute) =
        glVertexAttribPointer(
                attribute.index,
                attribute.size,
                attribute.type,
                attribute.normalized,
                attribute.interleavedStride,
                attribute.pointer)

fun glVertexAttribPointer(layout: VertexLayout, offset: Int) = glVertexAttribPointer(layout[0], offset)
fun glVertexAttribPointer(attribute: VertexAttribute, offset: Int) =
        glVertexAttribPointer(
                attribute.index,
                attribute.size,
                attribute.type,
                attribute.normalized,
                0, // tightly packed
                offset.L)