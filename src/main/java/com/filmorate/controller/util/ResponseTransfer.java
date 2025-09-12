package com.filmorate.controller.util;

/**
 * Standard wrapper for returning simple text responses from the REST API.
 * @param text message content
 */
public record ResponseTransfer(String text) {}
