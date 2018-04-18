/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.skywalker.syntaxhighlighter

import java.io.Closeable
import java.io.IOException
import java.io.StringReader
import java.nio.CharBuffer
import java.util.regex.MatchResult
import java.util.regex.Matcher
import java.util.regex.Pattern

class SyntaxScanner private constructor(// The input source
        private var source: Readable?) {

    // Internal buffer used to hold input
    private var buf: CharBuffer

    // The index into the buffer currently held by the SyntaxScanner
    private var position: Int = 0

    // Internal matcher used for finding delimiters
    private lateinit var matcher: Matcher

    // Boolean is true if source is done
    private var sourceClosed = false

    // Boolean indicating more input is required
    private var needInput = false

    // A store of a position that the scanner may fall back to
    private var savedScannerPosition = -1

    // Boolean indicating if a match result is available
    private var matchValid = false

    // Boolean indicating if this scanner has been closed
    private var closed = false

    private val BUFFER_SIZE = 1024

    var base = 0
        private set

    // A holder of the last IOException encountered
    private var lastException: IOException? = null


    init {
        buf = CharBuffer.allocate(BUFFER_SIZE)
        buf.limit(0)
    }

    /**
     * Constructs a new `SyntaxScanner` that produces values scanned
     * from the specified string.
     *
     * @param source A string to scan
     */
    constructor(source: String) : this(StringReader(source)) {}


    // Tries to read more input. May block.
    private fun readInput() {
        if (buf.limit() == buf.capacity())
            makeSpace()

        // Prepare to receive data
        val p = buf.position()
        buf.position(buf.limit())
        buf.limit(buf.capacity())

        var n = 0
        try {
            n = source!!.read(buf)
        } catch (ioe: IOException) {
            lastException = ioe
            n = -1
        }

        if (n == -1) {
            sourceClosed = true
            needInput = false
        }

        if (n > 0)
            needInput = false

        // Restore current position and limit for reading
        buf.limit(buf.position())
        buf.position(p)
    }

    // After this method is called there will either be an exception
    // or else there will be space in the buffer
    private fun makeSpace(): Boolean {
        val offset = if (savedScannerPosition == -1)
            position
        else
            savedScannerPosition
        buf.position(offset)

        base += offset

        // Gain space by compacting buffer
        if (offset > 0) {
            buf.compact()
            translateSavedIndexes(offset)
            position -= offset
            buf.flip()
            return true
        }
        // Gain space by growing buffer
        val newSize = buf.capacity() * 2
        val newBuf = CharBuffer.allocate(newSize)
        newBuf.put(buf)
        newBuf.flip()
        translateSavedIndexes(offset)
        position -= offset
        buf = newBuf
        matcher.reset(buf)

        return true
    }

    // When a buffer compaction/reallocation occurs the saved indexes must
    // be modified appropriately
    private fun translateSavedIndexes(offset: Int) {
        if (savedScannerPosition != -1)
            savedScannerPosition -= offset
    }

    // Finds the specified pattern in the buffer up to horizon.
    // Returns a match for the specified input pattern.
    private fun findPatternInBuffer(pattern: Pattern, horizon: Int): String? {
        matchValid = false
        matcher = pattern.matcher(buf)
        val bufferLimit = buf.limit()
        var horizonLimit = -1
        var searchLimit = bufferLimit
        if (horizon > 0) {
            horizonLimit = position + horizon
            if (horizonLimit < bufferLimit)
                searchLimit = horizonLimit
        }
        matcher.region(position, searchLimit)
        if (matcher.find()) {
            if (matcher.hitEnd() && !sourceClosed) {
                // The match may be longer if didn't hit horizon or real end
                if (searchLimit != horizonLimit) {
                    // Hit an artificial end; try to extend the match
                    needInput = true
                    return null
                }
                // The match could go away depending on what is next
                if (searchLimit == horizonLimit && matcher.requireEnd()) {
                    // Rare case: we hit the end of input and it happens
                    // that it is at the horizon and the end of input is
                    // required for the match.
                    needInput = true
                    return null
                }
            }
            // Did not hit end, or hit real end, or hit horizon
            position = matcher.end()
            return matcher.group()
        }

        if (sourceClosed)
            return null

        // If there is no specified horizon, or if we have not searched
        // to the specified horizon yet, get more input
        if (horizon == 0 || searchLimit != horizonLimit)
            needInput = true
        return null
    }


    // Throws if the scanner is closed
    private fun ensureOpen() {
        if (closed)
            throw IllegalStateException("SyntaxScanner closed")
    }

    // Public methods

    /**
     * Closes this scanner.
     *
     *
     *
     *  If this scanner has not yet been closed then if its underlying
     * [readable][Readable] also implements the [ ] interface then the readable's <tt>close</tt> method
     * will be invoked.  If this scanner is already closed then invoking this
     * method will have no effect.
     *
     *
     *
     * Attempting to perform search operations after a scanner has
     * been closed will result in an [IllegalStateException].
     */
    fun close() {
        if (closed)
            return
        if (source is Closeable) {
            try {
                (source as Closeable).close()
            } catch (ioe: IOException) {
                lastException = ioe
            }

        }
        sourceClosed = true
        source = null
        closed = true
    }

    /**
     * Returns the `IOException` last thrown by this
     * `SyntaxScanner`'s underlying `Readable`. This method
     * returns `null` if no such exception exists.
     *
     * @return the last exception thrown by this scanner's readable
     */
    fun ioException(): IOException? {
        return lastException
    }


    /**
     * Returns the match result of the last scanning operation performed
     * by this scanner. This method throws `IllegalStateException`
     * if no match has been performed, or if the last match was
     * not successful.
     *
     *
     *
     * The various `next`methods of `SyntaxScanner`
     * make a match result available if they complete without throwing an
     * exception.
     *
     * @return a match result for the last match operation
     * @throws IllegalStateException If no match result is available
     */
    fun match(): MatchResult {
        if (!matchValid)
            throw IllegalStateException("No match result available")
        return matcher.toMatchResult()
    }


    /**
     * Attempts to find the next occurrence of the specified pattern.
     *
     *
     *
     * This method searches through the input up to the specified
     * search horizon, ignoring delimiters. If the pattern is found the
     * scanner advances past the input that matched and returns the string
     * that matched the pattern. If no such pattern is detected then the
     * null is returned and the scanner's position remains unchanged. This
     * method may block waiting for input that matches the pattern.
     *
     *
     *
     * A scanner will never search more than `horizon` code
     * points beyond its current position. Note that a match may be clipped
     * by the horizon; that is, an arbitrary match result may have been
     * different if the horizon had been larger. The scanner treats the
     * horizon as a transparent, non-anchoring bound (see [ ][Matcher.useTransparentBounds] and [Matcher.useAnchoringBounds]).
     *
     *
     *
     * If horizon is `0`, then the horizon is ignored and
     * this method continues to search through the input looking for the
     * specified pattern without bound. In this case it may buffer all of
     * the input searching for the pattern.
     *
     *
     *
     * If horizon is negative, then an IllegalArgumentException is
     * thrown.
     *
     * @param pattern the pattern to scan for
     * @param horizon the search horizon
     * @return the text that matched the specified pattern
     * @throws IllegalStateException    if this scanner is closed
     * @throws IllegalArgumentException if horizon is negative
     */
    fun findWithinHorizon(pattern: Pattern?, horizon: Int): String? {
        ensureOpen()
        if (pattern == null)
            throw NullPointerException()
        if (horizon < 0)
            throw IllegalArgumentException("horizon < 0")

        // Search for the pattern
        while (true) {
            val token = findPatternInBuffer(pattern, horizon)
            if (token != null) {
                matchValid = true
                return token
            }
            if (needInput)
                readInput()
            else
                break // up to end of input
        }
        return null
    }



}
