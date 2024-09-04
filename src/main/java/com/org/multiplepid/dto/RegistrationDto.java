package com.org.multiplepid.dto;

import java.io.Serializable;

/**
 * Dto used for registration.
 *
 * @param id - player id. It's used to avoid dynamic generation on message system side
 * @param name - player name
 * @param host - player host (to send messages back to registered player)
 * @param port - player port (to send messages back to registered player)
 */
public record RegistrationDto(String id, String name, String host, int port) implements Serializable {

}
