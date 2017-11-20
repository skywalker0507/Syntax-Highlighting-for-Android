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

package com.skywalker.languages.common;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Scanner implements Closeable {

    // Internal buffer used to hold input
    private CharBuffer buf;

    // Size of internal character buffer
    private static final int BUFFER_SIZE = 1024; // change to 1024;

    // The index into the buffer currently held by the Scanner
    private int position;

    // Internal matcher used for finding delimiters
    private Matcher matcher;

    // The input source
    private Readable source;

    // Boolean is true if source is done
    private boolean sourceClosed = false;

    // Boolean indicating more input is required
    private boolean needInput = false;

    // A store of a position that the scanner may fall back to
    private int savedScannerPosition = -1;

    // Boolean indicating if a match result is available
    private boolean matchValid = false;

    // Boolean indicating if this scanner has been closed
    private boolean closed = false;

    private int base=-1024;

    // A holder of the last IOException encountered
    private IOException lastException;


    private Scanner(Readable source) {
        assert source != null : "source should not be null";
        this.source = source;
        buf = CharBuffer.allocate(BUFFER_SIZE);
        buf.limit(0);
    }
    /**
     * Constructs a new <code>Scanner</code> that produces values scanned
     * from the specified string.
     *
     * @param  source A string to scan
     */
    public Scanner(String source) {
        this(new StringReader(source));
    }


    // Tries to read more input. May block.
    private void readInput() {
        if (buf.limit() == buf.capacity())
            makeSpace();

        // Prepare to receive data
        int p = buf.position();
        buf.position(buf.limit());
        int t=buf.position();
        buf.limit(buf.capacity());

        int n = 0;
        try {
            n = source.read(buf);
            base+=BUFFER_SIZE-t;
        } catch (IOException ioe) {
            lastException = ioe;
            n = -1;
        }

        if (n == -1) {
            sourceClosed = true;
            needInput = false;
        }

        if (n > 0)
            needInput = false;

        // Restore current position and limit for reading
        buf.limit(buf.position());
        buf.position(p);
    }

    // After this method is called there will either be an exception
    // or else there will be space in the buffer
    private boolean makeSpace() {
        int offset = savedScannerPosition == -1 ?
                position : savedScannerPosition;
        buf.position(offset);
        // Gain space by compacting buffer
        if (offset > 0) {
            buf.compact();
            translateSavedIndexes(offset);
            position -= offset;
            buf.flip();
            return true;
        }
        // Gain space by growing buffer
        int newSize = buf.capacity() * 2;
        CharBuffer newBuf = CharBuffer.allocate(newSize);
        newBuf.put(buf);
        newBuf.flip();
        translateSavedIndexes(offset);
        position -= offset;
        buf = newBuf;
        matcher.reset(buf);
        return true;
    }

    // When a buffer compaction/reallocation occurs the saved indexes must
    // be modified appropriately
    private void translateSavedIndexes(int offset) {
        if (savedScannerPosition != -1)
            savedScannerPosition -= offset;
    }

    // Finds the specified pattern in the buffer up to horizon.
    // Returns a match for the specified input pattern.
    private String findPatternInBuffer(Pattern pattern, int horizon) {
        matchValid = false;
        matcher=pattern.matcher(buf);
        int bufferLimit = buf.limit();
        int horizonLimit = -1;
        int searchLimit = bufferLimit;
        if (horizon > 0) {
            horizonLimit = position + horizon;
            if (horizonLimit < bufferLimit)
                searchLimit = horizonLimit;
        }
        matcher.region(position, searchLimit);
        if (matcher.find()) {
            if (matcher.hitEnd() && (!sourceClosed)) {
                // The match may be longer if didn't hit horizon or real end
                if (searchLimit != horizonLimit) {
                    // Hit an artificial end; try to extend the match
                    needInput = true;
                    return null;
                }
                // The match could go away depending on what is next
                if ((searchLimit == horizonLimit) && matcher.requireEnd()) {
                    // Rare case: we hit the end of input and it happens
                    // that it is at the horizon and the end of input is
                    // required for the match.
                    needInput = true;
                    return null;
                }
            }
            // Did not hit end, or hit real end, or hit horizon
            position = matcher.end();
            return matcher.group();
        }

        if (sourceClosed)
            return null;

        // If there is no specified horizon, or if we have not searched
        // to the specified horizon yet, get more input
        if ((horizon == 0) || (searchLimit != horizonLimit))
            needInput = true;
        return null;
    }


    // Throws if the scanner is closed
    private void ensureOpen() {
        if (closed)
            throw new IllegalStateException("Scanner closed");
    }

    // Public methods

    /**
     * Closes this scanner.
     *
     * <p> If this scanner has not yet been closed then if its underlying
     * {@linkplain java.lang.Readable readable} also implements the {@link
     * java.io.Closeable} interface then the readable's <tt>close</tt> method
     * will be invoked.  If this scanner is already closed then invoking this
     * method will have no effect.
     *
     * <p>Attempting to perform search operations after a scanner has
     * been closed will result in an {@link IllegalStateException}.
     *
     */
    public void close() {
        if (closed)
            return;
        if (source instanceof Closeable) {
            try {
                ((Closeable)source).close();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        sourceClosed = true;
        source = null;
        closed = true;
    }

    /**
     * Returns the <code>IOException</code> last thrown by this
     * <code>Scanner</code>'s underlying <code>Readable</code>. This method
     * returns <code>null</code> if no such exception exists.
     *
     * @return the last exception thrown by this scanner's readable
     */
    public IOException ioException() {
        return lastException;
    }


    /**
     * Returns the match result of the last scanning operation performed
     * by this scanner. This method throws <code>IllegalStateException</code>
     * if no match has been performed, or if the last match was
     * not successful.
     *
     * <p>The various <code>next</code>methods of <code>Scanner</code>
     * make a match result available if they complete without throwing an
     * exception.
     *
     * @return a match result for the last match operation
     * @throws IllegalStateException  If no match result is available
     */
    public MatchResult match() {
        if (!matchValid)
            throw new IllegalStateException("No match result available");
        return matcher.toMatchResult();
    }

    public int getBase() {
        return base;
    }



    /**
     * Attempts to find the next occurrence of the specified pattern.
     *
     * <p>This method searches through the input up to the specified
     * search horizon, ignoring delimiters. If the pattern is found the
     * scanner advances past the input that matched and returns the string
     * that matched the pattern. If no such pattern is detected then the
     * null is returned and the scanner's position remains unchanged. This
     * method may block waiting for input that matches the pattern.
     *
     * <p>A scanner will never search more than <code>horizon</code> code
     * points beyond its current position. Note that a match may be clipped
     * by the horizon; that is, an arbitrary match result may have been
     * different if the horizon had been larger. The scanner treats the
     * horizon as a transparent, non-anchoring bound (see {@link
     * Matcher#useTransparentBounds} and {@link Matcher#useAnchoringBounds}).
     *
     * <p>If horizon is <code>0</code>, then the horizon is ignored and
     * this method continues to search through the input looking for the
     * specified pattern without bound. In this case it may buffer all of
     * the input searching for the pattern.
     *
     * <p>If horizon is negative, then an IllegalArgumentException is
     * thrown.
     *
     * @param pattern the pattern to scan for
     * @param horizon the search horizon
     * @return the text that matched the specified pattern
     * @throws IllegalStateException if this scanner is closed
     * @throws IllegalArgumentException if horizon is negative
     */
    public String findWithinHorizon(Pattern pattern, int horizon) {
        ensureOpen();
        if (pattern == null)
            throw new NullPointerException();
        if (horizon < 0)
            throw new IllegalArgumentException("horizon < 0");

        // Search for the pattern
        while (true) {
            String token = findPatternInBuffer(pattern, horizon);
            if (token != null) {
                matchValid = true;
                return token;
            }
            if (needInput)
                readInput();
            else
                break; // up to end of input
        }
        return null;
    }


}
