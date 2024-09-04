package com.org.multiplepid.dto;

import java.io.Serializable;

/**
 * Command to shut down players, once system is about to shut down
 */
public record ShutdownCommand() implements Serializable {}
